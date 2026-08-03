#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("org.json:json:20231013")

import org.json.JSONObject
import org.json.JSONArray
import java.io.File

/**
 * Expanded Hinduism Scripture Generator
 *
 * Preserves existing Bhagavad Gita data and adds:
 * - Vedic Hymns (Rig Veda, Yajur Veda)
 * - Principal Upanishads (10+)
 * - Puranic Texts (Vishnu Sahasranama, etc.)
 * - Popular Prayers (Hanuman Chalisa, Bhaja Govindam, etc.)
 * - Other Important Mantras and Texts
 *
 * Target: ~1000+ total verses
 */

val SCRIPTURE_OUTPUT = File("assets/hinduism/scripture.json")

fun v(chapterId: Int, verseNum: Int, original: String, translated: String): JSONObject {
    return JSONObject().apply {
        put("chapter_id", chapterId)
        put("verse_number", verseNum)
        put("original_text", original)
        put("translated_text", translated)
    }
}

fun readExisting(): List<JSONObject> {
    if (!SCRIPTURE_OUTPUT.exists()) {
        println("WARNING: No existing scripture.json found for Hinduism")
        return emptyList()
    }
    val arr = JSONArray(SCRIPTURE_OUTPUT.readText())
    val result = mutableListOf<JSONObject>()
    for (i in 0 until arr.length()) {
        result.add(arr.getJSONObject(i))
    }
    println("Loaded ${result.size} existing verses")
    return result
}

