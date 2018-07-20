package com.mokylin.cater_server.config;

import com.mongodb.MongoClient;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.mokylin.cater_server"})
public class MongoConfig extends AbstractMongoConfiguration {
    @Value("${mongo.servers}")
    private String serversStr;
    @Value("${mongo.username}")
    private String username;
    @Value("${mongo.password}")
    private String password;

    @Override
    public MongoClient mongoClient() {
        String[] strArr = serversStr.split(",");
        List<ServerAddress> servers = new ArrayList<>(strArr.length);
        for (String str : strArr) {
            servers.add(new ServerAddress(str));
        }
        MongoClient client;
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            MongoCredential credential = MongoCredential
                    .createCredential(username, getDatabaseName(), password.toCharArray());
            // 密码验证
            client = new MongoClient(servers, credential, new Builder().build());
        } else {
            client = new MongoClient(servers);
        }
        return client;
    }

    @Override
    protected String getDatabaseName() {
        return "cater-server";
    }

}
