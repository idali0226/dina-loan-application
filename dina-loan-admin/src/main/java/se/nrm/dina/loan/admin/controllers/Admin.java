package se.nrm.dina.loan.admin.controllers;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.loan.admin.policypdf.FileHandler;
import se.nrm.dina.loan.admin.vo.CollectionManager;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Collection;
import se.nrm.dina.mongodb.loan.vo.Loan;

/**
 *
 * @author idali
 */
@Named(value = "admin")
@SessionScoped
public class Admin implements Serializable {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final String LOCAL_EXTERNAL_FILES = "https://localhost:8443/dina-external-datasource/pdf?";
//    private final String REMOTE_EXTERNAL_FILES_LOAN = "https://dina-loans.nrm.se:8453/dina-external-datasource/pdf?";
  private final String REMOTE_EXTERNAL_FILES_AS = "https://dina-web.net/dina-external-datasource/pdf?";
//    private final String REMOTE_EXTERNAL_FILES_AS = "//www.dina-web.net/dina-external-datasource/pdf?";
//    private final String LOCAL_EXTERNAL_FILES = "https://dina-web.net/dina-external-datasource/pdf?";

  private int section = 0;

  private String newCollectionName;
  private String newCollectionCurator;
  private String newCollectionGroupName;
  private String newCollectionManager;
  private boolean managerDisable = true;

  private String department = "Zoology";

  private String selectedGroupName;

  private String changedGroupName;
  private String changedManager;

  private final String DEFAULT_COLLECTION_GROUP_NAME;
  private String default_manager;

  private final String NON_SCIENTIFIC_LOANS = "Non scientific";
  private String nonScientificLoansManager;

  private int activeTab;

  private Map<String, List<Collection>> map;
  private List<String> collectionGroupNames;
  private List<CollectionManager> cmList;

  private boolean isNewPolicy;

  private final String externalFilePath;
  private final String servername;

  @Inject
  private MongoJDBC mongo;

  @Inject
  private FileHandler fileHandler;

  @Inject
  private UserAccountController user;

  public Admin() {
    servername = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
    if (servername.contains("localhost")) {
      externalFilePath = LOCAL_EXTERNAL_FILES;
    } else {
      externalFilePath = REMOTE_EXTERNAL_FILES_AS;
    }
    section = 0;

    DEFAULT_COLLECTION_GROUP_NAME = "Insects";
    newCollectionGroupName = DEFAULT_COLLECTION_GROUP_NAME;

    isNewPolicy = false;
  }

  @PostConstruct
  public void init() {
    if (map == null || map.isEmpty()) {
      initialCollectionMap();
    } 
  }
   
  public void contact() {
    logger.info("contact");
  }

  private void initialCollectionMap() {
    map = new TreeMap<>();
    cmList = new ArrayList<>();
    collectionGroupNames = new ArrayList<>();
    try {
      map = mongo.findCollectionsByDepartment(department);

      map.entrySet().stream().forEach((entry) -> {
        if (entry.getKey().equals(NON_SCIENTIFIC_LOANS)) {
          nonScientificLoansManager = entry.getValue().get(0).getManager();
        } else {
          collectionGroupNames.add(entry.getKey());
          cmList.add(new CollectionManager(entry.getKey(), entry.getKey(), entry.getValue().get(0).getManager()));
          if (entry.getKey().equals(DEFAULT_COLLECTION_GROUP_NAME)) {
            default_manager = entry.getValue().get(0).getManager();
            newCollectionManager = default_manager;
          }
        }
      });
      map.remove(NON_SCIENTIFIC_LOANS);
    } catch (Exception e) {
      addError("", "Can not connect to datasource.  Please contact administrator.");
    }
  }

  public void manageAccount() {
    logger.info("managerAccount");
    section = 10;
  }

  public void manageCollection() {
    logger.info("managerCollection");
    section = 20;
  }

  public void manageLoans() {
    logger.info("manageLoans");
    section = 30;
  }

  public void managePolicyDocuments() {
    logger.info("manageLoans");
    section = 40;
  }

  public void managePortalPopup() {
    logger.info("managePortalPopup");
    section = 60;
  }

  public void changePassword() {
    logger.info("changePassword ");
    user.setIsChangePasswordSuccessed(false);
    section = 50;
  }

  public void departmentChanged() {
    logger.info("departmentChanged : {}", department);

    initialCollectionMap();
    if (collectionGroupNames.isEmpty()) {
      collectionGroupNames.add("------------ SELECT ------------");
    }
  }

