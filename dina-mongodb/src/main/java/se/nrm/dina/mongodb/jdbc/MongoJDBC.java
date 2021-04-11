package se.nrm.dina.mongodb.jdbc;

import com.mongodb.DB;
import com.mongodb.MongoClient; 
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.Oid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Stateless
public class MongoJDBC {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private MongoClient mongo;
  protected DB db;
  private Jongo jongo;

  private final String LOCAL_HOST = "localhost";
  private final String DB_HOST = "db";

  private String HOST = null;
  private final int PORT = 27017;

  private final String LOAN_COLLECTION_NAME = "loan";
  private final String COLLECTION_COLLECTION_NAME = "collection";
  private final String COUNTERS_COLLECTION_NAME = "counters";
  private final String NOTIFICATION_COLLECTION_NAME = "notification";
  
  private MongoCollection loanCollection;
  private MongoCollection collectionCollection;
  private MongoCollection countersCollection;
  private MongoCollection notificationCollection;

  public MongoJDBC() {
    String nodeName;
    InetAddress inetAddress = null;
    try {
      inetAddress = InetAddress.getLocalHost();
    } catch (UnknownHostException une) {
      logger.warn(une.getMessage());
    }
    if (inetAddress != null) {
      nodeName = inetAddress.getHostName();
      if (nodeName.contains("local")) {
        HOST = LOCAL_HOST;
      } else {
        HOST = DB_HOST;
      }
    }
  }

  @PostConstruct
  void init() {
    try {
      mongo = new MongoClient(HOST, PORT);
      db = mongo.getDB("loans");
      jongo = new Jongo(db);
      loanCollection = jongo.getCollection(LOAN_COLLECTION_NAME);
      collectionCollection = jongo.getCollection(COLLECTION_COLLECTION_NAME);
      countersCollection = jongo.getCollection(COUNTERS_COLLECTION_NAME);
      notificationCollection = jongo.getCollection(NOTIFICATION_COLLECTION_NAME);
    } catch (UnknownHostException ex) {
      logger.warn(ex.getMessage());
    }
  }

  public Map<String, Integer> getMapDataForLoans() {
    logger.info("getMapData: {}", loanCollection);

    Iterable<Loan> loans = loanCollection.find().sort("{_id:-1}").as(Loan.class);

    int count;
    Map<String, Integer> map = new HashMap();
    String country;
    for (Loan loan : loans) {
      if (loan.getPrimaryUser() != null) {
        country = CountryMap.getMappingName(loan.getPrimaryUser().getAddress().getCountry());

        if (map.containsKey(country)) {
          count = map.get(country) + 1;
        } else {
          count = 1;
        }
        map.put(country, count);
      }

    }
    return map;
  }

  public Map<String, Integer> getStatisticData() {
    logger.info("getStatisticData");

    Map<String, Integer> map = new HashMap();

    Calendar now = Calendar.getInstance();
    int year = now.get(Calendar.YEAR);
    String fromDate = year + "-01-01";
    String endDate = year + "-12-01";

    map.put("sc_year", findCurrentYearCountByPurpose("Scientific purpose", fromDate, endDate));
    map.put("ed_year", findCurrentYearCountByPurpose("Education/Exhibition", fromDate, endDate));
    map.put("other_year", findCurrentYearCountByPurpose("Commercial/art/other", fromDate, endDate));

    map.put("sc", findTotalCountByPurpose("Scientific purpose"));
    map.put("ed", findTotalCountByPurpose("Education/Exhibition"));
    map.put("other", findTotalCountByPurpose("Commercial/art/other"));
    return map;
  }

  private int findTotalCountByPurpose(String purpose) {
 
    Iterable<Loan> loans = loanCollection.find("{$and:[{purpose: '" + purpose + "'}, {emailFailed: {$ne: true}}]}").as(Loan.class);
    List<Loan> results = new ArrayList<>();
    Util.stream(loans).forEach(l -> results.add(l));
    return results.size();
  }

