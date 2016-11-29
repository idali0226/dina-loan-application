package se.nrm.dina.external.datasource.servlet;
 
import com.google.visualization.datasource.DataSourceServlet;
import com.google.visualization.datasource.base.DataSourceException; 
import com.google.visualization.datasource.datatable.DataTable; 
import com.google.visualization.datasource.query.Query;  
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   
import se.nrm.dina.external.data.ExternalDataSource;

/**
 *
 * @author idali
 */
@WebServlet("/map")
public class MapExternalDataSourceServlet extends DataSourceServlet {

    private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
       
    public MapExternalDataSourceServlet() { 
    }
 
    @Override
    public DataTable generateDataTable(Query query, HttpServletRequest hsr) throws DataSourceException {
        
        logger.info("generateDataTable");
        
        return ExternalDataSource.getInstance().getDataTable(); 
    }     
}
