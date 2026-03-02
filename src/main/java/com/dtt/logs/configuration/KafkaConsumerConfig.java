package com.dtt.logs.configuration;

import com.dtt.logs.Model.AuditLog;
import com.dtt.logs.dto.AuditDto;
import com.dtt.logs.dto.KycAuditDto;
import com.dtt.logs.dto.central.AuditDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;


import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;

import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import org.springframework.util.backoff.FixedBackOff;

import org.apache.kafka.common.serialization.ByteArraySerializer;

import org.apache.kafka.common.serialization.StringSerializer;

import org.apache.kafka.common.serialization.Serializer;

import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;

import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Value("${spring.kafka.consumer.group-id}")
    private String GROUP_ID;


    private Map<String, Object> commonConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return props;
    }

    private ObjectMapper baseObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Handle enum = "" cases
        mapper.coercionConfigFor(Enum.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        return mapper;
    }

    @Bean
    public ProducerFactory<Object, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        // 1. Setup specific serializers
        ByteArraySerializer byteSerializer = new ByteArraySerializer();
        JsonSerializer<Object> jsonSerializer = new JsonSerializer<>(baseObjectMapper());

        // 2. Map types EXPLICITLY
        // We add your specific DTO classes to the map so there is no "No matching delegate" error
        Map<Class<?>, Serializer<?>> delegates = new LinkedHashMap<>();
        delegates.put(byte[].class, byteSerializer);
        delegates.put(com.dtt.logs.dto.AuditDto.class, jsonSerializer);
        delegates.put(com.dtt.logs.dto.KycAuditDto.class, jsonSerializer);
        delegates.put(com.dtt.logs.dto.service.AuditDTO.class, jsonSerializer);
        delegates.put(com.dtt.logs.Model.AuditLog.class, jsonSerializer);

        // Fallback for any other objects
        delegates.put(Object.class, jsonSerializer);

        DelegatingByTypeSerializer delegatingSerializer = new DelegatingByTypeSerializer(delegates);

        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(configProps);
        factory.setKeySerializer((Serializer)new StringSerializer());
        factory.setValueSerializer(delegatingSerializer);

        return factory;
    }

    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

@Bean
public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
    // 1. This recoverer actually pushes the bad message to the DLT topic
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
            (records, ex) -> {
                System.out.println("Moving failed message to DLT: " + records.offset());
                return new TopicPartition("central-log-DLT", -1);
            });


    FixedBackOff backOff = new FixedBackOff(0L, 0L);

    DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);

    handler.addNotRetryableExceptions(
            org.springframework.kafka.support.serializer.DeserializationException.class,
            org.apache.kafka.common.errors.SerializationException.class,
            com.mongodb.MongoTimeoutException.class,
            java.net.ConnectException.class
    );

    return handler;
}



    @Bean
    public ConsumerFactory<String, AuditDto> auditDtoConsumerFactory() {
        Map<String, Object> props = commonConsumerConfigs();

        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.dtt.logs.dto.AuditDto");


        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuditDto> auditDtoContainerFactory(KafkaTemplate<Object, Object> template) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, AuditDto>();
        factory.setConsumerFactory(auditDtoConsumerFactory());
        factory.setCommonErrorHandler(errorHandler(template));
        return factory;
    }


    @Bean
    public ConsumerFactory<String, AuditDTO> auditDTOCentralConsumerFactory() {
        Map<String, Object> props = commonConsumerConfigs();

        // We must tell it which DTO class to use since we are using properties only
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.dtt.logs.dto.central.AuditDTO");

        return new DefaultKafkaConsumerFactory<>(props);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuditDTO> auditDTOCentralContainerFactory(KafkaTemplate<Object, Object> template) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, AuditDTO>();
        factory.setConsumerFactory(auditDTOCentralConsumerFactory());
        factory.setCommonErrorHandler(errorHandler(template));
        return factory;
    }


    @Bean
    public ConsumerFactory<String, com.dtt.logs.dto.service.AuditDTO> auditDTOServiceConsumerFactory() {
        Map<String, Object> props = commonConsumerConfigs();

        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.dtt.logs.dto.service.AuditDTO");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, com.dtt.logs.dto.service.AuditDTO> auditDTOServiceContainerFactory(KafkaTemplate<Object, Object> template) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, com.dtt.logs.dto.service.AuditDTO>();
        factory.setConsumerFactory(auditDTOServiceConsumerFactory());
        factory.setCommonErrorHandler(errorHandler(template));
        return factory;
    }


@Bean
public ConsumerFactory<String, KycAuditDto> kycAuditDtoConsumerFactory() {
    Map<String, Object> props = commonConsumerConfigs();

    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.dtt.logs.dto.KycAuditDto");

    return new DefaultKafkaConsumerFactory<>(props);
}




        @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KycAuditDto> kycAuditDtoContainerFactory(KafkaTemplate<Object, Object> template) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, KycAuditDto>();
        factory.setConsumerFactory(kycAuditDtoConsumerFactory());
        factory.setCommonErrorHandler(errorHandler(template));
        return factory;
    }



    @Bean
    public ConsumerFactory<String, AuditLog> auditLogConsumerFactory() {
        Map<String, Object> props = commonConsumerConfigs();

        // We must tell it which DTO class to use since we are using properties only
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.dtt.logs.Model.AuditLog");

        // Use the constructor that ONLY takes the map.
        // Do NOT pass any Deserializer objects here.
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuditLog> auditLogContainerFactory(KafkaTemplate<Object, Object> template) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, AuditLog>();
        factory.setConsumerFactory(auditLogConsumerFactory());
        factory.setCommonErrorHandler(errorHandler(template));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> dltConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "central-logs-dlt-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // Ensure it doesn't try to use any auto-configured JSON settings
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> dltContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        // Link the string-based consumer factory here
        factory.setConsumerFactory(dltConsumerFactory());

        // No ErrorHandler/DLT needed here to avoid the recursive loop error
        return factory;
    }
}
