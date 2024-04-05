package se.nrm.dina.loan.web.controllers;

import java.io.Serializable;
import java.util.ArrayList; 
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped; 
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
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
    
    
    private List<Sample> samples;
    private Sample sample;
    private List<SolrRecord> records;
    private SolrRecord selectedRecord; 
    private List<String> selectedCatalogNumbers;
      
    private String sbdiBaseUrl;
    private String sbdiSearchUrl;

    private boolean isSwedish;
    
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
    }
        
    public void searchFromSbdi(String taxa) {
        log.info("searchFromSbdi : {}", taxa);

        sbdiSearchUrl = sbdiBaseUrl + taxa;
    }
  
    public void searchWithCatalogNumber() {
        log.info("searchWithCatalogNumber : {}", sample.getCatalogNumber());

        isSwedish = form.isSwedish();
        records = new ArrayList<>();
        if (sample.getCatalogNumber().isEmpty()) {
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
            records = solr.searchByFamily(sample); 
            if (records == null || records.isEmpty()) {
                message.addInfo(emptyString, 
                        NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
            } else {
                buildSolrResult(isSwedish);
            }
        } else {
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.MissingFamily, isSwedish));
        }
    }

    public void searchWithGenus() {
        log.info("searchWithGenus");

        isSwedish = form.isSwedish();
        records = new ArrayList<>();
        if (!sample.getGenus().isEmpty()) {
            records = solr.searchByGunes(sample); 
            if (records == null || records.isEmpty()) {
                message.addInfo(emptyString, 
                        NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
            } else {
                buildSolrResult(isSwedish);
            }
        } else {
            message.addError(emptyString, 
                    NameMapping.getMsgByKey(CommonNames.MissingGenus, isSwedish));
        }
    }

    public void searchWithSpecies() {
        log.info("searchWithSpecies : {}", sample);

        isSwedish = form.isSwedish();
        records = new ArrayList<>();
        if (!sample.getSpecies().isEmpty()) {
            records = solr.searchBySpecies(sample); 
            if (records == null || records.isEmpty()) {
                message.addInfo(emptyString, 
                        NameMapping.getMsgByKey(CommonNames.NoResults, isSwedish));
            } else {
                buildSolrResult(isSwedish);
            }
        } else {
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
                        record.getFullname(), record.getLocality(), 
                        record.getCountry(), record.getCollectedYear(), 
                        record.getAuth(), record.getCollectors(), 
                        record.getType(), preparation, 
                        record.getStorageString(), emptyString);
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
                selectedRecord.getFamily(), selectedRecord.getGenus(), selectedRecord.getFullname(),
                selectedRecord.getLocality(), selectedRecord.getCountry(), 
                selectedRecord.getCollectedYear(), selectedRecord.getAuth(),
                selectedRecord.getCollectors(), selectedRecord.getType(), 
                preparation, selectedRecord.getStorageString(), emptyString);

        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('speciesDlg').hide();");
    }
 
    public void cleartaxafields() {
        log.info("cleartaxafields");
        
        isSwedish = form.isSwedish();

        sample = new Sample();
        sample.setType(NameMapping.getMsgByKey(CommonNames.PreservationTypeNotSpecified, isSwedish)); 
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

}
