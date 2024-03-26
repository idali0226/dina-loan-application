package se.nrm.dina.loan.web.controllers;

import java.io.Serializable; 
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException; 
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.loan.web.beans.BreadCrumbBean;
import se.nrm.dina.loan.web.util.Department;
import se.nrm.dina.loan.web.util.LoanPurpose;
import se.nrm.dina.loan.web.util.RequestType;
/**
 *
 * @author idali
 */
@Named(value = "viewBean")
@RequestScoped
@Slf4j
public class ViewManager implements Serializable {

    private final String educationExhibition = "educationexhibition";
     
    @Inject
    private BreadCrumbBean breadCrumb;
    @Inject
    private Navigation navigator;
    @Inject
    private LoanForm loanform;
    @Inject
    private InformationLoanForm otherForm;
    @Inject
    private ScientificLoanForm scientificForm;

    public ViewManager() {

    }

    // InformationLoan
    public void gotoLoanType() {
        log.info("gotoLoanType");

        breadCrumb.setNextItem(breadCrumb.getInformationItem(), breadCrumb.getLoanTypeItem());
        navigator.gotoCommercialLoanTypePath();
    }
        
    public void gotoLoanDetails() {
        log.info("gotoLoanDetails");
        
        if(loanform.getPurpose().equalsIgnoreCase(educationExhibition)) {
            breadCrumb.setNextItem(breadCrumb.getPurposeOfUesItem(), breadCrumb.getLoanDetailItem());
        } else {
            breadCrumb.setNextItem(breadCrumb.getLoanTypeItem(), breadCrumb.getLoanDetailItem());
        } 
        navigator.gotoLoanDetailPage(); 
    }
 
    // Educational Loan
     public void gotoPurposeOfUsePage() {
        log.info("gotoPurposeOfUsePage");

        breadCrumb.setNextItem(breadCrumb.getInformationItem(), breadCrumb.getPurposeOfUesItem());
        navigator.gotoPurposeOfUsePath(); 
    }
    
    
    
    public void gotoLoanDetailNaxtPage() {
        if(loanform.getPurpose().equalsIgnoreCase(educationExhibition)) {
            gotoStoragePage();
        } else {
            breadCrumb.setNextItem(breadCrumb.getLoanDetailItem(), breadCrumb.getContactItem()); 
            navigator.gotoNonDestructiveBorrowerPage();
        }
    }
     
    public void gotoStoragePage() {
        breadCrumb.setNextItem(breadCrumb.getLoanDetailItem(),breadCrumb.getStorageItem());
        navigator.gotoStoragePage();
    }
     
    // Scientific loan
    public void requestTypeChanged() {
        log.info("requestTypeChanged" );

        String requestType = scientificForm.getRequestType();
        log.info(requestType);
        switch (requestType) {
            case "Photo":
                breadCrumb.setPhotoElement();
                break;
            case "Information":
                breadCrumb.setInformationElement();
                break;
            default:
                breadCrumb.setPhiscalElement();
                break;
        }
    }
     
    
    public void departmentChanged() {
        log.info("departmentChanged");

        loanform.departmentChanged();
        String department = loanform.getDepartment();
        boolean isImplemented = Department.Zoology.isZoology(department);
 
        breadCrumb.setIsImplementDepartmentPath(isImplemented);
    }

    public void selectpurpose() {
        log.info("selectpurpose");

        LoanPurpose loanPurpose;
        switch (loanform.getPurpose()) {
            case "commercialartother":
                loanPurpose = LoanPurpose.CommercialArtOther;
                break;
            case "educationexhibition":
                loanPurpose = LoanPurpose.EducationExhibition;
                break;
            default:
                loanPurpose = LoanPurpose.ScientificPurpose;
                break;
        }
        breadCrumb.resetNavigationPathForPurpose(loanPurpose);
    }

