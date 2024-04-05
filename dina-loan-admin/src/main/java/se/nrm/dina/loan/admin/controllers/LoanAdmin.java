package se.nrm.dina.loan.admin.controllers;

import java.io.Serializable; 
//import java.util.List;
//import java.util.Map; 
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.inject.Inject;
import javax.inject.Named; 
import lombok.extern.slf4j.Slf4j; 
//import org.primefaces.event.CellEditEvent; 
//import se.nrm.dina.loan.admin.config.InitialProperties; 
//import se.nrm.dina.loan.admin.vo.CollectionManager;
//import se.nrm.dina.mongodb.jdbc.MongoJDBC;
//import se.nrm.dina.mongodb.loan.vo.Collection;
//import se.nrm.dina.mongodb.loan.vo.Loan;

/**
 *
 * @author idali
 */
@Named(value = "loanAdmin")
@SessionScoped
@Slf4j
public class LoanAdmin implements Serializable { 
 
//    private String newCollectionName;
//    private String newCollectionCurator;
//    private String newCollectionGroupName;
//    private String newCollectionManager;
//    private boolean managerDisable = true;
 
    
//    private String department;
//
//    private String selectedGroupName;
//    private String changedGroupName;
//    private String changedManager;

//    private final String defaultCollectionGroupName = "Insects";
//    private String defaultManager;
 

//    private Map<String, List<Collection>> map;
//    private List<String> collectionGroupNames;
//    private List<CollectionManager> collectionManagerList;
    
    private String contactEmail;

//    private boolean isNewPolicy;

//    @Inject
//    private MongoJDBC mongo;
// 
//    @Inject
//    private InitialProperties properties;
// 

    public LoanAdmin() {
 
    }

    @PostConstruct
    public void init() {
        log.info("init");
         
//        contactEmail = properties.getContactEmail();
    }
 
//    public void removeSubCollection(Collection collection) {
//        log.info("removeSubCollection : {}", collection);
//
//        mongo.removeCollection(collection);  
//    }
        
//    public void saveNewCollection() {
//        log.info("saveNewCollection : {}", newCollectionGroupName);
//
//        List<Collection> selectedGroup = map.get(newCollectionGroupName);
//
//        boolean nameValid = true;
//        if (selectedGroup != null && !selectedGroup.isEmpty()) {
//            if (isCollectionExist(selectedGroup, newCollectionName)) {
//                addDuplicatCollectionWarning(newCollectionName, newCollectionGroupName);
//                nameValid = false;
//            }
//        }
//        if (nameValid) {
//            Collection collection = new Collection(newCollectionName, 
//                    newCollectionCurator, newCollectionGroupName, 
//                    newCollectionManager, department);
//            mongo.saveCollection(collection); 
//
//            newCollectionName = null;
//            newCollectionCurator = null;
//            newCollectionGroupName = defaultCollectionGroupName;
//            newCollectionManager = defaultManager;
//
//            managerDisable = true;
//        }
//    }
 
//    private boolean isCollectionExist(List<Collection> selectedGroup, String name) {
//        return selectedGroup
//                .stream()
//                .anyMatch((c) ->
//                        (c.getName().toLowerCase().trim().equals(name.toLowerCase().trim())));
//    }
//         
//    public boolean isIsNewPolicy() {
//        return isNewPolicy;
//    }
//
//    public void setIsNewPolicy(boolean isNewPolicy) {
//        this.isNewPolicy = isNewPolicy;
//    }
         
    public String getContactEmail() {
        return contactEmail;
    }
 
//    private void addDuplicatCollectionWarning(String collectionName, String collectionGroup) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Collection name [");
//        sb.append(collectionName);
//        sb.append("] is already exist in ");
//        sb.append(collectionGroup);
//        sb.append(".");
//
//        addWarning("Duplicat Collection", sb.toString());
//    }
// 
//    public void groupNameChanged() {
//        log.info("groupNameChanged : {}", selectedGroupName);
//    }
 
//    public String pdf(Loan loan) {
//        log.info("pdf");
//
//        StringBuilder sb = new StringBuilder();
////        sb.append(externalFilePath);
////        sb.append("id=");
////        sb.append(loan.getUuid().replace("-", "/"));
////        sb.append("/loanrequest_");
////        sb.append(loan.getId());
////        sb.append("_admin.pdf");
//
//        log.info("link : {}", sb.toString());
//        return sb.toString();
//    }
 
//    public void onCellEdit(CellEditEvent event) {
//        log.info("onCellEdit : {} -- {}", event.getOldValue(), event.getNewValue());
//    }
//
//    public void onStatusChanged(Loan loan) {
//        log.info("onStatusChanged : {}", loan.getStatus());
//    }
// 
//    public void cancel() {
//        log.info("cancel");
//    }
// 
//    public String getSelectedGroupName() {
//        return selectedGroupName;
//    }
//
//    public void setSelectedGroupName(String selectedGroupName) {
//        this.selectedGroupName = selectedGroupName;
//    }

//    public String getChangedGroupName() {
//        return changedGroupName;
//    }
//
//    public void setChangedGroupName(String changedGroupName) {
//        this.changedGroupName = changedGroupName;
//    }

//    public String getChangedManager() {
//        return changedManager;
//    }
//
//    public void setChangedManager(String changedManager) {
//        this.changedManager = changedManager;
//    }
 
//    public String getDepartment() {
//        return department;
//    }

//    public void setDepartment(String department) {
//        this.department = department;
//    }
 
//    public String getNewCollectionGroupName() {
//        return newCollectionGroupName;
//    }
//
//    public void setNewCollectionGroupName(String newCollectionGroupName) {
//        this.newCollectionGroupName = newCollectionGroupName;
//    }
//    
//    public String getNewCollectionCurator() {
//        return newCollectionCurator;
//    }
//
//    public void setNewCollectionCurator(String newCollectionCurator) {
//        this.newCollectionCurator = newCollectionCurator;
//    }
//
//    public String getNewCollectionManager() {
//        return newCollectionManager;
//    }
//
//    public void setNewCollectionManager(String newCollectionManager) {
//        this.newCollectionManager = newCollectionManager;
//    }
//    
//    public String getNewCollectionName() {
//        return newCollectionName;
//    }
//
//    public void setNewCollectionName(String newCollectionName) {
//        this.newCollectionName = newCollectionName;
//    }
// 
//    private void addWarning(String warningTitle, String warningMsg) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warningTitle, warningMsg));
//    }
}
