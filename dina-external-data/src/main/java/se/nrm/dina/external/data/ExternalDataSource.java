package se.nrm.dina.external.data;
 
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;      
import javax.ejb.LocalBean; 
import javax.ejb.Singleton;   
import org.apache.commons.lang.StringUtils; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 */
//@Startup                            // initialize ejb at deployment time
@Singleton
@LocalBean
public class ExternalDataSource implements Serializable {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static DataTable dataTable;
  
    private final static String COORDINATES_LOCAL_FILE_PATH = "/Users/idali/git-idali/data/dnakey/geo_coords.tsv";
//    private final static String COORDINATES_REMOTE_FILE_PATH = "/home/dnakey/data/geo_coords.tsv";
    private final static String COORDINATES_REMOTE_FILE_PATH = "/home/admin/wildfly-8.1.0-0/dnakey/data/geo_coords.tsv";
    
    
//    private final static String COORDINATES_FILE_PATH = "/home/admin/data/ALL_NUCCORE_BARCODE_DATA.gnbnk.20140110.latlon.tsv";
   
    private String coordinates_file_path;
    
    private static ExternalDataSource instance;
    
    public ExternalDataSource() {
               
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost(); 
            coordinates_file_path = getPath(inetAddress.getHostName());  
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage());
        } 
    }
       
    
    private String getPath(String hostname) { 
        
        logger.info("hostname : {}", hostname);
        return hostname.toLowerCase().contains("ida") ? COORDINATES_LOCAL_FILE_PATH : COORDINATES_REMOTE_FILE_PATH;
    }
    
     /**
     * Start ejb every time deploy into glassfish or glassfish start
     */
//    @PostConstruct
    void init() {
       buildDataTable();
    }
    
    public static ExternalDataSource getInstance() {
        synchronized (ExternalDataSource.class) {
            if (null == instance) {
                instance = new ExternalDataSource();
                instance.buildDataTable();
            }
        }
        return instance;
    }
    
 
    public DataTable getDataTable() {
        return dataTable;
    }
    
    
//    @Schedule(dayOfWeek = "*", hour = "0") 
//    @Schedule(hour="*", minute="*/10", second="0")                // Scheduled every 30 seconds
//    @Schedule(dayOfWeek = "Tue", hour = "0")                        // run every Monday at midlenight
    public void buildDataTable() {
         
        logger.info("buildDataTable : coordinate_file_path : {}", coordinates_file_path);
         
        dataTable = new DataTable(); 
  
        List dataColumn = new ArrayList();
        dataColumn.add(new ColumnDescription("LATITUDE", ValueType.NUMBER, "Latitude"));
        dataColumn.add(new ColumnDescription("LONGITUDE", ValueType.NUMBER, "Longitude"));
        dataColumn.add(new ColumnDescription("VALUE", ValueType.NUMBER, "Value")); 

        dataTable.addColumns(dataColumn);
          
        FileReader fileReader = null;
        try {  
            File f = new File(coordinates_file_path); 
            if(f.exists()) {
                fileReader = new FileReader(coordinates_file_path);
                BufferedReader br = new BufferedReader(fileReader);
 
               // GeoService geoService = new GeoService(); 
                double lat;
                double lnt;
              //  String coordinates;
                String sCurrentLine;  
                String[] strArry;
              //  boolean isInRange = false;
              //  DoubleValidator validator = new DoubleValidator();
                int total; 
                while ((sCurrentLine = br.readLine()) != null) {  
                    strArry = StringUtils.split(sCurrentLine, "\t");
                    total = Integer.parseInt(strArry[0].trim());
                    lnt = Double.parseDouble(strArry[1]); 
                    lat = Double.parseDouble(strArry[2]); 
                    dataTable.addRowFromValues(lat, lnt, total); 
//                    if(CharMatcher.anyOf(sCurrentLine).matchesAllOf("NE")) { 
//                        strArry = StringUtils.split(sCurrentLine, "\t");
//                        coordinates = strArry[0];
//                        lat = Double.parseDouble(StringUtils.substringBefore(coordinates, "N").trim()); 
//                        if(validator.isInRange(lat, 55.33, 71.2)) {
//                            lnt = Double.parseDouble(StringUtils.substringBetween(coordinates, "N", "E").trim()); 
//                            if(validator.isInRange(lnt, 4.5, 31.59)) {  
//                                try {
////                                    dataTable.addRowFromValues(lat, lnt, Integer.parseInt(strArry[1]));
//                                    
//                                   isInRange = geoService.isInRange(lat, lnt); 
//                                   count++;
////                                   logger.info("Is in range : {}", isInRange);
//                                   if(isInRange) { 
//                                        dataTable.addRowFromValues(lat, lnt, Integer.parseInt(strArry[1])); 
//                                   }
//                                } catch (TypeMismatchException ex) {
//                                    logger.error(ex.getLocalizedMessage());
//                                }
//                            }
//                        } 
//                    }  
                } 
            }  
        } catch (TypeMismatchException ex) { 
            logger.error(ex.getLocalizedMessage());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if(fileReader != null) {
                    fileReader.close(); 
                } 
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        } 
    }   
    
    public static void main(String[] args) {
        ExternalDataSource dataSource = new ExternalDataSource();
        
        
        dataSource.buildDataTable();
    }
}
