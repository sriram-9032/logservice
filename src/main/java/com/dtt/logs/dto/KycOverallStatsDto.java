package com.dtt.logs.dto;

import com.dtt.logs.configuration.MonthlyStatsSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

public class KycOverallStatsDto {

    private long totalServiceProviders;
    private long totalSuccessfulKycs;
    private long totalFailedKycs;
    private long newServiceProvidersThisMonth;
    private long totalSuccessfulKycsThisMonth;
    private long totalKycsThisMonth;

    private Map<String, Map<String, Map<String, Long>>> serviceMonthlyStats;

    private Map<String, KycStatsDto> serviceStats;

    @JsonSerialize(using = MonthlyStatsSerializer.class)
    private Map<String, Map<String, Long>> monthlyStats;

    public long getTotalServiceProviders() { return totalServiceProviders; }
    public void setTotalServiceProviders(long totalServiceProviders) { this.totalServiceProviders = totalServiceProviders; }

    public long getTotalSuccessfulKycs() { return totalSuccessfulKycs; }
    public void setTotalSuccessfulKycs(long totalSuccessfulKycs) { this.totalSuccessfulKycs = totalSuccessfulKycs; }

    public long getTotalFailedKycs() { return totalFailedKycs; }
    public void setTotalFailedKycs(long totalFailedKycs) { this.totalFailedKycs = totalFailedKycs; }

    public long getNewServiceProvidersThisMonth() { return newServiceProvidersThisMonth; }
    public void setNewServiceProvidersThisMonth(long newServiceProvidersThisMonth) { this.newServiceProvidersThisMonth = newServiceProvidersThisMonth; }

    public long getTotalSuccessfulKycsThisMonth() { return totalSuccessfulKycsThisMonth; }
    public void setTotalSuccessfulKycsThisMonth(long totalSuccessfulKycsThisMonth) { this.totalSuccessfulKycsThisMonth = totalSuccessfulKycsThisMonth; }

    public long getTotalKycsThisMonth() { return totalKycsThisMonth; }
    public void setTotalKycsThisMonth(long totalKycsThisMonth) { this.totalKycsThisMonth = totalKycsThisMonth; }

    public Map<String, Map<String, Long>> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(Map<String, Map<String, Long>> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    public Map<String, Map<String, Map<String, Long>>> getServiceMonthlyStats() {
        return serviceMonthlyStats;
    }

    public void setServiceMonthlyStats(Map<String, Map<String, Map<String, Long>>> serviceMonthlyStats) {
        this.serviceMonthlyStats = serviceMonthlyStats;
    }

    public Map<String, KycStatsDto> getServiceStats() {
        return serviceStats;
    }

    public void setServiceStats(Map<String, KycStatsDto> serviceStats) {
        this.serviceStats = serviceStats;
    }

    @Override
    public String toString() {
        return "KycOverallStatsDto{" +
                "totalServiceProviders=" + totalServiceProviders +
                ", totalSuccessfulKycs=" + totalSuccessfulKycs +
                ", totalFailedKycs=" + totalFailedKycs +
                ", newServiceProvidersThisMonth=" + newServiceProvidersThisMonth +
                ", totalSuccessfulKycsThisMonth=" + totalSuccessfulKycsThisMonth +
                ", totalKycsThisMonth=" + totalKycsThisMonth +
                ", serviceMonthlyStats=" + serviceMonthlyStats +
                ", serviceStats=" + serviceStats +
                ", monthlyStats=" + monthlyStats +
                '}';
    }
}

