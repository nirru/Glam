package com.lotche.utils;

import android.util.Log;

import com.lotche.AppConstants;
import com.lotche.service.ServiceFactory;
import com.lotche.service.WebService;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nikk on 8/2/17.
 */

public class CurrencyConverter {
    public static String globalCurrency = "USD";
    public static double globalCurrencyChangeRate = 1;
    public static String getCountryFromCode(String countryCode){
        HashMap<String,String> map = new HashMap<>();
        map.put("Andorra, Principality Of", "AD");
        map.put("United Arab Emirates", "AE");
        map.put("Afghanistan, Islamic State Of", "AF");
        map.put("Antigua And Barbuda", "AG");
        map.put("Anguilla", "AI");
        map.put("Albania", "AL");
        map.put("Armenia", "AM");
        map.put("Netherlands Antilles", "AN");
        map.put("Angola", "AO");
        map.put("Antarctica", "AQ");
        map.put("Argentina", "AR");
        map.put("American Samoa", "AS");
        map.put("Austria", "AT");
        map.put("Australia", "AU");
        map.put("Aruba", "AW");
        map.put("Azerbaidjan", "AZ");
        map.put("Bosnia-Herzegovina", "BA");
        map.put("Barbados", "BB");
        map.put("Bangladesh", "BD");
        map.put("Belgium", "BE");
        map.put("Burkina Faso", "BF");
        map.put("Bulgaria", "BG");
        map.put("Bahrain", "BH");
        map.put("Burundi", "BI");
        map.put("Benin", "BJ");
        map.put("Bermuda", "BM");
        map.put("Brunei Darussalam", "BN");
        map.put("Bolivia", "BO");
        map.put("Brazil", "BR");
        map.put("Bahamas", "BS");
        map.put("Bhutan", "BT");
        map.put("Bouvet Island", "BV");
        map.put("Botswana", "BW");
        map.put("Belarus", "BY");
        map.put("Belize", "BZ");
        map.put("Canada", "CA");
        map.put("Cocos (Keeling) Islands", "CC");
        map.put("Central African Republic", "CF");
        map.put("Congo, The Democratic Republic Of The", "CD");
        map.put("Congo", "CG");
        map.put("Switzerland", "CH");
        map.put("Ivory Coast (Cote D'Ivoire)", "CI");
        map.put("Cook Islands", "CK");
        map.put("Chile", "CL");
        map.put("Cameroon", "CM");
        map.put("China", "CN");
        map.put("Colombia", "CO");
        map.put("Costa Rica", "CR");
        map.put("Former Czechoslovakia", "CS");
        map.put("Cuba", "CU");
        map.put("Cape Verde", "CV");
        map.put("Christmas Island", "CX");
        map.put("Cyprus", "CY");
        map.put("Czech Republic", "CZ");
        map.put("Germany", "DE");
        map.put("Djibouti", "DJ");
        map.put("Denmark", "DK");
        map.put("Dominica", "DM");
        map.put("Dominican Republic", "DO");
        map.put("Algeria", "DZ");
        map.put("Ecuador", "EC");
        map.put("Estonia", "EE");
        map.put("Egypt", "EG");
        map.put("Western Sahara", "EH");
        map.put("Eritrea", "ER");
        map.put("Spain", "ES");
        map.put("Ethiopia", "ET");
        map.put("Finland", "FI");
        map.put("Fiji", "FJ");
        map.put("Falkland Islands", "FK");
        map.put("Micronesia", "FM");
        map.put("Faroe Islands", "FO");
        map.put("France", "FR");
        map.put("France (European Territory)", "FX");
        map.put("Gabon", "GA");
        map.put("Great Britain", "UK");
        map.put("Grenada", "GD");
        map.put("Georgia", "GE");
        map.put("French Guyana", "GF");
        map.put("Ghana", "GH");
        map.put("Gibraltar", "GI");
        map.put("Greenland", "GL");
        map.put("Gambia", "GM");
        map.put("Guinea", "GN");
        map.put("Guadeloupe (French)", "GP");
        map.put("Equatorial Guinea", "GQ");
        map.put("Greece", "GR");
        map.put("S. Georgia & S. Sandwich Isls.", "GS");
        map.put("Guatemala", "GT");
        map.put("Guam (USA)", "GU");
        map.put("Guinea Bissau", "GW");
        map.put("Guyana", "GY");
        map.put("Hong Kong", "HK");
        map.put("Heard And McDonald Islands", "HM");
        map.put("Honduras", "HN");
        map.put("Croatia", "HR");
        map.put("Haiti", "HT");
        map.put("Hungary", "HU");
        map.put("Indonesia", "ID");
        map.put("Ireland", "IE");
        map.put("Israel", "IL");
        map.put("India", "IN");
        map.put("British Indian Ocean Territory", "IO");
        map.put("Iraq", "IQ");
        map.put("Iran", "IR");
        map.put("Iceland", "IS");
        map.put("Italy", "IT");
        map.put("Jamaica", "JM");
        map.put("Jordan", "JO");
        map.put("Japan", "JP");
        map.put("Kenya", "KE");
        map.put("Kyrgyz Republic (Kyrgyzstan)", "KG");
        map.put("Cambodia, Kingdom Of", "KH");
        map.put("Kiribati", "KI");
        map.put("Comoros", "KM");
        map.put("Saint Kitts & Nevis Anguilla", "KN");
        map.put("North Korea", "KP");
        map.put("South Korea", "KR");
        map.put("Kuwait", "KW");
        map.put("Cayman Islands", "KY");
        map.put("Kazakhstan", "KZ");
        map.put("Laos", "LA");
        map.put("Lebanon", "LB");
        map.put("Saint Lucia", "LC");
        map.put("Liechtenstein", "LI");
        map.put("Sri Lanka", "LK");
        map.put("Liberia", "LR");
        map.put("Lesotho", "LS");
        map.put("Lithuania", "LT");
        map.put("Luxembourg", "LU");
        map.put("Latvia", "LV");
        map.put("Libya", "LY");
        map.put("Morocco", "MA");
        map.put("Monaco", "MC");
        map.put("Moldavia", "MD");
        map.put("Madagascar", "MG");
        map.put("Marshall Islands", "MH");
        map.put("Macedonia", "MK");
        map.put("Mali", "ML");
        map.put("Myanmar", "MM");
        map.put("Mongolia", "MN");
        map.put("Macau", "MO");
        map.put("Northern Mariana Islands", "MP");
        map.put("Martinique (French)", "MQ");
        map.put("Mauritania", "MR");
        map.put("Montserrat", "MS");
        map.put("Malta", "MT");
        map.put("Mauritius", "MU");
        map.put("Maldives", "MV");
        map.put("Malawi", "MW");
        map.put("Mexico", "MX");
        map.put("Malaysia", "MY");
        map.put("Mozambique", "MZ");
        map.put("Namibia", "NA");
        map.put("New Caledonia (French)", "NC");
        map.put("Niger", "NE");
        map.put("Norfolk Island", "NF");
        map.put("Nigeria", "NG");
        map.put("Nicaragua", "NI");
        map.put("Netherlands", "NL");
        map.put("Norway", "NO");
        map.put("Nepal", "NP");
        map.put("Nauru", "NR");
        map.put("Neutral Zone", "NT");
        map.put("Niue", "NU");
        map.put("New Zealand", "NZ");
        map.put("Oman", "OM");
        map.put("Panama", "PA");
        map.put("Peru", "PE");
        map.put("Polynesia (French)", "PF");
        map.put("Papua New Guinea", "PG");
        map.put("Philippines", "PH");
        map.put("Pakistan", "PK");
        map.put("Poland", "PL");
        map.put("Saint Pierre And Miquelon", "PM");
        map.put("Pitcairn Island", "PN");
        map.put("Puerto Rico", "PR");
        map.put("Portugal", "PT");
        map.put("Palau", "PW");
        map.put("Paraguay", "PY");
        map.put("Qatar", "QA");
        map.put("Reunion (French)", "RE");
        map.put("Romania", "RO");
        map.put("Russian Federation", "RU");
        map.put("Rwanda", "RW");
        map.put("Saudi Arabia", "SA");
        map.put("Solomon Islands", "SB");
        map.put("Seychelles", "SC");
        map.put("Sudan", "SD");
        map.put("Sweden", "SE");
        map.put("Singapore", "SG");
        map.put("Saint Helena", "SH");
        map.put("Slovenia", "SI");
        map.put("Svalbard And Jan Mayen Islands", "SJ");
        map.put("Slovak Republic", "SK");
        map.put("Sierra Leone", "SL");
        map.put("San Marino", "SM");
        map.put("Senegal", "SN");
        map.put("Somalia", "SO");
        map.put("Suriname", "SR");
        map.put("Saint Tome (Sao Tome) And Principe", "ST");
        map.put("Former USSR", "SU");
        map.put("El Salvador", "SV");
        map.put("Syria", "SY");
        map.put("Swaziland", "SZ");
        map.put("Turks And Caicos Islands", "TC");
        map.put("Chad", "TD");
        map.put("French Southern Territories", "TF");
        map.put("Togo", "TG");
        map.put("Thailand", "TH");
        map.put("Tadjikistan", "TJ");
        map.put("Tokelau", "TK");
        map.put("Turkmenistan", "TM");
        map.put("Tunisia", "TN");
        map.put("Tonga", "TO");
        map.put("East Timor", "TP");
        map.put("Turkey", "TR");
        map.put("Trinidad And Tobago", "TT");
        map.put("Tuvalu", "TV");
        map.put("Taiwan", "TW");
        map.put("Tanzania", "TZ");
        map.put("Ukraine", "UA");
        map.put("Uganda", "UG");
        map.put("United Kingdom", "UK");
        map.put("USA Minor Outlying Islands", "UM");
        map.put("United States", "US");
        map.put("Uruguay", "UY");
        map.put("Uzbekistan", "UZ");
        map.put("Holy See (Vatican City State)", "VA");
        map.put("Saint Vincent & Grenadines", "VC");
        map.put("Venezuela", "VE");
        map.put("Virgin Islands (British)", "VG");
        map.put("Virgin Islands (USA)", "VI");
        map.put("Vietnam", "VN");
        map.put("Vanuatu", "VU");
        map.put("Wallis And Futuna Islands", "WF");
        map.put("Samoa", "WS");
        map.put("Yemen", "YE");
        map.put("Mayotte", "YT");
        map.put("Yugoslavia", "YU");
        map.put("South Africa", "ZA");
        map.put("Zambia", "ZM");
        map.put("Zaire", "ZR");
        map.put("Zimbabwe", "ZW");



        if((!countryCode.equals("")) && (countryCode != null)){
            if(map.containsKey(countryCode)){
                return "Free Shipping to "+map.get(countryCode);
            }
            else{
                return "Free Shipping";
            }
        }
        else{
            return "Free Shipping";
        }
    }
    public static void countryCurrency(String countryCode){
        HashMap<String,String> countriesCurrenciesObj = new HashMap<>();
        countriesCurrenciesObj.put("BD", "BDT");
        countriesCurrenciesObj.put("BE", "EUR");
        countriesCurrenciesObj.put("BF", "XOF");
        countriesCurrenciesObj.put("BG", "BGN");
        countriesCurrenciesObj.put("BA", "BAM");
        countriesCurrenciesObj.put("BB", "BBD");
        countriesCurrenciesObj.put("WF", "XPF");
        countriesCurrenciesObj.put("BL", "EUR");
        countriesCurrenciesObj.put("BM", "BMD");
        countriesCurrenciesObj.put("BN", "BND");
        countriesCurrenciesObj.put("BO", "BOB");
        countriesCurrenciesObj.put("BH", "BHD");
        countriesCurrenciesObj.put("BI", "BIF");
        countriesCurrenciesObj.put("BJ", "XOF");
        countriesCurrenciesObj.put("BT", "BTN");
        countriesCurrenciesObj.put("JM", "JMD");
        countriesCurrenciesObj.put("BV", "NOK");
        countriesCurrenciesObj.put("BW", "BWP");
        countriesCurrenciesObj.put("WS", "WST");
        countriesCurrenciesObj.put("BQ", "USD");
        countriesCurrenciesObj.put("BR", "BRL");
        countriesCurrenciesObj.put("BS", "BSD");
        countriesCurrenciesObj.put("JE", "GBP");
        countriesCurrenciesObj.put("BY", "BYR");
        countriesCurrenciesObj.put("BZ", "BZD");
        countriesCurrenciesObj.put("RU", "RUB");
        countriesCurrenciesObj.put("RW", "RWF");
        countriesCurrenciesObj.put("RS", "RSD");
        countriesCurrenciesObj.put("TL", "USD");
        countriesCurrenciesObj.put("RE", "EUR");
        countriesCurrenciesObj.put("TM", "TMT");
        countriesCurrenciesObj.put("TJ", "TJS");
        countriesCurrenciesObj.put("RO", "RON");
        countriesCurrenciesObj.put("TK", "NZD");
        countriesCurrenciesObj.put("GW", "XOF");
        countriesCurrenciesObj.put("GU", "USD");
        countriesCurrenciesObj.put("GT", "GTQ");
        countriesCurrenciesObj.put("GS", "GBP");
        countriesCurrenciesObj.put("GR", "EUR");
        countriesCurrenciesObj.put("GQ", "XAF");
        countriesCurrenciesObj.put("GP", "EUR");
        countriesCurrenciesObj.put("JP", "JPY");
        countriesCurrenciesObj.put("GY", "GYD");
        countriesCurrenciesObj.put("GG", "GBP");
        countriesCurrenciesObj.put("GF", "EUR");
        countriesCurrenciesObj.put("GE", "GEL");
        countriesCurrenciesObj.put("GD", "XCD");
        countriesCurrenciesObj.put("GB", "GBP");
        countriesCurrenciesObj.put("GA", "XAF");
        countriesCurrenciesObj.put("SV", "USD");
        countriesCurrenciesObj.put("GN", "GNF");
        countriesCurrenciesObj.put("GM", "GMD");
        countriesCurrenciesObj.put("GL", "DKK");
        countriesCurrenciesObj.put("GI", "GIP");
        countriesCurrenciesObj.put("GH", "GHS");
        countriesCurrenciesObj.put("OM", "OMR");
        countriesCurrenciesObj.put("TN", "TND");
        countriesCurrenciesObj.put("JO", "JOD");
        countriesCurrenciesObj.put("HR", "HRK");
        countriesCurrenciesObj.put("HT", "HTG");
        countriesCurrenciesObj.put("HU", "HUF");
        countriesCurrenciesObj.put("HK", "HKD");
        countriesCurrenciesObj.put("HN", "HNL");
        countriesCurrenciesObj.put("HM", "AUD");
        countriesCurrenciesObj.put("VE", "VEF");
        countriesCurrenciesObj.put("PR", "USD");
        countriesCurrenciesObj.put("PS", "ILS");
        countriesCurrenciesObj.put("PW", "USD");
        countriesCurrenciesObj.put("PT", "EUR");
        countriesCurrenciesObj.put("SJ", "NOK");
        countriesCurrenciesObj.put("PY", "PYG");
        countriesCurrenciesObj.put("IQ", "IQD");
        countriesCurrenciesObj.put("PA", "PAB");
        countriesCurrenciesObj.put("PF", "XPF");
        countriesCurrenciesObj.put("PG", "PGK");
        countriesCurrenciesObj.put("PE", "PEN");
        countriesCurrenciesObj.put("PK", "PKR");
        countriesCurrenciesObj.put("PH", "PHP");
        countriesCurrenciesObj.put("PN", "NZD");
        countriesCurrenciesObj.put("PL", "PLN");
        countriesCurrenciesObj.put("PM", "EUR");
        countriesCurrenciesObj.put("ZM", "ZMK");
        countriesCurrenciesObj.put("EH", "MAD");
        countriesCurrenciesObj.put("EE", "EUR");
        countriesCurrenciesObj.put("EG", "EGP");
        countriesCurrenciesObj.put("ZA", "ZAR");
        countriesCurrenciesObj.put("EC", "USD");
        countriesCurrenciesObj.put("IT", "EUR");
        countriesCurrenciesObj.put("VN", "VND");
        countriesCurrenciesObj.put("SB", "SBD");
        countriesCurrenciesObj.put("ET", "ETB");
        countriesCurrenciesObj.put("SO", "SOS");
        countriesCurrenciesObj.put("ZW", "ZWL");
        countriesCurrenciesObj.put("SA", "SAR");
        countriesCurrenciesObj.put("ES", "EUR");
        countriesCurrenciesObj.put("ER", "ERN");
        countriesCurrenciesObj.put("ME", "EUR");
        countriesCurrenciesObj.put("MD", "MDL");
        countriesCurrenciesObj.put("MG", "MGA");
        countriesCurrenciesObj.put("MF", "EUR");
        countriesCurrenciesObj.put("MA", "MAD");
        countriesCurrenciesObj.put("MC", "EUR");
        countriesCurrenciesObj.put("UZ", "UZS");
        countriesCurrenciesObj.put("MM", "MMK");
        countriesCurrenciesObj.put("ML", "XOF");
        countriesCurrenciesObj.put("MO", "MOP");
        countriesCurrenciesObj.put("MN", "MNT");
        countriesCurrenciesObj.put("MH", "USD");
        countriesCurrenciesObj.put("MK", "MKD");
        countriesCurrenciesObj.put("MU", "MUR");
        countriesCurrenciesObj.put("MT", "EUR");
        countriesCurrenciesObj.put("MW", "MWK");
        countriesCurrenciesObj.put("MV", "MVR");
        countriesCurrenciesObj.put("MQ", "EUR");
        countriesCurrenciesObj.put("MP", "USD");
        countriesCurrenciesObj.put("MS", "XCD");
        countriesCurrenciesObj.put("MR", "MRO");
        countriesCurrenciesObj.put("IM", "GBP");
        countriesCurrenciesObj.put("UG", "UGX");
        countriesCurrenciesObj.put("TZ", "TZS");
        countriesCurrenciesObj.put("MY", "MYR");
        countriesCurrenciesObj.put("MX", "MXN");
        countriesCurrenciesObj.put("IL", "ILS");
        countriesCurrenciesObj.put("FR", "EUR");
        countriesCurrenciesObj.put("IO", "USD");
        countriesCurrenciesObj.put("SH", "SHP");
        countriesCurrenciesObj.put("FI", "EUR");
        countriesCurrenciesObj.put("FJ", "FJD");
        countriesCurrenciesObj.put("FK", "FKP");
        countriesCurrenciesObj.put("FM", "USD");
        countriesCurrenciesObj.put("FO", "DKK");
        countriesCurrenciesObj.put("NI", "NIO");
        countriesCurrenciesObj.put("NL", "EUR");
        countriesCurrenciesObj.put("NO", "NOK");
        countriesCurrenciesObj.put("NA", "NAD");
        countriesCurrenciesObj.put("VU", "VUV");
        countriesCurrenciesObj.put("NC", "XPF");
        countriesCurrenciesObj.put("NE", "XOF");
        countriesCurrenciesObj.put("NF", "AUD");
        countriesCurrenciesObj.put("NG", "NGN");
        countriesCurrenciesObj.put("NZ", "NZD");
        countriesCurrenciesObj.put("NP", "NPR");
        countriesCurrenciesObj.put("NR", "AUD");
        countriesCurrenciesObj.put("NU", "NZD");
        countriesCurrenciesObj.put("cK", "NZD");
        countriesCurrenciesObj.put("XK", "EUR");
        countriesCurrenciesObj.put("cI", "XOF");
        countriesCurrenciesObj.put("cH", "cHF");
        countriesCurrenciesObj.put("cO", "cOP");
        countriesCurrenciesObj.put("cN", "cNY");
        countriesCurrenciesObj.put("cM", "XAF");
        countriesCurrenciesObj.put("cL", "cLP");
        countriesCurrenciesObj.put("cC", "AUD");
        countriesCurrenciesObj.put("cA", "cAD");
        countriesCurrenciesObj.put("cG", "XAF");
        countriesCurrenciesObj.put("cF", "XAF");
        countriesCurrenciesObj.put("cD", "cDF");
        countriesCurrenciesObj.put("cZ", "cZK");
        countriesCurrenciesObj.put("cY", "EUR");
        countriesCurrenciesObj.put("cX", "AUD");
        countriesCurrenciesObj.put("cR", "cRC");
        countriesCurrenciesObj.put("cW", "ANG");
        countriesCurrenciesObj.put("cV", "cVE");
        countriesCurrenciesObj.put("cU", "cUP");
        countriesCurrenciesObj.put("SZ", "SZL");
        countriesCurrenciesObj.put("SY", "SYP");
        countriesCurrenciesObj.put("SX", "ANG");
        countriesCurrenciesObj.put("KG", "KGS");
        countriesCurrenciesObj.put("KE", "KES");
        countriesCurrenciesObj.put("SS", "SSP");
        countriesCurrenciesObj.put("SR", "SRD");
        countriesCurrenciesObj.put("KI", "AUD");
        countriesCurrenciesObj.put("KH", "KHR");
        countriesCurrenciesObj.put("KN", "XCD");
        countriesCurrenciesObj.put("KM", "KMF");
        countriesCurrenciesObj.put("ST", "STD");
        countriesCurrenciesObj.put("SK", "EUR");
        countriesCurrenciesObj.put("KR", "KRW");
        countriesCurrenciesObj.put("SI", "EUR");
        countriesCurrenciesObj.put("KP", "KPW");
        countriesCurrenciesObj.put("KW", "KWD");
        countriesCurrenciesObj.put("SN", "XOF");
        countriesCurrenciesObj.put("SM", "EUR");
        countriesCurrenciesObj.put("SL", "SLL");
        countriesCurrenciesObj.put("SC", "SCR");
        countriesCurrenciesObj.put("KZ", "KZT");
        countriesCurrenciesObj.put("KY", "KYD");
        countriesCurrenciesObj.put("SG", "SGD");
        countriesCurrenciesObj.put("SE", "SEK");
        countriesCurrenciesObj.put("SD", "SDG");
        countriesCurrenciesObj.put("DO", "DOP");
        countriesCurrenciesObj.put("DM", "XCD");
        countriesCurrenciesObj.put("DJ", "DJF");
        countriesCurrenciesObj.put("DK", "DKK");
        countriesCurrenciesObj.put("VG", "USD");
        countriesCurrenciesObj.put("DE", "EUR");
        countriesCurrenciesObj.put("YE", "YER");
        countriesCurrenciesObj.put("DZ", "DZD");
        countriesCurrenciesObj.put("US", "USD");
        countriesCurrenciesObj.put("UY", "UYU");
        countriesCurrenciesObj.put("YT", "EUR");
        countriesCurrenciesObj.put("UM", "USD");
        countriesCurrenciesObj.put("LB", "LBP");
        countriesCurrenciesObj.put("LC", "XCD");
        countriesCurrenciesObj.put("LA", "LAK");
        countriesCurrenciesObj.put("TV", "AUD");
        countriesCurrenciesObj.put("TW", "TWD");
        countriesCurrenciesObj.put("TT", "TTD");
        countriesCurrenciesObj.put("TR", "TRY");
        countriesCurrenciesObj.put("LK", "LKR");
        countriesCurrenciesObj.put("LI", "cHF");
        countriesCurrenciesObj.put("LV", "EUR");
        countriesCurrenciesObj.put("TO", "TOP");
        countriesCurrenciesObj.put("LT", "LTL");
        countriesCurrenciesObj.put("LU", "EUR");
        countriesCurrenciesObj.put("LR", "LRD");
        countriesCurrenciesObj.put("LS", "LSL");
        countriesCurrenciesObj.put("TH", "THB");
        countriesCurrenciesObj.put("TF", "EUR");
        countriesCurrenciesObj.put("TG", "XOF");
        countriesCurrenciesObj.put("TD", "XAF");
        countriesCurrenciesObj.put("TC", "USD");
        countriesCurrenciesObj.put("LY", "LYD");
        countriesCurrenciesObj.put("VA", "EUR");
        countriesCurrenciesObj.put("VC", "XCD");
        countriesCurrenciesObj.put("AE", "AED");
        countriesCurrenciesObj.put("AD", "EUR");
        countriesCurrenciesObj.put("AG", "XCD");
        countriesCurrenciesObj.put("AF", "AFN");
        countriesCurrenciesObj.put("AI", "XCD");
        countriesCurrenciesObj.put("VI", "USD");
        countriesCurrenciesObj.put("IS", "ISK");
        countriesCurrenciesObj.put("IR", "IRR");
        countriesCurrenciesObj.put("AM", "AMD");
        countriesCurrenciesObj.put("AL", "ALL");
        countriesCurrenciesObj.put("AO", "AOA");
        countriesCurrenciesObj.put("AQ", "");
        countriesCurrenciesObj.put("AS", "USD");
        countriesCurrenciesObj.put("AR", "ARS");
        countriesCurrenciesObj.put("AU", "AUD");
        countriesCurrenciesObj.put("AT", "EUR");
        countriesCurrenciesObj.put("AW", "AWG");
        countriesCurrenciesObj.put("IN", "INR");
        countriesCurrenciesObj.put("AX", "EUR");
        countriesCurrenciesObj.put("AZ", "AZN");
        countriesCurrenciesObj.put("IE", "EUR");
        countriesCurrenciesObj.put("ID", "IDR");
        countriesCurrenciesObj.put("UA", "UAH");
        countriesCurrenciesObj.put("QA", "QAR");
        countriesCurrenciesObj.put("MZ", "MZN");

        if(countriesCurrenciesObj.containsKey(countryCode)){
           String  currencyCode = countriesCurrenciesObj.get(countryCode);
            globalCurrency = countriesCurrenciesObj.get(countryCode);

            getExchangeRate(currencyCode);
        }
        else{
            globalCurrency = "USD";
            globalCurrencyChangeRate = 1;
        }


    }
    public static void getExchangeRate(final String currencyCode){
        try {
            WebService service = ServiceFactory.createRetrofitService(WebService.class, AppConstants.BASE_URL);
            Observable<Response<ResponseBody>> currencyObservable = service.getExchangeRates();
            currencyObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<ResponseBody>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Response<ResponseBody> responseBodyResponse) {
                            try {
                                String responseStr = new String(responseBodyResponse.body().bytes());
                                JSONObject jsonObject = new JSONObject(responseStr);
                                JSONObject uniObject = jsonObject.getJSONObject("rates");
                                globalCurrencyChangeRate = uniObject.getDouble(currencyCode);
                                Log.e("responseBodyResponse","" +  globalCurrencyChangeRate);
                            } catch (Exception e) {
                                Log.e("responseBodyResponse Exception", e.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
