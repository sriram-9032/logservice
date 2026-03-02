package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.enterprise",
        mongoTemplateRef = "enterpriseLogMongoTemplate")
public class EnterpriseLogConfig {
    private EnterpriseLogConfig() {
    }
    protected static final String MONGO_TEMPLATE = "enterpriseLogMongoTemplate";
}
