package se.nrm.dina.loan.web.beans;

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped; 
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

/**
 *
 * @author idali
 */
@SessionScoped
@Named
public class StyleBean implements Serializable {
     
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
        
    private final String ODD_ROW = "samplespanelodd"; 
    private final String EVEN_ROW = "samplespaneleven";
     
    private final static String CURRENT = "current";
    
    private int currentTab = 1;
    
    private String menu1 = CURRENT;
    private String menu2;
    private String menu3;
    private String menu4;
       
    public StyleBean() { 
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
    
    
    
    public void setCurrentTab(int tab) {

        this.menu1 = "";
        this.menu2 = "";
        this.menu3 = "";
        this.menu4 = "";
        if (tab == 1) {
            this.menu1 = CURRENT; 
        } else if(tab == 2) { 
            this.menu2 = CURRENT; 
        } else if(tab == 3) { 
            this.menu3 = CURRENT;
        } else {
            this.menu4 = CURRENT;
        }
        currentTab = tab;
    }

    public int getCurrentTab() {
        return currentTab;
    }
    
    
  
    public String getSamplePanelColor(int step) {
        logger.info("getSamplePanelColor : {}", step);
         
        return (step % 2 == 0) ? ODD_ROW : EVEN_ROW;
    } 
}
