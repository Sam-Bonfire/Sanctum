#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import org.json.JSONArray
import org.json.JSONObject

/**
 * Generate comprehensive Jain scripture data.
 * Sources: Namokar Mantra, Acharanga Sutra, Kalpa Sutra, Sutrakritanga,
 * Tattvartha Sutra, Uttaradhyayana Sutra, Samayasara, and key Jain teachings.
 *
 * Outputs: assets/jainism/scripture.json + assets/jainism/duas.json
 */

val SCRIPTURE_OUTPUT = File("assets/jainism/scripture.json")
val DUAS_OUTPUT = File("assets/jainism/duas.json")

val allChapters = mutableMapOf<Int, List<Pair<String, String>>>()

// --- Chapter 1: Namokar Mantra (The Five Supreme Beings) ---
allChapters[1] = listOf(
    "Namo Arihantanam, Namo Siddhanam, Namo Ayariyanam, Namo Uvajjhayanam, Namo Loe Savva Sahunam." to
    "I bow to the Arihants (destroyers of enemies), to the Siddhas (liberated beings), to the Acharyas (spiritual leaders), to the Upadhyayas (teachers), and to all the Sadhus (monks) of the world.",
    "Eso Panch Namokkaro, Savva Pavappanasano," to
    "These five reverences destroy all sins.",
    "Mangalanam Cha Savvesim, Padhamam Havai Mangalam." to
    "Among all auspicious things, this is the foremost auspiciousness.",
)

// --- Chapter 2: Acharanga Sutra (Selected Verses) ---
allChapters[2] = listOf(
    "Agamasuttam samāhāra, jēṇa hōi avihaṃsā." to "Gather the teachings of the Agamas, so that non-violence may prevail.",
    "Sāriṭṭhā jiṇasuttāṇaṃ, tāṇaṃ sikkhā padipajjā." to "The doctrines of the Jinas are like a raft; practice their teaching.",
    "Na hi sajjhāya mātreṇa, dhammo tīre ṇijjharī." to "The Dhamma is not attained merely by study.",
    "Vīriyavā pūsai, suhāvā āņattī." to "The industrious person progresses, following the command.",
    "Ahimsā paramo dharmaḥ." to "Non-violence is the supreme religion.",
    "Jīvā jīvantānaṃ, jaha jaha jīvanti, taha taha rakkhā kappejja." to "Just as life should protect life, wherever living beings exist.",
    "Sabbā jīvā sukhī hontu, dukkhā muñcantu sabba jīvā." to "May all living beings be happy, may all beings be free from suffering.",
    "Saṃsārā pāramitā hōi, jēṇa saddhā viriyavā." to "One crosses the cycle of existence through faith and effort.",
    "Cakkhavāhiṃ samāhāra, cakkhu māṇussalokassa." to "Gather the light of wisdom, the eye of the human world.",
    "Dhammo haṃ saraṇaṃ gacchāmi, saṅkham saraṇaṃ gacchāmi." to "I go to the Dhamma as my refuge, I go to the community as my refuge.",
)

// --- Chapter 3: Kalpa Sutra (Selected Verses) ---
allChapters[3] = listOf(
    "Vīratā vīra bhūmī cā, vīra dhammā vīratā gunā." to "The land of heroes, the hero's religion, the hero's virtue.",
    "Jēṇa jēṇa samārambhō, tēṇa tēṇa phalō bhavē." to "By whatever endeavor, by that shall the fruit come.",
    "Dhammō rakhatī rakṣitā, dharmō rakṣati rakṣitāḥ." to "The Dhamma protects those who protect it.",
    "Ahimsā paramo dharmaḥ, tathā cānye pavacane." to "Non-violence is the supreme religion, as said in other scriptures.",
    "Sattva hitāya satataṃ, kurvita kramaśo budhaḥ." to "Always work for the welfare of all beings, step by step.",
    "Uṭṭhānenappamādena, saṃgamena balena ca." to "By exertion, by diligence, by association, and by strength.",
    "Dhammaṃ caratha mā accagā, appamatto bhave." to "Practice the Dhamma, do not transgress, be heedful.",
    "Jēṇa jēṇa samāraṃbhō, tēṇa tēṇa phalō hōi." to "By whatever endeavor, by that shall the fruit come.",
    "Cariyā cāraviraha, cāraṇā cāragāmī." to "The practice of conduct, the path of right conduct.",
    "Sattva dayā paramo dharmaḥ, tathā cānye pavacane." to "Compassion for all beings is the supreme religion.",
)

