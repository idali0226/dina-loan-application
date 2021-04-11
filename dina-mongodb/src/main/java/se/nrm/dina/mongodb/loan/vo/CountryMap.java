package se.nrm.dina.mongodb.loan.vo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author idali
 */
public class CountryMap {

  private static final Map<String, String> countryMap;

  static {
    countryMap = new HashMap();
    countryMap.put("Sverige", "Sweden");

    countryMap.put("Afghanistan", "Afghanistan");
    countryMap.put("Albanien", "Albania");
    countryMap.put("Algeriet", "Algeria");
    countryMap.put("amerikanska Samoa", "American Samoa");
    countryMap.put("Andorra", "Andorra");
    countryMap.put("Angola", "Angola");
    countryMap.put("Anguilla", "Anguilla");
    countryMap.put("Antarktis", "Antarctica");
    countryMap.put("Antigua och Barbuda", "Antigua and Barbuda");
    countryMap.put("Argentina", "Argentina");
    countryMap.put("Armenien", "Armenia");
    countryMap.put("Aruba", "Aruba");
    countryMap.put("Australien", "Australia");
    countryMap.put("Azerbajdzjan", "Azerbaijan");
    countryMap.put("Bahamas", "Bahamas");
    countryMap.put("Bahrain", "Bahrain");
    countryMap.put("Bangladesh", "Bangladesh");
    countryMap.put("Bargados", "Bargados");
    countryMap.put("Belgien", "Belgium");
    countryMap.put("Belize", "Belize");
    countryMap.put("Benin", "Benin");
    countryMap.put("Bermuda", "Bermuda");
    countryMap.put("Bhutan", "Bhutan");
    countryMap.put("Bolivia", "Bolivia");
    countryMap.put("Bosnien och Hercegovina", "Bosnia and Herzegovina");
    countryMap.put("Botswana", "Botswana");
    countryMap.put("Brasilien", "Brazil");
    countryMap.put("Brunei", "Brunei Darussalam");
    countryMap.put("Bulgarien", "Bulgaria");
    countryMap.put("Burkina Faso", "Burkina Faso");
    countryMap.put("Burundi", "Burundi");
    countryMap.put("Caymanöarna", "Cayman Islands");
    countryMap.put("Centralafrikanska republiken", "Central African Republic");
    countryMap.put("Chile", "Chile");
    countryMap.put("Cocos (Keeling) Islands", "Cocos (Keeling) Islands");
    countryMap.put("Colombia", "Colombia");
    countryMap.put("Cooköarna", "Cook Islands");
    countryMap.put("Costa Rica", "Costa Rica");
    countryMap.put("Cypern", "Cyprus");
    countryMap.put("Danmark", "Denmark");
    countryMap.put("Djibouti", "Djibouti");
    countryMap.put("Dominica", "Dominica");
    countryMap.put("Dominikanska republiken", "Dominican Republic");
    countryMap.put("Demokratiska republiken Kongo (Kinshasa)", "Democratic Republic of Congo (Kinshasa)");
    countryMap.put("Ecuador", "Ecuador");
    countryMap.put("Egypten", "Egypt");
    countryMap.put("El Salvador", "El Salvador");
    countryMap.put("Elfenbenskusten", "Ivory Coast");
    countryMap.put("Ekvatorialguinea", "Equatorial Guinea");
    countryMap.put("Eritrea", "Eritrea");
    countryMap.put("Estland", "Estonia");
    countryMap.put("Etiopien", "Ethiopia");
    countryMap.put("Falklandsöarna", "Falkland Islands");
    countryMap.put("Filippinerna", "Philippines");
    countryMap.put("Fiji", "Fiji");
    countryMap.put("Finland", "Finland");
    countryMap.put("Frankrike", "France");
    countryMap.put("Franska Guyana", "French Guiana");
    countryMap.put("Franska Polynesien", "French Polynesia");
    countryMap.put("Franska sydterritorierna", "French Southern Territories");
    countryMap.put("Färöarna", "Faroe Islands");
    countryMap.put("Förenade Arabemiraten", "United Arab Emirates");
    countryMap.put("Gabon", "Gabon");
    countryMap.put("Gambia", "Gambia");
    countryMap.put("Georgien", "Georgia");
    countryMap.put("Ghana", "Ghana");

    countryMap.put("Gibraltar", "Gibraltar");
    countryMap.put("Grekland", "Greece");
    countryMap.put("Grenada", "Grenada");
    countryMap.put("Grönland", "Greenland");
    countryMap.put("Guadeloupe", "Guadeloupe");
    countryMap.put("Guam", "Guam");
    countryMap.put("Guatemala", "Guatemala");
    countryMap.put("Guinea-Bissau", "Guinea-Bissau");
    countryMap.put("Guyana", "Guyana");
    countryMap.put("Haiti", "Haiti");
    countryMap.put("Heliga stolen", "Holy See");
    countryMap.put("Honduras", "Honduras");
    countryMap.put("Hongkong", "Hong Kong");

    countryMap.put("Island", "Iceland");
    countryMap.put("Indien", "India");
    countryMap.put("Indonesien", "Indonesia");
    countryMap.put("Iran", "Iran");
    countryMap.put("Irak", "Iraq");
    countryMap.put("Irland", "Ireland");
    countryMap.put("Israel", "Israel");
    countryMap.put("Italien", "Italy");

    countryMap.put("Jamaica", "Jamaica");
    countryMap.put("Japan", "Japan");
    countryMap.put("Jordanien", "Jordan");
    countryMap.put("Kambodja", "Cambodia");
    countryMap.put("Kamerun", "Cameroon");
    countryMap.put("Kanada", "Canada");
    countryMap.put("Kap Verde", "Cape Verde");
    countryMap.put("Kazakstan", "Kazakhstan");
    countryMap.put("Kenya", "Kenya");
    countryMap.put("Kina", "China");
    countryMap.put("Kirgistan", "Kyrgyzstan");
    countryMap.put("Kiribati", "Kiribati");
    countryMap.put("Kongo republiken (Brazzaville)", "Congo Republic of (Brazzaville)");
    countryMap.put("Korea, Demokratiska folk Rep", "Korea, Democratic People's Rep.");
    countryMap.put("Kosovo", "Kosovo");
    countryMap.put("Kroatien", "Croatia");
    countryMap.put("Kuba", "Cuba");
    countryMap.put("Kuwait", "Kuwait");
    countryMap.put("Lao", "Lao");
    countryMap.put("Lettland", "Latvia");
    countryMap.put("Lesotho", "Lesotho");
    countryMap.put("Libanon", "Lebanon");
    countryMap.put("Liberia", "Liberia");
    countryMap.put("Libyen", "Libya");
    countryMap.put("Liechtenstein", "Liechtenstein");
    countryMap.put("Litauen", "Lithuania");
    countryMap.put("Luxembourg", "Luxembourg");
    countryMap.put("Macau", "Macau");
    countryMap.put("Makedonien", "Macedonia");
    countryMap.put("Madagaskar", "Madagascar");
    countryMap.put("Malawi", "Malawi");
    countryMap.put("Malaysia", "Malaysia");
    countryMap.put("Maldiverna", "Maldives");
    countryMap.put("Mali", "Mali");
    countryMap.put("Malta", "Malta");
    countryMap.put("Marocko", "Morocco");
    countryMap.put("Marshallöarna", "Marshall Islands");
    countryMap.put("Martinique", "Martinique");
    countryMap.put("Mauretanien", "Mauritania");
    countryMap.put("Mayotte", "Mayotte");
    countryMap.put("Mexiko", "Mexico");
    countryMap.put("Mikronesien", "Micronesia");
    countryMap.put("Moldavien", "Moldova");
    countryMap.put("Monaco", "Monaco");
    countryMap.put("Mongoliet", "Mongolia");
    countryMap.put("Montenegro", "Montenegro");
    countryMap.put("Montserrat", "Montserrat");
    countryMap.put("Mocambique", "Mozambique");
    countryMap.put("Myanmar, Burma", "Myanmar, Burma");
    countryMap.put("Namibia", "Namibia");
    countryMap.put("Nauru", "Nauru");
    countryMap.put("Nederländerna", "Netherlands");
    countryMap.put("Nederländska Antillerna", "Netherlands Antilles");
    countryMap.put("Nepal", "Nepal");
    countryMap.put("Nicaragua", "Nicaragua");
    countryMap.put("Niger", "Niger");
    countryMap.put("Nigeria", "Nigeria");
    countryMap.put("Niue", "Niue");
    countryMap.put("Nordmarianerna", "Northern Mariana Islands");
    countryMap.put("Norge", "Norway");
    countryMap.put("Nya Kaledonien", "New Caledonia");
    countryMap.put("Nya Zeeland", "New Zealand");
    countryMap.put("Oman", "Oman");

    countryMap.put("Pakistan", "Pakistan");
    countryMap.put("Palau", "Palau");
    countryMap.put("Palestinska områdena", "Palestinian territories");
    countryMap.put("Panama", "Panama");
    countryMap.put("Papua Nya Guinea", "Papua New Guinea");
    countryMap.put("Paraguay", "Paraguay");
    countryMap.put("Peru", "Peru");
    countryMap.put("Pitcairn", "Pitcairn Island");
    countryMap.put("Polen", "Poland");
    countryMap.put("Portugal", "Portugal");
    countryMap.put("Puerto Rico", "Puerto Rico");
    countryMap.put("Qatar", "Qatar");
    countryMap.put("Reunion Island", "Reunion Island");
    countryMap.put("Rumänien", "Romania");
    countryMap.put("Rwanda", "Rwanda");
    countryMap.put("Ryska", "Russian");
    countryMap.put("Saint Kitts and Nevis", "Saint Kitts and Nevis");
    countryMap.put("Saint Lucia", "Saint Lucia");
    countryMap.put("Saint Vincent och Grenadinerna", "Saint Vincent and the Grenadines");
    countryMap.put("Salomonöarna", "Solomon Islands");
    countryMap.put("Samoa", "Samoa");
    countryMap.put("San Marino", "San Marino");

    countryMap.put("Sao Tomé och Principe", "Sao Tome and Principe");
    countryMap.put("Saudiarabien", "Saudi Arabia");
    countryMap.put("Schweiz", "Switzerland");
    countryMap.put("Senegal", "Senegal");
    countryMap.put("Serbien", "Serbia");
    countryMap.put("Seychellerna", "Seychelles");
    countryMap.put("Sierra Leone", "Sierra Leone");
    countryMap.put("Singapore", "Singapore");
    countryMap.put("Slovakien", "Slovakia");
    countryMap.put("Slovenien", "Slovenia");
    countryMap.put("Somalia", "Somalia");
    countryMap.put("Spanien", "Spain");
    countryMap.put("Sri Lanka", "Sri Lanka");
    countryMap.put("Storbritannien", "Great Britain");
    countryMap.put("Sudan", "Sudan");
    countryMap.put("Surinam", "Suriname");
    countryMap.put("Swaziland", "Swaziland");
    countryMap.put("Sydafrika", "South Africa");
    countryMap.put("Sydkorea", "Korea, Republic of");
    countryMap.put("Sydsudan", "South Sudan");
    countryMap.put("Syrien", "Syria");
    countryMap.put("Taiwan", "Taiwan");

    countryMap.put("Tadzjikistan", "");
    countryMap.put("Tanzania", "Tanzania");
    countryMap.put("Tchad", "Chad");
    countryMap.put("Thailand", "Thailand");
    countryMap.put("Tibet", "Tibet");
    countryMap.put("Timo-Leste", "Timo-Leste");
    countryMap.put("Tjeckien", "Czech Republic");
    countryMap.put("Togo", "Togo");
    countryMap.put("Tokelau", "Tokelau");
    countryMap.put("Tonga", "Tonga");
    countryMap.put("Trinidad och Tobago", "Trinidad and Tobago");
    countryMap.put("Tunisien", "Tunisia");
    countryMap.put("Turkiet", "Turkey");
    countryMap.put("Turkmenistan", "Turkmenistan");
    countryMap.put("Turks-och Caicosöarna", "Turks and Caicos Islands");
    countryMap.put("Tuvalu", "Tuvalu");
    countryMap.put("Tyskland", "Germany");
    countryMap.put("Uganda", "Uganda");
    countryMap.put("Ukraina", "Ukraine");
    countryMap.put("Ungern", "Hungary");
    countryMap.put("United States", "United States");
    countryMap.put("Uruguay", "Uruguay");

    countryMap.put("Uzbekistan", "Uzbekistan");
    countryMap.put("Vanuatu", "Vanuatu");
    countryMap.put("Vatikanstaten", "Vatican City");
    countryMap.put("Venezuela", "Venezuela");
    countryMap.put("Vietnam", "Vietnam");
    countryMap.put("Vitryssland", "Belarus");
    countryMap.put("Jungfruöarna", "Virgin Islands");
    countryMap.put("Wallis-och Futunaöarna", "Wallis and Futuna Islands");
    countryMap.put("Västsahara", "Western Sahara");
    countryMap.put("Yemen", "Yemen");
    countryMap.put("Zambia", "Zambia");
    countryMap.put("Zimbabwe", "Zimbabwe");
    countryMap.put("Österrike", "Austria");

    countryMap.put("Östtimor", "East Timor");

  }

  public static String getMappingName(String country) {
    return countryMap.get(country) == null ? country : countryMap.get(country);
  }

}
