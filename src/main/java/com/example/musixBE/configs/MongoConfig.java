package com.example.musixBE.configs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
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
        ImmutableMongodConfig mongodbConfig = ImmutableMongodConfig.builder().version(Version.Main.DEVELOPMENT).build();
        MongodStarter mongodStarter = MongodStarter.getDefaultInstance();
        MongodExecutable mongodExecutable = mongodStarter.prepare(mongodbConfig);
        mongodExecutable.start();
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        return new MongoTemplate(mongoClient, DATABASE_NAME);
    }
}
