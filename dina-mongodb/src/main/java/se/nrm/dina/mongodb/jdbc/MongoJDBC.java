package se.nrm.dina.mongodb.jdbc;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.time.LocalDate; 
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
//import javax.inject.Inject;
//import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.Oid;
import se.nrm.dina.mongodb.loan.vo.Collection;
import se.nrm.dina.mongodb.loan.vo.Counters;
import se.nrm.dina.mongodb.loan.vo.CountryMap;
import se.nrm.dina.mongodb.loan.vo.Loan;
import se.nrm.dina.mongodb.loan.vo.Notification;
import se.nrm.dina.mongodb.util.Util;

/**
 *
 * @author idali
 */
//@Stateless
@Slf4j
public class MongoJDBC implements Serializable {

    private MongoClient mongo;
    protected DB db;
    private Jongo jongo;
 

    private final String localholst = "localhost";
//    private final String dbServer = "dina-db.nrm.se";

    private final String scientificPurpose = "Scientific purpose";
    private final String educationPurpose = "Education/Exhibition";
    private final String otherPurpose = "Commercial/art/other";
    private final String scYear = "sc_year";
    private final String edYear = "ed_year";
    private final String otherYear = "other_year";
    private final String sc = "sc";
    private final String ed = "ed";
    private final String other = "other";

    private final String nonScientificCollectionQry = "{name: 'Non scientific'}";
    private final String scientificCollectionQry = "{group: {$ne: 'Non scientific'}}";
    

    private final String detartmentQry1 = "{department: '";
    private final String qryEnd = "'}";
    private final String dubleQryEnd = "'}}";
    private final String qryStart = "{";

    private final String qrySeparat = ": '";

    private final String updateGroupQry = "{$set: {group: '";
    private final String manageQry = "', manager: '";
    private final String groupQry = "{group: '";

    private final String sortName1 = "{name:1}";
    private final String sortEmail1 = "{email:1}";
    private final String nameQry = "{name: '";

    private final String sortByIdDecQry = "{_id:-1}";

    private final String mongoHost = "mongohost";

    private final String dbname = "loans";

    private final int port = 27017;

    private final String loanCollectionName = "loan";
    private final String collectionCollectionName = "collection";
    private final String countersCollectionName = "counters";
    private final String notificationCollectionName = "notification";

    private String host;

    private MongoCollection loanCollection;
    private MongoCollection collectionCollection;
    private MongoCollection countersCollection;
    private MongoCollection notificationCollection;

    private Iterable<Loan> loans;
    private List<Loan> scientificLoans;
    private List<Loan> educationLoans;
    private List<Loan> otherLoans;
    
    private Iterable<Collection> collections;

    private List<Collection> collectionResults;
    private Map<String, List<Collection>> collectionMap;
    private Map<String, Integer> map;  
    private List<Collection> scientificCollectionList;
    private Map<String, Integer> mapData;
 

//    private LocalDate from;
//    private LocalDate to;
    private String currentYear;

    public MongoJDBC() {
    }

    @PostConstruct
    void init() {
        log.info("init");
        
        currentYear = String.valueOf(LocalDate.now().getYear()); 
//        from = LocalDate.now().with(firstDayOfYear());
//        to = LocalDate.now().with(lastDayOfYear()); 

        host = System.getProperty(mongoHost, localholst); 
        log.info("mongohost  : {}", host);
        try {
            mongo = new MongoClient(host, port);
            db = mongo.getDB(dbname);
            jongo = new Jongo(db);
            loanCollection = jongo.getCollection(loanCollectionName);
            collectionCollection = jongo.getCollection(collectionCollectionName);
            countersCollection = jongo.getCollection(countersCollectionName);
            notificationCollection = jongo.getCollection(notificationCollectionName);
        } catch (UnknownHostException ex) {
            log.warn(ex.getMessage());
        }
    }

