package se.nrm.dina.loan.web.controllers;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable; 
import java.io.UnsupportedEncodingException;
import java.util.Date;    
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named; 
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.loan.web.filehander.LoanFileHandler;
import se.nrm.dina.loan.web.pdf.PDFCreator;
import se.nrm.dina.loan.web.util.CommonNames;
import se.nrm.dina.loan.web.util.NameMapping;
import se.nrm.dina.loan.web.util.RequestType;
import se.nrm.dina.loan.web.util.Util;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Collection;
import se.nrm.dina.mongodb.loan.vo.Loan;

/**
 *
 * @author idali
 */
@Named(value = "otherForm")
@SessionScoped
@Slf4j
public class InformationLoanForm implements Serializable {

    private final String educationExhibition = "educationexhibition";
    private final String nonScientificLoan = "Non scientific";
     
    private final String educationalLoanHeaderEn = "Education / Exhibition";
    private final String commercialLoanHeaderEn = "Commercial / art / other";
    
    private final String educationalLoanHeaderSv = "Utbildning / Utställning";
    private final String commercialLoanHeaderSv = "Kommersiellt / konst / annat";
    
    private final String emailFailStatus = "Email failed";
    private final String educationalPdf = "/pdf?pdf=education"; 
    
    private final String physical = "Physical";

    private final String name = "name";
    private final String emptyString = "";
//    private final String na = "N/A";

    private String loanType;
    private String loanDetailDescription;
    private Date fromDate;
    private Date toDate;
    private Date minDate;
    private Date toMinDate;
    
    private String purposeOfUse;   
    private String storageDescription;
 
    private boolean isAgree;

    private boolean isPolicyRead;
    private boolean isSwedish;
    private String loanPolicyPath;  

    private Loan loan;
    private Collection collection;

    @Inject
    private LoanForm form;

    @Inject
    private LoanFileHandler fileHander;

    @Inject
    private FileManager fileManager;

    @Inject
    private PDFCreator pdf;

    @Inject
    private MongoJDBC mongo;

    @Inject
    private Message message;
 
    @Inject
    private MailBuilder mailBuilder;

    @Inject
    private BorrowerController borrower;
     
    public InformationLoanForm() {
        loanType = RequestType.Physical.getText();
        isAgree = false;
        toMinDate = minDate;
        purposeOfUse = null;
    }

    @PostConstruct
    public void init() {
        fromDate = null;
        toDate = null;
        minDate = Util.getInstance().addMinDate();
        collection = mongo.findCollection(nonScientificLoan, name);
        loanPolicyPath = form.getPdfPath() + educationalPdf;   
        isPolicyRead = false;
        isAgree = false;
    }
    
    public void resetLocale(boolean isSwedish) {
        this.isSwedish = isSwedish;
    }

