package com.amairovi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

import static com.amairovi.Configs.*;

public class CreateDatabase {

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);

        var oldDatabases = mongoClient.listDatabases().into(new ArrayList<>());
        var dbAlreadyExists = oldDatabases.stream().anyMatch(it -> it.getString("name").equals(DB_NAME));
        if (dbAlreadyExists) {
            System.out.printf("Database '%s' already exists%n", DB_NAME);
            return;
        }

        MongoDatabase database = mongoClient.getDatabase(DB_NAME);

        insertDocumentToCreateDatabase(database);

        var databases = mongoClient.listDatabases().into(new ArrayList<>());
        databases.forEach(System.out::println);
        var isCreated = databases.stream().anyMatch(it -> it.getString("name").equals(DB_NAME));
        if (isCreated) {
            System.out.printf("Database '%s' has been created%n", DB_NAME);
        } else {
            System.out.printf("ATTENTION!!!%n Database '%s' hasn't been created%n", DB_NAME);
        }
    }

    private static void insertDocumentToCreateDatabase(final MongoDatabase database) {
        Document doc = new Document("name", "test doc")
                .append("foo", new Document("bar", 1).append("zip", "zap"));

        var insertOneResult = database.getCollection(COLLECTION_NAME)
                .insertOne(doc);
        System.out.printf("insertId = '%s', wasAcknowledge = '%b'%n", insertOneResult.getInsertedId(), insertOneResult.wasAcknowledged());
    }
}
