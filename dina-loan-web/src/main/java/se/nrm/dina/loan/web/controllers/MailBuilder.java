package se.nrm.dina.loan.web.controllers;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map; 
import javax.ejb.EJB; 
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException; 
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.email.NrmMail;
import se.nrm.dina.loan.web.util.Department;
import se.nrm.dina.manager.dao.AccountDao;
import se.nrm.dina.manager.entities.TblUsers;
import se.nrm.dina.mongodb.loan.vo.Loan;

/**
 *
 * @author idali
 */
@Slf4j
public class MailBuilder implements Serializable {
    
    private final String adminPdf = "_admin.pdf";
    private final String pdf = ".pdf";
    private final String loanrequest = "-loanrequest_"; 
    
     
    private Map<String, String> emailMap;
     
    @Inject
    private NrmMail nrmMail;
     
    
    @EJB
    private AccountDao dao;
    
    public MailBuilder() {
        
    } 
    
    
    public void buildOtherLoanMail(Loan loan, String loanPolicyPath,
            String sevletPath, String pdfFilePath, String adminPath, boolean isSwedish) 
            throws MessagingException, AddressException, UnsupportedEncodingException {  
        
        log.info("buildOtherLoanMail");
        
        emailMap = new HashMap<>();
        emailMap.put("isSwedish", String.valueOf(isSwedish));
        emailMap.put("loanId", loan.getId());
        emailMap.put("primarytitle", loan.getPrimaryUser().getTitle());
        emailMap.put("primarylastname", loan.getPrimaryUser().getLastname());
        emailMap.put("primaryFirstname", loan.getPrimaryUser().getFirstname());
        emailMap.put("primaryemail", loan.getPrimaryUser().getEmail());
        
        emailMap.put("collection", loan.getDepartment());
        emailMap.put("purpose", loan.getPurpose());
        emailMap.put("type", loan.getType()); 
        
        if(loan.getPrimaryUser().getDepartment() != null  
                && !loan.getPrimaryUser().getDepartment().isEmpty()) {
            emailMap.put("primarydepartment", loan.getPrimaryUser().getDepartment());
        } 
        
        if(loan.getPrimaryUser().getEoricode() != null  
                && !loan.getPrimaryUser().getEoricode().isEmpty()) {
            emailMap.put("primaryeoicode", loan.getPrimaryUser().getEoricode());
        } 
        
        emailMap.put("primaryinstitution", loan.getPrimaryUser().getInstitution());
        emailMap.put("primarycountry", loan.getPrimaryUser().getAddress().getCountry());
         
        emailMap.put("hasPrimaryContact", String.valueOf(false)); 
         
        
        emailMap.put("department", Department.getDepartName(loan.getDepartment()));
        
        log.info("uuid : {}", loan.getUuid());
        StringBuilder sb = new StringBuilder();
        sb.append(loan.getUuid());
        sb.append("-loanrequest_");
        sb.append(loan.getId());
        
        StringBuilder adminSb = new StringBuilder(sb.toString());
        adminSb.append("_admin.pdf");  
        
        sb.append(".pdf");
        log.info("summary file path : {} -- {}",sb.toString(), adminSb.toString() );
        
        
        emailMap.put("summaryFile", sb.toString().trim()); 
        emailMap.put("adminSummaryFile", adminSb.toString().trim());
 
        
       
        TblUsers curator = dao.findOneUserByEmail(loan.getCurator()); 
        if(curator != null) {
            emailMap.put("outofoffice", String.valueOf(curator.getOnvacation()));
        }
        emailMap.put("curratormail", loan.getCurator());
        emailMap.put("manager", loan.getManager());
        emailMap.put("pdfPath", pdfFilePath);  
        emailMap.put("loanPolicy", loanPolicyPath);  
        emailMap.put("adminPath", adminPath);
        emailMap.put("servletPath", sevletPath);
        
        nrmMail.send(emailMap);  
    }
    
