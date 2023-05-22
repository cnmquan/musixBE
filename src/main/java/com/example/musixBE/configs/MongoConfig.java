package com.example.musixBE.configs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    @Value("${spring.data.mongodb.uri}")
    private String CONNECTION_STRING;

    @Value("${spring.data.mongodb.database}")
    private String DATABASE_NAME;

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        return new MongoTemplate(mongoClient, DATABASE_NAME);
    }
}
