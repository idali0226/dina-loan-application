package se.nrm.dina.loan.web.beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.menu.DefaultMenuItem;
import se.nrm.dina.loan.web.controllers.Languages;
import se.nrm.dina.loan.web.util.BreadCrumbElement;
import se.nrm.dina.loan.web.util.LoanPurpose;

/**
 *
 * @author idali
 */
@Named(value = "breadcrumb")
@SessionScoped
@Slf4j
public class BreadCrumbBean implements Serializable {

    private final String unActiveTab = "unactiveTab";
    private final String activeTab = "activeTab";

    private final DefaultMenuItem notImplementItem;
    
    private final DefaultMenuItem homeItem;
    private final DefaultMenuItem departmentItem;
    private final DefaultMenuItem purposeItem;
    private final DefaultMenuItem requestTypeItem;
    private final DefaultMenuItem projectItem;
    private final DefaultMenuItem collectionItem;
    private final DefaultMenuItem specimentsItem;
    private final DefaultMenuItem desctructiveItem;
    private final DefaultMenuItem photoItem;
    private final DefaultMenuItem citesItem;
    private final DefaultMenuItem contactItem;
    private final DefaultMenuItem reviewItem;
    
    private final DefaultMenuItem informationItem;
    private final DefaultMenuItem purposeOfUesItem;
    private final DefaultMenuItem loanDetailItem;
    private final DefaultMenuItem storageItem;
    
    private final DefaultMenuItem loanTypeItem;

    private DefaultMenuItem activeItem;

    private boolean navigationPathEnabled;

    @Inject
    private Languages language;

