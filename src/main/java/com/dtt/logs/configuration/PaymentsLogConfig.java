package com.dtt.logs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.dtt.logs.repository.payments",
        mongoTemplateRef = "paymentsLogMongoTemplate")
public class PaymentsLogConfig {
    private PaymentsLogConfig() {
    }

    protected static final String MONGO_TEMPLATE = "paymentsLogMongoTemplate";
}
