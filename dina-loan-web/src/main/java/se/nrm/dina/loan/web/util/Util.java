/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.loan.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate; 
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author idali
 * 
 * 
 */
public class Util {
    
    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd"); 
      
    public static Date addWeeksToDate(int numOfWeeks) {
        
        logger.info("addWeeksToDate");
        
        Calendar now = Calendar.getInstance();
         
        now.add(Calendar.WEEK_OF_YEAR, numOfWeeks); 
        return now.getTime(); 
    }
    
    /**
     * This method is only for Christmas holiday
     * @return 
     */
    public static Date holidayMinDate() {
        
        logger.info("holidayMinDate");

        LocalDate today = LocalDate.now();
        
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
        
        LocalDate minDate = LocalDate.parse("28.11.2015", germanFormatter);  
        LocalDate maxDate = LocalDate.parse("01.01.2016", germanFormatter);  
        
        if (today.isAfter(minDate) && today.isBefore(maxDate)) {
            try { 
                LocalDate startDate = LocalDate.parse("14.01.2016", germanFormatter);  

                return new SimpleDateFormat("yyyy-MM-dd").parse(startDate.toString()); 
            } catch (ParseException ex) {
                return addWeeksToDate(6);
            }
        } else {
            return addWeeksToDate(2);
        } 
    }
    
    /**
     * Convert Date to String with "yyyy-MM-dd" format
     *
     * @param date - Date
     * @return String
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        return FORMAT.format(date);
    }
    
//    public static boolean isLocal() {
//        return IS_LOCAL;
//    }
}
