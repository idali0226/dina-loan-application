package se.nrm.dina.loan.web.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author idali
 */
public class NameMapping {

//    private static final Map<String, String> MAP_EN;
//    private static final Map<String, String> MAP_SV;
  private static final Map<String, String> NAME_MAP_EN;
  private static final Map<String, String> NAME_MAP_SV;

  private static final Map<Enum, String> MAIN_MENU_MAP_EN;
  private static final Map<Enum, String> MAIN_MENU_MAP_SV;

  private static final Map<Enum, String> EDUCATION_MENU_MAP_EN;
  private static final Map<Enum, String> EDUCATION_MENU_MAP_SV;

  private static final Map<Enum, String> ART_MENU_MAP_EN;
  private static final Map<Enum, String> ART_MENU_MAP_SV;

  private static final Map<Enum, String> COMMON_MAP_EN;
  private static final Map<Enum, String> COMMON_MAP_SV;

  static {
    MAIN_MENU_MAP_EN = new HashMap<>();
    MAIN_MENU_MAP_SV = new HashMap<>();

    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Department, "Department");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Purpose, "Purpose");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.NoImplement, "Not implemented");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.RequestType, "Request type");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Project, "Project");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Collection, "Collection");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Speciments, "Specimens");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Desctructive, "Destructive sampling");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Photo, "Photo details");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Cites, "CITES");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Contact, "Contact details");
    MAIN_MENU_MAP_EN.put(BreadCrumbElement.Review, "Review/Submit");

    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Department, "Enhet");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Purpose, "Syfte");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.NoImplement, "Inte anslutet");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.RequestType, "Förfrågningstyp");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Project, "Projekt");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Collection, "Samling");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Speciments, "Föremål");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Desctructive, "Destruktiv hantering");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Photo, "Foto-detaljer");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Cites, "CITES");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Contact, "Adressuppgifter");
    MAIN_MENU_MAP_SV.put(BreadCrumbElement.Review, "Granska/Skicka");

    EDUCATION_MENU_MAP_EN = new HashMap<>();
    EDUCATION_MENU_MAP_SV = new HashMap<>();

    EDUCATION_MENU_MAP_EN.put(BreadCrumbElement.LoanInformation, "General loan information");
    EDUCATION_MENU_MAP_EN.put(BreadCrumbElement.PurposeOfUse, "Purpose of use");
    EDUCATION_MENU_MAP_EN.put(BreadCrumbElement.LoanDetail, "Loan details");
    EDUCATION_MENU_MAP_EN.put(BreadCrumbElement.Storage, "Storage");
    EDUCATION_MENU_MAP_EN.put(BreadCrumbElement.Contact, "Contact details");
    EDUCATION_MENU_MAP_EN.put(BreadCrumbElement.Review, "Review/Submit");

    EDUCATION_MENU_MAP_SV.put(BreadCrumbElement.LoanInformation, "Generell låneinformation");
    EDUCATION_MENU_MAP_SV.put(BreadCrumbElement.PurposeOfUse, "Användningssyfte");
    EDUCATION_MENU_MAP_SV.put(BreadCrumbElement.LoanDetail, "Lånedetaljer");
    EDUCATION_MENU_MAP_SV.put(BreadCrumbElement.Storage, "Förvaring");
    EDUCATION_MENU_MAP_SV.put(BreadCrumbElement.Contact, "Adressuppgifter");
    EDUCATION_MENU_MAP_SV.put(BreadCrumbElement.Review, "Granska/Skicka");

    ART_MENU_MAP_EN = new HashMap<>();
    ART_MENU_MAP_SV = new HashMap<>();

    ART_MENU_MAP_EN.put(BreadCrumbElement.LoanInformation, "General loan information");
    ART_MENU_MAP_EN.put(BreadCrumbElement.LoanType, "Loan type");
    ART_MENU_MAP_EN.put(BreadCrumbElement.LoanDetail, "Loan details");
    ART_MENU_MAP_EN.put(BreadCrumbElement.Contact, "Contact details");
    ART_MENU_MAP_EN.put(BreadCrumbElement.Review, "Review/Submit");

    ART_MENU_MAP_SV.put(BreadCrumbElement.LoanInformation, "Generell låneinformation");
    ART_MENU_MAP_SV.put(BreadCrumbElement.LoanType, "Lånetyp");
    ART_MENU_MAP_SV.put(BreadCrumbElement.LoanDetail, "Lånedetaljer");
    ART_MENU_MAP_SV.put(BreadCrumbElement.Contact, "Adressuppgifter");
    ART_MENU_MAP_SV.put(BreadCrumbElement.Review, "Granska/Skicka");

    NAME_MAP_EN = new HashMap<>();
    NAME_MAP_SV = new HashMap<>();

    NAME_MAP_EN.put("exhibition", "Exhibitions");
    NAME_MAP_EN.put("education", "Education");

    NAME_MAP_SV.put("exhibition", "Utställning");
    NAME_MAP_SV.put("education", "Utbildning");

