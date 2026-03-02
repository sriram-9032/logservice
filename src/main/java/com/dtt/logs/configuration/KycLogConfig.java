package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.kyc",
        mongoTemplateRef = "kycLogMongoTemplate")
public class KycLogConfig {
    private KycLogConfig() {
    }
    protected static final String MONGO_TEMPLATE = "kycLogMongoTemplate";
}
