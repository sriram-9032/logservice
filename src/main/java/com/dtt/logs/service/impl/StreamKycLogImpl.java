package com.dtt.logs.service.impl;

import com.dtt.logs.Model.AuditLog;
import com.dtt.logs.service.iface.StreamKycLogIface;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class StreamKycLogImpl implements StreamKycLogIface {


    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public StreamKycLogImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<AuditLog> getAllStreamedLogsSortedByLatest() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "timestamp"));
        return reactiveMongoTemplate.find(query, AuditLog.class, "auditlogs");
    }
}
