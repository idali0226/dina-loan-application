package se.nrm.dina.loan.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author idali
 */
public class CountryNames {
    
    private static final List<String> COUNTRY_LIST_EN;
    private static final List<String> COUNTRY_LIST_SV;
    
    static {
        COUNTRY_LIST_EN = new ArrayList<>();
        COUNTRY_LIST_SV = new ArrayList<>();
         
        COUNTRY_LIST_EN.add("Afghanistan");
        COUNTRY_LIST_EN.add("Albania");
        COUNTRY_LIST_EN.add("Algeria");
        COUNTRY_LIST_EN.add("American Samoa");
        COUNTRY_LIST_EN.add("Andorra");
        COUNTRY_LIST_EN.add("Angola");
        COUNTRY_LIST_EN.add("Anguilla");
        COUNTRY_LIST_EN.add("Antarctica"); 
        COUNTRY_LIST_EN.add("Antigua and Barbuda");
        COUNTRY_LIST_EN.add("Argentina");
        COUNTRY_LIST_EN.add("Armenia");
        COUNTRY_LIST_EN.add("Aruba");
        COUNTRY_LIST_EN.add("Australia");
        COUNTRY_LIST_EN.add("Austria");
        COUNTRY_LIST_EN.add("Azerbaijan");
         
        COUNTRY_LIST_EN.add("Bahamas");
        COUNTRY_LIST_EN.add("Bahrain");
        COUNTRY_LIST_EN.add("Bangladesh");
        COUNTRY_LIST_EN.add("Bargados");
        COUNTRY_LIST_EN.add("Belarus");
        COUNTRY_LIST_EN.add("Belgium");
        COUNTRY_LIST_EN.add("Belize");
        COUNTRY_LIST_EN.add("Benin");
        COUNTRY_LIST_EN.add("Bermuda"); 
        COUNTRY_LIST_EN.add("Bhutan");
        COUNTRY_LIST_EN.add("Bolivia");
        COUNTRY_LIST_EN.add("Bosnia and Herzegovina");
        COUNTRY_LIST_EN.add("Botswana");
        COUNTRY_LIST_EN.add("Brazil");
        COUNTRY_LIST_EN.add("Brunei Darussalam");
        COUNTRY_LIST_EN.add("Bulgaria");
        COUNTRY_LIST_EN.add("Burkina Faso");
        COUNTRY_LIST_EN.add("Burundi"); 
        
        COUNTRY_LIST_EN.add("Cambodia");
        COUNTRY_LIST_EN.add("Cameroon");
        COUNTRY_LIST_EN.add("Canada");
        COUNTRY_LIST_EN.add("Cape Verde");
        COUNTRY_LIST_EN.add("Cayman Islands");
        COUNTRY_LIST_EN.add("Central African Republic"); 
        COUNTRY_LIST_EN.add("Chad");
        COUNTRY_LIST_EN.add("Chile");
        COUNTRY_LIST_EN.add("China");
        COUNTRY_LIST_EN.add("Christmas Island");
        COUNTRY_LIST_EN.add("Cocos (Keeling) Islands");
        COUNTRY_LIST_EN.add("Colombia");
        COUNTRY_LIST_EN.add("Democratic Republic of Congo (Kinshasa)"); 
        COUNTRY_LIST_EN.add("Congo Republic of (Brazzaville)");
        COUNTRY_LIST_EN.add("Cook Islands");
        COUNTRY_LIST_EN.add("Costa Rica");
        COUNTRY_LIST_EN.add("Ivory Coast");
        COUNTRY_LIST_EN.add("Croatia");
        COUNTRY_LIST_EN.add("Cuba");
        COUNTRY_LIST_EN.add("Cyprus");
        COUNTRY_LIST_EN.add("Czech Republic"); 
         
        COUNTRY_LIST_EN.add("Denmark");
        COUNTRY_LIST_EN.add("Djibouti");
        COUNTRY_LIST_EN.add("Dominica");
        COUNTRY_LIST_EN.add("Dominican Republic");
         
        COUNTRY_LIST_EN.add("East Timor");
        COUNTRY_LIST_EN.add("Ecuador");
        COUNTRY_LIST_EN.add("Egypt");
        COUNTRY_LIST_EN.add("El Salvador");
        COUNTRY_LIST_EN.add("Equatorial Guinea");
        COUNTRY_LIST_EN.add("Eritrea");
        COUNTRY_LIST_EN.add("Estonia");
        COUNTRY_LIST_EN.add("Ethiopia");
         
        COUNTRY_LIST_EN.add("Falkland Islands");
        COUNTRY_LIST_EN.add("Faroe Islands");
        COUNTRY_LIST_EN.add("Fiji"); 
        COUNTRY_LIST_EN.add("Finland");
        COUNTRY_LIST_EN.add("France");
        COUNTRY_LIST_EN.add("French Guiana");
        COUNTRY_LIST_EN.add("French Polynesia");
        COUNTRY_LIST_EN.add("French Southern Territories");
         
        COUNTRY_LIST_EN.add("Gabon");
        COUNTRY_LIST_EN.add("Gambia");
        COUNTRY_LIST_EN.add("Georgia");
        COUNTRY_LIST_EN.add("Germany");
        COUNTRY_LIST_EN.add("Ghana");
        COUNTRY_LIST_EN.add("Gibraltar");
        COUNTRY_LIST_EN.add("Great Britain");
        COUNTRY_LIST_EN.add("Greece");
        COUNTRY_LIST_EN.add("Greenland");
        COUNTRY_LIST_EN.add("Grenada"); 
        COUNTRY_LIST_EN.add("Guadeloupe");
        COUNTRY_LIST_EN.add("Guam");
        COUNTRY_LIST_EN.add("Guatemala");
        COUNTRY_LIST_EN.add("Guinea");
        COUNTRY_LIST_EN.add("Guinea-Bissau");
        COUNTRY_LIST_EN.add("Guyana");
         
        COUNTRY_LIST_EN.add("Haiti");
        COUNTRY_LIST_EN.add("Holy See");
        COUNTRY_LIST_EN.add("Honduras");
        COUNTRY_LIST_EN.add("Hong Kong");
        COUNTRY_LIST_EN.add("Hungary");
         
        COUNTRY_LIST_EN.add("Iceland");
        COUNTRY_LIST_EN.add("India");
        COUNTRY_LIST_EN.add("Indonesia");
        COUNTRY_LIST_EN.add("Iran"); 
        COUNTRY_LIST_EN.add("Iraq");
        COUNTRY_LIST_EN.add("Ireland");
        COUNTRY_LIST_EN.add("Israel");
        COUNTRY_LIST_EN.add("Italy");
         
        COUNTRY_LIST_EN.add("Jamaica");
        COUNTRY_LIST_EN.add("Japan");
        COUNTRY_LIST_EN.add("Jordan");
         
        COUNTRY_LIST_EN.add("Kazakhstan");
        COUNTRY_LIST_EN.add("Kenya");
        COUNTRY_LIST_EN.add("Kiribati");
        COUNTRY_LIST_EN.add("Korea, Democratic People's Rep.");
        COUNTRY_LIST_EN.add("Korea, Republic");
        COUNTRY_LIST_EN.add("Kosovo");
        COUNTRY_LIST_EN.add("Kuwait");
        COUNTRY_LIST_EN.add("Kyrgyzstan"); 
         
        COUNTRY_LIST_EN.add("Lao");
        COUNTRY_LIST_EN.add("Latvia");
        COUNTRY_LIST_EN.add("Lebanon");
        COUNTRY_LIST_EN.add("Lesotho");
        COUNTRY_LIST_EN.add("Liberia");
        COUNTRY_LIST_EN.add("Libya");
        COUNTRY_LIST_EN.add("Liechtenstein");
        COUNTRY_LIST_EN.add("Lithuania");
        COUNTRY_LIST_EN.add("Luxembourg");
         
        COUNTRY_LIST_EN.add("Macau");
        COUNTRY_LIST_EN.add("Macedonia");
        COUNTRY_LIST_EN.add("Madagascar");
        COUNTRY_LIST_EN.add("Malawi");
        COUNTRY_LIST_EN.add("Malaysia");
        COUNTRY_LIST_EN.add("Maldives"); 
        COUNTRY_LIST_EN.add("Mali");
        COUNTRY_LIST_EN.add("Malta");
        COUNTRY_LIST_EN.add("Marshall Islands");
        COUNTRY_LIST_EN.add("Martinique");
        COUNTRY_LIST_EN.add("Mauritania");
        COUNTRY_LIST_EN.add("Mayotte");
        COUNTRY_LIST_EN.add("Mexico");
        COUNTRY_LIST_EN.add("Micronesia");
        COUNTRY_LIST_EN.add("Moldova");
        COUNTRY_LIST_EN.add("Monaco");
        COUNTRY_LIST_EN.add("Mongolia");
        COUNTRY_LIST_EN.add("Montenegro");
        COUNTRY_LIST_EN.add("Montserrat");
        COUNTRY_LIST_EN.add("Morocco");
        COUNTRY_LIST_EN.add("Mozambique"); 
        COUNTRY_LIST_EN.add("Myanmar, Burma");
         
        COUNTRY_LIST_EN.add("Namibia");
        COUNTRY_LIST_EN.add("Nauru");
        COUNTRY_LIST_EN.add("Nepal");
        COUNTRY_LIST_EN.add("Netherlands");
        COUNTRY_LIST_EN.add("Netherlands Antilles");
        COUNTRY_LIST_EN.add("New Caledonia");
        COUNTRY_LIST_EN.add("New Zealand");
        COUNTRY_LIST_EN.add("Nicaragua");
        COUNTRY_LIST_EN.add("Niger");
        COUNTRY_LIST_EN.add("Nigeria");
        COUNTRY_LIST_EN.add("Niue");
        COUNTRY_LIST_EN.add("Northern Mariana Islands");
        COUNTRY_LIST_EN.add("Norway");
         
        COUNTRY_LIST_EN.add("Oman"); 
         
        COUNTRY_LIST_EN.add("Pakistan");
        COUNTRY_LIST_EN.add("Palau");
        COUNTRY_LIST_EN.add("Palestinian territories");
        COUNTRY_LIST_EN.add("Panama");
        COUNTRY_LIST_EN.add("Papua New Guinea");
        COUNTRY_LIST_EN.add("Paraguay");
        COUNTRY_LIST_EN.add("Peru");
        COUNTRY_LIST_EN.add("Philippines");
        COUNTRY_LIST_EN.add("Pitcairn Island");
        COUNTRY_LIST_EN.add("Poland");
        COUNTRY_LIST_EN.add("Portugal");
        COUNTRY_LIST_EN.add("Puerto Rico");
        
        COUNTRY_LIST_EN.add("Qatar");
         
        COUNTRY_LIST_EN.add("Reunion Island");
        COUNTRY_LIST_EN.add("Romania"); 
        COUNTRY_LIST_EN.add("Russian");
        COUNTRY_LIST_EN.add("Rwanda");
         
        COUNTRY_LIST_EN.add("Saint Kitts and Nevis");
        COUNTRY_LIST_EN.add("Saint Lucia");
        COUNTRY_LIST_EN.add("Saint Vincent and the Grenadines");
        COUNTRY_LIST_EN.add("Samoa");
        COUNTRY_LIST_EN.add("San Marino");
        COUNTRY_LIST_EN.add("Sao Tome and Principe");
        COUNTRY_LIST_EN.add("Saudi Arabia");
        COUNTRY_LIST_EN.add("Senegal");
        COUNTRY_LIST_EN.add("Serbia");
        COUNTRY_LIST_EN.add("Seychelles");
        COUNTRY_LIST_EN.add("Sierra Leone");
        COUNTRY_LIST_EN.add("Singapore");
        COUNTRY_LIST_EN.add("Slovakia"); 
        COUNTRY_LIST_EN.add("Slovenia");
        COUNTRY_LIST_EN.add("Solomon Islands");
        COUNTRY_LIST_EN.add("Somalia");
        COUNTRY_LIST_EN.add("South Africa");
        COUNTRY_LIST_EN.add("South Sudan");
        COUNTRY_LIST_EN.add("Spain");
        COUNTRY_LIST_EN.add("Sri Lanka");
        COUNTRY_LIST_EN.add("Sudan");
        COUNTRY_LIST_EN.add("Suriname"); 
        COUNTRY_LIST_EN.add("Swaziland");
        COUNTRY_LIST_EN.add("Sweden");
        COUNTRY_LIST_EN.add("Switzerland"); 
        COUNTRY_LIST_EN.add("Syria");
         
        COUNTRY_LIST_EN.add("Taiwan"); 
        COUNTRY_LIST_EN.add("Tajikistan");
        COUNTRY_LIST_EN.add("Tanzania");
        COUNTRY_LIST_EN.add("Thailand");
        COUNTRY_LIST_EN.add("Tibet");
        COUNTRY_LIST_EN.add("Timo-Leste");
        COUNTRY_LIST_EN.add("Togo");
        COUNTRY_LIST_EN.add("Tokelau");
        COUNTRY_LIST_EN.add("Tonga");
        COUNTRY_LIST_EN.add("Trinidad and Tobago");
        COUNTRY_LIST_EN.add("Tunisia");
        COUNTRY_LIST_EN.add("Turkey"); 
        COUNTRY_LIST_EN.add("Turkmenistan");
        COUNTRY_LIST_EN.add("Turks and Caicos Islands"); 
        COUNTRY_LIST_EN.add("Tuvalu");
         
        COUNTRY_LIST_EN.add("Uganda"); 
        COUNTRY_LIST_EN.add("Ukraine");
        COUNTRY_LIST_EN.add("United Arab Emirates");
        COUNTRY_LIST_EN.add("United Kingdom");
        COUNTRY_LIST_EN.add("United States");
        COUNTRY_LIST_EN.add("Uruguay");
        COUNTRY_LIST_EN.add("Uzbekistan");
         
        
        COUNTRY_LIST_EN.add("Vanuatu");
        COUNTRY_LIST_EN.add("Vatican City");
        COUNTRY_LIST_EN.add("Venezuela");
        COUNTRY_LIST_EN.add("Vietnam");
        COUNTRY_LIST_EN.add("Virgin Islands");
        
        COUNTRY_LIST_EN.add("Wallis and Futuna Islands");
        COUNTRY_LIST_EN.add("Western Sahara");
        
        COUNTRY_LIST_EN.add("Yemen");
        
        COUNTRY_LIST_EN.add("Zambia"); 
        COUNTRY_LIST_EN.add("Zimbabwe"); 
        
         
        COUNTRY_LIST_SV.add("Afghanistan");
        COUNTRY_LIST_SV.add("Albanien");
        COUNTRY_LIST_SV.add("Algeriet");
        COUNTRY_LIST_SV.add("amerikanska Samoa");
        COUNTRY_LIST_SV.add("Andorra");
        COUNTRY_LIST_SV.add("Angola");
        COUNTRY_LIST_SV.add("Anguilla");
        COUNTRY_LIST_SV.add("Antarktis"); 
        COUNTRY_LIST_SV.add("Antigua och Barbuda");
        COUNTRY_LIST_SV.add("Argentina");
        COUNTRY_LIST_SV.add("Armenien");
        COUNTRY_LIST_SV.add("Aruba");
        COUNTRY_LIST_SV.add("Australien"); 
        COUNTRY_LIST_SV.add("Azerbajdzjan");
         
        COUNTRY_LIST_SV.add("Bahamas");
        COUNTRY_LIST_SV.add("Bahrain");
        COUNTRY_LIST_SV.add("Bangladesh");
        COUNTRY_LIST_SV.add("Bargados"); 
        COUNTRY_LIST_SV.add("Belgien");
        COUNTRY_LIST_SV.add("Belize");
        COUNTRY_LIST_SV.add("Benin");
        COUNTRY_LIST_SV.add("Bermuda"); 
        COUNTRY_LIST_SV.add("Bhutan");
        COUNTRY_LIST_SV.add("Bolivia");
        COUNTRY_LIST_SV.add("Bosnien och Hercegovina");
        COUNTRY_LIST_SV.add("Botswana");
        COUNTRY_LIST_SV.add("Brasilien");
        COUNTRY_LIST_SV.add("Brunei");
        COUNTRY_LIST_SV.add("Bulgarien");
        COUNTRY_LIST_SV.add("Burkina Faso");
        COUNTRY_LIST_SV.add("Burundi"); 
         
        COUNTRY_LIST_SV.add("Caymanöarna");
        COUNTRY_LIST_SV.add("Centralafrikanska republiken");  
        COUNTRY_LIST_SV.add("Chile"); 
        COUNTRY_LIST_SV.add("Cocos (Keeling) Islands");
        COUNTRY_LIST_SV.add("Colombia"); 
        COUNTRY_LIST_SV.add("Cooköarna");
        COUNTRY_LIST_SV.add("Costa Rica"); 
        COUNTRY_LIST_SV.add("Cypern");
        
        
         
        COUNTRY_LIST_SV.add("Danmark");
        COUNTRY_LIST_SV.add("Djibouti");
        COUNTRY_LIST_SV.add("Dominica");
        COUNTRY_LIST_SV.add("Dominikanska republiken");
        COUNTRY_LIST_SV.add("Demokratiska republiken Kongo (Kinshasa)"); 
         
        
        COUNTRY_LIST_SV.add("Ecuador");
        COUNTRY_LIST_SV.add("Egypten");
        COUNTRY_LIST_SV.add("El Salvador");
        COUNTRY_LIST_SV.add("Elfenbenskusten");
        COUNTRY_LIST_SV.add("Ekvatorialguinea");
        COUNTRY_LIST_SV.add("Eritrea");
        COUNTRY_LIST_SV.add("Estland");
        COUNTRY_LIST_SV.add("Etiopien");
         
         
        COUNTRY_LIST_SV.add("Falklandsöarna"); 
        COUNTRY_LIST_SV.add("Filippinerna");
        COUNTRY_LIST_SV.add("Fiji"); 
        COUNTRY_LIST_SV.add("Finland");
        COUNTRY_LIST_SV.add("Frankrike");
        COUNTRY_LIST_SV.add("Franska Guyana");
        COUNTRY_LIST_SV.add("Franska Polynesien");
        COUNTRY_LIST_SV.add("Franska sydterritorierna");
        COUNTRY_LIST_SV.add("Färöarna");
        COUNTRY_LIST_SV.add("Förenade Arabemiraten");
         
        COUNTRY_LIST_SV.add("Gabon");
        COUNTRY_LIST_SV.add("Gambia");
        COUNTRY_LIST_SV.add("Georgien"); 
        COUNTRY_LIST_SV.add("Ghana");
        COUNTRY_LIST_SV.add("Gibraltar"); 
        COUNTRY_LIST_SV.add("Grekland");
        COUNTRY_LIST_SV.add("Grenada"); 
        COUNTRY_LIST_SV.add("Grönland"); 
        COUNTRY_LIST_SV.add("Guadeloupe");
        COUNTRY_LIST_SV.add("Guam");
        COUNTRY_LIST_SV.add("Guatemala");
        COUNTRY_LIST_SV.add("Guinea");
        COUNTRY_LIST_SV.add("Guinea-Bissau");
        COUNTRY_LIST_SV.add("Guyana");
         
        COUNTRY_LIST_SV.add("Haiti");
        COUNTRY_LIST_SV.add("Heliga stolen");
        COUNTRY_LIST_SV.add("Honduras");
        COUNTRY_LIST_SV.add("Hongkong");
        COUNTRY_LIST_SV.add("Hungary");
         
        COUNTRY_LIST_SV.add("Island");
        COUNTRY_LIST_SV.add("Indien");
        COUNTRY_LIST_SV.add("Indonesien");
        COUNTRY_LIST_SV.add("Iran"); 
        COUNTRY_LIST_SV.add("Irak");
        COUNTRY_LIST_SV.add("Irland");
        COUNTRY_LIST_SV.add("Israel");
        COUNTRY_LIST_SV.add("Italien");
         
        COUNTRY_LIST_SV.add("Jamaica");
        COUNTRY_LIST_SV.add("Japan");
        COUNTRY_LIST_SV.add("Jordanien"); 
        COUNTRY_LIST_SV.add("Julön");
        
        
        
        COUNTRY_LIST_SV.add("Kambodja");
        COUNTRY_LIST_SV.add("Kamerun");
        COUNTRY_LIST_SV.add("Kanada"); 
        COUNTRY_LIST_SV.add("Kap Verde"); 
        COUNTRY_LIST_SV.add("Kazakstan"); 
        COUNTRY_LIST_SV.add("Kenya");
        COUNTRY_LIST_SV.add("Kina");
        COUNTRY_LIST_SV.add("Kirgistan"); 
        COUNTRY_LIST_SV.add("Kiribati"); 
        COUNTRY_LIST_SV.add("Kongo republiken (Brazzaville)");
        COUNTRY_LIST_SV.add("Korea, Demokratiska folk Rep"); 
        COUNTRY_LIST_SV.add("Kosovo");
        COUNTRY_LIST_SV.add("Kroatien");
        COUNTRY_LIST_SV.add("Kuba");
        COUNTRY_LIST_SV.add("Kuwait");
        
         
        COUNTRY_LIST_SV.add("Lao");
        COUNTRY_LIST_SV.add("Lettland");
        COUNTRY_LIST_SV.add("Lesotho");
        COUNTRY_LIST_SV.add("Libanon"); 
        COUNTRY_LIST_SV.add("Liberia");
        COUNTRY_LIST_SV.add("Libyen");
        COUNTRY_LIST_SV.add("Liechtenstein");
        COUNTRY_LIST_SV.add("Litauen");
        COUNTRY_LIST_SV.add("Luxembourg");
        
         
        COUNTRY_LIST_SV.add("Macau");
        COUNTRY_LIST_SV.add("Makedonien");
        COUNTRY_LIST_SV.add("Madagaskar");
        COUNTRY_LIST_SV.add("Malawi");
        COUNTRY_LIST_SV.add("Malaysia");
        COUNTRY_LIST_SV.add("Maldiverna"); 
        COUNTRY_LIST_SV.add("Mali");
        COUNTRY_LIST_SV.add("Malta");
        COUNTRY_LIST_SV.add("Marocko");
        COUNTRY_LIST_SV.add("Marshallöarna");
        COUNTRY_LIST_SV.add("Martinique");
        COUNTRY_LIST_SV.add("Mauretanien");
        COUNTRY_LIST_SV.add("Mayotte");
        COUNTRY_LIST_SV.add("Mexiko");
        COUNTRY_LIST_SV.add("Mikronesien");
        COUNTRY_LIST_SV.add("Moldavien");
        COUNTRY_LIST_SV.add("Monaco");
        COUNTRY_LIST_SV.add("Mongoliet");
        COUNTRY_LIST_SV.add("Montenegro");
        COUNTRY_LIST_SV.add("Montserrat"); 
        COUNTRY_LIST_SV.add("Mocambique"); 
        COUNTRY_LIST_SV.add("Myanmar, Burma");
        
        
        
         
        COUNTRY_LIST_SV.add("Namibia");
        COUNTRY_LIST_SV.add("Nauru");
        COUNTRY_LIST_SV.add("Nederländerna");
        COUNTRY_LIST_SV.add("Nederländska Antillerna");
        COUNTRY_LIST_SV.add("Nepal"); 
        COUNTRY_LIST_SV.add("Nicaragua");
        COUNTRY_LIST_SV.add("Niger");
        COUNTRY_LIST_SV.add("Nigeria");
        COUNTRY_LIST_SV.add("Niue");
        COUNTRY_LIST_SV.add("Nordmarianerna");
        COUNTRY_LIST_SV.add("Norge");
        COUNTRY_LIST_SV.add("Nya Kaledonien");
        COUNTRY_LIST_SV.add("Nya Zeeland");
         
        COUNTRY_LIST_SV.add("Oman"); 
         
        COUNTRY_LIST_SV.add("Pakistan");
        COUNTRY_LIST_SV.add("Palau");
        COUNTRY_LIST_SV.add("Palestinska områdena");
        COUNTRY_LIST_SV.add("Panama");
        COUNTRY_LIST_SV.add("Papua Nya Guinea");
        COUNTRY_LIST_SV.add("Paraguay");
        COUNTRY_LIST_SV.add("Peru"); 
        COUNTRY_LIST_SV.add("Pitcairn");
        COUNTRY_LIST_SV.add("Polen");
        COUNTRY_LIST_SV.add("Portugal");
        COUNTRY_LIST_SV.add("Puerto Rico");
        
        COUNTRY_LIST_SV.add("Qatar");
         
        COUNTRY_LIST_SV.add("Reunion Island");
        COUNTRY_LIST_SV.add("Rumänien"); 
        COUNTRY_LIST_SV.add("Rwanda");
        COUNTRY_LIST_SV.add("Ryska");
         
         
        COUNTRY_LIST_SV.add("Saint Kitts and Nevis");
        COUNTRY_LIST_SV.add("Saint Lucia");
        COUNTRY_LIST_SV.add("Saint Vincent och Grenadinerna");
        COUNTRY_LIST_SV.add("Salomonöarna");
        COUNTRY_LIST_SV.add("Samoa"); 
        COUNTRY_LIST_SV.add("San Marino");
        COUNTRY_LIST_SV.add("Sao Tomé och Principe");
        COUNTRY_LIST_SV.add("Saudiarabien");
        COUNTRY_LIST_SV.add("Schweiz"); 
        COUNTRY_LIST_SV.add("Senegal");
        COUNTRY_LIST_SV.add("Serbien");
        COUNTRY_LIST_SV.add("Seychellerna");
        COUNTRY_LIST_SV.add("Sierra Leone");
        COUNTRY_LIST_SV.add("Singapore");
        COUNTRY_LIST_SV.add("Slovakien"); 
        COUNTRY_LIST_SV.add("Slovenien"); 
        COUNTRY_LIST_SV.add("Somalia");
        COUNTRY_LIST_SV.add("Spanien");
        COUNTRY_LIST_SV.add("Sri Lanka");
        COUNTRY_LIST_SV.add("Storbritannien");
        COUNTRY_LIST_SV.add("Sudan"); 
        COUNTRY_LIST_SV.add("Surinam"); 
        COUNTRY_LIST_SV.add("Sverige");
        COUNTRY_LIST_SV.add("Swaziland");
        COUNTRY_LIST_SV.add("Sydafrika");
        COUNTRY_LIST_SV.add("Sydkorea"); 
        COUNTRY_LIST_SV.add("Sydsudan");
        COUNTRY_LIST_SV.add("Syrien");  
      
         
        COUNTRY_LIST_SV.add("Taiwan"); 
        COUNTRY_LIST_SV.add("Tadzjikistan");
        COUNTRY_LIST_SV.add("Tanzania");
        COUNTRY_LIST_SV.add("Tchad");
        COUNTRY_LIST_SV.add("Thailand");
        COUNTRY_LIST_SV.add("Tibet");
        COUNTRY_LIST_SV.add("Timo-Leste");
        COUNTRY_LIST_SV.add("Tjeckien"); 
        COUNTRY_LIST_SV.add("Togo");
        COUNTRY_LIST_SV.add("Tokelau");
        COUNTRY_LIST_SV.add("Tonga");
        COUNTRY_LIST_SV.add("Trinidad och Tobago");
        COUNTRY_LIST_SV.add("Tunisien");
        COUNTRY_LIST_SV.add("Turkiet"); 
        COUNTRY_LIST_SV.add("Turkmenistan");
        COUNTRY_LIST_SV.add("Turks-och Caicosöarna"); 
        COUNTRY_LIST_SV.add("Tuvalu");
        COUNTRY_LIST_SV.add("Tyskland");
         
        COUNTRY_LIST_SV.add("Uganda"); 
        COUNTRY_LIST_SV.add("Ukraina");
        COUNTRY_LIST_SV.add("Ungern");
        
        

    }
    