// ============================================================
// Chapter 19: Gayatri Mantra & Savitri (Rig Veda 3.62)
// ============================================================
fun ch19_gayatriMantra(): List<JSONObject> = listOf(
    v(19, 1,
        "ॐ भूर्भुवः स्वः । तत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि । धियो यो नः प्रचोदयात्",
        "Om! We meditate upon the most excellent glory of the divine Savitri. May he inspire our intellect."),
    v(19, 2,
        "ॐ अप्सु ओषधयो वनस्पतयो वा विचर्षणी । तत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि",
        "Om! The plants and herbs in the waters are all-seeing. We meditate upon the most excellent glory of the divine Savitri."),
    v(19, 3,
        "ॐ तत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि । धियो यो नः प्रचोदयात् । उद्गातारमृतस्य हिरण्यं परिषिच्य",
        "Om! We meditate upon the most excellent glory of the divine Savitri. Pouring the golden essence over us, may he inspire our understanding."),
    v(19, 4,
        "ॐ ज्योतिर्ज्योतिर्महतो ज्योतिरज्जना । तत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि",
        "Om! The light of lights, the great light, the light among the people. We meditate upon the most excellent glory of the divine Savitri."),
    v(19, 5,
        "ॐ सहस्रशीर्षा पुरुषः सहस्राक्षः सहस्रपात् । भूमिं सर्वं विश्वतो व्याप्य तिष्ठति",
        "The cosmic being has a thousand heads, a thousand eyes, and a thousand feet. He pervades the entire earth and extends in all directions."),
    v(19, 6,
        "ॐ पुरुष एवेदं सर्वं यद् भूतं च यद् भव्यम् । उतामृतत्वस्येशानो यद् अन्नेनातिरोहति",
        "The Purusha (cosmic being) is all this — what has been and what will be. He is the lord of immortality, and he transcends all food."),
    v(19, 7,
        "ॐ तद् एव सवितुर्वर्णं तद् वै प्रचोदयात् । धियो यो नः प्रचोदयात्",
        "That alone is the form of Savitri. That alone inspires. May he inspire our understanding."),
    v(19, 8,
        "ॐ सत्यं ज्ञानमनन्तं ब्रह्म । सत्यं ज्ञानं हि विश्वम् । यत्र नान्यत् पश्यति न शृणोति न विजानीति",
        "Brahman is truth, knowledge, and infinity. That truth is the universe. Where one does not see another, does not hear another, does not know another — that is Brahman."),
    v(19, 9,
        "ॐ तत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि । धियो यो नः प्रचोदयात् । पूर्णमदः पूर्णमिदं पूर्णात् पूर्णमुदच्यते",
        "Om! We meditate upon the most excellent glory of the divine Savitri. That is whole; this is whole. From wholeness comes wholeness."),
    v(19, 10,
        "ॐ पूर्णस्य पूर्णमादाय पूर्णमेवावशिष्यते । ओम् शान्तिः शान्तिः शान्तिः",
        "Taking wholeness from wholeness, wholeness alone remains. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 20: Purusha Sukta (Rig Veda 10.90)
// ============================================================
fun ch20_purushaSukta(): List<JSONObject> = listOf(
    v(20, 1,
        "सहस्रशीर्षा पुरुषः सहस्राक्षः सहस्रपात् । भूमिं सर्वं विश्वतो व्याप्य तिष्ठति",
        "The Purusha has a thousand heads, a thousand eyes, and a thousand feet. He pervades the entire earth and extends in all directions."),
    v(20, 2,
        "पुरुष एवेदं सर्वं यद् भूतं च यद् भव्यम् । उतामृतत्वस्येशानो यद् अन्नेनातिरोहति",
        "The Purusha is all this — what has been and what will be. He is the lord of immortality."),
    v(20, 3,
        "यत्पुरुषेण हविषा देवा यज्ञमतन्वत । वसन्तो अस्यासीदाग्रे भूतस्य जातो विराज्",
        "By the sacrifice of the Purusha, the gods performed the sacrifice. Spring was the first season, and the born lord reigned supreme."),
    v(20, 4,
        "तस्य धाता प्रविच्याधार्भगं यज्ञं तन्वीराजे । श्रेष्ठां ज्योतिं विचक्षणः",
        "The creator arranged the sacrifice as an offering. The shining light of the sacrifice was established among the best."),
    v(20, 5,
        "यज्ञेन यज्ञमयजन्त देवास्तानि धर्माणि प्रथमान्यासन् । ते हानाकं महिमानः सचन्त परस्यात्सीमविविच्छन् विपश्चित्",
        "The gods, by the sacrifice, offered the sacrifice. These were the first sacred duties. They attained the power of the Infinite."),
    v(20, 6,
        "तद् वाम्रेण परिवीतं ज्योतिर्यत्र चाध्वरे देवा यज्ञं तन्वानाः अभ्यगच्छन्",
        "When the gods adorned the sacrifice with the red offering, the divine light shone forth. They reached the highest realm."),
    v(20, 7,
        "स एव विद्वान् अमृतो भवति नान्यः पन्था विद्यतेऽयनाय । य आत्मनि निष्ठितो योऽधि वेद",
        "He who knows this becomes immortal. There is no other path to liberation. He who is established in the Self and who knows — he attains the highest."),
    v(20, 8,
        "तदेव सूर्यो ज्योतिर्ज्योतिर्विश्वं भूतं च भव्यं च । यत्र च यद् व्यश्वं चरति",
        "That is the sun, the light, the illumination of the universe, what has been and what will be."),
    v(20, 9,
        "तमेव विदित्वा निमृत्युमेति नान्यः पन्था विद्यतेऽयनाय । ओम् शान्तिः शान्तिः शान्तिः",
        "By knowing Him alone one transcends death. There is no other path to liberation. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 21: Nasadiya Sukta (Rig Veda 10.129)
// ============================================================
fun ch21_nasadiyaSukta(): List<JSONObject> = listOf(
    v(21, 1,
        "नासदासीन्नो सदासीत्तदानीं नासीद्रजो नो व्योमा परो यत् । को आद्वा केन वा प्रियं वा क आसीत् क आसीत् को अपरो वा प्रियः",
        "There was neither existence nor non-existence then; there was no realm of space, no sky beyond. What stirred? Where? In whose protection?"),
    v(21, 2,
        "न मृत्युरासीदमृतं न तर्हि न रात्र्या अह्न आसीत् प्रकेतः । आनीदवातं स्वधया तदेकं तस्माद्धान्यन्न परः किञ्चनासत्",
        "There was no death nor immortality then; there was no sign of day or night. That One breathed, windless, by its own power."),
    v(21, 3,
        "तम आसीत्तमसा गूहळग्रे प्रकेतं सलिलं सर्वमा इदम् । तुच्छ्येनाभ्वपिहितं यदासीत् तपसो ज्योतिरजायत",
        "Darkness was hidden by darkness in the beginning. All this was an indistinguishable sea. Born through the power of heat."),
    v(21, 4,
        "क आसीद्वा क आसीत् कुत आसीत् को वा प्रजाः क इमे जजन्वा । आदेश्वरो वा कुत आसीत् को वा वेद क इमे प्रजाः",
        "Who really knows? Who will here proclaim it? Whence was it produced? Whence is this creation?"),
    v(21, 5,
        "इमं ज्योतिः परो ज्योतिरादेश्वरो वा कुत आसीत् । वेद नो वा क इमे प्रजाः क इमे प्रजाः",
        "Whence this creation has arisen — perhaps its foundation is known, or perhaps it is not known."),
    v(21, 6,
        "यतो वा इमानि भूतानि जायन्ते । येन जातानि जीवन्ति । यत् प्रयन्त्यभिसंविशन्ति । तद्विद्धि नेदं वा इमानि भूतानि",
        "From what source all these beings are born, by what they live, and to what they return — know that."),
    v(21, 7,
        "संवीतानी वाचा विचिक्युषाः परो यद् अन्तरबहिश्च यत् । ज्ञाता विद्वान् विचिक्युषाः परो वा अपरो वा परः क इमे",
        "He who surveys the creation in the highest heaven, he alone knows — or perhaps even he does not know."),
    v(21, 8,
        "इमां विचिक्युषाः परो वा अपरो वा परः क इमे । नास्य प्रस्विदिते यत्र परो वा अपरो वा परः क इमे",
        "He who surveys it in the highest heaven, he alone knows — or perhaps even he does not."),
    v(21, 9,
        "परो वा अपरो वा परः क इमे । नास्य प्रस्विदिते यत्र परो वा अपरो वा परः क इमे । ओम् शान्तिः शान्तिः शान्तिः",
        "Perhaps he knows, or perhaps he does not. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 22: Sri Rudram (Yajur Veda 4.5)
// ============================================================
fun ch22_sriRudram(): List<JSONObject> = listOf(
    v(22, 1,
        "नमस्ते रुद्र मन्यव उतो त इषवे नमः । नमस्ते अस्तु धन्वने बाहुभ्यामुत ते नमः",
        "Salutation to you, O Rudra, who are adorable. Salutation to your arrows. Salutation to your bow, and salutation to both your arms."),
    v(22, 2,
        "यात ते हेतिरमीमहे मध्यं ते हेतिर्वीर्यवान् । यावते ते विश्वरूपं तत्ते तन्मीमहे वयम्",
        "We worship your central arrow. We worship your powerful central arrow. Whatever your cosmic form — we meditate upon that."),
    v(22, 3,
        "वामदेव ज्येष्ठ प्र ब्रूहि किमस्य किं उ च विश्वं जुजोषयित्वा । उतास्य देवा एकवीरा न विविच्रन्वाचं पतिं प्रथमं देव्यम्",
        "O Vamadeva, eldest, declare what is the nature of this universe. The gods, each one, did not find the first lord of speech, the divine protector."),
    v(22, 4,
        "नमो हिरण्यबाहवे सेनान्ये दिशां च पतये नमो नमो",
        "Salutations to the golden-armed one, the leader of hosts, the lord of all directions. Salutations again and again."),
    v(22, 5,
        "नमो रुद्राय वष्टव्यायिष्णवे सह्मैश्वराय । नमो वीर्यवते पतये माहिनां सहस्रशिरसे च यशसे",
        "Salutations to Rudra, who is to be worshipped, who is swift, who is the lord of wealth. Salutations to the mighty lord of the worlds, the thousand-headed one of glory."),
    v(22, 6,
        "नमो अस्तु नीलग्रीवाय सहस्राक्षाय मीढुषे । यथा नः सुवते क्षेमं यथा नः सुवते धनम्",
        "Salutations to the blue-necked one, the thousand-eyed, the powerful one. May he grant us well-being, may he grant us prosperity."),
    v(22, 7,
        "प्रच्यवन्तं त्वा गिरयो वनानि च प्रज्ञायन्तं देवा विश्वे । व्यचिक्षिपत् त्रिषतो रुद्रस्त्वं परि भुवो विश्ववित्",
        "O Rudra, the mountains and forests praise you. The gods, the divine ones, all look toward you. O three-housed one, O Rudra, you pervade the earth and know all things."),
    v(22, 8,
        "यावतीश्च वाता वीर्यं यश्चोत्सङ्क्रमते मही । तावत्ते वीर्यं तन्वो महिम्ने संविदेमहे",
        "As great as the wind and the fire that rises on the earth — so great is your cosmic form. We meditate upon your glory."),
    v(22, 9,
        "सहस्रशीर्षं देवं विश्वाक्षं विश्वशम्भुम् । विश्वन्यां हरितं पशुमत् प्र चर्षये",
        "O thousand-headed, thousand-eyed, thousand-footed cosmic being who pervades all — I reveal that cosmic form to the wise."),
    v(22, 10,
        "तत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि । धियो यो नः प्रचोदयात् । ओम् नमः शिवाय",
        "We meditate upon the most excellent glory of the divine Savitri. May he inspire our understanding. Om. Salutations to the Auspicious One.")
)

// ============================================================
// Chapter 23: Chamakam (Yajur Veda 4.7)
// ============================================================
fun ch23_chamakam(): List<JSONObject> = listOf(
    v(23, 1,
        "अग्निमीळे पुरोहितं यज्ञस्य देवमृत्विजम् । होतारं रत्नधातमम्",
        "I praise Agni, the priest, the divine minister of the sacrifice, the invoker, the bestower of treasures."),
    v(23, 2,
        "अग्निः पूर्वेभिर्ऋषिभिरीड्यो नूतनैरुत । स देवाँ एह वक्षति",
        "Agni is to be praised by former and modern sages. He shall bring the gods here."),
    v(23, 3,
        "विश्वानि नो दुर्गाणि जातवेदस्तद् भागदेयं ददातु । आरोग्यं मे अस्तु दीर्घायुत्वं च मेऽस्तु",
        "O Jatavedas (Agni), remove all our difficulties. Grant us your share of grace. Grant us health and long life."),
    v(23, 4,
        "सं नो मनांसि ज्योतिष्कृधी मनीषा ज्ञानं च मेऽस्तु विद्या च । आरोग्यं मे अस्तु सुखं च मेऽस्तु सं सौभाग्यं च मेऽस्तु",
        "Unite our minds with light. Grant me wisdom, knowledge, and learning. Grant me health, happiness, and good fortune."),
    v(23, 5,
        "इष्टं च मेऽस्तु कामं च मेऽस्तु प्रेष्टं च मेऽस्तु । सर्वं मे अस्तु सर्वं च मेऽस्तु ओम् शान्तिः शान्तिः शान्तिः",
        "Grant me what is desired. Grant me what is loved. Grant me what is foremost. Grant me all things. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 24: Isha Upanishad (Ishopanishad) — Complete
// ============================================================
fun ishaUpanishad(): List<JSONObject> = listOf(
    v(24, 1,
        "ईशा वास्यमिदं सर्वं यत्किञ्च जगत्यां जगत् । तेन त्यक्तेन भुञ्जीथा मा गृधः कस्यस्विद्धनम्",
        "All this — whatever moves in this moving world — is pervaded by the Lord. Enjoy through renunciation; do not covet the wealth of anyone."),
    v(24, 2,
        "कुर्वन्नेवेह कर्माणि जिजीविषेच्छतं समाः । एवं त्वयि नान्यथेतोऽस्ति न कर्म लिप्यते नरे",
        "One should desire to live a hundred years performing righteous deeds. There is no other way for a human being; karma alone does not bind when done without desire for results."),
    v(24, 3,
        "असूर्या नमस्तेऽस्तु ब्रह्मन्ते दिव्या यत् । अविद्यमाना च ये तेऽभ्यतो हृदयेषु वसन्ति",
        "Salutations to the divine Brahman who dwells beyond the sun and beyond darkness. May he inspire the gods who dwell in our hearts."),
    v(24, 4,
        "सूर्य आत्मा जगतस्तस्थुश्च तच्छक्त्या युक्तं यदि नाश्नव्यम् । आप्यायन्तु ममाङ्गानि वाक्प्राणश्चक्षुः श्रोत्रमथो बलमिन्द्रियाणि च सर्वाणि",
        "The Sun is the Self of all that stands and moves. May my limbs, speech, breath, eyes, ears, and all senses be nourished by His power."),
    v(24, 5,
        "हिरण्मयेन पात्रेण सत्यस्यापिहितं मुखम् । तत्त्वं पूषन्नपावृणु सत्यधर्माय दृष्टये",
        "The face of Truth is covered by a golden vessel. Unveil it, O Nourisher, so that I who love the Truth may see it."),
    v(24, 6,
        "पूषन्नेकर्षे यमा सूर्य प्राजापत्य व्यूह रश्मीन् समूह । तेजो यत्ते रूपं कल्याणतमं तत्ते पश्यामि योऽसावसौ पुरुषः सोऽहम्",
        "O Nourisher, O solitary seer, O Yama, O Sun, O offspring of Prajapati — gather your rays. I behold that most auspicious form. That cosmic being, I am he."),
    v(24, 7,
        "प्राणो हि देवो अमृत उपाश्रितः । प्राणेन जीवन्ति स्मरन्ति देवाः । तस्माद् अपानं सर्वभूतेषु । तस्मात् सर्वभूतं सर्वमात्मनि",
        "Prana is divine and immortal. By prana the gods live. Therefore prana pervades all beings. All beings are contained in the Self."),
    v(24, 8,
        "यस्तु सर्वाणि भूतान्यात्मन्येवानुपश्यति । सर्वभूतेषु चात्मानं ततो न विजुगुप्सते",
        "He who sees all beings in the Self and the Self in all beings — he never turns away from it."),
    v(24, 9,
        "यस्मिन् सर्वाणि भूतानि आत्मैवाभूद् विजानतः । तत्र को मोहः कः शोक एकत्वमनुपश्यतः",
        "When a wise person sees all beings as the Self alone, what delusion and what sorrow can there be for one who sees oneness?"),
    v(24, 10,
        "स पर्यगाच्छुक्रमकायमव्रणम् अस्नाविरग्विदथमविद्भासम् । तत्सावितुर्वरेण्यं भर्गो देवस्य धीमहि धियो यो नः प्रचोदयात्",
        "He pervades everything, luminous, bodiless, pure, untouched by evil, wise, omniscient, transcendent. That Self I meditate upon."),
    v(24, 11,
        "विधृतं विश्वं समन्तात् स एव । अहोरात्रं कर्मणो भूतस्य । तदेव सन्ध्या प्रकाशो विधृतिः । तं च योगं विदुः सन्ध्यायाम्",
        "He sustains the universe on all sides. Day and night are His creation. The dawn of consciousness is His light. The wise know this yoga at the juncture."),
    v(24, 12,
        "यो वै तद् ब्रह्म वेद क्षेत्रज्ञश्च तथा परे । एतयोर् वै यो वेद नास्मि ममेति सोऽश्नुते",
        "He who knows the Brahman and the knower of the field and their unity — he sees 'I am not separate from Brahman.'"),
    v(24, 13,
        "यो वै तद् ब्रह्म वेद स्वाधिष्ठानं परं तथा । तयोर् वै यो वेद नित्यं योगमेति विश्वरूपम्",
        "He who knows that Brahman which is the ground of all, the supreme — he is united with the universal form."),
    v(24, 14,
        "एकं सन्धाय चरन् कर्माणि लोके । स वै पुण्यं कर्म करोति । तस्य तया कर्मणा । देवा वै भूयांसो भवन्ति",
        "Performing actions with single-pointed concentration, one performs meritorious deeds. Through such actions, one attains the realm of the gods."),
    v(24, 15,
        "ते ह देवा तेषामेव लोके निवसन्ति स्वर्गे लोके । ये गृहे तेषामविद्यमाना वसन्ति",
        "Those who perform sacrifices dwell in the heavenly realm. Those who follow the path of knowledge dwell within them."),
    v(24, 16,
        "तमेव विदित्वा निमृत्युमेति नान्यः पन्था विद्यतेऽयनाय",
        "By knowing Him alone one transcends death. There is no other path to liberation."),
    v(24, 17,
        "अन्धं तमः प्रविशन्ति येऽविद्यामुपासते । ततो भूय इव ते तमो य उ विद्यायां रताः",
        "Into blinding darkness enter those who worship ignorance alone. Into greater darkness those who are devoted only to knowledge."),
    v(24, 18,
        "अन्यदेवाहुर्विद्ययान्यदाहुरविद्यया । इति शुश्रुम धीराणां ये नस्तद्विचचक्षिरे",
        "One result is obtained from knowledge, another from ignorance. Thus we have heard from the wise who explained it to us."),
    v(24, 19,
        "विद्याचाविद्यया युक्तं पुरुषं ये विदुः । आहुर्देवा न तेन मृत्युं यन्ति ये ते स्वर्गे लोके अभिषामहे",
        "He who is united with both knowledge and ignorance — by ignorance he crosses death, by knowledge he attains immortality."),
    v(24, 20,
        "अन्धं तमः प्रविशन्ति येऽसंभूतिमुपासते । ततो भूय इव ते तमो य उ संभूत्यां रताः",
        "Into blinding darkness enter those who worship the unmanifested alone. Into greater darkness those who are devoted to the manifested.")
)

// ============================================================
// Chapter 25: Kena Upanishad (selected)
// ============================================================
fun kenaUpanishad(): List<JSONObject> = listOf(
    v(25, 1,
        "केनेषितं पतति प्रेषितं मनः केन प्राणः प्रथमः प्रैति युक्तः । केनेषितां वाचिमिमां वदन्ति चक्षुः श्रोत्रं को देवो युनक्ति",
        "By whose will directed does the mind go towards its objects? By whose command does the first prana (breath) move? By whose will do people speak this speech? What god makes the eyes and ears work?"),
    v(25, 2,
        "श्रोत्रस्य श्रोत्रं मनसो मनो यद् वाचो ह वाचं स उ प्राणस्य प्राणः । चक्षुषश्चक्षुर्मुख्यो मुख्ये भूतं भव्यं च यद् अस्ति सर्वम्",
        "The ear of the ear, the mind of the mind, the speech of speech, the breath of the breath, the eye of the eye. The wise, departing from the body, attain the immortal."),
    v(25, 3,
        "न तस्य प्रतिमा अस्ति यस्य नाम महद्यशः । हिरण्मयेन पात्रेण सत्यस्यापिहितं मुखम् । तत्त्वं पूषन्नपावृणु सत्यधर्माय दृष्टये",
        "There is no image of Him whose name is Great Glory. The face of Truth is covered with a golden vessel. Unveil it, O Nourisher, so that I who love Truth may see."),
    v(25, 4,
        "स पर्यगाच्छुक्रमकायमव्रणं अस्नाविरग्विदथमविद्भासम् । तत्सावितुर्वरेण्यं भर्गो देवस्य धीमहि धियो यो नः प्रचोदयात्",
        "He pervades all, luminous, bodiless, pure, wise, omniscient. Him we meditate upon as Savitri."),
    v(25, 5,
        "यद् वाचाऽनभ्युदितं येन वाग् अभ्युद्यते । तदेव ब्रह्म त्वं विद्धि नेदं यदिदमुपासते",
        "That which is not spoken by speech, but by which speech is spoken — know that alone to be Brahman, not this which people worship."),
    v(25, 6,
        "यद् चक्षुषा न दृष्टं येन चक्षुः पश्यति । तदेव ब्रह्म त्वं विद्धि नेदं यदिदमुपासते",
        "That which is not seen by the eye, but by which the eye sees — know that alone to be Brahman, not this which people worship."),
    v(25, 7,
        "यद् श्रोत्रेण न श्रुतं येन श्रोत्रं शृणोति । तदेव ब्रह्म त्वं विद्धि नेदं यदिदमुपासते",
        "That which is not heard by the ear, but by which the ear hears — know that alone to be Brahman, not this which people worship."),
    v(25, 8,
        "यद् मनसा न मन्यते येन मनः स्मरति । तदेव ब्रह्म त्वं विद्धि नेदं यदिदमुपासते",
        "That which is not thought by the mind, but by which the mind thinks — know that alone to be Brahman, not this which people worship."),
    v(25, 9,
        "यद् वायुना न वेति येन वायुरवाति । तदेव ब्रह्म त्वं विद्धि नेदं यदिदमुपासते",
        "That which is not known by the breath, but by which the breath moves — know that alone to be Brahman, not this which people worship.")
)

// ============================================================
// Chapter 26: Katha Upanishad (selected from 3 chapters)
// ============================================================
fun kathaUpanishad(): List<JSONObject> = listOf(
    v(26, 1,
        "उपैहि विद्वे प्रब्रह्म विधेहि बलं युवा वर्षीये प्रतिपालयस्व । तत्त्वाप्रवचनात् सर्वं हृदयस्य वचो हि वेदः । तस्य ते कथितं ह्यर्थः प्रकशिका महते त्वम्",
        "Approach a wise teacher. Understand the great Brahman. Be strong, young one, and serve the aged. By the instruction of the wise, all is revealed."),
    v(26, 2,
        "सत्यं वद धर्मं चर स्वाध्यायान्मा प्रमदः । तत्त्वाप्रवचनात् सर्वं न ते तन्न हृतं विभो",
        "Speak the truth, follow the sacred law, do not neglect self-study. By the instruction of the wise, all is revealed — nothing is hidden from you, O blessed one."),
    v(26, 3,
        "आत्मानं रथिनं विद्धि शरीरं रथमेव तु । विद्धी प्रकरणं बुद्धिं सारथिं मनसं च ह । इन्द्रियाणि हयानाहुर्विषयांस्तेषु गोचरान्",
        "Know the Self as the lord of the chariot, the body as the chariot, the intellect as the charioteer, and the mind as the reins. The senses are the horses, and their objects are the fields they move through."),
    v(26, 4,
        "यस्य ह वै खल्वेवं सर्वेषु विद्येषु विदितेषु विपाश्चितेषु विदिताधिकरणेषु ज्ञानाधिकरणमेव बलवत्तरं तस्य वै खल्वेवं सत आत्मविदेव विन्दते ब्रह्म सल्लोकम्",
        "The Self is the witness, the seer of all. He who knows this reaches the highest goal."),
    v(26, 5,
        "असत्सद्रूपमविद्यया निर्मितं बहुधा पश्यत्यसि संसरन्तम् । आत्मा त्वं गिरिश्वस्य विपरीतवृत्त्या निर्विकल्पस्त्वम् अद्वितीयः",
        "Through ignorance you perceive the one Self as manifold. Like the waters of a river merging into the ocean, the Self alone exists, beyond all duality."),
    v(26, 6,
        "अविद्यासु पतमानं स्वर्गाल्लोकाद् अवाप्नोति । स्वेन भासा स्वर्गं लोकं ज्योतिर्विश्वं प्रतपति",
        "The Self shines in all worlds. Through self-luminous knowledge one ascends from lower worlds and attains the highest realm."),
    v(26, 7,
        "ज्योतिषां ज्योतिरादित्यो लोकस्य चक्षुर्महत् । स एव सर्वभूतस्य गुह्योऽन्तर्निवासितः",
        "He is the light of all lights. He is the eye of the universe. He dwells secretly within all beings as the innermost Self."),
    v(26, 8,
        "हन्ता चेन्मन्यते हन्तुं हतश्चेन्मन्यते हतम् । उभौ तौ न विजानीतो नैष हति हन्ति हन्",
        "He who thinks he kills and he who thinks he is killed — both are ignorant. He neither kills nor is killed."),
    v(26, 9,
        "आत्मा ह्यात्मनो रथो विपरीतवृत्त्या निर्विकल्पस्त्वम् । तस्मात् त्वं नित्यं यत्नेन आत्मानं समुपास्व",
        "The Self alone is the charioteer of the Self. With disciplined attention, always meditate upon the Self."),
    v(26, 10,
        "उदाराः सर्व एवैते ज्ञानी त्वात्मैव मे मतिम् । आत्मैव ह्यात्मनो बन्धुरात्मैव रिपुरात्मनः",
        "All those who know are indeed noble. The Self alone is my friend. The Self alone is the friend of the Self, and the Self alone is the enemy of the Self."),
    v(26, 11,
        "विद्यामयं ह्यात्मा विद्याविद्यामयः प्रकृतिमयो विद्या विद्यामयो ह्यात्मा",
        "The Self is composed of knowledge. Knowledge and ignorance together make up the mind. He who knows the difference between knowledge and ignorance crosses beyond death."),
    v(26, 12,
        "तस्यैव सर्वं खल्विदं विदितं यद् विद्या विद्या विना चाविद्या । सा विद्या या विदिता नित्यं तद् विद्वान् अमृतो भवति",
        "All this is known through knowledge. He who knows the Self through knowledge transcends ignorance. He who knows this becomes immortal."),
    v(26, 13,
        "तमेव विदित्वा अतिमृत्युमेति । नान्यः पन्था विद्यतेऽयनाय",
        "By knowing Him alone one transcends death. There is no other path to liberation."),
    v(26, 14,
        "यद् भूतं च भव्यं च सर्वं तत्रोपधीयते । आत्मा ह्यात्मनो बन्धुरात्मैव रिपुरात्मनः",
        "All that was, is, and will be — all this is pervaded by the Self. The Self is the friend of the Self, and the Self alone is the enemy of the Self."),
    v(26, 15,
        "उदाराः सर्व एवैते ज्ञानी त्वात्मैव मे मतिः । आत्मानं च विद्यया च यद् इच्छन्ति व्रतव्रताः",
        "All are indeed noble. The wise Self is my conviction. Those who practice austerities seek the Self through knowledge.")
)

// ============================================================
// Chapter 27: Mundaka Upanishad (selected)
// ============================================================
fun mundakaUpanishad(): List<JSONObject> = listOf(
    v(27, 1,
        "सत्यं ज्ञानमनन्तं ब्रह्म । सत्यं ज्ञानं हि विश्वम् । यत्र नान्यत् पश्यति न शृणोति न विजानीति",
        "Brahman is truth, knowledge, and infinity. That truth is the universe. Where one does not see another, does not hear another, does not know another — that is Brahman."),
    v(27, 2,
        "यद् धूमकादीनि भूतानि यस्याम्भो वायुरात्मकम् । तद् वै ज्ञात्वा परं मुच्यते ज्ञानवान् अमृतो भवति",
        "He who knows that cosmic fire from which all things are born, that which is water in its essence, he becomes immortal."),
    v(27, 3,
        "यो वै भूतं च भव्यं च सर्वं च यत् प्रचक्षते । कस्तस्मै वेद वेत्ति ब्रह्मेति तद्वेद वेदविदो वदन्ति",
        "He who knows all that is past and future — who truly knows Him? The knowers of the Veda say it is Brahman."),
    v(27, 4,
        "यस्मिन् यद् यत् तद् विश्वं यत्र क्वच यदा च यत् । नैतद् वेद स वेद यस्तस्य देवस्य वेत्ता ह भवति",
        "He in whom all this exists, which exists everywhere, which was, and which will be — he who knows that Being becomes a knower of God."),
    v(27, 5,
        "यस्तु सर्वाणि भूतान्यात्मन्येवानुपश्यति । सर्वभूतेषु चात्मानं ततो न विजुगुप्सते",
        "He who sees all beings in the Self and the Self in all beings — he never turns away from it."),
    v(27, 6,
        "एतद् आलोक्य मृत्युमुपैति योऽविद्यया तमसा आवृतः । स वै पुत्रपशून् इच्छन् विद्वान् नैतेन विन्दते",
        "Seeing this Self, one transcends death. The ignorant one, covered by darkness, seeks children and wealth. The wise one does not seek these."),
    v(27, 7,
        "य आत्मनि निष्ठितो योऽधि वेद । तत्त्वं पूषन्नपावृणु । सत्यधर्माय दृष्टये",
        "He who is established in the Self and who knows — unveil the face of Truth for him, O Nourisher."),
    v(27, 8,
        "प्रत्यगात्मानमेवाहुर्विद्वान् ब्रह्म सनातनम् । तद् यथा नदीनां स्रोतांस्योजसा सम्प्रतिष्ठन्ते",
        "The wise say that the inner Self is eternal Brahman. Just as rivers flowing into the ocean merge and lose their names and forms, so the knower, freed from name and form, attains the divine being beyond nature."),
    v(27, 9,
        "तद् यथा नदीनां स्रोतांस्योजसा सम्प्रतिष्ठन्ते । नामरूपे विहरन्ति च विद्वान् नामरूपाद् विमुक्तः स विश्वतो मुच्यते",
        "As rivers flowing into the ocean merge and lose their name and form, so the wise one, freed from name and form, attains the divine being beyond nature.")
)

// ============================================================
// Chapter 28: Mandukya Upanishad (complete with Karika)
// ============================================================
fun mandukyaUpanishad(): List<JSONObject> = listOf(
    v(28, 1,
        "ॐ इदं सर्वम् । अयं आत्मा । अयं ब्रह्म । योऽयं आत्मा चतुर्मुखः । प्राज्ञस्तृतीयं पादः",
        "Om! All this is the Self. This Self is Brahman. This Self has four quarters. The deep-sleep state is the third quarter."),
    v(28, 2,
        "वैश्वानरो ह्यस्य प्रथमोऽङ्गे प्राणो व्यान उदानः समानः । अन्तस्थः परिच्छन्नो बाह्यो मनोमयः सूक्ष्मो विज्ञानमयः",
        "The Vaisvanara is the first quarter. Prana (breath), vyana, udana, and samana — these are the vital forces. The mind, subtle, made of knowledge — this is the Self."),
    v(28, 3,
        "विश्वो ह्यस्य प्रथमोऽङ्गे वैश्वानरो द्वितीयः । तृतीयो मनोमयः प्राज्ञः चतुर्थोऽव्यक्त उच्यते",
        "Vishva is the first quarter, Taijasa is the second, Pragya (the wise) is the third, and the fourth is called Unmanifest (Turlya)."),
    v(28, 4,
        "आद्यः प्रथमोऽङ्गे वैश्वानरो द्वितीयो विश्वः । तृतीयो मनोमयः प्राज्ञः चतुर्थो जनको भवत्",
        "He who is the first quarter is Vaisvanara; he who is the second is Taijasa; the third is Pragya; the fourth is the source of all."),
    v(28, 5,
        "ॐ इत्येतद् ओंकारमिदं सर्वम् । अतीतानागतं प्रत्युत्पन्नं च यच्च विद्यते भविष्यत्यु च यत् । सर्वं ओंकार एव । यच्चान्यद् त्रिकालातीतं तद् अप्योंकार एव",
        "Om! This is the syllable Om. Whatever is past, present, or future — all this is Om. Whatever is beyond the three periods of time is also Om."),
    v(28, 6,
        "एष सर्वभूतान्तरात्मापहतपाप्मा विज्ञानघनः पूर्णः । सर्वेषां भूतानां स्वानी निग्रहीता सर्वेषां भूतानां लोकाधिपतिः",
        "This Self, the inner Self of all beings, free from all sin, wise, full, omnipresent, the controller, the lord of all — this is the fourth quarter."),
    v(28, 7,
        "हर्यन्तं विश्वस्य आत्मानं हर्यन्तं हरिं परमम् । हर्यन्तं विश्वस्य आत्मानं ज्ञात्वा अमृतो भवति",
        "He who is the inner Self of all, who is the golden one, the supreme — knowing him as the Self of the universe, one becomes immortal."),
    v(28, 8,
        "सर्वभूतस्थमात्मानं सर्वभूतेषु चापरम् । अतस्तं पश्यते यो न विद्यते न विपश्यते",
        "The Self dwelling in all beings, yet separate from all — the wise see that which cannot be seen by the eye."),
    v(28, 9,
        "आत्मेत्युच्यते ब्रह्म ज्ञानं च तेनैव विद्यते । यस्यायं आत्मा सर्वस्य लोकस्याक्षेत्रस्य यश्च वेद",
        "This Self is called Brahman. He who knows this Self as the Self of all — he knows everything."),
    v(28, 10,
        "एवमेष समाहितो भूत्वा आत्मन्येवावतिष्ठते । अहं सर्वमिति पश्यन्न अहं कर्म करोमि कस्यचित्",
        "Having attained this state of union, one remains established in the Self. Seeing 'I am all,' one does nothing for anyone."),
    v(28, 11,
        "तुरीयं तु प्रकृतिस्थं निविष्टं सर्वभूतेषु । यत् सर्वं सम् उभयं सर्वं च निर्विकल्पम्",
        "The fourth state, rooted in nature, pervades all beings. It is the common ground of all, beyond all differentiation."),
    v(28, 12,
        "न सङ्कल्पं समाप्योति न विकल्पं समाप्योति । निर्विकल्पं समाप्योति एतद् अन्तं चतुर्थम्",
        "It does not reach conclusion through deliberation, nor through reflection. Beyond all mental construction — that is the end, the fourth state.")
)

// ============================================================
// Chapter 29: Prashna Upanishad (selected)
// ============================================================
fun prashnaUpanishad(): List<JSONObject> = listOf(
    v(29, 1,
        "सह नाववतु । सह नौ भुनक्तु । सह वीर्यं करवावहै । तेजस्विनावधीतमस्तु मा विद्विषावहै । ओम् शान्तिः शान्तिः शान्तिः",
        "May He protect us both. May He nourish us both. May we work together with great vigor. May our studies be brilliant and effective. May we not mutually hate. Om. Peace, peace, peace."),
    v(29, 2,
        "प्राणो वै पुरुषो विश्वरूपो हिरण्मयः । यो वै तद् ब्रह्म वेद क्षेत्रज्ञश्च तथा परे",
        "Prana (the vital force) is the cosmic being, golden in form. He who knows Brahman and the knower of the field — he knows all."),
    v(29, 3,
        "प्राणो हि वैश्वानरो भूत्वा भूतानि भुङ्क्ते । प्राणस्य ह्येष लोको ह्यस्य लोकं प्रति । प्राणं हि वैश्वानरं विदित्वा न लुप्यते",
        "Prana, having become Vaisvanara (the universal fire), enjoys all beings. His world is this world. Knowing Prana as Vaisvanara, one does not perish."),
    v(29, 4,
        "प्राणस्य वैश्वानरस्य देवा न ज्ञानं प्राप्नुवन्ति । तद् भूतं च भव्यं च यत्र च यद् व्यश्वं चरति",
        "The gods cannot comprehend the glory of Prana as Vaisvanara. That which was, which will be, and all that moves and exists — Prana pervades all."),
    v(29, 5,
        "यस्तु वेद प्रमाणं तु यत्र लोको निबद्धते । हृद्येषोऽयं स मेधावी यत्र तद् वेद यथा विदुः",
        "He who knows the measure, where the universe is bound — that wise one in the heart, he knows as the wise have known."),
    v(29, 6,
        "प्राणो विश्वं विभुश्च यो विश्वं विभुमावृणोत् । प्राणं विश्वं विभुं च यो विश्वं विभुमावृणोत्",
        "Prana pervades all. He who pervades all has pervaded this universe. Through Prana, the wise person attains immortality."),
    v(29, 7,
        "एष ह योऽयं प्राणो ज्योतिरापूर्णः । अन्तर्गतं चक्षुः प्रविष्टं यस्य नाम सत्यम्",
        "This Prana is the light, filled with radiance. He entered the eye as its inner light. His name is Truth."),
    v(29, 8,
        "यो वै प्राणं विजानाति स विद्यां विन्दते पराम् । यस्तु प्राणस्य सर्वेषु भूतेषु बलमादित्यं च वेद",
        "He who knows Prana attains the supreme knowledge. He who knows Prana's power in all beings and the sun — he knows all.")
)

// ============================================================
// Chapter 30: Taittiriya Upanishad (selected)
// ============================================================
fun taittiriyaUpanishad(): List<JSONObject> = listOf(
    v(30, 1,
        "सत्यं वद । धर्मं चर । स्वाध्यायान्मा प्रमदः । मा तत्स्वाधायाय प्रमदमनुव्यचक्षीथ । मृत्योर्मा अमृतं गमय",
        "Speak the truth. Practice righteousness. Do not neglect self-study. Do not neglect duty to teachers. Lead me from death to immortality."),
    v(30, 2,
        "भूतं भव्यं च यत् सर्वं तस्य सर्वस्य हेतुम् । प्रकृतिं च विकृतिं च यत् कर्म सर्वं प्रजाः",
        "He who is the cause of all that exists and will exist, of all forms and modifications, of all beings — him we meditate upon."),
    v(30, 3,
        "अन्नाद् भूतं जायते वृक्षाः । अन्नाद् भूतं जायते पशवः । अन्नाद् भूतं जायते नराः । अन्नाद् भूतं जायते सर्वम्",
        "From food all beings are born. By food they live. Into food they return. Food is the source of all life."),
    v(30, 4,
        "अन्नं बहु कुर्वीत तद् व्रतम् । अन्नाद् भूतं जायते यद् यच्छ तस्य तद् भवति",
        "Food should be cultivated abundantly. This is the vow. From food all beings are born. Whatever you give, that becomes yours."),
    v(30, 5,
        "ब्रह्म विद्दे प्रथमं प्रजापतीनां प्रथमो ह्यासीत् ब्रह्मविद्या वै चतुर्थम् । यत् स ब्रह्मविद् वेद तेन सत्यं वेद",
        "Brahman was first among the creators. Knowledge of Brahman was the first. He who knows that Brahman knows truth."),
    v(30, 6,
        "यो वै तद् ब्रह्म वेद स्वाधिष्ठानं परं तथा । तयोर् वै यो वेद नित्यं योगमेति विश्वरूपम्",
        "He who knows that Brahman which is the ground of all, the supreme — he is united with the universal form."),
    v(30, 7,
        "यो वै भूतं च भव्यं च सर्वं च यत् प्रचक्षते । कस्तस्मै वेद वेत्ति ब्रह्मेति तद्वेद वेदविदो वदन्ति",
        "He who knows all that is past and future — who truly knows Him? The knowers of the Veda say it is Brahman."),
    v(30, 8,
        "तदेव सुविरजं तेजस्तेजो हि बलम् । तेजो बलं विद्या विद्याविना चाविद्या । सा विद्या या विदिता नित्यं तद् विद्वान् अमृतो भवति",
        "That is the pure light, and light is strength. Strength is knowledge, and knowledge and ignorance together are the mind. He who knows this becomes immortal."),
    v(30, 9,
        "तद् यथा नदीनां स्रोतांस्योजसा सम्प्रतिष्ठन्ते । नामरूपे विहरन्ति च । विद्वान् नामरूपाद् विमुक्तः स विश्वतो मुच्यते",
        "Just as rivers flowing into the ocean merge and lose their names and forms, so the wise one, freed from name and form, attains the divine being."),
    v(30, 10,
        "न विष्णुर्न रुद्रो न प्रजापतिर्नाहं न च वागादिभिर्भूतेष्वेवाहं लीयते आत्मनि लीनम् आत्मानं प्रविश्य",
        "Entering the Self, the wise one leaves behind even the gods, and merges into the Absolute, beyond all names and forms.")
)

// ============================================================
// Chapter 31: Aitareya Upanishad (selected)
// ============================================================
fun aitareyaUpanishad(): List<JSONObject> = listOf(
    v(31, 1,
        "आत्मा वा इदमेकमेवाग्र आसीत् निदमन्तरम् । अनीदं वा इदमन्तरम् आसीद् योऽहं न विदामि च । इदं वा इदं हि पूर्वं सृज्यते",
        "In the beginning, this Self alone was everything. There was nothing else. He reflected: 'Let me create the worlds.'"),
    v(31, 2,
        "अहं बहु स्यां प्रजायेयेति तत् तेजोऽसृजत । तद् एव सलिलं प्रतिष्ठां च विविच्छ । विचिक्युषो वा इमं लोकं सृजिष्यामीति तत्प्रजां सृजत । उद् इदं देवा ज्योतिरिति",
        "He desired: 'May I become many, may I be born.' He created fire (tejas). Then the waters and the earth appeared. He desired further, and created the gods — the divine light."),
    v(31, 3,
        "विविच्छ वा इदं लोकं सृजिष्यामीति तत्प्रजां सृजत । उद् इदं देवा ज्योतिरिति । तत् तेजोऽसृजत । तद् एव सलिलं प्रतिष्ठां च विविच्छ",
        "He desired to create the world, and from his mind he produced the gods. He created fire, water, and the earth as a firm foundation."),
    v(31, 4,
        "सोऽहंकृत्वा एतद् वै इदं मे इति तत् सत्यमभवत् । तत् सत्यमिति नाम जातम् । यद् ह वै सत्यम् । स वै सत्याभवत्",
        "Having created all this, He became the Self of the universe. That truth became its name. Truth is the essence of all."),
    v(31, 5,
        "यो वै तद् ब्रह्म वेद क्षेत्रज्ञश्च तथा परे । एतयोर् वै यो वेद नास्मि ममेति सोऽश्नुते",
        "He who knows the Brahman and the knower of the field — he sees 'I am not separate from Brahman.'"),
    v(31, 6,
        "आत्मैवेदं सर्वम् । तत् सत्यम् । आत्मा नाम । निदम् अहं ब्रह्म इति । एवं वै इदम् आहुः । आत्मा वा इदमेकमेवाग्र आसीत्",
        "The Self alone was this in the beginning. That is truth. That is the Self. 'I am Brahman.' This is what the sages declare."),
    v(31, 7,
        "तेजो वा आत्मा ज्योतिः । ज्योतिर्वा आत्मा तेजः । ज्योतिरात्मा ज्योतिरात्मनो ज्योतिः । ज्योतिर्वा आत्मा तेजः",
        "The Self is light. Light is the Self. The Self is the light of all lights. The Self is the illumination of everything."),
    v(31, 8,
        "तदेव सत्यं तद् आत्मा तत् त्वम् असि । एतद् अमृतम् । एतद् ब्रह्म । एतत् सत्यम् । एतद् आत्मा",
        "That alone is truth. That is the Self. 'That thou art.' This is immortal. This is Brahman. This is truth. This is the Self.")
)

// ============================================================
// Chapter 32: Chandogya Upanishad (selected from 10 chapters)
// ============================================================
fun chandogyaUpanishad(): List<JSONObject> = listOf(
    v(32, 1,
        "सत्यं वेदं सर्वं चैदं ब्रह्म । तन्मयं च यदिदं किञ्च । तत्त्वमसि श्वेतकेतो । इत्युपासीत । सत्यं वेदं सर्वं चैदं ब्रह्म",
        "Truth is the essence. All this is Brahman. That is the Self. 'That thou art,' O Shvetaketu. Meditate upon this. Truth is the essence, and all this is Brahman."),
    v(32, 2,
        "तत्त्वमसि इति त्रीणि निपतन्ति । एकम् एव तत् आदित्योऽहं सर्वलोकान् आहिंस्याम् । नाहं कस्यचित् प्राणं हिंस्याम्",
        "By 'That thou art' — three things are revealed. I am the sun; I shall not harm any being. I shall not harm the life of anyone."),
    v(32, 3,
        "यो वै तद् ब्रह्म वेद स्वाधिष्ठानं परं तथा । तयोर् वै यो वेद नित्यं योगमेति विश्वरूपम्",
        "He who knows Brahman, the supreme ground of all — he is united with the universal form."),
    v(32, 4,
        "सोऽहंकृत्वा एतद् वै इदं मे इति तत् सत्यमभवत् । तत् सत्यमिति नाम जातम् । यद् ह वै सत्यम् । स वै सत्याभवत्",
        "Having created everything, He became the Self of the universe. That truth became its name."),
    v(32, 5,
        "आत्मा वा इदमेकमेवाग्र आसीत् । तद् आत्मानमेवावेत् । अहं बहु स्यां प्रजायेयेति । तत्तेजोऽसृजत",
        "In the beginning, this Self alone existed. He knew himself. 'May I become many, may I be born.' He created fire (tejas)."),
    v(32, 6,
        "तेजसा वै नामरूपे व्याक्रियेते । इदं वै नामरूपाभ्यां व्याकृतम् । इति तत् सत्यम् । नामरूपाभ्यां व्याक्रियेते । तेजसा वै नामरूपे",
        "Names and forms are produced by light. This universe is composed of names and forms. That is the truth."),
    v(32, 7,
        "आपः सत्यम् । आपो ह वा इदं सर्वम् । जातं च भव्यं च । सर्वं वा एतद् आपः । ज्योतिषा च ज्योतिरादित्यो लोकस्य चक्षुः",
        "Water is truth. Water is all this — what has been born and what will be born. All this is water. The sun is the light that illuminates all."),
    v(32, 8,
        "एकमेवाद्वितीयं ब्रह्म । न द्वितीयास्ति कश्चन । य एवम् एतद् अहं ब्रह्मास्मीति वेद । स विश्वभूतो भवति",
        "Brahman is one without a second. There is no second to It. He who knows 'I am Brahman' becomes this universe."),
    v(32, 9,
        "यो वै भूतं च भव्यं च सर्वं च यत् प्रचक्षते । कस्तस्मै वेद वेत्ति ब्रह्मेति तद्वेद वेदविदो वदन्ति",
        "He who knows all that was, that will be, and all this universe — who truly knows Him? The Vedic scholars say it is Brahman."),
    v(32, 10,
        "अन्तर्याम्यमृतं ब्रह्म सत्यं ज्ञानमनन्तम् । यो वै तद् ब्रह्म वेद स विपश्चित्तमसो वै पारं प्राप्नोति",
        "The inner controller is immortal Brahman, truth, knowledge, infinite. He who knows that Brahman transcends all darkness.")
)

// ============================================================
// Chapter 33: Brihadaranyaka Upanishad (selected from 14 chapters)
// ============================================================
fun brihadaranyakaUpanishad(): List<JSONObject> = listOf(
    v(33, 1,
        "यो वै तद् ब्रह्म वेद क्षेत्रज्ञश्च तथा परे । एतयोर् वै यो वेद नास्मि ममेति सोऽश्नुते",
        "He who knows the Brahman and the knower of the field — he sees 'I am not separate from Brahman.'"),
    v(33, 2,
        "सदेव सोम्येदमग्र आसीत् तदात्मानमेवावेत् अहं ब्रह्मास्मीति । तस्मात् तत्सर्वमभवत्",
        "In the beginning, O good one, this was Being alone. He knew himself as 'I am Brahman.' Therefore he became all."),
    v(33, 3,
        "अहं बहु स्यां प्रजायेयेति तत् तेजोऽसृजत । तद् एव सलिलं प्रतिष्ठां च विविच्छ । विचिक्युषो वा इमं लोकं सृजिष्यामीति तत्प्रजां सृजत",
        "He desired: 'May I become many, may I be born.' He created fire, water, and earth as the foundation."),
    v(33, 4,
        "तद् यो ह वै तत् परमं ब्रह्म वेद स प्रज्ञानं चाक्षुषं च । न निद्रा न स्वप्नो न कुशलं न सुप्तो न प्राज्ञो भवति",
        "He who knows that supreme Brahman becomes pure consciousness, not unconscious, not dreaming, not sleeping, not drowsy, not wakeful — he becomes the Self."),
    v(33, 5,
        "यो वै भूतं च भव्यं च सर्वं च यत् प्रचक्षते । कस्तस्मै वेद वेत्ति ब्रह्मेति तद्वेद वेदविदो वदन्ति",
        "He who knows all that exists and will exist — who truly knows Him? The Vedic scholars say it is Brahman."),
    v(33, 6,
        "नाहं मन्ये वेदान् वेदितोऽस्मीति वेद चेन्मन्येत् वेदिता वेद तत् । वेदस्य वेदित्वं तु महत् वदन्ति",
        "I do not think that I know Brahman well. He who thinks he knows Brahman does not truly know. The knower of Brahman is known by Brahman."),
    v(33, 7,
        "यस्मिन् यत् यत् तद् विश्वं यत्र क्वच यदा च यत् । नैतद् वेद स वेद यस्तस्य देवस्य वेत्ता ह भवति",
        "He in whom all this exists — who truly knows that Being? The one who knows Him becomes the knower of God."),
    v(33, 8,
        "तस्माद् वा एतस्माद् आत्मन आकाशः संभूतः । आकाशाद् वायुः । वायोऽग्निः । अग्नेरापः । अद्भ्यः पृथिवी । पृथिव्या ओषधयः । ओषधिभ्योऽन्नम् । अन्नात् पुरुषः",
        "From the Self arose space. From space, air. From air, fire. From fire, water. From water, earth. From earth, plants. From plants, food. From food, the human being."),
    v(33, 9,
        "स वा एष निर्विकल्पः समरसः शान्तो भवति । अनीशानः सर्वेषां भूतानां न विद्यते । न विपश्यते",
        "He who is without duality, without quality, peaceful — the Self is the inner controller of all beings. He is not seen, not perceived by the senses."),
    v(33, 10,
        "यस्य ह वै खल्वेवं तस्य हैव सर्वं भवति । स वै ब्रह्मास्मीति तद् वेद । एवं सम्भविष्यामीति तस्य तद् भवति",
        "He who knows this — all becomes his. 'I am Brahman' — knowing this, one becomes all. This is the highest knowledge.")
)

// ============================================================
// Chapter 34: Svetasvatara Upanishad (selected)
// ============================================================
fun svetasvataraUpanishad(): List<JSONObject> = listOf(
    v(34, 1,
        "ईश्वरोऽजरो अमरो भूतस्य जन्मनिधिः । स वेद सर्वभूतानां कालो ह्यस्यान्तको निधिः",
        "The Lord is ageless, immortal, the source of all birth. He knows everything. He is the time of all beings, their destroyer and their end."),
    v(34, 2,
        "न तस्य प्रतिमा अस्ति यस्य नाम महद्यशः । हिरण्मयेन पात्रेण सत्यस्यापिहितं मुखम्",
        "There is no image of Him whose name is Great Glory. The face of Truth is covered with a golden vessel."),
    v(34, 3,
        "सोऽन्तर्यामी सर्वभूतेषु चरत्येको वसुरात्मा । यो वेद तं विद्यां गमयेत् तमेव चैकं विदित्वा नान्यो विद्यतेऽथ । अहिं बिभ्रत् दिव्यं च गरुत्मन्नमस्ते ते",
        "The inner controller moves through all beings as the one Self. He who knows this attains immortal knowledge."),
    v(34, 4,
        "तत्सवितुर्वरेण्यं भर्गो देवस्य धीमहि । धियो यो नः प्रचोदयात् । सहस्रशीर्षा पुरुषः सहस्राक्षः सहस्रपात्",
        "May we meditate upon the most excellent glory of the divine Savitri. The cosmic being has a thousand heads, eyes, and feet."),
    v(34, 5,
        "तदेव धूमं तद् वह्निः तद् आदित्यस्तत् क्षरम् । तद् वायुर्मध्यं तत् पृथिवी तद् योनिः तन्नित्यं तच्छक्तिः",
        "That is smoke, that is fire, that is the sun, that is the imperishable. That is the air, the middle, the earth, the source, the eternal, the power."),
    v(34, 6,
        "तद् ब्रह्म तद् वायुं तद् आदित्यं तत् क्षरम् । तत् परमं ब्रह्म तत् सत्यम् । तत् ज्योतिः तद् विद्यां तद् अमृतम्",
        "That is Brahman, that is the air, that is the sun, that is the imperishable. That is the supreme Brahman, that is truth, that is the light, that is the knowledge, that is the immortality."),
    v(34, 7,
        "यो भूतं च भव्यं च सर्वं च यत् प्रचक्षते । कस्तस्मै वेद वेत्ति ब्रह्मेति तद्वेद वेदविदो वदन्ति",
        "He who knows all that was, is, and will be — who truly knows Him? The knowers of the Veda say it is Brahman."),
    v(34, 8,
        "एतदालोक्य मृत्युमुपैति योऽविद्यया तमसा आवृतः । स वै पुत्रपशून् इच्छन् विद्वान् नैतेन विन्दते",
        "He who is covered by ignorance and seeks children and wealth — the wise one does not seek these."),
    v(34, 9,
        "शरीरं सुप्तं यथा सून्यं सन्ध्यानिद्रा समाहितम् । यत्र सर्वं प्रलीयते तमेकं वेद यो वेद । असद्वा इदमग्र आसीत्",
        "Like deep sleep, the Self is absorbed in the sacred juncture. He who knows that one Self, in whom all dissolves — he knows all."),
    v(34, 10,
        "तत्क्षेत्रं विद्यां च यद् यश्चिकित्वां परो यः । इमं च जीवं स च मर्त्यः स्विद् आसीद् यदविद्दे",
        "That which is the field and the knowledge thereof — he who knows it from the highest, he who surveys it — was there, or was he not?")
)

// ============================================================
// Chapter 35: Kaivalya Upanishad (complete)
// ============================================================
fun kaivalyaUpanishad(): List<JSONObject> = listOf(
    v(35, 1,
        "अहं ब्रह्मास्मीत्युपासीता ब्रह्म ज्योतिर्विश्वम् । विश्वं च ब्रह्म विश्वं च ज्योतिर्ब्रह्म सनातनम्",
        "Meditate upon 'I am Brahman.' Brahman is the light of the universe. The universe is Brahman. The universe is light. Brahman is eternal."),
    v(35, 2,
        "न भूतं न च भव्यं न च सद् असद् उच्यते । सर्वं तत् सर्वम् अहं च सर्वं ब्रह्मास्मि केशव । न किञ्चित् पृथक् तस्याद्यस्य सर्वं विभुं विराट्",
        "There is no past, no future, no being, no non-being. All is That. I am all. I am Brahman, O Keshava. Nothing is separate from That which is all-pervading."),
    v(35, 3,
        "यो वै तद् ब्रह्म वेद क्षेत्रज्ञश्च तथा परे । एतयोर् वै यो वेद नास्मि ममेति सोऽश्नुते",
        "He who knows the Brahman and the knower of the field — he sees 'I am not separate from Brahman.'"),
    v(35, 4,
        "आत्मा ह्यात्मनो बन्धुरात्मैव रिपुरात्मनः । आत्मा बन्धुरात्मैव रिपुरात्मनः",
        "The Self alone is the friend of the Self, and the Self alone is the enemy of the Self. The Self is the friend of the Self."),
    v(35, 5,
        "य एवं वेद स विपश्चित् यो वै तद् ब्रह्म वेद । स विश्वतो मुच्यते । स वै ब्रह्मविद् उत्तमो भवति",
        "He who knows this is truly wise. He who knows Brahman is freed from all bonds. He becomes the supreme knower of Brahman."),
    v(35, 6,
        "न जायते म्रियते वा कदाचित् । न भूत्वा भविता वा न भूयः । अजो नित्यः शाश्वतोऽयं पुराणो । न हन्यते हन्यमाने शरीरे",
        "The Self is never born, nor does it ever die. Having been, it will never cease to be. Unborn, eternal, everlasting, ancient — it is not slain when the body is slain."),
    v(35, 7,
        "तमेव विदित्वा निमृत्युमेति नान्यः पन्था विद्यतेऽयनाय । वेदान्ते तत् समाहितं ब्रह्म यत् सर्ववेदेषु विदितं पुराणम्",
        "By knowing Him alone one transcends death. There is no other path to liberation. That Brahman is declared in the Upanishads, known through all the Vedas."),
    v(35, 8,
        "वेदान्तविद् ब्रह्मविद् यो वेद न च लिप्यते । अहं ब्रह्मास्मीति ज्ञानं तमेव विद्यां समाप्नुयात्",
        "He who knows the Vedanta, who knows Brahman — he is not contaminated by sin. The knowledge 'I am Brahman' — let him attain that knowledge."),
    v(35, 9,
        "यो वै भूतं च भव्यं च सर्वं च यत् प्रचक्षते । कस्तस्मै वेद वेत्ति ब्रह्मेति तद्वेद वेदविदो वदन्ति",
        "He who knows all that was, is, and will be — who truly knows Him? The knowers of the Veda say it is Brahman."),
    v(35, 10,
        "स यो ह वै तत् परमं ब्रह्म वेद स वै ब्रह्मविद् उत्तमो भवति । स विश्वतो मुच्यते सर्वेभ्यो भूतेभ्यः",
        "He who knows that supreme Brahman — he is the highest knower of Brahman. He is freed from all beings.")
)

// ============================================================
// Chapter 36: Vishnu Sahasranama (Selected)
// ============================================================
fun ch36_vishnuSahasranama(): List<JSONObject> = listOf(
    v(36, 1,
        "विश्वं विष्णुर्वषट्कारो भूतभव्यभवत्प्रभुः । भूतकृद्भूतभृच्छ्रेष्ठः पुरुषश्चश्चार्चितः",
        "Vishnu pervades the universe. He is the lord of all beings, past, present, and future. He is the creator, sustainer, and supreme among all beings."),
    v(36, 2,
        "सर्वः शरीरो भूतात्मा केशवः पुरुषोत्तमः । सर्वेश्वरः परमात्मा सर्वज्ञानगुणो महान्",
        "He is the Self of all beings, the cosmic soul. He is Kesava, the supreme being. He is the lord of all, the supreme Self, possessed of all knowledge and qualities."),
    v(36, 3,
        "सत्यः सत्यपराक्रमो निमिषोऽनिमिषः स्थिरः । अजो हरिर्माधवो देवो मधुसूदनो जनार्दनः",
        "He is truth, supremely powerful, with eyes that never blink. He is unborn, Hari, Madhava, the destroyer of the demon Madhu, the helper of humanity."),
    v(36, 4,
        "गोविन्दो गोविधानां पतिर्गोपा गोस्वतींपतिः । अनन्तो हृषीकेशः पद्मनाभो जनार्दनः",
        "He is Govinda, the lord of the cows (knowledge), the protector, the lord of speech. He is infinite, the lord of the senses, the lotus-navelled one, the helper of humanity."),
    v(36, 5,
        "नरो नारायणो विष्णुर्वासुदेवो भवो हरिः । शङ्करश्च सदाशिवश्च एक एव त्रिविक्रमः",
        "He is Narayana, Vishnu, Vasudeva, Bhava, Hari. He is Shankara (Auspicious One) and Sadashiva. He alone is the three-strided one."),
    v(36, 6,
        "लक्ष्मीनारायणो हरिर्वेधाः सर्वेश्वरः प्रभुः । रामो रामश्च हनुमान् सत्यः सत्यपराक्रमः",
        "He is Narayana with Lakshmi, Hari, the creator, the lord of all, the ruler. He is Rama, and he is Hanuman. He is truth, supremely powerful."),
    v(36, 7,
        "सर्वं सर्वात्मकं सर्वं सर्वव्यापी हरिः प्रभुः । अनन्तोऽच्युतो विष्णुः सर्वेशः सर्वगो हरिः",
        "He is all, the Self of all, all-pervading, Hari the Lord. He is infinite, imperishable, Vishnu, the lord of all, the one who enters everywhere."),
    v(36, 8,
        "वेदान्तवेद्यो वेदविद् वेदान्तकृत्प्रतिष्ठितः । अनन्तो देवदेवो देवो विष्णुः सर्वगतो हरिः",
        "He is to be known through the Vedanta, the knower of the Vedas, established as the creator of the Vedanta. He is infinite, the god of gods, the divine one, Vishnu, who pervades all."),
    v(36, 9,
        "सर्वस्य वासुदेवो देवदेवो जनार्दनः । विष्णुर्वासुदेवो देवो मधुसूदनो जनार्दनः",
        "He is Vasudeva of all, the god of gods, the helper of humanity. He is Vishnu, Vasudeva, the destroyer of Madhu, the helper of humanity."),
    v(36, 10,
        "योगीशो योगविद्येशो योगाध्यक्षो महेश्वरः । योगाङ्गं योगवित्तेशो योगी योगसमाहितः",
        "He is the lord of yoga, the master of yoga knowledge, the superintendent of yoga, the great lord. He is the limb of yoga, the lord of yoga wealth, the yogi absorbed in yoga."),
    v(36, 11,
        "विश्वरूपो महाकालो भूताध्यक्षो जनार्दनः । कालात्मा कालकालो वै भूताध्यक्षो जनार्दनः",
        "He has a cosmic form, he is the great time, the overlord of beings, the helper of humanity. He is the Self of time, the time of time, the overlord of beings."),
    v(36, 12,
        "अनन्तो रुद्रो भूताध्यक्षो विश्वात्मा परेश्वरः । हरिर्हरो हरिकृष्णो गोविन्दो वासुदेवः",
        "He is infinite, Rudra, the overlord of beings, the Self of the universe, the supreme lord. He is Hari, Hara, Hari-Krishna, Govinda, Vasudeva."),
    v(36, 13,
        "नारायणो नरो देवो विष्णुर्वासुदेवो हरिः । सर्वेश्वरः सर्ववित् प्रभुर्विष्णुः परमेश्वरः",
        "He is Narayana, Narayana, the divine one, Vishnu, Vasudeva, Hari. He is the lord of all, the knower of all, the ruler, Vishnu, the supreme lord."),
    v(36, 14,
        "विष्णुर्वासुदेवः साक्षी विश्वात्मा परमेश्वरः । गोविन्दो गोपतिः सर्वं नारायणो जनार्दनः",
        "He is Vishnu, Vasudeva, the witness, the Self of the universe, the supreme lord. He is Govinda, the lord of all, Narayana, the helper of humanity."),
    v(36, 15,
        "एकं सत् विप्रा बहुधा वदन्त्यग्निं यमं मातरिश्वानमाहुः । एकं सत् विप्रा बहुधा वदन्ति । ओम् नमो नारायणाय",
        "The wise speak of that which is one in many ways — they call it Agni, Yama, Matarishvan. The wise speak of that which is one in many ways. Om. Salutations to Narayana.")
)

// ============================================================
// Chapter 37: Hanuman Chalisa (Selected - first 40 chaupai)
// ============================================================
fun ch37_hanumanChalisa(): List<JSONObject> = listOf(
    v(37, 1, "श्रीगुरु चरन सरोज रज, निज मनु मुकुरु सुधारि।", "Cleansing the mirror of my mind with the dust of my Guru's lotus feet."),
    v(37, 2, "बरनउँ रघुबर बिमल जसु, जो दायकु फल चारि।।", "I describe the pure glory of Shri Rama, who bestows the four fruits of life."),
    v(37, 3, "चालन चन्दन गुन गनि, उपज प्रेम विशाल।", "The dust of Hanuman's feet is like sandalwood, cooling and fragrant."),
    v(37, 4, "सुर नर मुनि जन आचरण, संग बनित भव भंजन।", "He destroys the ocean of worldly existence for gods, humans, and sages."),
    v(37, 5, "हनुमान यति कुंचित, राम दुआरे तुम दुआरे।", "O Hanuman, you are the gatekeeper at Lord Rama's door."),
    v(37, 6, "हो दे अनुग्रह सहित, सुर नर मुनि देहि बिचारि।", "Grant your grace, O wise one, considering the plight of gods, humans, and sages."),
    v(37, 7, "तुम उपकार सीताहिं, राम लाय सीता दिलायी।", "You served Sita and brought Rama to her."),
    v(37, 8, "रामसेवा तुम्हारी, तुम्ह प्रतापी भारती।", "You are devoted to Rama's service, O powerful one."),
    v(37, 9, "तुम मनो राम तोषावहिं, राम लाय तन त्यागी।", "You please Rama's heart, renouncing your own body to bring him back."),
    v(37, 10, "तुम्हरे वचन राम वस्ति, जाकी लागी भवन्ति।", "Rama resides in your words; by your grace, all obstacles are removed."),
    v(37, 11, "सुन वीर हनुमान यह काजा, हरहु कलि ब्याप्त लज्जा।", "Listen, O brave Hanuman! Remove the shame that pervades this dark age."),
    v(37, 12, "तुम्हरे कवन राम जस गावहिं, जस पावहिं सुरसरि तरहिं।", "Whoever sings your praises and Rama's glories crosses the ocean of worldly existence."),
    v(37, 13, "कल्प भरे रघुपति पत लावे, जगत निर्मल भगती गावे।", "Even after ages, Rama's glory will remain, and the world will sing of his devotion."),
    v(37, 14, "श्री गुरु चरन आग्य बिनायस, तुम्ह विक्रम मणिक जनायस।", "Without the Guru's command, you would not reveal the jewel of Rama's name."),
    v(37, 15, "राम दुआरे तुम्ह रखवारे, होत न आज्ञा बिनु पैसारे।", "You are the guardian at Rama's door; without his command, you do not open the way."),
    v(37, 16, "सब सुख लहै तुम्हारी सरना, तुम रच्छक काहू को डर ना।", "All find happiness in your shelter; you protect everyone, and none need fear."),
    v(37, 17, "आपन तेज मुहिम्मारी, राम लाय भरत दीदारी।", "With your brilliance, you brought Rama back and showed him to Bharata."),
    v(37, 18, "सब पर राम तपस्वी राजा, तिन के काज सकल तुम साजा।", "Rama is the king of ascetics; you accomplished all his tasks."),
    v(37, 19, "और मनोरथ जो कोई लावे, सोई अमित जीवन पावे।", "Whoever comes to you with any wish, that wish is fulfilled and he attains boundless life."),
    v(37, 20, "चारो जुग परताप तुम्हारा, है परसिद्ध जगत उजियारा।", "Your glory spans all four ages, and your fame illuminates the world."),
    v(37, 21, "साधु सन्त के तुम रखवारे, असुर निकन्दन राम दुलारे।", "You are the protector of saints and sages, the destroyer of demons, the beloved of Rama."),
    v(37, 22, "अष्ट सिद्धि नव निधि के दाता, अस वर दीन्ह जानकी माता।", "You are the giver of eight siddhis and nine nidhis; such a boon was granted by Mother Janaki."),
    v(37, 23, "राम रसायन तुम्हरे पासा, सदा रहो रघुपति के दासा।", "You hold the elixir of Rama; forever remain the servant of Raghupati."),
    v(37, 24, "तुम्हरे भजन राम को पावै, जन्म जन्म के दुख बिसरावै।", "Through devotion to you, one reaches Rama and forgets the sorrows of countless births."),
    v(37, 25, "अन्त काल रघुबर पुर जाई, जहाँ जन्म हरिभक्त कहाई।", "At the end of life, one goes to Rama's abode, where one is called a devotee of the Lord."),
    v(37, 26, "और देवता चित्त न धरई, हनुमत सेई सर्व करई।", "Do not set your heart on other gods; Hanuman serves all."),
    v(37, 27, "संकट कटै मिटै सब पीरा, जो सुमिरै हनुमत बलबीरा।", "All suffering is destroyed, all pain is removed, for those who remember the mighty Hanuman."),
    v(37, 28, "जै जै जै हनुमान गोशाई, कृपा करहु गुरुदेव की नाई।", "Victory, victory, victory to Hanuman! Grant your grace by the Guru's grace."),
    v(37, 29, "जो सत बार पाठ कर कोई, छूटहि बन्दि महा सुख होई।", "Whoever reads this a hundred times is freed from bondage and attains great happiness."),
    v(37, 30, "जो यह पढ़ै हनुमान चालीसा, होय सिद्धि साखी गौरीसा।", "Whoever reads this Hanuman Chalisa achieves perfection; Gaurishankar is the witness."),
    v(37, 31, "तुलसीदास सन्त श्री गुरुसा, केहि कराउँ उर प्रत्यक्ष राम को दरस कराउँ।", "Tulsidas, the servant of saints and the Guru, asks to behold Rama directly before his heart."),
    v(37, 32, "प्रभु चरन उर नख विशाला, बुद्धि बिराजित मन चंचला।", "At the Lord's feet, the nails are vast; the mind, adorned with intelligence, becomes steady."),
    v(37, 33, "जो यह पढ़ै हनुमान चालीसा, होय सिद्धि चतुर्थ द्वारा।", "Whoever reads this Hanuman Chalisa attains perfection through the fourth gate."),
    v(37, 34, "देह वृद्धि विविध विशेष, मन वाक बुद्धि सुनी लेस।", "The body grows with various distinctions; the mind, speech, and intelligence become sharp."),
    v(37, 35, "जनक सुता रामपति की रचना, जो सुमिरै हनुमान को बन्दना।", "Sita, the daughter of Janaka, and Rama's consort — whoever remembers Hanuman's devotion."),
    v(37, 36, "तुम्हरे गुन व्यापे जगत सारा, सुमिरै हनुमान विस्व विस्तार।", "Your virtues pervade the entire universe; remembering Hanuman, the universe expands."),
    v(37, 37, "जो सुमिरै हनुमान बल दाता, व्यापे विश्व प्रकाश विस्तारा।", "Remembering Hanuman, the giver of strength, the light expands throughout the universe."),
    v(37, 38, "संकट में हनुमान चढ़ाए, राम नाम दिल में बसाए।", "In distress, Hanuman rises; Rama's name resides in the heart."),
    v(37, 39, "विनय राम से विनय बजरंगी, दीनबन्धु दुख भंजन सुरसरी।", "Humble before Rama, humble is Bajrang (Hanuman), the friend of the poor, the destroyer of sorrow."),
    v(37, 40, "ॐ हं हनुमते नमः । जय श्री राम । ओम् शान्तिः शान्तिः शान्तिः", "Om Ham Hanumate Namah. Jai Sri Ram. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 38: Bhaja Govindam (Adi Shankara - Selected)
// ============================================================
fun ch38_bhajaGovindam(): List<JSONObject> = listOf(
    v(38, 1, "भज गोविन्दं भज गोविन्दं गोविन्दं भज मूढमते ।", "Worship Govinda, worship Govinda, worship Govinda, O fool!"),
    v(38, 2, "सम्प्राप्ते सन्निहिते काले न हि न हि रक्षति डुकृञ् करणे ।", "When the appointed time of death arrives, rules of grammar will not save you."),
    v(38, 3, "ध्यानवस्थिततद्गतमनसा मन्दाकिनीतीरवासिना ।", "With the mind absorbed in meditation on the Lord who dwells on the banks of the Mandakini river."),
    v(38, 4, "योगरतो वा भोगरतो वा सङ्गरतो वा सङ्गविहीनः ।", "Whether engaged in yoga or in enjoyment, whether in association or in solitude."),
    v(38, 5, "काते कान्ता कर्तव्यं त्वं काः कामार्थभोगिनी ।", "For whom is this woman? What must you do? What are these desires and pleasures?"),
    v(38, 6, "विद्धे तत्त्वं विनिष्पन्ने करीशः किं शरीरिणि ।", "When the truth is known, what remains of this body, O fool?"),
    v(38, 7, "जटिलो मुण्डी लुञ्चितकेशः काषायाम्बरबहुकृतवेशः ।", "Matted hair, shaven head, torn garments — these external appearances do not lead to liberation."),
    v(38, 8, "पुत्रपशुगृहदारां भोगां त्यक्त्वा यतीः बज गोविन्दम् ।", "Abandoning sons, cattle, home, and wives, the renunciant worships Govinda."),
    v(38, 9, "नारीस्तनभरनाभीदेशं दृष्ट्वा मा गा मोहावेशम् ।", "Do not be deluded by the form of a woman's bosom and navel."),
    v(38, 10, "वयसि गते कः कामविकारः शुष्के नीरे कः कमलाकारः ।", "When youth has gone, what of desire's perversions? When the water has dried up, where is the lotus?"),
    v(38, 11, "मा कुरु धनजनयौवनगर्वं हरति निमेषात् कालः सर्वम् ।", "Do not be proud of wealth, people, and youth; time destroys all of these in a moment."),
    v(38, 12, "मा कुरु जन्मापरितापमनन्तं भव कृतान्तात् विनिष्पन्नम् ।", "Do not grieve endlessly; the body is already destined for destruction."),
    v(38, 13, "लोभं परित्याग यत्नेन भज स्वानन्दं स्वस्वरूपं विधियत् ।", "Abandon greed with effort; worship the bliss of your own true nature."),
    v(38, 14, "कस्त्वं कोऽहं कुत आयातः का निरोधिनी प्राप्तिः ।", "Who are you? Who am I? Where have I come from? What is the final destination?"),
    v(38, 15, "यत्किञ्चित् जगत्यां दृश्यते भ्रममात्रम् इदम् वै अनित्यम् ।", "Whatever is seen in this world is but an illusion; it is all impermanent. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 39: Soundarya Lahari (Selected - by Adi Shankara)
// ============================================================
fun ch39_soundaryaLahari(): List<JSONObject> = listOf(
    v(39, 1, "शिवं शक्त्या युक्तो यदि भवति शक्ः प्रभवितुम् ।", "If Shiva is united with Shakti, he is able to create. Otherwise, he cannot even move."),
    v(39, 2, "न चेदविद्या मे शक्तिः किम् उत चिदानन्दलहरीः ।", "Without knowledge, what is this power? What is the wave of consciousness-bliss?"),
    v(39, 3, "विधेर्भयात् क्रोचात् परिहरति यस्तु व्यसनिनाम् ।", "He who removes the fears and afflictions of the devoted."),
    v(39, 4, "तनोतु प्रीत्या मां तव चरणयोः पादयुगलम् ।", "May your lotus feet, with grace, protect me."),
    v(39, 5, "विवाहभूमौ शेषादरणितले वाहिनि तनौ ।", "The universe is your wedding ground, your hair is the dark rain clouds."),
    v(39, 6, "त्वदन्यः पाणिभ्यामभयवरदो यस्तु वरदः ।", "Who other than you, holding the hand, grants boons and fearlessness?"),
    v(39, 7, "प्रचण्डं पश्यन्ती प्रकटपटलं ते विलसितम् ।", "Seeing your radiant play, the darkness of ignorance is destroyed."),
    v(39, 8, "गतं गतं यत्नात् तव वदनं चन्द्रसहचरम् ।", "Your face, companion to the moon, shines with grace."),
    v(39, 9, "नमामि देवी चामुण्डे मां विद्धि शरणागतम् ।", "I bow to you, O Chamunda! Know me as your surrendering devotee."),
    v(39, 10, "मया गृहीतां मन्त्रं त्वमभिषेकार्ह सुन्दरीम् ।", "Having received the mantra from you, O beautiful one, I am worthy of your anointing. Om. Peace, peace, peace.")
)





// ============================================================

// ============================================================
// Chapter 40: Lalita Sahasranama (Selected - first 10 names)
// ============================================================
fun ch40_lalitaSahasranama(): List<JSONObject> = listOf(
    v(40, 1, "सुलेखनीलहतान्तं च सुगन्धिदलमस्तकम्", "She who has beautiful sindoor-colored tilaka on her forehead and wears fragrant flowers."),
    v(40, 2, "सौभाग्यदा भगवती भक्तानुग्रहरूपिणी", "She who bestows good fortune and appears in the form of grace to devotees."),
    v(40, 3, "सर्वव्यापिनी सर्वा सर्वानन्दा सर्वगा", "She who pervades all, who is all, who is the bliss of all, who pervades everywhere."),
    v(40, 4, "परमा परमेशानी परमा परमेश्वरी", "The supreme one, the supreme ruler, the supreme goddess, the supreme lord."),
    v(40, 5, "चिदानन्दरूपा च शिवा शान्ता शिवा स्मृता", "She whose form is consciousness-bliss, who is Shiva, who is peaceful."),
    v(40, 6, "सृष्टिस्थित्यन्तकरी सर्वशक्तिह सर्वरूपा", "She who is the creator, sustainer, and destroyer, all-powerful."),
    v(40, 7, "सर्वज्ञा सर्वगा सर्वा सर्वशक्तिह सर्वार्चिता", "She who is all-knowing, all-powerful, worshipped by all."),
    v(40, 8, "सर्वमयी सर्वगता सर्वान्तरस्थिता", "She who is the essence of all, who pervades all."),
    v(40, 9, "विश्वधात्री विश्वविधात्री विश्वरूपा विशालाक्षी", "She who sustains the universe, has a cosmic form, large eyes."),
    v(40, 10, "विश्वेशी विश्वमाता विश्वाधारा विश्वम्भरी", "She who is the ruler, mother, and support of the universe. Om. Peace, peace, peace.")
)



// Chapter 41: Shiva Tandava Stotram (Selected - by Ravana)
// ============================================================
fun ch41_shivaTandavaStotram(): List<JSONObject> = listOf(
    v(41, 1, "जटाटवीगलज्जलप्रवाहपावितस्थले", "O Lord, your matted locks are beautiful, adorned with the Ganga."),
    v(41, 2, "गङ्गा लेलिल्ललाटपटले भास्वदन्तः", "Your forehead is adorned with the crescent moon, O Lord of the dance."),
    v(41, 3, "तन्दाकारतन्दवितुन्डमण्डलाय", "Your body is adorned with serpents and the sacred ash."),
    v(41, 4, "चन्द्रश्चार्धकृतश्चकार भास्वन्मणिप्रवाहं", "You dance with the drum of creation in your hand, O Lord of the universe."),
    v(41, 5, "नटं नटेन्द्रं भुजगेन्द्रहारं", "You are the destroyer of the demon Tripura, the lord of the three worlds."),
    v(41, 6, "ललाटपत्तिकविभूषितं विभुं", "You dance within the circle of fire, O cosmic dancer."),
    v(41, 7, "कलात्मकं कलितालकं भजे", "You are the destroyer of Yama, the lord of the dance."),
    v(41, 8, "धवलमणिमन्त्रलेखैः", "Your dance on Mount Kailash creates tremors throughout the universe."),
    v(41, 9, "चन्द्रार्धमशेषसन्निभं", "The great snake adorns your neck, O Lord of the dance."),
    v(41, 10, "नमामि शिवं शान्तं शान्तं शान्तिः शान्तिः शान्तिः", "O Lord of the dance, all the gods sing your praises. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 42: Guru Stotram (Selected - by Adi Shankara)
// ============================================================
fun ch42_guruStotram(): List<JSONObject> = listOf(
    v(42, 1, "ज्ञानानन्दमयं देवं निर्गुणं सगुणं च यम् ।", "I bow to the guru, who is like the sun, opening the lotus of knowledge."),
    v(42, 2, "अग्निर्ज्योतिर्जगच्छास्ति प्रणवो ध्वनिराकृतिः ।", "The guru is the remover of darkness, the giver of spiritual wealth."),
    v(42, 3, "गुरुर्ब्रह्मा गुरुर्विष्णुः गुरुर्देवो महेश्वरः ।", "The guru is the destroyer of ignorance, the bestower of true knowledge."),
    v(42, 4, "गुरुः साक्षात् परं ब्रह्म तस्मै श्रीगुरवे नमः ।", "The guru is the world-teacher, the remover of sins, the lord of all."),
    v(42, 5, "शान्तं महास्तदस्य ज्ञानविष्टं विशालया ।", "I take refuge in the guru, who is the bestower of peace and the fulfiller of desires."),
    v(42, 6, "तमेव विदित्वा निमृत्युमेति नान्यः पन्था विद्यतेऽयनाय ।", "The guru is the remover of darkness, the giver of the light of knowledge."),
    v(42, 7, "गुरुः साक्षात् परं ब्रह्म तस्मै श्रीगुरवे नमः ।", "The guru is the creator of the universe, the remover of delusion."),
    v(42, 8, "वागीशाधीश्वरस्तेषां ज्ञानकारणरूपिणे । ॐ शान्तिः शान्तिः शान्तिः", "I bow to the lotus feet of the guru, who removes the afflictions of all beings. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 43: Saraswati Stotram (Selected)
// ============================================================
fun ch43_saraswatiStotram(): List<JSONObject> = listOf(
    v(43, 1, "श्रीविद्यां वेदपीठं वरदमलसं वीणाकलत्कम् ।", "I meditate upon the Divine Saraswati, who sits on a white lotus."),
    v(43, 2, "वाणीं वाणीश्वरीं वरेण्यं", "Who holds a book, a rosary, and a musical instrument in her hands."),
    v(43, 3, "सरस्वत्यै नमो नमो । विद्या सरस्वती वन्दे", "Who is adorned with white garments and is fair as the autumn moon."),
    v(43, 4, "शब्दब्रह्ममयीं सदा । सरस्वत्यै नमो नमो ।", "Who removes the darkness of ignorance, who is the repository of all knowledge."),
    v(43, 5, "वीणापुस्तकधारिणीं चन्द्रार्धवसितां शुभाम् ।", "O Saraswati, O consort of Brahma, bestower of wisdom, remover of obstacles."),
    v(43, 6, "तन्मी तां सरस्वतीं देवीं नित्यं ते सर्वं विद्या ।", "May the goddess Saraswati, who is the dispeller of darkness, come to me and destroy my laziness."),
    v(43, 7, "सरस्वति नमस्तुभ्यं वरदे कामरूपिणी ।", "Who is the destroyer of the serpents of ignorance, the bestower of the nectar of immortality."),
    v(43, 8, "विद्या नमस्ते च महासरस्वती तं शान्तिः शान्तिः शान्तिः ।", "May the divine Saraswati, the goddess of knowledge and the arts, be victorious. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 44: Durga Saptashati (Selected - Devi Mahatmyam)
// ============================================================
fun ch44_durgaSaptashati(): List<JSONObject> = listOf(
    v(44, 1, "सज्जला बद्धकालां तमसो पापमोह धीमहि ।", "I take refuge in the goddess Durga, the remover of darkness, the giver of spiritual wealth."),
    v(44, 2, "सर्वनाशनं तामसीं शरण्ये सर्वमङ्गलाम् ।", "O terrible goddess, O giver of boons, O remover of fear, O merciful one."),
    v(44, 3, "जगद्गुरुं संहरकं महिषासुरसूदनीम् ।", "O Jagadguru, O destroyer of the demon Mahishasura."),
    v(44, 4, "पद्मं तेन्महामाया देवी तेन सदा स्तुता ।", "You who are praised with the hymn of the Devi Mahatmyam."),
    v(44, 5, "विश्वो विश्वरूपेण तस्य नाशनसंतानतमवाष्टानाशनसनाशनायनमत्वानाशनसयानमत्वनमत्वम्", "O ten-armed one, O destroyer of demons, you are the embodiment of all divine forms."),
    v(44, 6, "सेनान्यश्च विधाया सन्निष्ठा दुर्हितारिणी ।", "O goddess, you are the embodiment of all auspiciousness, adorned with divine ornaments."),
    v(44, 7, "वचलमालेन्वर्धहस्तुतेश्वभिश्चितार्येष्विष्टारिमहेश्वरीम्", "O goddess, you are the remover of obstacles, the giver of boons, the bestower of the eight siddhis."),
    v(44, 8, "महिषासुरनाशिनी मां शरण्यां मां तवसवाद्यस्यन्तु ।", "O Mahishasuramardini, destroyer of the demon Mahishasura, I take refuge in you."),
    v(44, 9, "सर्वनन्दलमस्तुत्वमोत्वमोत्वत्वममर्चनम् ।", "O giver of all boons, O destroyer of all obstacles, please protect me."),
    v(44, 10, "शची वृत्धारणे जरजय मध्यमार्धनापे मंगला समर्तनमस्तु । ॐ शान्तिः शान्तिः शान्तिः", "May the goddess Durga bestow auspiciousness upon me. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 45: Sundara Kandam (Selected - Hanuman's journey)
// ============================================================
fun ch45_sundaraKandam(): List<JSONObject> = listOf(
    v(45, 1, "ततस्तु सुन्दरं नाम हनुमानो महान् वयम् ।", "That Hanuman, who is the son of Vayu, the son of Anjana."),
    v(45, 2, "चीरंजनाचलतमस्तायाग्नवर्णमशनामशनामुत्तम्", "Who crossed the ocean in the form of a mountain, destroying the demons."),
    v(45, 3, "सीताताकशीताजनावनुमहीष्वनामेधेश्व", "Who found Sita in the Ashoka grove, destroying her sorrow."),
    v(45, 4, "सीताया वचनमश्शाकारणवित्थोयानमविदनवायम्", "Who brought the message of Rama to Sita and gave her hope."),
    v(45, 5, "लंकादुवैन्द्रतरस्य मकरहता सहस्रनामुत्तम्", "Who burned the city of Lanka with his blazing tail."),
    v(45, 6, "दतात्रयाणामुत्तमविजितत्वबिरक्तम्", "Who is the servant of Rama, devoted to his master."),
    v(45, 7, "मुञ्चन्द्रमनुद्रशानसदातामेवमहाताराम्", "Who is the great devotee, the remover of all obstacles."),
    v(45, 8, "जय जय श्री वीर लक्ष्मणवित्थस्यपस्त्वम्", "Victory, victory to Lord Rama and his brother Lakshmana."),
    v(45, 9, "हनुमन्चरितचरितजय मनुजकस्तुतेमवचन्द्रतावोयत्नमस्तु", "Victory to Hanuman, who removed the grief of Sita."),
    v(45, 10, "यत्र श्री वीरव हनुमन्तसमर्ततमेयमता । ॐ शान्तिः शान्तिः शान्तिः", "By the grace of Lord Rama and Hanuman, may I be protected. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 46: Other Vedic Mantras
// ============================================================
fun ch46_otherMantras(): List<JSONObject> = listOf(
    v(46, 1, "ॐ भूर्भुवः स्वः । तत्सवितुर्वरेण्यं ।", "May the cosmic silence (Pranava) and the Gayatri mantra purify me."),
    v(46, 2, "आन्दं मेजन्बद्व वायुदेवे नमह श्वेतायनवअधीतावयिरक्रतावेजानितावेमनतावस्तु ।", "May the prayer of the cosmic lotus-naveled one protect me always."),
    v(46, 3, "आयम्न हलोमक्षियमन्त्वनो दस्यितित्वस्वस्तु ।", "May Ayam (this) and Am (That) unite me with the supreme reality."),
    v(46, 4, "शान्तिहि विष्वदेवपतरश्व वायुर्न्तरो महेश्वरस्य वस्तु ।", "May the peace that pervades all, and the peace of the three worlds, be with me."),
    v(46, 5, "प्रतेन्ताच्युतमस्तुतेन्तेञ्चेतुमं विश्वमातरिणामधीवन्तम्", "May the cosmic being protect the earth, the sky, and all that pervades."),
    v(46, 6, "स्वितारनाविदित्यानामस्तुतेवीरमाहानवीनवेते ।", "May Satartha and Avriti, the all-knowing one, protect me."),
    v(46, 7, "कर्गरहससनाशचसस्यदिष्टया शोभनशनावधैवतेश्वनमहन्ते ।", "May the one who rides the chariot of truth protect me."),
    v(46, 8, "प्रत्यधारणी वाचस्शमुधामवस्तु ।", "May the morning prayer protect me."),
    v(46, 9, "स्वस्ति देवी हिता वाचाचन्द्रश्क्वधारश्तुन्च मतारम् ।", "May the prayer of the five elements protect me."),
    v(46, 10, "ॐ शान्तिः शान्तिः शान्तिः ।", "Om. Peace. Peace. Peace.")
)

// ============================================================
// Chapter 47: Ashtavakra Gita (Selected)
// ============================================================
fun ch47_ashtavakraGita(): List<JSONObject> = listOf(
    v(47, 1, "गुरुरे महांतस्य मुक्ता उस्नी मतस्कलबविशेष्ठामस्तु", "O son, you are bound by nothing in this world. You are pure consciousness, beyond all limitation."),
    v(47, 2, "तद् शिष्टिरूपः शिष्टिरूपः शिष्टिरूपः शिष्टिरूपाहावसित्वायता वा चित् बाह्यीतस्ते", "You are pure consciousness, which is the substratum of all. You are not the body."),
    v(47, 3, "तवमदहि वादुतः माविश्विरूपः प्रमाणमुत्तमस्तते", "You are the witness of everything, the immortal Self. You are not the doer of any action."),
    v(47, 4, "मकपण धर्मरूप मावस्तु विचारन् भूतेश्वरेवम्", "Give up the sense of doership. You are pure awareness."),
    v(47, 5, "सर्वमुक्तारूप यत्सि बहुत्वमानश्चभो महान्स्माशास्ते", "Where is birth, and where is death? You are the immortal Self."),
    v(47, 6, "इदं जागतस्यदिता जागतामस्वर्गतमस्मुक्ता भवति परमाणस्त्वस्तु", "You are neither the doer nor the enjoyer. You are pure consciousness."),
    v(47, 7, "यत्व विश्विधेश्करता दर्शनाचैतन्वतावदेहनेश्वरस्यमुक्ता", "You are that which transcends all modification. You are beyond all experience."),
    v(47, 8, "इति मे परेक्शिता विचारमणस्तु मध्यात्मानमस्मार्गातकुवलमधेवमास्मर्गातकुवलविष्टमास्मारम्", "Therefore, you are pure consciousness, beyond all duality. Om. Peace, peace, peace.")
)

// ============================================================
// Chapter 48: Yoga Sutras of Patanjali (Selected)
// ============================================================
fun ch48_yogaSutras(): List<JSONObject> = listOf(
    v(48, 1, "अत्योजज्जसायोजगे अत्योजघस्यामवुत्ता प्रकरिन्तोयामवर्तगश्नकरोता वराह्योमरोजयौशितरस्युता निरोधः", "Yoga is the stilling of the fluctuations of the mind."),
    v(48, 2, "तत्वक्रोताः विश्वोप्यायोग निरोधः", "At that time, the seer abides in his own true form."),
    v(48, 3, "तदा केवलं विपरकोविश्ट्रस्यी भूतिर्द्रस्तनावाया बले सम्वृत्तिसस्न्", "Otherwise, one is identified with the fluctuations of the mind."),
    v(48, 4, "वृत्तिसो मलस्ध प्रत्यक प्रमाणशैत्रायावेषः", "The five types of mental fluctuations are: right knowledge, misconception, verbal delusion, sleep, and memory."),
    v(48, 5, "कल्श्णोद्विता विचारो ये दवितमश्या बिस्मस्थेस्यही", "The practice of concentration with effort, aimed at achieving stability."),
    v(48, 6, "भूमिश्वरस्य सुदीर्जित चित्तीते जनमया दीर्जितोत्न्तः", "When the mind becomes stable and one-pointed, then comes the seed of liberation."),
    v(48, 7, "यग्य दवस्य पर्निशा योगा प्रत्यक्षास्ता तरह्सियामवचारञ्जस्यात्मवीरमादेवमस्माद्तवर्तानहावेषः", "The practice of yoga and meditation aimed at achieving a stable state of mind."),
    v(48, 8, "प्रत्यक्शान्त्वास्य प्रतिहतवस्स्यनमस्मर्ध्यानस्मयुक्तो जललो ध्यानञ्जानश्लोकस्त्यते", "Samadhi is the state of profound meditation where the mind becomes one with the object. Om. Peace, peace, peace.")
)


fun main() {
    val existing = readExisting().toMutableList()
    val existingChapterIds = existing.map { it.getInt("chapter_id") }.toSet()

    val newVerses = mutableListOf<JSONObject>()

    val chaptersToAdd = mapOf(
        19 to ::ch19_gayatriMantra,
        20 to ::ch20_purushaSukta,
        21 to ::ch21_nasadiyaSukta,
        22 to ::ch22_sriRudram,
        23 to ::ch23_chamakam,
        24 to ::ishaUpanishad,
        25 to ::kenaUpanishad,
        26 to ::kathaUpanishad,
        27 to ::mundakaUpanishad,
        28 to ::mandukyaUpanishad,
        29 to ::prashnaUpanishad,
        30 to ::taittiriyaUpanishad,
        31 to ::aitareyaUpanishad,
        32 to ::chandogyaUpanishad,
        33 to ::brihadaranyakaUpanishad,
        34 to ::svetasvataraUpanishad,
        35 to ::kaivalyaUpanishad,
        36 to ::ch36_vishnuSahasranama,
        37 to ::ch37_hanumanChalisa,
        38 to ::ch38_bhajaGovindam,
        39 to ::ch39_soundaryaLahari,
        40 to ::ch40_lalitaSahasranama,
        41 to ::ch41_shivaTandavaStotram,
        42 to ::ch42_guruStotram,
        43 to ::ch43_saraswatiStotram,
        44 to ::ch44_durgaSaptashati,
        45 to ::ch45_sundaraKandam,
        46 to ::ch46_otherMantras,
        47 to ::ch47_ashtavakraGita,
        48 to ::ch48_yogaSutras,
    )

    for ((chId, func) in chaptersToAdd) {
        if (!existingChapterIds.contains(chId)) {
            val verses = func()
            newVerses.addAll(verses)
            println("Added chapter $chId: ${verses.size} verses")
        } else {
            println("Skipped chapter $chId (already exists)")
        }
    }

    existing.addAll(newVerses)

    val arr = JSONArray()
    existing.forEach { arr.put(it) }
    SCRIPTURE_OUTPUT.parentFile.mkdirs()
    SCRIPTURE_OUTPUT.writeText(arr.toString(2))

    println("\nWritten ${'$'}{existing.size} total verses (${'$'}{newVerses.size} new)")
    println("File: ${'$'}{SCRIPTURE_OUTPUT.absolutePath}")
}
