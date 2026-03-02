package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.authn",
        mongoTemplateRef = "authnLogMongoTemplate")
public class AuthnLogConfig {
    private AuthnLogConfig() {
    }

    protected static final String MONGO_TEMPLATE = "authnLogMongoTemplate";
}