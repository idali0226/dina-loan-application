package se.nrm.dina.email.model.loan.util;
 
import java.util.HashMap;
import java.util.Map; 


/**
 *
 * @author idali
 */
public class ConstantText {
     
    
    private final String loanRequestEn;
    private final String loanRequestSv;
    
    private final String informationRequest;  
    
    private final String LOAN_REFERENCE_EN;
    private final String LOAN_REFERENCE_SV;
    
    private final String RECEIPT_EN;
    private final String RECEIPT_SV;
    
    private final String dearEn;
    private final String dearSv;
    
    private final String HI_EN;
    private final String HI_SV;
    
    private final String AND_EN;
    private final String AND_SV;
    
    private final String loanBodyTextEn;
    private final String loanBodyTextSv;
    
    private final String loanSummaryLinkEn;
    private final String loanSummaryLinkSv;
    
    private final String sincerelyEn;
    private final String sincerelySv;
    private final String COLLECTION_MANAGER_EN;
    private final String COLLECTION_MENAGER_SV; 
    
    private final String NRM_EN;
    private final String NRM_SV;
    private final String POBOX_EN;
    private final String POBOX_SV;
    private final String COUNTRY_EN;
    private final String COUNTRY_SV;
    
    private final String PRIMARY_MAIL_TEXT_EN;
    private final String PRIMARY_MAIL_TEXT_SV;
    
    
    private final String loanPolicyLinkEn;
    private final String loanPolicyLinkSv;
    
    private final String CURATOR_EMAIL1_EN;
    private final String CURATOR_EMAIL1_SV;
    
    private final String CURATOR_EMAIL2_EN;
    private final String CURATOR_EMAIL2_SV;
      
    private final String loanAcceptanceTextEn;
    private final String loanAcceptanceTextSv;
    
    
    private final String AGREEMENT_ACCEPT_EN;
    private final String AGREEMENT_ACCEPT_SV;
    
    private final String AGREEMENT_DECLINE_EN;
    private final String AGREEMENT_DECLINE_SV;
     
   
    private final Map<String, String> departmentMap_en;
    private final Map<String, String> departmentMap_sv;
    
    private final String BR; 
    private static ConstantText instance = null;
     
    
    private final String LOAN_AGREEMENT_EN;
    private final String LOAN_AGREEMENT_SV;
    
    private final String loanRequestAttachmentEn;
    private final String loanRequestAttachmentSv;
    
    private final String informationRequestAttachment; 
    
    private final String RESPONSIBLE_CUROTOR_EN;
    private final String RESPONSIBLE_CUROTOR_SV;
  
    private final String curatorMailTitle;
    private final String curatorMailText;
    private final String curatorMailTextInformation; 
    
    private final String curatorMailTextNonScientiric;
    private final String curatorMailText1; 
    
    private final String curatorMailPrimaryText; 
    
    private final String borrower;
    private final String primaryBorrower;
    private final String secondaryBorrower;
    
    private final String loanInformation;
    private final String requestInformation; 
    
    private final String requestSummary1;
    private final String requestSummary2;
    
    
    private final String OUT_OF_OFFICE_EN;
    private final String OUT_OF_OFFICE_SV;
    
    private final String OUT_OF_OFFICE_NOTE_EN_1;
    private final String OUT_OF_OFFICE_NOTE_SV_1;
    
    private final String OUT_OF_OFFICE_NOTE_EN_2;
    private final String OUT_OF_OFFICE_NOTE_SV_2;
        