    public BreadCrumbBean() {

        homeItem = new DefaultMenuItem();
        departmentItem = new DefaultMenuItem();
        purposeItem = new DefaultMenuItem();
        requestTypeItem = new DefaultMenuItem();
        projectItem = new DefaultMenuItem();
        collectionItem = new DefaultMenuItem();
        specimentsItem = new DefaultMenuItem();
        desctructiveItem = new DefaultMenuItem();
        photoItem = new DefaultMenuItem();
        citesItem = new DefaultMenuItem();
        contactItem = new DefaultMenuItem();
        reviewItem = new DefaultMenuItem();
        
        informationItem = new DefaultMenuItem();
        purposeOfUesItem = new DefaultMenuItem();
        loanDetailItem = new DefaultMenuItem();
        storageItem = new DefaultMenuItem();
        notImplementItem = new DefaultMenuItem();
        
        loanTypeItem = new DefaultMenuItem();

        homeItem.setId(BreadCrumbElement.Home.getText());
        homeItem.setValue(BreadCrumbElement.Home.getMainMenuTextByLocale(false));
        homeItem.setDisabled(false);
        homeItem.setStyle(unActiveTab);

        departmentItem.setId(BreadCrumbElement.Department.getText());
        departmentItem.setValue(BreadCrumbElement.Department.getMainMenuTextByLocale(false));
        departmentItem.setDisabled(true);
        departmentItem.setStyle(unActiveTab);

        purposeItem.setValue(BreadCrumbElement.Purpose.getMainMenuTextByLocale(false));
        purposeItem.setId(BreadCrumbElement.Purpose.getText());
        purposeItem.setDisabled(true);
        purposeItem.setStyle(unActiveTab);

        requestTypeItem.setValue(BreadCrumbElement.RequestType.getMainMenuTextByLocale(false));
        requestTypeItem.setId(BreadCrumbElement.RequestType.getText());
        requestTypeItem.setDisabled(true);
        requestTypeItem.setStyle(unActiveTab);

        projectItem.setValue(BreadCrumbElement.Project.getMainMenuTextByLocale(false));
        projectItem.setId(BreadCrumbElement.Project.getText());
        projectItem.setDisabled(true);
        projectItem.setStyle(unActiveTab);

        collectionItem.setValue(BreadCrumbElement.Collection.getMainMenuTextByLocale(false));
        collectionItem.setId(BreadCrumbElement.Collection.getText());
        collectionItem.setDisabled(true);
        collectionItem.setStyle(unActiveTab);

        specimentsItem.setValue(BreadCrumbElement.Speciments.getMainMenuTextByLocale(false));
        specimentsItem.setId(BreadCrumbElement.Speciments.getText());
        specimentsItem.setDisabled(true);
        specimentsItem.setStyle(unActiveTab);

        desctructiveItem.setValue(BreadCrumbElement.Desctructive.getMainMenuTextByLocale(false));
        desctructiveItem.setId(BreadCrumbElement.Desctructive.getText());
        desctructiveItem.setDisabled(true);
        desctructiveItem.setStyle(unActiveTab);
        
        photoItem.setValue(BreadCrumbElement.Photo.getMainMenuTextByLocale(false));
        photoItem.setId(BreadCrumbElement.Photo.getText());
        photoItem.setDisabled(true);
        photoItem.setStyle(unActiveTab);
        photoItem.setRendered(false);

        citesItem.setValue(BreadCrumbElement.Cites.getMainMenuTextByLocale(false));
        citesItem.setId(BreadCrumbElement.Cites.getText());
        citesItem.setDisabled(true);
        citesItem.setStyle(unActiveTab);

        contactItem.setValue(BreadCrumbElement.Contact.getMainMenuTextByLocale(false));
        contactItem.setId(BreadCrumbElement.Contact.getText());
        contactItem.setDisabled(true);
        contactItem.setStyle(unActiveTab);

        reviewItem.setValue(BreadCrumbElement.Review.getMainMenuTextByLocale(false));
        reviewItem.setId(BreadCrumbElement.Review.getText());
        reviewItem.setDisabled(true);
        reviewItem.setStyle(unActiveTab);
        
        
        informationItem.setValue(BreadCrumbElement.LoanInformation.getEduMenuTextByLocale(false)); 
        informationItem.setId(BreadCrumbElement.LoanInformation.getText());
        informationItem.setDisabled(true);
        informationItem.setStyle(unActiveTab);
        informationItem.setRendered(false);
        
        purposeOfUesItem.setValue(BreadCrumbElement.PurposeOfUse.getEduMenuTextByLocale(false)); 
        purposeOfUesItem.setId(BreadCrumbElement.PurposeOfUse.getText());
        purposeOfUesItem.setDisabled(true);
        purposeOfUesItem.setStyle(unActiveTab);
        purposeOfUesItem.setRendered(false);
        
        loanDetailItem.setValue(BreadCrumbElement.LoanDetail.getEduMenuTextByLocale(false)); 
        loanDetailItem.setId(BreadCrumbElement.LoanDetail.getText());
        loanDetailItem.setDisabled(true);
        loanDetailItem.setStyle(unActiveTab);
        loanDetailItem.setRendered(false);
        
        storageItem.setValue(BreadCrumbElement.Storage.getEduMenuTextByLocale(false)); 
        storageItem.setId(BreadCrumbElement.Storage.getText());
        storageItem.setDisabled(true);
        storageItem.setStyle(unActiveTab);
        storageItem.setRendered(false);
         
        loanTypeItem.setValue(BreadCrumbElement.LoanType.getArtMenuTextByLocale(false)); 
        loanTypeItem.setId(BreadCrumbElement.LoanType.getText());
        loanTypeItem.setDisabled(true);
        loanTypeItem.setStyle(unActiveTab);
        loanTypeItem.setRendered(false);
        
        notImplementItem.setValue(
                    BreadCrumbElement.NoImplement.getMainMenuTextByLocale(false));
        notImplementItem.setId(BreadCrumbElement.NoImplement.getText());
        notImplementItem.setDisabled(true);
        notImplementItem.setStyle(unActiveTab);
        notImplementItem.setRendered(false);
   
        navigationPathEnabled = false;
        activeItem = homeItem;
    }
    
