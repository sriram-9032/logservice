package com.dtt.logs.dto;

public class KycStatsDto {
    private long successCount;
    private long failedCount;

    public long getSuccessCount() {
        return successCount;
    }
    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }
    public long getFailedCount() {
        return failedCount;
    }
    public void setFailedCount(long failedCount) {
        this.failedCount = failedCount;
    }
}

