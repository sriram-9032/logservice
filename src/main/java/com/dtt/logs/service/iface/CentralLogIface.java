package com.dtt.logs.service.iface;

import com.dtt.logs.Model.AuditLog;


public interface CentralLogIface {

    public void addAudit(AuditLog auditLog);
}
