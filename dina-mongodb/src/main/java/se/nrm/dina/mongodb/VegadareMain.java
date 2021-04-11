/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import se.nrm.dina.mongodb.loan.vo.Counters; 

/**
 *
 * @author idali
 */
public class VegadareMain {

    public static void main(String args[]) {

        try {
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB("vegadare");

            Jongo jongo = new Jongo(db);
            MongoCollection collection = jongo.getCollection("counters");
            Counters counters = new Counters("coid", 0);
            collection.insert(counters);
        } catch (Exception ex) {

        }
    }

}
