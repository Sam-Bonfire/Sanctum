#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.json.JSONArray
import org.json.JSONObject

/**
 * Fetch the Tanakh (Hebrew Bible) from bible-api.com
 * Uses KJV translation for English text
 * Outputs: assets/jewish/scripture.json + assets/jewish/duas.json
 *
 * Tanakh Structure:
 * - Torah (5 books, 187 chapters)
 * - Nevi'im - Prophets (8 books, 245 chapters)
 * - Ketuvim - Writings (11 books, 497 chapters)
 * - Total: 24 books, 929 chapters
 *
 * Features:
 * - Rate limiting with 2s delay between requests
 * - Handles "Retry later" by waiting and retrying
 * - Saves progress incrementally to avoid losing data on timeout
 * - Can resume from where it left off
 */

data class Book(val name: String, val canonical: String, val chapters: Int, val offset: Int)

// All 24 books of the Tanakh (929 total chapters)
val BOOKS = listOf(
    // Torah (5 books, 187 chapters)
    Book("genesis",     "Genesis",     50, 0),
    Book("exodus",      "Exodus",      40, 50),
    Book("leviticus",   "Leviticus",   27, 90),
    Book("numbers",     "Numbers",     36, 117),
    Book("deuteronomy", "Deuteronomy", 34, 153),
    // Nevi'im - Prophets (8 books, 245 chapters)
    Book("joshua",      "Joshua",      24, 187),
    Book("judges",      "Judges",      21, 211),
    Book("samuel",      "Samuel",      31, 232),
    Book("kings",       "Kings",       22, 263),
    Book("isaiah",      "Isaiah",      66, 285),
    Book("jeremiah",    "Jeremiah",    52, 351),
    Book("ezekiel",     "Ezekiel",     48, 403),
    Book("hosea",       "Hosea",       14, 451),
    Book("joel",        "Joel",         3, 465),
    Book("amos",        "Amos",         9, 468),
    Book("obadiah",     "Obadiah",      1, 477),
    Book("jonah",       "Jonah",        4, 478),
    Book("micah",       "Micah",        7, 482),
    Book("nahum",       "Nahum",        3, 489),
    Book("habakkuk",    "Habakkuk",     3, 492),
    Book("zephaniah",   "Zephaniah",    3, 495),
    Book("haggai",      "Haggai",       2, 498),
    Book("zechariah",   "Zechariah",   14, 500),
    Book("malachi",     "Malachi",      4, 514),
    // Ketuvim - Writings (11 books, 497 chapters)
    Book("psalms",      "Psalms",     150, 518),
    Book("proverbs",    "Proverbs",    31, 668),
    Book("job",         "Job",         42, 699),
    Book("songofsongs", "Song of Songs", 8, 741),
    Book("ruth",        "Ruth",         4, 749),
    Book("lamentations", "Lamentations", 5, 753),
    Book("ecclesiastes", "Ecclesiastes", 12, 758),
    Book("esther",      "Esther",      10, 770),
    Book("daniel",      "Daniel",      12, 780),
    Book("ezra",        "Ezra-Nehemiah", 23, 792),
    Book("chronicles",  "Chronicles",  36, 815),
)

val TOTAL_CHAPTERS = BOOKS.sumOf { it.chapters } // 929

val SCRIPTURE_OUTPUT = File("assets/jewish/scripture.json")
val DUAS_OUTPUT = File("assets/jewish/duas.json")
val PROGRESS_FILE = File("assets/jewish/.jewish_progress.json")

val client = HttpClient.newBuilder()
    .connectTimeout(java.time.Duration.ofSeconds(30))
    .build()

fun sleep(ms: Long) = Thread.sleep(ms)

fun fetchJson(url: String, maxRetries: Int = 5): JSONObject? {
    repeat(maxRetries) { attempt ->
        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "PrayerApp/1.0")
                .timeout(java.time.Duration.ofSeconds(30))
                .GET()
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val body = response.body()

            if (body.trimStart().startsWith("Retry later") || response.statusCode() == 429) {
                val waitTime = 15000L * (attempt + 1)
                println("    Rate limited. Waiting ${waitTime / 1000}s...")
                sleep(waitTime)
                return@repeat
            }

            if (response.statusCode() == 200) {
                return JSONObject(body)
            }
        } catch (e: Exception) {
            println("    Attempt ${attempt + 1} failed: ${e.message}")
            sleep(5000L * (attempt + 1))
        }
    }
    return null
}

