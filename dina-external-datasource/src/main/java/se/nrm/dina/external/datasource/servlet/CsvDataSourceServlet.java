package se.nrm.dina.external.datasource.servlet;

import com.google.visualization.datasource.DataSourceHelper;
import com.google.visualization.datasource.DataSourceServlet;
import com.google.visualization.datasource.base.DataSourceException;
import com.google.visualization.datasource.base.ReasonType;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.query.Query;
import com.google.visualization.datasource.util.CsvDataSourceHelper;
import com.ibm.icu.util.ULocale;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL; 
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 * 
 * Not in use
 */
public class CsvDataSourceServlet extends DataSourceServlet {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String URL_PARAM_NAME = "url";

    @Override
    public DataTable generateDataTable(Query query, HttpServletRequest request) throws DataSourceException {


        String url = request.getParameter(URL_PARAM_NAME);
        if (StringUtils.isEmpty(url)) {
            logger.error("url parameter not provided.");
            throw new DataSourceException(ReasonType.INVALID_REQUEST, "url parameter not provided");
        }

        DataTable dataTable = null;
        Reader reader;
        try { 
            reader = new BufferedReader(new InputStreamReader(new URL(url).openStream())); 
            ULocale requestLocale = DataSourceHelper.getLocaleFromRequest(request);

            // Note: We assume that all the columns in the CSV file are text columns. In cases where the
            // column types are known in advance, this behavior can be overridden by passing a list of
            // ColumnDescription objects specifying the column types. See CsvDataSourceHelper.read() for
            // more details.
            dataTable = CsvDataSourceHelper.read(reader, null, true, requestLocale);
        } catch (MalformedURLException e) {
            logger.error("url is malformed: " + url);
            throw new DataSourceException(ReasonType.INVALID_REQUEST, "url is malformed: " + url);  
        } catch (IOException e) {
            logger.error("Couldn't read from url: " + url, e);
            throw new DataSourceException(ReasonType.INVALID_REQUEST, "Couldn't read from url: " + url);
        }
        return dataTable;
    } 
}
