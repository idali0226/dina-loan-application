package se.nrm.dina.loan.web;
 
//import com.mongodb.DB; 
//import com.mongodb.MongoClient;
//import java.net.UnknownHostException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.jongo.Jongo;
//import org.jongo.MongoCollection; 
//import se.nrm.dina.mongodb.loan.vo.Collection;

 

 
/**
 *
 * @author idali
 */
public class MongoDBJDBC {

    public static void main(String args[]) {
        
//        try {
//            
//            MongoClient mongo = new MongoClient("localhost", 27017);
//            DB db = mongo.getDB("loans");
//
//            Jongo jongo = new Jongo(db); 
//            MongoCollection collection = jongo.getCollection("collection");
//            
//            
//            Collection one = collection.findOne("{name: 'fish'}").as(Collection.class);
//            System.out.println("email : " +one.getEmail());
//        
//        
////            collection.insert(  new Collection("echinodermata", "sabine.stohr@nrm.se"),
////                                new Collection("invertebrategroups", "karin.sindemarkkronestedt@nrm.se"));
// 
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(MongoDBJDBC.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }        
}
