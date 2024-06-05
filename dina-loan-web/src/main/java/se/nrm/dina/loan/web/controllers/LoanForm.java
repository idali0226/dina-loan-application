package se.nrm.dina.loan.web.controllers;
  
import java.io.Serializable; 
import java.sql.Timestamp; 
import java.util.UUID;
import javax.annotation.PostConstruct; 
import javax.enterprise.context.SessionScoped;  
import javax.inject.Inject;
import javax.inject.Named;  
import lombok.extern.slf4j.Slf4j;   
//import se.nrm.dina.loan.web.beans.BreadCrumbBean;
import se.nrm.dina.loan.web.beans.StyleBean;
import se.nrm.dina.loan.web.config.InitialProperties;
import se.nrm.dina.loan.web.filehander.LoanFileHandler;  
import se.nrm.dina.loan.web.util.Department;
import se.nrm.dina.loan.web.util.LoanPurpose;  
import se.nrm.dina.loan.web.util.UUIDGenerator;
import se.nrm.dina.loan.web.util.Util;  
import se.nrm.dina.mongodb.jdbc.MongoJDBC; 
import se.nrm.dina.mongodb.loan.vo.Loan; 

/**
 *
 * @author idali
 */
@Named(value = "loan")
@SessionScoped
@Slf4j
public class LoanForm implements Serializable {
 
    private UUID uuid;

    private final String loanReqNumberPrefix = "ReqNo";
    private final String loanInitialStatus = "Request pending";  
 
      
    private final String reqNumberFormat = "%06d";
    
     
    
    private boolean openSummary = false;
     
    private boolean holiday = false;
 
    private String department;
    private String purpose; 
    private String sbdiBasUrl; 
 
    private Loan loan;

    private int currentYear;

    private boolean isSwedish;
    
    private String pdfPath; 
    private String loanDocumentPath;
    private String adminPath; 
 

//    @Inject
//    private BreadCrumbBean breadCrumb;

    @Inject
    private MongoJDBC mongo;
 

    @Inject
    private Navigation navigator;

    @Inject
    private InitialProperties properties;

//    @Inject
//    private Languages languages;

    @Inject
    private LoanFileHandler fileHander;   
       
    @Inject
    private StyleBean style;

    @Inject
    private OtherDepartments otherDepartments;

    public LoanForm() { 
    }

    @PostConstruct
    public void init() {
        log.info("init"); 

        holiday = Util.getInstance().isHoliday();

        currentYear = Util.getInstance().getCurrentYear();
        log.info("is holiday .... : {}", holiday);

        department = Department.Zoology.getText();
        purpose = LoanPurpose.ScientificPurpose.getText(); 
  
        pdfPath = properties.getHost();
        loanDocumentPath = properties.getLoanFilePath();
        adminPath = properties.getAdmin();
        sbdiBasUrl = properties.getSbdiUrl();
    }

    public void closesummary() {
        log.info("colsesummary");
        openSummary = false;
    }

    public boolean isOpenSummary() {
        return openSummary;
    }
    
    
    public void reviewrequest() {
        log.info("reviewrequest");
        openSummary = true;
    }

    public String getPurposeName() {
        switch (purpose) {
            case "scientificpurpose":
                return "Scientific purpose";
            case "educationexhibition":
                return "Education/Exhibition";
            case "commercialartother":
                return "Commercial/art/other";
            default:
                return "Scientific purpose";
        }
    }
  
    public void resetData(boolean isHome) {
        log.info("resetData : {}", isHome);

        if (isHome) {
            department = Department.Zoology.getText(); 
            purpose = LoanPurpose.ScientificPurpose.getText();
            openSummary = false;

            if (uuid != null) {
                fileHander.removeTempDirectory(uuid.toString());
                uuid = null;
            }
        }  
    }
    
    public boolean isUUIDExist() {
        return uuid != null;
    }

    public UUID getUUID() {
        if (uuid == null) {
            uuid = getUniqueUUID();
        }
        log.info("uuid : {}", uuid);
        return uuid;
    }