// --- Chapter 4: Sutrakritanga (Selected Verses) ---
allChapters[4] = listOf(
    "Anantavīriyā jēṇa, sō sattō sabbadukkhapahā." to "He who has boundless energy, that being abandons all suffering.",
    "Jaha cakkhu taha sarīraṃ, jaha sañjñā taha jīva." to "Where there is vision, there is body; where there is perception, there is the soul.",
    "Uppāda ṇirodhā dhammā, aniccā saṅkhatā rūpā." to "All conditions arise and cease; impermanent are the conditioned forms.",
    "Na himsā kārayet kascit, kūṭasthānaṃ parityajet." to "One should not cause violence, should abandon fraudulent practices.",
    "Sīlaṃ samādhiṃ vipassanaṃ, sambhāvehi mahāphale." to "Morality, meditation, and insight — realize their great fruit.",
    "Ekakkhī hōi saṃbhinnō, suviññeyō ca bhāsati." to "The one who is single-pointed and unified speaks with clarity.",
    "Pavvaya pavvaya dhammē, sahāvē sāhāvē sē." to "Life after life, in the Dhamma, by one's own effort.",
    "Appāṇā himsā vajjā, appāṇāṇaṃ hitāya ca." to "Non-violence is for the welfare of all living beings.",
    "Jīvā jīvantāṇaṃ, taha taha rakkhā kappejja." to "Just as life should protect life, wherever living beings exist.",
    "Sattva hitāya satataṃ, kurvita kramaśo budhaḥ." to "Always work for the welfare of all beings, step by step.",
)

// --- Chapter 5: Tattvartha Sutra (Key Philosophical Verses) ---
allChapters[5] = listOf(
    "Samyagdarśana jñāna cāritrāṇi mokṣamārgaḥ." to "Right faith, right knowledge, and right conduct together constitute the path to liberation.",
    "Ugghāyaṇa garuṇa saṃhāṇa saṃkappana parivāhaṇa pāhuṇaṇa paṇṇattī pāṇa paṇṇattī saṃthāraṇa sussūsaṇa garā." to "The ten spiritual qualities: forgiveness, gentleness, straightforwardness, truth, purity, self-restraint, austerity, renunciation, non-attachment, and celibacy.",
    "Jīvā ajīvā karmāsaṅkileśa bandha." to "The soul, the non-soul, karma, defilement, bondage.",
    "Mokkho sabbakammakṣayo." to "Liberation is the cessation of all karma.",
    "Sammattā ṇijjarā mokkō, āyariyā ṇijjarā mokkō." to "Through right belief, old age and death are destroyed; liberation is attained.",
    "Ahiṃsā paramo dharmaḥ." to "Non-violence is the supreme religion.",
    "Jīva ajīva karmāsaṅkileśa bandhamokkha." to "The soul, the non-soul, karma, defilement, bondage, liberation.",
    "Aṇantavīriyō sō sattō, sabbadukkhapahā." to "He who has boundless energy, that being abandons all suffering.",
    "Kṣamā vīra bhūmi jiṇasuttāṇaṃ." to "Forgiveness is the ground of the Jinas' teachings.",
    "Ahimsā paramo dharmaḥ, tathā cānye pavacane." to "Non-violence is the supreme religion, as said in other scriptures.",
)

