package se.nrm.dina.loan.web.controllers;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import se.nrm.dina.loan.web.filehander.LoanFileHandler;
import se.nrm.dina.loan.web.util.CommonNames;
import se.nrm.dina.loan.web.util.NameMapping; 

/**
 *
 * @author idali
 */
@Named("files")
@SessionScoped
@Slf4j
public class FileManager implements Serializable {
    
    private final String emptyString = "";
    
    private UploadedFile loanDetailFile; 
    private String loanDetailFileName;
    
    private UploadedFile projectFile; 
    private String projectFileName;
    
    private UploadedFile photoInstructionFile; 
    private String photoInstructionFileName;
    
    private UploadedFile destructiveMethodFile;
    private String destructiveMethodFileName;
    
    @Inject
    private LoanForm loan; 
    @Inject
    private LoanFileHandler fileHander; 
    @Inject
    private Message message;
    
    public FileManager() {
        
    }
    
    public void handleProjectFileUpload(FileUploadEvent event) {
        log.info("handleFileUpload : {} ", event.getFile().getFileName());

        projectFile = event.getFile();
        projectFileName = projectFile.getFileName();
        saveTempFile(projectFile);
    }
    
    public void removeProjectfile() {
        log.info("removeScPurposefile");
        
        fileHander.removeFileFromTempDirectory(projectFileName, loan.getUUID().toString()); 
        projectFile = null;
        projectFileName = null;
    }
        
    public void handleLoanDetailFileUpload(FileUploadEvent event) {
        log.info("handleFileUpload : {} ", event.getFile().getFileName());
 
        loanDetailFile = event.getFile();
        loanDetailFileName = loanDetailFile.getFileName();
        saveTempFile(loanDetailFile);
    }
    
    public void removeLoanDetailFile() {
        log.info("removefileLoanDetailFile");
 
        fileHander.removeFileFromTempDirectory(loanDetailFileName, loan.getUUID().toString()); 
        loanDetailFile = null;
        loanDetailFileName = null;
    }
    
    public void handlePhotoInstFileUpload(FileUploadEvent event) {
        log.info("handlePhotoInstFileUpload : {} -- {}", 
                event.getFile(), event.getFile().getFileName());
        
        photoInstructionFile = event.getFile();
        photoInstructionFileName = photoInstructionFile.getFileName();
        saveTempFile(photoInstructionFile); 
    }
     
    public void removePhotoInstructionFile() {
        log.info("removePhotoInstructionFile");
 
        fileHander.removeFileFromTempDirectory(photoInstructionFileName, loan.getUUID().toString()); 
        photoInstructionFile = null;
        photoInstructionFileName = null;
    }

    public void removeDestructiveMethodFile() {
        log.info("removeDestructiveMethodFile");
 
        fileHander.removeFileFromTempDirectory(destructiveMethodFileName, loan.getUUID().toString()); 
        destructiveMethodFile = null;
        destructiveMethodFileName = null;
    }
    
    public void handleDestructiveMethodFileUpload(FileUploadEvent event) {
        log.info("handleDestructiveMethodFileUpload : {} -- {}", 
                event.getFile(), event.getFile().getFileName());
        
        destructiveMethodFile = event.getFile();
        destructiveMethodFileName = destructiveMethodFile.getFileName();
        saveTempFile(destructiveMethodFile); 
    }
    
    private void saveTempFile(UploadedFile uploadFile) {
        log.info("saveTempFile");
          
        try {
            fileHander.saveTempFile(uploadFile, loan.getUUID().toString());
        } catch (IOException ex) {
            log.error(ex.getMessage());
            message.addError(emptyString, NameMapping.getMsgByKey(
                    CommonNames.UploadFileFailed, loan.isSwedish()));
        }
    }

    public UploadedFile getLoanDetailFile() {
        return loanDetailFile;
    }

    public void setLoanDetailFile(UploadedFile loanDetailFile) {
        this.loanDetailFile = loanDetailFile;
    } 

    public String getLoanDetailFileName() {
        return loanDetailFileName;
    }

    public void setLoanDetailFileName(String loanDetailFileName) {
        this.loanDetailFileName = loanDetailFileName;
    }

    public UploadedFile getProjectFile() {
        return projectFile;
    }

    public void setProjectFile(UploadedFile projectFile) {
        this.projectFile = projectFile;
    }

    public String getProjectFileName() {
        return projectFileName;
    }

    public void setProjectFileName(String projectFileName) {
        this.projectFileName = projectFileName;
    }

    public UploadedFile getPhotoInstructionFile() {
        return photoInstructionFile;
    }

    public void setPhotoInstructionFile(UploadedFile photoInstructionFile) {
        this.photoInstructionFile = photoInstructionFile;
    }

    public String getPhotoInstructionFileName() {
        return photoInstructionFileName;
    }

    public void setPhotoInstructionFileName(String photoInstructionFileName) {
        this.photoInstructionFileName = photoInstructionFileName;
    }

    public UploadedFile getDestructiveMethodFile() {
        return destructiveMethodFile;
    }

    public void setDestructiveMethodFile(UploadedFile destructiveMethodFile) {
        this.destructiveMethodFile = destructiveMethodFile;
    }

    public String getDestructiveMethodFileName() {
        return destructiveMethodFileName;
    }

    public void setDestructiveMethodFileName(String destructiveMethodFileName) {
        this.destructiveMethodFileName = destructiveMethodFileName;
    }
    
    
    
    
}
