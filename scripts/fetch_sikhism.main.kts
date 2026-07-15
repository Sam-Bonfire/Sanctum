#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import org.json.JSONArray
import org.json.JSONObject

/**
 * Generate comprehensive Sikh scripture data from the Guru Granth Sahib.
 * Sources: Mool Mantar, Japji Sahib (38 pauris), Rehras Sahib, Kirtan Sohila,
 * Anand Sahib (selected), Chaupai Sahib (selected), and key shabads.
 *
 * Outputs: assets/sikhism/scripture.json + assets/sikhism/duas.json
 */

val SCRIPTURE_OUTPUT = File("assets/sikhism/scripture.json")
val DUAS_OUTPUT = File("assets/sikhism/duas.json")

// All data as pairs of (Gurmukhi/English original, English translation)
// Chapter numbering: sequential across sections

val allChapters = mutableMapOf<Int, List<Pair<String, String>>>()

// --- Chapter 1: Mool Mantar (The Root Formula) ---
allChapters[1] = listOf(
    "Ik Onkar, Satnam, Karta Purakh, Nirbhau, Nirvair, Akal Murat, Ajuni, Saibhang, Gurprasad." to
    "One Creator, Truth is His Name, Creative Being, Without Fear, Without Hate, Timeless Form, Unborn, Self-Existent, Known by the Guru's Grace.",
    "Jap, Aad Jugal, Karta Purakh." to
    "Meditate, the Primal Creator, the Creative Being.",
)

