package com.buzzword; //.database when you've updated the package structure

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.connection.ConnectionPoolSettings; //Set up connection pool
import org.bson.Document;

public class InitializeResourceDB {
    public static void buildResourceDB() throws Exception{ //pass in the connection to the DB here
        //Code to build resource database goes here
        //Put all of these variables in the configuration files
        

        String user = System.getenv().getOrDefault("DB_USER", "dbuser");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "password");
        String host = System.getenv().getOrDefault("DB_HOST", "localhost");
        String port = System.getenv().getOrDefault("DB_PORT", "27017");
        String databaseName = System.getenv().getOrDefault("DB_NAME", "buzzwordDB");

        String conn = String.format("mongodb://%s:%s@%s:%s/%s?authSource=admin", user, password, host, port, databaseName);
        //Configuration file!!

        try (MongoClient client = MongoClients.create(conn)) {
            MongoDatabase db = client.getDatabase(databaseName);

            boolean exists = db.listCollectionNames().into(new java.util.ArrayList<>()).contains("resources");
            if(!exists)
            {
                db.createCollection("resources");
            }

            MongoCollection<Document> tempCollection = db.getCollection("resources");

            tempCollection.insertOne(new Document("initialized DB at", java.time.Instant.now().toString()));
            System.out.println("Connected to MongoDB");

        }
    }
    
}