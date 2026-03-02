package com.dtt.logs.repository.kyc;

import com.dtt.logs.Model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface KycAuditRepository extends MongoRepository<AuditLog,String> {

}