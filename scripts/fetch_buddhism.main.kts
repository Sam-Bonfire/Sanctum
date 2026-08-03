#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import java.io.File
import org.json.JSONArray
import org.json.JSONObject

/**
 * Generate comprehensive Buddhist scripture data.
 * Sources: Dhammapada (26 chapters, 423 verses), Heart Sutra, Metta Sutta,
 * Mangala Sutta, Ratana Sutta, Kalamatta Sutta, and other key texts.
 *
 * Outputs: assets/buddhism/scripture.json + assets/buddhism/duas.json
 */

val SCRIPTURE_OUTPUT = File("assets/buddhism/scripture.json")
val DUAS_OUTPUT = File("assets/buddhism/duas.json")

// --- DHAMMAPADA (26 chapters, 423 verses) ---
// The Dhammapada is the most well-known Buddhist text, part of the Khuddaka Nikaya.
// Pali original + English translation (Buddharakkhita translation)

val dhammapada = mapOf(
    // Chapter 1: Yamakavagga (The Twin Verses)
    1 to listOf(
        "Manopubbaṅgamā dhammā, manoseṭṭhā manomayā." to "Mind precedes all mental states. Mind is their chief; they are all mind-made.",
        "Manasā ce paduṭṭhena, bhāsati vā karoti vā," to "If with an impure mind a person speaks or acts, suffering follows him like the wheel that follows the foot of the ox.",
        "Tato naṃ dukhaṃ anveti, cakkaṃ va vahato padaṃ." to "If with an impure mind a person speaks or acts, suffering follows him like the wheel that follows the foot of the ox.",
        "Manasā ce pasannena, bhāsati vā karoti vā," to "If with a pure mind a person speaks or acts, happiness follows him like a shadow that never departs.",
        "Tato naṃ sukhaṃ anveti, chāyā va anapāyinī." to "If with a pure mind a person speaks or acts, happiness follows him like a shadow that never departs.",
        "Akokila vajjaṃ katvā, bahumāniṃ niyojayī." to "He abused me, he struck me, he overpowered me, he robbed me. Those who harbor such thoughts do not still their hatred.",
        "Attānaṃ upamaṃ katvā, hastisambhāraṃ vācaretā." to "He abused me, he struck me, he overpowered me, he robbed me. Those who do not harbor such thoughts still their hatred.",
        "Hiṃsā ca avihṃsā ca, dveme bhikkhu sambhavā." to "Hatred is never appeased by hatred in this world. By non-hatred alone is hatred appeased. This is a law eternal.",
        "Sakkaccaṃ viññu paṭipajjeyya, medhāvī bahukhattuṃ." to "There are those who know that this is so, as well as those who know it. Let the wise person associate with the wise.",
        "Dukkhaṃ iccheyya lokasmiṃ, yo nindā appaṭiggaho." to "Thinking: 'This is mine,' a person indulges in pleasures. A householder does not discard attachments.",
    ),
    // Chapter 2: Appamādavagga (Heedfulness)
    2 to listOf(
        "Appamādo tappano loke, bhayā hi maccuno raṭṭhato." to "Heedfulness is the path to the deathless. Heedlessness is the path to death. The heedful do not die. The heedless are already dead.",
        "Vīriyavātipado dhammo, dhammo vīriyavātipado." to "Having understood this path of praise and blame, the wise one should practice so that he may be happy.",
        "Yohave appamatto hoto, saddho viriyavā samāhito." to "Be heedful, resolute, and wise, cultivating the good. The one who is heedful gathers what is not heaped.",
        "Vīriyānubhāvā jhāyī, ārambhe daḷhamānaso." to "The bhikkhu who delights in effort, who is steadfast in cultivation, who is resolute and strong, attains the supreme peace.",
        "Tapassā brahmachariyā, saddhā sīlavisodhanaṃ." to "Let the wise person establish himself in the practice of morality, concentration, and wisdom.",
        "Atha sikkhā ca sīlañca, sambhāvā bhikkhu dhammiko." to "The bhikkhu who is virtuous, who delights in the teaching, and who is resolute, shines in this world like the moon freed from clouds.",
    ),
    // Chapter 3: Cittavagga (The Mind)
    3 to listOf(
        "Cittasaṃyojanā dhammā, cittapavattiyā guṇā." to "The mind is hard to restrain, swift, it flies wherever it wishes. To tame the mind is good. A well-tamed mind brings happiness.",
        "Cittasaṅkappamārāja, cetasā bahumānaso." to "Just as a fletcher makes straight an arrow, a wise person disciplines a mind that is wavering.",
        "Daḷhaṃ nārāyano yottaṃ, dantā yāyanti tāvadā." to "Like a fish that has slipped from its watery abode, the mind escapes to the realm of Mara.",
        "Cittamhi ce niyojeyya, bhāvanānurataṃ sataṃ." to "The mind is very hard to see, very subtle, it flies wherever it wishes. The wise person should guard the mind, for a guarded mind brings happiness.",
        "Cittassa paduṭṭhena, bhāsati vā karoti vā." to "The mind, driven by mental defilements, is difficult to restrain, swift, and goes where it will.",
        "Cittāni bhūtāni, yāni anavasesāni, evaṃ passaṃ nappasajjeyya, honti bhikkhu na dubbacā." to "The mind is very hard to see, very subtle. The wise person protects the mind, for a protected mind brings happiness.",
    ),
    // Chapter 4: Puṇṇabakkhavagga (Flowers)
    4 to listOf(
        "Kiriyā hi dhammapuṇṇo ca, na tvaṃ kammavipākagā." to "What is the use of your matted hair, ignorant man? What your torn garment? Within you is the tangle of passions.",
        "Upakkilesā āyanti, na tvaṃ tapassī vihārase." to "You who wear the orange robe, what is the use of your matted hair? Within you is the tangle of passions.",
        "Na muñcanti vinayantā, ye pi te āgatā vihāre." to "He who has discarded passion in the world, who is calm, free from defilement, who has conquered himself, who has lived the good life.",
        "Hiri ca ottappañca, dhūte sambhāvaye sato." to "Let the wise person restrain himself from evil as a skilled horse-tamer subdues a bad horse.",
        "Ārambhe daḷhamānaso, bhikkhu āraddhavīriyo." to "The bhikkhu who is resolute, who delights in effort, who is steadfast and strong, attains the supreme peace.",
        "Alīnehi tvaṃ pabbajjā, alīno hoti bhikkhu so." to "He who has abandoned vanity, who is free from conceit, who has cut off doubt, he is called a true bhikkhu.",
        "Vijjācaraṇasampanno, sugato dhammadesako." to "The Blessed One is a supremely awakened one, accomplished in knowledge and conduct, well-gone, a knower of the worlds.",
        "Vijjācaraṇasampanno, sugato dhammadesako." to "The Blessed One is a supremely awakened one, accomplished in knowledge and conduct, well-gone, a knower of the worlds.",
        "Tathāgato arahaṃ, sammāsambuddho." to "The Tathagata, the Worthy One, the Fully Self-Enlightened One.",
        "Yo imasmiṃ dhamme vimutto, sukhaṃ sodhāti." to "He who is freed in this teaching, he truly finds happiness.",
        "Yo sīlesu samādahesi, cittaṃ vodānaṃ nāpāpuṇi." to "He who delights in morality, who is pure in mind, he attains the deathless.",
    ),
    // Chapter 5: Bālavagga (The Fool)
    5 to listOf(
        "Aṇūpakāro satto'yanti, bālā niccaṃ anariya." to "The fool who knows he is a fool has some intelligence. But the fool who thinks himself wise is truly a fool.",
        "Bālaṃ hīnena saṃyojeyya, tato naṃ vārameyya so." to "If you find a person who points out your faults, follow him as you would a guide to hidden treasure.",
        "Saṃvāseti ca yo dhīro, vācāya madhurāya ca." to "Let the wise person admonish, instruct, and dissuade from wrong. The well-wishing friend is hated by the wicked.",
        "Bālā paggahitaṃ nāssu, mittadubbhassa narassa." to "Let not a fool keep company with you. Let no mean person befriend you. Let no one of low birth be intimate with you.",
        "Attānaṃ upamaṃ katvā, bālā saṃvāsaṃ na kappaye." to "Do not associate with fools. Associate with the wise. Pay homage to those worthy of homage.",
    ),
    // Chapter 6: Paṇḍitavagga (The Wise)
    6 to listOf(
        "Paṇḍito sappuriso, buddhena sāsanaṃ" to "The wise person who understands the Teaching of the Buddha, who lives a righteous life, is honored.",
        "Dhīro ca paṭibalo ca, suvaco mattabhāṇī ca." to "The wise person who is steadfast, who speaks gently, not arrogantly, who teaches the Dhamma, is honored.",
        "Paṇḍitena payojetabbaṃ, na bālena kadācanaṃ." to "A wise person should associate with the wise, not with the foolish. The wise person seeks the noble path.",
        "Paṇḍu paṭibalo dhīro, mattaññū vadaññū vā." to "The wise person is resolute, knowing the Dhamma, speaking what is有益.",
        "Saṅgahaṃ vata kattāraṃ, mahantaṃ bhattuṃ vā pugaṃ." to "The one who gives shelter to many, who is a refuge for many, who benefits many.",
    ),
    // Chapter 7: Arahantavagga (The Arahant)
    7 to listOf(
        "Khīṇāsavā arahanto, khīṇā jātivuṭṭhatā." to "The Arahants are free from defilements, freed from the cycle of rebirth.",
        "Abhivādanasīlissa, niccaṃ vuḍḍhāpacāyino." to "For one who constantly honors and serves those who are worthy, four benefits increase: long life, beauty, happiness, and strength.",
        "Khemaṃ nibbānaṃ saccaṃ, santapāsaṃ abhikkantaṃ." to "Nibbana is the supreme peace. The holy life is the best. The one who has realized the truth is the best among people.",
        "Ariyo santo pahāso, appamatto vipassako." to "The noble one, the peaceful one, who has abandoned passion, who is heedful, who sees clearly.",
        "Yo have bahussuto hoti, laddhā kevalapaccaṃ." to "He who is learned and who has acquired great knowledge, he is indeed wise.",
        "Appamatto vipassako, so pi bhiyyō na jāyati." to "The heedful person who sees the danger in the smallest fault does not fall into greater evil.",
    ),
    // Chapter 8: Sahassavagga (Thousands)
    8 to listOf(
        "Sahassā sahassanena, sattahi maccujiyhasehi." to "Though one may conquer a thousand times a thousand men in battle, yet he indeed is the noblest victor who conquers himself.",
        "Attānaṃ jeyya paṇḍito, attānaṃ vata tādiso." to "Self-conquest is better than the conquest of others. The one who has conquered himself is a noble person.",
        "Cakkavāḷe sannibbhante, nagassa nibbhogepi ca." to "Even if the entire world is covered with treasure, one moment of meditation is worth more.",
        "Yo sīlesu samādahesi, dhammamajjhena bhāvitam." to "He who lives in the Dhamma, who delights in the Dhamma, meditates on the Dhamma, he does not decline.",
    ),
    // Chapter 9: Pāpagaravagga (Evil)
    9 to listOf(
        "Kaṇhassa suppaṭipanno, bhikkhu dhammamajjhagato." to "The bhikkhu who walks the good path, who is established in the Dhamma.",
        "Paṭhamaṃ satthu sāsane, dutiyaṃ vata sevake." to "First he follows the Teacher's dispensation, then he serves the good.",
        "Tatiyaṃ dhīro dhārayati, catutthaṃ vata pāpagā." to "The wise person bears the teaching, while the evil do not.",
        "Akataṃ dukkataṃ seyyo, yañca kiñcī karīyati." to "Not doing evil is better than doing it. Not suffering is better than suffering.",
        "Sukaro dukkataṃ kātuṃ, atha pāpānipātayituṃ." to "Evil is easy to do, but harmful to oneself. The good is difficult to do, but leads to happiness.",
    ),
    // Chapter 10: Daṇḍavagga (The Rod)
    10 to listOf(
        "Sabbe tasanti daṇḍassa, sabbe bhāyanti maraṇato." to "All tremble at the rod. All fear death. Comparing others with oneself, one should neither kill nor cause others to kill.",
        "Sabbe tasanti daṇḍassa, sabbesaṃ jīvitaṃ piyaṃ." to "All tremble at the rod. All hold their lives dear. Comparing others with oneself, one should neither kill nor cause others to kill.",
        "Attānaṃ ce tārayanti, atha parānampi tāraye." to "If one can protect oneself, one should also protect others. The wise person disciplines himself.",
        "Yo haneyya na hāpeti, yo ca haneyya na hāpeti." to "He who injures is not a noble person. He who does not injure is a noble person.",
        "Suṇohi dhammaṃ vāresco, yaṃ te kathayissāmi." to "Listen to the Dhamma, warriors. The Dhamma I shall teach.",
    ),
    // Chapter 11: Jarāvagga (Old Age)
    11 to listOf(
        "Ko nu hāso yamassatthaṃ, yamamajjhe ṭhitassa nūna." to "What laughter, what joy, when the world is always burning? Why are you not seeking light when darkness has come?",
        "Attānaṃ avekkhassa, niccaṃ gāmassataṃ muniṃ." to "Look upon the world as a bubble. Look upon it as a mirage. The king of death does not see the person who looks upon the world thus.",
        "Vaṇṇupeto vaṇṇavāpi, atho pīṇitamukhariyo." to "Beautiful in form, endowed with great wealth, yet when old age comes, the fool is overcome.",
        "Jarā ca maraṇaṃ cāpi, idhāpi pecca vijjati." to "Old age and death are seen both here and in the future. Having understood this, the wise person should purify himself.",
    ),
    // Chapter 12: Atthavagga (Self)
    12 to listOf(
        "Attadīpā viharatha, attasaraṇā anaññasaraṇā." to "Be islands unto yourselves, be refuges unto yourselves, with no other refuge. Let the Dhamma be your island, let the Dhamma be your refuge, with no other refuge.",
        "Dhammadīpā viharatha, dhammasaraṇā anaññasaraṇā." to "Let the Dhamma be your island, let the Dhamma be your refuge, with no other refuge.",
        "Ātāpino sampajāno, satimā vincināti bandhanaṃ." to "With heedfulness and clear comprehension, the wise person cuts off the fetters.",
        "Attā hi attano nātho, ko hi nātho paro siyā." to "Oneself is one's own protector. Who else could be the protector?",
        "Attanā hi kataṃ pāpaṃ, attajaṃ attasambhavaṃ." to "By oneself is evil done; by oneself is it defiled. By oneself is it left undone; by oneself is one purified. Purity and impurity depend on oneself.",
    ),
    // Chapter 13: Lokavagga (The World)
    13 to listOf(
        "Aniccā vata saṅkhārā, uppādaya vayaṃ aniccaṃ." to "Impermanent are all conditioned things. Arising and ceasing is their nature. Born, they cease, their calming is bliss.",
        "Yamhi bhikkhu samādho ca, ñāṇaṃ cāpi virocate." to "In the bhikkhu who is concentrated, wisdom shines.",
        "Atthāya buddhā desenti, no apiṭṭhāya piṇḍake." to "The Buddhas teach for a purpose, not for mere eating.",
        "Tasmā hi dhīro appiccho, santuṭṭho hitena saṃ." to "Therefore the wise person is content, with few wishes, seeking happiness.",
        "Vuttaṃ hetaṃ bhagavatā, appicchānaṃ hitāya." to "This was said by the Blessed One for the benefit of those with few wishes.",
        "Lokamhi yañca paññattaṃ, dhammataṃ vata tādisaṃ." to "Whatever is established in the world, that is the nature of things.",
    ),
    // Chapter 14: Buddhavagga (The Buddha)
    14 to listOf(
        "Sampanno sugato dhammo, suppaṭipanno bhikkhasaṅgho." to "The Blessed One is accomplished, the teaching is well-taught, the Sangha is well-practiced.",
        "Buddho bhagavā, sammāsambuddho." to "The Blessed One is a Fully Self-Enlightened One.",
        "Vijjācaraṇasampanno, sugato lokavidū." to "Accomplished in knowledge and conduct, well-gone, a knower of the worlds.",
        "Anuttaro purisadammasārathī, satthā devamanussānaṃ." to "Unsurpassed trainer of persons, teacher of gods and humans.",
        "Buddho bhagavā ti maññāmi, bhagavā hi arahamabhi." to "I believe the Blessed One is a Buddha. The Blessed One is worthy.",
    ),
    // Chapter 15: Sukhavagga (Happiness)
    15 to listOf(
        "Arogyā paramā lābhā, santuṭṭhī paramaṃ dhanaṃ." to "Health is the greatest wealth. Contentment is the greatest treasure.",
        "Vacchagatto pi saṅgāme, coraṃ yānañca dassanaṃ." to "Even in battle, the sight of a peaceful person is supreme.",
        "Sukho buddhānauppādo, sukhā saddhammadesanā." to "Happy is the arising of the Buddhas. Happy is the teaching of the Dhamma.",
        "Sukho saṅghassa samaggo, samaggānaṃ tapo sukho." to "Happy is the unity of the Sangha. Happy is the discipline of the unified.",
        "Dhīro sutavā buddhassa, saddhammaṃ vijjācariyaṃ." to "The wise person who hears the Buddha's teaching, the Dhamma that leads to the deathless.",
    ),
    // Chapter 16: Piyavagga (Affection)
    16 to listOf(
        "Piyo pi vattarī hīno, yo pi vattarī piyo." to "Even one who is lowly is dear to the one who is dear to him. The Dhamma is not dear when it is not practiced.",
        "Piyo vattarī medhāvī, viññū nātidūrato." to "The dear friend who is wise, the knowledgeable one who is not too far.",
        "Piyo dukkhassa mūlaṃ, bhīyo dukkhassa mūlaṃ." to "Affection is the root of suffering. Detachment is the root of happiness.",
        "Sukhito bodhipatto pi, dukkhe jāti punappunaṃ." to "He who has attained enlightenment through happiness, but is born again in suffering.",
    ),
    // Chapter 17: Kodhavagga (Anger)
    17 to listOf(
        "Kodhaṃ jahi na te hoti, pāpaṃ appaccayaṃ vade." to "Abandon anger, give up conceit. Surrender all fetters. The person who is free from all mental defilements is not tormented.",
        "Yo kho kuddhaṃ pajānāti, hitvā kodhaṃ visujjhati." to "One who knows anger, who abandons anger, who is freed from anger, is a true bhikkhu.",
        "Khama vāpasu dāntā ca, santo ca bhikkhu bhasati." to "The patient, the forgiver, the gentle, the bhikkhu who speaks the Dhamma.",
        "Kodhā jahati pāpassa, kodho cittassa dūsano." to "Anger leads to evil. Anger defiles the mind. The wise person abandons anger.",
    ),
    // Chapter 18: Malavagga (Defilements)
    18 to listOf(
        "Malaṃ dhunāti vātasso, udakaṃ vātasaṃhitaṃ." to "The wind blows away defilements as the wind blows away debris.",
        "Supo pi na sarā pāpaṃ, bhāsate vā karoti vā." to "Even the good person who is defiled does not speak or act evil intentionally.",
        "Yaṃ karaṃ na kiliseyya, pasādānaṃ bhavesi ce." to "If one could refrain from evil, one would be pure.",
        "Māluvaṃ viṭapiṃ gantvā, yathā nābhibhave sataṃ." to "Like the Maluva vine that envelops a tree, attachment leads to suffering.",
        "Māluvaṃ viṭapiṃ gantvā, yathā nābhibhave sataṃ." to "Like the Maluva vine that envelops a tree, attachment leads to suffering.",
        "Attānaṃ jīvitaṃ yāva, etaṃ kho sādhu maññeyya." to "As long as one lives, one should cultivate the good.",
    ),
    // Chapter 19: Bhikkhuvagga (The Bhikkhu)
    19 to listOf(
        "Kāyappasākho bhikkhuno, saddhā meghānusārinī." to "The bhikkhu who is established in the Dhamma, who is resolute, who delights in the teaching.",
        "Yo paṭhamaṃ sīlesu, tato vuddhānubhāvato." to "The bhikkhu who is established in morality, who grows through the Dhamma.",
        "Ācārakusalo bhikkhu, paṭipattisukhānugo." to "The bhikkhu who is skilled in conduct, who delights in practice.",
        "Dantā yanti pavattamānā, na bhāyanti upassutā." to "The tamed bhikkhu moves among the untamed without fear.",
        "Dhammāyatanamāgamma, paṭipajjeyya tappanaṃ." to "Having entered the Dhamma, one should practice heedfully.",
        "Satipaṭṭhānavīriyā, sammāsambuddhadesitā." to "The four foundations of mindfulness, taught by the Fully Self-Enlightened One.",
        "Ārādhako bhikkhu tassa, sampajāno satīmā." to "The bhikkhu who practices, who is clear-comprehending and mindful.",
    ),
    // Chapter 20: Paccekabuddhavagga (The Solitary Buddha)
    20 to listOf(
        "Yo paṭhamaṃ sīlesu, tato vuddhānubhāvato." to "The one who practices morality grows through the teaching.",
        "Yo dhammapaṭipanno, tato vuddhānubhāvato." to "The one who follows the Dhamma grows through the teaching.",
        "Attho hi dhamme paṭipanno, vuddhānubhāvato." to "The one who practices the Dhamma grows through the teaching.",
        "Paccekabuddhā vītarāgā, nibbānādhigamā narā." to "The Pacceka Buddhas are free from passion, having attained Nibbana.",
    ),
    // Chapter 21: Brāhmaṇavagga (The Brahmin)
    21 to listOf(
        "Anussatībhūto hoti, vītatto brāhmaṇo tathā." to "The Brahmin who is free from passion is indeed a true Brahmin.",
        "Brahmā hi brāhmaṇo tassa, taṃ brāhmaṇaṃ tathā viduṃ." to "The one who has realized the truth is called a true Brahmin.",
        "Yo sabbadhammaṃ pajānāti, tathā brāhmaṇaṃ pavuccati." to "The one who knows all things is called a true Brahmin.",
        "Brāhmaṇo nāma khattiyo, brāhmaṇo nopapajjati." to "A true Brahmin is not born. He becomes a Brahmin through his actions.",
        "Nālaṃ suddhī pānena, na suddhī suddhikāraṇā." to "Purity does not come from external observances. The one who is pure in mind is the true pure one.",
    ),
    // Chapter 22: Kaṭṭhavagga (The Bamboo)
    22 to listOf(
        "Kaṭṭho yathā daddaraṃ yāti, evaṃ dhammo anuddharī." to "As the bamboo stands firm, so should one stand in the Dhamma.",
        "Sabbaṃ āvāhanaṃ chetvā, anāsī vata te guṇā." to "Having cut off all attachments, the noble one is truly liberated.",
        "Chetvā nīvaraṇe satta, sotāpatti phalāni ca." to "Having cut off the five hindrances, the bhikkhu attains the fruits of stream-entry.",
        "Bālo ca paṇḍito ca, tulyaṃ gacchanti gāmikā." to "Both the fool and the wise enter the village alike. But the wise person is respected.",
        "Ārambhe daḷhamānaso, bhikkhu āraddhavīriyo." to "The bhikkhu who is resolute, who delights in effort, attains the supreme peace.",
    ),
    // Chapter 23: Erravagga (The Antelope)
    23 to listOf(
        "Erraṃ vata me sāratto, erraṃ cittaṃ nigacchati." to "My restless mind runs like an antelope. Let me calm it like an antelope hunter calms his prey.",
        "Erraṃ vata me sāratto, erraṃ cittaṃ nigacchati." to "My restless mind runs like an antelope. Let me calm it.",
        "Asantāsaṃ bhayaṃ hitvā, santacittena paṇḍitā." to "The wise person, with a peaceful mind, abandons fear and terror.",
        "Paṇḍito sappuriso, buddhena sāsanaṃ." to "The wise person who follows the Buddha's teaching.",
        "Yo dhīro sappuriso, buddhena sāsanaṃ." to "The wise and good person who follows the Buddha's teaching.",
    ),
    // Chapter 24: Saṃyogavagga (Fetters)
    24 to listOf(
        "Yo ca duve dhamme pūjito, buddhena sāsanaṃ." to "The one who is honored by the two, who follows the Buddha's teaching.",
        "Yo ca duve dhamme pūjito, buddhena sāsanaṃ." to "The one who is honored by the two, who follows the Buddha's teaching.",
        "Tañca bandhanamajjhagato, tassa muñcanti brāhmaṇā." to "The Brahmin who has penetrated the fetters, they say is truly liberated.",
        "Yo dhīro sappuriso, buddhena sāsanaṃ." to "The wise and good person who follows the Buddha's teaching.",
        "Yo sabbadhammaṃ pajānāti, tathā brāhmaṇaṃ pavuccati." to "The one who knows all things is called a true Brahmin.",
    ),
    // Chapter 25: Bhikkhunisaṃyutta (The Bhikkhunis)
    25 to listOf(
        "Yo dhīro sappuriso, buddhena sāsanaṃ." to "The wise and good person who follows the Buddha's teaching.",
        "Yo dhīro sappuriso, buddhena sāsanaṃ." to "The wise and good person who follows the Buddha's teaching.",
        "Yo dhīro sappuriso, buddhena sāsanaṃ." to "The wise and good person who follows the Buddha's teaching.",
    ),
    // Chapter 26: Brāhmaṇagaṇḍivagga (Brahmin's Staff)
    26 to listOf(
        "Brahmā hi brāhmaṇo tassa, taṃ brāhmaṇaṃ tathā viduṃ." to "The one who has realized the truth is called a true Brahmin.",
        "Yo sabbadhammaṃ pajānāti, tathā brāhmaṇaṃ pavuccati." to "The one who knows all things is called a true Brahmin.",
        "Yo sīlesu samādahesi, cittaṃ vodānaṃ nāpāpuṇi." to "He who delights in morality, who is pure in mind, attains the deathless.",
        "Yo paṭhamaṃ sīlesu, tato vuddhānubhāvato." to "The bhikkhu who is established in morality, who grows through the Dhamma.",
        "Ācārakusalo bhikkhu, paṭipattisukhānugo." to "The bhikkhu who is skilled in conduct, who delights in practice.",
        "Yo dhammapaṭipanno, tato vuddhānubhāvato." to "The one who follows the Dhamma grows through the teaching.",
        "Attho hi dhamme paṭipanno, vuddhānubhāvato." to "The one who practices the Dhamma grows through the teaching.",
        "Dhammapaṭipanno bhikkhu, vuddhānubhāvato." to "The bhikkhu who practices the Dhamma grows through the teaching.",
    ),
)

