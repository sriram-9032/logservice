package com.dtt.logs.dto;

import com.dtt.logs.Model.AdminAuditLog;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("auditlogs")
public class KycAuditDto implements Serializable {

    @Indexed
    private String identifier;
    private String correlationID;
    private String transactionID;
    private String subTransactionID;
    private String startTime;
    private String endTime;
    private String geoLocation;
    private String callStack;
    @Indexed
    private String serviceName;
    private String transactionType;
    private String transactionSubType;
    @Indexed
    private AdminAuditLog.LogMessageType logMessageType;
    private String logMessage;
    private String serviceProviderName;
    private String serviceProviderAppName;
    private String signatureType;
    private boolean eSealUsed;
    private String checksum;
    @Indexed
    private String timestamp;
    private String deviceId;
    private String sdkType;
    private String sdkVersion;
    private String transactionPrice;

    // Getters and Setters


    public String getSdkType() {
        return sdkType;
    }

    public void setSdkType(String sdkType) {
        this.sdkType = sdkType;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(String transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getSubTransactionID() {
        return subTransactionID;
    }

    public void setSubTransactionID(String subTransactionID) {
        this.subTransactionID = subTransactionID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getCallStack() {
        return callStack;
    }

    public void setCallStack(String callStack) {
        this.callStack = callStack;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionSubType() {
        return transactionSubType;
    }

    public void setTransactionSubType(String transactionSubType) {
        this.transactionSubType = transactionSubType;
    }

    public AdminAuditLog.LogMessageType getLogMessageType() {
        return logMessageType;
    }

    public void setLogMessageType(AdminAuditLog.LogMessageType logMessageType) {
        this.logMessageType = logMessageType;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getServiceProviderAppName() {
        return serviceProviderAppName;
    }

    public void setServiceProviderAppName(String serviceProviderAppName) {
        this.serviceProviderAppName = serviceProviderAppName;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public boolean iseSealUsed() {
        return eSealUsed;
    }

    public void seteSealUsed(boolean eSealUsed) {
        this.eSealUsed = eSealUsed;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "KycAuditDto{" +
                ", identifier='" + identifier + '\'' +
                ", correlationID='" + correlationID + '\'' +
                ", transactionID='" + transactionID + '\'' +
                ", subTransactionID='" + subTransactionID + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", geoLocation='" + geoLocation + '\'' +
                ", callStack='" + callStack + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", transactionSubType='" + transactionSubType + '\'' +
                ", logMessageType=" + logMessageType +
                ", logMessage='" + logMessage + '\'' +
                ", serviceProviderName='" + serviceProviderName + '\'' +
                ", serviceProviderAppName='" + serviceProviderAppName + '\'' +
                ", signatureType='" + signatureType + '\'' +
                ", eSealUsed=" + eSealUsed +
                ", checksum='" + checksum + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sdkType='" + sdkType + '\'' +
                ", sdkVersion='" + sdkVersion + '\'' +
                ", transactionPrice='" + transactionPrice + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
