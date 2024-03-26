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
import lombok.extern.slf4j.Slf4j;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;
import se.nrm.dina.loan.admin.config.InitialProperties;
import se.nrm.dina.loan.admin.policypdf.FileHandler;
import se.nrm.dina.loan.admin.vo.CollectionManager;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Collection;
import se.nrm.dina.mongodb.loan.vo.Loan;

/**
 *
 * @author idali
 */
@Named(value = "loanAdmin")
@SessionScoped
@Slf4j
public class LoanAdmin implements Serializable { 

//    private int section = 0;
    private final String emptyString = "";
    private final String mongoConnectionError = "Can not connect to datasource.  Please contact administrator.";
    private final String missingValueErrorMsg = "Missing value";
    private final String collectionNameMissingError = "Collection name and manager email can not be empty.";

    private final String departmentSelectItem = "------------ SELECT ------------";
    
    private String newCollectionName;
    private String newCollectionCurator;
    private String newCollectionGroupName;
    private String newCollectionManager;
    private boolean managerDisable = true;

    private final String defaultDepartmemt = "Zoology";
    
    private String department;

    private String selectedGroupName;
    private String changedGroupName;
    private String changedManager;

    private final String defaultCollectionGroupName = "Insects";
    private String defaultManager;

    private final String nonScientificLoans = "Non scientific";
    private String nonScientificLoansManager;

    private int activeTab;

    private Map<String, List<Collection>> map;
    private List<String> collectionGroupNames;
    private List<CollectionManager> collectionManagerList;
    
    private String contactEmail;

    private boolean isNewPolicy;

    @Inject
    private MongoJDBC mongo;

    @Inject
    private FileHandler fileHandler;
    
    @Inject
    private InitialProperties properties;

    @Inject
    private UserAccountController user;

    @Inject
    private Navigater navigate;

    public LoanAdmin() {

//        newCollectionGroupName = defaultCollectionName;
//        isNewPolicy = false;
    }

    @PostConstruct
    public void init() {
        log.info("init");
        
        department = defaultDepartmemt;
        if (map == null || map.isEmpty()) {
            initialCollectionMap();
        }  
        contactEmail = properties.getContactEmail();
    }

    private void initialCollectionMap() {
        map = new TreeMap<>();
        collectionManagerList = new ArrayList<>();
        collectionGroupNames = new ArrayList<>();
        try {
            map = mongo.findCollectionsByDepartment(department);

            map.entrySet()
                    .stream()
                    .forEach((entry) -> {
                        String collectionGroupName = entry.getKey();
                        String manager = entry.getValue().get(0).getManager();
                        if(collectionGroupName.equals(nonScientificLoans)) {
                            nonScientificLoansManager = manager;
                        } else {
                            collectionGroupNames.add(collectionGroupName);
                            collectionManagerList.add(new CollectionManager(collectionGroupName,
                                    collectionGroupName, manager));
                            if (collectionGroupName.equals(defaultCollectionGroupName)) {
                                defaultManager = manager;
                                newCollectionManager = defaultManager;
                            }
                        }
                    });
            map.remove(nonScientificLoans);
        } catch (Exception e) {
            addError(emptyString, mongoConnectionError);
        }
    } 
        
    public void home() {
        log.info("home clicked....");
        navigate.gotoHome();
    }

    public void changePassword() {
        log.info("changePassword ");
        user.setIsChangePasswordSuccessed(false);
        navigate.gotoChangePassword();
    }
 
    public void manageAccount() {
        log.info("managerAccount");
        navigate.gotoManageUser();
    }
     
    public void managePolicyDocuments() {
        log.info("manageLoans");
        navigate.gotoLoanPolicies();
    }
 
    public void manageCollection() {
        log.info("managerCollection");
        navigate.gotoCollections();
    }
    
    public void manageLoans() {
        log.info("manageLoans");
        navigate.gotoLoans();
    }

    public void managePortalPopup() {
        log.info("managePortalPopup");
        navigate.gotoNotification();
    }
 
    public void departmentChanged() {
        log.info("departmentChanged : {}", department);

        initialCollectionMap();
        if (collectionGroupNames.isEmpty()) {
            collectionGroupNames.add(departmentSelectItem);
        }
    }
    

    public void updateNonScientificManager() {
        log.info("updateNonScientificManager : {}", nonScientificLoansManager);
        mongo.updateNonScientificLoansManager(nonScientificLoansManager);
    }
    
    public void onTabChange(TabChangeEvent event) {
        String activeIndex = ((AccordionPanel) event.getComponent()).getActiveIndex(); 
        activeTab = Integer.parseInt(activeIndex);
    }
     
    public void updateCollection(CollectionManager collectionManager) {
        log.info("updateCollection : {} -- {}", collectionManager, department);

        if (collectionManager.getNewGroupName() != null && !collectionManager.getNewGroupName().isEmpty() 
                && collectionManager.getManager() != null && !collectionManager.getManager().isEmpty()) {
            mongo.updateCollectionGroupName(
                    collectionManager.getGroup(), 
                    collectionManager.getNewGroupName(), 
                    collectionManager.getManager(), department);
        } else {
            addError(missingValueErrorMsg, collectionNameMissingError);
        }
        initialCollectionMap();
    }
 
    public void removeSubCollection(Collection collection) {
        log.info("removeSubCollection : {}", collection);

        mongo.removeCollection(collection); 
        initialCollectionMap();
    }
        
    public String managermail(String key) {
        log.info("managermail : {}", key);
        return map.get(key).get(0).getManager();
    }
         
