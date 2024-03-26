package se.nrm.dina.loan.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month; 
import java.util.Calendar;
import java.util.Date; 
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 *
 *
 */
@Slf4j
public class Util {

    private LocalDate today;
    private int year;
    private int nextYear;
    
    //    private final DateTimeFormatter GERMAN_FORMATTER = 
//            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMAN);
    
    
    
    

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 
//    private LocalDate minDate = LocalDate.of(year, Month.NOVEMBER, 30);
//    private LocalDate maxDate = LocalDate.of(nextYear, Month.JANUARY, 1);
//    
//    private LocalDate holidayMinDate = LocalDate.of(year, Month.JANUARY, 14);
    
    
    
    private LocalDate minDate;
    private LocalDate maxDate;
  
    private LocalDate holidayMinDate;

    private static Util instance = null;

    public static synchronized Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    public int getCurrentYear() {
        return LocalDate.now().getYear();
    }
    
    private void buildMinAndMaxDates() {
        today = LocalDate.now();
        year = today.getYear() ;
        nextYear = year + 1;
 
        
        minDate = LocalDate.of(year, Month.NOVEMBER, 30);
        maxDate = LocalDate.of(nextYear, Month.JANUARY, 1);
    
        holidayMinDate = LocalDate.of(nextYear, Month.JANUARY, 14);
    }

    public boolean isHoliday() {
 
        if(minDate == null || maxDate == null) {
            buildMinAndMaxDates();
        }
        log.info("minDate and maxDate : {} -- {}", minDate, maxDate);
        log.info("holidayMinDate : {}", holidayMinDate);
        return today.isAfter(minDate) && today.isBefore(maxDate); 
    }
    
    public Date addWeeksToDate(int numOfWeeks) {
        log.info("addWeeksToDate");

        Calendar now = Calendar.getInstance();

        now.add(Calendar.WEEK_OF_YEAR, numOfWeeks);
        return now.getTime();
    }
    
    public Date addMinDate(boolean isPhysical) {
        log.info("addMinDate");
      
        if (isHoliday() && isPhysical) {
            if(holidayMinDate == null) {
                buildMinAndMaxDates();
            }
            try {  
                return format.parse(holidayMinDate.toString());
            } catch (ParseException ex) {
                return addWeeksToDate(6);
            }
        } else {
            return addWeeksToDate(2);
        } 
    }
    
    public Date addMinDate() {
        log.info("addMinDate");
        if (isHoliday()) {
            try {  
                return format.parse(holidayMinDate.toString());
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
    public String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        return format.format(date);
    }
    
 //       /**
//     * This method is only for Christmas holiday
//     *
//     * @return
//     */
//    public Date holidayMinDate() {
//        log.info("holidayMinDate");
//
//        if (isHoliday()) {
//            String holidayStr = "14.01." + nextYear;
//            try {
//                LocalDate startDate = LocalDate.parse(holidayStr, GERMAN_FORMATTER);
//
//                return new SimpleDateFormat("yyyy-MM-dd").parse(startDate.toString());
//            } catch (ParseException ex) {
//                return addWeeksToDate(6);
//            }
//        } else {
//            return addWeeksToDate(2);
//        }
//    }
    
//    public static boolean isHoliday() {
//
//        today = LocalDate.now();
//        year = today.getYear();
//        nextYear = year + 1;
//        String minStr = "30.11." + year;
//        String maxStr = "01.01." + nextYear;
//
//        LocalDate minDate = LocalDate.parse(minStr, GERMAN_FORMATTER);
//        LocalDate maxDate = LocalDate.parse(maxStr, GERMAN_FORMATTER);
//        return today.isAfter(minDate) && today.isBefore(maxDate);
//    }


    

}
