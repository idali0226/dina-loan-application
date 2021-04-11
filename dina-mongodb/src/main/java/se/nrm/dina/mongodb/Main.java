/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential;
import com.mongodb.QueryBuilder;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.Oid;
import org.jongo.marshall.jackson.oid.ObjectId; 
import se.nrm.dina.mongodb.loan.vo.Collection;
import se.nrm.dina.mongodb.loan.vo.Counters;
import se.nrm.dina.mongodb.loan.vo.Loan;
import se.nrm.dina.mongodb.util.Util;

/**
 *
 * @author idali
 */
public class Main {
    
    public static void main(String args[]) {
        
        try {
            
//            MongoClient mongo = new MongoClient("localhost", 27017);
            
//            MongoClient mongo = new MongoClient("localhost", 27017);
//            DB db = mongo.getDB("loans");
//
//            Jongo jongo = new Jongo(db); 
//            MongoCollection collection = jongo.getCollection("loan");
//            List<String> argList = new ArrayList<>();
//            argList.add("LoaNRM000001");
//            argList.add("sven.kullander@nrm.se");
//            System.out.println("array : " + Arrays.toString(argList.toArray()));
//            
//            StringUtils.join(argList, "; ");
//            
//            String query =  "{$and:[{ _id: \"LoaNRM000001\"} , {curator: \"sven.kullander@nrm.se\"}"
//                            + ", {status: 'Request pending'}]}";
//            
//            
//            
//            Iterable<Loan> loans = collection.find(query).as(Loan.class);
//            List<Loan> results = new ArrayList<>();
//            Util.stream(loans).forEach(l -> results.add(l));
//             
//             System.out.println("loans : " + results);
            
//            List<String> list = new ArrayList();
//            list.add("Request pending");
//            list.add("Loan returned");
//             
//             MongoClient mongo = new MongoClient("localhost", 27017);
//             DBCollection loans = mongo.getDB("loans").getCollection("loan");
//             DBCursor cursor = loans.find(
//                     QueryBuilder.start("status").in(list).and("_id").is("LoaNRM000005").get()
//                  );
// 
//             
//             System.out.println("size : " + cursor.size()); 

            MongoClient mongo = new MongoClient( "db", 27017);  
            DB db = mongo.getDB("loans");
               
            System.out.println("db..." + db);
            Jongo jongo = new Jongo(db);
            MongoCollection collection = jongo.getCollection("loan");
            
            Iterable<Loan> loans =  collection.find("{purpose: 'Education/Exhibition'}").as(Loan.class); 
             
//            Iterable<Collection> collections =  collection.find().sort("{name:-1}").as( Collection.class); 
//            
////            Iterable<Loan> loans = collection.find().sort("{_id:-1}").as(Loan.class);
//            List<Collection> results = new ArrayList<>();
//            Util.stream(collections).forEach(l -> results.add(l));
//            
//            System.out.println("loan : " + results);
//            
//            Iterable<Loan> loans = collection.find("{$and:[{'purpose': {'$in': ['Commercial/art/other','Scientific purpose']}}, {'department': {'$in': ['Zoology']}}, {'type': {'$in': ['Photo', 'Physical']}}, {submitDate: {$gte: '2014-07-30', $lte: '2014-08-05'}}]}").as(Loan.class);  
//          Iterable<Loan> loans = collection.find("{$or: [{purpose: 'Education/Exhibition'}, {$and:[{'purpose': {'$in': ['Commercial/art/other','Scientific purpose']}}, {'department': {'$in': ['Zoology']}}, {'type': {'$in': ['Photo', 'Physical']}}]}]}").as(Loan.class);  
//          
            
//             Iterable<Loan> loans = collection.find("{$or: [{purpose: 'Education/Exhibition'}, {$and:[{'department': {'$in': ['Zoology']}}]}").as(Loan.class);  
//         
            
//            Iterable<Loan> loans = collection.find("{$and: [{_id: 'LoaNRM000003'},  { 'status' : {'$in': ['Request pending', 'Loan returned']}}]}" ).as(Loan.class);
//            Iterable<Loan> loans = collection.find("{submitDate: {$gte: '2014-08-05', $lte: '2014-08-07'}}").as(Loan.class);  
//BasicDBObjectBuilder.start("$gte", 2014-08-05).add("$lte", 2014-08-07)
            for(Loan loan : loans) {
                System.out.println("size : "+ loan + " -- " + loan.getPurpose());
            }
            
////            System.out.println("isnull ? " );
////            System.out.println(loan != null);
////            System.out.println("loan : " + loan);
//            
//            Iterable<Collection> collections =  collection.find().as( Collection.class);  
////            
////            List<Collection> results = new ArrayList<>();
////            Util.stream(collections).forEach(c -> results.add(c));
////            
//            for(Collection c : collections) {
//                System.out.println("c : " + c);
//                collection.update("{group: '" + c.getGroup() + "'}").multi().with("{$set: {department: 'Zoology'}}"); 
////                c.setDepartment("Zoology");
////                collection.update("{_id: '" + c.getId() + "'}").with(c);   
////                System.out.println("c department:  " + c.getDepartment());
//            }
////            
//             
//            Iterable<Collection> list =  collection.find().as( Collection.class);  
//            
//            List<Collection> resultlist = new ArrayList<>();
//            Util.stream(list).forEach(c -> resultlist.add(c));
//            for(Collection c : resultlist) { 
//                System.out.println("c department:  " + c.getDepartment());
//            }
            
//            collection.update("{group: 'Ida Li Collection 2'}").multi().with("{$set: {department: 'Zoology'}}"); 
            
//            Iterable<Loan> loans = collection.find("{$and: [{_id: 'LoaNRM000005'}, {curator: 'sven.kullander@nrm.se'}, { 'status' : {'$in': ['Request pending', 'Loan returned']}}]}" ).as(Loan.class);
//            List<Loan> results = new ArrayList<>();
//            Util.stream(loans).forEach(l -> results.add(l));
//            System.out.println("loans : " + results);
            
            
            
//            collection.remove("{department: 'Zoology'}");
            
//            collection.update("{group: 'my new group'}").multi().with("{$set: {group: 'testgroup', manager: 'somemail@se.com'}}"); 
            
             Counters counters = new Counters("coid", 0);
                collection.insert(counters);
 
//            Counters counters = collection.findAndModify("{_id: 'loanid'}").with("{$inc: {seq: 1}}").returnNew().as(Counters.class); 
//            
//            System.out.println("counters : " + counters.getSeq());
//            collection.insert(  new Collection("mytest", "mytest@nrm.se", "mygroup", "test@nrm.se") );
            
            
            
            
            
//            Collection one = collection.findOne("{name: 'mytest'}").as(Collection.class); 
            
             
            
            
//            Collection two = (Collection)collection.findOne(Oid.withOid(one.getId())).as(Collection.class);
            
            
//            Collection one = collection.findOne(query).as(Collection.class);
//            Collection one = collection.findOne("{name: 'mytest'}").as(Collection.class);
//            collection.find(withOid(one.getId())).as(Collection.class);
            
//            System.out.println("email : " +two.getEmail());
//            System.out.println("_id : " + two.getId());
            
            
            
            
//            one.setEmail("idali@nrm.se");
            
//            collection.update(Oid.withOid(one.getId()), one); 
//             
            
//            String query ="{name: 'mytest'}";
//            collection.update(Oid.withOid(one.getId())).with(one); 
            
            
            
//            collection.update("{name: 'mytest'}", "{email: 'mytest@email.com'}", "{group: 'mygroup'}");
//            collection.insert(  new Collection("echinodermata", "sabine.stohr@nrm.se"),
//                                new Collection("invertebrategroups", "karin.sindemarkkronestedt@nrm.se"));
 
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }       
}
