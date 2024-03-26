package se.nrm.dina.loan.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.loan.web.service.MongoService;
import se.nrm.dina.loan.web.vo.MapVO; 

/**
 *
 * @author idali
 */ 
@Named(value = "mapBean") 
@SessionScoped
@Slf4j
public class MapBean implements Serializable {
      
    private List<MapVO> mapList;
    private HashMap<String, String> mapData;
    
    @Inject
    private MongoService service;
     

    @PostConstruct
    public void init() {  
        log.info("map init....");
        
//        mongo.setUpMongoHost(properties.getMongoHost());
         
        mapList = new ArrayList<>();
        service.getMapData()
                .entrySet()
                .stream()
                .forEach((entry) -> {
                    mapList.add(new MapVO(entry.getKey(), String.valueOf(entry.getValue())));
                }); 
//        setMapList(mapList); 
    }
     
    public HashMap<String, String> getMapData() {
//        logger.info("getMapData -- {}", mapData ); 
        return mapData;
    }

    public void setMapData(HashMap<String, String> mapData) {
//        logger.info("setMapData -- {}", mapData );  
        this.mapData = mapData;
    }

    public List<MapVO> getMapList() {
        log.info("getMapList -- {}", mapList.size());  
        return mapList;
    }

//    public void setMapList(List<MapVO> mapList) {
////        logger.info("setMapList -- {}", mapList.size()); 
//        this.mapList = mapList;
//    }
}
