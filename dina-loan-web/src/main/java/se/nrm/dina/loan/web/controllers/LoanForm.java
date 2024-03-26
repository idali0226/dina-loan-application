package se.nrm.dina.loan.web.controllers;
  
import java.io.Serializable; 
import java.sql.Timestamp; 
import java.util.UUID;
import javax.annotation.PostConstruct; 
import javax.enterprise.context.SessionScoped;  
import javax.inject.Inject;
import javax.inject.Named;  
import lombok.extern.slf4j.Slf4j;   
import se.nrm.dina.loan.web.beans.BreadCrumbBean;
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
 
    
    private final String localhost = "localhost";
    
    private final String localhostAndPortal = "http://localhost:8180";
    private final String https = "https://";
   
    private final String reqNumberFormat = "%06d";
    
    
    
    
    private boolean openSummary = false;
    
     
//    private Map<String, List<Collection>> map;
//    private List<SelectItem> collectionItems; 
 
    private boolean holiday = false;

//    private boolean isPolicyRead = false;

    private String department;
    private String purpose;
//    private String requestType; 
    private String sbdiBasUrl;
 
//    private String commercialLoanType;
 
    private Loan loan;

    private int currentYear;

    private boolean isSwedish;
    
    private String pdfPath; 
 

    @Inject
    private BreadCrumbBean breadCrumb;

    @Inject
    private MongoJDBC mongo;
 

    @Inject
    private Navigation navigator;

    @Inject
    private InitialProperties properties;

    @Inject
    private Languages languages;

    @Inject
    private LoanFileHandler fileHander;   
       
    @Inject
    private StyleBean style;

    @Inject
    private OtherDepartments otherDepartments;

    public LoanForm() {
//        samples = new ArrayList<>();
//        sample = new Sample();
//        sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
//
//        isSwedish = false;
//
//        user = new User();
//        primaryUser = new User();
//        user.getAddress().setCountry(swedenEn);
//        primaryUser.getAddress().setCountry(swedenEn);

//        commercialLoanType = RequestType.Physical.getText(); 
    }

    @PostConstruct
    public void init() {
        log.info("init"); 

        holiday = Util.getInstance().isHoliday();

        currentYear = Util.getInstance().getCurrentYear();
        log.info("is holiday .... : {}", holiday);

        department = Department.Zoology.getText();
        purpose = LoanPurpose.ScientificPurpose.getText();
//        requestType = RequestType.Physical.getText();
 
 
        String host = properties.getHost();
        if(host.equals(localhost)) {
            pdfPath = localhostAndPortal;
        } else {
            pdfPath = https + host;
        }
        
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
//            breadCrumb.resetNavigationPathToHomePage();
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
        return languages.isIsSwedish();
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
   

  
    
//    private void sendMails(String loanId)
//            throws MessagingException, AddressException, UnsupportedEncodingException {
//
//        log.info("sendMails : {}", loanId);
//
//        Map<String, String> emailMap = new HashMap<>();
//        emailMap.put("isSwedish", String.valueOf(isSwedish));
//        emailMap.put("loanId", loanId);
//        emailMap.put("primarytitle", loan.getPrimaryUser().getTitle());
//        emailMap.put("primarylastname", loan.getPrimaryUser().getLastname());
//        emailMap.put("primaryFirstname", loan.getPrimaryUser().getFirstname());
//        emailMap.put("primaryemail", loan.getPrimaryUser().getEmail());
//
//        emailMap.put("collection", loan.getDepartment());
//        emailMap.put("purpose", loan.getPurpose());
//        emailMap.put("type", loan.getType());
//        emailMap.put("subcollection", loan.getReleventCollection());
//
//        if (loan.getPrimaryUser().getDepartment() != null && !loan.getPrimaryUser().getDepartment().isEmpty()) {
//            emailMap.put("primarydepartment", loan.getPrimaryUser().getDepartment());
//        }
//        emailMap.put("primaryinstitution", loan.getPrimaryUser().getInstitution());
//        emailMap.put("primarycountry", loan.getPrimaryUser().getAddress().getCountry());
//
////        emailMap.put("hasPrimaryContact", String.valueOf(hasPrimaryContact));
//        emailMap.put("isLocal", String.valueOf(true));
////        emailMap.put("isLocal", String.valueOf(server.equals(LOCALHOST)));
//
////        if (hasPrimaryContact) {
////            emailMap.put("secondarytitle", loan.getSecondaryUser().getTitle());
////            emailMap.put("secondarylastname", loan.getSecondaryUser().getLastname());
////            emailMap.put("secondaryfirstname", loan.getSecondaryUser().getFirstname());
////            emailMap.put("secondaryemail", loan.getSecondaryUser().getEmail());
////            if (loan.getSecondaryUser().getDepartment() != null && !loan.getSecondaryUser().getDepartment().isEmpty()) {
////                emailMap.put("secondarydepartment", loan.getSecondaryUser().getDepartment());
////            }
////            emailMap.put("secondaryinstitution", loan.getPrimaryUser().getInstitution());
////            emailMap.put("secondarycountry", loan.getPrimaryUser().getAddress().getCountry());
////        }
//
//        emailMap.put("department", Department.getDepartName(department));
//
//        log.info("uuid : {}", uuid);
//        StringBuilder sb = new StringBuilder();
//        sb.append(uuid.toString());
//        sb.append("-loanrequest_");
//        sb.append(loanId);
//
//        StringBuilder adminSb = new StringBuilder(sb.toString());
//        adminSb.append("_admin.pdf");
//
//        sb.append(".pdf");
//        emailMap.put("summaryFile", sb.toString().trim());
//        emailMap.put("adminSummaryFile", adminSb.toString().trim());
//
////        String curatorEmail = null;
////        if(loan.getCurator() != null && !loan.getCurator().isEmpty()) {
////            curatorEmail = loan.getCurator();
////        } else {
////            Collection collection = (mongo.findCollection("Non scientific", "group")); 
////            if(collection != null) {
////                curatorEmail = collection.getManager();
////            }
////        }
//        TblUsers curator = dao.findOneUserByEmail(loan.getCurator());
//        if (curator != null) {
//            emailMap.put("outofoffice", String.valueOf(curator.getOnvacation()));
//        }
//
////        emailMap.put("curratormail", loan.getCurator());
//        emailMap.put("curratormail", "ida.li@nrm.se");
//        emailMap.put("manager", loan.getManager());
//
////        if (isScientificLoan() && selectedCollection != null) {
////            emailMap.put("loanPolicy", "testScientificLoanPath");
////        } else {
////            emailMap.put("loanPolicy", "educationLoanPath");
////        }
//
//        if (false) {
//            mail.send(emailMap);
//        } else {
//            nrmMail.send(emailMap);
//        }
//    }

    public void changelanguage(String locale) {
        log.info("changelanguage - locale: {}", locale);

        languages.setLocale(locale);
        isSwedish = languages.isIsSwedish(); 
//
        breadCrumb.resetLocale(isSwedish);
        departmentChanged(); 
    }

    public void start() {
        log.info("start");

        resetData(true);

        style.setCurrentTab(1);
        navigator.gotoHomePage();
    }
 

//    public void backtoform() {  
//        style.setCurrentTab(1);
//    }
 
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
 
//    public void purposeCheckChanged() {
//        log.info("purposeCheckChanged"); 
//    }
 
    public boolean isHolidaySeason() {
        return holiday;
    }

//    public boolean isIsInformation() {
//        return RequestType.Information.isInformation(requestType);
//    }

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

//    public boolean isIsPhysical() {
//        return RequestType.Physical.isPhysical(requestType);
//    }

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

//    public String getRequestType() {
//        return requestType;
//    }
//
//    public void setRequestType(String requestType) {
//        this.requestType = requestType;
//    }
// 
 
  
    public String getSbdiBaseUrl() {
        return sbdiBasUrl;
    }
// 
//    public boolean isIsPhoto() {
//        return RequestType.Photo.isPhoto(requestType);
//    }
// 
    public int getCurrentYear() {
        return currentYear;
    }
  
    public String getLoanId() { 
        return loan == null ? null : loan.getId();
    }
//
//    public boolean isIsPolicyRead() {
//        return isPolicyRead;
//    }
//
//    public void setIsPolicyRead(boolean isPolicyRead) {
//        this.isPolicyRead = isPolicyRead;
//    }

    public boolean isScientificLoan() {
        return LoanPurpose.ScientificPurpose.isScientificPurpose(purpose);
    }

//    private boolean isPhoto() {
//        return RequestType.Photo.isPhoto(requestType);
//    }
//
//    private boolean isInformational() {
//        return RequestType.Information.isInformation(requestType);
//    }
// 
//    public String getCommercialLoanType() {
//        return commercialLoanType;
//    }
//
//    public void setCommercialLoanType(String commercialLoanType) {
//        this.commercialLoanType = commercialLoanType;
//    }
    
    public String getPdfPath() {
        return pdfPath;
    }
 
}
