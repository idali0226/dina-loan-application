package se.nrm.dina.loan.web.controllers;

import java.io.Serializable;
import java.util.ArrayList;  
import java.util.List; 
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import se.nrm.dina.loan.web.util.CommonNames;
import se.nrm.dina.loan.web.util.NameMapping; 
import se.nrm.dina.loan.web.service.SolrService; 
import se.nrm.dina.loan.web.vo.SolrRecord; 
import se.nrm.dina.mongodb.loan.vo.Sample;

/**
 *
 * @author idali
 */
@Named(value = "sampleBean")
@SessionScoped
@Slf4j
public class SampleBean implements Serializable {
    
    private final String emptyString = "";
    private final String emptySpace = " ";
    private final String notSpecifiedSv = "ej specifierad";
    private final String notSpecifiedEn = "Not specified";
    
    private final String catelogNumberKey = "+cn:";
    private final String familyKey = " +family:";
    private final String genusKey = " +genus:";
//    private final String speciesKey = " +species:";
    private final String txKey = " +tx:";
    private final String wildCard = "*";
    private final String searchAndStart = " +(";
    private final String searchAndStop = ") ";
    
    private final String replaceString = "[\\[\\](),]";
    private final String replaceChars = "(),";
    
    private final String searchCollections = " +collectionId:(163840 ev et ma va fish herps)"; 
    
//    private final String entomologyCode = "163840";
    
    
    private List<Sample> samples;
    private Sample sample;
    private List<SolrRecord> records;
    private SolrRecord selectedRecord; 
    private List<String> selectedCatalogNumbers;
      
    private String sbdiBaseUrl;
    private String sbdiSearchUrl;

    private boolean isSwedish;
    
    private String catalogNumber;
    private String family;
    private String genus;
    private String species;
     
    private boolean showSearchResult;
    
    @Inject
    private Message message;
    @Inject
    private LoanForm form;
    @Inject
    private SolrService solr;
    
    public SampleBean() {
        isSwedish = false;
        samples = new ArrayList<>();
        sample = new Sample();
        sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
    }
    
    @PostConstruct
    public void init() {  
        selectedCatalogNumbers = new ArrayList();
        sbdiBaseUrl = form.getSbdiBaseUrl();   
        showSearchResult = false;
    }
    
    public void resetLocale(boolean isSwedish) {
        log.info("resetLocale : {}", isSwedish);
        this.isSwedish = isSwedish;
        
        sample.setType(NameMapping.getMsgByKey(
                CommonNames.PreservationTypeNotSpecified, isSwedish));
        if (sample.getType().equals(notSpecifiedSv) || 
                sample.getType().equals(notSpecifiedEn)) {
            sample.setType(NameMapping.getMsgByKey(
                    CommonNames.PreservationTypeNotSpecified, isSwedish));
        }
        samples.stream().filter((samp) -> (samp.getType().equals(notSpecifiedSv) 
                || samp.getType().equals(notSpecifiedEn))).forEach((samp) -> {
            samp.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
        });
    }
    
    public void resetData() { 
        isSwedish = form.isSwedish();
        
        selectedCatalogNumbers = new ArrayList();
        samples = new ArrayList<>();
        sample = new Sample();
        sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
        
        showSearchResult = false;
    }
        
    public void searchFromSbdi(String taxa) {
        log.info("searchFromSbdi : {}", taxa);

        sbdiSearchUrl = sbdiBaseUrl + taxa;
    }
    
