package se.nrm.dina.loan.web.util;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Slf4j
public enum Department {
 
    Botany,
    EnvionmentSpecimenBank,
    Geology,
    Palaeobiology, 
    Zoology;
 
    private static final String esb_en = "Environmental Specimen Bank"; 
    
    private static final String esb_sv = "Miljöprovbanken"; 
    private static final String botany_se = "Botanik"; 
    private static final String geology_se = "Geologi"; 
    private static final String paleobiology_se = "Paleobiologi";  
    
    private static final String geology_collection_se = "Mineralogi";  
    private static final String geology_collection_en = "Mineralogy";  
    
    public String getText() {
        return this.name().toLowerCase();
    }
     
    public boolean isZoology(String department) {
        return isSelectedDepartment(department);
    }
     
    public boolean isSelectedDepartment(String department) {  
        return this.name().equalsIgnoreCase(department);
    }
    
    public static String getDepartCollectionName(String name, boolean isSwedish) {
        switch (name) {
            case "botany" :   
                return isSwedish ? botany_se : Botany.name();  
            case "envionmentspecimenbank" : 
                return isSwedish ? esb_sv : esb_en ;  
            case "geology":   
                return isSwedish ? geology_collection_se : geology_collection_en; 
            case "palaeobiology":   
                return isSwedish ? paleobiology_se : Palaeobiology.name(); 
            default: return Zoology.name();
        }
    }
    
    public static String getDepartName(String name, boolean isSwedish) {
        switch (name) {
            case "botany" :   
                return isSwedish ? botany_se : Botany.name();  
            case "envionmentspecimenbank" : 
                return isSwedish ? esb_sv : esb_en ;  
            case "geology":   
                return isSwedish ? geology_se : Geology.name(); 
            case "palaeobiology":   
                return isSwedish ? paleobiology_se : Palaeobiology.name(); 
            default: return Zoology.name();
        }
    }
    
    public static String getDepartName(String name) { 
        switch (name) {
            case "botany" :   
                return Botany.name();  
            case "envionmentspecimenbank" : 
                return EnvionmentSpecimenBank.name();  
            case "geology":   
                return Geology.name(); 
            case "palaeobiology":   
                return Palaeobiology.name(); 
            default: return Zoology.name();
        }
    }
}
