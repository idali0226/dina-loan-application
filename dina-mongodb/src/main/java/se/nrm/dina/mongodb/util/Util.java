package se.nrm.dina.mongodb.util;
 
import java.time.LocalDate;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Slf4j
public class Util {
    
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize((iterable.iterator()), Spliterator.ORDERED), false);
    }  
    
    public static LocalDate convertDate(String strDate) {
        
        if(strDate.startsWith("2024")) {
            log.info("date .. : {}", strDate);
        }
        
        return LocalDate.parse(strDate);
    }
    
    public static boolean isInTheCurrentYear(String strDate) {
        if(strDate.startsWith("2024")) {
            log.info("date .. : {}", strDate);
        }
        
        int currentYear = LocalDate.now().getYear();
        return strDate.startsWith(String.valueOf(currentYear));
    }
}
