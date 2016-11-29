package se.nrm.dina.email.model.loan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import se.nrm.dina.email.model.loan.util.HelpClass;

/**
 *
 * @author idali
 */
public class LoanNotificationEmail {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String TEXT_1 = "The following loans are overdue:";
    
    public String preparaEmailText(List<OverDueLoan> overDueLoans) {
         
        Map<String, List<OverDueLoan>> map = getBorrowerMap(overDueLoans);
         
        StringBuilder sb = new StringBuilder(); 
        sb.append("<div>");
        sb.append("<div style=\"font-size: 1.2em;  font-weight: bold; \">");
        sb.append(TEXT_1);
        sb.append("</div>");
        
        sb.append("<br />");
        sb.append("<br />");
          
        sb.append("<div style=\"float: left;  width: 120px; font-weight: bold; padding-left: 20px;   \">");
        sb.append("Loan number");
        sb.append("</div>");
        
        
        sb.append("<div style=\"float: left;  width: 100px; font-weight: bold;  \">");
        sb.append("Loan date");
        sb.append("</div>");
        
        
        sb.append("<div style=\"float: left;  width: 100px; font-weight: bold; \">");
        sb.append("Overdue date");
        sb.append("</div>");
// 
        sb.append("<br />");
        
        sb.append("<div style=\"background-color: ivory; border-top: 1px solid #000000; float: left; font-weight: normal; margin-top: 10px; padding: 10px 0 0 0; text-align: left; width: 800px; \">");
 
         
        for(Map.Entry<String, List<OverDueLoan>> entry : map.entrySet()) {
           
            List<OverDueLoan> list = entry.getValue();
            Person borrower = list.get(0).getBorrower();
            
            if(borrower != null) {
                sb.append("<div style=\"float: left;  width: 800px; \">"); 
                sb.append("<div style=\"float: left; \">"); 
                if(borrower.getAgentFirstName() == null) {
                    sb.append(" ");
                } else {
                    sb.append(borrower.getAgentFirstName());
                }
                sb.append(" ");
                
                if(borrower.getAgentLastName() == null) {
                    sb.append(" ");
                } else {
                    sb.append(borrower.getAgentLastName());
                } 
                sb.append("</div>"); 
                
                sb.append("<div style=\"float: left;  width: 20px;\">");  
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                sb.append("</div>");  
                
                sb.append("<div style=\"float: left;  \">");  
                
                if(borrower.getAgentEmail() == null) {
                    sb.append(" ");
                } else { 
                    sb.append(borrower.getAgentEmail());
                }    
                sb.append("</div>");  
                 
            } else {
                sb.append("<div style=\"float: left;  width: 800px; \">"); 
                sb.append("<div style=\"float: left; \">"); 
                sb.append("Borrower not exist!"); 
                sb.append(" "); 
                sb.append(" "); 
                sb.append("</div>"); 
                
                sb.append("<div style=\"float: left;  width: 20px;\">");  
                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                sb.append("</div>");  
                
                sb.append("<div style=\"float: left;  \">");   
                sb.append(" "); 
                sb.append("</div>");  
            }
            sb.append("<br />"); 
            
            for(OverDueLoan overDueLoan : list) {
            
                sb.append("<div style=\"float: left;  width: 120px; padding-left: 20px; \">"); 
                sb.append(overDueLoan.getLoanNumber());
                sb.append("</div>");

                Date loanDate = overDueLoan.getLoanDate(); 
                sb.append("<div style=\"float: left;  width: 100px;\">"); 
                sb.append(HelpClass.dateToString(loanDate));
                sb.append("</div>"); 

                Date overDueDate = overDueLoan.getDueDate();
                sb.append("<div style=\"float: left;  width: 100px;\">"); 
                sb.append(HelpClass.dateToString(overDueDate));
                sb.append("</div>"); 
                sb.append("<br />"); 
            }
            
            sb.append("<br />"); 
            sb.append("<br />"); 
            sb.append("</div>"); 
        }
        sb.append("</div>");  
        sb.append("</div>"); 
        return sb.toString();
    }
    
    private Map<String, List<OverDueLoan>> getBorrowerMap(List<OverDueLoan> OverDueLoans) {
        Map<String, List<OverDueLoan>> map = new HashMap<>();
        
        for(OverDueLoan loan : OverDueLoans) {
            Person person = loan.getBorrower();
            String key = "key";
            if(person != null) {
                key = person.getAgentEmail();
            } 
            if(map.containsKey(key)) {
                List list = map.get(key);
                list.add(loan);
            } else {
                List<OverDueLoan> list = new ArrayList<>();
                list.add(loan);
                map.put(key, list);
            } 
        }
        
        return map;
    }
}