    public void goToHome() {
        log.info("goToHome");

        loanform.resetData(true); 
        if(LoanPurpose.ScientificPurpose.isScientificPurpose(loanform.getPurpose())) {
            scientificForm.resetData();
        } else {
            otherForm.resetData();
        }
        
        breadCrumb.resetNavigationPathToHomePage();
        navigator.gotoHomePage();
    }
    
    public void gotoBackFormView() {
        navigator.gotoFormPage();
    }

    public void goToDepartments() {
        log.info("goToDepartments");

        breadCrumb.setNextItem(breadCrumb.getHomeItem(), breadCrumb.getDepartmentItem());
        navigator.gotoDepartmentsPage();
    }

    public void goToPurpose() {
        log.info("goToPurpose");

        breadCrumb.setNextItem(breadCrumb.getDepartmentItem(), breadCrumb.getPurposeItem());
        navigator.gotoPurposePage();
    } 
    
    public void gotoPurposeNextPage() {
        log.info("gotoPurposeNextPage");

        switch (loanform.getPurpose()) {
            case "commercialartother":
                breadCrumb.setNextItem(breadCrumb.getPurposeItem(), breadCrumb.getInformationItem());
                navigator.gotoCommercialInformationPath();
                break;
            case "educationexhibition":
                breadCrumb.setNextItem(breadCrumb.getPurposeItem(), breadCrumb.getInformationItem());
                navigator.gotoEducationInformationPath();
                break;
            default:
                breadCrumb.setNextItem(breadCrumb.getPurposeItem(), breadCrumb.getRequestTypeItem());
                navigator.gotoScientificTypePage();
                break;
        }
    }
    
    public void gotoCollections() {
        log.info("gotoCollections");

        breadCrumb.setNextItem(breadCrumb.getProjectItem(), breadCrumb.getCollectionItem());
        navigator.gotoCollectionsPage();
    }
    
    public void gotoSpecimen() {
        log.info("gotoSpecimen");

        breadCrumb.setNextItem(breadCrumb.getCollectionItem(), breadCrumb.getSpecimentsItem());
        navigator.gotoSpecimenPage();
    }
    
    public void gotoScientificDetailPage() {
        log.info("gotoScientificDetailPage");

        if(scientificForm.isIsPhoto()) {
            breadCrumb.setNextItem(breadCrumb.getSpecimentsItem(), breadCrumb.getPhotoItem());
            navigator.gotoPhotoDetailPage();
        } else if(scientificForm.isIsInformation()) {
            breadCrumb.setNextItem(breadCrumb.getSpecimentsItem(), breadCrumb.getContactItem());
            navigator.gotoScNonDestructiveBorrowerPage();
        } else {
            breadCrumb.setNextItem(breadCrumb.getSpecimentsItem(), breadCrumb.getDesctructiveItem());
            navigator.gotoDesctructivePage();
        } 
    }
        
    public void gotoScDescPurpose() {
        log.info("gotoDescPurpose : {}", scientificForm.getRequestType());
         
        if (RequestType.Information.isInformation(scientificForm.getRequestType())) {
            breadCrumb.setNextItem(breadCrumb.getRequestTypeItem(), breadCrumb.getCollectionItem()); 
            navigator.gotoCollectionsPage(); 
        } else {
            breadCrumb.setNextItem(breadCrumb.getRequestTypeItem(), breadCrumb.getProjectItem());
            navigator.gotoScientificPurposeDescPage(); 
        }
    }
    
    public void gotoCitesPage() {
        log.info("gotoCitesPage");

        if(scientificForm.isIsPhoto()) {
            breadCrumb.setNextItem(breadCrumb.getPhotoItem(), breadCrumb.getCitesItem());
        } else {
            breadCrumb.setNextItem(breadCrumb.getDesctructiveItem(), breadCrumb.getCitesItem());
        } 
        navigator.gotoCitesPage();
    }

    
    public void gotoBorrowerPage() {
        log.info("gotoBorrowerPage : {}", loanform.getPurpose());
         
        breadCrumb.setNextItem(breadCrumb.getCitesItem(), breadCrumb.getContactItem()); 
        if(scientificForm.isIsPhoto()) { 
            navigator.gotoScNonDestructiveBorrowerPage();
        } else { 
            navigator.gotoBorrowerPage();
        } 
    }
    