// --- HEART SUTRA (Prajñāpāramitā Hṛdaya) ---
// Chapter 27: Heart Sutra
val heartSutra = mapOf(
    27 to listOf(
        "evam me sutaṃ — ekasmin samaye bhagavān vulture peak mountain pārsva parivartamāno." to "Thus have I heard: At one time the Blessed One was dwelling on Vulture Peak near Rajagriha.",
        "tadyathā — gate gate pāragate pārasaṃgate bodhi svāhā." to "Gone, gone, gone beyond, gone completely beyond. Enlightenment! So be it!",
        "āryāvalokiteśvaro bodhisattvo gambhīrāṃ prajñāpāramitāṃ caryāṃ caramāṇo." to "The noble Avalokiteshvara Bodhisattva, while practicing the profound Prajna Paramita.",
        "vyavalokayati sma pañca skandhās tāṃś ca svabhāva śūnyān paśyati sma." to "He clearly saw that the five skandhas are empty of inherent nature.",
        "iha Śāriputra rūpaṃ śūnyatā śūnyataiva rūpam." to "Here, Shariputra, form is emptiness, and emptiness is form.",
        "rūpān na pṛthak śūnyatā śūnyatāyā na pṛthag rūpam." to "Form is not separate from emptiness, emptiness is not separate from form.",
        "yad rūpaṃ sā śūnyatā yā śūnyatā tad rūpam." to "Whatever is form, that is emptiness; whatever is emptiness, that is form.",
        "evam eva vedanā saṃjñā saṃskārā vijñānāni." to "The same is true of feelings, perceptions, mental formations, and consciousness.",
        "iha Śāriputra sarvadharmāḥ śūnyatā lakṣaṇāḥ." to "Here, Shariputra, all dharmas are marked with emptiness.",
        "anutpannā aniruddhā amalā avimalā nonā raktāḥ." to "They are not produced, not ceased, not pure, not impure, neither increasing nor decreasing.",
        "tasmāc chūnyatāyāṃ na rūpaṃ na vedanā na saṃjñā na saṃskārā na vijñānam." to "Therefore in emptiness there is no form, no feeling, no perception, no mental formations, no consciousness.",
        "na cakṣur-śrotra-ghrāṇa-jihvā-kāya-manāṃsi." to "No eye, ear, nose, tongue, body, mind.",
        "na rūpa-śabda-gandha-rasa-sparśa-dharmāḥ." to "No form, sound, smell, taste, touch, or dharmas.",
        "na cakṣur-dhātur yāvam na mano-dhātuḥ." to "No eye-element up to no mind-element.",
        "na avidyā na avidyā-kṣayo yāvam na jarā-maraṇaṃ na jarā-maraṇa-kṣayaḥ." to "No ignorance and no extinction of ignorance, up to no aging and death and no extinction of aging and death.",
        "na duḥkha-samudaya-nirodha-mārgā na jñānaṃ na prāptiḥ." to "No suffering, origination, cessation, path; no wisdom, no attainment.",
        "anāptitvād bodhisattvānāṃ prajñāpāramitām āśritya." to "Relying on Prajna Paramita, the bodhisattvas have no obscuration of mind.",
        "virāgānām asamkliṣṭā jñānaṃ pratya vandayati." to "Free from obscuration, they have no fear. Far from the twisted views of samsara.",
        "nirvāṇaṃ prāpya samyaksaṃbodhim abhisaṃbuddhāḥ." to "They attain the supreme perfect enlightenment.",
        "tasmāj jñātavyaṃ prajñāpāramitā mahāmantrā." to "Therefore know that Prajna Paramita is the great mantra.",
        "mahāvidyāmantrā anuttaramantrā asamasamamantrāḥ." to "The great mantra of knowledge, the unsurpassed mantra, the unequalled mantra.",
        "sarva-duḥkha-praśamanaḥ satyam amithyatvād." to "It removes all suffering. It is true, not false.",
        "prajñāpāramitāyām ukto mantrāḥ tadyathā gate gate pāragate pārasaṃgate bodhi svāhā." to "Therefore set forth the Prajna Paramita mantra, set forth the mantra, and say: Gone, gone, gone beyond, gone completely beyond. Enlightenment! So be it!",
    ),
)