    ConstantText() {
        
        RECEIPT_EN = "Receipt";
        RECEIPT_SV = "Kvitto";
        
        loanRequestEn = "Loan request";
        loanRequestSv = "Låneansökan";
        
        LOAN_REFERENCE_EN = "Reference #: ";
        LOAN_REFERENCE_SV = "Referens #: ";
        
        informationRequest = "Information request";
                
          
        PRIMARY_MAIL_TEXT_EN = "A request for a loan was submitted to the Swedish Museum of Natural History by the person listed below, and with you listed as primary borrow.";
        PRIMARY_MAIL_TEXT_SV = "En låneansökan har inkommit till Naturhistoriska Riksmuseet I Stockholm, med dig som primär låntagare.";
        
        loanPolicyLinkEn = "Please read our Loan Policy here.";
        loanPolicyLinkSv = "Du kan läsa vår utlånspolicy här.";
        
        loanAcceptanceTextEn = "The loan request will not be considered before we have recieved your acceptance.";
        loanAcceptanceTextSv = "Låneansökan kommer inte att beaktas innan vi fått ditt godkännande.";
         
        CURATOR_EMAIL1_EN = "Please forward this email to the responsible curator ";
        CURATOR_EMAIL1_SV = "Vänligen returnera detta mail till vår ansvarig personal "; 
        
        CURATOR_EMAIL2_EN = " stating whether:";
        CURATOR_EMAIL2_SV = " och uppge om:"; 
         
        AGREEMENT_ACCEPT_EN = "I accept to take responsibility as primary borrower of the requested loan and agree to the terms in the loan policy";
        AGREEMENT_ACCEPT_SV = "Jag accepterar att ansvara för det berörda lånet och godkänner villkoren i riksmuseet lånepolicy.";
    
        AGREEMENT_DECLINE_EN = "I decline to take responsibility as primary borrower of the requested loan";
        AGREEMENT_DECLINE_SV = "Jag nekar till ansvar för det berörda lånet.";
        
        loanBodyTextEn = "We have received your loan request and will process it in due course.";
        loanBodyTextSv = "Vi har tagit emot din låneansökan och kommer att bearbeta den så fort vi kan.";
        loanSummaryLinkEn = "Review loan request summary";
        loanSummaryLinkSv = "Granska låneansökan";
        
        loanRequestAttachmentEn = "Loan request is attached.";
        loanRequestAttachmentSv = "Loan request is attached.";
        
        informationRequestAttachment = "Information request is attached";
        
        
        sincerelyEn = "Sincerely,";
        sincerelySv = "Vänliga hälsningar,";
        COLLECTION_MANAGER_EN = "Collection Manager";
        COLLECTION_MENAGER_SV = "Collection Manager";
        
        LOAN_AGREEMENT_EN = "Loan agreement";
        LOAN_AGREEMENT_SV = "Låneavtal";
 
//        LOAN_AGREEMENT_AGREE_EN = "";
//        LOAN_AGREEMENT_AGREE_SV = "";
//    
//        LOAN_AGREEMENT_DECLINE_EN = "";
//        LOAN_AGREEMENT_DECLINE_SV = " ";
       
        departmentMap_en = new HashMap<>();
        departmentMap_sv = new HashMap<>();
        
        
        departmentMap_en.put("Botany", "Department of Botany");
        departmentMap_en.put("EnvionmentSpecimenBank", "Department of Environmental Specimen Bank");
        departmentMap_en.put("Geology", "Department of Geology");
        departmentMap_en.put("Palaeobiology", "Department of Palaeobiology");
        departmentMap_en.put("Zoology", "Department of Zoology");
        
        
        departmentMap_sv.put("Botany", "Enheten för Botanik");
        departmentMap_sv.put("EnvionmentSpecimenBank", "Enheten för Miljöprovbanken");
        departmentMap_sv.put("Geology", "Enheten för Geologi");
        departmentMap_sv.put("Palaeobiology", "Enheten för Paleobiologi");
        departmentMap_sv.put("Zoology", "Enheten för Zoologi");
                 
        NRM_EN = "Swedish Museum of Natural History";
        NRM_SV = "Naturhistoriska riksmuseet";
        POBOX_EN = "Box 50007, 10405 Stockholm";
        POBOX_SV = "Box 50007, 10405 Stockholm";
        COUNTRY_EN = "Sweden";
        COUNTRY_SV = "Sveriga";
        
        dearEn = "Dear";
        dearSv = "Hej";
        
        HI_EN = "Hi";
        HI_SV = "Bäste";
        
        AND_EN = "and";
        AND_SV = "och";
        
        BR = "<br />";
        
        RESPONSIBLE_CUROTOR_EN = "Please forward this mail to responsible curator: ";
        RESPONSIBLE_CUROTOR_SV = "Please forward this mail to responsible curator: ";
        
        
        curatorMailTitle = "Dear responsible curator ";
        curatorMailText = "A loan request regarding specimens of ";
        curatorMailTextInformation = "An information request regarding specimens of ";
        curatorMailTextNonScientiric = "A loan request ";
        curatorMailText1 = " has been submitted, as shown below.";
    
        curatorMailPrimaryText = "This loan request involves a primary borrower.  Before taking action - wait for a letter of responsibility acceptance from the primary borrower.";
        
        loanInformation = "Loan information";
        requestInformation = "Request information";
        
        borrower = "Borrower";
        primaryBorrower = "Primary borrower";
        secondaryBorrower = "Secondary borrower";
        
        requestSummary1 = "Check and download request summary and attached files at the ";
        
        requestSummary2 = ".  If you don't have an account you should contact your department's Collection Manager who can create one for you.";
    
    
        OUT_OF_OFFICE_EN = " - Responsible curator away from office.";
        OUT_OF_OFFICE_SV = " - Ansvarig personal på semester.";
    
        OUT_OF_OFFICE_NOTE_EN_1 = "The curator responsible for your loan request is currently away from office.";
        OUT_OF_OFFICE_NOTE_EN_2 = "The processing of the loan request may therefor take a while longer than normal.";
        OUT_OF_OFFICE_NOTE_SV_1 = "Personalen som är ansvarig för er låneansökan är för tillfället borta från kontoret.";
        OUT_OF_OFFICE_NOTE_SV_2 = "Hantering av låneansökan kan därför ta längre tid än normalt.";
        
    } 
    
    
    
    
    public static synchronized ConstantText getInstance() {
        if (instance == null) {
            instance = new ConstantText();
        }
        return instance;
    }
    