    /**
     * To change navigation path language
     *
     * @param isSwedish 
     */
    public void resetLocale(boolean isSwedish) { 
        log.info("resetLocale : {}", isSwedish);

        homeItem.setValue(BreadCrumbElement.Home.getMainMenuTextByLocale(isSwedish));
        departmentItem.setValue(BreadCrumbElement.Department.getMainMenuTextByLocale(isSwedish));
        purposeItem.setValue(BreadCrumbElement.Purpose.getMainMenuTextByLocale(isSwedish));
        requestTypeItem.setValue(BreadCrumbElement.RequestType.getMainMenuTextByLocale(isSwedish));
        projectItem.setValue(BreadCrumbElement.Project.getMainMenuTextByLocale(isSwedish));
        collectionItem.setValue(BreadCrumbElement.Collection.getMainMenuTextByLocale(isSwedish));
        specimentsItem.setValue(BreadCrumbElement.Speciments.getMainMenuTextByLocale(isSwedish));
        desctructiveItem.setValue(BreadCrumbElement.Desctructive.getMainMenuTextByLocale(isSwedish));
        photoItem.setValue(BreadCrumbElement.Photo.getMainMenuTextByLocale(isSwedish));
        citesItem.setValue(BreadCrumbElement.Cites.getMainMenuTextByLocale(isSwedish));
        contactItem.setValue(BreadCrumbElement.Contact.getMainMenuTextByLocale(isSwedish));
        reviewItem.setValue(BreadCrumbElement.Review.getMainMenuTextByLocale(isSwedish));
        
        loanTypeItem.setValue(BreadCrumbElement.LoanType.getArtMenuTextByLocale(isSwedish));
        informationItem.setValue(BreadCrumbElement.LoanInformation.getEduMenuTextByLocale(isSwedish));
        storageItem.setValue(BreadCrumbElement.Storage.getEduMenuTextByLocale(isSwedish));
        loanDetailItem.setValue(BreadCrumbElement.LoanDetail.getEduMenuTextByLocale(isSwedish));
        purposeOfUesItem.setValue(BreadCrumbElement.PurposeOfUse.getEduMenuTextByLocale(isSwedish));
        informationItem.setValue(BreadCrumbElement.LoanInformation.getEduMenuTextByLocale(isSwedish));
        
        notImplementItem.setValue(BreadCrumbElement.NoImplement.getMainMenuTextByLocale(isSwedish)); 
    }
    
    public void setIsImplementDepartmentPath(boolean isImplemented) {
        log.info("setNotImplementDepartmentPath : {}", isImplemented);
 
        notImplementItem.setRendered(!isImplemented);
        
        requestTypeItem.setRendered(isImplemented);
        purposeItem.setRendered(isImplemented);
        projectItem.setRendered(isImplemented);
        collectionItem.setRendered(isImplemented);
        specimentsItem.setRendered(isImplemented);
        desctructiveItem.setRendered(isImplemented);
        photoItem.setRendered(false);
        citesItem.setRendered(isImplemented);
        contactItem.setRendered(isImplemented);
        reviewItem.setRendered(isImplemented);
        
        loanTypeItem.setRendered(false);
        informationItem.setRendered(false);
        storageItem.setRendered(false);
        loanDetailItem.setRendered(false);
        purposeOfUesItem.setRendered(false);
        informationItem.setRendered(false);

        purposeItem.setDisabled(true);
        requestTypeItem.setDisabled(true);
        projectItem.setDisabled(true);
        collectionItem.setDisabled(true);
        specimentsItem.setDisabled(true);
        desctructiveItem.setDisabled(true);
        photoItem.setDisabled(true);
        citesItem.setDisabled(true);
        contactItem.setDisabled(true);
        reviewItem.setDisabled(true);
        
        loanTypeItem.setDisabled(true);
        informationItem.setDisabled(true);
        storageItem.setDisabled(true);
        loanDetailItem.setDisabled(true);
        purposeOfUesItem.setDisabled(true);
        informationItem.setDisabled(true);
    }
    
