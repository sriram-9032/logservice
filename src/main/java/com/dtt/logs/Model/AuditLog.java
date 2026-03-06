package com.dtt.logs.Model;

import java.time.LocalDateTime;

import com.dtt.logs.enums.central.*;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import org.springframework.format.annotation.DateTimeFormat;

import lombok.SneakyThrows;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Document("auditlogs")

@CompoundIndex(
        name = "org_service_log_timestamp_idx",
        def = "{'serviceProviderName': 1, 'serviceName': 1, 'logMessageType': 1, 'timestamp': -1}"
)


public class AuditLog {


    @Id
    private ObjectId id;

    @Field("identifier")
    @Indexed
    private String identifier;

    @NotNull
    @Field("serviceName")
    @Indexed
    private ServiceName serviceName;

    @NotNull
    @Field("transactionType")
    private TransactionType transactionType;

    @NotNull
    @Field("logMessageType")
    @Indexed
    private LogMessageType logMessageType;
    @NotNull
    @Field("authenticationType")
    @Indexed
    private AuthenticationType authenticationType;

    @Field("signatureType")
    private SignatureType signatureType;

    @NotNull
    @NotEmpty
    @Field("correlationID")
    private String correlationID;

    @NotNull
    @NotEmpty
    @Field("transactionID")
    private String transactionID;

    @Field("subTransactionID")
    private String subTransactionID;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Field("timestamp")
    @Indexed
    private LocalDateTime timestamp;


    @NotNull
    @NotEmpty
    @Field("startTime")
    private String startTime;

    @Field("endTime")
    private String endTime;

    @Field("geoLocation")
    private String geoLocation;

    @Field("callStack")
    private String callStack;

    @NotNull
    @NotEmpty
    @Field("logMessage")
    private String logMessage;

    @Field("transactionSubType")
    private String transactionSubType;

    @Field("serviceProviderName")
    private String serviceProviderName;

    @Field("serviceProviderAppName")
    private String serviceProviderAppName;

    @Field("eSealUsed")
    private Boolean eSealUsed;

    @NotNull
    @NotEmpty
    @Field("checksum")
    private String checksum;

    @Field("deviceId")
    private String deviceId;

    @Field("sdkType")
    private String sdkType;
    @Field("sdkVersion")
    private String sdkVersion;
    @Field("transactionPrice")
    private String transactionPrice;

    @Field("userActivityType")
    private UserActivityType userActivityType;
    public AuditLog() {
    }

    @PersistenceCreator
    public AuditLog(
            String identifier,
            String correlationID,
            String transactionID,
            String subTransactionID,
            LocalDateTime timestamp,
            String startTime,
            String endTime,
            String geoLocation,
            String callStack,
            ServiceName serviceName,
            TransactionType transactionType,
            String transactionSubType,
            LogMessageType logMessageType,
            String logMessage,
            String serviceProviderName,
            String serviceProviderAppName,
            SignatureType signatureType,
            Boolean eSealUsed,
            String checksum,
            String deviceId, String sdkType, String sdkVersion, String transactionPrice, AuthenticationType authenticationType, UserActivityType userActivityType) {

        this.identifier = identifier;
        this.correlationID = correlationID;
        this.transactionID = transactionID;
        this.subTransactionID = subTransactionID;
        this.timestamp = timestamp;
        this.startTime = startTime;
        this.endTime = endTime;
        this.geoLocation = geoLocation;
        this.callStack = callStack;
        this.serviceName = serviceName;
        this.transactionType = transactionType;
        this.transactionSubType = transactionSubType;
        this.logMessageType = logMessageType;
        this.logMessage = logMessage;
        this.serviceProviderName = serviceProviderName;
        this.serviceProviderAppName = serviceProviderAppName;
        this.signatureType = signatureType;
        this.eSealUsed = eSealUsed;
        this.checksum = checksum;
        this.deviceId = deviceId;
        this.sdkVersion = sdkVersion;
        this.sdkType = sdkType;
        this.transactionPrice = transactionPrice;
        this.authenticationType = authenticationType;
        this.userActivityType = userActivityType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public void setServiceName(ServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LogMessageType getLogMessageType() {
        return logMessageType;
    }

    public void setLogMessageType(LogMessageType logMessageType) {
        this.logMessageType = logMessageType;
    }

    public SignatureType getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(SignatureType signatureType) {
        this.signatureType = signatureType;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getTransactionSubType() {
        return transactionSubType;
    }

    public void setTransactionSubType(String transactionSubType) {
        this.transactionSubType = transactionSubType;
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

    public Boolean geteSealUsed() {
        return eSealUsed;
    }

    public void seteSealUsed(Boolean eSealUsed) {
        this.eSealUsed = eSealUsed;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

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

    public @NotNull AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(@NotNull AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public UserActivityType getUserActivityType() {
        return userActivityType;
    }

    public void setUserActivityType(UserActivityType userActivityType) {
        this.userActivityType = userActivityType;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @SneakyThrows
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", id);
            jsonObject.put("identifier", identifier);
            jsonObject.put("serviceName", serviceName);
            jsonObject.put("transactionType", transactionType);
            jsonObject.put("signatureType", signatureType);
            jsonObject.put("correlationID", correlationID);
            jsonObject.put("transactionID", transactionID);
            jsonObject.put("subTransactionID", subTransactionID);
            jsonObject.put("timestamp", timestamp);
            jsonObject.put("startTime", startTime);
            jsonObject.put("endTime", endTime);
            jsonObject.put("geoLocation", geoLocation);
            jsonObject.put("callStack", callStack);
            jsonObject.put("logMessage", logMessage);
            jsonObject.put("logMessageType", logMessageType);
            jsonObject.put("transactionSubType", transactionSubType);
            jsonObject.put("serviceProviderName", serviceProviderName);
            jsonObject.put("serviceProviderAppName", serviceProviderAppName);
            jsonObject.put("eSealUsed", eSealUsed);
            jsonObject.put("checksum", checksum);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("sdkVersion", sdkVersion);
            jsonObject.put("sdkType", sdkType);
            jsonObject.put("transactionPrice", transactionPrice);
            jsonObject.put("authenticationType", authenticationType);
            jsonObject.put("userActivityType", userActivityType);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject.toString();
        }
    }
}