    public void onRowEdit(RowEditEvent event) {
        log.info("onRowEdit : {}", ((Collection) event.getObject()).getName());

        Collection editCollection = (Collection) event.getObject();
        if (!isDuplicatName(editCollection)) {
            mongo.updateCollection(editCollection);
        } else {
            addDuplicatCollectionWarning(editCollection.getName(), 
                    editCollection.getGroup());
        }
        map = mongo.findCollectionsByDepartment(department);
    }
        
    public void onRowCancel(RowEditEvent event) {
        log.info("onRowCancel : {}", ((Collection) event.getObject()).getName());
    }
         
    public void addNewCollection() {
        log.info("addNewCollection : {}", newCollectionGroupName);

        if (!collectionGroupNames.contains(newCollectionGroupName)) {
            collectionGroupNames.add(newCollectionGroupName);
            newCollectionManager = null;
            managerDisable = false;
        } else {
            if (map.get(newCollectionGroupName) != null 
                    && !map.get(newCollectionGroupName).isEmpty()) {
                newCollectionManager = map.get(newCollectionGroupName).get(0).getManager();
                managerDisable = true;
            } else {
                newCollectionManager = null;
                managerDisable = false;
            }
        }
    }  
         
    public void saveNewCollection() {
        log.info("saveNewCollection : {}", newCollectionGroupName);

        List<Collection> selectedGroup = map.get(newCollectionGroupName);

        boolean nameValid = true;
        if (selectedGroup != null && !selectedGroup.isEmpty()) {
            if (isCollectionExist(selectedGroup, newCollectionName)) {
                addDuplicatCollectionWarning(newCollectionName, newCollectionGroupName);
                nameValid = false;
            }
        }
        if (nameValid) {
            Collection collection = new Collection(newCollectionName, 
                    newCollectionCurator, newCollectionGroupName, 
                    newCollectionManager, department);
            mongo.saveCollection(collection);
            initialCollectionMap();

            newCollectionName = null;
            newCollectionCurator = null;
            newCollectionGroupName = defaultCollectionGroupName;
            newCollectionManager = defaultManager;

            managerDisable = true;
        }
    }
 
    private boolean isCollectionExist(List<Collection> selectedGroup, String name) {
        return selectedGroup
                .stream()
                .anyMatch((c) ->
                        (c.getName().toLowerCase().trim().equals(name.toLowerCase().trim())));
    }
         
    public boolean isIsNewPolicy() {
        return isNewPolicy;
    }

    public void setIsNewPolicy(boolean isNewPolicy) {
        this.isNewPolicy = isNewPolicy;
    }
        
    public void updatePolicy() {
        log.info("updatePolicy"); 
        isNewPolicy = false;
    }
     
    public void uploadScFile(FileUploadEvent event) {
        log.info("uploadScFile : {} -- {}", 
                event.getFile(), event.getFile().getFileName());
        
        fileHandler.saveFile(event.getFile(), properties.getScientificPolicyPath()); 
        isNewPolicy = true;
    }

    public void uploadEdFile(FileUploadEvent event) { 
        log.info("uploadEdFile : {} -- {}", 
                event.getFile(), event.getFile().getFileName());
        
        fileHandler.saveFile(event.getFile(), properties.getEducationalPolicyPath()); 
        isNewPolicy = true;
    }

    public String getContactEmail() {
        return contactEmail;
    }

        
        
        
        
        
        
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
//
//    public void contact() {
//        log.info("contact");
//    }

    














    

    private void addDuplicatCollectionWarning(String collectionName, String collectionGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection name [");
        sb.append(collectionName);
        sb.append("] is already exist in ");
        sb.append(collectionGroup);
        sb.append(".");

        addWarning("Duplicat Collection", sb.toString());
    }



    private boolean isDuplicatName(Collection collection) {
        List<Collection> collectionList = map.get(collection.getGroup());
        return collectionList.stream().filter((c) -> (!c.getId().equals(collection.getId()))).anyMatch((c) -> (c.getName().equals(collection.getName())));
    }




    public void groupNameChanged() {
        log.info("groupNameChanged : {}", selectedGroupName);
    }




    public String pdf(Loan loan) {
        log.info("pdf");

        StringBuilder sb = new StringBuilder();
//        sb.append(externalFilePath);
//        sb.append("id=");
//        sb.append(loan.getUuid().replace("-", "/"));
//        sb.append("/loanrequest_");
//        sb.append(loan.getId());
//        sb.append("_admin.pdf");

        log.info("link : {}", sb.toString());
        return sb.toString();
    }

    public void removeCollection(CollectionManager cm) {
        log.info("removeCollection : {} -- {}", cm, department);
        mongo.removeWholeCollection(cm.getGroup());
        initialCollectionMap();
    }

    public void onCellEdit(CellEditEvent event) {
        log.info("onCellEdit : {} -- {}", event.getOldValue(), event.getNewValue());
    }

    public void onStatusChanged(Loan loan) {
        log.info("onStatusChanged : {}", loan.getStatus());
    }





    public void cancel() {
        log.info("cancel");
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
        return collectionManagerList;
    }

    public void setCmList(List<CollectionManager> collectionManagerList) {
        this.collectionManagerList = collectionManagerList;
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
 
    public List<String> getCollectionGroupNames() {
        return collectionGroupNames;
    }

    public void setCollectionGroupNames(List<String> collectionGroupNames) {
        this.collectionGroupNames = collectionGroupNames;
    }
 
    public String getNewCollectionGroupName() {
        return newCollectionGroupName;
    }

    public void setNewCollectionGroupName(String newCollectionGroupName) {
        this.newCollectionGroupName = newCollectionGroupName;
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
    
    public String getNewCollectionName() {
        return newCollectionName;
    }

    public void setNewCollectionName(String newCollectionName) {
        this.newCollectionName = newCollectionName;
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
