package se.nrm.dina.loan.web.controllers;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j; 
import org.apache.commons.lang.StringUtils;
import se.nrm.dina.loan.web.filehander.LoanFileHandler;
import se.nrm.dina.loan.web.pdf.PDFCreator;
import se.nrm.dina.loan.web.service.MongoService; 
import se.nrm.dina.loan.web.util.CommonNames;
import se.nrm.dina.loan.web.util.CommonString;
import se.nrm.dina.loan.web.util.NameMapping;
import se.nrm.dina.loan.web.util.RequestType;    
import se.nrm.dina.mongodb.loan.vo.Collection; 
import se.nrm.dina.mongodb.loan.vo.Loan; 

/**
 *
 * @author idali
 */
@Named(value = "scientificLoanForm")
@SessionScoped
@Slf4j
public class ScientificLoanForm implements Serializable {
    
    private final String nameFiledKey = "name";
//    private final String educationalPdf = "/pdf?pdf=education"; 
    private final String scientificPdf = "/pdf?pdf=scientific"; 
    private final String emptyString = "";
    
    private final String emailFailStatus = "Email failed";
 
    private String requestType; 
    private String selectedCollection;
    
    private String additionInformation;
    private String photoInstruction;
    private String destractive;
    
    private String loanPolicyPath;
    
    private String citesNumber;
    private boolean noCITE = false;
    
    private List<SelectItem> collectionItems;
    private Map<String, List<Collection>> map;
    
    private String descriptionOfLoan;
    private String destructiveMethod;
    private boolean isPolicyRead;
    
    private String servletPath;
    
    private Loan loan; 
    private boolean isSwedish;  
    
    @Inject
    private LoanForm form; 
    @Inject
    private LoanFileHandler fileHander; 
    @Inject
    private FileManager fileManager; 
    @Inject
    private PDFCreator pdf; 
    @Inject
    private MongoService service;  
    @Inject
    private Message message; 
    @Inject
    private MailBuilder mailBuilder; 
    @Inject
    private BorrowerController borrower; 
    @Inject
    private SampleBean samples;
     
    public ScientificLoanForm() { 
    }
    
    @PostConstruct
    public void init() {
        servletPath = form.getPdfPath();
        loanPolicyPath = servletPath + scientificPdf; 
        
        requestType = RequestType.Physical.getText(); 
        isPolicyRead = false;
         
        if (map == null || map.isEmpty()) {
            map = new LinkedHashMap<>();
            map = service.findAllScientificCollection();

            collectionItems = new ArrayList<>();

            map.entrySet().stream().map((entry) -> {
                SelectItemGroup group = new SelectItemGroup(entry.getKey());
                SelectItem[] list = new SelectItem[entry.getValue().size()];
                int count = 0;
                for (Collection value : entry.getValue()) {
                    list[count] = new SelectItem(value.getName(), value.getName());
                    count++;
                }
                group.setSelectItems(list);
                return group;
            }).forEach((group) -> {
                collectionItems.add(group);
            });
        }
    }
  
    public void resetLocale(boolean isSwedish) {
        this.isSwedish = isSwedish; 
        samples.resetLocale(isSwedish);
    }
    
