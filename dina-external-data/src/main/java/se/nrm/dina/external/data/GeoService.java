package se.nrm.dina.external.data;
 
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;  
import java.io.IOException;
import java.io.InputStream; 
import java.io.InputStreamReader;
import java.io.Serializable; 
import java.net.URL;   
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   

/**
 *
 * @author idali
 */
public class GeoService implements Serializable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final static String GEO_WEB_SERVICE = "http://ws.geonames.org/findNearbyPlaceName?";
    private final static String GOOGLE_SERVICE = "http://maps.googleapis.com/maps/api/geocode/xml?latlng=";
    private final static String SENSOR = "&sensor=false";
    private String content = "";
    
    private URL url;
    private StringBuilder sb;
    
    public boolean isInRange(double lat, double lnt) {
//        logger.info("isInSweden");
          
        String text = "";
        InputStream is = null;  
        try { 
            url = new URL(buildUrl(lat, lnt)); 
            is = url.openStream();    
//            byte[] bytes = IOUtils.toByteArray(is);
//            VTDGen vg = new VTDGen();
//            vg.setDoc(bytes);
//            vg.parse(true); 
//            
//            VTDNav vn = vg.getNav();
//            AutoPilot ap = new AutoPilot(vn);
//            ap.selectXPath("/GeocodeResponse/result/formatted_address"); 
//            while (ap.evalXPath() != -1) {
//                int t = vn.getText(); // get the index of the text (char data or CDATA)
//                if (t != -1) {
//                    text = vn.toNormalizedString(t);
//                    logger.info(text); 
//                    if(text.contains("Sverige") || text.contains("Sweden") ) {
//                        return true; 
//                    } else {
//                        return false;
//                    }
//                }
//            } 
            
            
            content = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8)); 
            if(content.contains("<countryName>Sweden</countryName>")) {
                return true;
            } else if(content.contains("<countryName>Norway</countryName>")) {
                return true;
            } else if(content.contains("<countryName>Finland</countryName>")) {
                return true;
            }
 
//        } catch (XPathEvalException ex) {
//            logger.error(ex.getMessage());
//        } catch (NavException ex) {
//            logger.error(ex.getMessage());
//        } catch (XPathParseException ex) {
//            logger.error(ex.getMessage());
//        } catch (EncodingException ex) {
//            logger.error(ex.getMessage());
//        } catch (EOFException ex) {
//            logger.error(ex.getMessage());
//        } catch (EntityException ex) {
//            logger.error(ex.getMessage());
//        } catch (ParseException ex) {
//            logger.error(ex.getMessage());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                Closeables.closeQuietly(is); 
                is.close();
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        } 
        return false;
    }
    
    
    private String buildUrl(double lat, double lnt) {
//        sb = new StringBuilder(GOOGLE_SERVICE);
//        sb.append(lat);
//        sb.append(",");
//        sb.append(lnt);
//        sb.append(SENSOR);
        sb = new StringBuilder(GEO_WEB_SERVICE);
        sb.append("lat=");
        sb.append(lat);
        sb.append("&lng=");
        sb.append(lnt); 
        return sb.toString();
    } 
}
