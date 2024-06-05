package se.nrm.dina.email.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;
import lombok.extern.slf4j.Slf4j; 
import se.nrm.dina.email.model.loan.util.ConstantText;

/**
 *
 * @author idali
 */
@Slf4j
public class LoanMail implements Serializable {
  
    private final String loanId;
    private final String primarytitle;
    private final String primarylastName;
    private final String primaryfirstName;
    private final String primaryinstitution;
    private final String primaryeoicode;
    private final String primarydepartment;
    private final String primarycountry;

    private final String secondarytitle;
    private final String secondarylastName;
    private final String secondaryfirstName;
    private final String secondaryinstitution;
    private final String secondaryeoicode;
    private final String secondarydepartment;
    private final String secondarycountry;

    private final String collection;
    private final String purpose;
    private final String type;
    private final String subCollection;

    private final String department;
    private final String summaryFilePath;
    private final String policyFileName;
    private final String managerEmail;
    private final String curator;

    private final boolean hasPrimary;
    private final boolean isSwedish;
//  private final String host;

    private final String startDivTagWithFont = "<div style=\"font-size: 1.2em; \">";
    private final String startDivTag = "<div>";

    private final String endDivTag = "</div>";
    private final String brTag = "<br />";
    private final String bold = "<b>";
    private final String boldEnd = "</b>";

    private final String startLinkTag = "<a href=\"";
    private final String endLinkTag = "</a>";

    private final String endTag = "\" >";
    
    private final String startH1Tag = "<h1>";
    private final String endH1Tag = "</h1>";
    private final String startH3Tag = "<h3>";
    private final String endH3Tag = "</h3>";

    private final String emptyString = "";
    private final String emptySpace = " ";
    private final String comma = ",";
    private final String commaWithSpace = ", ";
    
    private final String information = "Information";
    private final String collectionLabel = "Collection: ";
    private final String loanPurposeLabel = "Loan purpose: ";
    private final String loanTypeLabel = "Loan type: ";
    
    
    private final String relevantSubcollectionLabel = "Relevant subcollection: ";
    private final String loanAdminLink = "\" target=\"_blank\">NRM Online Loan Form Administration</a>";
    private final String pdfQry = "pdf?id=";
    
    private final String scientificPurpose = "Scientific purpose";
    
//    private final String pdfPath;
    private final String adminPath;
    private final String servletPath;

    public LoanMail(Map<String, String> map) {
        this.isSwedish = Boolean.parseBoolean(map.get("isSwedish"));
        this.hasPrimary = Boolean.parseBoolean(map.get("hasPrimaryContact"));
  
        this.loanId = map.get("loanId");
  
        this.primarytitle = map.get("primarytitle") != null
                ? map.get("primarytitle").trim() : "";
        this.primarylastName = map.get("primarylastname").trim();
        this.primaryfirstName = map.get("primaryFirstname");
        this.primaryinstitution = map.get("primaryinstitution");
        this.primaryeoicode = map.get("primaryeoicode");
        this.primarydepartment = map.get("primarydepartment");
        this.primarycountry = map.get("primarycountry");

        this.managerEmail = map.get("manager");

        if (hasPrimary) { 
            this.secondarytitle = map.get("secondarytitle") != null
                    ? map.get("secondarytitle").trim() : emptyString;
            this.secondarylastName = map.get("secondarylastname").trim();
            this.secondaryfirstName = map.get("secondaryfirstname").trim();
            this.secondaryinstitution = map.get("secondaryinstitution");
            this.secondaryeoicode = map.get("secondaryeoicode");
            this.secondarydepartment = map.get("secondarydepartment");
            this.secondarycountry = map.get("secondarycountry");
        } else {
            this.secondarytitle = emptyString;
            this.secondarylastName = emptyString;
            this.secondaryfirstName = emptyString;
            this.secondaryinstitution = emptyString;
            this.secondaryeoicode = null;
            this.secondarydepartment = null;
            this.secondarycountry = emptyString;
        }

        this.curator = map.get("curratormail");

        this.department = map.get("department").trim();
        this.collection = map.get("collection");
        this.purpose = map.get("purpose");
        this.type = map.get("type");
        this.subCollection = map.get("subcollection");

        this.summaryFilePath = map.get("summaryFile").trim();
        this.policyFileName = map.get("loanPolicy").trim();
        this.servletPath = map.get("servletPath");
//        this.pdfPath = map.get("pdfPath");
        this.adminPath = map.get("adminPath");
    }
     