//        MAP_EN = new HashMap<>();
//        MAP_SV = new HashMap<>();
//         
//        MAP_EN.put("insectsworld", "Coleoptera (World)");
//        MAP_EN.put("insectssweden", "Coleoptera (Sweden)");
//        MAP_EN.put("hymenoptera", "Hymenoptera");
//        MAP_EN.put("diptera", "Diptera");
//        MAP_EN.put("siphonaptera", "Siphonaptera, Phthiraptera");
//        MAP_EN.put("hemiptera", "Hemiptera");
//        MAP_EN.put("thysanoptera", "Thysanoptera");
//        MAP_EN.put("neuropterida", "Neuropterida, Mecoptera");
//        MAP_EN.put("odonata", "Odonata");
//        MAP_EN.put("mantodea", "Mantodea");
//        MAP_EN.put("orthopteroid", "Other Orthopteroid insect orders");
//        MAP_EN.put("lepidoptera", "Other insect orders except Lepidoptera");
//          
//        MAP_EN.put("mammals", "Mammals");
//        MAP_EN.put("birds", "Birds");
//        MAP_EN.put("fish", "Fish");
//        MAP_EN.put("herptiles", "Herptiles"); 
//        
//        
//        
//        MAP_EN.put("cephalocordata", "Cephalocordata");
//        MAP_EN.put("acari", "Acari");
//        MAP_EN.put("arachnida", "Other Arachnida");
//        MAP_EN.put("porifera", "Porifera");
//        MAP_EN.put("hydrozoa", "Hydrozoa");
//        MAP_EN.put("anthozoa", "Anthozoa");
//        MAP_EN.put("platyhelminthes", "Platyhelminthes");
//        MAP_EN.put("nematoda", "Nematoda");
//        MAP_EN.put("annelida", "Annelida");
//        MAP_EN.put("mollusca", "Mollusca");
//        MAP_EN.put("crustacea", "Crustacea");
//        MAP_EN.put("bryozoa", "Bryozoa");
//        MAP_EN.put("echinodermata", "Echinodermata");
//        MAP_EN.put("invertebrategroups", "Other Ivertebrate groups"); 
//        
//        
//        
//        
//        
//        MAP_SV.put("insectsworld", "Coleoptera (Världsfauna)");
//        MAP_SV.put("insectssweden", "Coleoptera (Svenska)");
//        MAP_SV.put("hymenoptera", "Hymenoptera");
//        MAP_SV.put("diptera", "Diptera");
//        MAP_SV.put("siphonaptera", "Siphonaptera, Phthiraptera");
//        MAP_SV.put("hemiptera", "Hemiptera");
//        MAP_SV.put("thysanoptera", "Thysanoptera");
//        MAP_SV.put("neuropterida", "Neuropterida, Mecoptera");
//        MAP_SV.put("odonata", "Odonata");
//        MAP_SV.put("mantodea", "Mantodea");
//        MAP_SV.put("orthopteroid", "Andra Orthopteroida insektsordningar");
//        MAP_SV.put("lepidoptera", "Andra insektsordningar förutom Lepidoptera");
//          
//        MAP_SV.put("mammals", "Däggdjur");
//        MAP_SV.put("birds", "Fåglar");
//        MAP_SV.put("fish", "Fiskar");
//        MAP_SV.put("herptiles", "Herptiler");
//         
//        MAP_SV.put("cephalocordata", "Cephalocordata");
//        MAP_SV.put("acari", "Acari");
//        MAP_SV.put("arachnida", "Andra Arachnida");
//        MAP_SV.put("porifera", "Porifera");
//        MAP_SV.put("hydrozoa", "Hydrozoa");
//        MAP_SV.put("anthozoa", "Anthozoa");
//        MAP_SV.put("platyhelminthes", "Platyhelminthes");
//        MAP_SV.put("nematoda", "Nematoda");
//        MAP_SV.put("annelida", "Annelida");
//        MAP_SV.put("mollusca", "Mollusca");
//        MAP_SV.put("crustacea", "Crustacea");
//        MAP_SV.put("bryozoa", "Bryozoa");
//        MAP_SV.put("echinodermata", "Echinodermata");
//        MAP_SV.put("invertebrategroups", "Andra evertebrat-grupper");  
    COMMON_MAP_EN = new HashMap<>();
    COMMON_MAP_SV = new HashMap<>();

    COMMON_MAP_EN.put(CommonNames.NoResults, "No records exist in our database for this search term");
    COMMON_MAP_EN.put(CommonNames.MissingSearchText, "Please fill in search criteria!");
    COMMON_MAP_EN.put(CommonNames.MissingCatNum, "Please fill in catalog number!");
    COMMON_MAP_EN.put(CommonNames.MissingFamily, "Please fill in family name!");
    COMMON_MAP_EN.put(CommonNames.MissingGenus, "Please fill in genus name!");
    COMMON_MAP_EN.put(CommonNames.MissingSpecies, "Please fill in species name!");
    COMMON_MAP_EN.put(CommonNames.DuplicatValue, " is selected in the list!");

    COMMON_MAP_EN.put(CommonNames.DestructivePolicy, "Destructive Sampling/DNA policy");
    COMMON_MAP_EN.put(CommonNames.ScientificLoanPolicy, "Loanpolicy for scientific purpose");
    COMMON_MAP_EN.put(CommonNames.CommonLoanPolicy, "Common loanpolicy");
    COMMON_MAP_EN.put(CommonNames.PreservationTypeNotSpecified, "Not specified");

    COMMON_MAP_EN.put(CommonNames.RequestFailed, "Loan request failed.  Please try again.");
    COMMON_MAP_EN.put(CommonNames.UploadFileFailed, "Upload file failed, please check file size or file type.");

    COMMON_MAP_EN.put(CommonNames.SendingEmailsFailed, "Failed sending emails. The mail server is currently unavailable. Please wait and try again in a while or contact loan admin: tobias.malm@nrm.se");

    COMMON_MAP_EN.put(CommonNames.Idle, "No activity");
    COMMON_MAP_EN.put(CommonNames.IdleMsg, "Page is inactive for two hours. Session is expired. Redirect to start page.");

    COMMON_MAP_EN.put(CommonNames.DataSourceConnectionError, "Database connection error");
    COMMON_MAP_EN.put(CommonNames.EmptySample, "Please fill in relevant fields!");

    COMMON_MAP_SV.put(CommonNames.NoResults, "Inga taxa i våran databas överensstämmer med din sökterm");
    COMMON_MAP_SV.put(CommonNames.MissingSearchText, "Fyll i sökkriterier!");
    COMMON_MAP_SV.put(CommonNames.MissingCatNum, "Fyll i samlings-id!");
    COMMON_MAP_SV.put(CommonNames.MissingFamily, "Fyll i familjnamn!");
    COMMON_MAP_SV.put(CommonNames.MissingGenus, "Fyll i släktnamn!");
    COMMON_MAP_SV.put(CommonNames.MissingSpecies, "Fyll i artnamn!");
    COMMON_MAP_SV.put(CommonNames.DuplicatValue, " är valt i listan!");

    COMMON_MAP_SV.put(CommonNames.DestructivePolicy, "Destructive Sampling/DNA policy");
    COMMON_MAP_SV.put(CommonNames.ScientificLoanPolicy, "Lånepolicy för vetenskapliga ändamål");
    COMMON_MAP_SV.put(CommonNames.CommonLoanPolicy, "Allmän lånepolicy");
    COMMON_MAP_SV.put(CommonNames.PreservationTypeNotSpecified, "ej specificerad");

    COMMON_MAP_SV.put(CommonNames.RequestFailed, "Låneansökan misslyckades.  Försök igen.");
    COMMON_MAP_SV.put(CommonNames.UploadFileFailed, "Kunde inte ladda upp filen.  Kontrollera filstorlek eller filtyp.");

    COMMON_MAP_SV.put(CommonNames.SendingEmailsFailed, "Misslyckades med e-mailutskick. Mailservern är tillfälligt ur funktion. Vänligen vänta ett tag och försök igen eller kontakta låneadministrator: tobias.malm@nrm.se");

    COMMON_MAP_SV.put(CommonNames.Idle, "Ingen aktivitet");
    COMMON_MAP_SV.put(CommonNames.IdleMsg, "Sidan är inaktiv i två timmar. Session har gått ut. Redirectory till startsidan.");

    COMMON_MAP_SV.put(CommonNames.DataSourceConnectionError, "Databas anslutningsfel");
    
    COMMON_MAP_SV.put(CommonNames.EmptySample, "Fyll i relevanta fält!");
  }

  public static String getName(String key, String locale) {
    return locale.equals("en") ? NAME_MAP_EN.get(key) : NAME_MAP_SV.get(key);
  }

