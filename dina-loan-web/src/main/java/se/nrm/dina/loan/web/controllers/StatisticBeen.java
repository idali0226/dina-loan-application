package se.nrm.dina.loan.web.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped; 
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.loan.web.service.MongoService;

/**
 *
 * @author idali
 */
@Named("statistic")
@RequestScoped
@Slf4j
public class StatisticBeen implements Serializable {
    
    private final String sc = "sc";
    private final String ed = "ed";
    private final String other = "other";
    private final String scYear = "sc_year";
    private final String edYear = "ed_year";
    private final String otherYear = "other_year";
      
    private Map<String, Integer> statisticMap;
    
    @Inject
    private MongoService service;
    
    public StatisticBeen() {
        
    }
    
    public void getStatisticMap() {
        if(statisticMap == null || statisticMap.isEmpty()) {
            statisticMap = new HashMap<>();
            statisticMap = service.getStatisticMap();
        } 
    }
    
    public int getSecientificTotalCount() {
        if(statisticMap == null || statisticMap.isEmpty()) {
            getStatisticMap();
        }
        return statisticMap.get(sc);
    }

    public int getEducationalTotalCount() {
        if(statisticMap == null || statisticMap.isEmpty()) {
            getStatisticMap();
        }
        return statisticMap.get(ed);
    }

    public int getOtherTotalCount() {
        if(statisticMap == null || statisticMap.isEmpty()) {
            getStatisticMap();
        }
        return statisticMap.get(other);
    }

    public int getSecientificYearCount() {
        if(statisticMap == null || statisticMap.isEmpty()) {
            getStatisticMap();
        }
        return statisticMap.get(scYear);
    }

    public int getEducationalYearCount() {
        if(statisticMap == null || statisticMap.isEmpty()) {
            getStatisticMap();
        }
        return statisticMap.get(edYear);
    }

    public int getOtherYearCount() {
        if(statisticMap == null || statisticMap.isEmpty()) {
            getStatisticMap();
        }
        return statisticMap.get(otherYear);
    }
}