    public String getTempDirectory() {
        return properties.getTempFilePath();
    }

    public boolean isSwedish() {
        return isSwedish;
    }
    
    public void resetLocale(boolean isSwedish) {
        this.isSwedish = isSwedish;
        otherDepartments.setDepartmentName(Department.getDepartName(department, isSwedish));
        otherDepartments.setUrl(properties.getDepartmentUrl(department, isSwedish));
        otherDepartments.setDepartmentCollectionName(
                Department.getDepartCollectionName(department, isSwedish));
    }

    public Loan buildLoanInitialData() {

        if (uuid == null) {
            uuid = getUniqueUUID();
        }
          
        loan = new Loan(uuid);
        loan.setId(buildLoanId());
        
        loan.setSubmitDate(Util.getInstance()
                .dateToString(new Timestamp(System.currentTimeMillis())));

        loan.setDepartment(getDepartmentName());
        loan.setPurpose(getPurposeName());
        loan.setStatus(loanInitialStatus);  
        
        return loan;
    }
    
    private String buildLoanId() {
        return loanReqNumberPrefix
                + String.format(reqNumberFormat, mongo.getNextSequence());
    }

    public String getDepartmentName() {
        switch (department) {
            case "botany":
                return "Botany";
            case "envionmentspecimenbank":
                return "Envionment Specimen Bank";
            case "geology":
                return "Geology";
            case "palaeobiology":
                return "Palaeobiology";
            case "zoology":
                return "Zoology";
        }
        return "Zoology";
    }
    
    public void departmentChanged() {
        log.info("departmentChanged : {}", department);
 
        boolean isImplemented = Department.Zoology.isZoology(department);
        if (isImplemented) {
            resetData(false);
            purpose = LoanPurpose.ScientificPurpose.getText();
        } else {
            otherDepartments.setDepartmentName(Department.getDepartName(department, isSwedish));
            otherDepartments.setUrl(properties.getDepartmentUrl(department, isSwedish));
            otherDepartments.setDepartmentCollectionName(
                    Department.getDepartCollectionName(department, isSwedish));
        } 
    } 
 

    public void start() {
        log.info("start");

        resetData(true);

        style.setCurrentTab(1);
        navigator.gotoHomePage();
    }
  
 
    public void saveText() {
        log.info("saveText");
    }
 

    private UUID getUniqueUUID() {
        log.info("getUniqueUUID");

        uuid = UUIDGenerator.generateUUID();
        while (mongo.uuidExist(uuid)) {
            uuid = UUIDGenerator.generateUUID();
        }
        return uuid;
    }
 
    public boolean isHolidaySeason() {
        return holiday;
    }
 
    public boolean isBotanyDepartment() {
        return Department.Botany.name().equalsIgnoreCase(department);
    }

    public boolean isEsbDepartment() {
        return Department.EnvionmentSpecimenBank.name().equalsIgnoreCase(department);
    }

    public boolean isZoologyDepartment() {
        return Department.Zoology.name().equalsIgnoreCase(department);
    }

    public boolean isGeologyDepartment() {
        return Department.Geology.name().equalsIgnoreCase(department);
    }

    public boolean isPalaeobiologyDepartment() {
        return Department.Palaeobiology.name().equalsIgnoreCase(department);
    }
 

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
 
    public String getSbdiBaseUrl() {
        return sbdiBasUrl;
    }
 
    public int getCurrentYear() {
        return currentYear;
    }
  
    public String getLoanId() { 
        return loan == null ? null : loan.getId();
    }
 
    public boolean isScientificLoan() {
        return LoanPurpose.ScientificPurpose.isScientificPurpose(purpose);
    }
 
    
    public String getPdfPath() {
        return pdfPath;
    }

    public String getLoanDocumentPath() {
        return loanDocumentPath;
    } 

    public String getAdminPath() {
        return adminPath;
    }  
}