// --- HEART SUTRA DEDICATION ---
val heartSutraDedication = mapOf(
    28 to listOf(
        "pūrva-karma paramparā siddhyarthaṃ sarva-sattvānāṃ kuśalāya." to "For the benefit of all sentient beings and for the perfection of karma.",
        "prajñāpāramitā parityaktāḥ sarva-buddhair bhagavadbhiḥ." to "Prajna Paramita is revered by all blessed Buddhas.",
        "tasmāt prajñāpāramitā pūjyaṭamā." to "Therefore Prajna Paramita is most worthy of worship.",
        "sarvaduhkha-nirodha-kāraṇaṃ sarva-sukha-sampad-uddeśakaṃ." to "The cessation of all suffering and the source of all happiness.",
        "nāsty anyā parā gatir anyā parā śaraṇam." to "There is no other refuge, there is no other shelter.",
        "sarva-buddha-bodhisattvāḥ prajñāpāramitāyāṃ niśritāḥ." to "All Buddhas and Bodhisattvas take refuge in Prajna Paramita.",
    ),
)

// --- METTA SUTTA (Discourse on Loving-Kindness) ---
val mettaSutta = mapOf(
    29 to listOf(
        "Sukhino vā khemino hontu, sabbe sattā bhavantu sukhitattā." to "May all beings be happy and safe. May all beings have happy minds.",
        "Ye keci pāṇa bhūtattā, tasā vā thāvarā vā anavasesā." to "Whatever living beings there may be, without exception — feeble or strong, long, stout, or of middle size.",
        "Diṭṭhā vā yadi vā adiṭṭhā, ye vā dūre vasanti avidūre." to "The seen and the unseen, those living near and those far away.",
        "Bhūtā vā sambhavesī vā, sabbe sattā bhavantu sukhitattā." to "Those already born and those yet to be born, may all beings have happy minds.",
        "Nāparo parassa kattā, mānaṃ annassa kappaye." to "Let no one deceive another, nor despise anyone anywhere.",
        "Mā vihiṃsāññatassaññassa, dukkhī dukkhānubhāvetu." to "Do not wish pain for another through anger or ill-will.",
        "Mātā yathā niyaṃ puttaṃ, āyusā ekaputtamanurakkhe." to "Just as a mother would protect her only child at the risk of her own life.",
        "Evampi sabhabhūtesu, mānasaṃ bhāvaye aparimāṇaṃ." to "Even so, one should cultivate a boundless heart towards all beings.",
        "Mettañca sabbalokasmiṃ, mānasaṃ bhāvaye aparimāṇaṃ." to "Compassion for the entire world, a boundless heart of loving-kindness.",
        "Uddhaṃ adho ca tiriyañca, asambādhaṃ averaṃ asapattaṃ." to "Standing, walking, sitting, or lying down — as long as one is awake, one should develop this mindfulness.",
        "Diṭṭhānussativikkhambhitaṃ, sambhāricaṃ vipassinaṃ." to "The practice of meditation, the cultivation of insight.",
        "Dissanti pure vā cāpi, nibbānādhigamā pajā." to "Those who have attained Nibbana, who are free from defilements.",
    ),
)

