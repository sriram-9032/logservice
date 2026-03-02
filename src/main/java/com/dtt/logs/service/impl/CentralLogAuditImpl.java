package com.dtt.logs.service.impl;

import com.dtt.logs.Model.AuditLog;
import com.dtt.logs.repository.central.CentralAuditRepository;
import com.dtt.logs.service.iface.CentralLogIface;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Qualifier("centralLogMongoTemplate")
public class CentralLogAuditImpl implements CentralLogIface {

    private final CentralAuditRepository centralAuditRepository;

    public CentralLogAuditImpl(CentralAuditRepository centralAuditRepository) {
        this.centralAuditRepository = centralAuditRepository;
    }

    public void addAudit(AuditLog auditLog) {
        auditLog.setTimestamp(LocalDateTime.now());
        centralAuditRepository.insert(auditLog);
    }

}