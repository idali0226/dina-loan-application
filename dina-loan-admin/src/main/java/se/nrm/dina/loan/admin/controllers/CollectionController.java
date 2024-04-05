package se.nrm.dina.loan.admin.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;
import se.nrm.dina.loan.admin.vo.CollectionManager;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Collection;

/**
 *
 * @author idali
 */
@Named(value = "collection")
@SessionScoped
@Slf4j
public class CollectionController implements Serializable {
    
    private final String defaultDepartmemt = "Zoology"; 
    private final String nonScientificLoans = "Non scientific";
    private final String defaultCollectionGroupName = "Insects";
    private final String emptyString = "";
    private final String departmentSelectItem = "------------ SELECT ------------";
    private final String mongoConnectionError = "Can not connect to datasource.  Please contact administrator.";
    
    private final String missingValueErrorMsg = "Missing value";
    private final String collectionNameMissingError = "Collection name and manager email can not be empty.";
    
    private final String updateTitle = "Update";
    private final String updateMsg = "Collection is updated!";
    
    private final String msgId = "collectionMsg";
    
    private String department;
    
    private String nonScientificLoansManager;
    private String defaultManager;
    private int activeTab;
        
    private String newCollectionName;
    private String newCollectionCurator;
    private String newCollectionGroupName;
    private String newCollectionManager;
    private boolean managerDisable = true;
    
    private Map<String, List<Collection>> map;
    private List<String> collectionGroupNames;
    private List<CollectionManager> collectionManagerList;
    
    private boolean disableSaveBtn;
    
    @Inject
    private MongoJDBC mongo;
    @Inject
    private MessageBean message;
    
    public CollectionController() {
        
    }
    
    
    @PostConstruct
    public void init() {
        log.info("init");
        
        department = defaultDepartmemt;
        if (map == null || map.isEmpty()) {
            initialCollectionMap();
        }   
        disableSaveBtn = true;
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
            message.addError(emptyString, mongoConnectionError);
        }
    } 
    
    public void departmentChanged() {
        log.info("departmentChanged : {}", department);

        initialCollectionMap();
        if (collectionGroupNames.isEmpty()) {
            collectionGroupNames.add(departmentSelectItem);
        }
    }
    
    public void onTabChange(TabChangeEvent event) {
        log.info("onTabChange");
        String activeIndex = ((AccordionPanel) event.getComponent()).getActiveIndex(); 
        activeTab = Integer.parseInt(activeIndex);
        log.info("activeTab :  {}", activeTab);
    }
    
    public void updateNonScientificManager() {
        log.info("updateNonScientificManager : {}", nonScientificLoansManager);
        mongo.updateNonScientificLoansManager(nonScientificLoansManager);
         
        disableSaveBtn = true;
        PrimeFaces.current().ajax().update(msgId);
    }
    
    public void updateEmail() {
        log.info("updateEmail : {}", nonScientificLoansManager);
        disableSaveBtn = false;
    }
    
    public void removeCollection(CollectionManager cm) {
        log.info("removeCollection : {} -- {}", cm, department);
        mongo.removeWholeCollection(cm.getGroup());
        initialCollectionMap();
    }
     
    public void updateCollection(CollectionManager collectionManager) {
        log.info("updateCollection : {} -- {}", collectionManager, department);

        if (collectionManager.getNewGroupName() != null && !collectionManager.getNewGroupName().isEmpty() 
                && collectionManager.getManager() != null && !collectionManager.getManager().isEmpty()) {
            mongo.updateCollectionGroupName(
                    collectionManager.getGroup(), 
                    collectionManager.getNewGroupName(), 
                    collectionManager.getManager(), department);
            message.addInfo(updateTitle, updateMsg);
        } else {
            message.addError(missingValueErrorMsg, collectionNameMissingError);
        }
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
            message.addInfo(updateTitle, updateMsg);
        } else {  
            message.addDuplicatCollectionWarning(editCollection.getName(), 
                    editCollection.getGroup()); 
        } 
        PrimeFaces.current().ajax().update(msgId);
        initialCollectionMap(); 
    }
  
     
    public void onRowCancel(RowEditEvent event) {
        log.info("onRowCancel : {}", ((Collection) event.getObject()).getName());
    }
        
    
    private boolean isDuplicatName(Collection collection) {
        List<Collection> collectionList = map.get(collection.getGroup());
        
        return collectionList.stream().filter((c) -> 
                (!c.getId().equals(collection.getId()))).anyMatch((c) -> 
                        (c.getName().equals(collection.getName())));
    }

        
    public void saveNewCollection() {
        log.info("saveNewCollection : {}", newCollectionGroupName);

        List<Collection> selectedGroup = map.get(newCollectionGroupName);

        boolean nameValid = true;
        if (selectedGroup != null && !selectedGroup.isEmpty()) {
            if (isCollectionExist(selectedGroup, newCollectionName)) {
                message.addDuplicatCollectionWarning(newCollectionName, newCollectionGroupName);
                nameValid = false;
                PrimeFaces.current().ajax().update(msgId);
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
        
    public void removeSubCollection(Collection collection) {
        log.info("removeSubCollection : {}", collection);

        mongo.removeCollection(collection); 
        initialCollectionMap();
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
          
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
    public int getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(int activeTab) {
        this.activeTab = activeTab;
    }
    
    public String getNonScientificLoansManager() {
        return nonScientificLoansManager;
    }

    public void setNonScientificLoansManager(String nonScientificLoansManager) {
        this.nonScientificLoansManager = nonScientificLoansManager;
    }

    public List<CollectionManager> getCollectionManagerList() {
        return collectionManagerList;
    }

    public void setCollectionManagerList(List<CollectionManager> collectionManagerList) {
        this.collectionManagerList = collectionManagerList;
    }

    public Map<String, List<Collection>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<Collection>> map) {
        this.map = map;
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
    
    public boolean isManagerDisable() {
        return managerDisable;
    }

    public void setManagerDisable(boolean managerDisable) {
        this.managerDisable = managerDisable;
    }

    public String getNewCollectionManager() {
        return newCollectionManager;
    }

    public void setNewCollectionManager(String newCollectionManager) {
        this.newCollectionManager = newCollectionManager;
    } 

    public boolean isDisableSaveBtn() {
        return disableSaveBtn;
    }

    public void setDisableSaveBtn(boolean disableSaveBtn) {
        this.disableSaveBtn = disableSaveBtn;
    }
    
    
}
