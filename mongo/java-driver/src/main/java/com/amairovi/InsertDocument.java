package com.amairovi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.amairovi.Configs.*;

public class InsertDocument {

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);

        Document doc = new Document("name", "test doc")
                .append("foo", new Document("bar", 1).append("zip", "zap"));

        var insertOneResult = database.getCollection(COLLECTION_NAME)
                .insertOne(doc);
        System.out.printf("insertId = '%s', wasAcknowledge = '%b'%n", insertOneResult.getInsertedId(), insertOneResult.wasAcknowledged());
    }
}