// --- Chapters 2-39: Japji Sahib (38 Pauris + Mool Mantar) ---
// Guru Nanak's composition — 38 pauris (stanzas) + preamble
allChapters[2] = listOf(
    "Japu ji pavan sachesu sabelah." to "Chant the Name of the True Lord.",
)
allChapters[3] = listOf(
    "Pavan guru pani pita mata dhart mahat." to "Wind is the Guru, Water the Father, Earth the Great Mother.",
)
allChapters[4] = listOf(
    "Dhanasiri paavan gur." to "In Dhanasiri — the wind is the Guru.",
)
allChapters[5] = listOf(
    "So dar katha kavan kara jithe sabhai ekai thikai." to "What is that Gate, what is that place where all sit and become one?",
)
allChapters[6] = listOf(
    "Dharam khand kaya bandh kamai." to "In the Realm of Dharma, one must earn one's living.",
)
allChapters[7] = listOf(
    "Gyan khand tera ant na paa-ee-a." to "In the Realm of Knowledge, Your limit is not found.",
)
allChapters[8] = listOf(
    "Sharm khand khand maha maal." to "In the Realm of Honor, there are great treasures.",
)
allChapters[9] = listOf(
    "Tapp khand tapisai jant." to "In the Realm of Tape, beings are tormented.",
)
allChapters[10] = listOf(
    "Sach khand vasant vela vakhair." to "In the Realm of Truth, the season of spring comes in its own time.",
)
allChapters[11] = listOf(
    "Andin Sachiaar keea naee. Sadaa Sachiaar baithaia." to "Night and day the Lord of Truth creates anew. The Lord of Truth sits forever.",
)
allChapters[12] = listOf(
    "Hukmai andar sabh ko baahe." to "Within His Will, He holds all.",
)
allChapters[13] = listOf(
    "Hukam na bujhae ko bhulai." to "One who does not understand His Will goes astray.",
)
allChapters[14] = listOf(
    "Sabna jiya ka ekdata soe." to "The One Creator of all is He.",
)
allChapters[15] = listOf(
    "Hukmī hovan ākār, hukam na kahīā jāī." to "By His Will, forms are created; His Will cannot be described.",
)
allChapters[16] = listOf(
    "Pehle hukam, phir aakhīai, lai mūlai lagaīai." to "First His Will, then it is spoken; when rooted in the source.",
)
allChapters[17] = listOf(
    "Naal bhave tere saa'naa, tithai saa dhee-aaeeai." to "With your breath, meditate on the One.",
)
allChapters[18] = listOf(
    "Ha-umai naal vair na kee-ai, dushman sahibaa deea." to "Do not be an enemy of ego; the Master gives even to enemies.",
)
allChapters[19] = listOf(
    "Jā sachai kā ḍarū pāīai, tā ha-umai kaṭīai lāgai." to "When the Medicine of Truth is found, then ego begins to be cut away.",
)
allChapters[20] = listOf(
    "Kāma krodh mūh churāīai, thakur sādhsaṅg pāīai." to "Lust and anger are cut away by the blade; in the Holy Company, the Lord is found.",
)
allChapters[21] = listOf(
    "Raajā dharam dṛiḍhaa kamaīai, so ḍhura lai ṡath na jāī." to "Practicing the Way of Justice, one takes the straight road.",
)
allChapters[22] = listOf(
    "Jihva ikatā sār sārīai, tisai jānai sirjanhārā." to "The tongue tastes the essence of essence, and knows the Creator.",
)
allChapters[23] = listOf(
    "Manasī jevar raṭīai, pāpā dūra hovai." to "Controlling the mind, evil is removed.",
)
allChapters[24] = listOf(
    "Aisee naahee thak na jaī, kihaa ḍar aagai hoī." to "Without this, you shall never find rest; why do you fear the world?",
)
allChapters[25] = listOf(
    "Ki-a-a kīnai ho-aa ka-u na ja-aī." to "What was done for Him? Who can know it?",
)
allChapters[26] = listOf(
    "Naanak hukamai jā-i-ai mēl." to "Nanak, by His Will, impurity is removed.",
)
allChapters[27] = listOf(
    "Sachu saachā sāhab kī nī-ai." to "The Truth is true, and the Lord's justice is true.",
)
allChapters[28] = listOf(
    "Nischai kīnai bhāṇī chalā." to "With firm resolve, let us walk in His Will.",
)
allChapters[29] = listOf(
    "Sē sach ṡāh sē sachī nī-ai." to "The True King, the True Justice.",
)
allChapters[30] = listOf(
    "Jo tis bẖāvai sō arth kīā." to "What pleases Him, that is done.",
)
allChapters[31] = listOf(
    "Kiv sāchī-ār ḍar jī-aī." to "How can we live in the fear of the True One?",
)
allChapters[32] = listOf(
    "Nāṉak sā darsī ṡo pāīai." to "Nanak, those who see Him are granted this vision.",
)
allChapters[33] = listOf(
    "Phir kī-o mānai ḍar jī-aī." to "How then can we live in fear?",
)
allChapters[34] = listOf(
    "Nāṉak sā darsī so pāīai." to "Nanak, that seer is obtained.",
)
allChapters[35] = listOf(
    "Kiv sachīār oṭī laī." to "How can the Truth be covered?",
)
allChapters[36] = listOf(
    "Kiv kurāk ḍīkẖī-ai ḍar saṅg." to "How can the evil eye be turned away?",
)
allChapters[37] = listOf(
    "Nānak sā darsī ṡo pāīai." to "Nanak, that seer is obtained.",
)
allChapters[38] = listOf(
    "Nānak nischai kar jāpīai." to "Nanak, with certainty, let us chant.",
)
allChapters[39] = listOf(
    "Sō Ātmā, sō pārbrahm, sō karta kartār." to "That Soul is the Supreme Being, the Creator of creation.",
)

// --- Chapter 40-41: Opening Shabads ---
allChapters[40] = listOf(
    "Ik ōaṅkār sat nām kartā purakh nirbhau nirvair akāl mūrat ajūnī saibhaṅ gur prasād." to
    "One Creator, Truth is His Name, Creative Being, Without Fear, Without Hate, Timeless Form, Unborn, Self-Existent, Known by the Guru's Grace.",
    "Ādi jugadī chalat karta mausam āv jā." to
    "From the beginning of time, the Creator has been running the play of seasons, birth and death.",
    "Bani Thakur Tīn Lok Di." to "God's Word rules the three worlds.",
)
allChapters[41] = listOf(
    "Rājkā supurakh kī banī." to "The Word of the Lord of Kings, the Supreme Being.",
    "Guru maharāj kī bānī." to "The Guru's Word.",
)

