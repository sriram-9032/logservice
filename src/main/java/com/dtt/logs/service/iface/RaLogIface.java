package com.dtt.logs.service.iface;

import com.dtt.logs.Model.ServiceAuditLog;

public interface RaLogIface {
    public void addAudit(ServiceAuditLog auditLog);
}