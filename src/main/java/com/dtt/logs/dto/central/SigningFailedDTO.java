package com.dtt.logs.dto.central;

import com.dtt.logs.enums.central.TransactionStatus;

public class SigningFailedDTO {

    private String startDate;
    private String endDate;
    private TransactionStatus transactionStatus;


    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getStartDate() {
        return (startDate);
    }

    public void setStartDate(String startDate) {
        this.startDate = (startDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return "SigningFailedDTO{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", transactionStatus=" + transactionStatus +
                '}';
    }
}

