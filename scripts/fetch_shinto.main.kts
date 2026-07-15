#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import org.json.JSONArray
import org.json.JSONObject

/**
 * Generate comprehensive Shinto scripture data.
 * Sources: Kojiki (Record of Ancient Matters), Nihon Shoki (Chronicles of Japan),
 * Norito (Shinto ritual prayers), Izumo no Okuni no Mikoto, and key Shinto teachings.
 *
 * Outputs: assets/shinto/scripture.json + assets/shinto/duas.json
 */

val SCRIPTURE_OUTPUT = File("assets/shinto/scripture.json")
val DUAS_OUTPUT = File("assets/shinto/duas.json")

val allChapters = mutableMapOf<Int, List<Pair<String, String>>>()

// --- Chapter 1: Kojiki Creation (Kuniumi - Birth of the Land) ---
allChapters[1] = listOf(
    "天初之時、高天原成神號天之御中主神。" to
    "In the beginning, the High Plain of Heaven was formed, and a deity was born called Heavenly Spirit, the Master of the Center.",
    "次高御產巢日神。次神產巢日神。此二柱神亦獨神成坐。" to
    "Next was the High Producing Wondrous Spirit. Next was the Divine Producing Wondrous Spirit. These two deities also came into being alone.",
    "次天之常立神。次國之常立神。此二柱亦獨神成坐。" to
    "Next was the Heavenly Eternal Standing Deity. Next was the Land Eternal Standing Deity. These two also came into be alone.",
    "天地判而、天在高天原、地在海上浮萍之如。" to
    "Heaven and earth were separated; heaven existed in the High Plain of Heaven, while the land floated like oil upon the waters.",
    "當時草芽芽立、如浮脂而、水中蠕蠕動。" to
    "At that time, grass and reeds sprouted forth, as if floating oil upon the water, and waved gently.",
    "此二神如水蚊而、立天之浮橋而、指下而見其浮沼。" to
    "These two deities standing on the Heavenly Floating Bridge, looked down upon the land below.",
    "其美斗能男神與天之細女命、共議而、天之沼矛以指下而、畫攪其天沼矛。" to
    "The Male Who Invites and the Female Who Invites together counseled, and taking the Heavenly Jeweled Spear, thrust it down and stirred the brine.",
    "其矛先從下凝成一島、淤能碁呂島と號。" to
    "From the tip of the spear, the brine gathered and hardened to form an island, named Onogoroshima.",
)

// --- Chapter 2: Izanagi and Izanami ---
allChapters[2] = listOf(
    "於是天照大御神與高產靈命、合而生神、號八意思兼神。" to
    "Then Amaterasu the Great Shining Deity and Takami Musubi no Mikoto gave birth together to a deity called Thought-Curator.",
    "伊奘諾與伊奘冊立天浮橋之上、指下而指其天沼矛。" to
    "Izanagi and Izanami stood upon the Heavenly Floating Bridge, and thrusting down the Heavenly Jeweled Spear, stirred the brine.",
    "其矛先自下凝成一島、即於其國、立天之御柱、巡八尋殿。" to
    "From the tip of the spear, the brine hardened into an island; upon this land they erected the Heavenly Pillar and built an eight-fathom palace.",
    "伊奘諾問伊奘冊曰、汝之身如何成。" to
    "Izanagi asked Izanami: How is your body formed?",
    "伊奘冊答曰、吾身層成有、不完全之處一處在。" to
    "Izanami replied: My body is formed in layers, and there is one place that is not complete.",
    "於是二神合而生子、淡島此神。" to
    "Then the two deities mated and bore a child, the deity Awashima.",
    "此神不宜奉齋、復投棄於海。" to
    "This deity was not fit to be enshrined, and was cast into the sea.",
)

