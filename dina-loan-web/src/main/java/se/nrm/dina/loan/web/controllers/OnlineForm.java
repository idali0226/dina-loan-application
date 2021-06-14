package se.nrm.dina.loan.web.controllers;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.email.Mail;
import se.nrm.dina.email.NrmMail;
import se.nrm.dina.loan.web.beans.BreadCrumbBean;
import se.nrm.dina.loan.web.beans.StyleBean;
import se.nrm.dina.loan.web.filehander.FileHandler;
import se.nrm.dina.loan.web.pdf.PDFCreator;
import se.nrm.dina.loan.web.service.SolrClient;
import se.nrm.dina.loan.web.util.CommonNames;
import se.nrm.dina.loan.web.util.CommonString;
import se.nrm.dina.loan.web.util.CountryNames;
import se.nrm.dina.loan.web.util.Department;
import se.nrm.dina.loan.web.util.LoanPurpose;
import se.nrm.dina.loan.web.util.NameMapping;
import se.nrm.dina.loan.web.util.RequestType;
import se.nrm.dina.loan.web.util.UUIDGenerator;
import se.nrm.dina.loan.web.util.Util;
import se.nrm.dina.loan.web.vo.SolrRecord;
import se.nrm.dina.manager.dao.AccountDao;
import se.nrm.dina.manager.entities.TblUsers;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Collection;
import se.nrm.dina.mongodb.loan.vo.Loan;
import se.nrm.dina.mongodb.loan.vo.Sample;
import se.nrm.dina.mongodb.loan.vo.User;

/**
 *
 * @author idali
 */
@Named(value = "onlineForm")
//@ApplicationScoped
@SessionScoped
public class OnlineForm implements Serializable {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private String department;
  private String purpose;
  private String requestType;
  private String descriptionOfLoan = " - ";

  private String destractive;                         // radio button to indicate the sample involves destractive sample
  private String additionInformation = " - ";
  private String destructiveMethod = " - ";

//    private String appropriate;
  private String photoInstruction = " - ";
  private String citesNumber = " - ";
  private String page3requestType;                    // radio buttons for commercial loan request type
  private String page4Description = " - ";
  private String exhPorpuseDesc = " - ";

  private final String GBIF_BASE_URL = "http://old.gbif.se/search/";
  private String gbifUrl;

  private final String LOAN_REQUEST_PREFIX = "ReqNo";

  private String scPurposeFileName;
  private String edPurposeFileName;
  private String destructiveMethodFileName;
  private String photoInstructionFileName;

  private int section = 1;
  private boolean isPhoto = false;
  private boolean isAgree = false;
  private boolean isCollectionSelect = false;
  private boolean isPolicyRead = false;
  private boolean isDestractiveSelected = false;
  private boolean openSummary = false;
  private boolean hasPrimaryContact = false;
  private boolean isPurposeChecked = false;
  private boolean isDestractive = false;
  private boolean isPhd = false;
  private boolean isPolicyLinkEnabled = false;
  private boolean isDestructivePolicyLinkEnabled = false;
  private boolean isImplemented = true;                                           // to indecat if this department is implemented or not

  private int oldStep = 0;
  private int oldSection = 0;

  private Loan loan;

//    private final boolean isLocal;
  private boolean isSwedish = false;

  private boolean isSuccess = false;

  private boolean noCITE = false;
  private boolean disableCITE = false;

  private String educationPurpose;                                // radio button for select purpose for education loan

  private Date fromDate;
  private Date toDate;
  private Date minDate;
  private Date toMinDate;

  private int step;

  private String dinaSearch = "dina";
  private String currentpage = "";

  private User contact1;
  private User contact2;

  private final List<Sample> samples;
  private Sample sample;

  private Map<String, List<Collection>> map;
  private List<SelectItem> collectionItems;
  private Map<String, Integer> statisticMap;

  private List<SolrRecord> records;
  private SolrRecord selectedRecord;

  private int numOfPages;

  private String loanRequestPeriod;
//https://localhost:8443/dina-external-datasource/pdf?pdf=Loanpolicycommon.pdf 
  
  private final String LOCAL_EXTERNAL_FILES = "https://localhost:8443/dina-external-datasource/pdf?pdf=";
  private final String REMOTE_EXTERNAL_FILES_AS = "https://dina-web.net/dina-external-datasource/pdf?pdf=";

  private final String DESTRUCTIVE_SAMPLING_POLICY;
  private final String SCIENTIFIC_PURPOSE_LOAN_POLICY;
  private final String EDUCATIONAL_PURPOSE_LOAN_POLICY;

  private String selectedCountry;

  private List<String> selectedCatalogNumbers;

  private final String NON_SCIENTIFIC_LOANS = "Non scientific";
  private final int CURRENT_YEAR;

  private String selectedCollection;

  private boolean isProjectDescriptionSet;
  private boolean isDestractiveLoan;

  @Inject
  private BreadCrumbBean breadCrumb;

  @Inject
  private Mail mail;

  @Inject
  private NrmMail nrmMail;

  @Inject
  private SolrClient solr;

  @Inject
  private PDFCreator pdf;

  @Inject
  private Languages languages;

  @Inject
  private StyleBean style;

  @Inject
  private MongoJDBC mongo;

  @EJB
  private AccountDao dao;

  @Inject
  private FileHandler fileHander;

  private UUID uuid;

  private RequestContext requestContext;

  private final String servername;
  private final String server;
  private final String host;
  
  private final String sweden = "Sweden";

  private final String LOCALHOST = "localhost";
//    private final String DINA_WEB = "dina-web";

  /**
   * Creates a new insta;nce of OnlineForm
   */
  public OnlineForm() {

    logger.info("OnlinForm os.name : {}", System.getProperty("os.name"));

    String basePath;
    servername = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
    
    if (servername.contains(LOCALHOST)) {
      basePath = LOCAL_EXTERNAL_FILES;
      server = "localhost";
      host = server + ":8443";
    } else {
      basePath = REMOTE_EXTERNAL_FILES_AS;
      server = "dina-web";
      host = "dina-web.net";
    }

    DESTRUCTIVE_SAMPLING_POLICY = basePath + "Loanpolicyforscientificpurposes.pdf#page=2";
    SCIENTIFIC_PURPOSE_LOAN_POLICY = basePath + "Loanpolicyforscientificpurposes.pdf";
    EDUCATIONAL_PURPOSE_LOAN_POLICY = basePath + "Loanpolicycommon.pdf";

    department = Department.Zoology.getText();
    purpose = LoanPurpose.ScientificPurpose.getText();
    requestType = RequestType.Physical.getText();

    selectedCollection = null;

    selectedCatalogNumbers = new ArrayList<>();
    records = new ArrayList<>();

    page3requestType = RequestType.Physical.getText();
    step = 0;

    contact1 = new User();
    contact2 = new User();
    contact1.getAddress().setCountry(sweden);
    contact2.getAddress().setCountry(sweden);

    samples = new ArrayList<>();
    sample = new Sample();
    sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
    section = 1;
    minDate = Util.addWeeksToDate(2);
    toMinDate = minDate;

    Calendar now = Calendar.getInstance();
    CURRENT_YEAR = now.get(Calendar.YEAR);

    uuid = null;

    numOfPages = 9;
  }