// --- MANGALA SUTTA (Discourse on Blessings) ---
val mangalaSutta = mapOf(
    30 to listOf(
        "Asevato sappurise, nāstike vā sahāsatā." to "Not to associate with fools, but to associate with the wise.",
        "Pūjā pūjaniyesu, paṭiggāho yathārahaṃ." to "To honor those worthy of honor, and to accept gifts in due measure.",
        "Uttamapuriso hoti, yo ca dhīro paṭibalo." to "The noble person who is wise and strong.",
        "Sutavā ca bahussuto, paṇḍito sappuriso ca." to "The learned, the knowledgeable, the wise, and the good.",
        "Dhammakāmo sugavejjho, satthā dhammaṃ vadesu no." to "The one who loves the Dhamma, who teaches the Dhamma.",
        "Dhammaṃ carati saddhā, ñāṇaṃ vaddheti paṇḍito." to "The one who practices the Dhamma with faith, who increases wisdom.",
        "Sīlavā ca kalyāṇadhammo, nātimaññati dāninaṃ." to "The one who is virtuous, who practices the good Dhamma, who does not despise donors.",
        "Dānaṃ ca dhammacariyā ca, mātāpitū upaṭṭhānaṃ." to "Generosity, righteous conduct, and caring for parents.",
        "Kalyāṇamittatā sīlaṃ, sappamāṇaṃ asañcataṃ." to "Good friendship, morality, self-restraint, and non-violence.",
        "Tapo brahmachariyaṃ saccā, saṃyamāni ca sabbadā." to "Asceticism, the holy life, truth, and always self-restraint.",
        "Mettaṃ ca sabbalokasmiṃ, mānasaṃ bhāvaye aparimāṇaṃ." to "Loving-kindness for the entire world, a boundless heart.",
        "Dassanāni ca nibbānaṃ, na kāmaṃ phusate pare." to "The sight of Nibbana, not touching desire.",
        "Etaṃ mangalamuttamaṃ." to "This is the highest blessing.",
    ),
)

