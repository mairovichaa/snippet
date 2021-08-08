package com.amairovi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import static com.amairovi.Configs.CONNECTION_STRING;
import static com.amairovi.Configs.DB_NAME;

public class ListCollections {

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);

        System.out.printf("Database '%s' contains following collections: %n", DB_NAME);
        database.listCollectionNames().
                forEach(collectionName -> System.out.printf("\t %s", collectionName));
    }
}