  @PostConstruct
  public void init() {

    logger.info("init");

    if (map == null || map.isEmpty()) {
      map = new LinkedHashMap<>();
      map = mongo.findAllScientificCollection();

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

    logger.info("statisticMap : {}", statisticMap);
    if (statisticMap == null || statisticMap.isEmpty()) {
      statisticMap = new HashMap<>();
      statisticMap = mongo.getStatisticData();
    }
  }

  public List<SelectItem> getCollectionItems() {
    return collectionItems;
  }

  public void setCollectionItems(List<SelectItem> collectionItems) {
    this.collectionItems = collectionItems;
  }

  private void resetData(boolean isHome) {
    logger.info("resetData");

    if (isHome) {
      department = Department.Zoology.getText();
      breadCrumb.resetNavigationPathToHomePage();
      step = 0;
      numOfPages = 9;
      style.setCurrentTab(1);
      isImplemented = true;
      purpose = LoanPurpose.ScientificPurpose.getText();

      if (uuid != null) {
        fileHander.removeTempDirectory(uuid);
        uuid = null;
      }
    }

    isSuccess = false;

    oldSection = 0;
    oldStep = 0;

    educationPurpose = null;
    requestType = RequestType.Physical.getText();
    page3requestType = RequestType.Physical.getText();

    // attachement file name
    scPurposeFileName = null;
    edPurposeFileName = null;
    destructiveMethodFileName = null;
    photoInstructionFileName = null;
    // attachement file name

    isCollectionSelect = false;
    openSummary = false;
    isPurposeChecked = false;
    hasPrimaryContact = false;
    isAgree = false;
    isPolicyRead = false;
    isDestractive = false;
    isDestractiveLoan = false;
    isPhd = false;
    isPolicyLinkEnabled = false;
    isDestructivePolicyLinkEnabled = false;

    noCITE = false;
    disableCITE = false;

    isDestractiveSelected = false;

    destractive = null;
    selectedCollection = null;

    descriptionOfLoan = " - ";
    additionInformation = " - ";
    exhPorpuseDesc = " - ";
    destructiveMethod = " - ";
    currentpage = "";

    page4Description = " - ";
    fromDate = null;
    toDate = null;
    samples.clear();
    sample = new Sample();
    sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));

    isProjectDescriptionSet = false;

    isPhoto = false;
    photoInstruction = " - ";

    citesNumber = " - ";
    contact1 = new User();
    contact2 = new User();

    if (isSwedish) {
      contact1.getAddress().setCountry("Sverige");
      contact2.getAddress().setCountry("Sverige");
    } else {
      contact1.getAddress().setCountry("Sweden");
      contact2.getAddress().setCountry("Sweden");
    }

