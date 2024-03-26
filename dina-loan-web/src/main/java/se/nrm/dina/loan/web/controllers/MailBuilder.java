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
    private final String localhost = "localhost";
     
    private Map<String, String> emailMap;
    
    
    @Inject
    private NrmMail nrmMail;
     
    
    @EJB
    private AccountDao dao;
    
    public MailBuilder() {
        
    } 
    
    public void buildEmail(Loan loan, String pdfPath, boolean isSwedish, 
            boolean hasSecondaryBorrow) throws MessagingException, 
            AddressException, UnsupportedEncodingException {
        
        log.info("buildEmail");
        
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
        emailMap.put("primaryinstitution", loan.getPrimaryUser().getInstitution());
        emailMap.put("primarycountry", loan.getPrimaryUser().getAddress().getCountry());

        emailMap.put("hasPrimaryContact", String.valueOf(hasSecondaryBorrow));
        emailMap.put("isLocal", String.valueOf(pdfPath.contains(localhost))); 

        if (hasSecondaryBorrow) {
            emailMap.put("secondarytitle", loan.getSecondaryUser().getTitle());
            emailMap.put("secondarylastname", loan.getSecondaryUser().getLastname());
            emailMap.put("secondaryfirstname", loan.getSecondaryUser().getFirstname());
            emailMap.put("secondaryemail", loan.getSecondaryUser().getEmail());
            if (loan.getSecondaryUser().getDepartment() != null 
                    && !loan.getSecondaryUser().getDepartment().isEmpty()) {
                emailMap.put("secondarydepartment", loan.getSecondaryUser().getDepartment());
            }
            emailMap.put("secondaryinstitution", loan.getPrimaryUser().getInstitution());
            emailMap.put("secondarycountry", loan.getPrimaryUser().getAddress().getCountry());
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

//        String curatorEmail = null;
//        if(loan.getCurator() != null && !loan.getCurator().isEmpty()) {
//            curatorEmail = loan.getCurator();
//        } else {
//            Collection collection = (mongo.findCollection("Non scientific", "group")); 
//            if(collection != null) {
//                curatorEmail = collection.getManager();
//            }
//        }
        TblUsers curator = dao.findOneUserByEmail(loan.getCurator());
        if (curator != null) {
            emailMap.put("outofoffice", String.valueOf(curator.getOnvacation()));
        }

        emailMap.put("curratormail", loan.getCurator());
//        emailMap.put("curratormail", "ida.li@nrm.se");
        emailMap.put("manager", loan.getManager());
         
        emailMap.put("loanPolicy", pdfPath);
        
        nrmMail.send(emailMap); 
    } 
}