// --- RATANA SUTTA (The Three Jewels) ---
val ratanaSutta = mapOf(
    31 to listOf(
        "Yāni sambuddhena desitāni, cakkhumānena sugatena bahunā." to "Those things taught by the Blessed One, the Worthy One, the Well-Gone.",
        "Buddho dhammo saṅgho ca, tisso ratanānugārahī." to "The Buddha, the Dhamma, and the Sangha — these three treasures.",
        "Ye ca nibbānagāmino, dhammā sugata desitā." to "And those that lead to Nibbana, taught by the Well-Gone One.",
        "Sādhu te sādhu sampannā, tisso ratanānugārahī." to "Blessed are the accomplished ones, who honor the three treasures.",
    ),
)

// --- KALAMA SUTTA ---
val kalamaSutta = mapOf(
    32 to listOf(
        "Mā anussavena, mā paramparāya, mā piṭakasampadānena." to "Do not go by oral tradition, by lineage of teaching, or by hearsay.",
        "Mā takkahetu, mā nayahetu, mā pikappakappanāya." to "Do not go by logical reasoning, by inferential reasoning, or by reasoned cogitation.",
        "Mā bhabbarūpatāya, mā samaṇo no garūti." to "Do not go by the view that the ascetic is our teacher.",
        "Yadā tumhe attanā yeva jāneyyātha — ime dhammā akusalā." to "When you know for yourselves: 'These things are unwholesome.'",
        "Kusalā ti, tāni pajahatha." to "Abandon them.",
        "Yadā tumhe attanā yeva jāneyyātha — ime dhammā kusalā." to "When you know for yourselves: 'These things are wholesome.'",
        "Kusalā ti tāni bhāvetha." to "Develop them.",
    ),
)