    public void buildEmail(Loan loan, String loanPolicyPath, String servletPath, 
            String documentPath, String adminPath, 
            boolean isSwedish,  boolean hasSecondaryBorrow)
            throws MessagingException, AddressException, UnsupportedEncodingException {  
        log.info("buildEmail : {}", hasSecondaryBorrow);
        
        emailMap = new HashMap<>();
        emailMap.put("isSwedish", String.valueOf(isSwedish));
        emailMap.put("loanId", loan.getId());
        emailMap.put("primarytitle", loan.getPrimaryUser().getTitle());
        emailMap.put("primarylastname", loan.getPrimaryUser().getLastname());
        emailMap.put("primaryFirstname", loan.getPrimaryUser().getFirstname());
        emailMap.put("primaryemail", loan.getPrimaryUser().getEmail());

        emailMap.put("collection", loan.getDepartment());
        emailMap.put("purpose", loan.getPurpose());
        emailMap.put("type", loan.getType());
        emailMap.put("subcollection", loan.getReleventCollection());

        if (loan.getPrimaryUser().getDepartment() != null
                && !loan.getPrimaryUser().getDepartment().isEmpty()) {
            emailMap.put("primarydepartment", loan.getPrimaryUser().getDepartment());
        }
        if (loan.getPrimaryUser().getEoricode() != null
                && !loan.getPrimaryUser().getEoricode().isEmpty()) {
            emailMap.put("primaryeoicode", loan.getPrimaryUser().getEoricode());
        }
        emailMap.put("primaryinstitution", loan.getPrimaryUser().getInstitution());
        emailMap.put("primarycountry", loan.getPrimaryUser().getAddress().getCountry());

        emailMap.put("hasPrimaryContact", String.valueOf(hasSecondaryBorrow));
//        emailMap.put("isLocal", String.valueOf(pdfPath.contains(localhost))); 

        if (hasSecondaryBorrow) {
            emailMap.put("secondarytitle", loan.getSecondaryUser().getTitle());
            emailMap.put("secondarylastname", loan.getSecondaryUser().getLastname());
            emailMap.put("secondaryfirstname", loan.getSecondaryUser().getFirstname());
            emailMap.put("secondaryemail", loan.getSecondaryUser().getEmail());
            if (loan.getSecondaryUser().getEoricode() != null 
                    && !loan.getSecondaryUser().getEoricode().isEmpty()) {
                emailMap.put("secondaryeoicode", loan.getSecondaryUser().getEoricode());
            }
            if (loan.getSecondaryUser().getDepartment() != null 
                    && !loan.getSecondaryUser().getDepartment().isEmpty()) {
                emailMap.put("secondarydepartment", loan.getSecondaryUser().getDepartment());
            }
            emailMap.put("secondaryinstitution", loan.getSecondaryUser().getInstitution());
            emailMap.put("secondarycountry", loan.getSecondaryUser().getAddress().getCountry());
        }

        emailMap.put("department", loan.getDepartment());
  
        StringBuilder sb = new StringBuilder();
        sb.append(loan.getUuid());
        sb.append(loanrequest);
        sb.append(loan.getId());

        StringBuilder adminSb = new StringBuilder(sb.toString());
        adminSb.append(adminPdf);

        sb.append(pdf);
        emailMap.put("summaryFile", sb.toString().trim());
        emailMap.put("adminSummaryFile", adminSb.toString().trim());
 
        TblUsers curator = dao.findOneUserByEmail(loan.getCurator());
        if (curator != null) {
            emailMap.put("outofoffice", String.valueOf(curator.getOnvacation()));
        }

        emailMap.put("curratormail", loan.getCurator());
//        emailMap.put("curratormail", "ida.li@nrm.se");
        emailMap.put("manager", loan.getManager()); 
        emailMap.put("pdfPath", documentPath);  
        emailMap.put("loanPolicy", loanPolicyPath);  
        emailMap.put("adminPath", adminPath);
        emailMap.put("servletPath", servletPath); 
        nrmMail.send(emailMap); 
    } 
}
