package com.dtt.logs.service.iface;

import com.dtt.logs.Model.ServiceAuditLog;

public interface SigningLogIface {
    public void addAudit(ServiceAuditLog auditLog);
}
