package se.nrm.dina.loan.admin.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import se.nrm.dina.loan.admin.config.InitialProperties;
import se.nrm.dina.mongodb.loan.vo.Loan;

/**
 *
 * @author idali
 */
@Named("pdf")
@SessionScoped
@Slf4j
public class PdfHandler implements Serializable   {
    
    private final String contentType = "application/pdf"; 
    
    private final String dash = "-";
    private final String slash = "/";
    private final String adminFile = "_admin.pdf";
    private final String loanrequest = "/loanrequest_";
     
    
    @Inject
    private  InitialProperties properities;
    
    public PdfHandler() {
        
    }
    
        
    public StreamedContent getPdfFile(Loan loan) { 
        log.info("getPdfFile");
        String path = buildFilePath(loan.getUuid(), loan.getId());
//        String path = "/Users/idali/Documents/loans/ede9301c/3927/47c6/af01/d8107cd7e153/loanrequest_ReqNo002204_admin.pdf";
//                    "/Users/idali/Documents/loans/ede9301c/3927/47c6/af01/d8107cd7e153/loanrequest_ReqNo002204_admin.pdf"
//                    "/Users/idali/Documents/loans/ede9301c/3927/47c6/af01/d8107cd7e153/loanrequest_ReqNo002204_admin.pdf"
        log.info("what is the path : {}", path);
        try { 
            File pdf = new File(path);
            
            log.info("pdf name : {}", pdf.getName());
            
            StreamedContent sc = new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
            log.info("sc ? {}", sc);
            return sc;
//            return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
    
    public StreamedContent getPdfFile1(Loan loan) { 
        log.info("getPdfFile");
        
        try { 
            File pdf = new File(buildFilePath(loan.getUuid(), loan.getId()));
            
            log.info("pdf name : {} -- {}", pdf.getName(), pdf.exists());
             
            return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
    
        
    private String buildFilePath(String uuid, String loanNamber) {
        StringBuilder sb = new StringBuilder();
        sb.append(properities.getLoanFilePath());
        sb.append(uuid.replace(dash, slash));  
        sb.append(loanrequest);
        sb.append(loanNamber);
        sb.append(adminFile); 
        return sb.toString().trim();
    }

}
