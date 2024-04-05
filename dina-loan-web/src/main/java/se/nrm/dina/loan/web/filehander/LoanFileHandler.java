package se.nrm.dina.loan.web.filehander;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List; 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.UploadedFile;
import se.nrm.dina.loan.web.config.InitialProperties;

/**
 *
 * @author idali
 */
@Slf4j
public class LoanFileHandler implements Serializable {

    private final String dash = "-";
    private final String slash = "/";
    
    private String tempFileDirectory;
    private String fileDirectory;
    
    @Inject
    private InitialProperties properties;

    public LoanFileHandler() {

    }
            
    @PostConstruct
    public void init() {
        log.info("init");
 
        tempFileDirectory = properties.getTempFilePath();
        fileDirectory = properties.getLoanFilePath();
    }
    
    public void saveTempFile(UploadedFile uploadFile, String uuid) throws IOException {
        log.info("saveTempFile : {} -- {}", uploadFile.getFileName(), uuid);

        InputStream initialStream = uploadFile.getInputstream();

        File targetFile = new File(
                buildTempDirectory(uuid),
                uploadFile.getFileName());

        FileUtils.copyInputStreamToFile(initialStream, targetFile);
    }
    
    public String transferFiles(String uuid) throws IOException {
        log.info("transferFiles : {}", uuid);
        
        String loanFilePath = makeLoanDirectory(uuid);
        File trgDir = new File(loanFilePath); 
        File srcDir = new File(tempFileDirectory + uuid);
        
        if(srcDir.exists()) { 
            FileUtils.copyDirectory(srcDir, trgDir); 
            removeTempDirectory(uuid);
            log.info("loan file path : {}", loanFilePath);
        } 
        return loanFilePath;
    }

//    public String transferFiles1(String uuid) {
//        log.info("transferFiles : {}", uuid);
//
//        String loanFilePath = makeLoanDirectory(uuid);
//        File tempDirectory = new File(tempFileDirectory + uuid);
//
//        log.info("loanFilePath : {} -- {}", loanFilePath, tempDirectory.getPath());
//
//        File orgFile;
//        File targetFile;
//
//        if (tempDirectory.exists()) {
//            for (File tempFile : tempDirectory.listFiles()) {
//                log.info("temp file : {}", tempFile.getName());
//
//                targetFile = new File(loanFilePath, tempFile.getName());
//                 
//
//                log.info("targetFile : {}", targetFile.getName());
//                orgFile = tempFile;
//                orgFile.renameTo(targetFile);
//
//                log.info("orgFile : {}", orgFile.getName());
//            }
//
//            removeTempDirectory(uuid);
//            log.info("loan file path : {}", loanFilePath);
//        }
//
//        return loanFilePath;
//    }

    public void removeTempDirectory(String uuid) {
        log.info("removeTempDirectory {}", tempFileDirectory);

        File tempDirectory = new File(tempFileDirectory + uuid);

        if (tempDirectory.exists()) {
            try {
                FileUtils.cleanDirectory(tempDirectory);
                tempDirectory.delete();
            } catch (IOException ex) {
                log.warn(ex.getMessage());
            }
        }
    }

    public void removeFileFromTempDirectory(String fileName, String uuid) {
        log.info("removeFileFromTempDirectory : {}", fileName);

        File thisFile = new File(buildTempDirectory(uuid), fileName);
        if (thisFile.exists()) {
            thisFile.delete();
        }
    }

 
    private String makeLoanDirectory(String uuid) {

        StringBuilder sb = new StringBuilder(fileDirectory);
//        File file = new File(fileDirectory); 

        List<String> subDirs = new ArrayList<>(
                Arrays.asList(uuid.split(dash)));

        subDirs.stream().forEach((subDir) -> {
            makeSubDir(subDir, sb);
        });

        log.info("final file path : {}", sb.toString());
        return sb.toString().trim();
        //        subDirs.forEach(element -> makeSubDir(element, sb, summaryFile));  
    }

    private void makeSubDir(String subDir, StringBuilder sb) {
        sb.append(subDir);
        sb.append(slash);
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private File buildTempDirectory(String uuid) {
        File tempDirectory = new File(tempFileDirectory + uuid);
        if (!tempDirectory.exists()) {
            tempDirectory.mkdir();
        }
        return tempDirectory;
    }

}
