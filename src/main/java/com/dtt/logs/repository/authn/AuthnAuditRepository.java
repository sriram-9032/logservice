package com.dtt.logs.repository.authn;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.dtt.logs.Model.ServiceAuditLog;

import java.util.List;

@Repository
public interface AuthnAuditRepository extends MongoRepository<ServiceAuditLog,String> {
    @Query(value = "{serviceProviderName : ?0}")
    List<ServiceAuditLog> findBySpName(String serviceProviderName);

    @Query(value= "{identifier : ?0}")
    List<ServiceAuditLog> findByIdentifier(String identifier);
}

