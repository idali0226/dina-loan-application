/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.loan.admin.util;

//import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;  

/**
 *
 * @author idali
 */
public class Util {
    
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd"); 
//    private final static DateFormat DFORMAT  = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
    
//    private final static boolean IS_LOCAL = false;

    public static String getTodayAsString() {
        Calendar now = Calendar.getInstance(); 
        Date date = now.getTime(); 
        return FORMAT.format(date);
    }
    
    public static Date getTodayDate() {
        Calendar now = Calendar.getInstance(); 
        return now.getTime();  
    }
    
    public static Date stringToDate(String strDate) {
        
        if(strDate == null || strDate.isEmpty()) {
            return null;
        }
        try {
            return (Date)FORMAT.parse(strDate);
        } catch (ParseException ex) { 
            return null;
        }
    }
    
    public static Date getStartDate() {  
        try {
            return (Date)FORMAT.parse("2014-01-01");
        } catch (ParseException ex) {
            return null;
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
}
