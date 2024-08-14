package se.nrm.dina.loan.admin.filedownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;  
import java.io.InputStream;
import java.io.Serializable;  
import javax.activation.MimetypesFileTypeMap; 
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
@Named(value = "download") 
@SessionScoped
@Slf4j
public class FileDownloadView implements Serializable {
      
    private final String dash = "-";
    private final String slash = "/";
     
    @Inject
    private InitialProperties properties;
    
    private StreamedContent file; 
    private String filePath;
    
    public FileDownloadView() {  
    }
 
    public void downloadDestructivefile(Loan loan) {
        log.info("downloadDestructivefile : {}", loan);
        
        filePath = buildFilePath(loan.getUuid(), loan.getDestructiveFile()); 
        download(filePath, loan.getDestructiveFile());
    }
    
    public void downloadDescriptionfile(Loan loan) {
        log.info("downloadDescriptionfile : {}", loan);
        
        filePath = buildFilePath(loan.getUuid(), loan.getLoanDescriptionFile()); 
        download(filePath, loan.getLoanDescriptionFile());
    }
    
    public void downloadTypematerialfile(Loan loan) {
        log.info("downloadTypematerialfile : {}", loan);
        
        filePath = buildFilePath(loan.getUuid(), loan.getTypeMaterialFile()); 
        download(filePath, loan.getTypeMaterialFile());
    }
    
    
    public void downloadPhotoInstractionFile(Loan loan) {
        log.info("photoInstractionFile : {}", loan);
        
        filePath = buildFilePath(loan.getUuid(), loan.getPhotoInstractionFile()); 
        download(filePath, loan.getPhotoInstractionFile());
    }
    
    public void downloadEdPurposeFile(Loan loan) {
        log.info("downloadEdPurposeFile : {}", loan);
        
        filePath = buildFilePath(loan.getUuid(), loan.getEdPurposeFile()); 
        download(filePath, loan.getEdPurposeFile());
    }
    
    private void download(String filePath, String filename) {
        String mimetype = getFileType(filePath);
        log.info("mimetype : {}", mimetype);
        
        InputStream stream;
        try { 
            stream = new FileInputStream(filePath); 
            file = new DefaultStreamedContent(stream, mimetype, filename);
        } catch (FileNotFoundException ex) {
            log.warn(ex.getMessage()); 
        }
    }
    
    private String getFileType(String filePath) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        
        File f = new File(filePath);
        if(f.exists()) { 
            return mimeTypesMap.getContentType(f); 
        }
        return null;
    }
    
    
    private String buildFilePath(String uuid, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(properties.getLoanFilePath());
        sb.append(uuid.replace(dash, slash)); 
        sb.append(slash);
        sb.append(fileName); 
        return sb.toString();
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    } 
}
