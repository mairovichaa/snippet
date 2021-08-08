package com.amairovi.spring;

import com.mongodb.client.MongoClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    private static final Log log = LogFactory.getLog(App.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        context.start();
        MongoClient mongoClient = context.getBean(MongoClient.class);

        log.info("Existing databases");
        mongoClient.listDatabases()
                .forEach(log::info);
    }
}