     /**
     * To reset navigation path to home page
     */
    public void resetNavigationPathToHomePage() {
        log.info("resetNavigationPathToHomePage");

        homeItem.setStyle(unActiveTab);
        activeItem = homeItem;

        departmentItem.setStyle(unActiveTab);
        purposeItem.setStyle(unActiveTab);
        requestTypeItem.setStyle(unActiveTab);
        projectItem.setStyle(unActiveTab);
        collectionItem.setStyle(unActiveTab);
        specimentsItem.setStyle(unActiveTab);
        desctructiveItem.setStyle(unActiveTab);
        photoItem.setStyle(unActiveTab);
        citesItem.setStyle(unActiveTab);
        contactItem.setStyle(unActiveTab);
        reviewItem.setStyle(unActiveTab);

        departmentItem.setDisabled(true);
        purposeItem.setDisabled(true);
        requestTypeItem.setDisabled(true);
        projectItem.setDisabled(true);
        collectionItem.setDisabled(true);
        specimentsItem.setDisabled(true);
        desctructiveItem.setDisabled(true);
        photoItem.setDisabled(true);
        citesItem.setDisabled(true);
        contactItem.setDisabled(true);
        reviewItem.setDisabled(true);

        departmentItem.setRendered(true);
        purposeItem.setRendered(true);
        requestTypeItem.setRendered(true);
        projectItem.setRendered(true);
        collectionItem.setRendered(true);
        specimentsItem.setRendered(true);
        desctructiveItem.setRendered(true);
        photoItem.setRendered(false);
        citesItem.setRendered(true);
        contactItem.setRendered(true);
        reviewItem.setRendered(true);
        
        loanTypeItem.setRendered(false);
        informationItem.setRendered(false);
        storageItem.setRendered(false);
        loanDetailItem.setRendered(false);
        purposeOfUesItem.setRendered(false);
        informationItem.setRendered(false);
        
        loanTypeItem.setDisabled(true);
        informationItem.setDisabled(true);
        storageItem.setDisabled(true);
        loanDetailItem.setDisabled(true);
        purposeOfUesItem.setDisabled(true);
        informationItem.setDisabled(true);
        
        notImplementItem.setRendered(false);
 
        navigationPathEnabled = false;
    }
    