    public String getRequestSummary1() {
        return requestSummary1;
    }
    
    public String getRequestSummary2() {
        return requestSummary2;
    }
    
    public String getReferenceNumber(boolean isSwedish) {
        return isSwedish ? LOAN_REFERENCE_SV : LOAN_REFERENCE_EN;
    }
    
    public String getLoanRequest(boolean isSwedish) {
        return isSwedish ? loanRequestSv : loanRequestEn;
    }
    
    public String getInformationRequest() {
        return informationRequest;
    }
    
    public String getPrimaryText(boolean isSwedish) {
        return isSwedish ? PRIMARY_MAIL_TEXT_SV : PRIMARY_MAIL_TEXT_EN;
    }

    public String getLoanPolicyLinkText(boolean isSwedish) {
        return isSwedish ? loanPolicyLinkSv : loanPolicyLinkEn;
    }
    
    public String getLoanSummaryLinkText(boolean isSwedish) {
        return isSwedish ? loanSummaryLinkSv : loanSummaryLinkEn;
    }
    
    public String getManagerText1(boolean isSwedish) { 
        return isSwedish ? CURATOR_EMAIL1_SV : CURATOR_EMAIL1_EN;
    }
    
    public String getManagerText2(boolean isSwedish) { 
        return isSwedish ? CURATOR_EMAIL2_SV : CURATOR_EMAIL2_EN;
    }
     
    public String getAgreementAccept(boolean isSwedish) {
        return isSwedish ? AGREEMENT_ACCEPT_SV : AGREEMENT_ACCEPT_EN;
    }
    
    public String getAgreementDecline(boolean isSwedish) {
        return isSwedish ? AGREEMENT_DECLINE_SV : AGREEMENT_DECLINE_EN;
    }
    
    public String getRECEIPT(boolean isSwedish) {
        return isSwedish ? RECEIPT_SV : RECEIPT_EN;
    } 