    public void gotoEducationalBorrowPage() {
        breadCrumb.setNextItem(breadCrumb.getStorageItem(), breadCrumb.getContactItem()); 
        navigator.gotoNonDestructiveBorrowerPage();
    }
    
    public void gotoInformationLoanBorrowPage() {
        breadCrumb.setNextItem(breadCrumb.getLoanDetailItem(), breadCrumb.getContactItem()); 
        navigator.gotoNonDestructiveBorrowerPage();
    }
    
//    public void gotoNonScientificLoanBorrowPage() {
//        switch (loanform.getPurpose()) {
//            case "commercialartother":
//                breadCrumb.setNextItem(breadCrumb.getLoanDetailItem(), breadCrumb.getContactItem()); 
//                break;
//            case "educationexhibition":
//                breadCrumb.setNextItem(breadCrumb.getStorageItem(), breadCrumb.getContactItem()); 
//                break;
//            default:
//                breadCrumb.setNextItem(breadCrumb.getCitesItem(), breadCrumb.getContactItem()); 
//                break;
//        }  
//        navigator.gotoNonDestructiveBorrowerPage();
//    }
    
    public void gotoPreviewPage() {
        log.info("gotoPreviewPage");

        breadCrumb.setNextItem(breadCrumb.getContactItem(), breadCrumb.getReviewItem());
        navigator.gotoScientificLoanPreviewPage();
    }
    
    public void gotoInformationLoanPreviewPage() {
        log.info("gotoInformationLoanPreviewPage");

        breadCrumb.setNextItem(breadCrumb.getContactItem(), breadCrumb.getReviewItem());
        navigator.gotoInformationLoanPreviewPage();
    }
    
    
    public void gotoScNonPhysicalLoanPreviewPage() {
        
         breadCrumb.setNextItem(breadCrumb.getContactItem(), breadCrumb.getReviewItem());
         
        if(RequestType.Information.isInformation(scientificForm.getRequestType())) {  
          
           navigator.gotoScInformationLoanPreviewPage();  
        } else {
            navigator.gotoScientificLoanPreviewPage();
        }  
    }
    
    
    

    public void gotoInformationConfirmationPage() {
        try {
            otherForm.submit();
            gotoConfirmationPage();
        } catch (MessagingException ex) {
            log.error(ex.getMessage());
            gotoMailServerDownPage();
        } 
    }
    
    public void gotoScientificConfirmationPage() {
        try {
            scientificForm.submit();
            gotoConfirmationPage();
        } catch (MessagingException ex) {
            log.error(ex.getMessage());
            gotoMailServerDownPage();
        } 
    }
    
    
    
    public void gotoMailServerDownPage() {
        navigator.gotoMailServerDownPage();
    }
    
