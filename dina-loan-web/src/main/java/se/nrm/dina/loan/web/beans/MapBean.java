/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.loan.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.loan.web.vo.MapVO;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;

/**
 *
 * @author idali
 */
@ManagedBean(name="mapBean")
@SessionScoped 
public class MapBean implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<MapVO> mapList;
    private HashMap<String, String> mapData;
    
    @Inject
    private MongoJDBC mongo;

    @PostConstruct
    public void init() {  
         
        Map<String, Integer> map = mongo.getMapDataForLoans();  
        mapList = new ArrayList<>();
        map.entrySet().stream().forEach((entry) -> {
            mapList.add(new MapVO(entry.getKey(), String.valueOf(entry.getValue())));
        }); 
        setMapList(mapList);
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
        logger.info("getMapList -- {}", mapList.size()); 
        return mapList;
    }

    public void setMapList(List<MapVO> mapList) {
//        logger.info("setMapList -- {}", mapList.size()); 
        this.mapList = mapList;
    }
}
