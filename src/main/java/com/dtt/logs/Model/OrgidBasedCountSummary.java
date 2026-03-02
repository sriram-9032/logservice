package com.dtt.logs.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "orgid-based-count")
public class OrgidBasedCountSummary {

    @Id
    private String id;   // "orgId-serviceName-yyyy-MM-dd"

    @Field("identifier")
    private String identifier;   // yyyy-MM-dd

    @Field("serviceName")
    private String serviceName;      // serviceName

    @Field("orgId")
    private String orgId;        // orgId

    @Field("kycSuccCount")
    private int kycSuccCount;

    @Field("kycFailCount")
    private int kycFailCount;

    @Field("updatedTime")
    private String updatedTime;

    public OrgidBasedCountSummary() {
    }

    public OrgidBasedCountSummary(String id, String identifier, String kycType, String orgId, int kycSuccCount, int kycFailCount, String updatedTime) {
        this.id = id;
        this.identifier = identifier;
        this.serviceName = kycType;
        this.orgId = orgId;
        this.kycSuccCount = kycSuccCount;
        this.kycFailCount = kycFailCount;
        this.updatedTime = updatedTime;
    }

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

    public int getKycSuccCount() {
        return kycSuccCount;
    }

    public void setKycSuccCount(int kycSuccCount) {
        this.kycSuccCount = kycSuccCount;
    }

    public int getKycFailCount() {
        return kycFailCount;
    }

    public void setKycFailCount(int kycFailCount) {
        this.kycFailCount = kycFailCount;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}

