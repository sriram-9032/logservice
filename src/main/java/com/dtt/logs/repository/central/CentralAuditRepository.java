package com.dtt.logs.repository.central;

import com.dtt.logs.Model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CentralAuditRepository extends MongoRepository<AuditLog,String> {

    @Query(value = "{serviceProviderName : ?0}")
    List<AuditLog> findBySpName(String serviceProviderName);
}
