package com.dtt.logs.dto.service;

import com.dtt.logs.Model.ServiceAuditLog;

import java.util.List;

public class SigningFailedResponse {
    private List<ServiceAuditLog> auditLogList;

    public List<ServiceAuditLog> getAuditLogList() {
        return auditLogList;
    }

    public void setAuditLogList(List<ServiceAuditLog> auditLogList) {
        this.auditLogList = auditLogList;
    }
}