    selectedCatalogNumbers = new ArrayList<>();

  }

  /**
   * changelanguage - change ui language
   *
   * @param locale
   */
  public void changelanguage(String locale) {

    logger.info("changelanguage - locale: {} -- {}", locale, currentpage);

    languages.setLocale(locale);
    isSwedish = languages.isIsSwedish();
    languages.setOpenGbif(false);

    breadCrumb.resetLocale(isSwedish, isImplemented, isPhoto, numOfPages);
    if (locale.equals("sv")) {
      contact1.getAddress().setCountry("Sverige");
      contact2.getAddress().setCountry("Sverige");
    } else {
      contact1.getAddress().setCountry("Sweden");
      contact2.getAddress().setCountry("Sweden");
    }

    if (sample.getType().equals("ej specifierad") || sample.getType().equals("Not specified")) {
      sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
    }
    samples.stream().filter((samp) -> (samp.getType().equals("ej specifierad") || samp.getType().equals("Not specified"))).forEach((samp) -> {
      samp.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
    });
  }

  /**
   * To go to start page (home page)
   */
  public void start() {
    logger.info("start");

    section = 1;
    if (step > 0) {
      resetData(true);
    }
    style.setCurrentTab(1);
  }

  public void about() {
    logger.info("about");

    if (style.getCurrentTab() == 1) {
      oldSection = section;
      oldStep = step;
    }

    breadCrumb.setNavigationPathEnabled(false);

    section = 2;
    step = 999;
    style.setCurrentTab(2);
  }

  public void readloanpolicies() {
    logger.info("readloanpolicies");

    if (style.getCurrentTab() == 1) {
      oldSection = section;
      oldStep = step;
    }

    breadCrumb.setNavigationPathEnabled(false);

    section = 3;
    step = 999;
    style.setCurrentTab(3);
  }

  public void contact() {
    logger.info("contact");

    if (style.getCurrentTab() == 1) {
      oldSection = section;
      oldStep = step;
    }

    breadCrumb.setNavigationPathEnabled(false);

    section = 4;
    step = 999;
    style.setCurrentTab(4);
  }

  public void backtoform() {

    breadCrumb.setNavigationPathEnabled(true);
    section = oldSection;
    step = oldStep;
    style.setCurrentTab(1);
  }

  /**
   * Go to departments selection page
   */
  public void goToDepartments() {
    logger.info("goToDepartments");

    if (step == 0) {
      breadCrumb.setNext(breadCrumb.getHomeItem());
    } else {
      breadCrumb.setPrevious(breadCrumb.getPage1Item());
    }
    currentpage = "";
    isImplemented = true;
    step = 1;
    section = 10;
  }

  private UUID getUniqueUUID() {

    logger.info("getUniqueUUID");

    uuid = UUIDGenerator.generateUUID();
    while (mongo.uuidExist(uuid)) {
      uuid = UUIDGenerator.generateUUID();
    }
    return uuid;
  }

  public void goToPage1() {
    logger.info("goToPage1 : department: {}", department);

    if (department.equals(Department.Zoology.getText())) {

      logger.info("uuid : {}", uuid);
      if (uuid == null) {
        uuid = getUniqueUUID();
      }
      section = 11;
    } else {
      if (department.equals(Department.Botany.getText())) {
        section = 110;
      } else if (department.equals(Department.EnvionmentSpecimenBank.getText())) {
        section = 111;
      } else if (department.equals(Department.Geology.getText())) {
        section = 112;
      } else {
        section = 113;
      }
    }
    currentpage = "";
    if (step == 1) {
      breadCrumb.setNext(breadCrumb.getOnlineFormItem());
    } else {
      breadCrumb.setPrevious(breadCrumb.getPage2Item());
    }
    step = 2;
  }

  public void gotoPage2() {
    logger.info("goToPage2");
    section = 500;
    currentpage = "";
    if (step == 2) {
      breadCrumb.setNext(breadCrumb.getPage1Item());
    } else {
      breadCrumb.setPrevious(breadCrumb.getPage3Item());
    }
    step = 3;
  }

  public void gotoPage3() {
    logger.info("goToPage3 -- {} -- {}", requestType, page3requestType);

    if (numOfPages == 9 && RequestType.Information.isInformation(requestType)) {
      if (step == 3) {
        step = 4;
        breadCrumb.enableManuItem(breadCrumb.getPage2Item());

        gotoPage4();
      } else {
        breadCrumb.disableManuItem(breadCrumb.getPage4Item());

        gotoPage2();
      }
    } else {
      switch (numOfPages) {
        case 9:
          currentpage = "descLoanPage3";
          break;
        case 7:
          currentpage = "page32panel";
          break;
        default:
          currentpage = "page33panel";
          break;
      }

      if (step == 3) {
        breadCrumb.setNext(breadCrumb.getPage2Item());
      } else {
        breadCrumb.setPrevious(breadCrumb.getPage4Item());
      }
      step = 4;
    }
  }

  public void gotoPage4() {
    logger.info("goToPage4 : {}", page3requestType);

    if (step == 4) {
      breadCrumb.setNext(breadCrumb.getPage3Item());
    } else {
      breadCrumb.setPrevious(breadCrumb.getPage5Item());
    }

    if (!LoanPurpose.ScientificPurpose.isScientificPurpose(purpose)) {
      currentpage = "page42panel";

      fromDate = null;
      toDate = null;
      minDate = Util.holidayMinDate();

      if (RequestType.Photo.isPhoto(page3requestType)) {
        minDate = Util.addWeeksToDate(2);
      }
    } else {
      currentpage = "page4panel1";
    }
    step = 5;
    languages.setOpenGbif(false);
  }

  public void gotoPage5() {
    logger.info("goToPage5 : {}", selectedCollection);

    isPolicyLinkEnabled = false;
    isDestructivePolicyLinkEnabled = false;
    if (step == 5) {
      breadCrumb.setNext(breadCrumb.getPage4Item());
    } else {
      breadCrumb.setPrevious(breadCrumb.getPage6Item());
    }

    step = 6;
    currentpage = "page51panel";
    switch (numOfPages) {
      case 9:
        currentpage = "page51panel";
        break;
      case 7:
        currentpage = "page52panel";
        break;
      default:
        step = 9;
        currentpage = "";
        break;
    }
    languages.setOpenGbif(false);
  }

  public void gotoPage6() {
    logger.info("goToPage6 : {} -- {}", step, additionInformation);

    if (numOfPages == 9 && RequestType.Information.isInformation(requestType)) {
      if (!validateSampleList()) {
        addError("Value required", "Type can not be empty!!!");
      } else {
        breadCrumb.enableManuItem(breadCrumb.getPage5Item());
        step = 8;
        gotoPage8();
      }
    } else {
      switch (numOfPages) {
        case 9:
          isDestractiveLoan = false;
          if (!validateSampleList()) {
            addError("Value required", "Type can not be empty!!!");
          } else {
            if (RequestType.Photo.isPhoto(requestType)) {
              currentpage = "page6photoInst";
            } else {
              isDestructivePolicyLinkEnabled = true;
              currentpage = "page6Desc";
            }
            if (step == 6) {
              breadCrumb.setNext(breadCrumb.getPage5Item());
            } else {
              breadCrumb.setPrevious(breadCrumb.getPage7Item());
            }
            step = 7;
          } break;
        case 7:
          if (step == 6) {
            breadCrumb.setNext(breadCrumb.getPage5Item());
          } else {
            breadCrumb.setPrevious(breadCrumb.getPage7Item());
          } step = 9;
          currentpage = "page5desc";
          break;
        default:
          if (step == 6) {
            breadCrumb.setNext(breadCrumb.getPage5Item());
          } else {
            breadCrumb.setPrevious(breadCrumb.getPage7Item());
          } isPolicyLinkEnabled = true;
          step = 10;
          currentpage = "summaryPanel";
          break;
      }
    }
    languages.setOpenGbif(false);
  }

  public void gotoPage7() {
    logger.info("goToPage7 : {}", step);

    isPolicyLinkEnabled = false;
    isDestructivePolicyLinkEnabled = false;

    switch (numOfPages) {
      case 9:
        if (RequestType.Information.isInformation(requestType)) {
          breadCrumb.disableManuItem(breadCrumb.getPage8Item()); 
          gotoPage5();
        } else {
          if (step == 7) {
            breadCrumb.setNext(breadCrumb.getPage6Item());
          } else {
            breadCrumb.setPrevious(breadCrumb.getPage8Item());
          }
          currentpage = "citesnum";
          step = 8;
        } break;
      case 7:
        breadCrumb.setPrevious(breadCrumb.getPage6Item());
        currentpage = "";
        step = 6;
        break;
      default:
        breadCrumb.setPrevious(breadCrumb.getPage5Item());
        currentpage = "";
        step = 5;
        break;
    }
  }

  public void gotoPage8() {
    logger.info("goToPage8 : {}", step);

    if (step == 8) {
      breadCrumb.setNext(breadCrumb.getPage7Item());
    } else {
      breadCrumb.setPrevious(breadCrumb.getPage9Item());
      switch (numOfPages) {
        case 9:
          breadCrumb.setPrevious(breadCrumb.getPage9Item());
          break;
        case 7:
          breadCrumb.setPrevious(breadCrumb.getPage7Item());
          break;
        default:
          breadCrumb.setPrevious(breadCrumb.getPage6Item());
          break;
      }
    }

    if (numOfPages == 9 && RequestType.Physical.isPhysical(requestType)) {

      if (contact1 != null && contact1.getTitle() != null) {
        if (contact1.getTitle().contains("student")) {
          hasPrimaryContact = true;
        }
      }
    }
    currentpage = "";
    step = 9;
  }

  public void gotoPage9() {
    logger.info("goToPage9 : {}", step);

    openSummary = false;
    isPolicyLinkEnabled = true;

    switch (numOfPages) {
      case 9:
        breadCrumb.setNext(breadCrumb.getPage8Item());
        break;
      case 7:
        breadCrumb.setNext(breadCrumb.getPage6Item());
        break;
      default:
        breadCrumb.setNext(breadCrumb.getPage5Item());
        break;
    }
    currentpage = "summaryPanel";
    step = 10;
  }

  private void buildLoan(String loanId) {

    loan = new Loan(uuid);
    loan.setId(loanId);

    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
    loan.setSubmitDate(Util.dateToString(timeStamp));

    loan.setDepartment(getDepartmentName());
    loan.setPurpose(getPurposeName());
    loan.setStatus("Request pending");

    Collection c = numOfPages == 9 ? 
            mongo.findCollection(selectedCollection, "name") : 
            mongo.findCollection(NON_SCIENTIFIC_LOANS, "name");
    loan.setCurator(c.getEmail());
    loan.setManager(c.getManager());

    if (numOfPages == 9) {
      loan.setType(requestType);
      loan.setReleventCollection(selectedCollection);

      if (isPhoto) {
        loan.setPhotoInstraction(photoInstruction);
        if (photoInstructionFileName != null && !photoInstructionFileName.isEmpty()) {
          loan.setPhotoInstractionFile(photoInstructionFileName);
        }
      } else {
        loan.setIsDestructive(destractive);
        if (Boolean.valueOf(destractive)) {
          if (destructiveMethodFileName != null && !destructiveMethodFileName.isEmpty()) {
            loan.setDestructiveFile(destructiveMethodFileName);
          }
          loan.setDestructiveMethod(destructiveMethod);
        }
      }
      loan.setLoanDescription(descriptionOfLoan);
      if (scPurposeFileName != null && !scPurposeFileName.isEmpty()) {
        loan.setLoanDescriptionFile(scPurposeFileName);
      }
      loan.setSamples(samples);
      loan.setSampleSetAdditionalInfo(additionInformation);

      if (citesNumber == null || citesNumber.isEmpty()) {
        loan.setCitesNumber("-");
      } else {
        loan.setCitesNumber(citesNumber);
      }
    } else {
      loan.setLoanDescription(page4Description);

      if (edPurposeFileName != null && !edPurposeFileName.isEmpty()) {
        loan.setEdPurposeFile(edPurposeFileName);
      }

      loan.setFromDate(Util.dateToString(fromDate));
      loan.setToDate(Util.dateToString(toDate));
      if (numOfPages == 6) {
        loan.setType(page3requestType);
      } else if (numOfPages == 7) {
        loan.setEduPurpose(educationPurpose);
        loan.setExhPorpuseDesc(exhPorpuseDesc);
        loan.setType("Physical");
      }
    }

    if (hasPrimaryContact) {
      loan.setBorrower(contact2.getFirstname() + " " + contact2.getLastname());
      loan.setPrimaryUser(contact2);
      loan.setSecondaryUser(contact1);
    } else {
      loan.setBorrower(contact1.getFirstname() + " " + contact1.getLastname());
      loan.setPrimaryUser(contact1);
      loan.setSecondaryUser(null);
    }
  }

  public void gotoConfirmationPage() throws IOException { 
    logger.info("gotoConfirmationPage");

    isSuccess = false;

    isPolicyLinkEnabled = false;
    isDestructivePolicyLinkEnabled = false;
//        fileNameList = new ArrayList<>();

    String loanId = LOAN_REQUEST_PREFIX + String.format("%06d", mongo.getNextSequence());
    buildLoan(loanId);
    String path;
    try {
      path = fileHander.transferFiles(uuid);
      pdf.createPDF(loan, isSwedish, path, numOfPages, server);
      mongo.save(loan);
      sendMails(loanId);

      breadCrumb.resetNavigationPathToHomePage();
      statisticMap = null;
      uuid = null;
      isSuccess = true; 

      step = 100;
    } catch (DocumentException e) {
      addError("", NameMapping.getMsgByKey(CommonNames.RequestFailed, isSwedish));
    } catch (MessagingException | UnsupportedEncodingException ex) {
      logger.error("sending email failed: {}", ex.getMessage());
      loan.setEmailFailed(true);
      loan.setStatus("Email failed");
      mongo.updateLoan(loan);
      addError("", NameMapping.getMsgByKey(CommonNames.SendingEmailsFailed, isSwedish));
    }
  }

  private void sendMails(String loanId) throws MessagingException, AddressException, UnsupportedEncodingException {

    logger.info("sendMails");

    Map<String, String> emailMap = new HashMap<>();
    emailMap.put("isSwedish", String.valueOf(isSwedish));
    emailMap.put("loanId", loanId);
    emailMap.put("primarytitle", loan.getPrimaryUser().getTitle());
    emailMap.put("primarylastname", loan.getPrimaryUser().getLastname());
    emailMap.put("primaryFirstname", loan.getPrimaryUser().getFirstname());
    emailMap.put("primaryemail", loan.getPrimaryUser().getEmail());

    emailMap.put("collection", loan.getDepartment());
    emailMap.put("purpose", loan.getPurpose());
    emailMap.put("type", loan.getType());
    emailMap.put("subcollection", loan.getReleventCollection());

    if (loan.getPrimaryUser().getDepartment() != null && !loan.getPrimaryUser().getDepartment().isEmpty()) {
      emailMap.put("primarydepartment", loan.getPrimaryUser().getDepartment());
    }
    emailMap.put("primaryinstitution", loan.getPrimaryUser().getInstitution());
    emailMap.put("primarycountry", loan.getPrimaryUser().getAddress().getCountry());

    emailMap.put("hasPrimaryContact", String.valueOf(hasPrimaryContact));
    emailMap.put("isLocal", String.valueOf(server.equals(LOCALHOST)));

    if (hasPrimaryContact) {
      emailMap.put("secondarytitle", loan.getSecondaryUser().getTitle());
      emailMap.put("secondarylastname", loan.getSecondaryUser().getLastname());
      emailMap.put("secondaryfirstname", loan.getSecondaryUser().getFirstname());
      emailMap.put("secondaryemail", loan.getSecondaryUser().getEmail());
      if (loan.getSecondaryUser().getDepartment() != null && !loan.getSecondaryUser().getDepartment().isEmpty()) {
        emailMap.put("secondarydepartment", loan.getSecondaryUser().getDepartment());
      }
      emailMap.put("secondaryinstitution", loan.getPrimaryUser().getInstitution());
      emailMap.put("secondarycountry", loan.getPrimaryUser().getAddress().getCountry());
    }

    emailMap.put("department", Department.getDepartName(department));

    logger.info("uuid : {}", uuid);
    StringBuilder sb = new StringBuilder();
    sb.append(uuid.toString());
    sb.append("-loanrequest_");
    sb.append(loanId);

    StringBuilder adminSb = new StringBuilder(sb.toString());
    adminSb.append("_admin.pdf");

    sb.append(".pdf");
    emailMap.put("summaryFile", sb.toString().trim());
    emailMap.put("adminSummaryFile", adminSb.toString().trim());

//        String curatorEmail = null;
//        if(loan.getCurator() != null && !loan.getCurator().isEmpty()) {
//            curatorEmail = loan.getCurator();
//        } else {
//            Collection collection = (mongo.findCollection("Non scientific", "group")); 
//            if(collection != null) {
//                curatorEmail = collection.getManager();
//            }
//        }
    TblUsers curator = dao.findOneUserByEmail(loan.getCurator());
    if (curator != null) {
      emailMap.put("outofoffice", String.valueOf(curator.getOnvacation()));
    }
    emailMap.put("curratormail", loan.getCurator());
    emailMap.put("manager", loan.getManager());

    if (numOfPages == 9 && selectedCollection != null) {
      emailMap.put("loanPolicy", SCIENTIFIC_PURPOSE_LOAN_POLICY);
    } else {
      emailMap.put("loanPolicy", EDUCATIONAL_PURPOSE_LOAN_POLICY);
    }

    if (server.equals(LOCALHOST)) {
      mail.send(emailMap);
    } else {
      nrmMail.send(emailMap);
    }
  }

  public UUID getUuid() {
    return uuid;
  }

  public int getSecientificTotalCount() {
    return statisticMap.get("sc");
  }

  public int getEducationalTotalCount() {
    return statisticMap.get("ed");
  }

  public int getOtherTotalCount() {
    return statisticMap.get("other");
  }

  public int getSecientificYearCount() {
    return statisticMap.get("sc_year");
  }

  public int getEducationalYearCount() {
    return statisticMap.get("ed_year");
  }

  public int getOtherYearCount() {
    return statisticMap.get("other_year");
  }

  public int getCURRENT_YEAR() {
    return CURRENT_YEAR;
  }

  /**
   * Group 1 collection selected.
   */
  public void collectionChanged() {
    logger.info("collectionChanged : {}", selectedCollection);

    isCollectionSelect = true;
  }

  public void departmentChanged() {
    logger.info("departmentChanged : {}", department);

    isImplemented = Department.Zoology.isZoology(department);

    if (isImplemented) {
      resetData(false);
      purpose = LoanPurpose.ScientificPurpose.getText();
    } else {
      if (department.equals(Department.Botany.getText())) {
        section = 110;
      } else if (department.equals(Department.EnvionmentSpecimenBank.getText())) {
        section = 111;
      } else if (department.equals(Department.Geology.getText())) {
        section = 112;
      } else {
        section = 113;
      }
    }

    breadCrumb.setNotImplementDepartmentPath(isImplemented);
  }

  /**
   * Sample type (Physical or photo) changed
   */
  public void sampleTypeChanged() {
    logger.info("sampleTypeChanged : {}", requestType);

    isPhoto = false;
    isAgree = false;
    isPolicyRead = false;
    switch (requestType) {
      case "Photo":
        isPhoto = true;
        isAgree = true;
        hasPrimaryContact = false;
        breadCrumb.setPhotoElement();
        break;
      case "Information":
        breadCrumb.setInformationElement();
        isAgree = true;
        isPolicyRead = true;
        hasPrimaryContact = false;
        break;
      default:
        breadCrumb.setPhiscalElement();
        break;
    }
  }

  public void purposeCheckChanged() {
    logger.info("purposeCheckChanged");

    isPurposeChecked = true;
  }

  public void selectpurpose() {
    logger.info("selectpurpose : {}", purpose);

    resetData(false);
    switch (purpose) {
      case "commercialartother":
        breadCrumb.resetNavigationPath(LoanPurpose.CommercialArtOther);
        numOfPages = 6;
        break;
      case "educationexhibition":
        breadCrumb.resetNavigationPath(LoanPurpose.EducationExhibition);
        numOfPages = 7;
        break;
      default:
        breadCrumb.resetNavigationPath(LoanPurpose.ScientificPurpose);
        numOfPages = 9;
        break;
    }
  }

  public void remove(Sample sample) {
    logger.info("remove");

    samples.remove(sample);
  }

  public void onIsAgreeStatusChange() {
    logger.info("onIsAgreeStatusChange : {}", isAgree);
  }

  public void onIsPolicyReadStatusChange() {
    logger.info("onIsPolicyReadStatusChange : {}", isPolicyRead);
  }

  public void selectDestractive() {
    logger.info("selectDestractive : {} - {}", isDestractiveSelected, destructiveMethod);

//        isDestractiveSelected = destractive != null && !destractive.isEmpty(); 
    isDestractiveSelected = destractive.equals("false");
    isDestractive = destractive.equals("true");
    isDestractiveLoan = destractive.equals("true");

    logger.info("isDestractive : {} -- {}", isDestractive, destractive);
  }

  public void addPrimaryContact() {
    logger.info("addPrimaryContact : {}", hasPrimaryContact);
    hasPrimaryContact = true;
  }

  public void removePrimaryContact() {
    hasPrimaryContact = false;
  }

  public void primaryContactTitleChanged() {

    logger.info("primaryContactTitleChanged : {} -- {}", numOfPages, contact1.getTitle());

    if (RequestType.Physical.isPhysical(requestType) && numOfPages == 9) {
      if (contact1.getTitle().contains("student")) {
        hasPrimaryContact = true;
        isPhd = true;
      } else {
        isPhd = false;
      }
    }
  }

  public void cleartaxafields() {
    logger.info("cleartaxafields");

    sample = new Sample();
    sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
    RequestContext.getCurrentInstance().reset("onlineform:taxarequestpanel");
  }

  public void removeSample(String catalognumber) {
    logger.info("removeSample : {}", catalognumber);

    selectedCatalogNumbers.remove(catalognumber);
  }

  public void addTaxa() {
    logger.info("addTaxa : {} - {}", sample, isPhoto);

    boolean isDuplicated = false;
    String catNum = sample.getCatalogNumber();
    if (catNum != null && !catNum.isEmpty()) {
      if (selectedCatalogNumbers.contains(catNum)) {
        addWarning("", catNum + " " + NameMapping.getMsgByKey(CommonNames.DuplicatValue, isSwedish));
        isDuplicated = true;
      } else {
        selectedCatalogNumbers.add(catNum);
      }
    }
    if (!isDuplicated) {
      samples.add(sample);
      cleartaxafields();
    }
    languages.setOpenGbif(false);
  }

  public void searchCatalogNumberFromDina() {
    logger.info("searchCatalogNumberFromDina : {}", sample.getCatalogNumber());

    if (sample.getCatalogNumber().isEmpty()) {
      addError("", NameMapping.getMsgByKey(CommonNames.MissingCatNum, isSwedish));
    } else {
      SolrRecord record = solr.searchByCatalogNumber(sample.getCatalogNumber());
      if (record != null) {
        String preparation = record.getPreparationString();
        if (preparation == null || preparation.isEmpty()) {
          preparation = NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish);
        }

        sample = new Sample(record.getCatalogNum(), record.getFamily(), record.getGenus(), record.getFullname(),
                record.getLocality(), record.getCountry(), record.getCollectedYear(), record.getAuth(),
                record.getCollectors(), record.getType(), preparation, record.getStorageString(), "");
      } else {
        addInfo("", NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
      }
    }
  }

  public void searchFamilyFromDina() {
    logger.info("searchFamilyFromDina : {}", sample.getFamily());

    records = new ArrayList<>();
    if (!sample.getFamily().isEmpty()) {
      records = solr.searchByTaxa(sample, "fm", String.join(" ", selectedCatalogNumbers));
      if (records.isEmpty()) {
        addInfo("", NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
      } else {
        buildSolrResult();
      }
    } else {
      addError("", NameMapping.getMsgByKey(CommonNames.MissingFamily, isSwedish));
    }
  }

  public void searchGenusFromDina() {
    logger.info("searchGenusFromDina : {}", sample.getGenus());

    records = new ArrayList<>();
    if (!sample.getGenus().isEmpty()) {
      records = solr.searchByTaxa(sample, "gn", String.join(" ", selectedCatalogNumbers));
      if (records.isEmpty()) {
        addInfo("", NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
      } else {
        buildSolrResult();
      }
    } else {
      addError("", NameMapping.getMsgByKey(CommonNames.MissingGenus, isSwedish));
    }
  }

  public void searchSpeciesFromDina() {
    logger.info("searchSpeciesFromDina : {}", sample.getSpecies());

    records = new ArrayList<>();
    if (!sample.getSpecies().isEmpty()) {
      records = solr.searchByTaxa(sample, "sp", String.join(" ", selectedCatalogNumbers));
      if (records.isEmpty()) {
        addInfo("", NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
      } else {
        buildSolrResult();
      }
    } else {
      addError("", NameMapping.getMsgByKey(CommonNames.MissingSpecies, isSwedish));
    }
  }

  private void buildSolrResult() {
    if (records.size() > 1) {
      requestContext = RequestContext.getCurrentInstance();
      requestContext.execute("PF('speciesDlg').show()");
    } else {
      if (records.size() == 1) {
        SolrRecord record = records.get(0);
        String preparation = record.getPreparationString();
        if (preparation == null || preparation.isEmpty()) {
          preparation = NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish);
        }
        sample = new Sample(record.getCatalogNum(), record.getFamily(), record.getGenus(), record.getFullname(),
                record.getLocality(), record.getCountry(), record.getCollectedYear(), record.getAuth(),
                record.getCollectors(), record.getType(), preparation, record.getStorageString(), "");
      }
    }
  }

  public void onCITESChanged() {
    logger.info("onCITESChanged : {}", noCITE);

    if (noCITE) {
      citesNumber = CommonString.getNoCITESText(isSwedish);
      disableCITE = true;
    } else {
      citesNumber = null;
      disableCITE = false;
    }
  }

  public void searchFromGbif(String taxa) {
    logger.info("searchFromGbif : {}", taxa);

    gbifUrl = GBIF_BASE_URL + taxa;

    languages.setOpenGbif(true);
  }

  public void onRowSelect(SelectEvent event) {
    logger.info("onRowSelect");

    selectedRecord = (SolrRecord) event.getObject();

    String preparation = selectedRecord.getPreparationString();
    if (preparation == null || preparation.isEmpty()) {
      preparation = NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish);
    }
    sample = new Sample(selectedRecord.getCatalogNum(), selectedRecord.getFamily(), selectedRecord.getGenus(), selectedRecord.getFullname(),
            selectedRecord.getLocality(), selectedRecord.getCountry(), selectedRecord.getCollectedYear(), selectedRecord.getAuth(),
            selectedRecord.getCollectors(), selectedRecord.getType(), preparation, selectedRecord.getStorageString(), "");

    requestContext = RequestContext.getCurrentInstance();
    requestContext.execute("PF('speciesDlg').hide()");
  }

  private boolean validateSampleList() {
    if (requestType.equals(RequestType.Physical.getText())) {
      return samples.stream().noneMatch((s) -> (s.getType().isEmpty()));
    }
    return true;
  }

  public void navigateIndexHome() {
    step = 0;
    section = 1;
    resetData(true);
    currentpage = "";
  }

  public void navigateIndexOnlineForm() {
    step = 1;
    section = 500;
    breadCrumb.setManuItem(breadCrumb.getOnlineFormItem());
    currentpage = "";
  }

  public void navigateIndexPage1() {
    step = 2;

    if (department.equals(Department.Zoology.getText())) {
      section = 11;
    } else if (department.equals(Department.Botany.getText())) {
      section = 110;
    } else if (department.equals(Department.EnvionmentSpecimenBank.getText())) {
      section = 111;
    } else if (department.equals(Department.Geology.getText())) {
      section = 112;
    } else {
      section = 113;
    }

    breadCrumb.setManuItem(breadCrumb.getPage1Item());
    currentpage = "";
  }

  public void navigateIndexPage2() {
    step = 3;
    section = 500;
    breadCrumb.setManuItem(breadCrumb.getPage2Item());
    currentpage = "";
  }

  public void navigateIndexPage3() {
    step = 4;
    section = 500;
    breadCrumb.setManuItem(breadCrumb.getPage3Item());

    if (numOfPages == 9) {
      currentpage = "page31panel";
    } else if (numOfPages == 7) {
      currentpage = "page32panel";
    } else {
      currentpage = "page33panel";
    }
  }

  public void navigateIndexPage4() {
    step = 5;
    section = 500;
    breadCrumb.setManuItem(breadCrumb.getPage4Item());
    if (!LoanPurpose.ScientificPurpose.isScientificPurpose(purpose)) {
      currentpage = "page42panel";
    } else {
      currentpage = "";
    }
  }

  public void navigateIndexPage5() {

    section = 500;
    if (LoanPurpose.CommercialArtOther.isCommercial(purpose)) {
      step = 9;
      currentpage = "";
    } else {
      step = 6;
      currentpage = "page51panel";
    }
    breadCrumb.setManuItem(breadCrumb.getPage5Item());
  }

  public void navigateIndexPage6() {

    section = 500;
    if (LoanPurpose.ScientificPurpose.isScientificPurpose(purpose)) {
      step = 7;
      if (isPhoto) {
        currentpage = "page6photoInst";
      } else {
        isDestructivePolicyLinkEnabled = true;
        currentpage = "page6Desc";
      }
    } else if (LoanPurpose.EducationExhibition.isEducation(purpose)) {
      step = 9;
      currentpage = "";
    } else {
      step = 10;
      currentpage = "summaryPanel";
    }
    breadCrumb.setManuItem(breadCrumb.getPage6Item());
  }

  public void navigateIndexPage7() {
    section = 500;
    if (LoanPurpose.ScientificPurpose.isScientificPurpose(purpose)) {
      step = 8;
      currentpage = "citesnum";
    } else if (LoanPurpose.EducationExhibition.isEducation(purpose)) {
      step = 10;
      currentpage = "summaryPanel";
    }
    breadCrumb.setManuItem(breadCrumb.getPage7Item());
  }

  public void navigateIndexPage8() {
    section = 500;
    step = 9;
    breadCrumb.setManuItem(breadCrumb.getPage8Item());
    currentpage = "";
  }

  public void navigateIndexPage9() {
    step = 10;
    section = 500;
    breadCrumb.setManuItem(breadCrumb.getPage9Item());
    currentpage = "summaryPanel";
  }

  public void openPolicyPdf() {
    logger.info("openPolicyPdf");

  }

  public void reviewrequest() {
    logger.info("reviewrequest");
    openSummary = true;
  }

  public void closesummary() {
    logger.info("colsesummary");
    openSummary = false;
  }

  public void saveText() {
    logger.info("saveText");
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

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public void handleDesctractiveMethod() {
    logger.info("handleProjectDescription : {} -- {}", 
            destructiveMethod, destructiveMethodFileName);

    setIsDestractiveMethodSet();
  }

  private void setIsDestractiveMethodSet() {
    isDestractiveSelected = false;
    if (destractive != null && !destractive.isEmpty()) {
      if (destractive.equals("false")) {
        isDestractiveSelected = true;
      } else {
        if (destructiveMethodFileName != null) {
          isDestractiveSelected = true;
        } else if (destructiveMethod != null && !destructiveMethod.isEmpty()) {
          if (!destructiveMethod.trim().equals("-")) {
            isDestractiveSelected = true;
          }
        }
      }
    }
  }

  public void handleProjectDescription() {
    logger.info("handleProjectDescription : {} -- {}", descriptionOfLoan, scPurposeFileName);
    logger.info("descriptionOfLoan: {} -- {}", descriptionOfLoan, descriptionOfLoan == null);
    setIsProjectDescriptionSet();
  }

  private void setIsProjectDescriptionSet() {
    isProjectDescriptionSet = false;
    if (scPurposeFileName != null) {
      isProjectDescriptionSet = true;
    } else {
      isProjectDescriptionSet = descriptionOfLoan == null 
              ? false : descriptionOfLoan.trim().isEmpty() 
              ? false : !descriptionOfLoan.trim().equals("-");
    }
    
   
//    else if (descriptionOfLoan != null && !descriptionOfLoan.isEmpty()) {
//      if (!descriptionOfLoan.trim().equals("-")) {
//        isProjectDescriptionSet = true;
//      }
//    }
  }

  /**
   * Upload file
   *
   * @param event
   */
  public void handleFileUpload(FileUploadEvent event) {
    logger.info("handleFileUpload : {} ", event.getFile().getFileName());
    try {
      scPurposeFileName = event.getFile().getFileName();
      fileHander.saveTempFile(event.getFile(), uuid);
    } catch (IOException ex) {
      logger.warn(ex.getMessage());
      addError("", NameMapping.getMsgByKey(CommonNames.UploadFileFailed, isSwedish));
    }
    setIsProjectDescriptionSet();
  }

  /**
   * Upload file
   *
   * @param event
   */
  public void handleEDFileUpload(FileUploadEvent event) {
    logger.info("handleEDFileUpload : {} ", event.getFile().getFileName());
    try {
      edPurposeFileName = event.getFile().getFileName();
      fileHander.saveTempFile(event.getFile(), uuid);
    } catch (IOException ex) {
      logger.warn(ex.getMessage());
      addError("", NameMapping.getMsgByKey(CommonNames.UploadFileFailed, isSwedish));
    }
  }

  /**
   * Upload file
   *
   * @param event
   */
  public void handleDestructiveFileUpload(FileUploadEvent event) {
    logger.info("handleDestructiveFileUpload : {}", event.getFile().getFileName());
    try {
      destructiveMethodFileName = event.getFile().getFileName();
      fileHander.saveTempFile(event.getFile(), uuid);
      setIsDestractiveMethodSet();
    } catch (IOException ex) {
      logger.warn(ex.getMessage());
      addError("", NameMapping.getMsgByKey(CommonNames.UploadFileFailed, isSwedish));
    }
  }

  /**
   * Upload file
   *
   * @param event
   */
  public void handlePhotoInstFileUpload(FileUploadEvent event) {
    logger.info("handlePhotoInstFileUpload : {} -- {}", event.getFile(), event.getFile().getFileName());
    try {
      photoInstructionFileName = event.getFile().getFileName();
      fileHander.saveTempFile(event.getFile(), uuid);
    } catch (IOException ex) {
      logger.warn(ex.getMessage());
      addError("", NameMapping.getMsgByKey(CommonNames.UploadFileFailed, isSwedish));
    }
  }

  public void removefile(String filename) {

    logger.info("removefile : {}", filename);

    if (filename.equals(scPurposeFileName)) {
      scPurposeFileName = null;
      setIsProjectDescriptionSet();
    } else if (filename.equals(edPurposeFileName)) {
      edPurposeFileName = null;
    } else if (filename.equals(destructiveMethodFileName)) {
      destructiveMethodFileName = null;
      setIsDestractiveMethodSet();
    } else if (filename.equals(photoInstructionFileName)) {
      photoInstructionFileName = null;
    }
    fileHander.removeFileFromTempDirectory(filename, uuid);
  }

  public String getPurposeName() {

    logger.info("getPurposeName : {}", purpose);
    switch (purpose) {
      case "scientificpurpose":
        return "Scientific purpose";
      case "educationexhibition":
        return "Education/Exhibition";
      case "commercialartother":
        return "Commercial/art/other";
    }
    return "Scientific purpose";
  }

  public String getSamplePanelColor(int step) {
    logger.info("samplePanelColor : {}", step);

    if (step == 0) {
      return "samplespanelodd";
    }
    return step % 2 == 0 ? "samplespaneleven" : "samplespanelodd";
  }

  public void handleFromDateSelect() {
    logger.info("handleFromDateSelect");

    toMinDate = fromDate;
  }

  public boolean isIsHolidaySession() {
    return Util.isHoliday();
  }

  public String getDestructiveSamplingPolicy() {
    return DESTRUCTIVE_SAMPLING_POLICY;
  }

  public String getScientificPurposeLoanPolicy() {
    return SCIENTIFIC_PURPOSE_LOAN_POLICY;
  }

  public String getEducationalPurposeLoanPolicy() {
    return EDUCATIONAL_PURPOSE_LOAN_POLICY;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public int getSection() {
    return section;
  }

  public String getDescriptionOfLoan() {
    return descriptionOfLoan.isEmpty() ? " - " : descriptionOfLoan;
  }

  public void setDescriptionOfLoan(String descriptionOfLoan) {
    this.descriptionOfLoan = descriptionOfLoan;
  }

  public String getAdditionInformation() {
    return additionInformation;
  }

  public void setAdditionInformation(String additionInformation) {
    this.additionInformation = additionInformation;
  }

  public String getDestructiveMethod() {
    return destructiveMethod;
  }

  public void setDestructiveMethod(String destructiveMethod) {
    this.destructiveMethod = destructiveMethod;
  }

  public String getScPurposeFileName() {
    return scPurposeFileName;
  }

  public void setScPurposeFileName(String scPurposeFileName) {
    this.scPurposeFileName = scPurposeFileName;
  }

  public String getEdPurposeFileName() {
    return edPurposeFileName;
  }

  public void setEdPurposeFileName(String edPurposeFileName) {
    this.edPurposeFileName = edPurposeFileName;
  }

  public String getDestructiveMethodFileName() {
    return destructiveMethodFileName;
  }

  public void setDestructiveMethodFileName(String destructiveMethodFileName) {
    this.destructiveMethodFileName = destructiveMethodFileName;
  }

  public String getPhotoInstructionFileName() {
    return photoInstructionFileName;
  }

  public void setPhotoInstructionFileName(String photoInstructionFileName) {
    this.photoInstructionFileName = photoInstructionFileName;
  }

  public boolean isNoCITE() {
    return noCITE;
  }

  public void setNoCITE(boolean noCITE) {
    this.noCITE = noCITE;
  }

  public String getLoanRequestPeriod() {
    return CommonString.getLoanRequestPeriod(isSwedish, Util.isHoliday());
  }

  public boolean isIsProjectDescriptionSet() {
    return isProjectDescriptionSet;
  }

  public void setIsProjectDescriptionSet(boolean isProjectDescriptionSet) {
    this.isProjectDescriptionSet = isProjectDescriptionSet;
  }

  public boolean isIsPhoto() {
    return isPhoto;
  }

  public void setIsPhoto(boolean isPhoto) {
    this.isPhoto = isPhoto;
  }

  public String getPhotoInstruction() {
    return photoInstruction;
  }

  public void setPhotoInstruction(String photoInstruction) {
    this.photoInstruction = photoInstruction;
  }

  public String getCitesNumber() {
    return citesNumber;
  }

  public void setCitesNumber(String citesNumber) {
    this.citesNumber = citesNumber;
  }

  public User getContact1() {
    return contact1;
  }

  public void setContact1(User contact1) {
    this.contact1 = contact1;
  }

  public User getContact2() {
    return contact2;
  }

  public void setContact2(User contact2) {
    this.contact2 = contact2;
  }

  public boolean isIsCollectionSelect() {
    return isCollectionSelect;
  }

  public void setIsCollectionSelect(boolean isCollectionSelect) {
    this.isCollectionSelect = isCollectionSelect;
  }

  public String getDestractive() {
    return destractive;
  }

  public void setDestractive(String destractive) {
    this.destractive = destractive;
  }

  public String getEducationPurpose() {
    return educationPurpose;
  }

  public void setEducationPurpose(String educationPurpose) {
    this.educationPurpose = educationPurpose;
  }

  public String getSelectedCollection() {
    return selectedCollection;
  }

  public void setSelectedCollection(String selectedCollection) {
    this.selectedCollection = selectedCollection;
  }

  public String getScPolicy() {
    return SCIENTIFIC_PURPOSE_LOAN_POLICY;
  }

  public String getEdPolicy() {
    return EDUCATIONAL_PURPOSE_LOAN_POLICY;
  }

  public boolean isIsPolicyRead() {
    return isPolicyRead;
  }

  public void setIsPolicyRead(boolean isPolicyRead) {
    this.isPolicyRead = isPolicyRead;
  }

  public boolean isIsDestractiveSelected() {
    return isDestractiveSelected;
  }

  public void setIsDestractiveSelected(boolean isDestractiveSelected) {
    this.isDestractiveSelected = isDestractiveSelected;
  }

  public boolean isOpenSummary() {
    return openSummary;
  }

  public void setOpenSummary(boolean openSummary) {
    this.openSummary = openSummary;
  }

  public boolean isHasPrimaryContact() {
    return hasPrimaryContact;
  }

  public void setHasPrimaryContact(boolean hasPrimaryContact) {
    this.hasPrimaryContact = hasPrimaryContact;
  }

  public List<Sample> getSamples() {
    return samples;
  }

  public Sample getSample() {
    return sample;
  }

  public void setSample(Sample sample) {
    this.sample = sample;
  }

  public List<SolrRecord> getRecords() {
    return records;
  }

  public void setRecords(List<SolrRecord> records) {
    this.records = records;
  }

  public SolrRecord getSelectedRecord() {
    return selectedRecord;
  }

  public void setSelectedRecord(SolrRecord selectedRecord) {
    this.selectedRecord = selectedRecord;
  }

  public String getDinaSearch() {
    return dinaSearch;
  }

  public void setDinaSearch(String dinaSearch) {
    this.dinaSearch = dinaSearch;
  }

  public boolean isIsPolicyLinkEnabled() {
    return isPolicyLinkEnabled;
  }

  public void setIsPolicyLinkEnabled(boolean isPolicyLinkEnabled) {
    this.isPolicyLinkEnabled = isPolicyLinkEnabled;
  }

  public boolean isIsDestructivePolicyLinkEnabled() {
    return isDestructivePolicyLinkEnabled;
  }

  public void setIsDestructivePolicyLinkEnabled(boolean isDestructivePolicyLinkEnabled) {
    this.isDestructivePolicyLinkEnabled = isDestructivePolicyLinkEnabled;
  }

  public String getPage3requestType() {
    return page3requestType;
  }

  public void setPage3requestType(String page3requestType) {
    this.page3requestType = page3requestType;
  }

  public String getPage4Description() {
    return page4Description;
  }

  public void setPage4Description(String page4Description) {
    this.page4Description = page4Description;
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

  public boolean isIsDestractive() {
    return isDestractive;
  }

  public void setIsDestractive(boolean isDestractive) {
    this.isDestractive = isDestractive;
  }

  public boolean isIsPhd() {
    return isPhd;
  }

  public void setIsPhd(boolean isPhd) {
    this.isPhd = isPhd;
  }

  public String getExhPorpuseDesc() {
    return exhPorpuseDesc;
  }

  public void setExhPorpuseDesc(String exhPorpuseDesc) {
    this.exhPorpuseDesc = exhPorpuseDesc;
  }

  public boolean isDisableCITE() {
    return disableCITE;
  }

  public void setDisableCITE(boolean disableCITE) {
    this.disableCITE = disableCITE;
  }

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    this.step = step;
  }

  public boolean isIsPurposeChecked() {
    return isPurposeChecked;
  }

  public void setIsPurposeChecked(boolean isPurposeChecked) {
    this.isPurposeChecked = isPurposeChecked;
  }

  public Map<String, List<Collection>> getMap() {
    return map;
  }

  public void setMap(Map<String, List<Collection>> map) {
    this.map = map;
  }

  public String getSelectedCountry() {
    return selectedCountry;
  }

  public void setSelectedCountry(String selectedCountry) {
    this.selectedCountry = selectedCountry;
  }

  public List<String> getCountryList() {
    return CountryNames.getCountryList(isSwedish);
  }

  public boolean isIsAgree() {
    return isAgree;
  }

  public void setIsAgree(boolean isAgree) {
    this.isAgree = isAgree;
  }

  public int getNumOfPages() {
    return numOfPages;
  }

  public void setNumOfPages(int numOfPages) {
    this.numOfPages = numOfPages;
  }

  public String getEducationPurposeName() {
    return NameMapping.getName(educationPurpose, languages.getLocale());
  }

  private String getCurrentpage() {
    return currentpage;
  }

  public void setCurrentpage(String currentpage) {
    this.currentpage = currentpage;
  }

  public String getGbifUrl() {
    return gbifUrl;
  }

  public void setGbifUrl(String gbifUrl) {
    this.gbifUrl = gbifUrl;
  }

  public String getLoanId() {
    return loan.getId();
  }

  public String getHost() {
    return host;
  }

  public boolean isIsImplemented() {
    return isImplemented;
  }

  public boolean isIsDestractiveLoan() {
    return isDestractiveLoan;
  }

  public void setIsDestractiveLoan(boolean isDestractiveLoan) {
    this.isDestractiveLoan = isDestractiveLoan;
  }

  public boolean isIsInformation() {
    return RequestType.Information.isInformation(requestType);
  }

  public boolean isIsPhysical() {
    return RequestType.Physical.isPhysical(requestType);
  }

  public boolean getPrimaryUserTitle() {
    return contact2.getTitle() != null && 
            contact2.getTitle().trim().length() > 0;
  }
  
  public boolean getSecondaryUserTitle() {
    return contact1.getTitle() != null && 
            contact1.getTitle().trim().length() > 0;
  }
    
  private void addError(String errorTitle, String errorMsg) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorTitle, errorMsg));
  }

  private void addInfo(String msgTitle, String msg) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msgTitle, msg));
  }

  private void addWarning(String warningTitle, String warningMsg) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warningTitle, warningMsg));
  }
}
