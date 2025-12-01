package se.nrm.dina.loan.web.service;

import java.io.IOException;
import java.io.Serializable;  
import java.util.List; 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery; 
import org.apache.solr.client.solrj.SolrServerException; 
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse; 
import se.nrm.dina.loan.web.config.InitialProperties;
import se.nrm.dina.loan.web.vo.SolrRecord; 

/**
 *
 * @author idali
 */ 
@Slf4j
public class SolrService implements Serializable {
   
    // private final String searchCollections = " +collectionId:(163840 ev et ma va fish herps)";
    
//    private final static String EXCLUDED_COLLECTIONS = " -cln:(262144 294912 458752 491521)";

  private final String searchCollections = " +collectionCode:(NHRS ev et MA AV PI HE";
 
    private final int size = 1000;
    private SolrQuery solrQuery;
    private QueryRequest request;
    private QueryResponse response;
 
    private final String catelogNumberKey = "+cn:";
    private final String familyKey = "+family:";
    private final String genusKey = "+genus:";
    private final String speciesKey = "+species:";
    private final String wildCard = "*";
    private final String searchAndStart = "+(";
    private final String searchAndStop = ") ";
    
    private final String emptySpace = " ";
//    private final String comma = ",";
//    private final String parenthesesStart = "(";
//    private final String parenthesesEnd = ")";
    private final String replaceChars = "(),";
    
//    private String searchCollectionText;
    
    
    private String username;
    private String password;
    
    private SolrClient client;
    
    @Inject
    private InitialProperties properties;


    public SolrService() {  
    }
    
    @PostConstruct
    public void init() {
        log.info("init from search...");
        client = new HttpSolrClient.Builder(properties.getSolrUrl()).build();
        username = properties.getSolrUsername();
        password = properties.getSolrPassword(); 
    }
    
    public List<SolrRecord> searchFromNrmCollections(String text) {
        log.info("searchFromNrmCollections : {}", text);
        
        solrQuery = new SolrQuery();
        solrQuery.setQuery(text); 
        solrQuery.setRows(size);
        request = new QueryRequest(solrQuery);
        request.setBasicAuthCredentials(username, password); 
           
        try { 
            response = request.process(client); 
            if (response.getResults().getNumFound() > 0) {
                return response.getBeans(SolrRecord.class);
            }  
        } catch (SolrServerException | IOException ex) {            
            log.error(ex.getMessage());
            return null;
        }     
        return null;
    }
    
    
    public List<SolrRecord> searchByCatalogNumber(String catalognumber) {
        log.info("searchCatalogNumber :{}", catalognumber);
 
        String searchText = catelogNumberKey + catalognumber + searchCollections;
        log.info("search text : {}", searchText);
         
        solrQuery = new SolrQuery();
        solrQuery.setQuery(searchText); 
        request = new QueryRequest(solrQuery);
        request.setBasicAuthCredentials(username, password); 
           
        try { 
            response = request.process(client); 
            if (response.getResults().getNumFound() > 0) {
                return response.getBeans(SolrRecord.class);
            }  
        } catch (SolrServerException | IOException ex) {            
            log.error(ex.getMessage());
            return null;
        }     
        return null;
    }
     
    public List<SolrRecord> searchByFamily(String family) {
        log.info("searchByFamily :{}", family);
           
        solrQuery = new SolrQuery();
        solrQuery.setQuery(buildSearchText(family, familyKey)); 
        solrQuery.setRows(size);
        request = new QueryRequest(solrQuery);
        request.setBasicAuthCredentials(username, password); 
            
        try { 
            response = request.process(client);  
            if (response.getResults().getNumFound() > 0) {
                log.info("result size : {}", response.getResults().getNumFound());  
                return response.getBeans(SolrRecord.class);
            }  
        } catch (SolrServerException | IOException ex) {            
            log.error(ex.getMessage());
            return null;
        }     
        return null; 
    }
    
    public List<SolrRecord> searchByGunes(String genus) {
        log.info("searchByGunes :{}", genus);
           
        solrQuery = new SolrQuery();
        solrQuery.setQuery(buildSearchText(genus, genusKey)); 
        solrQuery.setRows(size);
        request = new QueryRequest(solrQuery);
        request.setBasicAuthCredentials(username, password); 
           
        try { 
            response = request.process(client); 
            if (response.getResults().getNumFound() > 0) {
                return response.getBeans(SolrRecord.class);
            }  
        } catch (SolrServerException | IOException ex) {            
            log.error(ex.getMessage());
            return null;
        }     
        return null; 
    }
    
    
    public List<SolrRecord> searchBySpecies(String species) {
        log.info("searchBySpecies :{}", species);
           
        solrQuery = new SolrQuery();
        solrQuery.setQuery(buildSearchText(species, speciesKey)); 
        solrQuery.setRows(size);
        request = new QueryRequest(solrQuery);
        request.setBasicAuthCredentials(username, password); 
           
        try { 
            response = request.process(client); 
            if (response.getResults().getNumFound() > 0) {
                return response.getBeans(SolrRecord.class);
            }  
        } catch (SolrServerException | IOException ex) {            
            log.error(ex.getMessage());
            return null;
        }     
        return null; 
    }
    
