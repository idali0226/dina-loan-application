/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.loan.web.filehander;

import java.io.File; 
import java.io.FileOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList; 
import java.util.Arrays;   
import java.util.List;   
import java.util.UUID;      
import javax.ejb.Stateless;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;  

/**
 *
 * @author idali
 */
@Stateless
public class FileHandler implements Serializable {
    private final static Logger logger = LoggerFactory.getLogger(FileHandler.class);
      
    private static final String FILE_LOCAL = "/Users/idali/Documents/onlineloans/loan_files/"; 
    private static final String FILE_REMOTE_LOAN = "/home/admin/wildfly-8.0.0-2/loans/";
    private static final String FILE_REMOTE_AS = "/home/admin/wildfly-8.1.0-0/loans/";
    private String host;
    private File file;  
    private final String basePath;
    
    private File tempDirectory;
     
    public FileHandler() { 
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException une) {
            logger.warn(une.getMessage());
        }
        if (inetAddress != null) {
            host = inetAddress.getHostName(); 
        }
        
        logger.info("host : {}", host);
        
        if(host.toLowerCase().contains("ida")) {
            basePath = FILE_LOCAL;
        } else if(host.contains("dina-loans")) {
            basePath = FILE_REMOTE_LOAN; 
        } else {
            basePath = FILE_REMOTE_AS;
        } 
    }

    private void buildTempDirectory(UUID uuid) { 
        tempDirectory = new File(basePath + uuid.toString());
        if(!tempDirectory.exists()) {
            tempDirectory.mkdir();
        } 
    }
    
    public void saveTempFile(UploadedFile uploadFile, UUID uuid) throws IOException {
        logger.info("saveTempFile : {} -- {}", uploadFile.getFileName(), uuid);
        
        buildTempDirectory(uuid);
        try (InputStream initialStream = uploadFile.getInputstream()) {
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);

            
            File targetFile = new File(tempDirectory, uploadFile.getFileName());
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
        }
    }
    
    public void removeFileFromTempDirectory(String fileName, UUID uuid) {
        logger.info("removeFileFromTempDirectory : {}", fileName);
  
        buildTempDirectory(uuid);
        File thisFile = new File(tempDirectory, fileName);
        if(thisFile.exists()) {
            thisFile.delete();
        } 
    } 
    
    
    public String transferFiles(UUID uuid) {

        logger.info("transferFiles");

        makeLoanDirectory(uuid); 
        buildTempDirectory(uuid);
        
        File orgFile;
        File targetFile; 
        for (File tempFile : tempDirectory.listFiles()) { 
            targetFile = new File(file, tempFile.getName()); 
            orgFile = tempFile;
            orgFile.renameTo(targetFile); 
        }
        
        removeTempDirectory(uuid); 
        return file.getPath();
    }
    
    public void removeTempDirectory(UUID uuid) {
        logger.info("removeTempDirectory {}", tempDirectory);
        
        if(tempDirectory == null || !tempDirectory.exists()) {
            buildTempDirectory(uuid);
        }
        try {
            FileUtils.cleanDirectory(tempDirectory);
            tempDirectory.delete();  
        } catch (IOException ex) {
            logger.warn(ex.getMessage());
        }
    }
       
    private void makeLoanDirectory(UUID uuid) { 
        
        StringBuilder sb = new StringBuilder(basePath); 
        file = new File(basePath); 
        List<String> subDirs = new ArrayList<>(Arrays.asList(uuid.toString().split("-"))); 
         
        subDirs.stream().forEach((subDir) -> {
            makeSubDir(subDir, sb); 
        });
        //        subDirs.forEach(element -> makeSubDir(element, sb, summaryFile));  
    }
    
    private void makeSubDir(String subDir, StringBuilder sb) {  
        sb.append(subDir);  
        sb.append("/");
        file = new File(sb.toString());
        if(!file.exists()) {
            file.mkdir(); 
        } 
    }
}