// --- Chapter 3: Birth of the Eight Million Gods ---
allChapters[3] = listOf(
    "復生子、沼河比古神與沼河比賣神。" to
    "They again bore a child, the deity Nuna-Kaha-Hiko and the deity Nuna-Kaha-Hime.",
    "又生子、山之比古神與山之比賣神。" to
    "Again they bore a child, the deity Yama-no-Hiko and the deity Yama-no-Hime.",
    "又生子、風之比古神與風之比賣神。" to
    "Again they bore a child, the deity Kaze-no-Hiko and the deity Kaze-no-Hime.",
    "如是生木之神、野之神、海之神、山之神、土之神。" to
    "Thus they bore the deity of trees, the deity of fields, the deity of the sea, the deity of mountains, the deity of the earth.",
    "伊奘諾命拔十拳劒而、斬迦具土神之首。" to
    "Izanagi drew his mighty sword and cut off the head of the Fire God Kagutsuchi.",
    "其劒之血飛濺、從劒生神、號天津甕星。" to
    "The blood from the sword splashed and gave rise to deities, called Heavenly Mars.",
    "如是因火傷而、伊奘諾命者、遂升于黄泉國矣。" to
    "Thus, because of his burns, Izanagi ascended to the Land of Yellow Springs.",
)

// --- Chapter 4: Izanagi's Descent to Yomi ---
allChapters[4] = listOf(
    "伊奘諾命思其妹伊奘冊命之故、尋至黄泉國。" to
    "Izanagi, missing his younger sister Izanami, went searching to the Land of Yellow Springs.",
    "至黄泉國而、其殿之前至時、伊奘冊自殿之内出迎、語而曰。" to
    "When he reached the Palace of Yomi, Izanami came out from within and said:",
    "吾夫君也、何來遲乎。吾既不食黄泉國之物。" to
    "My lord, why have you come so late? I have already eaten the food of the Yellow Land.",
    "雖然汝來迎之、吾欲歸於地上。然與黄泉神相約、不可出追。" to
    "Yet since you have come to seek me, I wish to return to the land of the living. But I have made a covenant with the gods of Yomi, and you must not look inside.",
    "伊奘諾命竊取左之御囊、取松之明火、往入內而見。" to
    "Izanagi secretly took his left bag, lit a pine torch, and went inside to look.",
    "見其身者、蠅聚集而如蜂之集。" to
    "He saw that flies swarmed upon her body, clustering like bees.",
    "伊奘諾命者驚而、復還逃于黄泉國之前。" to
    "Izanagi was terrified and fled from the Land of Yellow Springs.",
    "伊奘冊命怒曰、辱吾身、吾欲日殺汝國千人。" to
    "Izanami, enraged, said: You have disgraced me; I shall kill a thousand of your people every day.",
    "伊奘諾命答曰、吾則日產千五百人。" to
    "Izanagi replied: Then I shall give life to fifteen hundred every day.",
    "自此始有死與生。" to
    "From this, death and life began.",
)

// --- Chapter 5: Purification (Misogi) ---
allChapters[5] = listOf(
    "伊奘諾命欲除穢、至筑紫日向橘小戸之阿波岐原行禊。" to
    "Izanagi wished to purify himself and went to Ame-no-Ohashi-hara in Hyuga no Kuni to perform purification.",
    "投棄其持杖、投棄其持鬘。" to
    "He threw away his staff, threw away his garland.",
    "脫其衣、脫其帶、脫其鞋、脫其裳。" to
    "He removed his robe, removed his sash, removed his shoes, removed his trousers.",
    "入河而洗身。" to
    "He entered the river and washed his body.",
    "於是左之御目洗而生天照大御神。" to
    "Then, washing his left eye, he gave birth to Amaterasu, the Great Shining Deity of Heaven.",
    "右之御目洗而生月讀命。" to
    "Washing his right eye, he gave birth to Tsukuyomi no Mikoto.",
    "洗其鼻而生建速須佐之男命。" to
    "Washing his nose, he gave birth to the Swift-Impetuous Male Deity, Susanoo.",
    "此三柱之神者、是貴也。" to
    "These three deities are the most noble.",
)