    public void getCountryName() {
        Map countryMap = new HashMap();
        
        countryMap.put("Afghanistan", "Afghanistan");
        countryMap.put("Albania", "Albanien"); 
        countryMap.put("Algeria", "Algeriet");
        countryMap.put("American Samoa", "amerikanska Samoa");  
        countryMap.put("Andorra", "Andorra");
        countryMap.put("Angola", "Angola"); 
        countryMap.put("Anguilla", "Anguilla"); 
        countryMap.put("Antarctica", "Antarktis");
        countryMap.put("Antigua and Barbuda", "Antigua och Barbuda"); 
        countryMap.put("Argentina", "Argentina");
        countryMap.put("Armenia", "Armenien"); 
        countryMap.put("Aruba", "Aruba");         COUNTRY_LIST_SV.add("United States");
        COUNTRY_LIST_SV.add("Uruguay");
        COUNTRY_LIST_SV.add("Uzbekistan");
        
        COUNTRY_LIST_SV.add("Vanuatu");
        COUNTRY_LIST_SV.add("Vatikanstaten");
        COUNTRY_LIST_SV.add("Venezuela");
        COUNTRY_LIST_SV.add("Vietnam");
        COUNTRY_LIST_SV.add("Vitryssland");
        COUNTRY_LIST_SV.add("Jungfruöarna");
        
        COUNTRY_LIST_SV.add("Wallis-och Futunaöarna");
        COUNTRY_LIST_SV.add("Västsahara");
        
        COUNTRY_LIST_SV.add("Yemen");
        
        COUNTRY_LIST_SV.add("Zambia"); 
        COUNTRY_LIST_SV.add("Zimbabwe"); 
        
        
        COUNTRY_LIST_SV.add("Österrike");
        COUNTRY_LIST_SV.add("Östtimor"); 
        countryMap.put("Australia", "Australien");
        countryMap.put("Austria", "Österrike"); 
        countryMap.put("Azerbaijan", "Azerbajdzjan");
         
        countryMap.put("Bahamas", "Bahamas"); 
        countryMap.put("Bahrain", "Bahrain"); 
        countryMap.put("Bangladesh", "Bangladesh");
        countryMap.put("Bargados", "Bargados"); 
        countryMap.put("Belarus", "Belarus");
        countryMap.put("Belgium", "Belgien");  
        countryMap.put("Belize", "Belize");
        countryMap.put("Benin", "Benin");  
        countryMap.put("Bermuda", "Bermuda");
        countryMap.put("Bhutan", "Bhutan"); 
        countryMap.put("Bolivia", "Bolivia"); 
        countryMap.put("Bosnia and Herzegovina", "Bosnien och Hercegovina");
        countryMap.put("Botswana", "Botswana"); 
        countryMap.put("Brazil", "Brasilien");
        countryMap.put("Brunei Darussalam", "Brunei Darussalam"); 
        countryMap.put("Bulgaria", "Bulgarien"); 
        countryMap.put("Burkina Faso", "Burkina Faso");
        countryMap.put("Burundi", "Burundi"); 
         
        
        countryMap.put("Cambodia", "Kambodja");
        countryMap.put("Cameroon", "Kamerun");  
        countryMap.put("Canada", "Kanada"); 
        countryMap.put("Cape Verde", "Cape Verde");   
        countryMap.put("Cayman Islands", "Caymanöarna");
        countryMap.put("Central African Republic", "Centralafrikanska republiken"); 
        countryMap.put("Chad", "Tchad"); 
        countryMap.put("Chile", "Chile");
        countryMap.put("China", "Kina"); 
        countryMap.put("Christmas Island", "Julön");
        countryMap.put("Cocos (Keeling) Islands", "Cocos (Keeling) Islands"); 
        countryMap.put("Colombia", "Colombia"); 
        countryMap.put("Democratic Republic of Congo (Kinshasa)", "Demokratiska republiken Kongo (Kinshasa)");
        countryMap.put("Congo Republic of (Brazzaville)", "Republiken Kongo (Brazzaville)"); 
        countryMap.put("Cook Islands", "Cooköarna"); 
        countryMap.put("Costa Rica", "Costa Rica");
        countryMap.put("Ivory Coast", "Elfenbenskusten"); 
        countryMap.put("Croatia", "Kroatien");
        countryMap.put("Cuba", "Kuba");  
        countryMap.put("Cyprus", "Cypern");
        countryMap.put("Czech Republic", "Tjeckien"); 
         
        countryMap.put("Denmark", "Danmark"); 
        countryMap.put("Djibouti", "Djibouti");
        countryMap.put("Dominica", "Dominica"); 
        countryMap.put("Dominican Republic", "Dominikanska republiken");
        
        
        countryMap.put("East Timor", "Östtimor"); 
        countryMap.put("Ecuador", "Ecuador"); 
        countryMap.put("Egypt", "Egypten");
        countryMap.put("El Salvador", "El Salvador"); 
        countryMap.put("Equatorial Guinea", "Ekvatorialguinea");  
        countryMap.put("Eritrea", "Eritrea"); 
        countryMap.put("Estonia", "Estland");
        countryMap.put("Ethiopia", "Etiopien"); 
         
        
        countryMap.put("Falkland Islands", "Falklandsöarna"); 
        countryMap.put("Faroe Islands", "Färöarna"); 
        countryMap.put("Fiji", "Fiji");
        countryMap.put("Finland", "Finland"); 
        countryMap.put("France", "Frankrike"); 
        countryMap.put("French Guiana", "Franska Guyana");  
        countryMap.put("French Polynesia", "Franska Polynesien");
        countryMap.put("French Southern Territories", "Franska sydterritorierna");  
         
        
        countryMap.put("Gabon", "Gabon");
        countryMap.put("Gambia", "Gambia"); 
        countryMap.put("Georgia", "Georgien"); 
        countryMap.put("Germany", "Tyskland");
        countryMap.put("Ghana", "Ghana"); 
        countryMap.put("Gibraltar", "Gibraltar");
        countryMap.put("Great Britain", "Storbritannien");  
        countryMap.put("Greece", "Grekland"); 
        countryMap.put("Greenland", "Grönland");
        countryMap.put("Grenada", "Grenada");  
        countryMap.put("Guadeloupe", "Guadeloupe"); 
        countryMap.put("Guam", "Guam"); 
        countryMap.put("Guatemala", "Guatemala");
        countryMap.put("Guinea", "Guinea"); 
        countryMap.put("Guinea-Bissau", "Guinea-Bissau");
        countryMap.put("Guyana", "Guyana");  
         
        countryMap.put("Haiti", "Haiti");
        countryMap.put("Holy See", "Heliga stolen");  
        countryMap.put("Honduras", "Honduras");
        countryMap.put("Hong Kong", "Hongkong"); 
        countryMap.put("Hungary", "Hungary"); 
        
        
        
        countryMap.put("Iceland", "Island");
        countryMap.put("India", "Indien"); 
        countryMap.put("Indonesia", "Indonesien");
        countryMap.put("Iran", "Iran"); 
        countryMap.put("Iraq", "Irak"); 
        countryMap.put("Ireland", "Irland");
        countryMap.put("Israel", "Israel");  
        countryMap.put("Italy", "Italien"); 
         
        
        countryMap.put("Jamaica", "Jamaica");
        countryMap.put("Japan", "Japan"); 
        countryMap.put("Jordan", "Jordanien");
        
        
        countryMap.put("Kazakhstan", "Kazakstan");  
        countryMap.put("Kenya", "Kenya");
        countryMap.put("Kiribati", "Kiribati"); 
        countryMap.put("Korea, Democratic People's Rep.", "Korea, Demokratiska folk Rep."); 
        countryMap.put("Korea, Republic", "Korea, Republiken");
        countryMap.put("Kosovo", "Kosovo"); 
        countryMap.put("Kuwait", "Kuwait");
        countryMap.put("Kyrgyzstan", "Kirgizistan"); 
        
        
        
        countryMap.put("Lao", "Lao"); 
        countryMap.put("Latvia", "Lettland");
        countryMap.put("Lebanon", "Libanon"); 
        countryMap.put("Lesotho", "Lesotho"); 
        countryMap.put("Liberia", "Liberia"); 
        countryMap.put("Libya", "Libyen"); 
        countryMap.put("Liechtenstein", "Liechtenstein");
        countryMap.put("Lithuania", "Litauen"); 
        countryMap.put("Luxembourg", "Luxembourg");
         
        countryMap.put("Macau", "Macau");  
        countryMap.put("Macedonia", "Makedonien");
        countryMap.put("Madagascar", "Madagaskar");  
        countryMap.put("Malawi", "Malawi");
        countryMap.put("Malaysia", "Malaysia"); 
        countryMap.put("Maldives", "Maldiverna"); 
        countryMap.put("Mali", "Mali");
        countryMap.put("Malta", "Malta"); 
        countryMap.put("Marshall Islands", "Marshallöarna");
        countryMap.put("Martinique", "Martinique"); 
        countryMap.put("Mauritania", "Mauretanien"); 
        countryMap.put("Mayotte", "Mayotte");
        countryMap.put("Mexico", "Mexiko");  
        countryMap.put("Micronesia", "Mikronesien");  
        countryMap.put("Moldova", "Moldavien");
        countryMap.put("Monaco", "Monaco");  
        countryMap.put("Mongolia", "Mongoliet");
        countryMap.put("Montenegro", "Montenegro"); 
        countryMap.put("Montserrat", "Montserrat"); 
        countryMap.put("Morocco", "Marocko");
        countryMap.put("Mozambique", "Mocambique"); 
        countryMap.put("Myanmar, Burma", "Myanmar, Burma");
         
        countryMap.put("Namibia", "Namibia"); 
        countryMap.put("Nauru", "Nauru"); 
        countryMap.put("Nepal", "Nepal");
        countryMap.put("Netherlands", "Nederländerna");  
        countryMap.put("Netherlands Antilles", "Nederländska Antillerna");  
        countryMap.put("New Caledonia", "Nya Kaledonien");
        countryMap.put("New Zealand", "Nya Zeeland");  
        countryMap.put("Nicaragua", "Nicaragua");
        countryMap.put("Niger", "Niger"); 
        countryMap.put("Nigeria", "Nigeria"); 
        countryMap.put("Niue", "Niue");  
        countryMap.put("Northern Mariana Islands", "Nordmarianerna");
        countryMap.put("Norway", "Norge"); 
         
        countryMap.put("Oman", "Oman"); 
         
        countryMap.put("Pakistan", "Pakistan");
        countryMap.put("Palau", "Palau");  
        countryMap.put("Palestinian territories", "Palestinska områdena");  
        countryMap.put("Panama", "Panama");
        countryMap.put("Papua New Guinea", "Papua Nya Guinea");  
        countryMap.put("Paraguay", "Paraguay");
        countryMap.put("Peru", "Peru"); 
        countryMap.put("Philippines", "Filippinerna"); 
        countryMap.put("Pitcairn Island", "Pitcairn");
        countryMap.put("Poland", "Polen");  
        countryMap.put("Portugal", "Portugal");  
        countryMap.put("Puerto Rico", "Puerto Rico");
        
        countryMap.put("Qatar", "Qatar");  
         
        countryMap.put("Reunion Island", "Reunion Island");
        countryMap.put("Romania", "Rumänien"); 
        countryMap.put("Russian", "Ryska"); 
        countryMap.put("Rwanda", "Rwanda");
         
        countryMap.put("Saint Kitts and Nevis", "Saint Kitts and Nevis"); 
        countryMap.put("Saint Lucia", "Saint Lucia");
        countryMap.put("Saint Vincent and the Grenadines", "Saint Vincent och Grenadinerna"); 
        countryMap.put("Samoa", "Samoa"); 
        countryMap.put("San Marino", "San Marino");
        countryMap.put("Sao Tome and Principe", "Sao Tomé och Principe");  
        countryMap.put("Saudi Arabia", "Saudiarabien");  
        countryMap.put("Senegal", "Senegal");
        countryMap.put("Serbia", "Serbien");  
        countryMap.put("Seychelles", "Seychellerna");
        countryMap.put("Sierra Leone", "Sierra Leone"); 
        countryMap.put("Singapore", "Singapore"); 
        countryMap.put("Slovakia", "Slovakien");
        countryMap.put("Slovenia", "Slovenien");  
        countryMap.put("Solomon Islands", "Salomonöarna");  
        countryMap.put("Somalia", "Somalia");
        countryMap.put("South Africa", "Sydafrika");  
        countryMap.put("South Sudan", "Sydsudan");
        countryMap.put("Spain", "Spanien"); 
        countryMap.put("Sri Lanka", "Sri Lanka"); 
        countryMap.put("Sudan", "Sudan");
        countryMap.put("Suriname", "Surinam"); 
        countryMap.put("Swaziland", "Swaziland");
        countryMap.put("Sweden", "Sverige"); 
        countryMap.put("Switzerland", "Schweiz"); 
        countryMap.put("Syria", "Syrien");
         
        countryMap.put("Taiwan", "Taiwan");  
        countryMap.put("Tajikistan", "Tadzjikistan");  
        countryMap.put("Tanzania", "Tanzania");
        countryMap.put("Thailand", "Thailand");  
        countryMap.put("Tibet", "Tibet");
        countryMap.put("Timo-Leste", "Timo-Leste"); 
        countryMap.put("Togo", "Togo"); 
        countryMap.put("Tokelau", "Tokelau");
        countryMap.put("Tonga", "Tonga");  
        countryMap.put("Trinidad and Tobago", "Trinidad och Tobago");  
        countryMap.put("Tunisia", "Tunisien");
        countryMap.put("Turkey", "Turkiet");  
        countryMap.put("Turkmenistan", "Turkmenistan");
        countryMap.put("Turks and Caicos Islands", "Turks-och Caicosöarna"); 
        countryMap.put("Tuvalu", "Tuvalu"); 
         
        countryMap.put("Uganda", "Uganda");
        countryMap.put("Ukraine", "Ukraina"); 
        countryMap.put("United Arab Emirates", "Förenade arabemiraten");
        countryMap.put("United Kingdom", "Storbritannien"); 
        countryMap.put("United States", "United States"); 
        countryMap.put("Uruguay", "Uruguay");
        countryMap.put("Uzbekistan", "Uzbekistan");  
        
        
        countryMap.put("Vanuatu", "Vanuatu");  
        countryMap.put("Vatican City", "Vatikanstaten");
        countryMap.put("Venezuela", "Venezuela");  
        countryMap.put("Vietnam", "Vietnam");
        countryMap.put("Virgin Islands", "Jungfruöarna"); 
         
        countryMap.put("Wallis and Futuna Islands", "Wallis-och Futunaöarna"); 
        countryMap.put("Western Sahara", "Västsahara");
        
        countryMap.put("Yemen", "Yemen"); 
        
        countryMap.put("Zambia", "Zambia"); 
        countryMap.put("Zimbabwe", "Zimbabwe");  
        
        
    }
    
    public static List<String> getCountryList(boolean isSwedish) {
        return isSwedish ? COUNTRY_LIST_SV : COUNTRY_LIST_EN; 
    }
}