    public void resetNavigationPathForPurpose(LoanPurpose loanPurpose) { 
        log.info("resetNavigationPath : {}", loanPurpose);

//        boolean isSwedish = language.isIsSwedish();
        
        informationItem.setRendered(false);
        purposeOfUesItem.setRendered(false);
        loanDetailItem.setRendered(false);
        storageItem.setRendered(false);
        loanTypeItem.setRendered(false);

        desctructiveItem.setRendered(false);
        photoItem.setRendered(false);
        citesItem.setRendered(false);
        requestTypeItem.setRendered(false);
        projectItem.setRendered(false);
        collectionItem.setRendered(false);
        specimentsItem.setRendered(false);
        desctructiveItem.setRendered(false);
        citesItem.setRendered(false);

        if(loanPurpose.isScientificPurpose()) {
            desctructiveItem.setRendered(true);
            citesItem.setRendered(true);
            requestTypeItem.setRendered(true);
            projectItem.setRendered(true);
            collectionItem.setRendered(true);
            specimentsItem.setRendered(true);
            desctructiveItem.setRendered(true);
            citesItem.setRendered(true);
        } else {
            informationItem.setRendered(true); 
            loanDetailItem.setRendered(true);
             
            if(loanPurpose.isEducation()) {
                purposeOfUesItem.setRendered(true);
                storageItem.setRendered(true);
            } else {
                loanTypeItem.setRendered(true);
            } 
        }
        
        requestTypeItem.setDisabled(true);
        projectItem.setDisabled(true);
        collectionItem.setDisabled(true);
        specimentsItem.setDisabled(true);
        desctructiveItem.setDisabled(true);
        photoItem.setDisabled(true);
        citesItem.setDisabled(true);
        contactItem.setDisabled(true);
        reviewItem.setDisabled(true);
        
        informationItem.setDisabled(true);
        loanDetailItem.setDisabled(true);
        
        purposeOfUesItem.setDisabled(true);
        storageItem.setDisabled(true);
        loanTypeItem.setDisabled(true);

//        if (loanPurpose.isEducation()) {
//            log.info("is educational");
//            informationItem.setRendered(true);
//            purposeOfUesItem.setRendered(true);
//            loanDetailItem.setRendered(true);
//            storageItem.setRendered(true);
//            
//            desctructiveItem.setRendered(false);
//            citesItem.setRendered(false); 
//            requestTypeItem.setRendered(false);
//            projectItem.setRendered(false);
//            collectionItem.setRendered(false);
//            specimentsItem.setRendered(false);
//            desctructiveItem.setRendered(false);
//            citesItem.setRendered(false); 
//        } else if (loanPurpose.isCommercial()) {
//            log.info("is comercial");
//            informationItem.setRendered(true);
//            loanTypeItem.setRendered(true);
//            loanDetailItem.setRendered(true);
//             
//            desctructiveItem.setRendered(false);
//            citesItem.setRendered(false); 
//            requestTypeItem.setRendered(false);
//            projectItem.setRendered(false);
//            collectionItem.setRendered(false);
//            specimentsItem.setRendered(false);
//            desctructiveItem.setRendered(false);
//            citesItem.setRendered(false); 
//        } else {
//            log.info("is scientific");
//            citesItem.setRendered(true);
//            contactItem.setRendered(true);
//            reviewItem.setRendered(true);
//
//            requestTypeItem.setValue(BreadCrumbElement.RequestType.getMainMenuTextByLocale(isSwedish));
//            projectItem.setValue(BreadCrumbElement.Project.getMainMenuTextByLocale(isSwedish));
//            collectionItem.setValue(BreadCrumbElement.Collection.getMainMenuTextByLocale(isSwedish));
//            specimentsItem.setValue(BreadCrumbElement.Speciments.getMainMenuTextByLocale(isSwedish));
//            desctructiveItem.setValue(BreadCrumbElement.Desctructive.getMainMenuTextByLocale(isSwedish));
//            citesItem.setValue(BreadCrumbElement.Cites.getMainMenuTextByLocale(isSwedish));
//            contactItem.setValue(BreadCrumbElement.Contact.getMainMenuTextByLocale(isSwedish));
//            reviewItem.setValue(BreadCrumbElement.Review.getMainMenuTextByLocale(isSwedish));
//        }

//        requestTypeItem.setDisabled(true);
//        projectItem.setDisabled(true);
//        collectionItem.setDisabled(true);
//        specimentsItem.setDisabled(true);
//        desctructiveItem.setDisabled(true);
//        citesItem.setDisabled(true);
//        contactItem.setDisabled(true);
//        reviewItem.setDisabled(true);
    }
    
   
    
    public void setNextItem(DefaultMenuItem current, DefaultMenuItem next) {
        current.setStyle(unActiveTab);
        current.setDisabled(false);
        next.setDisabled(true);
        next.setStyle(activeTab);
        activeItem = next;
    }
    
    public void setManuItem(DefaultMenuItem item) {
        log.info("setManuItem 1 : {}", item.getId());
        item.setStyle(activeTab);
        item.setDisabled(true);
        disableOldActiveElement();
        activeItem = item;
    }