//    public static String getCollectionName(String collection, String locale) {
//        return locale.equals("en") ? MAP_EN.get(collection) : MAP_SV.get(collection);
//    }
  public static String getMainMenuSvByKey(Enum key) {
    return MAIN_MENU_MAP_SV.get(key);
  }

  public static String getMainMenuEnByKey(Enum key) {
    return MAIN_MENU_MAP_EN.get(key);
  }

  public static String getEduMenuSvByKey(Enum key) {
    return EDUCATION_MENU_MAP_SV.get(key);
  }

  public static String getEduMenuEnByKey(Enum key) {
    return EDUCATION_MENU_MAP_EN.get(key);
  }

  public static String getArtMenuSvByKey(Enum key) {
    return ART_MENU_MAP_SV.get(key);
  }

  public static String getArtMenuEnByKey(Enum key) {
    return ART_MENU_MAP_EN.get(key);
  }

  public static Map<Enum, String> getMAIN_MENU_MAP_EN() {
    return MAIN_MENU_MAP_EN;
  }

  public static Map<Enum, String> getMAIN_MENU_MAP_SV() {
    return MAIN_MENU_MAP_SV;
  }

  public static Map<Enum, String> getEDUCATION_MENU_MAP_EN() {
    return EDUCATION_MENU_MAP_EN;
  }

  public static Map<Enum, String> getEDUCATION_MENU_MAP_SV() {
    return EDUCATION_MENU_MAP_SV;
  }

  public static Map<Enum, String> getART_MENU_MAP_EN() {
    return ART_MENU_MAP_EN;
  }

  public static Map<Enum, String> getART_MENU_MAP_SV() {
    return ART_MENU_MAP_SV;
  }

  public static String getMsgByKey(Enum key, boolean isSwedish) {
    return isSwedish ? COMMON_MAP_SV.get(key) : COMMON_MAP_EN.get(key);
  }

}
