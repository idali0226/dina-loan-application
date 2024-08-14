package se.nrm.dina.loan.web.controllers;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.loan.web.beans.StyleBean;

/**
 *
 * @author idali
 */
@Named("navigate")
@SessionScoped
@Slf4j
public class Navigation implements Serializable {
    
    private ExternalContext externalContext;
    
    private final String conatctPath = "/faces/clientpages/contact.xhtml";
    private final String homePath = "/faces/clientpages/home.xhtml";
    private final String aboutPath = "/faces/clientpages/about.xhtml";
    private final String policiesPath = "/faces/clientpages/policy.xhtml";
    private final String departmentsPath = "/faces/clientpages/departments.xhtml";
    private final String purposePath = "/faces/clientpages/purpose.xhtml";
    private final String scientificTypePath = "/faces/clientpages/scientificType.xhtml";
    private final String scientificPurposeDescPath = "/faces/clientpages/scPurposeDescription.xhtml";
    private final String collectionsPath = "/faces/clientpages/collections.xhtml";
    private final String specimenPath = "/faces/clientpages/specimen.xhtml";
    private final String desctructivePath = "/faces/clientpages/destructive.xhtml";
    private final String photoDetailPath = "/faces/clientpages/photoDetail.xhtml";
    private final String citesPath = "/faces/clientpages/cites.xhtml";
    private final String borrowerPath = "/faces/clientpages/borrower.xhtml";
    private final String nonDestructiveBorrowerPath = "/faces/clientpages/nonDestructiveLoanBorrow.xhtml";
    private final String scNonDestructiveBorrowerPath = "/faces/clientpages/scNonDestructiveLoanBorrow.xhtml";
    
    private final String previewPath = "/faces/clientpages/preview.xhtml"; 
    private final String informationLoanReviewPath = "/faces/clientpages/informationLoanPreview.xhtml"; 
    private final String scNonPhysicalLoanReviewPath = "/faces/clientpages/scNonPhysicalLoanPreview.xhtml"; 
    
    private final String scientificLoanReviewPath = "/faces/clientpages/scientificLoanPreview.xhtml"; 
    
    private final String scInformationReviewPath = "/faces/clientpages/scInformationLoanPreview.xhtml"; 
    
    private final String confirmationPath = "/faces/clientpages/confirmation.xhtml";
    
    private final String educationInformationPath = "/faces/clientpages/educationInformation.xhtml";
    
    private final String purposeOfUsePath = "/faces/clientpages/purposeOfUse.xhtml";
    private final String loanDetailPath = "/faces/clientpages/loanDetail.xhtml";
    
    private final String storagePath = "/faces/clientpages/storage.xhtml";
    
    
    
    
    private final String commercialInformationPath = "/faces/clientpages/commercialInformation.xhtml";
    private final String commercialLoanTypePath = "/faces/clientpages/loantype.xhtml";
    
     
    private final String mailServerDownPath = "/faces/clientpages/submitFailPage.xhtml";
    private final String requestFailedPath = "/faces/clientpages/requestFailPage.xhtml";
    
    private String formView;
    
     
    
    @Inject
    private StyleBean style;
        
    public Navigation() {
        
    }
    
    public void gotoHomePage() {
        log.info("gotoHomePage");
        style.setCurrentTab(1);
         
        formView = homePath;
        redirectPage(homePath);
    }
    
    public void gotoAboutPage() {
        log.info("gotoAboutPage");
        style.setCurrentTab(2);
         
        redirectPage(aboutPath);
    }
    
    public void gotoPolicyPage() {
        log.info("gotoPolicyPage");
        style.setCurrentTab(3);
         
        redirectPage(policiesPath);
    } 
    
    public void gotoContactPage() {
        log.info("gotoContactPage");
        
        style.setCurrentTab(4);
        redirectPage(conatctPath);
    }
        
    public void gotoDepartmentsPage() {
        log.info("gotoDepartmentsPage"); 
        
        formView = departmentsPath;
        redirectPage(departmentsPath);
    }
    
    public void gotoPurposePage() {
        log.info("gotoPurposePage"); 
        
        formView = purposePath;
        redirectPage(purposePath);
    }
    
    
    
    
    // commercial purpose    
    public void gotoCommercialInformationPath() {
        log.info("gotoCommercialInformationPath"); 
        
        formView = commercialInformationPath;
        redirectPage(commercialInformationPath);
    }
    