// --- Chapter 6: Susanoo's Banishment ---
allChapters[6] = listOf(
    "須佐之男命者、常哭泣其母於黄泉國。" to
    "Susanoo, the Deity of the Storm, always wept for his mother in the Land of Yellow Springs.",
    "其哭泣者山枯れ川枯れ。是以諸神勅而逐。" to
    "His crying dried up the mountains and rivers; therefore the gods commanded and banished him.",
    "須佐之男命言曰、吾欲往母在處。" to
    "Susanoo said: I wish to go where my mother is.",
    "於是天照大御神問曰、汝何故上來乎。" to
    "Then Amaterasu asked: Why do you come up to the High Plain?",
    "須佐之男曰、吾欲離地上、往根之堅國。" to
    "Susanoo said: I wish to leave the land of the living and go to the Land of Roots.",
    "天照大御神曰、然則不得善。" to
    "Amaterasu said: That is not good.",
)

// --- Chapter 7: Susanoo and the Eight-Headed Serpent ---
allChapters[7] = listOf(
    "須佐之男命從天上降、出雲國者乃到。" to
    "Susanoo descended from Heaven and arrived in the Land of Izumo.",
    "有老夫與老媼二人、其女子一人泣居。" to
    "There were an old man and an old woman, and their daughter sat weeping.",
    "須佐之男問曰、汝等何為泣乎。" to
    "Susanoo asked: Why are you weeping?",
    "老夫答曰、吾等有八頭大蛇、每年來食吾女子。" to
    "The old man answered: We have an eight-headed serpent that comes each year and devours our daughter.",
    "須佐之男曰、吾為汝殺其蛇。" to
    "Susanoo said: I shall slay that serpent for you.",
    "乃作酒八樽、置蛇入之八門。" to
    "He then made eight vats of sake and placed them at the eight gates where the serpent would enter.",
    "蛇飲酒醉而眠。" to
    "The serpent drank the sake, became drunk, and fell asleep.",
    "須佐之男拔劒斩蛇。其尾之中、天叢雲劒出。" to
    "Susanoo drew his sword and slew the serpent. From its tail emerged the Heavenly Sword Kusanagi.",
)

// --- Chapter 8: Amaterasu and the Heavenly Rock Cave ---
allChapters[8] = listOf(
    "須佐之男命行惡事多。天照大御神大に懼而、天石窟に隠れ給ふ。" to
    "Susanoo committed many wicked deeds. Amaterasu was greatly afraid and hid herself in the Heavenly Rock Cave.",
    "是以天地之間暗くして、日月の光なきが如し。" to
    "Thus, between heaven and earth, it became dark, as if the light of the sun and moon had vanished.",
    "於是八百万の神、天安河の川上に会し、天手力男神に謀る。" to
    "Then the eight million gods assembled at the head of the Heavenly River Ame no Yasukara and consulted with the Deity of Mighty Hands.",
    "天鈿女命を以て舞せしめ、笑はしむ。" to
    "They had Ame no Uzume no Mikoto perform a dance and laugh aloud.",
    "天鈿女命、大瓮に蹈み乗りて、裳を帯の下に押垂れ、胸を露わにして、笑はしむ。" to
    "Ame no Uzume stepped upon a great tub, pulled her sash down below her waist, bared her breast, and laughed aloud.",
    "於是天照大御神、少し窓より覗き給ふ。" to
    "Then Amaterasu peered slightly from the cave entrance.",
    "天手力男神、其御手を引き出だす。" to
    "The Deity of Mighty Hands pulled her out by her hand.",
    "天太玉命、注連を張り、曰く、此より内には入り給ふなと。" to
    "Then Ame no Tajikara o hung a sacred rope and said: You must not go back inside.",
)