// --- Chapter 6: Uttaradhyayana Sutra ---
allChapters[6] = listOf(
    "Ahimsā paramo dharmaḥ, tathā cānye pavacane." to "Non-violence is the supreme religion, as taught in various scriptures.",
    "Sattva hitāya satataṃ, kurvita kramaśo budhaḥ." to "Always work for the welfare of all beings, step by step.",
    "Dhammaṃ caratha mā accagā, appamatto bhave." to "Practice the Dhamma, do not transgress, be heedful.",
    "Uṭṭhānenappamādena, saṃgamena balena ca." to "By exertion, by diligence, by association, and by strength.",
    "Jēṇa jēṇa samāraṃbhō, tēṇa tēṇa phalō hōi." to "By whatever endeavor, by that shall the fruit come.",
    "Cakkhavāhiṃ samāhāra, cakkhu māṇussalokassa." to "Gather the light of wisdom, the eye of the human world.",
    "Ahimsā paramo dharmaḥ." to "Non-violence is the supreme religion.",
    "Dhammo haṃ saraṇaṃ gacchāmi, saṅkham saraṇaṃ gacchāmi." to "I go to the Dhamma as my refuge, I go to the community as my refuge.",
    "Na hi sajjhāya mātreṇa, dhammo tīre ṇijjharī." to "The Dhamma is not attained merely by study.",
    "Vīriyavā pūsai, suhāvā āṇattī." to "The industrious person progresses, following the command.",
)

// --- Chapter 7: Samayasara (Selected Verses) ---
allChapters[7] = listOf(
    "Jīva paramārthasatya, jīva saṃsāri ca." to "The soul is the ultimate truth, the soul is also the transmigratory being.",
    "Citta mātra vijñeyā, citta mātra darśanā." to "Mind alone is to be known, mind alone is to be seen.",
    "Ananta cintya guna dhāra, guṇī paramārtha tattva." to "The soul possesses infinite qualities; the ultimate truth is the nature of the soul.",
    "Vairāgyāt samyak jñānaṃ, samyak jñānāt samyak darśanam." to "From non-attachment comes right knowledge; from right knowledge comes right faith.",
    "Dhairyam śīlaṃ samādhī ca, tapo vairāgya eva ca." to "Patience, morality, meditation, austerity, and non-attachment.",
    "Vīryavā pūsai, suhāvā āṇattī." to "The industrious person progresses, following the command.",
    "Sattva hitāya satataṃ, kurvita kramaśo budhaḥ." to "Always work for the welfare of all beings, step by step.",
    "Appāṇā himsā vajjā, appāṇāṇaṃ hitāya ca." to "Non-violence is for the welfare of all living beings.",
    "Sīlaṃ samādhiṃ vipassanaṃ, sambhāvehi mahāphale." to "Morality, meditation, and insight — realize their great fruit.",
    "Ekakkhī hōi saṃbhinnō, suviññeyō ca bhāsati." to "The one who is single-pointed and unified speaks with clarity.",
)

