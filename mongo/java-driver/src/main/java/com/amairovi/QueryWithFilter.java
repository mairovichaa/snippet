package com.amairovi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import static com.amairovi.Configs.*;

public class QueryWithFilter {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);

        var collection = database.getCollection(COLLECTION_NAME);
        collection.find(Filters.gte("num", 50))
                .forEach(it -> System.out.printf("%s%n", it));
    }
}