    private void disableOldActiveElement() {
        log.info("activeItem : {}", activeItem.getId());
        switch (activeItem.getId()) {
            case "home":
                resetNavigationPathToHomePage();
            case "department":
                departmentItem.setStyle(unActiveTab);
                departmentItem.setDisabled(false);
                break;
            case "purpose":
                purposeItem.setStyle(unActiveTab);
                purposeItem.setDisabled(false);
                break;
            case "requesttype":
                requestTypeItem.setStyle(unActiveTab);
                requestTypeItem.setDisabled(false);
                break;
            case "project":
                projectItem.setStyle(unActiveTab);
                projectItem.setDisabled(false);
                break;
            case "collection":
                collectionItem.setStyle(unActiveTab);
                collectionItem.setDisabled(false);
                break;
            case "speciments":
                specimentsItem.setStyle(unActiveTab);
                specimentsItem.setDisabled(false);
                break;
            case "desctructive":
                desctructiveItem.setStyle(unActiveTab);
                desctructiveItem.setDisabled(false);
                break;
            case "photo":
                photoItem.setStyle(unActiveTab);
                photoItem.setDisabled(false);
                break;
            case "cites":
                citesItem.setStyle(unActiveTab);
                citesItem.setDisabled(false);
                break;
            case "contact":
                contactItem.setStyle(unActiveTab);
                contactItem.setDisabled(false);
                break;
            case "review":
                reviewItem.setStyle(unActiveTab);
                reviewItem.setDisabled(false);
                break;
            case "loaninformation":
                informationItem.setStyle(unActiveTab);
                informationItem.setDisabled(false);
                break;
            case "purposeofuse":
                purposeOfUesItem.setStyle(unActiveTab);
                purposeOfUesItem.setDisabled(false);
                break;
            case "storage":
                storageItem.setStyle(unActiveTab);
                storageItem.setDisabled(false);
                break;
            case "loantype":
                loanTypeItem.setStyle(unActiveTab);
                loanTypeItem.setDisabled(false);
                break;
            case "loandetail":
                loanDetailItem.setStyle(unActiveTab);
                loanDetailItem.setDisabled(false);
                break;
        }
    }
    
     /**
     * Set navigation path to photo text
     */
    public void setPhotoElement() {
        log.info("setPhotoElement");
        
        desctructiveItem.setRendered(false);
        projectItem.setRendered(true);
        photoItem.setRendered(true);
        citesItem.setRendered(true);  
    }

    /**
     * Set navigation path to photo text
     */
    public void setInformationElement() {
        projectItem.setRendered(false);
        desctructiveItem.setRendered(false);
        
        photoItem.setRendered(false);
        citesItem.setRendered(false);
    }

