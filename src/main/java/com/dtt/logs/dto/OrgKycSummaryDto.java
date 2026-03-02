package com.dtt.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgKycSummaryDto {
    private String orgName;
    private String orgId;
    private String orgLogo;
    private String spocEmail;
    private String spocName;
    private String spocMobileNumber;

    private List<String> kycMethods; // Ignored for now

    private long totalKycCountSuccessful;
    private long totalKycCountFailed;
    private long totalKycCountSuccessfulCurrentMonth;
    private long totalKycCountFailedCurrentMonth;
    private long totalKycCountSuccessfulCurrentYear;
    private long totalKycCountFailedCurrentYear;

    private LocalDateTime lastKycSuccessfulTimestamp;
    private LocalDateTime lastKycFailedTimestamp;

    private Map<String, KycMonthlyStats> monthlyStats;

    private Map<String, KycStatsDto> serviceStats;


    public Map<String, KycStatsDto> getServiceStats() {
        return serviceStats;
    }

    public void setServiceStats(Map<String, KycStatsDto> serviceStats) {
        this.serviceStats = serviceStats;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgLogo() {
        return orgLogo;
    }

    public void setOrgLogo(String orgLogo) {
        this.orgLogo = orgLogo;
    }

    public String getSpocEmail() {
        return spocEmail;
    }

    public void setSpocEmail(String spocEmail) {
        this.spocEmail = spocEmail;
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

    public List<String> getKycMethods() {
        return kycMethods;
    }

    public void setKycMethods(List<String> kycMethods) {
        this.kycMethods = kycMethods;
    }

    public long getTotalKycCountSuccessful() {
        return totalKycCountSuccessful;
    }

    public void setTotalKycCountSuccessful(long totalKycCountSuccessful) {
        this.totalKycCountSuccessful = totalKycCountSuccessful;
    }

    public long getTotalKycCountFailed() {
        return totalKycCountFailed;
    }

    public void setTotalKycCountFailed(long totalKycCountFailed) {
        this.totalKycCountFailed = totalKycCountFailed;
    }

    public long getTotalKycCountSuccessfulCurrentMonth() {
        return totalKycCountSuccessfulCurrentMonth;
    }

    public void setTotalKycCountSuccessfulCurrentMonth(long totalKycCountSuccessfulCurrentMonth) {
        this.totalKycCountSuccessfulCurrentMonth = totalKycCountSuccessfulCurrentMonth;
    }

    public long getTotalKycCountFailedCurrentMonth() {
        return totalKycCountFailedCurrentMonth;
    }

    public void setTotalKycCountFailedCurrentMonth(long totalKycCountFailedCurrentMonth) {
        this.totalKycCountFailedCurrentMonth = totalKycCountFailedCurrentMonth;
    }

    public long getTotalKycCountSuccessfulCurrentYear() {
        return totalKycCountSuccessfulCurrentYear;
    }

    public void setTotalKycCountSuccessfulCurrentYear(long totalKycCountSuccessfulCurrentYear) {
        this.totalKycCountSuccessfulCurrentYear = totalKycCountSuccessfulCurrentYear;
    }

    public long getTotalKycCountFailedCurrentYear() {
        return totalKycCountFailedCurrentYear;
    }

    public void setTotalKycCountFailedCurrentYear(long totalKycCountFailedCurrentYear) {
        this.totalKycCountFailedCurrentYear = totalKycCountFailedCurrentYear;
    }

    public LocalDateTime getLastKycSuccessfulTimestamp() {
        return lastKycSuccessfulTimestamp;
    }

    public void setLastKycSuccessfulTimestamp(LocalDateTime lastKycSuccessfulTimestamp) {
        this.lastKycSuccessfulTimestamp = lastKycSuccessfulTimestamp;
    }

    public LocalDateTime getLastKycFailedTimestamp() {
        return lastKycFailedTimestamp;
    }

    public void setLastKycFailedTimestamp(LocalDateTime lastKycFailedTimestamp) {
        this.lastKycFailedTimestamp = lastKycFailedTimestamp;
    }

    public Map<String, KycMonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(Map<String, KycMonthlyStats> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    @Override
    public String toString() {
        return "OrgKycSummaryDto{" +
                "orgName='" + orgName + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgLogo='" + orgLogo + '\'' +
                ", spocEmail='" + spocEmail + '\'' +
                ", spocName='" + spocName + '\'' +
                ", spocMobileNumber='" + spocMobileNumber + '\'' +
                ", kycMethods=" + kycMethods +
                ", totalKycCountSuccessful=" + totalKycCountSuccessful +
                ", totalKycCountFailed=" + totalKycCountFailed +
                ", totalKycCountSuccessfulCurrentMonth=" + totalKycCountSuccessfulCurrentMonth +
                ", totalKycCountFailedCurrentMonth=" + totalKycCountFailedCurrentMonth +
                ", totalKycCountSuccessfulCurrentYear=" + totalKycCountSuccessfulCurrentYear +
                ", totalKycCountFailedCurrentYear=" + totalKycCountFailedCurrentYear +
                ", lastKycSuccessfulTimestamp=" + lastKycSuccessfulTimestamp +
                ", lastKycFailedTimestamp=" + lastKycFailedTimestamp +
                ", monthlyStats=" + monthlyStats +
                ", serviceStats=" + serviceStats +
                '}';
    }
}
