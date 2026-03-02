package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.central",
        mongoTemplateRef = "centralLogMongoTemplate")
public class CentralLogConfig {
    private CentralLogConfig() {
    }
    protected static final String MONGO_TEMPLATE = "centralLogMongoTemplate";
}
