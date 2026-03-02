package com.dtt.logs.service.iface;

import com.dtt.logs.Model.ServiceAuditLog;
import com.dtt.logs.dto.ApiResponse;

public interface AuthnLogIface {
    public void addAudit(ServiceAuditLog auditLog);

    public ApiResponse getAuthenticationTypeCount(String identifier);
}

