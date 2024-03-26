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
@Named("policies")
@SessionScoped
@Slf4j
public class PolicyFiles implements Serializable  {
    
    private final String contentType = "application/pdf"; 
    
    @Inject
    private  InitialProperties properities;
    
//    public StreamedContent getScientificPolicyPdfFilePage2() throws IOException {
//        
////        File file = new File(properities.getScientificPolicyPath() + pdfPage2);
////        byte[] content = FileUtils.readFileToByteArray(file); 
//        
//        File pdf = new File(properities.getScientificPolicyPath());
//        log.info("pdf name : {}", pdf.getName());
////        InputStream is = new FileInputStream(new File(properities.getScientificPolicyPath()));
//         
////        log.info("is file exist : {}", pdf.exists()); 
//  
////        return DefaultStreamedContent.builder()
////                .contentType(contentType)
////                .name("scientifc.pdf")
////                .stream(() -> is)
////                .build();
//   
//
//          
//        return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
//    }
    
    
    public StreamedContent getPdfFile(Loan loan) { 
        log.info("getPdfFile");
        String path = "/Users/idali/Documents/loans/ede9301c/3927/47c6/af01/d8107cd7e153/loanrequest_ReqNo002204_admin.pdf"; 
        try { 
            File pdf = new File(path);
            
            log.info("pdf name : {}", pdf.getName());
            
            return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
    

    public StreamedContent getScientificPolicyPdfFile() { 
        log.info("getScientificPolicyPdfFile");
        
        try { 
            File pdf = new File(properities.getScientificPolicyPath());
            
            log.info("pdf name : {}", pdf.getName());
            
            return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
    
    public StreamedContent getEducationPolicyPdfFile() {
        log.info("getEducationPolicyPdfFile");
         
        try { 
            File pdf = new File(properities.getEducationalPolicyPath());
            
            log.info("pdf name : {}", pdf.getName());
             
            return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        }
        return null;
    } 
}