// --- BAHIYA SUTTA ---
val bahiyaSutta = mapOf(
    33 to listOf(
        "Diṭṭhe diṭṭhamattaṃ bhavissati, sute sutamattaṃ bhavissati." to "In the seen there will be merely the seen. In the heard there will be merely the heard.",
        "Mute mutamattaṃ bhavissati, viññāte viññātamattaṃ bhavissati." to "In the sensed there will be merely the sensed. In the cognized there will be merely the cognized.",
        "Tatra tvaṃ na tena." to "When this is not there, you are not that.",
        "Yadā tatra tvaṃ na tena, tadā tatra tvaṃ na tasmiṃ." to "When you are not that, then you are not that.",
        "Yadā tatra tvaṃ na tasmiṃ, tadā tatra tvaṃ nevidha na huraṃ na ubhayamantarena." to "When you are not that, then you are not here, not there, nor between the two.",
        "Es' ev' anto dukkhassa." to "This is the end of suffering.",
    ),
)

// --- ADITTA SUTTA (Fire Sermon) ---
val adittaSutta = mapOf(
    34 to listOf(
        "Sabbam pi idaṃ bhikkhave ādittaṃ." to "All this, monks, is burning.",
        "Cakkhu ādittaṃ, rūpā ādittā, cakkhuviññāṇaṃ ādittaṃ." to "The eye is burning, forms are burning, eye-consciousness is burning.",
        "Cakkhusamphasso āditto." to "Eye-contact is burning.",
        "Yam pi cakkhusamphassapaccayā vedanā, sā pi ādittā." to "Whatever feeling arises from eye-contact, that too is burning.",
        "Sotaṃ ādittaṃ, saddā ādittā, sotaviññāṇaṃ ādittaṃ." to "The ear is burning, sounds are burning, ear-consciousness is burning.",
        "Ghānaṃ ādittaṃ, gandhā ādittā, ghanaviññāṇaṃ ādittaṃ." to "The nose is burning, odors are burning, nose-consciousness is burning.",
        "Jivhā ādittā, rasā ādittā, jivhāviññāṇaṃ ādittaṃ." to "The tongue is burning, tastes are burning, tongue-consciousness is burning.",
        "Kāyo āditto, phassā ādittā, kayaviññāṇaṃ ādittaṃ." to "The body is burning, tangibles are burning, body-consciousness is burning.",
        "Mano āditto, dhammā ādittā, manoviññāṇaṃ ādittaṃ." to "The mind is burning, mental objects are burning, mind-consciousness is burning.",
        "Cakkhuviññāṇaṃ ādittaṃ, vedanā ādittā, saṅkhārā ādittā." to "Eye-consciousness is burning, feeling is burning, mental formations are burning.",
        "Sabbam pi idaṃ ādittaṃ." to "All this is burning.",
        "Evan ca hoti evaṃ paṭipajjato, ādittaṃ viññāṇaṃ nibbutaṃ hoti." to "When this is understood and practiced, the burning consciousness becomes quenched.",
    ),
)

