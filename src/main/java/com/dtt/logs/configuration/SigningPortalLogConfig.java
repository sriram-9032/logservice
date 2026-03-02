package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.signingPortal",
        mongoTemplateRef = "signingPortalLogMongoTemplate")
public class SigningPortalLogConfig {
    private SigningPortalLogConfig() {
    }

    protected static final String MONGO_TEMPLATE = "signingPortalLogMongoTemplate";
}