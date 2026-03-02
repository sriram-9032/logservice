package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.admin",
        mongoTemplateRef = "adminLogMongoTemplate")
public class AdminLogConfig {
    private AdminLogConfig() {
    }

    protected static final String MONGO_TEMPLATE = "adminLogMongoTemplate";
}