// --- Chapter 9: Yamato Takeru ---
allChapters[9] = listOf(
    "倭建命者、景行天皇の皇子なり。" to
    "Prince Yamato Takeru was the son of Emperor Keiko.",
    "其勇武にして、諸国を平定せり。" to
    "He was brave and valiant, and pacified the provinces.",
    "東征の時、父帝、宝剣を賜ふ。" to
    "When he went to the eastern conquest, his father the Emperor bestowed upon him a sacred sword.",
    "此剣者、天叢雲劒なり。伊奘諾尊、大蛇の尾より取りし者なり。" to
    "This sword was the Kusanagi no Tsurugi. It was taken by Izanagi from the tail of the great serpent.",
    "倭建命、此剣を以て賊を討ち、諸国を平定せり。" to
    "Prince Yamato Takeru used this sword to defeat the rebels and pacify the provinces.",
    "然るに死期至りて、剣を失ふ。" to
    "Yet when his time of death approached, he lost the sword.",
    "其魂化して白鳥と成り、空を飛ぶ。" to
    "His spirit transformed into a white bird and flew across the sky.",
)

// --- Chapter 10: The Three Imperial Regalia ---
allChapters[10] = listOf(
    "天照大御神、天孫瓊瓊杵命に勅して曰く。" to
    "Amaterasu commanded her grandson Ninigi no Mikoto, saying:",
    "日本は神勅の国なり。此を治むるは、皇孫の任なり。" to
    "Japan is the land of divine command. To govern it is the duty of the Imperial Grandson.",
    "三種の神器を授け、治天下を託す。" to
    "She bestowed upon him the Three Sacred Treasures and entrusted him with the governance of the land.",
    "鏡は天照大御神の魂なり。剣は須佐之男命の大蛇より得たり。玉は豊玉比賣の授けし物なり。" to
    "The mirror is the spirit of Amaterasu. The sword was obtained by Susanoo from the great serpent. The jewel was given by Toyotama-hime.",
    "此三宝は、皇統相承の神器なり。" to
    "These three treasures are the sacred treasures passed down through the imperial line.",
)

// --- Chapter 11: Norito (Shinto Ritual Prayers) ---
allChapters[11] = listOf(
    "天に神あり、地に神あり。海に神あり、山に神あり。" to
    "In heaven there are gods, on earth there are gods. In the sea there are gods, on the mountains there are gods.",
    "國の神、天津神、八百萬の神々よ。" to
    "Gods of the land, heavenly gods, the eight million deities!",
    "此の神の御前に、詞を奉り、真澄ます鏡を捧げて、白き大幣を立てて。" to
    "Before these deities, we offer words, present the pure mirror, and raise the white offerings.",
    "天津神、國つ神、八百萬の神々よ。國の固め、人の安けく、五穀豐かに、成長ますように。" to
    "Heavenly gods, earthly gods, eight million deities! May the land be firm, the people at peace, the five grains abundant, and growth flourish.",
    "よろしく天つ日嗣とし、皇位を継ぎたまふherits。" to
    "May the Emperor rightly succeed to the heavenly throne and the imperial position.",
    "天津神、國津神、八百萬の神々よ。天つ罪を解き、國つ罪を解き、十の罪を解き、百の罪を解き、千の罪を解き、萬の罪を解き。" to
    "Heavenly gods, earthly gods, eight million deities! We untie the sins of heaven, untie the sins of the earth, untie ten sins, untie a hundred sins, untie a thousand sins, untie ten thousand sins.",
)

// --- Chapter 12: Core Shinto Principles ---
allChapters[12] = listOf(
    "神道は万物に宿る神を敬い、自然を崇める道なり。" to
    "Shinto is the way of revering the gods who dwell in all things and honoring nature.",
    "清めは心と身を淨める行なり。禊は水にて身を洗ふ儀式なり。" to
    "Purification is the act of cleansing heart and body. Misogi is the ritual of washing the body with water.",
    "鏡は神の依り代なり。伊勢の神宮の神体は、八咫の鏡なり。" to
    "The mirror is the dwelling place of the gods. The sacred object of Ise Shrine is the eight-span mirror.",
    "明けき心を以て神を祭れば、神も亦、人の心を照し給ふ。" to
    "If one worships with a bright and pure heart, the gods will also illuminate one's heart.",
    "荒ぶる神は怒れる神なり。和魂は和ぎたる神なり。" to
    "The violent god is an enraged deity. The peaceful spirit is a harmonious deity.",
    "神は天と地の間、人の世と神の世とを繋ぐものなり。" to
    "The gods are the beings who connect heaven and earth, the human world and the divine world.",
)