    public void gotoConfirmationPage() {
        navigator.gotoConfirmaetionPag();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    public void navigateIndexDepartment() {
        breadCrumb.setManuItem(breadCrumb.getDepartmentItem());
        navigator.gotoDepartmentsPage();
    }

    public void navigateIndexPurpose() {
        breadCrumb.setManuItem(breadCrumb.getPurposeItem());
        navigator.gotoPurposePage();
    }

    public void navigateIndexRequestType() {
        breadCrumb.setManuItem(breadCrumb.getRequestTypeItem());
        navigator.gotoScientificTypePage();
    }

    public void navigateIndexInformationPage() {
        breadCrumb.setManuItem(breadCrumb.getInformationItem());
        if (loanform.getPurpose().equalsIgnoreCase(educationExhibition)) {
            navigator.gotoEducationInformationPath();
        } else {
            navigator.gotoCommercialInformationPath();
        }
    }



    public void navigateIndexWithRequestType() {
        log.info("navigateIndexWithRequestType : {}", scientificForm.getRequestType());
        
        if(RequestType.Information.isInformation(scientificForm.getRequestType())) {
            breadCrumb.setManuItem(breadCrumb.getRequestTypeItem());
            navigator.gotoScientificTypePage();
        } else {
            breadCrumb.setManuItem(breadCrumb.getProjectItem());
            navigator.gotoScientificPurposeDescPage();
        } 
    }
    
    public void navigateIndexProject() {
        breadCrumb.setManuItem(breadCrumb.getProjectItem());
        navigator.gotoScientificPurposeDescPage();
    }

    public void navigateIndexPurposeOfUse() {
        breadCrumb.setManuItem(breadCrumb.getPurposeOfUesItem());
        navigator.gotoPurposeOfUsePath();
    }
    
    public void navigateIndexLoanWithPurpose() {
        if(loanform.getPurpose().equalsIgnoreCase(educationExhibition)) {
            navigateIndexPurposeOfUse();
        } else {
            navigateIndexLoanType();
        }
    }


    
    public void navigateIndexLoanType() {
        breadCrumb.setManuItem(breadCrumb.getLoanTypeItem());
        navigator.gotoCommercialLoanTypePath();
    }

    public void navigateIndexCollection() {
        breadCrumb.setManuItem(breadCrumb.getCollectionItem());
        navigator.gotoCollectionsPage();
    }

    public void navigateIndexLoanDetail() {
        breadCrumb.setManuItem(breadCrumb.getLoanDetailItem());
        navigator.gotoLoanDetailPage();
    }

    public void navigateIndexStorage() {
        breadCrumb.setManuItem(breadCrumb.getStorageItem());
        navigator.gotoStoragePage();
    }

    public void navigateIndexSpecimens() {
        breadCrumb.setManuItem(breadCrumb.getSpecimentsItem());
        navigator.gotoSpecimenPage();
    }

    public void navigateIndexDestructive() {
            
        if(scientificForm.isIsPhoto()) {
            breadCrumb.setManuItem(breadCrumb.getPhotoItem());
            navigator.gotoPhotoDetailPage();
        } else {
            breadCrumb.setManuItem(breadCrumb.getDesctructiveItem());
            navigator.gotoDesctructivePage();
        } 
    }
    
    public void navigateIndexPhotoDetail() {
        breadCrumb.setManuItem(breadCrumb.getPhotoItem());
        navigator.gotoPhotoDetailPage();
    }

    public void navigateIndexCites() {
        
        breadCrumb.setManuItem(breadCrumb.getCitesItem());
        navigator.gotoCitesPage();
    }

    public void navigateIndexBorrow() {
        breadCrumb.setManuItem(breadCrumb.getContactItem());
        navigator.gotoBorrowerPage();
    }
    
    public void navigateIndexBorrowWithPurpose() {
        
        breadCrumb.setManuItem(breadCrumb.getContactItem()); 
        switch (loanform.getPurpose()) {
            case "commercialartother":
                navigator.gotoNonDestructiveBorrowerPage();
                break;
            case "educationexhibition":
                navigator.gotoNonDestructiveBorrowerPage();
                break;
            default:
                 navigator.gotoBorrowerPage();
                break;
        }   
    }

    public void navigateIndexReview() { 
        
        breadCrumb.setManuItem(breadCrumb.getReviewItem()); 
        
        if(loanform.isScientificLoan()) {
            navigator.gotoPreviewPage();
        } else {
            navigator.gotoInformationLoanPreviewPage();
        }
//        switch (loanform.getPurpose()) {
//            case "commercialartother":
//                navigator.gotoInformationLoanPreviewPage();
//                break;
//            case "educationexhibition":
//                navigator.gotoInformationLoanPreviewPage();
//                break;
//            default:
//                 navigator.gotoPreviewPage();
//                break;
//        }    
    }



    public void about() {
        log.info("about");
        navigator.gotoAboutPage();
    }

    public void readloanpolicies() {
        log.info("readloanpolicies");

        navigator.gotoPolicyPage();
    }

    public void contact() {
        log.info("contact");
        navigator.gotoContactPage();
    }
}