    public String getDear(boolean isSwedish) {
        return isSwedish ? dearSv : dearEn;
    } 
    
    public String getHi(boolean isSwedish) {
        return isSwedish ? HI_SV : HI_EN;
    } 
    
    public String getAnd(boolean isSwedish) {
        return isSwedish ? AND_SV : AND_EN;
    }
    
    public String getAcceptText(boolean isSwedish) {
        return isSwedish ? loanAcceptanceTextSv : loanAcceptanceTextEn;
    }
    
    public String getPrimaryBorrower() {
        return primaryBorrower;
    }
    
    public String getBorrower() {
        return borrower;
    }
    
    public String getSecondaryBorrower() {
        return secondaryBorrower;
    }
            
    
    public String getLoanBodyText(boolean isSwedish) {
        return isSwedish ? loanBodyTextSv : loanBodyTextEn;
    }
  
    public String getSincerely(boolean isSwedish) {
        return isSwedish ? sincerelySv : sincerelyEn;
    }
 
    public String getCOLLECTION_MANAGER(boolean isSwedish) {
        return isSwedish ? COLLECTION_MENAGER_SV : COLLECTION_MANAGER_EN;
    } 

    public String getDEPARTMENT(boolean isSwedish, String department) { 
        return isSwedish ? departmentMap_sv.get(department) : departmentMap_en.get(department);
    }
    
    
    public String getDEPARTMENT(String department) { 
        return departmentMap_en.get(department);
    }
 
    public String getNRM(boolean isSwedish) {
        return isSwedish ? NRM_SV : NRM_EN;
    } 
    
    public String getResponsibleCurator(boolean isSwedish) {
        return isSwedish ? RESPONSIBLE_CUROTOR_SV : RESPONSIBLE_CUROTOR_EN;
    }
    
    public String getPOBOX(boolean isSwedish) {
        return isSwedish ? POBOX_SV : POBOX_EN;
    } 

    public String getCOUNTRY (boolean isSwedish) {
        return isSwedish ? COUNTRY_SV : COUNTRY_EN;
    } 
     
        
    public String getBaseExternalPolicyFilePath(String host) { 
        if(host.contains("localhost")) {
            return "";
        } else {
            return "https:";
        }  
    }
    
    public String getCuratorMailTitle() {
        return curatorMailTitle;
    }
    
    public String getCuratorMailText() {
        return curatorMailText;
    }
    
    public String getCuratorMailTextInformation() {
        return curatorMailTextInformation;
    }

    public String getCuratorMailTextNonScientific() {
        return curatorMailTextNonScientiric;
    }
     
    public String getCuratorMailText1() {
        return curatorMailText1;
    }
    
    
     
    public String getCuratorMailPrimaryText() {
        return curatorMailPrimaryText;
    } 
    
    public String getLoanInformation() {
        return loanInformation;
    }
 
    public String getRequestInformation() {
        return requestInformation;
    }
 
    public String getLoanRequestAttachment(boolean isSwedish) {
        return isSwedish ? loanRequestAttachmentSv : loanRequestAttachmentEn;
    }
    
    public String getInformationRequestAttachment() {
        return informationRequestAttachment;
    }
    
    public String getLoanAgreement(boolean isSwedish) {
        return isSwedish ? LOAN_AGREEMENT_SV : LOAN_AGREEMENT_EN;
    } 
    
    public String getOutofOfficeTitle(boolean isSwedish) {
        return isSwedish ? OUT_OF_OFFICE_SV : OUT_OF_OFFICE_EN;
    }
    
    public String getOutOfOfficeNote1(boolean isSwedish) {
        return isSwedish ? OUT_OF_OFFICE_NOTE_SV_1 : OUT_OF_OFFICE_NOTE_EN_1;
    }
    
    public String getOutOfOfficeNote2(boolean isSwedish) {
        return isSwedish ? OUT_OF_OFFICE_NOTE_SV_2 : OUT_OF_OFFICE_NOTE_EN_2;
    } 
}