    public void gotoCommercialLoanTypePath() {
        log.info("gotoLoanTypePath"); 
        
        formView = commercialLoanTypePath;
        redirectPage(commercialLoanTypePath);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public void gotoScientificTypePage() {
        log.info("gotoScientificTypePage"); 
        
        formView = scientificTypePath;
        redirectPage(scientificTypePath);
    }
    
    public void gotoScientificPurposeDescPage() {
        log.info("gotoScientificPurposeDescPage"); 
        
        formView = scientificPurposeDescPath;
        redirectPage(scientificPurposeDescPath);
    }
    
    public void gotoCollectionsPage() {
        log.info("gotoCollectionsPage"); 
        
        formView = collectionsPath;
        redirectPage(collectionsPath);
    }
    
    public void gotoSpecimenPage() {
        log.info("gotoSpecimenPage"); 
        
        formView = specimenPath;
        redirectPage(specimenPath);
    }
    
    public void gotoDesctructivePage() {
        log.info("gotoDesctructivePage"); 
        
        formView = desctructivePath;
        redirectPage(desctructivePath);
    }
        
    public void gotoPhotoDetailPage() {
        log.info("gotoPhotoDetailPage"); 
        
        formView = photoDetailPath;
        redirectPage(photoDetailPath);
    }
    
    public void gotoCitesPage() {
        log.info("gotoCitesPage"); 
        
        formView = citesPath;
        redirectPage(citesPath);
    }
    
    public void gotoBorrowerPage() {
        log.info("gotoBorrowerPage"); 
        
        formView = borrowerPath;
        redirectPage(borrowerPath);
    }
    
    public void gotoNonDestructiveBorrowerPage() {
        log.info("gotoNonDestructiveBorrowerPage"); 
        
        formView = nonDestructiveBorrowerPath;
        redirectPage(nonDestructiveBorrowerPath);
    }
    
    public void gotoScNonDestructiveBorrowerPage() {
        log.info("gotoScNonDestructiveBorrowerPage"); 
        
        formView = scNonDestructiveBorrowerPath;
        redirectPage(scNonDestructiveBorrowerPath);
    }
    
    public void gotoPreviewPage() {
        log.info("gotoPreviewPage"); 
        
        formView = previewPath;
        redirectPage(previewPath);
    }
    
    public void gotoInformationLoanPreviewPage() {
        log.info("gotoInformationLoanPreviewPage"); 
        
        formView = informationLoanReviewPath;
        redirectPage(informationLoanReviewPath);
    }
    
    public void gotoScNonPhysicalLoanPreviewPage() {
        log.info("gotoScNonPhysicalLoanPreviewPage"); 
        
        formView = scNonPhysicalLoanReviewPath;
        redirectPage(scNonPhysicalLoanReviewPath);
    }
    
    public void gotoScInformationLoanPreviewPage() {
        log.info("gotoScInformationLoanPreviewPage"); 
        
        formView = scInformationReviewPath;
        redirectPage(scInformationReviewPath);
    }
        
    public void gotoScientificLoanPreviewPage() {
        log.info("gotoScientificLoanPreviewPage"); 
        
        formView = scientificLoanReviewPath;
        redirectPage(scientificLoanReviewPath);
    }
    
    
    
        
    
    public void gotoConfirmaetionPag() {
        log.info("gotoConfirmaetionPag"); 
        
        formView = confirmationPath;
        redirectPage(confirmationPath);
    }
    
    public void gotoEducationInformationPath() {
        log.info("gotoEducationInformationPath"); 
        
        formView = educationInformationPath;
        redirectPage(educationInformationPath);
    }
    
        
    public void gotoPurposeOfUsePath() {
        log.info("gotoPurposeOfUsePath"); 
        
        formView = purposeOfUsePath;
        redirectPage(purposeOfUsePath);
    }

    public void gotoLoanDetailPage() {
        log.info("gotoLoanDetailPage");
        redirectPage(loanDetailPath);
    }
    
    public void gotoStoragePage() {
        log.info("gotoStoragePage");
        
        formView = storagePath;
        redirectPage(storagePath);
    }
    
    public void gotoMailServerDownPage() {
        log.info("gotoMailServerDownPage");
        
        formView = mailServerDownPath;
        redirectPage(mailServerDownPath);
    }
    
    public void gotoRequestFailedPage() {
        log.info("gotoRequestFailedPage");
        
        formView = requestFailedPath;
        redirectPage(requestFailedPath);
    }
    
    public void gotoFormPage() {
        if(formView == null) {
            formView = homePath;
        }
        style.setCurrentTab(1);
        redirectPage(formView);
    }
        
    private void redirectPage(String path) {
 
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        log.info("path... {} -- {}", externalContext.getRequestContextPath(), path);
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + path);
        } catch (IOException ex) {
        }
    }
}