    public String buildAdminEmailBody() {
        log.info("buildAdminEmailBody : {}", type);

        StringBuilder sb = new StringBuilder();
         
        if (type.equals(information)) {
            appendMailTitle(sb, ConstantText.getInstance().getInformationRequest());
        } else {
            appendMailTitle(sb, ConstantText.getInstance().getLoanRequest(false));
        }
        sb.append(brTag);
        sb.append(startDivTag);
        sb.append(ConstantText.getInstance().getCuratorMailTitle()); 
        sb.append(commaWithSpace);
        sb.append(brTag);
        sb.append(brTag);
 
        if(purpose.equals(scientificPurpose)) {
            if (type.equals(information)) {
                sb.append(ConstantText.getInstance().getCuratorMailTextInformation());
            } else { 
                sb.append(ConstantText.getInstance().getCuratorMailText()); 
            } 
            if (subCollection != null) {
                sb.append(bold);
                sb.append(subCollection);
                sb.append(boldEnd);
            }  
        } else {
            sb.append(ConstantText.getInstance().getCuratorMailTextNonScientific());
        }
        
        sb.append(ConstantText.getInstance().getCuratorMailText1());
        sb.append(endDivTag);

        sb.append(brTag);
        sb.append(brTag);

        if (hasPrimary) {
            sb.append(ConstantText.getInstance().getCuratorMailPrimaryText());
            sb.append(brTag);
            sb.append(brTag);
        }

        sb.append(startH3Tag);
        if (hasPrimary) {
            sb.append(ConstantText.getInstance().getPrimaryBorrower());
        } else {
            sb.append(ConstantText.getInstance().getBorrower());
        }
        sb.append(endH3Tag);

        sb.append(primarytitle);
        sb.append(emptySpace);
        sb.append(primaryfirstName);
        sb.append(emptySpace);
        sb.append(primarylastName);
        sb.append(brTag);

        if (primarydepartment != null) {
            sb.append(primarydepartment);
            sb.append(brTag);
        }
        sb.append(primaryinstitution);
        sb.append(brTag);
        if (primaryeoicode != null) {
            sb.append(primaryeoicode);
            sb.append(brTag);
        }

        sb.append(primarycountry);
        sb.append(brTag);
        sb.append(brTag);

        if (hasPrimary) {
            sb.append(startH3Tag);
            sb.append(ConstantText.getInstance().getSecondaryBorrower());
            sb.append(endH3Tag);

            sb.append(secondarytitle);
            sb.append(emptySpace);
            sb.append(secondaryfirstName);
            sb.append(emptySpace);
            sb.append(secondarylastName);
            sb.append(brTag);
            if (secondarydepartment != null) {
                sb.append(secondarydepartment);
                sb.append(brTag);
            }
            sb.append(secondaryinstitution);
            sb.append(brTag);
            if (secondaryeoicode != null) {
                sb.append(secondaryeoicode);
                sb.append(brTag);
            } 
            sb.append(secondarycountry);
            sb.append(brTag);
            sb.append(brTag);
        }

        sb.append(startH3Tag);
        if (type.equals(information)) {
            sb.append(ConstantText.getInstance().getRequestInformation());
        } else {
            sb.append(ConstantText.getInstance().getLoanInformation());
        }
        sb.append(startH3Tag);
        sb.append(collectionLabel);
        sb.append(collection);
        sb.append(brTag);
        sb.append(loanPurposeLabel);
        sb.append(purpose);
        sb.append(brTag);

        if (type != null) {
            sb.append(loanTypeLabel);
            sb.append(type);
            sb.append(brTag);
        }

        if (subCollection != null) {
            sb.append(relevantSubcollectionLabel);
            sb.append(subCollection);
        }

        sb.append(brTag);
        sb.append(brTag);
        sb.append(brTag);

        sb.append(startDivTag);
        if (type.equals(information)) {
            sb.append(ConstantText.getInstance().getInformationRequestAttachment());
        } else {
            sb.append(ConstantText.getInstance().getLoanRequestAttachment(isSwedish));
        }
        sb.append(endDivTag);

        sb.append(brTag);
        sb.append(brTag);

        sb.append(ConstantText.getInstance().getRequestSummary1());
        sb.append(startLinkTag);
        sb.append(adminPath);
         
        sb.append(loanAdminLink);
        sb.append(ConstantText.getInstance().getRequestSummary2());

        sb.append(brTag);
        sb.append(endDivTag);
        return sb.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    private void appendTitle(StringBuilder sb) {
        sb.append(ConstantText.getInstance().getDear(isSwedish));
        sb.append(emptySpace);
        if (hasPrimary) {
            if (secondarytitle != null && secondarytitle.trim().length() > 0) {
                sb.append(secondarytitle);
                sb.append(emptySpace);
            }
        } else {
            if (primarytitle != null && primarytitle.trim().length() > 0) {
                sb.append(secondarytitle);
                sb.append(emptySpace);
            }
        } 
        sb.append(hasPrimary ? secondarylastName : primarylastName);
        sb.append(comma);
    }

    private void appendMailTitle(final StringBuilder sb, String mailTitle) {
        sb.append(startH1Tag);
        sb.append(mailTitle);
        sb.append(endH1Tag);
        sb.append(new Timestamp(System.currentTimeMillis()));
        sb.append(brTag);
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getReferenceNumber(isSwedish));
        sb.append(loanId);
        sb.append(brTag);
        sb.append(brTag);
    }