    public void submit() throws MessagingException, Exception {
        log.info("submit");
 
        loan = form.buildLoanInitialData();
        Collection collection = service.findCollection(selectedCollection, nameFiledKey);

        loan.setCurator(collection.getEmail());
        loan.setManager(collection.getManager());
        log.info("collection email : {} -- {}", collection.getManager(), collection.getEmail());
           
        buildLoan();
        buildBorrow(borrower.isHasPrimaryUser());
        
        try { 
            String path = fileHander.transferFiles(loan.getUuid());
             
            pdf.createScientificLoanPDF(loan, requestType, isSwedish, path);
            service.saveLoan(loan);
            mailBuilder.buildEmail(loan, loanPolicyPath, servletPath,
                    form.getLoanDocumentPath(), form.getAdminPath(), 
                    isSwedish, borrower.isHasPrimaryUser()); 
            
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
            service.updateLoan(loan);
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.SendingEmailsFailed, isSwedish));
            resetData();
            throw new MessagingException(NameMapping.getMsgByKey(CommonNames.SendingEmailsFailed, isSwedish));
        } catch (IOException ex) {
            log.error(ex.getMessage());
            message.addError(emptyString,
                    NameMapping.getMsgByKey(CommonNames.RequestFailed, isSwedish)); 
            resetData();
            throw new Exception(NameMapping.getMsgByKey(CommonNames.RequestFailed, isSwedish));
        }
    }
    
    private void buildLoan() {
        
        if(!RequestType.Information.isInformation(requestType)) {
            loan.setCitesNumber(citesNumber);
            loan.setLoanDescription(descriptionOfLoan);
            if (fileManager.getProjectFile() != null) {
                loan.setLoanDescriptionFile(fileManager.getProjectFileName()); 
            }
            if(isIsPhoto()) {
                loan.setPhotoInstraction(photoInstruction);
                if (fileManager.getPhotoInstructionFile() != null) {
                    loan.setPhotoInstractionFile(fileManager.getPhotoInstructionFileName());
                } 
            } else {
                loan.setDestructiveMethod(destructiveMethod);
                if (fileManager.getDestructiveMethodFile() != null) {
                    loan.setDestructiveFile(fileManager.getDestructiveMethodFileName());
                } 
            }
        }  
        loan.setType(requestType);
        loan.setReleventCollection(selectedCollection);
        loan.setSampleSetAdditionalInfo(additionInformation);
          
        loan.setSamples(samples.getSamples()); 
    }
    
    private void buildBorrow(boolean hasPrimaryUser) {
        log.info("buildBorrow : {}", hasPrimaryUser);
        
        loan.setBorrower(borrower.getUserFullName());
        
        if(hasPrimaryUser) {
            loan.setPrimaryUser(borrower.getPrimaryUser());
            loan.setSecondaryUser(borrower.getUser()); 
        } else {
            loan.setPrimaryUser(borrower.getUser());  
            loan.setSecondaryUser(null); 
        }
        
    }
    
    public void resetData() {  
        log.info("resetData");
        requestType = RequestType.Physical.getText();
        selectedCollection = null;
        additionInformation = null;
        photoInstruction = null; 
        citesNumber = null;
        noCITE = false;
        descriptionOfLoan = null;
        destructiveMethod = null;
        isPolicyRead = false;
        
        destractive = null;
        
        fileManager.setPhotoInstructionFile(null);
        fileManager.setPhotoInstructionFileName(null);
        fileManager.setProjectFile(null);
        fileManager.setProjectFileName(null);
        fileManager.setDestructiveMethodFile(null);
        fileManager.setDestructiveMethodFileName(null);
        
        borrower.resetData();
        
        samples.resetData();
    }
    
    public void onIsPolicyReadStatusChange() {
        log.info("onIsPolicyReadStatusChange : {}", isPolicyRead);
    }
    
    
    public void saveText() {
        log.info("saveText");
    }
     
    public void collectionChanged() {
        log.info("collectionChanged : {}", selectedCollection);
    }
     
    public void onCITESChanged() {
        log.info("onCITESChanged : {}", noCITE);

        if (noCITE) {
            citesNumber = CommonString.getNoCITESText(isSwedish);
        } else {
            citesNumber = null;
        }
    }
    
    public void selectDestractive() {
        log.info("selectDestractive : {}", destractive);  
    }
 
    public void handleDesctractiveMethod() {
        log.info("handleProjectDescription : {}", destructiveMethod);
//        setIsDestractiveMethodSet();
    }
 
 
    public boolean isSubmitBtnDisabled() {
        return isIsInformation() ? false : !isPolicyRead;
    }
 
    public boolean isIsPhoto() {
        return RequestType.Photo.isPhoto(requestType);
    }
    
    public boolean isIsInformation() {
        return RequestType.Information.isInformation(requestType); 
    }
    
    public boolean isIsPhysical() {
        return RequestType.Physical.isPhysical(requestType); 
    }
    
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDescriptionOfLoan() {
        return descriptionOfLoan;
    }

    public void setDescriptionOfLoan(String descriptionOfLoan) {
        this.descriptionOfLoan = descriptionOfLoan;
    }

    public String getSelectedCollection() {
        return selectedCollection;
    }

    public void setSelectedCollection(String selectedCollection) {
        this.selectedCollection = selectedCollection;
    }

    public List<SelectItem> getCollectionItems() {
        return collectionItems;
    }

    public void setCollectionItems(List<SelectItem> collectionItems) {
        this.collectionItems = collectionItems;
    } 
     
    public String getAdditionInformation() {
        return additionInformation;
    }

    public void setAdditionInformation(String additionInformation) {
        this.additionInformation = additionInformation;
    }

    public String getPhotoInstruction() {
        return photoInstruction;
    }

    public void setPhotoInstruction(String photoInstruction) {
        this.photoInstruction = photoInstruction;
    }

    public String getDestructiveMethod() {
        return destructiveMethod;
    }

    public void setDestructiveMethod(String destructiveMethod) {
        this.destructiveMethod = destructiveMethod;
    }
    

    public String getCitesNumber() {
        return citesNumber;
    }

    public void setCitesNumber(String citesNumber) {
        this.citesNumber = citesNumber;
    }

    public boolean isNoCITE() {
        return noCITE;
    }

    public void setNoCITE(boolean noCITE) {
        this.noCITE = noCITE;
    }

    public boolean isIsPolicyRead() {
        return isPolicyRead;
    }

    public void setIsPolicyRead(boolean isPolicyRead) {
        this.isPolicyRead = isPolicyRead;
    }

    public String getDestractive() {
        return destractive;
    }

    public boolean isDisableDestractiveBtn() {
        if (destractive == null) {
            return true;
        }

        if (Boolean.parseBoolean(destractive)) {
            return StringUtils.isBlank(destructiveMethod)
                    && fileManager.getDestructiveMethodFile() == null;
        } else {
            return false;
        }
    }
 
    public void setDestractive(String destractive) {
        this.destractive = destractive;
    }
    
    public boolean isIsDestractiveLoan() {  
        return destractive == null ? false : Boolean.parseBoolean(destractive);
    }  
}
