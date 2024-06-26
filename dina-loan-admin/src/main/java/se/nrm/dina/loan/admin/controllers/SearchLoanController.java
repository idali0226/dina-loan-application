package se.nrm.dina.loan.admin.controllers;

import java.io.Serializable;
import java.util.ArrayList; 
import java.util.Calendar;
import java.util.Date; 
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List; 
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.loan.admin.config.InitialProperties;
import se.nrm.dina.loan.admin.util.Util;
import se.nrm.dina.mongodb.jdbc.MongoJDBC;
import se.nrm.dina.mongodb.loan.vo.Loan;

/**
 *
 * @author idali
 */
@Named(value = "search")
@SessionScoped
@Slf4j
public class SearchLoanController implements Serializable {

    private final String defaultDepartment = "Zoology";
    
    private final String dash = "-";
    private final String slash = "/";
    private final String adminFile = "_admin.pdf";
    private final String loanrequest = "/loanrequest_";
     
    private List<Loan> loans; 
    private List<Loan> filteredLoans;
    private List<String> curators;  
     
    private String loanId;
    private String status;
    private String curator;
    private String releventCollection;
    private String borrower;

    private String searchLoanId;
//    private List<String> nameList;  

    private SelectItem[] statusOptions;
    private final List<String> statusList;

    private SelectItem[] curatorOptions;
     
    private SelectItem[] collectionOptions;
    private List<String> collections;
  
    private Loan editedLoan;

    private List<String> collectionGroups;

    private Date fromDate;
    private Date toDate;
    private Date mindate;

    private String dateForSearch;

    

    private List<String> selectedDepartments;
    private List<String> selectedPurposes;
    private List<String> selectedTypes;

    private boolean selectAllDepartments;
    private boolean selectAllPurposes;
    private boolean selectAllTypes;

    private final String requestPending = "Request pending";
    private final String inProgress = "In progress";
    private final String requestDenied = "Request denied";
    private final String requestAccepted = "Request accepted";
//  private final String emailFailed = "Email failed";

    private final String emptyString = "";
    private final String select = "Select";
    private final String all = "All";
    private final String zoology = "Zoology";
    private final String botany = "Botany";
    private final String enviromental = "Environmental Specimen Bank";
    private final String geology = "Geology";
    private final String palaeobiology = "Palaeobiology";

    private final String scientificPurpose = "Scientific purpose";
    private final String educationExhibition = "Education/Exhibition";
    private final String commercialArt = "Commercial/art/other";

    private final String physical = "Physical";
    private final String photo = "Photo";
    private final String informtion = "Information";

    private final String submitDate = "submitDate";
    private final String id = "_id";
    private final String curatorString = "curator";
    private final String relevenCollection = "releventCollection";
    private final String borrowerString = "borrower";
    private final String department = "department";
    private final String purpose = "purpose";
    private final String type = "type";
    private final String initialStartDate = "2014-01-01";

    @Inject
    private MongoJDBC mongo;

    public SearchLoanController() {
        loans = new ArrayList<>();

        statusOptions = new SelectItem[5];

        statusOptions[0] = new SelectItem(emptyString, select);
        statusOptions[1] = new SelectItem(requestPending, requestPending);
        statusOptions[2] = new SelectItem(inProgress, inProgress);
        statusOptions[3] = new SelectItem(requestAccepted, requestAccepted);
        statusOptions[4] = new SelectItem(requestDenied, requestDenied);
//    statusOptions[5] = new SelectItem(emailFailed, emailFailed);

        statusList = new ArrayList<>();
        statusList.add(requestPending);
        statusList.add(inProgress);
        statusList.add(requestDenied);
        statusList.add(requestAccepted);
//    statusList.add(emailFailed);

        selectedDepartments = new ArrayList<>();
        selectedDepartments.add(zoology);

        selectedPurposes = new ArrayList<>();
        selectedPurposes.add(all);
        selectedPurposes.add(scientificPurpose);
        selectedPurposes.add(educationExhibition);
        selectedPurposes.add(commercialArt);

        selectedTypes = new ArrayList<>();
        selectedTypes.add(all);
        selectedTypes.add(physical);
        selectedTypes.add(photo);
        selectedTypes.add(informtion);

        selectAllDepartments = false;
        selectAllPurposes = true;
        selectAllTypes = true;

        dateForSearch = submitDate;

        mindate = Util.getStartDate();
    }

    
    @Inject
    private InitialProperties properties;
        
