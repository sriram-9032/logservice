package com.dtt.logs.repository.enterprise;

import com.dtt.logs.Model.AdminAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnterpriseRepo extends MongoRepository<AdminAuditLog,String> {

}