    private String buildSearchText() {
        log.info("buildSearchText");
          
        catalogNumber = catalogNumber.trim();
        family = family.trim();
        genus = genus.trim();
        species = species.trim();
        
        StringBuilder sb = new StringBuilder();
        if(catalogNumber != null && !catalogNumber.isEmpty()) {
            sb.append(catelogNumberKey);
            sb.append(catalogNumber);
        }
        
        if(family != null && !family.trim().isEmpty()) {
            sb.append(familyKey);
            sb.append(StringUtils.capitalize(family)); 
        }
        
        if(genus.trim() != null && !genus.isEmpty()) {
            sb.append(genusKey);
            sb.append(StringUtils.capitalize(genus));
        }
        
        if(species != null && !species.isEmpty()) {
//            species = StringUtils.capitalize(species);
            if(StringUtils.containsAny(species, replaceChars)) {
                replaceChars(species);
            }
            String[] strings = species.split(emptySpace);
            
            if (strings.length > 1) {
                sb.append(searchAndStart);
                for (String s : strings) {
                    if (!s.isEmpty()) { 
                        sb.append(txKey);
                        sb.append(wildCard);
                        sb.append(s);
                        sb.append(wildCard);
                        sb.append(emptySpace);
                    }
                }
                sb.append(searchAndStop);
            } else {
                sb.append(txKey);  
                sb.append(wildCard);
                sb.append(species); 
                sb.append(wildCard);
                sb.append(emptySpace);
            } 
        }
        if(!sb.toString().isEmpty()) {
            sb.append(searchCollections);
        }
        return sb.toString().trim();
    }
    
    private String replaceChars(String value) {  
        return value.replaceAll(replaceString, emptySpace).trim();
    }
    