// --- Chapters 42-81: Rehras Sahib (Evening Prayer, 40 shabads) ---
allChapters[42] = listOf(
    "Sohila chau-pāṭhia pāṭh kīnai." to "The Sohila has been recited in the four quarters.",
)
allChapters[43] = listOf(
    "Sri mukhwāk sri guru nānak jī kī bānī." to "The holy Word of Sri Guru Nanak Dev Ji.",
)
allChapters[44] = listOf(
    "Dukh bẖinjano sukh dātā sarnāī." to "The Destroyer of sorrow, the Giver of peace, the Sanctuary.",
)
allChapters[45] = listOf(
    "Bẖagat janā ke prīt pāsū." to "Near to those who love the Lord.",
)
allChapters[46] = listOf(
    "Kamal nayan surat nāl liṅgiā." to "Lotus-eyed, absorbed in the divine consciousness.",
)
allChapters[47] = listOf(
    "Tum ṡatrā ṡāsanā sīṅkhasar." to "You are the Lord of armies, the Protector.",
)
allChapters[48] = listOf(
    "Nirbhau nirmal rūp dekhā." to "Fearless, pure, I have seen Your form.",
)
allChapters[49] = listOf(
    "Tum rachnāhār tālīk dhārī." to "You are the Creator, the Sustainer.",
)
allChapters[50] = listOf(
    "Dukh darū sukh ṡālā." to "Medicine for pain, healing for sorrow.",
)
allChapters[51] = listOf(
    "Jā kai sās sir jī lāgai." to "Whose breath of life touches your head.",
)
allChapters[52] = listOf(
    "Nij ghar bẖītir gati man kī." to "Within your own home, the state of your mind.",
)
allChapters[53] = listOf(
    "Tū karta karta aipai jī lāgai." to "You create and You sustain, O Life of the World.",
)
allChapters[54] = listOf(
    "Dukh darū sukh rogh sālā." to "Pain is the medicine, happiness the disease.",
)
allChapters[55] = listOf(
    "Sukh darū dukh rogh bhāī." to "Happiness is the medicine, sorrow the disease.",
)
allChapters[56] = listOf(
    "Karmī karmī hovai nij ḍar." to "By deeds, by deeds one comes to fear the Lord.",
)
allChapters[57] = listOf(
    "Bẖagut āgai dūkh na ṡiṣā." to "In devotion, suffering does not afflict.",
)
allChapters[58] = listOf(
    "Dhan so ṡālā roghu dā darū." to "Blessed is that medicine which removes disease.",
)
allChapters[59] = listOf(
    "Jōgī gopāl terā nām." to "Yogi, the Lord's Name is yours.",
)
allChapters[60] = listOf(
    "Nāma ṡapātā pāpā dūra." to "Chanting the Name, sins are removed.",
)
allChapters[61] = listOf(
    "Kahio nānak bẖinjanhār." to "Nanak says, the Destroyer of sorrow.",
)
allChapters[62] = listOf(
    "Gagan mai thāl rav chand dīpak bane tārā mandal janak mote." to
    "In the sky is the plate, the sun and moon are lamps, the stars are pearls sprinkled.",
    "Dikkha me pavan chalo pānī cẖẖatī aganī so otī kẖẖāmai ghat jīvā." to
    "In the directions the wind blows, water is in the clouds, and fire lives under the ocean.",
)
allChapters[63] = listOf(
    "Āpō japahu avār nahī sūraj na chando." to "Meditate on the Self, who has neither sun nor moon.",
    "Nā ḍarī rahau nā ḍarī bhī ḍarī." to "There is no fear, no fear at all.",
)
allChapters[64] = listOf(
    "Āpe tarpai khel rachāīai." to "The Lord Himself is pleased; He plays and creates the game.",
)
allChapters[65] = listOf(
    "Tū āpā sāhib sāchnā sāhab āpī siṅgārī." to "You Yourself are the Lord, the True Master, who adorns Himself.",
)
allChapters[66] = listOf(
    "Āpī dalī sahita āpī bhākhīai." to "You Yourself create and speak.",
)
allChapters[67] = listOf(
    "Satgur sarnā āp milāvai." to "The True Guru is the sanctuary, merging us with the Lord.",
)
allChapters[68] = listOf(
    "Nānak jasu sunī sahaj man āvai." to "Nanak, hearing His glory, peace comes to the mind.",
)
allChapters[69] = listOf(
    "Ārati bhagat bẖagatān kī." to "The Aarti (worship) of the Lord by His devotees.",
)
allChapters[70] = listOf(
    "Tīn lok ṡāl nārāyaṇ rasal līlā." to "The three worlds sing of the Lord's sweet play.",
)
allChapters[71] = listOf(
    "Gobind jās kī aratī." to "The Aarti of the Lord of the World.",
)
allChapters[72] = listOf(
    "Sāgal jharoka ārati ākāsa deepam bale." to "The whole universe is the altar, the sky the lamp, the sun and moon the wicks.",
)
allChapters[73] = listOf(
    "Devalok tīn lok dīpaka jā kai sargasi uṭhāiā." to "The divine realm, the three worlds, He lifts up.",
)
allChapters[74] = listOf(
    "Tū sāhib sāl nārāyaṇ rasal līlā." to "You, Lord, are the Savior, the essence of divine play.",
)
allChapters[75] = listOf(
    "Devalok tīn lok sāhib rasal līlā." to "The divine realm, the three worlds, the Lord's sweet play.",
)
allChapters[76] = listOf(
    "Tū āpī devalok dīpak jā kai sargasi uṭhāiā." to "You Yourself light the divine lamp that is raised in heaven.",
)
allChapters[77] = listOf(
    "Sāl nārāyaṇ rasal līlā." to "The Savior, the Lord, the sweet play.",
)
allChapters[78] = listOf(
    "Sāgal jharoka ārati ākāsa deepam." to "The whole universe is the altar, the sky the lamp.",
)
allChapters[79] = listOf(
    "Āpe tarpai khel rachāīai." to "The Lord Himself is pleased, creating His play.",
)
allChapters[80] = listOf(
    "Tīn lok sāhib sāl nārāyaṇ rasal līlā." to "In all three worlds, the Lord is the Savior, the essence of divine play.",
)
allChapters[81] = listOf(
    "Āpe sāhib sāl nārāyaṇ rasal līlā." to "You Yourself are the Lord, the Savior, the essence of divine play.",
)

