package se.nrm.dina.external.datasource;

import com.google.common.base.CharMatcher;
import java.io.BufferedReader;  
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 

/**
 *
 * @author idali
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        FileReader fileReader = new FileReader("/Users/idali/large/ALL_NUCCORE_BARCODE_DATA.gnbnk.20131115.latlon.tsv");
     
        BufferedReader br = new BufferedReader(fileReader);

        String sCurrentLine;
         
        while ((sCurrentLine = br.readLine()) != null) {  
//            System.out.println(sCurrentLine);
           if(CharMatcher.anyOf(sCurrentLine).matchesAllOf("NE")) { 
               System.out.println(sCurrentLine);
            }  
        }






//        
//        
//
//        BufferedReader br = null;
//        try {
//            fis = new FileInputStream(file);
//
//            System.out.println("Total file size to read (in bytes) : " + fis.available());
//
//            String line;
//            while ((line=r.readLine())!=null){
//                file.append(line);
//            }
//            while ((content = fis.read()) != -1) {
//                // convert to char and display it
//                System.out.print((char) content);
//                
//                String[] contents = StringUtils.split(content, "\t");
//                
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fis != null) {
//                    fis.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
    }
}
