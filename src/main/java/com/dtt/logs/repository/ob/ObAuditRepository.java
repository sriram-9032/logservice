package com.dtt.logs.repository.ob;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.dtt.logs.Model.ServiceAuditLog;

import java.util.List;

@Repository
public interface ObAuditRepository extends MongoRepository<ServiceAuditLog,String> {
    @Query(value = "{serviceProviderName : ?0}")
    List<ServiceAuditLog> findBySpName(String serviceProviderName);

    @Query(value= "{identifier : ?0}")
    List<ServiceAuditLog> findByIdentifier(String identifier);
}

