package com.dtt.logs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycMonthlyStats {
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

    @Override
    public String toString() {
        return "KycMonthlyStats{" +
                "successCount=" + successCount +
                ", failedCount=" + failedCount +
                '}';
    }
}
