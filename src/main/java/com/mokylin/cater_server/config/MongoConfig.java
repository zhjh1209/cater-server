package com.mokylin.cater_server.config;

import com.mongodb.MongoClient;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.mokylin.cater_server"})
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    public MongoClient mongoClient() {
        MongoClient client = new MongoClient();
        return client;
    }

    @Override
    protected String getDatabaseName() {
        return "cater-server";
    }

}
