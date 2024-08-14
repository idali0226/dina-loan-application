package se.nrm.dina.loan.web.logic;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.loan.web.service.MongoService;
import se.nrm.dina.mongodb.loan.vo.Collection;

/**
 *
 * @author idali
 */
@Slf4j
public class InitialData implements Serializable {
    
    private static Map<String, List<Collection>> map;
    
    @Inject
    private MongoService service;  
    
    public InitialData() {
        
    }
    
    public void process() {
        map = new LinkedHashMap<>();
        map = service.findAllScientificCollection();
        
//        log.info("map : {}", map);
    }

    public Map<String, List<Collection>> getMap() {
        return map;
    }
 
    
}
