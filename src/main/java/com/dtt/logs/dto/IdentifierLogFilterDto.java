package com.dtt.logs.dto;

import java.util.List;

public class IdentifierLogFilterDto {
    private String identifier;
    private String logMessageType; // "SUCCESS" or "FAILED"
    private String orgId;
    private int page;
    private int perPage;
    private List<String> serviceNames;
    private String fromDate;
    private String toDate;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLogMessageType() {
        return logMessageType;
    }

    public void setLogMessageType(String logMessageType) {
        this.logMessageType = logMessageType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public List<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "IdentifierLogFilterDto{" +
                "identifier='" + identifier + '\'' +
                ", logMessageType='" + logMessageType + '\'' +
                ", orgId='" + orgId + '\'' +
                ", page=" + page +
                ", perPage=" + perPage +
                ", serviceNames=" + serviceNames +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                '}';
    }
}