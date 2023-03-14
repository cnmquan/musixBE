package com.example.musixBE;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MongoConnectionApplicationLiveTest {
    @Test
    void assertInsertSucceeds(ConfigurableApplicationContext context){
        String name = "Test";

        MongoTemplate mongo = context.getBean(MongoTemplate.class);
        Document doc = Document.parse("{\"name\":\"" + name + "\"}");
        Document inserted = mongo.insert(doc, "test");

        assertNotNull(inserted.get("_id"));
        assertEquals(inserted.get("name"), name);
    }
}