    @PostConstruct
    public void init() {
        log.info("init");
        if (loans == null || loans.isEmpty()) {
            loans = mongo.findAllLoans(); 
            log.info("loans : {}", loans.size()); 
        } 

        if (curators == null || curators.isEmpty()) {
//            curators = new ArrayList();
            curators = loans.stream() 
                    .map(l -> l.getCurator())
                    .filter(c -> c != null)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            curatorOptions = new SelectItem[curators.size() + 1];
            curatorOptions[0] = new SelectItem(emptyString, select);

            IntStream.range(0, curators.size())
                    .forEach(i -> {
                        curatorOptions[i + 1] = 
                                new SelectItem(curators.get(i), curators.get(i));
                    });
        }

        if (collections == null || collections.isEmpty()) {
//            collections = new ArrayList<>();
            collections = mongo.findAllCollectionNameListByDepartmentName(defaultDepartment);

            collectionOptions = new SelectItem[collections.size() + 1];
            collectionOptions[0] = new SelectItem(emptyString, select);
            IntStream.range(0, collections.size())
                    .forEach(i -> {
                        collectionOptions[i + 1] = new SelectItem(collections.get(i), collections.get(i));
                    });

            collections.add(0, emptyString);
        }
    }
    
    public String getAdminPdf(Loan loan) { 
        return buildFilePath(loan.getUuid(), loan.getId());
    }
    
            
    private String buildFilePath(String uuid, String id) { 
        StringBuilder sb = new StringBuilder(); 
        sb.append(properties.getAdminPdfPath());
        sb.append(uuid.replace(dash, slash));  
        sb.append(loanrequest); 
        sb.append(id);
        sb.append(adminFile); 
        return sb.toString().trim(); 
    }

    
    public void refreshLoans() {
        log.info("refreshLoans : {}", loans.size()); 
        searchFilterLoans();
    }
 
    public void filterLoansByDepartment() {
        log.info("filterLoansByDepartment: {}", selectedDepartments);

        if (selectAllDepartments) {
            if (selectedDepartments.contains(all)) {
                selectedDepartments.remove(all);
            } else {
                selectedDepartments.clear();
            }
            selectAllDepartments = false;
        } else {
            if (selectedDepartments.contains(all)) {
                selectedDepartments = new ArrayList<>();
                selectedDepartments.add(all);
                selectedDepartments.add(botany);
                selectedDepartments.add(enviromental);
                selectedDepartments.add(geology);
                selectedDepartments.add(palaeobiology);
                selectedDepartments.add(zoology);

                selectAllDepartments = true;
            } else if (selectedDepartments.size() == 5) {
                selectedDepartments.add(all);
                selectAllDepartments = true;
            }
        } 
        searchFilterLoans();
    }
    
    private void searchFilterLoans() { 
        log.info("searchFilterLoans");
        Map<String, List<String>> conditions = new HashMap<>();

        //TODO:  only zoology department is enabled.
        if (!selectedDepartments.contains(all)) {
            conditions.put(department, selectedDepartments);
        }

        if (!selectedPurposes.contains(all)) {
            conditions.put(purpose, selectedPurposes);
        }

        if (!selectedTypes.contains(all)) {
            conditions.put(type, selectedTypes);
        }

        boolean searchForDate = true;
        String strFromDate = null;
        String strToDate = null;

        if (fromDate == null && toDate == null) {
            searchForDate = false;
        } else {
            if (fromDate == null) {
                strFromDate = initialStartDate;
            } else {
                strFromDate = Util.dateToString(fromDate);
            }

            if (toDate == null) {
                strToDate = Util.getTodayAsString();
            } else {
                strToDate = Util.dateToString(toDate);
            }
        }

        loans = mongo.findLoansWithQueryMap(conditions, dateForSearch, strFromDate, strToDate, searchForDate)
                .stream()
                .filter(l -> !l.isEmailFailed())
                .collect(Collectors.toList());
        filteredLoans = null;
    }
     
    public void cancel() {
        loanId = null;
        curator = null;
        releventCollection = null;
        borrower = null;
    }

    public void search() {

        Map<String, String> queryMap = new LinkedHashMap<>();

        if (loanId != null && !loanId.isEmpty()) {
            queryMap.put(id, loanId.trim());
        }

        if (curator != null && !curator.isEmpty()) {
            queryMap.put(curatorString, curator.trim());
        }

        if (releventCollection != null && !releventCollection.isEmpty()) {
            queryMap.put(relevenCollection, releventCollection.trim());
        }

        if (borrower != null && !borrower.isEmpty()) {
            queryMap.put(borrowerString, borrower);
        }
    }

    public void onRowEdit(Loan loan) {
        log.info("onRowEdit: {} -- {}", loan, loan.getReleventCollection());

        if (!loan.getStatus().equals(requestPending)) {
            String strDate = Util.getTodayAsString();
            loan.setCloseDate(strDate);
        } else {
            loan.setCloseDate(emptyString);
        }

        if (loan.getReleventCollection() == null) {
            mongo.updateLoan(loan, true);
        } else {
            mongo.updateLoan(loan);
        }
    }