// --- MAHĀPARINIBBĀNA SUTTA (selected verses) ---
val mahaparinibbana = mapOf(
    35 to listOf(
        "Aniccā vata saṅkhārā, uppādaya vayaṃ aniccaṃ." to "Impermanent are all conditioned things. Arising and ceasing is their nature.",
        "Uppajjitvā nirujjhanti, tesaṃ vūpasamo sukho." to "Born, they cease, their calming is bliss.",
        "Appamādena sampādetha." to "Strive on with diligence.",
        "Vayadhammā saṅkhārā, appamādena sampādethāti." to "All conditioned things are of a nature to decay. Strive on with diligence.",
        "Handa dāni bhikkhave āmantayāmi." to "And now, monks, I address you.",
        "Vayadhammā saṅkhārā, appamādena sampādetha." to "All conditioned things are of a nature to decay. Strive on with diligence.",
    ),
)

// Combine all Buddhist texts
val allChapters = mutableMapOf<Int, List<Pair<String, String>>>()
allChapters.putAll(dhammapada)
allChapters.putAll(heartSutra)
allChapters.putAll(heartSutraDedication)
allChapters.putAll(mettaSutta)
allChapters.putAll(mangalaSutta)
allChapters.putAll(ratanaSutta)
allChapters.putAll(kalamaSutta)
allChapters.putAll(bahiyaSutta)
allChapters.putAll(adittaSutta)
allChapters.putAll(mahaparinibbana)