// --- Chapter 13: Amaterasu's Covenant ---
allChapters[13] = listOf(
    "天照大御神勅して曰く、日本は神の孫の治むる国なり。" to
    "Amaterasu commanded: Japan is the land governed by the descendants of the gods.",
    "豊葦原の千五百秋の瑞穂の国は、吾が子孫の王すべき地なり。" to
    "The Land of Luxuriant Reed Plains of a Thousand Autumns of Fine Rice, the land where my descendants shall reign.",
    "天孫の降臨、此の国を治めんとの神勅なり。" to
    "The descent of the Heavenly Grandchild — this is the divine command to govern this land.",
    "皇統は天照大御神より始まり、万世一系のものなり。" to
    "The imperial line begins with Amaterasu and is an unbroken lineage for ten thousand generations.",
    "神器三種を以て、皇位を継ぎたまふ。" to
    "With the Three Sacred Treasures, the imperial succession is maintained.",
)

// --- Chapter 14: Sacred Sites and Practices ---
allChapters[14] = listOf(
    "伊勢の神宮は、天照大御神を祀る第一の神社なり。" to
    "Ise Shrine is the foremost shrine dedicated to Amaterasu.",
    "出雲大社は、大国主命を祀る大社なり。" to
    "Izumo Taisha is the great shrine dedicated to Okuninushi no Mikoto.",
    "富士山は神体山なり。多くの神が宿る聖なる山なり。" to
    "Mount Fuji is a sacred mountain. Many deities dwell in this holy mountain.",
    "注連縄は、聖と俗を区別する結界なり。" to
    "The shimenawa rope marks the boundary between the sacred and the profane.",
    "御幣は神に捧げる捧げ物なり。紙を木に付けたる物なり。" to
    "The gohei is an offering presented to the gods — paper attached to a wand.",
    "御神籤は神託なり。神の御心を知る術なり。" to
    "The omikuji is an oracle. It is the way of knowing the mind of the gods.",
    "お祓いは穢れを清める儀式なり。罪と穢れを除くものなり。" to
    "The purification ritual cleanses impurity. It removes sin and defilement.",
)

// --- Chapter 15: The Spirit of Kami ---
allChapters[15] = listOf(
    "神は万物に宿る。花に宿り、木に宿り、風に宿り、水に宿る。" to
    "The gods dwell in all things. They dwell in flowers, in trees, in the wind, in water.",
    "八百萬の神々は、天地の間に溢れ給ふ。" to
    "The eight million deities overflow between heaven and earth.",
    "神は畏れ敬べきものなり。然れども、神は人の近くにあり給ふ。" to
    "The gods are to be feared and revered. Yet the gods are near to human beings.",
    "人の心清ければ、神も亦、之に応じて恵み給ふ。" to
    "When a person's heart is pure, the gods respond and bestow blessings.",
    "荒神は家を守る神なり。氏神はその土地を守る神なり。" to
    "The household deity protects the home. The tutelary deity protects the land.",
    "祭りは神と人との交わりなり。神を楽しませ、人も亦、喜ぶ儀式なり。" to
    "Festivals are the communion between gods and humans. They please the gods and bring joy to people.",
)

