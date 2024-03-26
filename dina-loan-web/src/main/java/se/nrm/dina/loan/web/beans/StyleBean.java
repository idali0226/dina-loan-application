package se.nrm.dina.loan.web.beans;

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j; 

/**
 *
 * @author idali
 */
@SessionScoped
@Named
@Slf4j
public class StyleBean implements Serializable {
      
        
    private final String ODD_ROW = "samplespanelodd"; 
    private final String EVEN_ROW = "samplespaneleven";
     
    private final String current = "current";
    private final String emptyString = "";
    
    private int currentTab = 1;
    
    private String menu1 = current;
    private String menu2;
    private String menu3;
    private String menu4;
       
    public StyleBean() { 
    }
    
     public void setCurrentTab(int tab) {

        this.menu1 = emptyString;
        this.menu2 = emptyString;
        this.menu3 = emptyString;
        this.menu4 = emptyString;
        switch (tab) {
            case 1:
                this.menu1 = current;
                break;
            case 2:
                this.menu2 = current;
                break;
            case 3:
                this.menu3 = current;
                break;
            default:
                this.menu4 = current;
                break;
        }
        currentTab = tab;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    public String getMenu1() {
        return menu1;
    }

    public void setMenu1(String menu1) {
        this.menu1 = menu1;
    }

    public String getMenu2() {
        return menu2;
    }
 
    public void setMenu2(String menu2) { 
        this.menu2 = menu2;
    }
    
    public String getMenu3() {
        return menu3;
    }

    public void setMenu3(String menu3) {
        this.menu3 = menu3;
    }

    public String getMenu4() {
        return menu4;
    }

    public void setMenu4(String menu4) {
        this.menu4 = menu4;
    }
    
    
    
   

    public int getCurrentTab() {
        return currentTab;
    }
    
     
    public String getSamplePanelColor(int step) {
        log.info("getSamplePanelColor : {}", step);
         
        return (step % 2 == 0) ? ODD_ROW : EVEN_ROW;
    } 
}