    private String buildSearchText(String taxa, String field) {
        if(StringUtils.containsAny(taxa, replaceChars)) {
            replaceChars(taxa);
        }
        String[] strings = taxa.split(emptySpace);
        StringBuilder sb = new StringBuilder();
        if (strings.length > 1) {
            sb.append(searchAndStart);
            for (String s : strings) {
                if (!s.isEmpty()) { 
                    sb.append(field);
                    sb.append(wildCard);
                    sb.append(s);
                    sb.append(wildCard);
                    sb.append(emptySpace);
                }
            }
            sb.append(searchAndStop);
        } else {
            sb.append(field); 
            sb.append(wildCard);
            sb.append(taxa);
            sb.append(wildCard);
            sb.append(emptySpace);
        } 
        sb.append(searchCollections);
        log.info("search text : {}", sb.toString());
        return sb.toString().trim();
    }
    

    
    
    
    

//    public List<SolrRecord> searchByTaxa(Sample sample, String field, String catalogNumbers) {
//        log.info("searchByTaxa :{}", sample);
// 
//        List<SolrRecord> solrRecords = new ArrayList<>();
//////    String searchText = isLocal ? buildSearchTextStringLocal(sample, field, catalogNumbers) :
//////      buildSearchTextStringRemote(sample, field, catalogNumbers);
//
//
//        String searchText = buildSearchTextStringRemote(sample, field, catalogNumbers);
//        log.info("searchByTaxa: search text : {}", searchText);
////        try {
////            solrQuery = new SolrQuery();
////            solrQuery.setQuery(searchText);
////
////            solrQuery.setStart(0);
////            solrQuery.setRows(5000);
////            QueryResponse response = solrServer.query(solrQuery);
////
////            solrRecords = response.getBeans(SolrRecord.class);
////            logger.info("solrRecords : {}", solrRecords != null ? solrRecords.size() : 0);
////        } catch (SolrServerException ex) {
////            logger.warn(ex.getMessage());
////        }
//        return null;
////        return solrRecords;
//    }

//    private void buildSubString(String text, String field, StringBuilder sb) {
//
//        if (text.contains("(") || text.contains(")") || text.contains(comma)) {
//            replaceChars(text);
//        }
//
//        String[] strings = text.split(emptySpace);
//        if (strings.length > 1) {
//            sb.append("+(");
//            for (String s : strings) {
//                if (!s.isEmpty()) {
//                    sb.append("+");
//                    sb.append(field);
//                    sb.append(":*");
//                    sb.append(s);
//                    sb.append("* ");
//                }
//            }
//            sb.append(") ");
//        } else {
//            sb.append("+");
//            sb.append(field);
//            sb.append(":*");
//            sb.append(text);
//            sb.append("* ");
//        }
//    }

//    private String buildSearchTextStringRemote(Sample sample, String field, String catalogNumbers) {
//        log.info("buildSearchTextStringRemote: sample : {} -- {}", 
//                sample.getFamily(), field);
//        StringBuilder sb = new StringBuilder();
//
//        if (sample.getFamily() != null && !sample.getFamily().isEmpty()) {
//            buildSubString(sample.getFamily(), "family", sb);
//        }
//        log.info("buildSearchTextStringRemote : {}", sb.toString());
//        switch (field) {
//            case "sp":
//                if (sample.getGenus() != null && !sample.getGenus().isEmpty()) {
//                    buildSubString(sample.getGenus(), "species", sb);
//                }
//                if (sample.getSpecies() != null && !sample.getSpecies().isEmpty()) {
//                    buildSubString(sample.getSpecies(), "species", sb);
//                }
//                break;
//            case "gn":
//                if (sample.getGenus() != null && !sample.getGenus().isEmpty()) {
//                    buildSubString(sample.getGenus(), "species", sb);
//                }
//                break;
//        }
//
//        sb.append("+txRank:220");
//
//        if (catalogNumbers != null && !catalogNumbers.isEmpty()) {
//            sb.append(" -cn:(");
//            sb.append(catalogNumbers);
//            sb.append(")");
//        }
//        sb.append(searchCollectionText);
//        return sb.toString().trim();
//    }

//  private String buildSearchTextStringLocal(Sample sample, String field, String catalogNumbers) { 
//    logger.info("buildSearchTextStringLocal: sample : {} -- {}", sample.getFamily(), sample.getGenus());
//
//    StringBuilder sb = new StringBuilder();
//
//    if (sample.getFamily() != null && !sample.getFamily().isEmpty()) {
//      buildSubString(sample.getFamily(), "fm", sb);
//    }
//
//    switch (field) {
//      case "sp":
//        if (sample.getGenus() != null && !sample.getGenus().isEmpty()) {
//          buildSubString(sample.getGenus(), "gn", sb);
//        }
//        if (sample.getSpecies() != null && !sample.getSpecies().isEmpty()) {
//          buildSubString(sample.getSpecies(), field, sb);
//        }
//        break;
//      case "gn":
//        if (sample.getGenus() != null && !sample.getGenus().isEmpty()) {
//          buildSubString(sample.getGenus(), "gn", sb);
//        }
//        break;
//    }
//
//    sb.append("+taxarank:220");
//
//    if (catalogNumbers != null && !catalogNumbers.isEmpty()) {
//      sb.append(" -cn:(");
//      sb.append(catalogNumbers);
//      sb.append(")");
//    }
//    sb.append(searchCollectionText);
//    return sb.toString().trim();
//  }
    
    private String replaceChars(String value) {
//        String s = value.replaceAll("[\\[\\](),]", " ");
//        return s.trim();
        return value.replaceAll("[\\[\\](),]", " ").trim();
    }
}
