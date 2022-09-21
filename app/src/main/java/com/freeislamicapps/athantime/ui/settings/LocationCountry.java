package com.freeislamicapps.athantime.ui.settings;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class LocationCountry {

    private int id;
    private String name;
    ArrayList<LocationCountry> countryList = new ArrayList<>();

    public LocationCountry(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private ArrayList<LocationCountry> getCountryList() {
        return countryList;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    /*
    private void initCountries() {

        countryList.add(new LocationCountry(2,"TURKIYE"));
        countryList.add(new LocationCountry(33,"ABD"));
        countryList.add(new LocationCountry(166,"AFGANISTAN"));
        countryList.add(new LocationCountry(13,"ALMANYA"));
        countryList.add(new LocationCountry(17,"ANDORRA"));
        countryList.add(new LocationCountry(140,"ANGOLA"));
        countryList.add(new LocationCountry(125,"ANGUILLA"));
        countryList.add(new LocationCountry(90,"ANTIGUA VE BARBUDA"));
        countryList.add(new LocationCountry(199,"ARJANTIN"));
        countryList.add(new LocationCountry(25,"ARNAVUTLUK"));
        countryList.add(new LocationCountry(153,"ARUBA"));
        countryList.add(new LocationCountry(59,"AVUSTRALYA"));
        countryList.add(new LocationCountry(35,"AVUSTURYA"));
        countryList.add(new LocationCountry(5,"AZERBAYCAN"));
        countryList.add(new LocationCountry(54,"BAHAMALAR"));
        countryList.add(new LocationCountry(132,"BAHREYN"));
        countryList.add(new LocationCountry(177,"BANGLADES"));
        countryList.add(new LocationCountry(188,"BARBADOS"));
        countryList.add(new LocationCountry(208,"BELARUS"));
        countryList.add(new LocationCountry(11,"BELCIKA"));
        countryList.add(new LocationCountry(182,"BELIZE"));
        countryList.add(new LocationCountry(181,"BENIN"));
        countryList.add(new LocationCountry(51,"BERMUDA"));
        countryList.add(new LocationCountry(93,"BIRLESIK ARAP EMIRLIGI"));
        countryList.add(new LocationCountry(83,"BOLIVYA"));
        countryList.add(new LocationCountry(9,"BOSNA HERSEK"));
        countryList.add(new LocationCountry(167,"BOTSVANA"));
        countryList.add(new LocationCountry(146,"BREZILYA"));
        countryList.add(new LocationCountry(97,"BRUNEI"));
        countryList.add(new LocationCountry(44,"BULGARISTAN"));
        countryList.add(new LocationCountry(91,"BURKINA FASO"));
        countryList.add(new LocationCountry(154,"BURMA (MYANMAR)"));
        countryList.add(new LocationCountry(65,"BURUNDI"));
        countryList.add(new LocationCountry(155,"BUTAN"));
        countryList.add(new LocationCountry(156,"CAD"));
        countryList.add(new LocationCountry(43,"CECENISTAN"));
        countryList.add(new LocationCountry(16,"CEK CUMHURIYETI"));
        countryList.add(new LocationCountry(86,"CEZAYIR"));
        countryList.add(new LocationCountry(160,"CIBUTI"));
        countryList.add(new LocationCountry(61,"CIN"));
        countryList.add(new LocationCountry(26,"DANIMARKA"));
        countryList.add(new LocationCountry(180,"DEMOKRATIK KONGO CUMHURIYETI"));
        countryList.add(new LocationCountry(176,"DOGU TIMOR"));
        countryList.add(new LocationCountry(123,"DOMINIK"));
        countryList.add(new LocationCountry(72,"DOMINIK CUMHURIYETI"));
        countryList.add(new LocationCountry(139,"EKVATOR"));
        countryList.add(new LocationCountry(63,"EKVATOR GINESI"));
        countryList.add(new LocationCountry(165,"EL SALVADOR"));
        countryList.add(new LocationCountry(117,"ENDONEZYA"));
        countryList.add(new LocationCountry(175,"ERITRE"));
        countryList.add(new LocationCountry(104,"ERMENISTAN"));
        countryList.add(new LocationCountry(6,"ESTONYA"));
        countryList.add(new LocationCountry(95,"ETYOPYA"));
        countryList.add(new LocationCountry(145,"FAS"));
        countryList.add(new LocationCountry(197,"FIJI"));
        countryList.add(new LocationCountry(120,"FILDISI SAHILI"));
        countryList.add(new LocationCountry(126,"FILIPINLER"));
        countryList.add(new LocationCountry(204,"FILISTIN"));
        countryList.add(new LocationCountry(41,"FINLANDIYA"));
        countryList.add(new LocationCountry(21,"FRANSA"));
        countryList.add(new LocationCountry(79,"GABON"));
        countryList.add(new LocationCountry(109,"GAMBIYA"));
        countryList.add(new LocationCountry(143,"GANA"));
        countryList.add(new LocationCountry(111,"GINE"));
        countryList.add(new LocationCountry(58,"GRENADA"));
        countryList.add(new LocationCountry(48,"GRONLAND"));
        countryList.add(new LocationCountry(171,"GUADELOPE"));
        countryList.add(new LocationCountry(169,"GUAM ADASI"));
        countryList.add(new LocationCountry(99,"GUATEMALA"));
        countryList.add(new LocationCountry(67,"GUNEY AFRIKA"));
        countryList.add(new LocationCountry(128,"GUNEY KORE"));
        countryList.add(new LocationCountry(62,"GURCISTAN"));
        countryList.add(new LocationCountry(82,"GUYANA"));
        countryList.add(new LocationCountry(70,"HAITI"));
        countryList.add(new LocationCountry(187,"HINDISTAN"));
        countryList.add(new LocationCountry(30,"HIRVATISTAN"));
        countryList.add(new LocationCountry(4,"HOLLANDA"));
        countryList.add(new LocationCountry(66,"HOLLANDA ANTILLERI"));
        countryList.add(new LocationCountry(105,"HONDURAS"));
        countryList.add(new LocationCountry(113,"HONG KONG"));
        countryList.add(new LocationCountry(15,"INGILTERE"));
        countryList.add(new LocationCountry(124,"IRAK"));
        countryList.add(new LocationCountry(202,"IRAN"));
        countryList.add(new LocationCountry(32,"IRLANDA"));
        countryList.add(new LocationCountry(23,"ISPANYA"));
        countryList.add(new LocationCountry(205,"ISRAIL"));
        countryList.add(new LocationCountry(12,"ISVEC"));
        countryList.add(new LocationCountry(49,"ISVICRE"));
        countryList.add(new LocationCountry(8,"ITALYA"));
        countryList.add(new LocationCountry(122,"IZLANDA"));
        countryList.add(new LocationCountry(119,"JAMAIKA"));
        countryList.add(new LocationCountry(116,"JAPONYA"));
        countryList.add(new LocationCountry(161,"KAMBOCYA"));
        countryList.add(new LocationCountry(184,"KAMERUN"));
        countryList.add(new LocationCountry(52,"KANADA"));
        countryList.add(new LocationCountry(34,"KARADAG"));
        countryList.add(new LocationCountry(94,"KATAR"));
        countryList.add(new LocationCountry(92,"KAZAKISTAN"));
        countryList.add(new LocationCountry(114,"KENYA"));
        countryList.add(new LocationCountry(168,"KIRGIZISTAN"));
        countryList.add(new LocationCountry(57,"KOLOMBIYA"));
        countryList.add(new LocationCountry(88,"KOMORLAR"));
        countryList.add(new LocationCountry(18,"KOSOVA"));
        countryList.add(new LocationCountry(162,"KOSTARIKA"));
        countryList.add(new LocationCountry(209,"KUBA"));
        countryList.add(new LocationCountry(206,"KUDUS"));
        countryList.add(new LocationCountry(133,"KUVEYT"));
        countryList.add(new LocationCountry(1,"KUZEY KIBRIS"));
        countryList.add(new LocationCountry(142,"KUZEY KORE"));
        countryList.add(new LocationCountry(134,"LAOS"));
        countryList.add(new LocationCountry(174,"LESOTO"));
        countryList.add(new LocationCountry(20,"LETONYA"));
        countryList.add(new LocationCountry(73,"LIBERYA"));
        countryList.add(new LocationCountry(203,"LIBYA"));
        countryList.add(new LocationCountry(38,"LIECHTENSTEIN"));
        countryList.add(new LocationCountry(47,"LITVANYA"));
        countryList.add(new LocationCountry(42,"LUBNAN"));
        countryList.add(new LocationCountry(31,"LUKSEMBURG"));
        countryList.add(new LocationCountry(7,"MACARISTAN"));
        countryList.add(new LocationCountry(98,"MADAGASKAR"));
        countryList.add(new LocationCountry(100,"MAKAO"));
        countryList.add(new LocationCountry(28,"MAKEDONYA"));
        countryList.add(new LocationCountry(55,"MALAVI"));
        countryList.add(new LocationCountry(103,"MALDIVLER"));
        countryList.add(new LocationCountry(107,"MALEZYA"));
        countryList.add(new LocationCountry(152,"MALI"));
        countryList.add(new LocationCountry(24,"MALTA"));
        countryList.add(new LocationCountry(87,"MARTINIK"));
        countryList.add(new LocationCountry(164,"MAURITIUS ADASI"));
        countryList.add(new LocationCountry(157,"MAYOTTE"));
        countryList.add(new LocationCountry(53,"MEKSIKA"));
        countryList.add(new LocationCountry(85,"MIKRONEZYA"));
        countryList.add(new LocationCountry(189,"MISIR"));
        countryList.add(new LocationCountry(60,"MOGOLISTAN"));
        countryList.add(new LocationCountry(46,"MOLDAVYA"));
        countryList.add(new LocationCountry(3,"MONAKO"));
        countryList.add(new LocationCountry(147,"MONTSERRAT (U.K.)"));
        countryList.add(new LocationCountry(106,"MORITANYA"));
        countryList.add(new LocationCountry(151,"MOZAMBIK"));
        countryList.add(new LocationCountry(196,"NAMIBYA"));
        countryList.add(new LocationCountry(76,"NEPAL"));
        countryList.add(new LocationCountry(84,"NIJER"));
        countryList.add(new LocationCountry(127,"NIJERYA"));
        countryList.add(new LocationCountry(141,"NIKARAGUA"));
        countryList.add(new LocationCountry(178,"NIUE"));
        countryList.add(new LocationCountry(36,"NORVEC"));
        countryList.add(new LocationCountry(80,"ORTA AFRIKA CUMHURIYETI"));
        countryList.add(new LocationCountry(131,"OZBEKISTAN"));
        countryList.add(new LocationCountry(77,"PAKISTAN"));
        countryList.add(new LocationCountry(149,"PALAU"));
        countryList.add(new LocationCountry(89,"PANAMA"));
        countryList.add(new LocationCountry(185,"PAPUA YENI GINE"));
        countryList.add(new LocationCountry(194,"PARAGUAY"));
        countryList.add(new LocationCountry(69,"PERU"));
        countryList.add(new LocationCountry(183,"PITCAIRN ADASI"));
        countryList.add(new LocationCountry(39,"POLONYA"));
        countryList.add(new LocationCountry(45,"PORTEKIZ"));
        countryList.add(new LocationCountry(68,"PORTO RIKO"));
        countryList.add(new LocationCountry(112,"REUNION"));
        countryList.add(new LocationCountry(37,"ROMANYA"));
        countryList.add(new LocationCountry(81,"RUANDA"));
        countryList.add(new LocationCountry(207,"RUSYA"));
        countryList.add(new LocationCountry(64,"SAUDI ARABISTAN"));
        countryList.add(new LocationCountry(198,"SAMOA"));
        countryList.add(new LocationCountry(102,"SENEGAL"));
        countryList.add(new LocationCountry(138,"SEYSEL ADALARI"));
        countryList.add(new LocationCountry(200,"SILI"));
        countryList.add(new LocationCountry(179,"SINGAPUR"));
        countryList.add(new LocationCountry(27,"SIRBISTAN"));
        countryList.add(new LocationCountry(14,"SLOVAKYA"));
        countryList.add(new LocationCountry(19,"SLOVENYA"));
        countryList.add(new LocationCountry(150,"SOMALI"));
        countryList.add(new LocationCountry(74,"SRI LANKA"));
        countryList.add(new LocationCountry(129,"SUDAN"));
        countryList.add(new LocationCountry(172,"SURINAM"));
        countryList.add(new LocationCountry(191,"SURIYE"));
        countryList.add(new LocationCountry(163,"SVALBARD"));
        countryList.add(new LocationCountry(170,"SVAZILAND"));
        countryList.add(new LocationCountry(101,"TACIKISTAN"));
        countryList.add(new LocationCountry(110,"TANZANYA"));
        countryList.add(new LocationCountry(137,"TAYLAND"));
        countryList.add(new LocationCountry(108,"TAYVAN"));
        countryList.add(new LocationCountry(71,"TOGO"));
        countryList.add(new LocationCountry(130,"TONGA"));
        countryList.add(new LocationCountry(96,"TRINIDAT VE TOBAGO"));
        countryList.add(new LocationCountry(118,"TUNUS"));
        countryList.add(new LocationCountry(159,"TURKMENISTAN"));
        countryList.add(new LocationCountry(75,"UGANDA"));
        countryList.add(new LocationCountry(40,"UKRAYNA"));
        countryList.add(new LocationCountry(29,"UKRAYNA-KIRIM"));
        countryList.add(new LocationCountry(173,"UMMAN"));
        countryList.add(new LocationCountry(192,"URDUN"));
        countryList.add(new LocationCountry(201,"URUGUAY"));
        countryList.add(new LocationCountry(56,"VANUATU"));
        countryList.add(new LocationCountry(10,"VATIKAN"));
        countryList.add(new LocationCountry(186,"VENEZUELA"));
        countryList.add(new LocationCountry(135,"VIETNAM"));
        countryList.add(new LocationCountry(148,"YEMEN"));
        countryList.add(new LocationCountry(115,"YENI KALEDONYA"));
        countryList.add(new LocationCountry(193,"YENI ZELLANDA"));
        countryList.add(new LocationCountry(144,"YESIL BURUN"));
        countryList.add(new LocationCountry(22,"YUNANISTAN"));
        countryList.add(new LocationCountry(158,"ZAMBIYA"));
        countryList.add(new LocationCountry(136,"ZIMBABVE"));
    }
*/
}