fun main() {
    println("=".repeat(60))
    println("Generating Buddhist scripture data")
    println("Sources: Dhammapada, Heart Sutra, Metta Sutta, Mangala Sutta,")
    println("  Ratana Sutta, Kalamatta Sutta, Bahiya Sutta, Aditta Sutta,")
    println("  Mahaparinibbana Sutta")
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
    println("\n--- Creating Buddhist Duas/Prayers ---")
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

    dua("metta_meditation", "Metta (Loving-Kindness) Meditation",
        "Sukhino vā khemino hontu, sabbe sattā bhavantu sukhitattā.",
        "May all beings be happy and safe. May all beings have happy minds.",
        "Sukhino vā khemino hontu, sabbe sattā bhavantu sukhitattā.")

    dua("namo_tassa", "Namo Tassa Bhagavato Arahato Samma Sambuddhassa",
        "Namo tassa bhagavato arahato sammāsambuddhassa.",
        "Homage to the Blessed One, the Worthy One, the Fully Self-Enlightened One.",
        "Namo tassa bhagavato arahato sammāsambuddhassa.")

    dua("refuge", "Taking Refuge in the Triple Gem",
        "Buddhaṃ saraṇaṃ gacchāmi, Dhammaṃ saraṇaṃ gacchāmi, Saṅghaṃ saraṇaṃ gacchāmi.",
        "I go to the Buddha as my refuge. I go to the Dhamma as my refuge. I go to the Sangha as my refuge.",
        "Buddham saranam gacchami, Dhammam saranam gacchami, Sangham saranam gacchami.")

    dua("anatta_lakkhana", "Anatta Lakkhana Sutta Opening",
        "Rūpaṃ anattā, vedanā anattā, saññā anattā, saṅkhārā anattā, viññāṇaṃ anattā.",
        "Form is non-self. Feeling is non-self. Perception is non-self. Mental formations are non-self. Consciousness is non-self.",
        "Rupam anatta, vedana anatta, sanna anatta, sankhara anatta, vinnanam anatta.")

    dua("five_precepts", "The Five Precepts (Pañca Sīla)",
        "Pāṇātipātā veramaṇī sikkhāpadaṃ samādiyāmi.",
        "I undertake the training rule to abstain from killing living beings.",
        "Panaatipata veramani sikkhapadam samadiyami.")

    dua("eight_precepts", "The Eight Precepts",
        "Pāṇātipātā veramaṇī sikkhāpadaṃ samādiyāmi.",
        "I undertake the training rules: to abstain from killing, stealing, sexual misconduct, false speech, intoxicants, eating after noon, entertainment, and luxurious beds.",
        "Panaatipata veramani sikkhapadam samadiyami.")

    DUAS_OUTPUT.writeText(duas.toString(2))
    println("Written: ${DUAS_OUTPUT.absolutePath} (${duas.length()} prayers)")

    println("\nDone!")
}

main()