fun loadProgress(): MutableMap<String, MutableList<JSONObject>> {
    if (PROGRESS_FILE.exists()) {
        try {
            val json = JSONObject(PROGRESS_FILE.readText())
            val result = mutableMapOf<String, MutableList<JSONObject>>()
            for (key in json.keys()) {
                val arr = json.getJSONArray(key)
                val list = mutableListOf<JSONObject>()
                for (i in 0 until arr.length()) list.add(arr.getJSONObject(i))
                result[key] = list
            }
            return result
        } catch (e: Exception) {
            println("Warning: Could not load progress file: ${e.message}")
        }
    }
    return mutableMapOf()
}

fun saveProgress(progress: Map<String, List<JSONObject>>) {
    val json = JSONObject()
    for ((key, verses) in progress) {
        val arr = JSONArray()
        verses.forEach { arr.put(it) }
        json.put(key, arr)
    }
    PROGRESS_FILE.writeText(json.toString())
}

fun main() {
    println("=".repeat(60))
    println("Fetching Tanakh (24 books of the Hebrew Bible) from bible-api.com")
    println("Edition: KJV (English)")
    println("Total: $TOTAL_CHAPTERS chapters across ${BOOKS.size} books")
    println("=".repeat(60))

    val progress = loadProgress()
    val allVerses = mutableListOf<JSONObject>()

    for (book in BOOKS) {
        println("\n--- ${book.canonical} (${book.chapters} chapters, chapter_id ${book.offset + 1}-${book.offset + book.chapters}) ---")

        for (ch in 1..book.chapters) {
            val chapterId = book.offset + ch
            val key = "${book.name}_$ch"

            if (progress.containsKey(key)) {
                println("  ${book.canonical} $ch (id:$chapterId)... CACHED (${progress[key]!!.size} verses)")
                allVerses.addAll(progress[key]!!)
                continue
            }

            print("  ${book.canonical} $ch (id:$chapterId)... ")

            try {
                val data = fetchJson("https://bible-api.com/${book.name}+$ch?translation=kjv")
                if (data == null || !data.has("verses") || data.getJSONArray("verses").length() == 0) {
                    println("NO VERSES")
                    continue
                }

                val verses = data.getJSONArray("verses")
                val chapterVerses = mutableListOf<JSONObject>()
                for (i in 0 until verses.length()) {
                    val v = verses.getJSONObject(i)
                    val text = v.getString("text").trim()
                    val verse = JSONObject().apply {
                        put("chapter_id", chapterId)
                        put("verse_number", v.getInt("verse"))
                        put("original_text", text)
                        put("translated_text", text)
                    }
                    allVerses.add(verse)
                    chapterVerses.add(verse)
                }

                progress[key] = chapterVerses
                saveProgress(progress)

                println("OK (${verses.length()} verses)")
            } catch (e: Exception) {
                println("FAILED (${e.message})")
            }

            sleep(2000)
        }
    }

    println("\n${"=".repeat(60)}")
    println("Total verses collected: ${allVerses.size}")

    if (allVerses.isEmpty()) {
        println("ERROR: No verses collected. Aborting.")
        kotlin.system.exitProcess(1)
    }

    SCRIPTURE_OUTPUT.parentFile.mkdirs()
    val arr = JSONArray()
    allVerses.forEach { arr.put(it) }
    SCRIPTURE_OUTPUT.writeText(arr.toString(2))
    val sizeMB = SCRIPTURE_OUTPUT.length() / (1024.0 * 1024.0)
    println("\nWritten: assets/jewish/scripture.json (${String.format("%.1f", sizeMB)} MB)")

    val chapters = allVerses.map { it.getInt("chapter_id") }.distinct().sorted()
    println("Chapters: ${chapters.size} (range: ${chapters.first()}-${chapters.last()})")

    for (book in BOOKS) {
        val bookVerses = allVerses.filter { it.getInt("chapter_id") > book.offset && it.getInt("chapter_id") <= book.offset + book.chapters }
        val bookChapters = bookVerses.map { it.getInt("chapter_id") }.distinct()
        println("  ${book.canonical}: ${bookVerses.size} verses in ${bookChapters.size}/${book.chapters} chapters")
    }

    val missing = (1..TOTAL_CHAPTERS).filter { it !in chapters }
    if (missing.isNotEmpty()) {
        println("\nMissing chapters (${missing.size}): ${missing.joinToString(", ")}")
    } else {
        println("\nAll $TOTAL_CHAPTERS chapters present!")
    }

    // Write duas.json
    println("\n--- Creating Jewish Duas ---")
    val duas = JSONArray()

    fun dua(id: String, title: String, original: String, translated: String, transliteration: String) {
        duas.put(JSONObject().apply {
            put("id", id)
            put("title", title)
            put("original_text", original)
            put("translated_text", translated)
            put("transliteration", transliteration)
        })
    }

    dua("shema_yisrael", "Shema Yisrael",
        "שְׁמַע יִשְׂרָאֵל יְהוָה אֱלֹהֵינוּ יְהוָה אֶחָד",
        "Hear, O Israel: The LORD our God, the LORD is one.",
        "Shema Yisrael Adonai Eloheinu Adonai Echad")

    dua("modeh_ani", "Modeh Ani",
        "מוֹדֶה אֲנִי לְפָנֶיךָ מֶלֶךְ חַי וְקַיָּם שֶׁהֶחֱזַרְתָּ בִּי נִשְׁמָתִי בְּחֶמְלָה רַבָּה אֱמוּנָתֶךָ",
        "I give thanks before You, living and eternal King, for You have mercifully restored my soul within me; Your faithfulness is great.",
        "Modeh ani lefanecha Melech chai v'kayam, shehechezarta bi nishmati b'chemla raba emunatecha")

    dua("asher_yatzar", "Asher Yatzar",
        "בָּרוּךְ אַתָּה יְהוָה אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם אֲשֶׁר יָצַר אֶת הָאָדָם בְּחָכְמָה וּבָרָא בוֹ נְקָבִים נְקָבִים חֲלוּלִים חֲלוּלִים גָּלוּי וְיָדוּעַ לִפְנֵי כִסֵּא כְבוֹדֶךָ שֶׁאִם יִפָּתֵחַ אֶחָד מֵהֶם אוֹ יִסָּתֵם אֶחָד מֵהֶם אִי אֶפְשַׁר לְהִתְקַיֵּם וְלַעֲמוֹד לְפָנֶיךָ. בָּרוּךְ אַתָּה יְהוָה רוֹפֵא כָּל בָּשָׂר וּמַפְלִיא לַעֲשׂוֹת",
        "Blessed are You, LORD our God, King of the universe, who has formed man in wisdom and created in him many openings and many cavities. Blessed are You, LORD, who heals all flesh and works wonders.",
        "Baruch Atah Adonai Eloheinu Melech ha'olam asher yatzar et ha'adam b'chokhmah uv'aro bo nekavim nekavim chalulim chalulim galui v'yadua lifnei kis'evodecha she'im yipateach echad meihem o yistam echad meihem i efshar l'hithakayem la'amod lefanecha. Baruch Atah Adonai rofei kol basar umafli la'asot.")

    dua("elohai_neshamah", "Elohai Neshamah",
        "אֱלֹהַי הַנְּשָׁמָה שֶׁנָּתַתָּ בִּי טְהוֹרָה הִיא. אַתָּה בְּרָאתָהּ, אַתָּה יְצַרְתָּהּ, אַתָּה נְפַחְתָּהּ בִּי, וְאַתָּה מְשַׁמְּרָהּ בְּקִרְבִּי. בָּרוּךְ אַתָּה יְהוָה הַמַּחֲזִיר נְשָׁמוֹת לִפְגָרִים מֵתִים",
        "My God, the soul You have given me is pure. You created it, You formed it, You breathed it into me, and You preserve it within me. Blessed are You, LORD, who restores souls to dead bodies.",
        "Elohai haneshamah shenatata bi tehorah hi. Atah b'ratah, atah yatzarta, atah nafachtah bi, v'atah mashamrah b'kirbi. Baruch Atah Adonai hamachazir neshamot l'pegarim metim.")

    dua("birkat_hamazon", "Birkat HaMazon (Grace After Meals)",
        "בָּרוּךְ אַתָּה יְהוָה אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם הַזָּן אֶת הָעוֹלָם כֻּלּוֹ בְּטוּבוֹ בְּחֵן בְּחֶסֶד וּבְרַחֲמִים הוּא נוֹתֵן לֶחֶם לְכָל בָּשָׂר כִּי לְעוֹלָם חַסְדּוֹ. בָּרוּךְ אַתָּה יְהוָה הַזָּן אֶת הַכֹּל",
        "Blessed are You, LORD our God, King of the universe, who feeds the entire world with goodness, with grace, with mercy, and with compassion. Blessed are You, LORD, who feeds all.",
        "Baruch Atah Adonai Eloheinu Melech ha'olam hazan et ha'olam kulo b'tuvo b'chen b'chesed uv'rachamim hu noten lechem l'kol basar ki l'olam chasdo. Baruch Atah Adonai hazan et hakol.")

    dua("shemoneh_esrei", "Shemoneh Esrei (Amidah) Opening",
        "בָּרוּךְ אַתָּה יְהוָה אֱלֹהֵינוּ וֵאלֹהֵי אֲבוֹתֵינוּ אֱלֹהֵי אַבְרָהָם אֱלֹהֵי יִצְחָק וֵאלֹהֵי יַעֲקֹב הָאֵל הַגָּדוֹל הַגִּבֹּר וְהַנּוֹרָא אֵל עֶלְיוֹן",
        "Blessed are You, LORD our God and God of our fathers, God of Abraham, God of Isaac, and God of Jacob, the great, the mighty, and the awesome God, the Most High.",
        "Baruch Atah Adonai Eloheinu vElohei avoteinu Elohei Avraham Elohei Yitzchak vElohei Ya'akov ha'el hagadol hagibor vhanora El elyon.")

    dua("mi_shebeirach", "Mi Shebeirach (Prayer for Healing)",
        "מִי שֶׁבֵּרַךְ אֲבוֹתֵינוּ אַבְרָהָם יִצְחָק וְיַעֲקֹב, מֹשֶׁה וְאַהֲרֹן דָּוִד וּשְׁלֹמֹה, הוּא יְבָרֵךְ וִירַפֵּא אֶת הַחוֹלֶה. הַקָּדוֹשׁ בָּרוּךְ הוּא יִמָּלֵא רַחֲמִים עָלָיו לְהַחֲלִימוֹ וּלְרַפְּאוֹתוֹ. וְיִשְׁלַח לוֹ מְהֵרָה רְפוּאָה שְׁלֵמָה מִן הַשָּׁמַיִם. בְּתוֹךְ שְׁאָר חוֹלֵי יִשְׂרָאֵל, בִּמְהֵרָה וּבִזְמַן קָרוֹב. וְנֹאמַר אָמֵן",
        "May He who blessed our fathers bless and heal the sick. The Holy One, blessed be He, may He be filled with compassion for him/her to restore and heal him/her. And may He send a complete healing from heaven, swiftly and speedily. And let us say: Amen.",
        "Mi sheberach avoteinu Avraham Yitzchak v'Ya'akov Moshe v'Aaron David u'Sholmo, hu y'varech virapei et hacholeh. Hakadosh Baruch Hu yimalei rachamim alav l'hachalimo ul'rap'oto. V'yishloach lo meheira refuah shleimah min hashamayim. V'nomar Amen.")

    dua("tefilat_haderech", "Tefilat HaDerech (Traveler's Prayer)",
        "יְהִי רָצוֹן מִלְּפָנֶיךָ יְהוָה אֱלֹהֵינוּ שֶׁתּוֹלִיכֵנוּ לְשָׁלוֹם וְתַדְּבִּיבֵנוּ לְשָׁלוֹם. וְתַצִּילֵנוּ מִיַּד כָּל אֹיֵב. וּבְצֵל כְּנָפֶיךָ תַּסְתִּירֵנוּ. כִּי אֵל שׁוֹמֵר וּמַצִּיל אָתָּה. בָּרוּךְ אַתָּה יְהוָה שׁוֹמֵר עַמּוֹ יִשְׂרָאֵל לָעַד",
        "May it be Your will, LORD our God, that You lead us toward peace, guide us toward peace, and bring us to our destination in peace. And save us from every enemy. And under the shadow of Your wings hide us. For You are a God who guards and saves us. Blessed are You, LORD, who guards His people Israel forever.",
        "Yehi ratzon milfanecha Adonai Eloheinu sh'tolichenu l'shalom v'tad'bireinu l'shalom. V'tatzileinu miyad kol oyev. Uv'tzel k'nafekha tastireinu. Ki el shomeir umatzil atah. Baruch Atah Adonai shomeir amo Yisrael la'ad.")

    dua("kol_nidre", "Kol Nidre",
        "כָּל נִדְרֵי וֶאֱסָרֵי וּשְׁבוּעֵי דְּנִדְרָן וּדְנִשְׁבַּעְנָן מִיּוֹם כִּפּוּרִים זֶה עַד יוֹם כִּפּוּרִים. כֻּלְּהוֹן יְהוֹן שָׁרָן מְשָׁרֵין וּמְבֻטָּלִין. וּלְעַם יִשְׂרָאֵל יֵהֵוֵי שָׁלָם. כִּי סָלַחְתִּי כִּדְבָרֶךָ",
        "All vows, prohibitions, and oaths from this Day of Atonement until the next, may they all be released and absolved. For the people of Israel it shall be peace. For I have forgiven as You have spoken.",
        "Kol nidrei v'esarei ush'v'ei d'nidran ud'nishb'anan miyom kipurim zeh ad yom kipurim. Kulhon y'hon sharin m'sharin u'vutlin. Ul'am Yisrael y'hieu shalom. Ki slichti k'dvarecha.")

    dua("hashkiveinu", "Hashkiveinu (Evening Prayer for Protection)",
        "הַשְׁכִּיבֵנוּ יְהוָה אֱלֹהֵינוּ לְשָׁלוֹם וְהַעֲמִידֵנוּ מַלְכֵּנוּ לְחַיִּים. וּפְרֹשׂ עָלֵינוּ סֻכַּת שְׁלוֹמֶךָ. וְהָסֵר מֵעָלֵינוּ אֹיֵב דֶּבֶר וְחֶרֶב וְרָעָב וְיָגוֹן. וּבְצֵל כְּנָפֶיךָ תַּסְתִּירֵנוּ. כִּי אֵל שׁוֹמֵר וּמַצִּיל אָתָּה. בָּרוּךְ אַתָּה יְהוָה שׁוֹמֵר עַמּוֹ יִשְׂרָאֵל לָעַד",
        "Cause us to lie down, LORD our God, in peace, and raise us up, our King, to life. Spread over us the shelter of Your peace. Remove from before us enemy, pestilence, sword, famine, and sorrow. And under the shadow of Your wings hide us. For You are a God who guards and saves us. Blessed are You, LORD, who guards His people Israel forever.",
        "Hashkiveinu Adonai Eloheinu l'shalom v'ha'amideinu malkeinu l'chayim. Ufros aleinu sukat sh'lomecha. V'haseir me'alenu oyev dever v'cherev vra'ah v'yagon. Uv'tzel k'nafekha tastireinu. Ki el shomeir umatzil atah. Baruch Atah Adonai shomeir amo Yisrael la'ad.")

    DUAS_OUTPUT.writeText(duas.toString(2))
    println("Written: assets/jewish/duas.json (${duas.length()} prayers)")

    if (PROGRESS_FILE.exists()) {
        PROGRESS_FILE.delete()
        println("Progress file cleaned up.")
    }

    println("\nDone!")
}

main()