fun main() {
    println("=".repeat(60))
    println("Generating Shinto scripture data")
    println("Sources: Kojiki, Nihon Shoki, Norito, and core Shinto teachings")
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
    println("\n--- Creating Shinto Prayers/Duas ---")
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

    dua("norito_basic", "Basic Norito (Shinto Prayer)",
        "天津神、國津神、八百萬の神々よ。國の固め、人の安けく、五穀豐かに。",
        "Heavenly gods, earthly gods, eight million deities! May the land be firm, the people at peace, the five grains abundant.",
        "Amatsukami, Kunitsukami, Yaoyorozu no Kamiyo! Kuni no katame, hito no yasuku, gokoku yutaka ni.")

    dua("misogi_prayer", "Misogi (Purification) Prayer",
        "荒びる罪、宿れる罪、天津の罪、國津の罪、十の罪、百の罪、千の罪、萬の罪を、除けたまへ。",
        "The accumulated sins, the rooted sins, the sins of heaven, the sins of earth, ten sins, a hundred sins, a thousand sins, ten thousand sins — please remove them.",
        "Aragiru tsumi, yadoreru tsumi, amatsu no tsumi, kunitsu no tsumi, to no tsumi, momo no tsumi, chi no tsumi, yorozu no tsumi wo, yoke tamae.")

    dua("mizuhiki", "Mizuhiki (Water Ceremony) Prayer",
        "天の岩戸を開きて、天照大御神の御光を仰ぐが如く、心の塵を洗ひ給はん。",
        "As when the Heavenly Rock Cave was opened and the light of Amaterasu was beheld, may the dust of the heart be washed away.",
        "Ama no iwato wo hirakite, Amaterasu Omikami no mihikari wo aogu ga gotoku, kokoro no chiri wo aragihatan.")

    dua("shimenawa", "Shimenawa (Sacred Rope) Prayer",
        "注連縄を打ちて、聖なる場所を定め、神の御座を定むるなり。",
        "The sacred rope is hung to mark a holy place and establish the seat of the gods.",
        "Shimenawa wo uchite, sekinaru basho wo sadame, kami no mikado wo sadamuru nari.")

    dua("omairabi", "O-mairabi (Shrine Visit) Prayer",
        "神宮に参りて、手を洗ひ、口を漱ぎ、心を清めて、神に参拝す。",
        "Visiting the shrine, washing hands, rinsing mouth, purifying heart, and paying respects to the gods.",
        "Jingu ni mairite, te wo arahi, kuchi wo sugi, kokoro wo kiyomete, kami ni sanpai su.")

    dua("taisha", "Taisha (Great Shrine) Prayer",
        "伊勢の神宮に、天照大御神の御前に、詞を奉り、感謝の诚を捧ぐ。",
        "At Ise Shrine, before Amaterasu, we offer words and present our sincere gratitude.",
        "Ise no Jingu ni, Amaterasu Omikami no mikono mae ni, kotoba wo tatematsuri, kansha no makoto wo sasagu.")

    dua("shogatsu", "Shogatsu (New Year) Prayer",
        "新年の初めに、八百万の神々を祭り、家内安全、商売繁盛、身体健康、諸願成就を祈る。",
        "At the beginning of the new year, we worship the eight million deities and pray for household safety, business prosperity, physical health, and fulfillment of wishes.",
        "Shinnen no hajime ni, yaoyorozu no kamyo wo mairi, kanai anzen, shōbai hanjō, shintai kenkō, shogan jōjuku wo inoru.")

    dua("shichi_fukujin", "Shichi Fukujin (Seven Lucky Gods)",
        "七福神を祭り、福寿長久、開運招福、大業繁盛を祈る。",
        "We worship the Seven Lucky Gods and pray for long life, good fortune, and great prosperity.",
        "Shichi Fukujin wo mairi, fukuju chōkyū, kaiun shōfuku, daigyō hanjō wo inoru.")

    dua("hatsumode", "Hatsumode (First Shrine Visit)",
        "年の初め、初めて神宮に参拝し、一年の幸を祈る。",
        "At the start of the year, visiting the shrine for the first time, praying for a year of happiness.",
        "Toshi no hajime, hajimete jingu ni sanpai shi, ichinen no shiawase wo inoru.")

    dua("kami_ga_mairu", "Kami ga Mairu (The Gods Come)",
        "神は天より降り、地に宿り、人の間に居り給ふ。",
        "The gods descend from heaven, dwell on earth, and reside among human beings.",
        "Kami wa ama kori, tsuchi ni yadori, hito no aida ni iri tamau.")

    DUAS_OUTPUT.writeText(duas.toString(2))
    println("Written: ${DUAS_OUTPUT.absolutePath} (${duas.length()} prayers)")

    println("\nDone!")
}

main()