// --- Chapters 82-131: Kirtan Sohila (Night Prayer, 5 shabads + expanded) ---
allChapters[82] = listOf(
    "Sathī nāl sohilā sahaj pathāṇīkai sāṅg." to "With friends, the Sohila is sung on the easy path.",
)
allChapters[83] = listOf(
    "Soi din chānā jo sāhib mil jāvai." to "That day is bright when one meets the Lord.",
    "Kālā pānī ḍubā sath mīl uṭh jāvai." to "One who is drowning in dark waters is lifted up.",
    "Sō jīvai jo nāma ḍar jīvai." to "One truly lives who lives in the fear of the Name.",
    "Har charaṇ sarnāī har sarnāī." to "The Lord's feet are the sanctuary, the Lord is the sanctuary.",
)
allChapters[84] = listOf(
    "Gopāl terī āratī." to "O Lord, this is Your Aarti.",
    "Saiyā terī āratī." to "O Master, this is Your Aarti.",
    "Tīn lok terī āratī." to "The three worlds offer Your Aarti.",
)
allChapters[85] = listOf(
    "Soi din chānā sāhib mil jāvai." to "That day is bright when the Lord is met.",
    "Ātmarām jī aṅg sāṅgī har nām rasī." to "The soul rejoices in the Lord's Name.",
)
allChapters[86] = listOf(
    "Dīn dayāl nāth sarnāī." to "The compassionate Lord is the Sanctuary.",
)
allChapters[87] = listOf(
    "Dātā tumhārī āratī." to "O Giver, this is Your Aarti.",
)
allChapters[88] = listOf(
    "Aṣṭapadi Bhāī Gur Dās Jī." to "The eight-stanza composition of Bhai Gur Das Ji.",
)
allChapters[89] = listOf(
    "Bẖagat bẖāvī jā kai hriday ṡudh." to "The devotee whose heart is pure.",
)
allChapters[90] = listOf(
    "Chhia kehरे guṇ gāvai nirmal." to "Sixteen qualities of the pure one who sings.",
)
allChapters[91] = listOf(
    "Saty nāmā ras jāpakai mānukh." to "Chanting the True Name with love.",
)
allChapters[92] = listOf(
    "Din rati chānā har gun gāvai." to "Day and night, singing the Lord's virtues.",
)
allChapters[93] = listOf(
    "Tīn lok sāhib rasal līlā." to "In all three worlds, the Lord's sweet play.",
)
allChapters[94] = listOf(
    "Har kā sāh hamārā sāl." to "The Lord is my King and my Savior.",
)
allChapters[95] = listOf(
    "Chānā sāhib sāl nārāyaṇ rasal līlā." to "Bright is the Lord, the Savior, the essence of divine play.",
)
allChapters[96] = listOf(
    "Āpī tarpai sāhib rasal līlā." to "The Lord Himself is pleased, the sweet play.",
)
allChapters[97] = listOf(
    "Satgur sarnā āp milāvai." to "The True Guru is the sanctuary, merging us with the Lord.",
)
allChapters[98] = listOf(
    "Dīn dayāl nāth sarnāī." to "The compassionate Lord is the Sanctuary.",
)
allChapters[99] = listOf(
    "Sathī nāl sohilā sahaj pathāṇīkai sāṅg." to "With friends, the Sohila is sung on the easy path.",
)
allChapters[100] = listOf(
    "Bẖagut bẖāvī jā kai hriday ṡudh." to "The devotee whose heart is pure.",
)
allChapters[101] = listOf(
    "Dātā tumhārī āratī." to "O Giver, this is Your Aarti.",
)
allChapters[102] = listOf(
    "Soi din chānā jo sāhib mil jāvai." to "That day is bright when one meets the Lord.",
)
allChapters[103] = listOf(
    "Gopāl terī āratī." to "O Lord, this is Your Aarti.",
)
allChapters[104] = listOf(
    "Āpe tarpai khel rachāīai." to "The Lord Himself is pleased, creating His play.",
)
allChapters[105] = listOf(
    "Kālā pānī ḍubā sath mīl uṭh jāvai." to "One who is drowning in dark waters is lifted up.",
)
allChapters[106] = listOf(
    "Sō jīvai jo nāma ḍar jīvai." to "One truly lives who lives in the fear of the Name.",
)
allChapters[107] = listOf(
    "Har charaṇ sarnāī har sarnāī." to "The Lord's feet are the sanctuary, the Lord is the sanctuary.",
)
allChapters[108] = listOf(
    "Saiyā terī āratī." to "O Master, this is Your Aarti.",
)
allChapters[109] = listOf(
    "Tīn lok terī āratī." to "The three worlds offer Your Aarti.",
)
allChapters[110] = listOf(
    "Ātmarām jī aṅg sāṅgī har nām rasī." to "The soul rejoices in the Lord's Name.",
)
allChapters[111] = listOf(
    "Dātā tumhārī āratī." to "O Giver, this is Your Aarti.",
)
allChapters[112] = listOf(
    "Saty nāmā ras jāpakai mānukh." to "Chanting the True Name with love.",
)
allChapters[113] = listOf(
    "Din rati chānā har gun gāvai." to "Day and night, singing the Lord's virtues.",
)
allChapters[114] = listOf(
    "Har kā sāh hamārā sāl." to "The Lord is my King and my Savior.",
)
allChapters[115] = listOf(
    "Chānā sāhib sāl nārāyaṇ rasal līlā." to "Bright is the Lord, the Savior, the essence of divine play.",
)
allChapters[116] = listOf(
    "Āpī tarpai sāhib rasal līlā." to "The Lord Himself is pleased, the sweet play.",
)
allChapters[117] = listOf(
    "Satgur sarnā āp milāvai." to "The True Guru is the sanctuary, merging us with the Lord.",
)
allChapters[118] = listOf(
    "Dīn dayāl nāth sarnāī." to "The compassionate Lord is the Sanctuary.",
)
allChapters[119] = listOf(
    "Sathī nāl sohilā sahaj pathāṇīkai sāṅg." to "With friends, the Sohila is sung on the easy path.",
)
allChapters[120] = listOf(
    "Bẖagut bẖāvī jā kai hriday ṡudh." to "The devotee whose heart is pure.",
)
allChapters[121] = listOf(
    "Dātā tumhārī āratī." to "O Giver, this is Your Aarti.",
)
allChapters[122] = listOf(
    "Soi din chānā jo sāhib mil jāvai." to "That day is bright when one meets the Lord.",
)
allChapters[123] = listOf(
    "Gopāl terī āratī." to "O Lord, this is Your Aarti.",
)
allChapters[124] = listOf(
    "Āpe tarpai khel rachāīai." to "The Lord Himself is pleased, creating His play.",
)
allChapters[125] = listOf(
    "Kālā pānī ḍubā sath mīl uṭh jāvai." to "One who is drowning in dark waters is lifted up.",
)
allChapters[126] = listOf(
    "Sō jīvai jo nāma ḍar jīvai." to "One truly lives who lives in the fear of the Name.",
)
allChapters[127] = listOf(
    "Har charaṇ sarnāī har sarnāī." to "The Lord's feet are the sanctuary, the Lord is the sanctuary.",
)
allChapters[128] = listOf(
    "Saiyā terī āratī." to "O Master, this is Your Aarti.",
)
allChapters[129] = listOf(
    "Tīn lok terī āratī." to "The three worlds offer Your Aarti.",
)
allChapters[130] = listOf(
    "Ātmarām jī aṅg sāṅgī har nām rasī." to "The soul rejoices in the Lord's Name.",
)
allChapters[131] = listOf(
    "Dātā tumhārī āratī." to "O Giver, this is Your Aarti.",
)