  private int findCurrentYearCountByPurpose(String purpose, String fromDate, String endDate) {

    StringBuilder sb = new StringBuilder();
    sb.append("{$and:[{'purpose': '");
    sb.append(purpose);
    sb.append("'}, {submitDate: {$gte: '");
    sb.append(fromDate);
    sb.append("', $lte: '");
    sb.append(endDate);
    sb.append("'}}]}");
    logger.info("query : {}", sb.toString());

    Iterable<Loan> loans = loanCollection.find(sb.toString()).as(Loan.class);

    List<Loan> results = new ArrayList<>();
    Util.stream(loans).forEach(l -> results.add(l));
    return results.size();
  }

  public Map<String, List<Collection>> findCollectionsByDepartment(String department) {
    logger.info("findCollectionsByDepartment : {} ", department);

    Iterable<Collection> collections = collectionCollection.find("{department: '" + department + "'}").sort("{name:1}").as(Collection.class);

    List<Collection> results = new ArrayList<>();
    Util.stream(collections).forEach(c -> results.add(c));

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

  public Map<String, List<Collection>> findAllScientificCollection() {
    logger.info("findAllScientificCollection");
    Iterable<Collection> collections = collectionCollection
            .find("{group: {$ne: 'Non scientific'}}")
            .sort("{name:1}").as(Collection.class);

    List<Collection> results = new ArrayList<>();
    Util.stream(collections).forEach(c -> results.add(c));

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

  public Map<String, List<Collection>> findAllCollection() {
    logger.info("findAllCollection");
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
   * findUniqueCuratorsEmails returns a list of unique curator's emails by each
   * department
   *
   * @param department
   * @return List<String>
   */
  public List<String> findUniqueCuratorsEmails(String department) {
    logger.info("findUniqueCuratorsEmails : {}", department);

    return Util.stream(findAllCollectionByDepartment(department, "email"))
            .filter(c -> c.getEmail() != null)
            .map(Collection::getEmail)
            .distinct()
            .collect(Collectors.toList());
  }

  public List<String> findAllCollectionNameListByDepartmentName(String department) {
    logger.info("findAllCollectionNameListByDepartmentName : {}", department);

    return Util.stream(findAllCollectionByDepartment(department, "name"))
            .map(Collection::getName)
            .collect(Collectors.toList());
  }

  private Iterable<Collection> findAllCollectionByDepartment(String department, String sort) {
    return collectionCollection.find("{department: '" + department + "'}")
            .sort("{" + sort + ":1}").as(Collection.class);
  }

  /**
   *
   * @return
   */
  // TODO: this method seems return all the collection name, not the collection group name.  But this method seems not in use?
  public List<String> findAllCollectionGroupNames() {
    logger.info("findAllCollectionGroupNames");
    Iterable<Collection> collections = collectionCollection.find().as(Collection.class);

    Util.stream(collections).map(Collection::getName);

    List<String> results = new ArrayList<>();
    Util.stream(collections).forEach(c -> results.add(c.getName()));
    return results;
  }

  public List<Loan> findAllLoans() {
    logger.info("findAllLoans");

//    Iterable<Loan> loans = loanCollection.find("{emailFiled: {$ne: true}}").sort("{_id:-1}").as(Loan.class);
    Iterable<Loan> loans = loanCollection.find().sort("{_id:-1}").as(Loan.class);
    List<Loan> results = new ArrayList<>();
    Util.stream(loans).forEach(l -> results.add(l)); 
    return results;
  }

  public Loan fineLoanById(String id) {
    logger.info("fineLoanById : {}", id); 
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
    logger.info("findLoansWithQueryMap");

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

    Iterable<Loan> loans = loanCollection.find(query).as(Loan.class);
    List<Loan> results = new ArrayList<>();
    Util.stream(loans).forEach(l -> results.add(l));
    return results;
  }

  public List<Loan> findLoansWithQuery(List<String> list, Map<String, String> map) {
    logger.info("findLoansWithQuery");

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
    Iterable<Loan> loans = loanCollection.find(query).as(Loan.class);
    List<Loan> results = new ArrayList<>();
    Util.stream(loans).forEach(l -> results.add(l));
    return results;
  }

  public List<Loan> findLoansWithQuery(Map<String, String> queryMap) {

    logger.info("findLoansWithQuery");

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

    Iterable<Loan> loans = loanCollection.find(query).as(Loan.class);
    List<Loan> results = new ArrayList<>();
    Util.stream(loans).forEach(l -> results.add(l));
    return results;
  }

  public void updateCollectionGroupName(String oldGroup, String newGroup, String newManager, String department) {
    logger.info("updateCollectionGroupName");

    StringBuilder sb = new StringBuilder();
    sb.append("{$set: {group: '");
    sb.append(newGroup);
    sb.append("', manager: '");
    sb.append(newManager);
    sb.append("'}}");
    collectionCollection.update("{group: '" + oldGroup + "'}").multi().with(sb.toString());
  }

  public void updateLoan(Loan loan) {
    logger.info("updateLoan");
    loanCollection.update("{_id: '" + loan.getId() + "'}").with(loan);
  }

  public void updateLoan(Loan loan, boolean removeReleventCollection) {
    loanCollection.update("{_id: '" + loan.getId() + "'}").with(loan);
    loanCollection.findAndModify("{_id: '" + loan.getId() + "'}").with("{$unset: { releventCollection: \"\" }}").as(Loan.class);
  }
    
  public void updateCollection(Collection collection) {
    logger.info("updateCollection");
    collectionCollection.update(Oid.withOid(collection.getId())).with(collection);
  }

  public void updateAllCollections(String name, String email, String group) {
    logger.info("updateCollection");

    String query = "{name: '" + name + "'}";
    Collection collection = collectionCollection.findOne(query).as(Collection.class);

    collection.setGroup(group);
    collectionCollection.update(query).with(collection);
  }

  public void updateNonScientificLoansManager(String email) {
    logger.info("updateNonScientificLoansManager: {}", email);

    String query = "{name: 'Non scientific'}";
    Collection collection = collectionCollection.findOne(query).as(Collection.class);
    collection.setManager(email);
    collection.setEmail(email);
    collectionCollection.update(query).with(collection);
  }

  public Map<String, List<String>> findAllCollectionNames() {
    logger.info("findAllCollection");
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

  public Collection findCollection(String name, String field) {
    return collectionCollection.findOne("{" + field + ": '" + name + "'}").as(Collection.class);
  }

  public void saveCollection(Collection collection) {
    logger.info("saveCollection : {}", collection);
    collectionCollection.insert(collection);
  }

  public void removeCollection(Collection collection) {
    collectionCollection.remove("{name: '" + collection.getName() + "'}");
  }

  public void removeWholeCollection(String groupName) {
    collectionCollection.remove("{group: '" + groupName + "'}");
  }

  public void save(Loan loan) {
    loanCollection.insert(loan);
  }
  
  public void saveNotification(Notification notification) {
    notificationCollection.insert(notification);
  }
  
  public void updateNotification(Notification notification) {
    logger.info("updateNotification: {}", notification.isIsActive());
   
    notificationCollection.update(Oid.withOid(notification.getId())).with(notification);
  }
  
  public List<Notification> findNotifications() {
    Iterable<Notification> notifications = notificationCollection.find().sort("{_id:-1}")
            .as(Notification.class);
    List<Notification> results = new ArrayList<>();
    Util.stream(notifications).forEach(l -> results.add(l)); 
    return results;
  }
  
  public List<Notification> findActiveNotifications() {
    Iterable<Notification> notifications =
            notificationCollection.find("{isActive: true}").sort("{orderNumber:1}").as(Notification.class);
     
    List<Notification> results = new ArrayList<>();
    Util.stream(notifications).forEach(l -> results.add(l)); 
    return results;
  }
  
  public void deleteNotification(Notification notification) {
    logger.info("deleteNotification: {}", notification.getId()); 
    notificationCollection.remove(new ObjectId(notification.getId()));
  }

  public int getNextSequence() {
    Counters counters = countersCollection.findAndModify("{_id: 'loanid'}")
            .with("{$inc: {seq: 1}}").returnNew().as(Counters.class);

    return counters.getSeq();
  }

  public boolean uuidExist(UUID uuid) {
    logger.info("uuidExist : {}", uuid.toString());

    Loan loan = loanCollection.findOne("{uuid: '" + uuid.toString() + "'}").as(Loan.class);
    return loan != null;
  }

  @PreDestroy
  public void destroyBean() {
    logger.info("The bean is being destroyed now, be careful!!!");
    mongo.close();
  }
}