    // start statistic 
    public Map<String, Integer> getStatisticData() {
        log.info("getStatisticData");

        map = new HashMap();
 
        scientificLoans = findTotalCountByPurpose(scientificPurpose);
        educationLoans = findTotalCountByPurpose(educationPurpose);
        otherLoans = findTotalCountByPurpose(otherPurpose);
         
        map.put(sc, scientificLoans.size()); 
        map.put(ed, educationLoans.size());
        map.put(other, educationLoans.size());
        
        
        map.put(scYear, findYearCount(scientificLoans));
        map.put(edYear, findYearCount(educationLoans));
        map.put(otherYear, findYearCount(otherLoans));
         
        return map;
    }
  
    private List<Loan> findTotalCountByPurpose(String purpose) {
         
        loans = loanCollection.find("{$and:[{purpose: '" + purpose
                + "'}, {emailFailed: {$ne: true}}]}").as(Loan.class);
        
        return StreamSupport.stream(loans.spliterator(), false)
                     .collect(Collectors.toList()); 
    }

    private int findYearCount(List<Loan> loanList) {
        return (int) loanList.stream()
                    .map(Loan::getSubmitDate)
                    .filter(isInCurrent)
                    .count(); 
    } 
    private final Predicate<String> isInCurrent = date -> date.startsWith(currentYear);
  
    public Map<String, Integer> getMapDataForLoans() {
        log.info("getMapData: {}", loanCollection);
 
        loans = findAllLoans(); 
        int count;
        mapData = new HashMap();
        String country;
        for (Loan loan : loans) { 
            if (loan.getPrimaryUser() != null) {
                country = CountryMap.getMappingName(loan.getPrimaryUser().getAddress().getCountry()); 
                if (mapData.containsKey(country)) {
                    count = mapData.get(country) + 1;
                } else {
                    count = 1;
                }
                mapData.put(country, count);
            }
        }
        return mapData;
    }
    // end statistic 
    
    
    
    
    // Scientific loan
    public List<Collection> findAllScientificCollection() {
        log.info("findAllScientificCollection");
        
        collections = collectionCollection
                .find(scientificCollectionQry)
                .sort(sortName1).as(Collection.class);

        scientificCollectionList = new ArrayList<>();
        Util.stream(collections).forEach(c -> scientificCollectionList.add(c));
        return scientificCollectionList; 
    }
      
    public Collection findCollection(String name, String field) {
        return collectionCollection
                .findOne(buildFindCollectionQry(name, field)).as(Collection.class);
    }
    
    private String buildFindCollectionQry(String name, String field) {
        StringBuilder sb = new StringBuilder();
        sb.append(qryStart);
        sb.append(field);
        sb.append(qrySeparat);
        sb.append(name);
        sb.append(qryEnd);
        return sb.toString().trim();
    }
    
    public void save(Loan loan) {
        log.info("save : {}", loan.getId());
        
        log.info("samples : {}", loan.getSamples());
        loanCollection.insert(loan);
    }

    public void updateLoan(Loan loan) {
        log.info("updateLoan");
        loanCollection.update("{_id: '" + loan.getId() + "'}").with(loan);
    }

      
//    public Map<String, List<Collection>> findAllScientificCollection() {
//        log.info("findAllScientificCollection");
//        
//        collections = collectionCollection
//                .find(scientificCollectionQry)
//                .sort(sortName1).as(Collection.class);
//
//        List<Collection> results = new ArrayList<>();
//        Util.stream(collections).forEach(c -> results.add(c));
//
//        scientificCollectionMap = new TreeMap<>();
//        results.stream().forEach((Collection c) -> {
//            if (scientificCollectionMap.containsKey(c.getGroup())) {
//                scientificCollectionMap.get(c.getGroup()).add(c);
//            } else {
//                List<Collection> list = new ArrayList<>();
//                list.add(c);
//                scientificCollectionMap.put(c.getGroup(), list);
//            }
//        });
//
//        return scientificCollectionMap;
//    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

 
//    private int findCurrentYearCountByPurpose(String purpose, String fromDate, String endDate) {
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("{$and:[{'purpose': '");
//        sb.append(purpose);
//        sb.append("'}, {submitDate: {$gte: '");
//        sb.append(fromDate);
//        sb.append("', $lte: '");
//        sb.append(endDate);
//        sb.append("'}}]}");
//
//        loans = loanCollection.find(sb.toString()).as(Loan.class);
//
//        List<Loan> results = new ArrayList<>();
//        Util.stream(loans).forEach(l -> results.add(l));
//        return results.size();
//    }

    






