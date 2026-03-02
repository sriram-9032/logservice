package com.dtt.logs.dto.service;


import com.dtt.logs.enums.service.TransactionStatus;

public class LogsDTO {

    private String startDate;
    private String endDate;
    private String transactionType;
    private TransactionStatus transactionStatus;
    private Integer perPage;


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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "LogsDTO{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", perPage=" + perPage +
                '}';
    }
}