    /**
     * Set navigation path to physical text
     */
    public void setPhiscalElement() {
        desctructiveItem.setValue(BreadCrumbElement.Desctructive.getMainMenuTextByLocale(language.isIsSwedish()));
        desctructiveItem.setRendered(true);
        photoItem.setRendered(false);
        projectItem.setRendered(true);
        photoItem.setRendered(false);
        citesItem.setRendered(true);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void setNext(DefaultMenuItem item) {
        log.info("setNext : {}", item);

        if (item == null) {
            resetNavigationPathToHomePage();
        } else {
            navigationPathEnabled = true;

            item.setStyle(unActiveTab);
            item.setDisabled(false);
            setNextItem(item);
        }
    }

    private void setNextItem(DefaultMenuItem item) {
        log.info("setNextItem : {}", item.getId());
        switch (item.getId()) {
            case "home":
                departmentItem.setDisabled(true);
                departmentItem.setStyle(activeTab);
                activeItem = departmentItem;
                break;
            case "department":
                purposeItem.setDisabled(true);
                purposeItem.setStyle(activeTab);
                activeItem = purposeItem;
                break;
            case "purpose":
                requestTypeItem.setDisabled(true);
                requestTypeItem.setStyle(activeTab);
                activeItem = requestTypeItem;
                break;
            case "requestType":
                projectItem.setDisabled(true);
                projectItem.setStyle(activeTab);
                activeItem = projectItem;
                break;
            case "project":
                collectionItem.setDisabled(true);
                collectionItem.setStyle(activeTab);
                activeItem = collectionItem;
                break;
            case "collection":
                specimentsItem.setDisabled(true);
                specimentsItem.setStyle(activeTab);
                activeItem = specimentsItem;
                break;
            case "specimemts":
                desctructiveItem.setDisabled(true);
                desctructiveItem.setStyle(activeTab);
                activeItem = desctructiveItem;
                break;
            case "desctructive":
                citesItem.setDisabled(true);
                citesItem.setStyle(activeTab);
                activeItem = citesItem;
                break;
            case "cites":
                contactItem.setDisabled(true);
                contactItem.setStyle(activeTab);
                activeItem = contactItem;
                break;
            case "contact":
                reviewItem.setDisabled(true);
                reviewItem.setStyle(activeTab);
                activeItem = reviewItem;
                break;
            case "review":
                break;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

   

   


    

    

    private void setMainPathValue() {
        boolean isSwedish = language.isIsSwedish();
        purposeItem.setValue(BreadCrumbElement.Purpose.getMainMenuTextByLocale(isSwedish));
        requestTypeItem.setValue(BreadCrumbElement.RequestType.getMainMenuTextByLocale(isSwedish));
        projectItem.setValue(BreadCrumbElement.Project.getMainMenuTextByLocale(isSwedish));
        collectionItem.setValue(BreadCrumbElement.Collection.getMainMenuTextByLocale(isSwedish));
        specimentsItem.setValue(BreadCrumbElement.Speciments.getMainMenuTextByLocale(isSwedish));
        desctructiveItem.setValue(BreadCrumbElement.Desctructive.getMainMenuTextByLocale(isSwedish));
        citesItem.setValue(BreadCrumbElement.Cites.getMainMenuTextByLocale(isSwedish));
        contactItem.setValue(BreadCrumbElement.Contact.getMainMenuTextByLocale(isSwedish));
        reviewItem.setValue(BreadCrumbElement.Review.getMainMenuTextByLocale(isSwedish));
    }
    
//    private void setEducationPathValue() {
//        boolean isSwedish = language.isIsSwedish();
//        purposeItem.setValue(BreadCrumbElement.Purpose.getMainMenuTextByLocale(isSwedish));
//        requestTypeItem.setValue(BreadCrumbElement.RequestType.getMainMenuTextByLocale(isSwedish));
//        projectItem.setValue(BreadCrumbElement.Project.getMainMenuTextByLocale(isSwedish));
//        collectionItem.setValue(BreadCrumbElement.Collection.getMainMenuTextByLocale(isSwedish));
//        specimentsItem.setValue(BreadCrumbElement.Speciments.getMainMenuTextByLocale(isSwedish));
//        desctructiveItem.setValue(BreadCrumbElement.Desctructive.getMainMenuTextByLocale(isSwedish));
//        citesItem.setValue(BreadCrumbElement.Cites.getMainMenuTextByLocale(isSwedish));
//        contactItem.setValue(BreadCrumbElement.Contact.getMainMenuTextByLocale(isSwedish));
//        reviewItem.setValue(BreadCrumbElement.Review.getMainMenuTextByLocale(isSwedish));
//    }

   

    

    

    public void enableManuItem(DefaultMenuItem item) {
        log.info("disableManuItem : {}", item.getId());
        item.setStyle(unActiveTab);
        item.setDisabled(false);
    }

    public void disableManuItem(DefaultMenuItem item) {
        log.info("disableManuItem : {}", item.getId());
        item.setStyle(unActiveTab);
        item.setDisabled(false);
    }

    

    public void setManuItem(DefaultMenuItem item, int numOfPages) {
        log.info("setManuItem : {}", item);
        item.setStyle(activeTab);
        item.setDisabled(true);

        switch (activeItem.getId()) {
            case "home":
                navigationPathEnabled = false;
            case "department":
                departmentItem.setStyle(unActiveTab);
                departmentItem.setDisabled(false);
                break;
            case "purpose":
                purposeItem.setStyle(unActiveTab);
                purposeItem.setDisabled(false);
                break;
            case "requestType":
                requestTypeItem.setStyle(unActiveTab);
                requestTypeItem.setDisabled(false);
                break;
            case "project":
                projectItem.setStyle(unActiveTab);
                projectItem.setDisabled(false);
                break;
            case "collection":
                collectionItem.setStyle(unActiveTab);
                collectionItem.setDisabled(false);
                break;
            case "speciments":
                specimentsItem.setStyle(unActiveTab);
                specimentsItem.setDisabled(false);
                break;
            case "desctructive":
                desctructiveItem.setStyle(unActiveTab);
                desctructiveItem.setDisabled(false);
                break;
            case "cites":
                citesItem.setStyle(unActiveTab);
                citesItem.setDisabled(false);
                break;
            case "contact":
                contactItem.setStyle(unActiveTab);
                contactItem.setDisabled(false);
                break;
            case "review":
                reviewItem.setStyle(unActiveTab);
                reviewItem.setDisabled(false);
                break;
        }
        activeItem = item;
    }
    
     public void setPrevious(DefaultMenuItem item) {
        log.info("setPrevious : {}", item.getId());

        navigationPathEnabled = true;

        item.setStyle(unActiveTab);
        item.setDisabled(false);
        setPreviousItem(item);
    }

    private void setPreviousItem(DefaultMenuItem item) {
        log.info("setPreviousItem : {}", item.getId());

        switch (item.getId()) {
            case "home":
            case "department":
                navigationPathEnabled = false;
                break;
            case "purpose":
                departmentItem.setDisabled(true);
                departmentItem.setStyle(activeTab);
                activeItem = departmentItem;
                break;
            case "requesttype":
                purposeItem.setDisabled(true);
                purposeItem.setStyle(activeTab);
                activeItem = purposeItem;
                break;
            case "project":
                requestTypeItem.setDisabled(true);
                requestTypeItem.setStyle(activeTab);
                activeItem = requestTypeItem;
                break;
            case "collection":
                projectItem.setDisabled(true);
                projectItem.setStyle(activeTab);
                activeItem = projectItem;
                break;
            case "speciments":
                collectionItem.setDisabled(true);
                collectionItem.setStyle(activeTab);
                activeItem = collectionItem;
                break;
            case "descructive":
                specimentsItem.setDisabled(true);
                specimentsItem.setStyle(activeTab);
                activeItem = specimentsItem;
                break;
            case "cites":
                desctructiveItem.setDisabled(true);
                desctructiveItem.setStyle(activeTab);
                activeItem = desctructiveItem;
                break;
            case "contact":
                citesItem.setDisabled(true);
                citesItem.setStyle(activeTab);
                activeItem = citesItem;
                break;
            case "review":
                contactItem.setDisabled(true);
                contactItem.setStyle(activeTab);
                activeItem = contactItem;
                break;
        }
    }

    public DefaultMenuItem getHomeItem() {
        return homeItem;
    }

    public DefaultMenuItem getDepartmentItem() {

        return departmentItem;
    }

    public DefaultMenuItem getPurposeItem() {
        return purposeItem;
    }

    public DefaultMenuItem getRequestTypeItem() {
        return requestTypeItem;
    }

    public DefaultMenuItem getProjectItem() {
        return projectItem;
    }

    public DefaultMenuItem getCollectionItem() {
        return collectionItem;
    }

    public DefaultMenuItem getSpecimentsItem() {
        return specimentsItem;
    }

    public DefaultMenuItem getDesctructiveItem() {
        return desctructiveItem;
    }

    public DefaultMenuItem getPhotoItem() {
        return photoItem;
    }
     
    public DefaultMenuItem getCitesItem() {
        return citesItem;
    }

    public DefaultMenuItem getContactItem() {
        return contactItem;
    }

    public DefaultMenuItem getReviewItem() {
        return reviewItem;
    }

    public DefaultMenuItem getInformationItem() {
        return informationItem;
    }

    public DefaultMenuItem getPurposeOfUesItem() {
        return purposeOfUesItem;
    }

    public DefaultMenuItem getLoanDetailItem() {
        return loanDetailItem;
    }

    public DefaultMenuItem getStorageItem() {
        return storageItem;
    }

    public DefaultMenuItem getLoanTypeItem() {
        return loanTypeItem;
    }

    public DefaultMenuItem getNotImplementItem() {
        return notImplementItem;
    }
    
    

    public boolean isNavigationPathEnabled() {
        return navigationPathEnabled;
    }

    public void setNavigationPathEnabled(boolean navigationPathEnabled) {
        this.navigationPathEnabled = navigationPathEnabled;
    }

}
