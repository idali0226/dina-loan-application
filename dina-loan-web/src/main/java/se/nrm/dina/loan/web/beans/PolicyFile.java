package se.nrm.dina.loan.web.beans;
 
import java.io.File;
import java.io.FileInputStream; 
import java.io.IOException; 
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;   
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent; 
import se.nrm.dina.loan.web.config.InitialProperties;

/**
 *
 * @author idali
 */
@Named("policy")
@SessionScoped
@Slf4j
public class PolicyFile implements Serializable {
    
    private final String contentType = "application/pdf";
    private final String pdfPage2 = "#page=2";
    
    @Inject
    private  InitialProperties properities;
    
    public StreamedContent getScientificPolicyPdfFilePage2() throws IOException {
        
//        File file = new File(properities.getScientificPolicyPath() + pdfPage2);
//        byte[] content = FileUtils.readFileToByteArray(file); 
        
        File pdf = new File(properities.getScientificPolicyPath() );
        log.info("pdf name : {}", pdf.getName());
//        InputStream is = new FileInputStream(new File(properities.getScientificPolicyPath()));
         
//        log.info("is file exist : {}", pdf.exists()); 
  
//        return DefaultStreamedContent.builder()
//                .contentType(contentType)
//                .name("scientifc.pdf")
//                .stream(() -> is)
//                .build();
   

          
        return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
    }
    

    public StreamedContent getScientificPolicyPdfFile() throws IOException {
        
        File pdf = new File(properities.getScientificPolicyPath()); 
        log.info("pdf name : {}", pdf.getName()); 
        return new DefaultStreamedContent(new FileInputStream(pdf), contentType, pdf.getName());
    }
    
    public StreamedContent getEducationPolicyPdfFile() throws IOException {
        
         File pdf = new File(properities.getEducationalPolicyPath());
 
        log.info("is edu file exist : {}", pdf.exists());
        return new DefaultStreamedContent(new FileInputStream(pdf), contentType);
    }

}
