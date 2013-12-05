package com.noobsqn;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

        import java.util.Arrays;

/**
 * Created by Samuel on 05/12/13.
 */

public class NSQN_Profiler {
// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
// if it's a member of a replica set:
        //MongoClient mongoClient = new MongoClient();
// or
        //MongoClient mongoClient = new MongoClient( "localhost" );
// or
        MongoClient mongoClient = new MongoClient( "http://noobsqn.com.br:27017/" , 27017 );
// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
        //MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
        new ServerAddress("localhost", 27018),
        new ServerAddress("localhost", 27019)));

        DB db = mongoClient.getDB( "mydb" );

        db.
}