package com.dtt.logs.dto;

import java.util.List;

public class KycTransactionDto {
    private String identifier;
    private List<String> serviceName;
    private String date;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<String> getServiceName() {
        return serviceName;
    }

    public void setServiceName(List<String> serviceName) {
        this.serviceName = serviceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "KycTransactionDto{" +
                "identifier='" + identifier + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