    public void updateNonScientificLoansManager(String email) {
        log.info("updateNonScientificLoansManager: {}", email);

        Collection collection = collectionCollection
                .findOne(nonScientificCollectionQry)
                .as(Collection.class);
        collection.setManager(email);
        collection.setEmail(email);
        collectionCollection.update(nonScientificCollectionQry).with(collection);
    }

    public void updateCollectionGroupName(String oldGroup, String newGroup,
            String newManager, String department) {
        log.info("updateCollectionGroupName : {} -- {}", oldGroup, newGroup);

        StringBuilder sb = new StringBuilder();
        sb.append(updateGroupQry);
        sb.append(newGroup);
        sb.append(manageQry);
        sb.append(newManager);
        sb.append(dubleQryEnd);
        collectionCollection.update(buildGroupQry(oldGroup))
                .multi().with(sb.toString());
    }

    private String buildGroupQry(String group) {
        StringBuilder sb = new StringBuilder();
        sb.append(groupQry);
        sb.append(group);
        sb.append(qryEnd);
        return sb.toString().trim();
    }

    public void removeCollection(Collection collection) {
        collectionCollection.remove(buildNameQry(collection.getName()));
    }

    private String buildNameQry(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(nameQry);
        sb.append(name);
        sb.append(qryEnd);
        return sb.toString().trim();
    }

    public void updateCollection(Collection collection) {
        log.info("updateCollection");
        collectionCollection.update(Oid.withOid(collection.getId())).with(collection);
    }

    public void saveCollection(Collection collection) {
        log.info("saveCollection : {}", collection);
        collectionCollection.insert(collection);
    }

    public void updateNotification(Notification notification) {
        log.info("updateNotification: {}", notification.isIsActive());

        notificationCollection
                .update(Oid.withOid(notification.getId()))
                .with(notification);
    }

    public List<Notification> findNotifications() {
        Iterable<Notification> notifications = notificationCollection
                .find()
                .sort(sortByIdDecQry)
                .as(Notification.class);

        List<Notification> results = new ArrayList<>();
        Util.stream(notifications).forEach(l -> results.add(l));
        return results;
    }

    public void deleteNotification(Notification notification) {
        log.info("deleteNotification: {}", notification.getId());

        notificationCollection.remove(new ObjectId(notification.getId()));
    }

    public void saveNotification(Notification notification) {
        notificationCollection.insert(notification);
    }

    public List<Loan> findAllLoans() {
        log.info("findAllLoans");

//    Iterable<Loan> loans = loanCollection.find("{emailFiled: {$ne: true}}").sort("{_id:-1}").as(Loan.class);
        loans = loanCollection.find().sort("{_id:-1}").as(Loan.class);

        return StreamSupport.stream(loans.spliterator(), false)
                .filter(l -> !l.isEmailFailed())
                .collect(Collectors.toList());
    }

    public Map<String, List<Collection>> findCollectionsByDepartment(String department) {
        log.info("findCollectionsByDepartment : {} ", department);

//        collections = collectionCollection
//                .find(buildDepartmentQry(department))
//                .sort(sortName1).as(Collection.class);
        collections = findAllCollectionByDepartment(department, sortName1);

        collectionResults = new ArrayList<>();
        Util.stream(collections)
                .forEach(c -> collectionResults.add(c));

        collectionMap = new TreeMap<>();
        collectionResults.stream()
                .forEach((Collection c) -> {
                    if (collectionMap.containsKey(c.getGroup())) {
                        collectionMap.get(c.getGroup()).add(c);
                    } else {
                        List<Collection> list = new ArrayList<>();
                        list.add(c);
                        collectionMap.put(c.getGroup(), list);
                    }
                });
        log.info("collectionMap size : {}", collectionMap.size());
        return collectionMap;
    }