  public void removeSubCollection(Collection c) {
    logger.info("removeSubCollection : {}", c);

    mongo.removeCollection(c);
//        map = mongo.findAllCollection();  
    initialCollectionMap();
  }

  public void addNewCollection() {
    logger.info("addNewCollection : {}", newCollectionGroupName);

    if (!collectionGroupNames.contains(newCollectionGroupName)) {
      collectionGroupNames.add(newCollectionGroupName);
      newCollectionManager = null;
      managerDisable = false;
    } else {
      if (map.get(newCollectionGroupName) != null && !map.get(newCollectionGroupName).isEmpty()) {
        newCollectionManager = map.get(newCollectionGroupName).get(0).getManager();
        managerDisable = true;
      } else {
        newCollectionManager = null;
        managerDisable = false;
      }
    }
  }

  private boolean isCollectionExist(List<Collection> selectedGroup, String name) {
    return selectedGroup.stream().anyMatch((c) -> (c.getName().toLowerCase().trim().equals(name.toLowerCase().trim())));
  }

  public void saveNewCollection() {
    logger.info("saveNewCollection : {}", newCollectionGroupName);

    List<Collection> selectedGroup = map.get(newCollectionGroupName);

    boolean nameValid = true;
    if (selectedGroup != null && !selectedGroup.isEmpty()) {
      if (isCollectionExist(selectedGroup, newCollectionName)) {
        addDuplicatCollectionWarning(newCollectionName, newCollectionGroupName);
        nameValid = false;
      }
    }
    if (nameValid) {
      Collection collection = new Collection(newCollectionName, newCollectionCurator, newCollectionGroupName, newCollectionManager, department);
      mongo.saveCollection(collection);
      initialCollectionMap();

      newCollectionName = null;
      newCollectionCurator = null;
      newCollectionGroupName = DEFAULT_COLLECTION_GROUP_NAME;
      newCollectionManager = default_manager;

      managerDisable = true;
    }
  }

  private void addDuplicatCollectionWarning(String collectionName, String collectionGroup) {
    StringBuilder sb = new StringBuilder();
    sb.append("Collection name [");
    sb.append(collectionName);
    sb.append("] is already exist in ");
    sb.append(collectionGroup);
    sb.append(".");

    addWarning("Duplicat Collection", sb.toString());
  }

  public String managermail(String key) {

    logger.info("managermail : {}", key);
    return map.get(key).get(0).getManager();
  }

  private boolean isDuplicatName(Collection collection) {
    List<Collection> collectionList = map.get(collection.getGroup());
    return collectionList.stream().filter((c) -> (!c.getId().equals(collection.getId()))).anyMatch((c) -> (c.getName().equals(collection.getName())));
  }

  public void onRowEdit(RowEditEvent event) {
    logger.info("onRowEdit : {}", ((Collection) event.getObject()).getName());

    Collection editCollection = (Collection) event.getObject();
    if (!isDuplicatName(editCollection)) {
      mongo.updateCollection(editCollection);
    } else {
      addDuplicatCollectionWarning(editCollection.getName(), editCollection.getGroup());
    }
    map = mongo.findCollectionsByDepartment(department);
  }

  public void onRowCancel(RowEditEvent event) {
    logger.info("onRowCancel : {}", ((Collection) event.getObject()).getName());
  }

  public void groupNameChanged() {
    logger.info("groupNameChanged : {}", selectedGroupName);
  }

  public void home() {
    logger.info("home clicked....");
    section = 0;
  }

  public void uploadScFile(FileUploadEvent event) {
    logger.info("uploadScFile : {} -- {}", event.getFile(), event.getFile().getFileName());
    fileHandler.saveFile(event.getFile(), true);

    isNewPolicy = true;
  }

  public void uploadEdFile(FileUploadEvent event) {

    logger.info("uploadEdFile : {} -- {}", event.getFile(), event.getFile().getFileName());
    fileHandler.saveFile(event.getFile(), false);

    isNewPolicy = true;
  }

  public void updatePolicy() {
    logger.info("updatePolicy");

    isNewPolicy = false;
  }

  public String pdf(Loan loan) {
    logger.info("pdf : {}", externalFilePath);

    StringBuilder sb = new StringBuilder();
    sb.append(externalFilePath);
    sb.append("id=");
    sb.append(loan.getUuid().replace("-", "/"));
    sb.append("/loanrequest_");
    sb.append(loan.getId());
    sb.append("_admin.pdf");

    logger.info("link : {}", sb.toString());
    return sb.toString();
  }

