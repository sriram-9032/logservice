package com.dtt.logs.dto;

import lombok.Data;
import java.util.List;

@Data
public class KycLogFilterDto {
    private String organisationId;
    private List<String> serviceNames;
    private String timestamp;
    private List<String> logMessageTypes;
    private Boolean groupByService;
    private Integer page = 0;
    private Integer size = 10;

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public List<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getLogMessageTypes() {
        return logMessageTypes;
    }

    public void setLogMessageTypes(List<String> logMessageTypes) {
        this.logMessageTypes = logMessageTypes;
    }

    public Boolean getGroupByService() {
        return groupByService;
    }

    public void setGroupByService(Boolean groupByService) {
        this.groupByService = groupByService;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
