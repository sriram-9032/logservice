package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.ra",
        mongoTemplateRef = "raLogMongoTemplate")
public class RaLogConfig {
    private RaLogConfig() {
    }

    protected static final String MONGO_TEMPLATE = "raLogMongoTemplate";
}