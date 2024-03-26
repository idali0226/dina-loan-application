package se.nrm.dina.loan.web.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Collection; 
import se.nrm.dina.mongodb.loan.vo.Loan;

/**
 *
 * @author idali
 */ 
@Slf4j
public class MongoService implements Serializable {
    
    private Map<String, List<Collection>> sciefificCollectionMap;
    
    
    @Inject
    private MongoJDBC mongo;
    
    public MongoService() {
        
    }
    
    public void saveLoan(Loan loan) {
        mongo.save(loan);
    }
    
     public void updateLoan(Loan loan) {
        log.info("updateLoan");
        mongo.updateLoan(loan);
    }

    
    public Map<String, List<Collection>> findAllScientificCollection() {
        log.info("findAllScientificCollection"); 
         
        sciefificCollectionMap = new TreeMap<>();
        mongo.findAllScientificCollection().stream()
                .forEach((Collection c) -> {
            if (sciefificCollectionMap.containsKey(c.getGroup())) {
                sciefificCollectionMap.get(c.getGroup()).add(c);
            } else {
                List<Collection> list = new ArrayList<>();
                list.add(c);
                sciefificCollectionMap.put(c.getGroup(), list);
            }
        });

        return sciefificCollectionMap; 
    }
    
    public Map<String, Integer> getStatisticMap() {
        return mongo.getStatisticData();
    }
    
    public Map<String, Integer> getMapData() {
        return mongo.getMapDataForLoans();   
    }
    
    public Collection findCollection(String name, String field) {
        return mongo.findCollection(name, field);
    }
}
