package com.dtt.logs.Model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("daycountspmodels")
public class CountSPLog {
    
    private int countOfSignaturesWithXadesSuccess;
    
    private int countOfSignaturesWithXadesFailed;
    
    private int countOfSignaturesWithPadesSuccess;
    
    private int countOfSignaturesWithPadesFailed;
    
    private int countOfSignaturesWithCadesSuccess;
    
    private int countOfSignaturesWithCadesFailed;
    
    private int countOfSignaturesWithESealSuccess;
    
    private int countOfSignaturesWithESealFailed;
    
    private int countOfSignaturesWithDataSuccess;
    
    private int countOfSignaturesWithDataFailed;
    
    private int countOfAuthenticationsSuccess;
    
    private int countOfAuthenticationsFailed;
    private LocalDateTime lastUpdatedOn;
    
    private String serviceProviderName;
    
//    @Indexed(unique = true)
    private String identifier;


    public int getCountOfSignaturesWithXadesSuccess() {
        return countOfSignaturesWithXadesSuccess;
    }

    public void setCountOfSignaturesWithXadesSuccess(int countOfSignaturesWithXadesSuccess) {
        this.countOfSignaturesWithXadesSuccess = countOfSignaturesWithXadesSuccess;
    }

    public int getCountOfSignaturesWithXadesFailed() {
        return countOfSignaturesWithXadesFailed;
    }

    public void setCountOfSignaturesWithXadesFailed(int countOfSignaturesWithXadesFailed) {
        this.countOfSignaturesWithXadesFailed = countOfSignaturesWithXadesFailed;
    }

    public int getCountOfSignaturesWithPadesSuccess() {
        return countOfSignaturesWithPadesSuccess;
    }

    public void setCountOfSignaturesWithPadesSuccess(int countOfSignaturesWithPadesSuccess) {
        this.countOfSignaturesWithPadesSuccess = countOfSignaturesWithPadesSuccess;
    }

    public int getCountOfSignaturesWithPadesFailed() {
        return countOfSignaturesWithPadesFailed;
    }

    public void setCountOfSignaturesWithPadesFailed(int countOfSignaturesWithPadesFailed) {
        this.countOfSignaturesWithPadesFailed = countOfSignaturesWithPadesFailed;
    }

    public int getCountOfSignaturesWithCadesSuccess() {
        return countOfSignaturesWithCadesSuccess;
    }

    public void setCountOfSignaturesWithCadesSuccess(int countOfSignaturesWithCadesSuccess) {
        this.countOfSignaturesWithCadesSuccess = countOfSignaturesWithCadesSuccess;
    }

    public int getCountOfSignaturesWithCadesFailed() {
        return countOfSignaturesWithCadesFailed;
    }

    public void setCountOfSignaturesWithCadesFailed(int countOfSignaturesWithCadesFailed) {
        this.countOfSignaturesWithCadesFailed = countOfSignaturesWithCadesFailed;
    }

    public int getCountOfSignaturesWithESealSuccess() {
        return countOfSignaturesWithESealSuccess;
    }

    public void setCountOfSignaturesWithESealSuccess(int countOfSignaturesWithESealSuccess) {
        this.countOfSignaturesWithESealSuccess = countOfSignaturesWithESealSuccess;
    }

    public int getCountOfSignaturesWithESealFailed() {
        return countOfSignaturesWithESealFailed;
    }

    public void setCountOfSignaturesWithESealFailed(int countOfSignaturesWithESealFailed) {
        this.countOfSignaturesWithESealFailed = countOfSignaturesWithESealFailed;
    }

    public int getCountOfSignaturesWithDataSuccess() {
        return countOfSignaturesWithDataSuccess;
    }

    public void setCountOfSignaturesWithDataSuccess(int countOfSignaturesWithDataSuccess) {
        this.countOfSignaturesWithDataSuccess = countOfSignaturesWithDataSuccess;
    }

    public int getCountOfSignaturesWithDataFailed() {
        return countOfSignaturesWithDataFailed;
    }

    public void setCountOfSignaturesWithDataFailed(int countOfSignaturesWithDataFailed) {
        this.countOfSignaturesWithDataFailed = countOfSignaturesWithDataFailed;
    }

    public int getCountOfAuthenticationsSuccess() {
        return countOfAuthenticationsSuccess;
    }

    public void setCountOfAuthenticationsSuccess(int countOfAuthenticationsSuccess) {
        this.countOfAuthenticationsSuccess = countOfAuthenticationsSuccess;
    }

    public int getCountOfAuthenticationsFailed() {
        return countOfAuthenticationsFailed;
    }

    public void setCountOfAuthenticationsFailed(int countOfAuthenticationsFailed) {
        this.countOfAuthenticationsFailed = countOfAuthenticationsFailed;
    }

    public LocalDateTime getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(LocalDateTime lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "CountSPLog{" +
                "countOfSignaturesWithXadesSuccess=" + countOfSignaturesWithXadesSuccess +
                ", countOfSignaturesWithXadesFailed=" + countOfSignaturesWithXadesFailed +
                ", countOfSignaturesWithPadesSuccess=" + countOfSignaturesWithPadesSuccess +
                ", countOfSignaturesWithPadesFailed=" + countOfSignaturesWithPadesFailed +
                ", countOfSignaturesWithCadesSuccess=" + countOfSignaturesWithCadesSuccess +
                ", countOfSignaturesWithCadesFailed=" + countOfSignaturesWithCadesFailed +
                ", countOfSignaturesWithESealSuccess=" + countOfSignaturesWithESealSuccess +
                ", countOfSignaturesWithESealFailed=" + countOfSignaturesWithESealFailed +
                ", countOfSignaturesWithDataSuccess=" + countOfSignaturesWithDataSuccess +
                ", countOfSignaturesWithDataFailed=" + countOfSignaturesWithDataFailed +
                ", countOfAuthenticationsSuccess=" + countOfAuthenticationsSuccess +
                ", countOfAuthenticationsFailed=" + countOfAuthenticationsFailed +
                ", lastUpdatedOn=" + lastUpdatedOn +
                ", serviceProviderName='" + serviceProviderName + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}