  public void updateCollection(CollectionManager cm) {
    logger.info("updateCollection : {} -- {}", cm, department);

    if (cm.getNewGroupName() != null && !cm.getNewGroupName().isEmpty() && cm.getManager() != null && !cm.getManager().isEmpty()) {
      mongo.updateCollectionGroupName(cm.getGroup(), cm.getNewGroupName(), cm.getManager(), department);
    } else {
      addError("Missing value", "Collection name and manager email can not be empty.");
    }
    initialCollectionMap();
  }

  public void removeCollection(CollectionManager cm) {
    logger.info("removeCollection : {} -- {}", cm, department);
    mongo.removeWholeCollection(cm.getGroup());
    initialCollectionMap();
  }

  public void onCellEdit(CellEditEvent event) {
    logger.info("onCellEdit : {} -- {}", event.getOldValue(), event.getNewValue());
  }

  public void onStatusChanged(Loan loan) {
    logger.info("onStatusChanged : {}", loan.getStatus());
  }

  public void onTabChange(TabChangeEvent event) {
    String activeIndex = ((AccordionPanel) event.getComponent()).getActiveIndex();

    activeTab = Integer.parseInt(activeIndex);
  }

  public void updateNonScientificManager() {
    logger.info("updateNonScientificManager : {}", nonScientificLoansManager);
    mongo.updateNonScientificLoansManager(nonScientificLoansManager);
  }

  public void cancel() {
    logger.info("cancel");
  }

  public int getSection() {
    return section;
  }

  public void setSection(int section) {
    this.section = section;
  }

  public String getNewCollectionGroupName() {
    return newCollectionGroupName;
  }

  public void setNewCollectionGroupName(String newCollectionGroupName) {
    this.newCollectionGroupName = newCollectionGroupName;
  }

  public String getNewCollectionName() {
    return newCollectionName;
  }

  public void setNewCollectionName(String newCollectionName) {
    this.newCollectionName = newCollectionName;
  }

  public String getNewCollectionCurator() {
    return newCollectionCurator;
  }

  public void setNewCollectionCurator(String newCollectionCurator) {
    this.newCollectionCurator = newCollectionCurator;
  }

  public String getNewCollectionManager() {
    return newCollectionManager;
  }

  public void setNewCollectionManager(String newCollectionManager) {
    this.newCollectionManager = newCollectionManager;
  }

  public List<String> getCollectionGroupNames() {
    return collectionGroupNames;
  }

  public void setCollectionGroupNames(List<String> collectionGroupNames) {
    this.collectionGroupNames = collectionGroupNames;
  }

  public boolean isIsNewPolicy() {
    return isNewPolicy;
  }

  public void setIsNewPolicy(boolean isNewPolicy) {
    this.isNewPolicy = isNewPolicy;
  }

  public int getActiveTab() {
    return activeTab;
  }

  public void setActiveTab(int activeTab) {
    this.activeTab = activeTab;
  }

  public String getSelectedGroupName() {
    return selectedGroupName;
  }

  public void setSelectedGroupName(String selectedGroupName) {
    this.selectedGroupName = selectedGroupName;
  }

  public String getChangedGroupName() {
    return changedGroupName;
  }

  public void setChangedGroupName(String changedGroupName) {
    this.changedGroupName = changedGroupName;
  }

  public String getChangedManager() {
    return changedManager;
  }

  public void setChangedManager(String changedManager) {
    this.changedManager = changedManager;
  }

  public Map<String, List<Collection>> getMap() {
    return map;
  }

  public void setMap(Map<String, List<Collection>> map) {
    this.map = map;
  }

  public List<CollectionManager> getCmList() {
    return cmList;
  }

  public void setCmList(List<CollectionManager> cmList) {
    this.cmList = cmList;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public boolean isManagerDisable() {
    return managerDisable;
  }

  public void setManagerDisable(boolean managerDisable) {
    this.managerDisable = managerDisable;
  }

  public String getScPolicy() {
    return externalFilePath + "pdf=Loanpolicyforscientificpurposes.pdf";
  }

  public String getEdPolicy() {
    return externalFilePath + "pdf=Loanpolicycommon.pdf";
  }

  public String getNonScientificLoansManager() {
    return nonScientificLoansManager;
  }

  public void setNonScientificLoansManager(String nonScientificLoansManager) {
    this.nonScientificLoansManager = nonScientificLoansManager;
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
