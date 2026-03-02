package com.dtt.logs.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "organization-list")
public class KycOrganization {


    @Id
    private String id;

    private String orgId;
    private String orgName;
    private String spocEmail;
    private String orgLogo;
    private String spocName;
    private String spocMobileNumber;

    private LocalDateTime inserted_at;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSpocEmail() {
        return spocEmail;
    }

    public void setSpocEmail(String spocEmail) {
        this.spocEmail = spocEmail;
    }

    public String getOrgLogo() {
        return orgLogo;
    }

    public void setOrgLogo(String orgLogo) {
        this.orgLogo = orgLogo;
    }

    public String getSpocName() {
        return spocName;
    }

    public void setSpocName(String spocName) {
        this.spocName = spocName;
    }

    public String getSpocMobileNumber() {
        return spocMobileNumber;
    }

    public void setSpocMobileNumber(String spocMobileNumber) {
        this.spocMobileNumber = spocMobileNumber;
    }

    public LocalDateTime getInserted_at() {
        return inserted_at;
    }

    public void setInserted_at(LocalDateTime inserted_at) {
        this.inserted_at = inserted_at;
    }

    @Override
    public String toString() {
        return "KycOrganization{" +
                "id='" + id + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgName='" + orgName + '\'' +
                ", spocEmail='" + spocEmail + '\'' +
                ", orgLogo='" + orgLogo + '\'' +
                ", spocName='" + spocName + '\'' +
                ", spocMobileNumber='" + spocMobileNumber + '\'' +
                ", inserted_at=" + inserted_at +
                '}';
    }
}
