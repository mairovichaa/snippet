package com.amairovi.without_spring;


import com.mongodb.client.MongoClients;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static com.amairovi.Configs.CONNECTION_STRING;
import static com.amairovi.Configs.DB_NAME;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class WithoutSpring {

    private static final Log log = LogFactory.getLog(WithoutSpring.class);

    public static void main(String[] args) throws Exception {

        MongoOperations mongoOps = new MongoTemplate(MongoClients.create(CONNECTION_STRING), DB_NAME);
        mongoOps.insert(new Person("Joe", 34));

        log.info(mongoOps.findOne(new Query(where("name").is("Joe")), Person.class));

        mongoOps.dropCollection("person");
    }
}
