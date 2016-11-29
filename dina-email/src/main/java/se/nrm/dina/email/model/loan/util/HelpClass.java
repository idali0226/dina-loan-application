/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.email.model.loan.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat; 
import java.util.Date;

/**
 *
 * @author idali
 */
public class HelpClass {
    
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Convert Date to String with "yyyy-MM-dd" format
     *
     * @param date - Date
     * @return String
     */
    public static String dateToString(Date date) {
         
         
        if (date == null) {
            return null;
        } else {
            return df.format(date);
        }
    }
    
        
    public static String getNow() { 
        Date now = new Date(); 
        return FORMAT.format(now);
    }
}