    public List<String> completeText(String query) {

        return collectionGroups.stream()
                .filter(name -> name.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
 
    public void filterLoansByType() {
        log.info("filterLoansByType: {}", selectedTypes);

        if (selectAllTypes) {
            if (selectedTypes.contains(all)) {
                selectedTypes.remove(all);
            } else {
                selectedTypes.clear();
            }
            selectAllTypes = false;
        } else {
            if (selectedTypes.contains(all)) {
                selectedTypes = new ArrayList<>();
                selectedTypes.add(all);
                selectedTypes.add(physical);
                selectedTypes.add(photo);
                selectedTypes.add(informtion);
                selectAllTypes = true;
            } else if (selectedTypes.size() == 3) {
                selectedTypes.add(all);
                selectAllTypes = true;
            }
        }
        searchFilterLoans();
    }

    public void filterLoansByPurpose() {
        log.info("filterLoansByPurpose: {}", selectedPurposes);

        if (selectAllPurposes) {
            if (selectedPurposes.contains(all)) {
                selectedPurposes.remove(all);
            } else {
                selectedPurposes.clear();
            }
            selectAllPurposes = false;
        } else {
            if (selectedPurposes.contains(all)) {
                selectedPurposes = new ArrayList<>();
                selectedPurposes.add(all);
                selectedPurposes.add(scientificPurpose);
                selectedPurposes.add(educationExhibition);
                selectedPurposes.add(commercialArt);
                selectAllPurposes = true;
            } else if (selectedPurposes.size() == 3) {
                selectedPurposes.add(all);
                selectAllPurposes = true;
            }
        }
        searchFilterLoans();
    }
 
    public void handleDateSelect() {
        log.info("handleDateSelect");

        mindate = fromDate;

        searchFilterLoans();
    }

    public void dateSearch() {
        log.info("dateSearch : {} -- {}", dateForSearch, fromDate + " -- " + toDate);

        searchFilterLoans();
    }

    public List<String> getSelectedDepartments() {
        return selectedDepartments;
    }

    public void setSelectedDepartments(List<String> selectedDepartments) {
        this.selectedDepartments = selectedDepartments;
    }

    public List<String> getSelectedPurposes() {
        return selectedPurposes;
    }

    public void setSelectedPurposes(List<String> selectedPurposes) {
        this.selectedPurposes = selectedPurposes;
    }

    public List<String> getSelectedTypes() {
        return selectedTypes;
    }

    public void setSelectedTypes(List<String> selectedTypes) {
        this.selectedTypes = selectedTypes;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurator() {
        return curator;
    }

    public void setCurator(String curator) {
        this.curator = curator;
    }

    public String getReleventCollection() {
        return releventCollection;
    }

    public void setReleventCollection(String releventCollection) {
        this.releventCollection = releventCollection;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
    
    

    public String getSearchLoanId() {
        return searchLoanId;
    }

    public void setSearchLoanId(String searchLoanId) {
        this.searchLoanId = searchLoanId;
    }

    public Loan getEditedLoan() {
        return editedLoan;
    }

    public void setEditedLoan(Loan editedLoan) {
        this.editedLoan = editedLoan;
    }

    public String getDateForSearch() {
        return dateForSearch;
    }

    public void setDateForSearch(String dateForSearch) {
        this.dateForSearch = dateForSearch;
    }

    public SelectItem[] getStatusOptions() {
        return statusOptions;
    }

    public void setStatusOptions(SelectItem[] statusOptions) {
        this.statusOptions = statusOptions;
    }

    public Date getMaxDate() {
        Calendar now = Calendar.getInstance();
        return now.getTime();
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getMindate() {
        return mindate;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public List<Loan> getFilteredLoans() {
        return filteredLoans;
    }

    public void setFilteredLoans(List<Loan> filteredLoans) {
        this.filteredLoans = filteredLoans;
    }

    public SelectItem[] getCuratorOptions() {
        return curatorOptions;
    }

    public void setCuratorOptions(SelectItem[] curatorOptions) {
        this.curatorOptions = curatorOptions;
    }

    public List<String> getCurators() {
        return curators;
    }

    public void setCurators(List<String> curators) {
        this.curators = curators;
    }

    public SelectItem[] getCollectionOptions() {
        return collectionOptions;
    }

    public void setCollectionOptions(SelectItem[] collectionOptions) {
        this.collectionOptions = collectionOptions;
    }

    public List<String> getCollections() {
        return collections;
    }

    public void setCollections(List<String> collections) {
        this.collections = collections;
    } 
}