    private void appendExternalLink(final StringBuilder sb, final boolean isPolicy) {
        sb.append(startLinkTag);
        sb.append(isPolicy ? emptyString : servletPath + pdfQry);
        sb.append(isPolicy ? policyFileName : summaryFilePath);
        sb.append(endTag);
        sb.append(isPolicy ? ConstantText.getInstance().getLoanPolicyLinkText(isSwedish)
                : ConstantText.getInstance().getLoanSummaryLinkText(isSwedish));
        sb.append(endLinkTag);

        log.info("policy link : {}", sb.toString());
    }

    private void appendBorrowEmail(StringBuilder sb) {

        sb.append(startDivTagWithFont);
        sb.append(startDivTag);
        appendTitle(sb);
        sb.append(brTag);
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getLoanBodyText(isSwedish));
        sb.append(brTag);
        sb.append(brTag);
        appendExternalLink(sb, false);
        sb.append(endDivTag);

        sb.append(brTag);
        sb.append(brTag);
        sb.append(brTag);
        sb.append(startDivTag);
        sb.append(ConstantText.getInstance().getSincerely(isSwedish)); 
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getDEPARTMENT(isSwedish, department));
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getNRM(isSwedish));
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getPOBOX(isSwedish));
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getCOUNTRY(isSwedish));
        sb.append(endDivTag);
        sb.append(endDivTag);
    }

    private void appendOutOfOfficeEmail(StringBuilder sb) {

        sb.append(startDivTagWithFont);
        sb.append(startDivTag);
        appendTitle(sb);
        sb.append(brTag);
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getOutOfOfficeNote1(isSwedish));
        sb.append(brTag);
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getOutOfOfficeNote2(isSwedish));
        sb.append(brTag);
        sb.append(brTag);

        sb.append(endDivTag);

        sb.append(brTag);
        sb.append(brTag);
        sb.append(brTag);
        sb.append(startDivTag);
        sb.append(ConstantText.getInstance().getSincerely(isSwedish));
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getCOLLECTION_MANAGER(isSwedish));
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getDEPARTMENT(isSwedish, department));
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getNRM(isSwedish));
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getPOBOX(isSwedish));
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getCOUNTRY(isSwedish));
        sb.append(endDivTag);
        sb.append(endDivTag);
    }

    public String buildSubject() {

        StringBuilder sb = new StringBuilder();
        sb.append(ConstantText.getInstance().getRECEIPT(isSwedish));
        sb.append(" [");
        sb.append(ConstantText.getInstance().getReferenceNumber(isSwedish));
        sb.append(loanId);
        sb.append("]");

        return sb.toString();
    }

    public String buildOutOfOfficeSubject() {
        return buildSubject() + ConstantText.getInstance().getOutofOfficeTitle(isSwedish);
    }

    public String buildAdminMailSubject() {
        StringBuilder sb = new StringBuilder();
        if (type.equals("Information")) {
            sb.append(ConstantText.getInstance().getInformationRequest());
        } else {
            sb.append(ConstantText.getInstance().getLoanRequest(false));
        }
        sb.append(" [");
        sb.append(ConstantText.getInstance().getReferenceNumber(false));
        sb.append(loanId);
        sb.append("]");

        return sb.toString();
    }

    public String getPdfFileName() {
        return "loanrequest_" + loanId + "_admin.pdf";
    }

    public String buildPrimaryBorrowerMailBody() {
        log.info("buildPrimaryBorrowerMailBody");

        StringBuilder sb = new StringBuilder();

        sb.append(ConstantText.getInstance().getDear(isSwedish));
        sb.append(" ");
        if (primarytitle != null && primarytitle.trim().length() > 0) {
            sb.append(primarytitle);
            sb.append(" ");
        }

        sb.append(primarylastName);
        sb.append(brTag);
        sb.append(brTag);

        sb.append(ConstantText.getInstance().getPrimaryText(isSwedish));
        sb.append(brTag);
        sb.append(brTag);
        appendExternalLink(sb, true);
        sb.append(brTag);
        sb.append(brTag);
        sb.append(ConstantText.getInstance().getAcceptText(isSwedish));
        sb.append(brTag);
        sb.append(brTag);

        sb.append(ConstantText.getInstance().getManagerText1(isSwedish));
        sb.append(curator);
        sb.append(ConstantText.getInstance().getManagerText2(isSwedish));

        sb.append(brTag);
        sb.append(brTag);
        sb.append("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;        ");
        sb.append(ConstantText.getInstance().getAgreementAccept(isSwedish));
        sb.append(brTag);
        sb.append(brTag);
        sb.append("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;        ");
        sb.append(ConstantText.getInstance().getAgreementDecline(isSwedish));
        sb.append(brTag);
        sb.append(brTag);
        sb.append(brTag);

        appendMailTitle(sb, ConstantText.getInstance().getRECEIPT(isSwedish));

        appendBorrowEmail(sb);
        return sb.toString();
    }

    public String buildBorrowerMailBody() {
        log.info("buildBorrowerMailBody");

        StringBuilder sb = new StringBuilder();
        appendMailTitle(sb, ConstantText.getInstance().getRECEIPT(isSwedish));
        appendBorrowEmail(sb);
        return sb.toString();
    }

    public String buildOutOfOfficeMailBody() {
        StringBuilder sb = new StringBuilder();
        appendMailTitle(sb, ConstantText.getInstance().getLoanRequest(isSwedish));
        appendOutOfOfficeEmail(sb);

        return sb.toString();
    }  
}
