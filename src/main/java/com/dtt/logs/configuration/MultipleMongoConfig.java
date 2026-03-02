package com.dtt.logs.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class MultipleMongoConfig {

@Value("${mongo.url}")
private String mongoBaseUrl;

    // This is the ONLY MongoClient in the whole application
    @Bean
    public MongoClient sharedMongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoBaseUrl + "/admin?authSource=admin");

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder ->
                        builder.maxSize(100).maxWaitTime(2, TimeUnit.SECONDS)
                                .minSize(5))
                .build();
        return MongoClients.create(settings);
    }

    // Helper method to create factories using the shared client
    private MongoDatabaseFactory createFactory(String dbName) {
        return new SimpleMongoClientDatabaseFactory(sharedMongoClient(), dbName);
    }



    @Primary
    @Bean(name = "adminLogMongoTemplate")
    public MongoTemplate adminLogMongoTemplate() {
        return new MongoTemplate(createFactory("admin-log"));
    }


    @Bean(name = "enterpriseLogMongoTemplate")
    public MongoTemplate enterpriseLogMongoTemplate() {
        return new MongoTemplate(createFactory("enterprise-logs"));
    }

    @Bean(name ="centralLogMongoTemplate")
    public MongoTemplate centralLogMongoTemplate() {
        return new MongoTemplate(createFactory("central-log"));
    }

    @Bean(name ="signingPortalLogMongoTemplate")
    public MongoTemplate signingPortalLogMongoTemplate() {
        return new MongoTemplate(createFactory("signing-portal-log"));
    }


    @Bean(name ="signingLogMongoTemplate")
    public MongoTemplate signingLogMongoTemplate() {
        return new MongoTemplate(createFactory("signing-log"));
    }


    @Bean(name ="raLogMongoTemplate")
    public MongoTemplate raLogMongoTemplate() {
        return new MongoTemplate(createFactory("ra-log"));
    }

    @Bean(name ="paymentsLogMongoTemplate")
    public MongoTemplate paymentsLogMongoTemplate() {
        return new MongoTemplate(createFactory("payments-log"));
    }


    @Bean(name ="obLogMongoTemplate")
    public MongoTemplate obLogMongoTemplate() {
        return new MongoTemplate(createFactory("ob-log"));
    }


    @Bean(name ="authnLogMongoTemplate")
    public MongoTemplate authnLogMongoTemplate() {
        return new MongoTemplate(createFactory("authn-log"));
    }


    @Bean(name ="kycLogMongoTemplate")
    public MongoTemplate kycLogMongoTemplate() {
        return new MongoTemplate(createFactory("kyc-log"));
    }

}