    public void submit() throws MessagingException, Exception {
        log.info("submitInformationLoan");
  
        loan = form.buildLoanInitialData();

        loan.setCurator(collection.getEmail());
        loan.setManager(collection.getManager());
        log.info("c : {} -- {}", collection.getManager(), collection.getEmail());
        
        boolean isEducationalLoan = isIsEducationalLoan();
 
        buildInformationLoan(isEducationalLoan);
        
        buildBorrow();

        try { 
            String  path = fileHander.transferFiles(loan.getUuid());
            
            pdf.createInformationLoanPDF(loan, isEducationalLoan, isSwedish, path);
            mongo.save(loan);
            mailBuilder.buildOtherLoanMail(loan, loanPolicyPath, form.getPdfPath(),
                    form.getLoanDocumentPath(), form.getAdminPath(), isSwedish); 
            
            resetData(); 
            
        } catch (FileNotFoundException | DocumentException ex) {
            log.error(ex.getMessage());
            message.addError(emptyString,
                    NameMapping.getMsgByKey(CommonNames.RequestFailed, isSwedish)); 
            resetData();
            throw new Exception(NameMapping.getMsgByKey(CommonNames.RequestFailed, isSwedish));
        } catch (MessagingException | UnsupportedEncodingException ex) { 
            log.error("sending email failed: {}", ex.getMessage());
            loan.setEmailFailed(true);
            loan.setStatus(emailFailStatus);
            mongo.updateLoan(loan);
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.SendingEmailsFailed, isSwedish));
            resetData();
            throw new MessagingException(NameMapping.getMsgByKey(CommonNames.SendingEmailsFailed, isSwedish));
        }  catch (IOException ex) {
            log.error(ex.getMessage());
            message.addError(emptyString,
                    NameMapping.getMsgByKey(CommonNames.RequestFailed, isSwedish)); 
            resetData();
            throw new Exception(NameMapping.getMsgByKey(CommonNames.RequestFailed, isSwedish));
        }
    }
 

    private void buildInformationLoan(boolean isEducationalLoan) {
        log.info("buildInformationLoan : {}", loanType);

        loan.setLoanDescription(loanDetailDescription);
        if (fileManager.getLoanDetailFile() != null) {
            loan.setEdPurposeFile(fileManager.getLoanDetailFileName());
        }
        loan.setFromDate(Util.getInstance().dateToString(fromDate));
        loan.setToDate(Util.getInstance().dateToString(toDate));

        if(isEducationalLoan) {
            log.info("storageDescription : {}", storageDescription);
            loan.setExhPorpuseDesc(storageDescription);
            loan.setEduPurpose(purposeOfUse); 
             loan.setType(physical);
        } else {
            loan.setType(loanType);
        } 
    }

    private void buildBorrow() {
        loan.setBorrower(borrower.getUserFullName());
        loan.setPrimaryUser(borrower.getUser());
        loan.setSecondaryUser(null);
    }

    public void loanTypeChanged() {
        log.info("loanTypeChanged : {}", loanType);

        minDate = Util.getInstance().addMinDate(
                RequestType.Physical.isPhysical(loanType));

//        isAgree = !isPhysicalLoan();
    }

    public void handleFromDateSelect() {
        log.info("handleFromDateSelect : {}", fromDate);

        toMinDate = fromDate;
    }

    public void handleToDateSelect() {
        log.info("handleToDateSelect . {}", toDate);
    }

    public void saveText() {
        log.info("saveText");
    }
 

    public void onIsAgreeStatusChange() {
        log.info("onIsAgreeStatusChange : {}", isAgree);
    }

    public void onIsPolicyReadStatusChange() {
        log.info("onIsPolicyReadStatusChange : {}", isPolicyRead);
    }
    
     public void purposeOfUseCheckChanged() {
        log.info("purposeOfUseCheckChanged : {}", purposeOfUse);
        loanType = RequestType.Physical.getText();
        isAgree = false;
    }


    public void resetData() {
        log.info("resetData.....");
        fromDate = null;
        toDate = null;
        minDate = Util.getInstance().addMinDate();
        toMinDate = minDate;
        
        loanType = RequestType.Physical.getText();
        loanDetailDescription = null;
        fileManager.setLoanDetailFile(null);
        fileManager.setLoanDetailFileName(null);
        borrower.resetData();
        purposeOfUse = null;
        isPolicyRead = false;
        storageDescription = null;
        isAgree = false;
    }

    public String getLoanDetailDescription() {
        return loanDetailDescription;
    }

    public void setLoanDetailDescription(String loanDetailDescription) {
        this.loanDetailDescription = loanDetailDescription;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getToMinDate() {
        return toMinDate;
    }

    public void setToMinDate(Date toMinDate) {
        this.toMinDate = toMinDate;
    }

    public boolean isIsAgree() {
        return isAgree;
    }

    public void setIsAgree(boolean isAgree) {
        this.isAgree = isAgree;
    }

    public boolean isPhysicalLoan() {
        log.info("isPhysicalLoan : {}", loanType);
        return RequestType.Physical.isPhysical(loanType);
    }

    public boolean isIsPolicyRead() {
        return isPolicyRead;
    }

    public void setIsPolicyRead(boolean isPolicyRead) {
        this.isPolicyRead = isPolicyRead;
    }

    public String getPurposeOfUse() {
        return purposeOfUse;
    }

    public void setPurposeOfUse(String purposeOfUse) {
        this.purposeOfUse = purposeOfUse;
    }

    public String getStorageDescription() {
        return storageDescription;
    }

    public void setStorageDescription(String storageDescription) {
        this.storageDescription = storageDescription;
    }
    
    public boolean isIsEducationalLoan() {
        return form.getPurpose().equalsIgnoreCase(educationExhibition);
    }
    
    public String getLoanPreviewHeader() {
        if(isIsEducationalLoan()) {
            return isSwedish ? educationalLoanHeaderSv : educationalLoanHeaderEn;
        }
        return isSwedish ? commercialLoanHeaderSv : commercialLoanHeaderEn;
    } 
}
