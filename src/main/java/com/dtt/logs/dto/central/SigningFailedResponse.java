package com.dtt.logs.dto.central;

import com.dtt.logs.Model.AuditLog;

import java.util.List;

public class SigningFailedResponse {
    private List<AuditLog> auditLogList;

    public List<AuditLog> getAuditLogList() {
        return auditLogList;
    }

    public void setAuditLogList(List<AuditLog> auditLogList) {
        this.auditLogList = auditLogList;
    }
}
