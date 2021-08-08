package com.amairovi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.amairovi.Configs.*;

public class InsertMultipleDocuments {

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);

        List<Document> docsForInsert = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Document doc = new Document("name", "test doc #" + i)
                    .append("num", i);
            docsForInsert.add(doc);
        }

        var manyResult = database.getCollection(COLLECTION_NAME)
                .insertMany(docsForInsert);

        System.out.printf("insertedIds = '%s', wasAcknowledge = '%b'%n", manyResult.getInsertedIds(), manyResult.wasAcknowledged());
    }
}
