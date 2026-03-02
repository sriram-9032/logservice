package com.dtt.logs.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection ="servicename-based-count")
public class KycServiceNameBasedCount {
    @Id
    private String id;

    private String identifier; // Date in yyyy-MM-dd
    private String serviceName;
    private String orgId; // Can be null
    private int kycSucccount;
    private int kycFailcount;

    private String updatedTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getKycSucccount() {
        return kycSucccount;
    }

    public void setKycSucccount(int kycSucccount) {
        this.kycSucccount = kycSucccount;
    }

    public int getKycFailcount() {
        return kycFailcount;
    }

    public void setKycFailcount(int kycFailcount) {
        this.kycFailcount = kycFailcount;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "ServiceNameBasedCount{" +
                "id='" + id + '\'' +
                ", identifier='" + identifier + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", orgId='" + orgId + '\'' +
                ", kycSucccount=" + kycSucccount +
                ", kycFailcount=" + kycFailcount +
                ", updatedTime='" + updatedTime + '\'' +
                '}';
    }
}
