package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.signing",
        mongoTemplateRef = "signingLogMongoTemplate")
public class SigningLogConfig {
    private SigningLogConfig() {
    }

    protected static final String MONGO_TEMPLATE = "signingLogMongoTemplate";
}