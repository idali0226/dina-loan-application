/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.loan.web.service;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.loan.web.vo.SolrRecord;
import se.nrm.dina.mongodb.loan.vo.Sample;

/**
 *
 * @author idali
 */
@Stateless
public class SolrClient implements Serializable {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final static String LOCAL_SOLR_URL = "http://localhost:8983/solr/";                             // local
//    private final static String REMOTE_SOLR_URL_LOAN = "http://dina-loans:8983/solr/";                      // dina-portal  
//    private final static String REMOTE_SOLR_URL_AS = "http://dina-db.nrm.se:8983/solr/";
  private final static String REMOTE_NATURARV_SOLR = "http://naturarv.nrm.se:18983/solr/nrm/";

  private static String SOLR_URL;

  private final static String EXCLUDED_COLLECTIONS = " -cln:(262144 294912 458752 491521)";
  private final static String SOLR_COLLECTION_REMOTE = " +collectionId:163840";
  private final static String SOLR_COLLECTION_LOCAL = " +cln:163840";

//  private StringBuilder sb;
  private final SolrServer solrServer;
  private SolrQuery solrQuery;

  private String host;
  private boolean isLocal;
  private final String catelogNumberKey = "cn:";
  private String searchCollectionText;

  public SolrClient() {

    InetAddress inetAddress = null;
    try {
      inetAddress = InetAddress.getLocalHost();
    } catch (UnknownHostException une) {
      logger.warn(une.getMessage());
    }
    if (inetAddress != null) {
      host = inetAddress.getHostName();
    }

    if (host.toLowerCase().contains("ida")) {
      SOLR_URL = LOCAL_SOLR_URL;
      isLocal = true;
      searchCollectionText = SOLR_COLLECTION_LOCAL;
//        } else if(host.toLowerCase().contains("dina-loans")) {
//            SOLR_URL = REMOTE_SOLR_URL_LOAN;
    } else {
      isLocal = false;
      SOLR_URL = REMOTE_NATURARV_SOLR;
      searchCollectionText = SOLR_COLLECTION_REMOTE;
    }
    solrServer = new HttpSolrServer(SOLR_URL);
  }

//  private String getSearchCollectionText() {
//    return isLocal ? SOLR_COLLECTION_REMOTE : SOLR_COLLECTION_LOCAL;
//  }
  public SolrRecord searchByCatalogNumber(String catalognumber) {
    logger.info("searchCatalogNumber :{}", catalognumber);
//    String searchTaxt = "cn:" + catalognumber + EXCLUDED_COLLECTIONS;
//    StringBuilder sb = new StringBuilder();
//    sb.append(catelogNumberKey);
//    sb.append(catalognumber);
//    sb.append(SOLR_COLLECTION_REMOTE);

    String searchTaxt = "catalogNumber:" + catalognumber + EXCLUDED_COLLECTIONS;
    solrQuery = new SolrQuery();
    solrQuery.setQuery(searchTaxt);
//
//    solrQuery = new SolrQuery();
//    solrQuery.setQuery(sb.toString().trim());
    try {
      QueryResponse response = solrServer.query(solrQuery);

      if (response.getResults().getNumFound() > 0) {
        return response.getBeans(SolrRecord.class).get(0);
      }
    } catch (SolrServerException ex) {
      logger.warn(ex.getMessage());
    }
    return null;
  }

  public List<SolrRecord> searchByTaxa(Sample sample, String field, String catalogNumbers) {
    logger.info("searchByTaxa :{}", sample);

    List<SolrRecord> solrRecords = new ArrayList<>();
//    String searchText = isLocal ? buildSearchTextStringLocal(sample, field, catalogNumbers) :
//      buildSearchTextStringRemote(sample, field, catalogNumbers);
    String searchText = buildSearchTextStringRemote(sample, field, catalogNumbers);
    logger.info("search text : {}", searchText);
    try {
      solrQuery = new SolrQuery();
      solrQuery.setQuery(searchText);

      solrQuery.setStart(0);
      solrQuery.setRows(5000);
      QueryResponse response = solrServer.query(solrQuery);

      solrRecords = response.getBeans(SolrRecord.class);
      logger.info("solrRecords : {}", solrRecords != null ? solrRecords.size() : 0);
    } catch (SolrServerException ex) {
      logger.warn(ex.getMessage());
    }
    return solrRecords;
  }

  private void buildSubString(String text, String field, StringBuilder sb) {

    if (text.contains("(") || text.contains(")") || text.contains(",")) {
      replaceChars(text);
    }

    String[] strings = text.split(" ");
    if (strings.length > 1) {
      sb.append("+(");
      for (String s : strings) {
        if (!s.isEmpty()) {
          sb.append("+");
          sb.append(field);
          sb.append(":*");
          sb.append(s);
          sb.append("* ");
        }
      }
      sb.append(") ");
    } else {
      sb.append("+");
      sb.append(field);
      sb.append(":*");
      sb.append(text);
      sb.append("* ");
    }
  }

  private String buildSearchTextStringRemote(Sample sample, String field, String catalogNumbers) {
    logger.info("buildSearchTextStringRemote: sample : {} -- {}", sample.getFamily(), sample.getGenus());
    StringBuilder sb = new StringBuilder();

    if (sample.getFamily() != null && !sample.getFamily().isEmpty()) {
      buildSubString(sample.getFamily(), "family", sb);
    }

    switch (field) {
      case "sp":
        if (sample.getGenus() != null && !sample.getGenus().isEmpty()) {
          buildSubString(sample.getGenus(), "species", sb);
        }
        if (sample.getSpecies() != null && !sample.getSpecies().isEmpty()) {
          buildSubString(sample.getSpecies(), "species", sb);
        }
        break;
      case "gn":
        if (sample.getGenus() != null && !sample.getGenus().isEmpty()) {
          buildSubString(sample.getGenus(), "species", sb);
        }
        break;
    }

    sb.append("+txRank:220");

    if (catalogNumbers != null && !catalogNumbers.isEmpty()) {
      sb.append(" -cn:(");
      sb.append(catalogNumbers);
      sb.append(")");
    }
    sb.append(searchCollectionText);
    return sb.toString().trim();
  }

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
    String s = value.replaceAll("[\\[\\](),]", " ");
    return s.trim();
  }
}