    public List<String> findAllCollectionNameListByDepartmentName(String department) {
        log.info("findAllCollectionNameListByDepartmentName : {}", department);

        return Util.stream(findAllCollectionByDepartment(department, sortName1))
                .map(Collection::getName)
                .collect(Collectors.toList());
    }

    /**
     * findUniqueCuratorsEmails returns a list of unique curator's emails by
     * each department
     *
     * @param department
     * @return List
     */
    public List<String> findUniqueCuratorsEmails(String department) {
        log.info("findUniqueCuratorsEmails : {}", department);

        return Util.stream(findAllCollectionByDepartment(department, sortEmail1))
                .filter(c -> c.getEmail() != null)
                .map(Collection::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    private Iterable<Collection> findAllCollectionByDepartment(String department, String sort) {
        return collectionCollection.find(buildDepartmentQry(department))
                .sort(sort).as(Collection.class);
    }

    private String buildDepartmentQry(String department) {
        StringBuilder sb = new StringBuilder();
        sb.append(detartmentQry1);
        sb.append(department);
        sb.append(qryEnd);
        return sb.toString().trim();
    }

    public List<Notification> findActiveNotifications() {
        Iterable<Notification> notifications
                = notificationCollection.find("{isActive: true}")
                        .sort("{orderNumber:1}").as(Notification.class);

        List<Notification> results = new ArrayList<>();
        Util.stream(notifications).forEach(l -> results.add(l));
        return results;
    }

    

    public Map<String, List<Collection>> findAllCollection() {
        log.info("findAllCollection");
        Iterable<Collection> collections = collectionCollection.find().sort("{name:1}").as(Collection.class);

        List<Collection> results = Util.stream(collections)
                .collect(Collectors.toList());

        Map<String, List<Collection>> map = new TreeMap<>();
        results.stream().forEach((Collection c) -> {
            if (map.containsKey(c.getGroup())) {
                map.get(c.getGroup()).add(c);
            } else {
                List<Collection> list = new ArrayList<>();
                list.add(c);
                map.put(c.getGroup(), list);
            }
        });
        return map;
    }

    /**
     *
     * @return
     */
    // TODO: this method seems return all the collection name, not the collection group name.  But this method seems not in use?
    public List<String> findAllCollectionGroupNames() {
        log.info("findAllCollectionGroupNames");
        Iterable<Collection> collections = collectionCollection.find().as(Collection.class);

        Util.stream(collections).map(Collection::getName);

        List<String> results = new ArrayList<>();
        Util.stream(collections).forEach(c -> results.add(c.getName()));
        return results;
    }

    public Loan fineLoanById(String id) {
        log.info("fineLoanById : {}", id);
        return loanCollection.findOne("{_id: '" + id + "'}").as(Loan.class);
    }

    private String buildSubString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        list.stream().map((s) -> {
            sb.append("'");
            sb.append(s);
            return s;
        }).forEach((_item) -> {
            sb.append("', ");
        });
        String string = StringUtils.substringBeforeLast(sb.toString(), ",");

        return string;
    }

    public List<Loan> findLoansWithQueryMap(Map<String, List<String>> map,
            String dateForSearch, String fromDate, String toDate, boolean searchDate) {
        log.info("findLoansWithQueryMap");

        StringBuilder sb = new StringBuilder();
        sb.append("{$and:[");

        map.entrySet().stream().map((entry) -> {
            sb.append("{'");
            sb.append(entry.getKey());
            return entry;
        }).map((entry) -> {
            sb.append("': {'$in': [");
            sb.append(buildSubString(entry.getValue()));
            return entry;
        }).forEach((_item) -> {
            sb.append("]}}, ");
        });

        String query;
        if (searchDate) {
            sb.append("{'");
            sb.append(dateForSearch);
            sb.append("': {$gte: '");
            sb.append(fromDate);
            sb.append("', $lte: '");
            sb.append(toDate);
            sb.append("'}}]}");
            query = sb.toString();
        } else {
            query = StringUtils.substringBeforeLast(sb.toString(), ",") + "]}";
        }

        loans = loanCollection.find(query).as(Loan.class);
        List<Loan> results = new ArrayList<>();
        Util.stream(loans).forEach(l -> results.add(l));
        return results;
    }

    public List<Loan> findLoansWithQuery(List<String> list, Map<String, String> map) {
        log.info("findLoansWithQuery");

        StringBuilder sb = new StringBuilder();
        sb.append("{$and:[");

        map.entrySet().stream().map((entry) -> {
            sb.append("{");
            sb.append(entry.getKey());
            return entry;
        }).map((entry) -> {
            sb.append(": '");
            sb.append(entry.getValue());
            return entry;
        }).forEach((_item) -> {
            sb.append("'},");
        });

        if (list != null) {
            sb.append(" { 'status' : {'$in': [");

            list.stream().map((s) -> {
                sb.append("'");
                sb.append(s);
                return s;
            }).forEach((_item) -> {
                sb.append("', ");
            });
        }
        String query = StringUtils.substringBeforeLast(sb.toString(), ",") + "]}}]}";
        loans = loanCollection.find(query).as(Loan.class);
        List<Loan> results = new ArrayList<>();
        Util.stream(loans).forEach(l -> results.add(l));
        return results;
    }

    public List<Loan> findLoansWithQuery(Map<String, String> queryMap) {

        log.info("findLoansWithQuery");

        StringBuilder sb = new StringBuilder();
        sb.append("{$and:[");
        queryMap.entrySet().stream().map((entry) -> {
            sb.append("{");
            sb.append(entry.getKey());
            return entry;
        }).map((entry) -> {
            sb.append(": '");
            sb.append(entry.getValue());
            return entry;
        }).forEach((_item) -> {
            sb.append("'},");
        });
        String query = StringUtils.substringBeforeLast(sb.toString(), ",") + "]}";

        loans = loanCollection.find(query).as(Loan.class);
        List<Loan> results = new ArrayList<>();
        Util.stream(loans).forEach(l -> results.add(l));
        return results;
    }

    public void updateLoan(Loan loan, boolean removeReleventCollection) {
        loanCollection.update("{_id: '" + loan.getId() + "'}").with(loan);
        loanCollection.findAndModify("{_id: '" + loan.getId() + "'}").with("{$unset: { releventCollection: \"\" }}").as(Loan.class);
    }

    public void updateAllCollections(String name, String email, String group) {
        log.info("updateCollection");

        String query = "{name: '" + name + "'}";
        Collection collection = collectionCollection.findOne(query).as(Collection.class);

        collection.setGroup(group);
        collectionCollection.update(query).with(collection);
    }

    public Map<String, List<String>> findAllCollectionNames() {
        log.info("findAllCollection");
        Iterable<Collection> collections = collectionCollection.find().as(Collection.class);

        List<Collection> results = new ArrayList<>();
        Util.stream(collections).forEach(c -> results.add(c));

        Map<String, List<String>> map = new LinkedHashMap<>();
        results.stream().forEach((Collection c) -> {
            if (map.containsKey(c.getGroup())) {
                map.get(c.getGroup()).add(c.getName());
            } else {
                List<String> list = new ArrayList<>();
                list.add(c.getName());
                map.put(c.getGroup(), list);
            }
        });
        return map;
    }

    public void removeWholeCollection(String groupName) {
        collectionCollection.remove("{group: '" + groupName + "'}");
    }

    public int getNextSequence() {
        Counters counters = countersCollection.findAndModify("{_id: 'loanid'}")
                .with("{$inc: {seq: 1}}").returnNew().as(Counters.class);

        return counters.getSeq();
    }

    public boolean uuidExist(UUID uuid) {
        log.info("uuidExist : {}", uuid.toString());

        Loan loan = loanCollection.findOne("{uuid: '" + uuid.toString() + "'}").as(Loan.class);
        return loan != null;
    }

    @PreDestroy
    public void destroyBean() {
        log.info("The bean is being destroyed now, be careful!!!");
        mongo.close();
    }
}
