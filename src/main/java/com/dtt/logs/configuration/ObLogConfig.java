package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.ob",
        mongoTemplateRef = "obLogMongoTemplate")
public class ObLogConfig {
    private ObLogConfig() {
    }

    protected static final String MONGO_TEMPLATE = "obLogMongoTemplate";
}
