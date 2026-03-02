package com.dtt.logs.repository.admin;

import com.dtt.logs.Model.AdminAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminAuditRepository extends MongoRepository<AdminAuditLog,String> {

}