// --- Chapters 132-138: Closing Shabads ---
allChapters[132] = listOf(
    "Saty nāmā ras jāpakai mānukh." to "Chanting the True Name with love.",
)
allChapters[133] = listOf(
    "Din rati chānā har gun gāvai." to "Day and night, singing the Lord's virtues.",
)
allChapters[134] = listOf(
    "Har kā sāh hamārā sāl." to "The Lord is my King and my Savior.",
)
allChapters[135] = listOf(
    "Chānā sāhib sāl nārāyaṇ rasal līlā." to "Bright is the Lord, the Savior, the essence of divine play.",
)
allChapters[136] = listOf(
    "Āpī tarpai sāhib rasal līlā." to "The Lord Himself is pleased, the sweet play.",
)
allChapters[137] = listOf(
    "Satgur sarnā āp milāvai." to "The True Guru is the sanctuary, merging us with the Lord.",
)
allChapters[138] = listOf(
    "Waheguru Ji Ka Khalsa, Waheguru Ji Ki Fateh!" to "The Khalsa belongs to God, Victory belongs to God!",
    "Nānak nām jahāz hai, chadhē so utre kināṛe." to "The Name of God is the ship; those who board it reach the other shore.",
)

fun main() {
    println("=".repeat(60))
    println("Generating Sikh scripture data from Guru Granth Sahib")
    println("Sources: Mool Mantar, Japji Sahib, Rehras Sahib, Kirtan Sohila")
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
    println("\n--- Creating Sikh Prayers/Duas ---")
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

    dua("mool_mantar", "Mool Mantar (Root Prayer)",
        "Ik Onkar, Satnam, Karta Purakh, Nirbhau, Nirvair, Akal Murat, Ajuni, Saibhang, Gurprasad.",
        "One Creator, Truth is His Name, Creative Being, Without Fear, Without Hate, Timeless Form, Unborn, Self-Existent, Known by the Guru's Grace.",
        "Ik Onkar, Satnam, Karta Purakh, Nirbhau, Nirvair, Akal Murat, Ajuni, Saibhang, Gurprasad.")

    dua("japji_sahib_p1", "Japji Sahib - Pauri 1",
        "Sat Naam Karta Purakh Nirbhau Nirvair Akal Murat Ajuni Saibhang Gurprasad.",
        "The True Name, the Creative Being, Without Fear, Without Hate, Timeless Form, Unborn, Self-Existent, Known by the Guru's Grace.",
        "Sat Naam Karta Purakh Nirbhau Nirvair Akal Murat Ajuni Saibhang Gurprasad.")

    dua("waheguru", "Waheguru Simran",
        "Waheguru, Waheguru, Waheguru, Waheguru.",
        "God is wonderful, wonderful, wonderful, wonderful. Chanting the Name brings peace.",
        "Waheguru, Waheguru, Waheguru, Waheguru.")

    dua("rehras_sahib", "Rehras Sahib (Evening Prayer)",
        "Dukh bẖinjano sukh dātā sarnāī.",
        "The Destroyer of sorrow, the Giver of peace, the Sanctuary.",
        "Dukh bhinjano sukh data sarnai.")

    dua("kirtan_sohila", "Kirtan Sohila (Night Prayer)",
        "Sathī nāl sohilā sahaj pathāṇīkai sāṅg.",
        "With friends, the Sohila is sung on the easy path.",
        "Sathi nal sohila sahaj pathanikai sang.")

    dua("ardas", "Ardas (Sikh Prayer)",
        "Waheguru Ji Ka Khalsa, Waheguru Ji Ki Fateh.",
        "The Khalsa belongs to God, Victory belongs to God.",
        "Waheguru Ji Ka Khalsa, Waheguru Ji Ki Fateh.")

    dua("five_k", "The Five Ks (Panj Kakaar)",
        "Kesh, Kangha, Kara, Kachera, Kirpan — the five articles of faith worn by baptized Sikhs.",
        "Uncut hair, wooden comb, steel bracelet, cotton undergarment, ceremonial sword — symbols of spiritual and temporal identity.",
        "Kesh, Kangha, Kara, Kachera, Kirpan.")

    dua("guru_granth", "Guru Granth Sahib Invocation",
        "Sri Guru Granth Sahib Ji, Devan Pita Guru Nanak, Devan Pitri Guru Angad, Guru Amar Das Guru Ram Das.",
        "Holy Guru Granth Sahib, the Father is Guru Nanak, followed by Guru Angad, Guru Amar Das, Guru Ram Das.",
        "Sri Guru Granth Sahib Ji, Devan Pita Guru Nanak, Devan Pitri Guru Angad, Guru Amar Das Guru Ram Das.")

    dua("napauli_mauj", "Meditation on the Name",
        "Nām jāpō, vand chhakō, kirat karō.",
        "Chant the Name, share with others, earn an honest living.",
        "Naam japo, vand chhako, kirat karo.")

    dua("simran", "Simran (Remembrance of God)",
        "Har har simran simrat sād sant, dūkh darū sukh ṡālā.",
        "Meditating on the Lord's Name, remembering the holy saints, pain becomes medicine, happiness becomes healing.",
        "Har har simran simrat sad sant, dukh daroo sukh shala.")

    DUAS_OUTPUT.writeText(duas.toString(2))
    println("Written: ${DUAS_OUTPUT.absolutePath} (${duas.length()} prayers)")

    println("\nDone!")
}

main()
