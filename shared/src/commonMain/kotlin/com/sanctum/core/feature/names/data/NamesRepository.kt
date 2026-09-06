package com.sanctum.core.feature.names.data

import com.russhwolf.settings.Settings
import com.sanctum.core.feature.names.domain.DivineName
import kotlinx.serialization.json.Json

class NamesRepository(
    private val settings: Settings,
) {
    private val json = Json { ignoreUnknownKeys = true }
    private var cachedNames: List<DivineName>? = null

    private val embeddedJson: String = """
[
  {"id":1,"arabic":"الرَّحْمَٰنُ","transliteration":"Ar-Rahman","meaning":"The Beneficent","explanation":"The One who has plenty of mercy for the believers and the disobedient among the creation.","audioFileName":"names_001.mp3"},
  {"id":2,"arabic":"الرَّحِيمُ","transliteration":"Ar-Raheem","meaning":"The Most Merciful","explanation":"The One who has perfect mercy for the believers.","audioFileName":"names_002.mp3"},
  {"id":3,"arabic":"الْمَلِكُ","transliteration":"Al-Malik","meaning":"The Absolute Ruler","explanation":"The One with the complete dominion, the One whose dominion is clear from imperfection.","audioFileName":"names_003.mp3"},
  {"id":4,"arabic":"الْقُدُّوسُ","transliteration":"Al-Quddus","meaning":"The Pure One","explanation":"The One who is pure from any imperfection, clear from evil and sin.","audioFileName":"names_004.mp3"},
  {"id":5,"arabic":"السَّلَامُ","transliteration":"As-Salaam","meaning":"The Source of Peace","explanation":"The One who is free from every imperfection, from Whom the believers seek shelter.","audioFileName":"names_005.mp3"},
  {"id":6,"arabic":"الْمُؤْمِنُ","transliteration":"Al-Mu'min","meaning":"The Guardian of Faith","explanation":"The One with Whom all hearts find peace, and Who grants safety and security.","audioFileName":"names_006.mp3"},
  {"id":7,"arabic":"الْمُهَيْمِنُ","transliteration":"Al-Muhaymin","meaning":"The Protector","explanation":"The One who witnesses the actions of His servants and watches over them.","audioFileName":"names_007.mp3"},
  {"id":8,"arabic":"الْعَزِيزُ","transliteration":"Al-Azeez","meaning":"The Almighty","explanation":"The One whose strength overcomes all others, the Most Mighty.","audioFileName":"names_008.mp3"},
  {"id":9,"arabic":"الْجَبَّارُ","transliteration":"Al-Jabbar","meaning":"The Compeller","explanation":"The One who prevails over His servants by His power, and mends what is broken.","audioFileName":"names_009.mp3"},
  {"id":10,"arabic":"الْمُتَكَبِّرُ","transliteration":"Al-Mutakabbir","meaning":"The Supreme","explanation":"The One who is far above being comparable to His creation.","audioFileName":"names_010.mp3"},
  {"id":11,"arabic":"الْخَالِقُ","transliteration":"Al-Khaliq","meaning":"The Creator","explanation":"The One who brought everything from non-existence into existence.","audioFileName":"names_011.mp3"},
  {"id":12,"arabic":"الْبَارِئُ","transliteration":"Al-Bari","meaning":"The Evolver","explanation":"The One who has the power to originate and fashion creation with wisdom.","audioFileName":"names_012.mp3"},
  {"id":13,"arabic":"الْمُصَوِّرُ","transliteration":"Al-Musawwir","meaning":"The Fashioner","explanation":"The One who fashions every creation in a beautiful form.","audioFileName":"names_013.mp3"},
  {"id":14,"arabic":"الْغَفَّارُ","transliteration":"Al-Ghaffar","meaning":"The Forgiver","explanation":"The One who forgives His servants again and again.","audioFileName":"names_014.mp3"},
  {"id":15,"arabic":"الْقَهَّارُ","transliteration":"Al-Qahhar","meaning":"The Subduer","explanation":"The One whose power overwhelms everything in the heavens and earth.","audioFileName":"names_015.mp3"},
  {"id":16,"arabic":"الْوَهَّابُ","transliteration":"Al-Wahhab","meaning":"The Bestower","explanation":"The One who gives freely without any reward or compensation.","audioFileName":"names_016.mp3"},
  {"id":17,"arabic":"الرَّزَّاقُ","transliteration":"Ar-Razzaq","meaning":"The Provider","explanation":"The One who provides sustenance for all His creatures.","audioFileName":"names_017.mp3"},
  {"id":18,"arabic":"الْفَتَّاحُ","transliteration":"Al-Fattah","meaning":"The Opener","explanation":"The One who opens the gates of mercy and guidance for His servants.","audioFileName":"names_018.mp3"},
  {"id":19,"arabic":"الْعَلِيمُ","transliteration":"Al-Aleem","meaning":"The All-Knowing","explanation":"The One who knows everything without any effort.","audioFileName":"names_019.mp3"},
  {"id":20,"arabic":"الْقَابِضُ","transliteration":"Al-Qabid","meaning":"The Restrictor","explanation":"The One who withholds and restricts as He wills.","audioFileName":"names_020.mp3"},
  {"id":21,"arabic":"الْبَاسِطُ","transliteration":"Al-Basit","meaning":"The Reliever","explanation":"The One who expands and relieves as He wills.","audioFileName":"names_021.mp3"},
  {"id":22,"arabic":"الْخَافِضُ","transliteration":"Al-Khafid","meaning":"The Abaser","explanation":"The One who lowers whoever He wills by His wisdom.","audioFileName":"names_022.mp3"},
  {"id":23,"arabic":"الرَّافِعُ","transliteration":"Ar-Rafi","meaning":"The Exalter","explanation":"The One who raises and elevates whoever He wills.","audioFileName":"names_023.mp3"},
  {"id":24,"arabic":"الْمُعِزُّ","transliteration":"Al-Mu'izz","meaning":"The Bestower of Honor","explanation":"The One who gives honor and dignity to whom He wills.","audioFileName":"names_024.mp3"},
  {"id":25,"arabic":"الْمُذِلُّ","transliteration":"Al-Muzill","meaning":"The Humiliator","explanation":"The One who humbles whoever He wills.","audioFileName":"names_025.mp3"},
  {"id":26,"arabic":"السَّمِيعُ","transliteration":"As-Samee","meaning":"The All-Hearing","explanation":"The One who hears all sounds without any ears.","audioFileName":"names_026.mp3"},
  {"id":27,"arabic":"الْبَصِيرُ","transliteration":"Al-Baseer","meaning":"The All-Seeing","explanation":"The One who sees all things without any eyes.","audioFileName":"names_027.mp3"},
  {"id":28,"arabic":"الْحَكَمُ","transliteration":"Al-Hakam","meaning":"The Judge","explanation":"The One whose judgment is final and just.","audioFileName":"names_028.mp3"},
  {"id":29,"arabic":"الْعَدْلُ","transliteration":"Al-Adl","meaning":"The Just","explanation":"The One who is perfectly just in all His judgments.","audioFileName":"names_029.mp3"},
  {"id":30,"arabic":"اللَّطِيفُ","transliteration":"Al-Lateef","meaning":"The Subtle One","explanation":"The One whose knowledge is subtle and gentle with His servants.","audioFileName":"names_030.mp3"},
  {"id":31,"arabic":"الْخَبِيرُ","transliteration":"Al-Khabeer","meaning":"The All-Aware","explanation":"The One who is aware of all things, even the innermost secrets.","audioFileName":"names_031.mp3"},
  {"id":32,"arabic":"الْحَلِيمُ","transliteration":"Al-Haleem","meaning":"The Forbearing","explanation":"The One who is slow to anger and quick to forgive.","audioFileName":"names_032.mp3"},
  {"id":33,"arabic":"الْعَظِيمُ","transliteration":"Al-Azeem","meaning":"The Magnificent","explanation":"The One who is supremely great and majestic.","audioFileName":"names_033.mp3"},
  {"id":34,"arabic":"الْغَفُورُ","transliteration":"Al-Ghafoor","meaning":"The Forgiving","explanation":"The One who forgives sins again and again.","audioFileName":"names_034.mp3"},
  {"id":35,"arabic":"الشَّكُورُ","transliteration":"Ash-Shakoor","meaning":"The Appreciative","explanation":"The One who acknowledges and rewards even the smallest good deed.","audioFileName":"names_035.mp3"},
  {"id":36,"arabic":"الْعَلِيُّ","transliteration":"Al-Aliyy","meaning":"The Most High","explanation":"The One who is exalted in status and above all His creation.","audioFileName":"names_036.mp3"},
  {"id":37,"arabic":"الْكَبِيرُ","transliteration":"Al-Kabeer","meaning":"The Most Great","explanation":"The One who is the greatest in every way.","audioFileName":"names_037.mp3"},
  {"id":38,"arabic":"الْحَفِيظُ","transliteration":"Al-Hafeedh","meaning":"The Preserver","explanation":"The One who preserves and protects all His creation.","audioFileName":"names_038.mp3"},
  {"id":39,"arabic":"الْمُقِيتُ","transliteration":"Al-Muqeet","meaning":"The Sustainer","explanation":"The One who sustains and nourishes every living thing.","audioFileName":"names_039.mp3"},
  {"id":40,"arabic":"الْحَسِيبُ","transliteration":"Al-Haseeb","meaning":"The Reckoner","explanation":"The One who takes account of every deed.","audioFileName":"names_040.mp3"},
  {"id":41,"arabic":"الْجَلِيلُ","transliteration":"Al-Jaleel","meaning":"The Majestic","explanation":"The One who possesses majesty and greatness.","audioFileName":"names_041.mp3"},
  {"id":42,"arabic":"الْكَرِيمُ","transliteration":"Al-Kareem","meaning":"The Generous","explanation":"The One who is extremely generous and gives abundantly.","audioFileName":"names_042.mp3"},
  {"id":43,"arabic":"الرَّقِيبُ","transliteration":"Ar-Raqeeb","meaning":"The Watchful","explanation":"The One who watches over everything His servants do.","audioFileName":"names_043.mp3"},
  {"id":44,"arabic":"الْمُجِيبُ","transliteration":"Al-Mujeeb","meaning":"The Responder","explanation":"The One who answers prayers and responds to needs.","audioFileName":"names_044.mp3"},
  {"id":45,"arabic":"الْوَاسِعُ","transliteration":"Al-Wasi","meaning":"The All-Encompassing","explanation":"The One whose knowledge and mercy encompass everything.","audioFileName":"names_045.mp3"},
  {"id":46,"arabic":"الْحَكِيمُ","transliteration":"Al-Hakeem","meaning":"The All-Wise","explanation":"The One who is perfectly wise in all His actions and decrees.","audioFileName":"names_046.mp3"},
  {"id":47,"arabic":"الْوَدُودُ","transliteration":"Al-Wadud","meaning":"The Loving","explanation":"The One who loves His righteous servants and is loved by them.","audioFileName":"names_047.mp3"},
  {"id":48,"arabic":"الْمَجِيدُ","transliteration":"Al-Majeed","meaning":"The Glorious","explanation":"The One who is glorious and worthy of all praise.","audioFileName":"names_048.mp3"},
  {"id":49,"arabic":"الْبَاعِثُ","transliteration":"Al-Ba'ith","meaning":"The Resurrector","explanation":"The One who will resurrect all of creation on the Day of Judgment.","audioFileName":"names_049.mp3"},
  {"id":50,"arabic":"الشَّهِيدُ","transliteration":"Ash-Shaheed","meaning":"The Witness","explanation":"The One who witnesses everything at all times.","audioFileName":"names_050.mp3"},
  {"id":51,"arabic":"الْحَقُّ","transliteration":"Al-Haqq","meaning":"The Truth","explanation":"The One who is the ultimate truth and reality.","audioFileName":"names_051.mp3"},
  {"id":52,"arabic":"الْوَكِيلُ","transliteration":"Al-Wakeel","meaning":"The Trustee","explanation":"The One who is completely trusted to manage all affairs.","audioFileName":"names_052.mp3"},
  {"id":53,"arabic":"الْقَوِيُّ","transliteration":"Al-Qawee","meaning":"The Strong","explanation":"The One whose strength is invincible and never diminishes.","audioFileName":"names_053.mp3"},
  {"id":54,"arabic":"الْمَتِينُ","transliteration":"Al-Mateen","meaning":"The Firm One","explanation":"The One whose power is strong and firm.","audioFileName":"names_054.mp3"},
  {"id":55,"arabic":"الْوَلِيُّ","transliteration":"Al-Waliyy","meaning":"The Protector","explanation":"The One who is the guardian and protector of the believers.","audioFileName":"names_055.mp3"},
  {"id":56,"arabic":"الْحَمِيدُ","transliteration":"Al-Hameed","meaning":"The Praiseworthy","explanation":"The One who is worthy of all praise and thanks.","audioFileName":"names_056.mp3"},
  {"id":57,"arabic":"الْمُحْصِي","transliteration":"Al-Muhsi","meaning":"The Accounter","explanation":"The One who counts and enumerates every single thing.","audioFileName":"names_057.mp3"},
  {"id":58,"arabic":"الْمُبْدِئُ","transliteration":"Al-Mubdi","meaning":"The Originator","explanation":"The One who creates for the first time.","audioFileName":"names_058.mp3"},
  {"id":59,"arabic":"الْمُعِيدُ","transliteration":"Al-Mu'eed","meaning":"The Restorer","explanation":"The One who brings creation back to life.","audioFileName":"names_059.mp3"},
  {"id":60,"arabic":"الْمُحْيِي","transliteration":"Al-Muhyee","meaning":"The Giver of Life","explanation":"The One who gives life to the dead and animates all creation.","audioFileName":"names_060.mp3"},
  {"id":61,"arabic":"الْمُمِيتُ","transliteration":"Al-Mumeet","meaning":"The Bringer of Death","explanation":"The One who causes death by His wisdom and will.","audioFileName":"names_061.mp3"},
  {"id":62,"arabic":"الْحَيُّ","transliteration":"Al-Hayy","meaning":"The Ever-Living","explanation":"The One who is eternally alive and never dies.","audioFileName":"names_062.mp3"},
  {"id":63,"arabic":"الْقَيُّومُ","transliteration":"Al-Qayyoom","meaning":"The Self-Sustaining","explanation":"The One who exists by Himself and sustains everything else.","audioFileName":"names_063.mp3"},
  {"id":64,"arabic":"الْوَاجِدُ","transliteration":"Al-Wajid","meaning":"The Finder","explanation":"The One who is never in need and always finds what He seeks.","audioFileName":"names_064.mp3"},
  {"id":65,"arabic":"الْمَاجِدُ","transliteration":"Al-Maajid","meaning":"The Illustrious","explanation":"The One who possesses glory and honor.","audioFileName":"names_065.mp3"},
  {"id":66,"arabic":"الْواحِدُ","transliteration":"Al-Waahid","meaning":"The One","explanation":"The One who is unique and without any partner.","audioFileName":"names_066.mp3"},
  {"id":67,"arabic":"الصَّمَدُ","transliteration":"As-Samad","meaning":"The Eternal","explanation":"The One who is self-sufficient and on Whom all depend.","audioFileName":"names_067.mp3"},
  {"id":68,"arabic":"الْقَادِرُ","transliteration":"Al-Qadir","meaning":"The All-Powerful","explanation":"The One whose power can do all things.","audioFileName":"names_068.mp3"},
  {"id":69,"arabic":"الْمُقْتَدِرُ","transliteration":"Al-Muqtadir","meaning":"The Creator of All Power","explanation":"The One who has complete dominion and authority.","audioFileName":"names_069.mp3"},
  {"id":70,"arabic":"الْمُقَدِّمُ","transliteration":"Al-Muqaddim","meaning":"The Expediter","explanation":"The One who brings forward or advances whatever He wills.","audioFileName":"names_070.mp3"},
  {"id":71,"arabic":"الْمُؤَخِّرُ","transliteration":"Al-Mu'akhkhir","meaning":"The Delayer","explanation":"The One who delays or puts back whatever He wills.","audioFileName":"names_071.mp3"},
  {"id":72,"arabic":"الأوَّلُ","transliteration":"Al-Awwal","meaning":"The First","explanation":"The One who is before all creation.","audioFileName":"names_072.mp3"},
  {"id":73,"arabic":"الآخِرُ","transliteration":"Al-Aakhir","meaning":"The Last","explanation":"The One who will remain after all creation has perished.","audioFileName":"names_073.mp3"},
  {"id":74,"arabic":"الظَّاهِرُ","transliteration":"Az-Zaahir","meaning":"The Manifest","explanation":"The One whose existence is clear and evident.","audioFileName":"names_074.mp3"},
  {"id":75,"arabic":"الْبَاطِنُ","transliteration":"Al-Baatin","meaning":"The Hidden","explanation":"The One whose true essence is hidden from human perception.","audioFileName":"names_075.mp3"},
  {"id":76,"arabic":"الْوَالِي","transliteration":"Al-Waali","meaning":"The Governor","explanation":"The One who governs and manages all affairs of creation.","audioFileName":"names_076.mp3"},
  {"id":77,"arabic":"الْمُتَعَالِي","transliteration":"Al-Muta'ali","meaning":"The Self-Exalted","explanation":"The One who is exalted and transcendent above all imperfections.","audioFileName":"names_077.mp3"},
  {"id":78,"arabic":"الْبَرُّ","transliteration":"Al-Barr","meaning":"The Source of Goodness","explanation":"The One who is extremely kind and righteous.","audioFileName":"names_078.mp3"},
  {"id":79,"arabic":"التَّوَّابُ","transliteration":"At-Tawwab","meaning":"The Acceptor of Repentance","explanation":"The One who accepts repentance from His servants and forgives sins.","audioFileName":"names_079.mp3"},
  {"id":80,"arabic":"الْمُنْتَقِمُ","transliteration":"Al-Muntaqim","meaning":"The Avenger","explanation":"The One who takes retribution against the wrongdoers.","audioFileName":"names_080.mp3"},
  {"id":81,"arabic":"الْعَفُوُّ","transliteration":"Al-Afuw","meaning":"The Pardoner","explanation":"The One who pardons sins and erases bad deeds.","audioFileName":"names_081.mp3"},
  {"id":82,"arabic":"الرَّؤُوفُ","transliteration":"Ar-Ra'uf","meaning":"The Compassionate","explanation":"The One who is extremely kind and compassionate to His servants.","audioFileName":"names_082.mp3"},
  {"id":83,"arabic":"مَالِكُ الْمُلْكِ","transliteration":"Maalik-ul-Mulk","meaning":"The Owner of Sovereignty","explanation":"The One who owns absolute dominion and sovereignty.","audioFileName":"names_083.mp3"},
  {"id":84,"arabic":"ذُو الْجَلَالِ وَالْإِكْرَامِ","transliteration":"Dhul-Jalaali wal-Ikraam","meaning":"The Lord of Majesty and Honor","explanation":"The One who possesses glory, majesty, and generosity.","audioFileName":"names_084.mp3"},
  {"id":85,"arabic":"الْمُقْسِطُ","transliteration":"Al-Muqsit","meaning":"The Equitable","explanation":"The One who acts with perfect justice and equity.","audioFileName":"names_085.mp3"},
  {"id":86,"arabic":"الْجَامِعُ","transliteration":"Al-Jami","meaning":"The Gatherer","explanation":"The One who gathers all creation on the Day of Judgment.","audioFileName":"names_086.mp3"},
  {"id":87,"arabic":"الْغَنِيُّ","transliteration":"Al-Ghaniyy","meaning":"The Self-Sufficient","explanation":"The One who is free from all needs and depends on nothing.","audioFileName":"names_087.mp3"},
  {"id":88,"arabic":"الْمُغْنِي","transliteration":"Al-Mugni","meaning":"The Enricher","explanation":"The One who enriches those who are in need.","audioFileName":"names_088.mp3"},
  {"id":89,"arabic":"الْمَانِعُ","transliteration":"Al-Mani","meaning":"The Preventer","explanation":"The One who prevents harm and guides away from evil.","audioFileName":"names_089.mp3"},
  {"id":90,"arabic":"الضَّارَّ","transliteration":"Ad-Daarr","meaning":"The Distresser","explanation":"The One who sends trials and hardship by His wisdom.","audioFileName":"names_090.mp3"},
  {"id":91,"arabic":"النَّافِعُ","transliteration":"An-Nafi","meaning":"The Benefiter","explanation":"The One who grants benefit and good to His servants.","audioFileName":"names_091.mp3"},
  {"id":92,"arabic":"النُّورُ","transliteration":"An-Noor","meaning":"The Light","explanation":"The One who is the light of the heavens and the earth.","audioFileName":"names_092.mp3"},
  {"id":93,"arabic":"الْهَادِي","transliteration":"Al-Haadi","meaning":"The Guide","explanation":"The One who guides His servants to the right path.","audioFileName":"names_093.mp3"},
  {"id":94,"arabic":"الْبَدِيعُ","transliteration":"Al-Badee","meaning":"The Incomparable","explanation":"The One who creates in unique and unprecedented ways.","audioFileName":"names_094.mp3"},
  {"id":95,"arabic":"الْبَاقِي","transliteration":"Al-Baqi","meaning":"The Everlasting","explanation":"The One whose existence is eternal and never ends.","audioFileName":"names_095.mp3"},
  {"id":96,"arabic":"الْوَارِثُ","transliteration":"Al-Waarith","meaning":"The Inheritor","explanation":"The One who inherits everything after creation perishes.","audioFileName":"names_096.mp3"},
  {"id":97,"arabic":"الرَّشِيدُ","transliteration":"Ar-Rasheed","meaning":"The Righteous Guide","explanation":"The One who guides to the correct path and right conduct.","audioFileName":"names_097.mp3"},
  {"id":98,"arabic":"الصَّبُورُ","transliteration":"As-Saboor","meaning":"The Patient One","explanation":"The One who is patient and does not hasten to punish.","audioFileName":"names_098.mp3"},
  {"id":99,"arabic":"اللَّهُ","transliteration":"Allah","meaning":"God","explanation":"The One true God, the Creator and Sustainer of all existence.","audioFileName":"names_099.mp3"}
]
    """.trimIndent()

    fun getNames(): List<DivineName> {
        cachedNames?.let { return it }
        val names = try {
            json.decodeFromString<List<DivineName>>(embeddedJson)
        } catch (_: Exception) {
            emptyList()
        }
        cachedNames = names
        return names
    }

    fun isFavorited(nameId: Int): Boolean {
        return settings.getBoolean("divine_fav_$nameId", false)
    }

    fun setFavorited(nameId: Int, favorited: Boolean) {
        settings.putBoolean("divine_fav_$nameId", favorited)
    }

    fun isMemorized(nameId: Int): Boolean {
        return settings.getBoolean("divine_mem_$nameId", false)
    }

    fun setMemorized(nameId: Int, memorized: Boolean) {
        settings.putBoolean("divine_mem_$nameId", memorized)
    }

    fun searchNames(query: String): List<DivineName> {
        if (query.isBlank()) return getNames()
        val q = query.lowercase()
        return getNames().filter {
            it.transliteration.lowercase().contains(q) ||
                it.meaning.lowercase().contains(q) ||
                it.arabic.contains(query)
        }
    }
}
