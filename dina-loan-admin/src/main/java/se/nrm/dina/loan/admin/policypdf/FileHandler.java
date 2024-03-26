package se.nrm.dina.loan.admin.policypdf;

import java.io.File; 
import java.io.IOException;
import java.io.InputStream; 
import java.io.Serializable; 
import javax.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author idali
 */
@Stateless
@Slf4j
public class FileHandler implements Serializable {
 
    public FileHandler() { 
    }

    public void saveFile(UploadedFile uploadFile, String filePath) { 
        log.info("saveFile : {} -- {}", uploadFile.getFileName(), filePath);

        removeOldFile(filePath);

        File targetFile = new File(filePath);
        
        InputStream initialStream = null;
        try {
            initialStream = uploadFile.getInputstream();
            FileUtils.copyInputStreamToFile(initialStream, targetFile); 
        } catch (IOException ex) {
            log.error(ex.getMessage());
        } finally {
            try {
                if (initialStream != null) {
                    initialStream.close();
                }
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
    }

    private void removeOldFile(String filePath) {
        log.info("removeOldFile");

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
