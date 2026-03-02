package com.dtt.logs.dto;

public class KycOrganizationDto {
    private String orgId;
    private String orgName;
    private String spocEmail;
    private String orgLogo;
    private String spocName;
    private String spocMobileNumber;

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

    @Override
    public String toString() {
        return "KycOrganizationDto{" +
                "orgId='" + orgId + '\'' +
                ", orgName='" + orgName + '\'' +
                ", spocEmail='" + spocEmail + '\'' +
                ", orgLogo='" + orgLogo + '\'' +
                ", spocName='" + spocName + '\'' +
                ", spocMobileNumber='" + spocMobileNumber + '\'' +
                '}';
    }
}