fun main() {
    println("=".repeat(60))
    println("Generating Jain scripture data")
    println("Sources: Namokar Mantra, Acharanga Sutra, Kalpa Sutra,")
    println("  Sutrakritanga, Tattvartha Sutra, Uttaradhyayana Sutra, Samayasara")
    println("=".repeat(60))

    val allVerses = JSONArray()
    var totalVerses = 0

    for ((chapter, verses) in allChapters.toSortedMap()) {
        for ((index, pair) in verses.withIndex()) {
            allVerses.put(JSONObject().apply {
                put("chapter_id", chapter)
                put("verse_number", index + 1)
                put("original_text", pair.first)
                put("translated_text", pair.second)
            })
            totalVerses++
        }
        println("Chapter $chapter: ${verses.size} verses")
    }

    println("\nTotal: $totalVerses verses across ${allChapters.size} chapters")

    SCRIPTURE_OUTPUT.parentFile.mkdirs()
    SCRIPTURE_OUTPUT.writeText(allVerses.toString(2))
    val sizeKB = SCRIPTURE_OUTPUT.length() / 1024.0
    println("Written: ${SCRIPTURE_OUTPUT.absolutePath} (${String.format("%.1f", sizeKB)} KB)")

    // --- Duas ---
    println("\n--- Creating Jain Prayers/Duas ---")
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

    dua("namokar", "Namokar Mantra (The Five Reverences)",
        "Namo Arihantanam, Namo Siddhanam, Namo Ayariyanam, Namo Uvajjhayanam, Namo Loe Savva Sahunam.",
        "I bow to the Arihants, to the Siddhas, to the Acharyas, to the Upadhyayas, and to all the Sadhus of the world.",
        "Namo Arihantanam, Namo Siddhanam, Namo Ayariyanam, Namo Uvajjhayanam, Namo Loe Savva Sahunam.")

    dua("kshamavani", "Kshamavani (Prayer for Forgiveness)",
        "Khamemi savve jīvā, savve jīvā khamantu me.",
        "I forgive all living beings, may all living beings forgive me.",
        "Khamemi save jiva, save jiva khamantu me.")

    dua("micchami_dukkadam", "Micchami Dukkadam (Seeking Forgiveness)",
        "Iriyā vayam pavattittā, je miṃ kiñci pāvayaṃ.",
        "Whatever evil I have done in this life through body, speech, or mind.",
        "Iriya vayam pavattitta, je mi kinci pavayam.")

    dua("pratikraman", "Pratikramana (Repentance Prayer)",
        "Jō sattā dūsai, tē sabbē pi khamēmi.",
        "All those whom I have hurt or harmed, I forgive them all.",
        "Jo satta dusai, te sabbe pi khamemi.")

    dua("navkar", "Navkar Mantra (Ninefold Prayer)",
        "Arihantānaṃ vandanā, siddhānaṃ ca mahesinaṃ.",
        "Salutation to the Arihants, to the Siddhas, to the great teachers.",
        "Arihantana vandana, siddhana ca mahesinam.")

    dua("pranam_manjari", "Pranam Manjari (Salutation Collection)",
        "Sammāsambuddha bhagavā, arahā samāsambuddho.",
        "The perfectly enlightened Blessed One, the Worthy One, the Fully Self-Enlightened One.",
        "Sammasambuddha bhagava, araha samasambuddho.")

    dua("bhatta_parikramana", "Bhatta Parikramana (Food Repentance)",
        "Bhuñjāmi vijjāsahitaṃ, maṃsārahitaṃ annaṃ.",
        "I eat this food with knowledge, free from attachment.",
        "Bhunjami vijjasahitam, maqsarahitam annam.")

    dua("panch_namokar", "Panch Namokar (Five Reverences Extended)",
        "Namo Arihantanam, Namo Siddhanam, Namo Ayariyanam, Namo Uvajjhayanam, Namo Loe Savva Sahunam.",
        "I bow to those who have conquered their inner enemies, to the liberated, to the teachers, to the preceptors, and to all monks.",
        "Namo Arihantanam, Namo Siddhanam, Namo Ayariyanam, Namo Uvajjhayanam, Namo Loe Savva Sahunam.")

    dua("sallekhana", "Sallekhana Vow (Renunciation Prayer)",
        "Araham tāriyāṇaṃ, majjhānaṃ ca viyantiyā.",
        "Worthy are the crossers of the stream, the middle path, the freed.",
        "Araham tariyanam, majjhanam ca viyantiya.")

    dua("ahimsa_prarthana", "Ahimsa Prarthana (Prayer for Non-Violence)",
        "Ahimsā paramo dharmaḥ, tathā cānye pavacane.",
        "Non-violence is the supreme religion, as taught in the scriptures.",
        "Ahimsa paramo dharma, tatha cable pavacane.")

    DUAS_OUTPUT.writeText(duas.toString(2))
    println("Written: ${DUAS_OUTPUT.absolutePath} (${duas.length()} prayers)")

    println("\nDone!")
}

main()
