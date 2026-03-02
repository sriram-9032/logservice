package com.dtt.logs.dto.central;

import com.dtt.logs.enums.central.TransactionStatus;

public class AuthPageDTO {
    private String identifier;
    private String startDate;
    private String endDate;
    private TransactionStatus transactionStatus;
    private Integer perPage;


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

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

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    @Override
    public String toString() {
        return "AuthPageDTO{" +
                "identifier='" + identifier + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", perPage=" + perPage +
                '}';
    }
}