    public void searchFromNrmCollections() {
        log.info("searchFromNrmCollections : {}", sample);

        showSearchResult = false;
        isSwedish = form.isSwedish();
        records = new ArrayList<>();
        String searchText = buildSearchText();
        
        log.info("searchtext : {} -- {}", searchText, searchText.isEmpty());
        if(searchText.isEmpty()) {
            sample = new Sample();
            sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.MissingSearchText, isSwedish)); 
        } else {
            records = solr.searchFromNrmCollections(StringUtils.capitalize(searchText)); 
    
            if (records == null || records.isEmpty()) {
                sample = new Sample();
                sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish));
                message.addInfo(emptyString, 
                        NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
            } else {
                log.info("number of records : {}", records.size());
                buildSolrResult(isSwedish);
            }
            
            catalogNumber = null;
            family = null;
            genus = null;
            species = null;
        } 
    }
  
    public void searchWithCatalogNumber() {
        log.info("searchWithCatalogNumber : {}", sample.getCatalogNumber());

        isSwedish = form.isSwedish();
        records = new ArrayList<>();
        if (sample.getCatalogNumber().isEmpty()) {
            sample = new Sample();
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.MissingCatNum, isSwedish));
        } else {
            records = solr.searchByCatalogNumber(sample.getCatalogNumber());
            if (records == null || records.isEmpty()) {
                sample = new Sample();
                message.addInfo(emptyString, 
                        NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
            } else {
                buildSolrResult(isSwedish);
            }
        } 
    }

    public void searchWithFamily() {
        log.info("searchWithFamily : {}", sample.getFamily());

        isSwedish = form.isSwedish();
        records = new ArrayList<>();
        if (!sample.getFamily().isEmpty()) {
            records = solr.searchByFamily(StringUtils.capitalize(sample.getFamily())); 
            
            if (records == null || records.isEmpty()) {
                sample = new Sample();
                message.addInfo(emptyString, 
                        NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
            } else {
                buildSolrResult(isSwedish);
            }
        } else {
            sample = new Sample();
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.MissingFamily, isSwedish));
        }
    }

    public void searchWithGenus() {
        log.info("searchWithGenus");

        isSwedish = form.isSwedish();
        records = new ArrayList<>();
        if (!sample.getGenus().isEmpty()) {
            records = solr.searchByGunes(StringUtils.capitalize(sample.getGenus())); 
            if (records == null || records.isEmpty()) {
                sample = new Sample();
                message.addInfo(emptyString, 
                        NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
            } else {
                buildSolrResult(isSwedish);
            }
        } else {
            sample = new Sample();
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.MissingGenus, isSwedish));
        }
    }

    public void searchWithSpecies() {
        log.info("searchWithSpecies : {}", sample);

        isSwedish = form.isSwedish();
        records = new ArrayList<>();
        if (!sample.getSpecies().isEmpty()) {
            records = solr.searchBySpecies(StringUtils.capitalize(sample.getSpecies())); 
            if (records == null || records.isEmpty()) {
                message.addInfo(emptyString, 
                        NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
                sample = new Sample();
            } else {
                buildSolrResult(isSwedish);
            }
        } else {
            sample = new Sample();
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.MissingSpecies, isSwedish));
        }
    }
    
    private void buildSolrResult(boolean isSwedish) {

        if (records.size() > 1) {  
            PrimeFaces current = PrimeFaces.current(); 
            current.executeScript("PF('speciesDlg').show();");
        } else {
            if (records.size() == 1) {
                SolrRecord record = records.get(0);
                
                String preparation = record.getPreparationString();
        
                if (preparation == null || preparation.isEmpty()) {
                    preparation = NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish);
                }
                sample = new Sample(record.getCatalogNum(), 
                        record.getFamily(), record.getGenus(), 
                        record.getFullname(), record.getSpecies(),
                        record.getLocality(), 
                        record.getCountry(), record.getCollectedYear(), 
                        record.getAuth(), record.getCollectors(), 
                        record.getType(), preparation, 
                        record.getStorageString(), emptyString);
                showSearchResult = true;
            }
        }
    }
    
    public void addTaxa() {
        log.info("addTaxa : {}", sample);
 
        isSwedish = form.isSwedish();
        if(sample.isEmptySample()) {
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.EmptySample, isSwedish));
        } else {
            boolean isDuplicated = false;
            String catNum = sample.getCatalogNumber();
            if (catNum != null && !catNum.isEmpty()) {
                if (selectedCatalogNumbers.contains(catNum)) {
                    message.addWarning(emptyString,
                            catNum + emptySpace
                            + NameMapping.getMsgByKey(CommonNames.DuplicatValue, isSwedish));
                    isDuplicated = true;
                } else {
                    selectedCatalogNumbers.add(catNum);
                }
            }
            if (!isDuplicated) {
                samples.add(sample);
                cleartaxafields();  
            } 
        } 
    }
    
    public void removeSample(Sample removedSample) {
        log.info("removeSample : {}", removedSample);

        selectedCatalogNumbers.remove(removedSample.getCatalogNumber());
        samples.remove(removedSample);
    }
    
    public void onRowSelect(SelectEvent event) {
        log.info("onRowSelect");

        isSwedish = form.isSwedish();
        selectedRecord = (SolrRecord) event.getObject();
        log.info("selected record : {}", selectedRecord);

        String preparation = selectedRecord.getPreparationString();
        if (preparation == null || preparation.isEmpty()) {
            preparation = NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish);
        }
        sample = new Sample(selectedRecord.getCatalogNum(), 
                selectedRecord.getFamily(), selectedRecord.getGenus(),
                selectedRecord.getFullname(), selectedRecord.getSpecies(), 
                selectedRecord.getLocality(), selectedRecord.getCountry(), 
                selectedRecord.getCollectedYear(), selectedRecord.getAuth(),
                selectedRecord.getCollectors(), selectedRecord.getType(), 
                preparation, selectedRecord.getStorageString(), emptyString);

        showSearchResult = true;
        
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('speciesDlg').hide();");
    }
 
    public void cleartaxafields() {
        log.info("cleartaxafields");
        
        isSwedish = form.isSwedish();

        sample = new Sample();
        sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish)); 
        
        catalogNumber = null;
        family = null;
        genus = null;
        species = null;
        showSearchResult = false;
    }
    
    public boolean isIsEmptySample() {
        return sample.isEmptySample();
    }
    
    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    } 
    
    public List<Sample> getSamples() {
        return samples;
    }
    
    
    public String getSbdiSearchUrl() {
        return sbdiSearchUrl;
    }

    public void setSbdiSearchUrl(String sbdiSearchUrl) {
        this.sbdiSearchUrl = sbdiSearchUrl;
    }

    public List<SolrRecord> getRecords() {
        return records;
    }

    public void setRecords(List<SolrRecord> records) {
        this.records = records;
    }

    public SolrRecord getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(SolrRecord selectedRecord) {
        this.selectedRecord = selectedRecord;
    } 

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
 
    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }



    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isShowSearchResult() {
        return showSearchResult;
    }
    
    
    
}
