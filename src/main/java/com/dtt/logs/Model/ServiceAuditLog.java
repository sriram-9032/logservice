package com.dtt.logs.Model;

import java.time.LocalDateTime;

import com.dtt.logs.enums.central.AuthenticationType;
import com.dtt.logs.enums.central.UserActivityType;
import jakarta.validation.constraints.NotEmpty;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;



import lombok.SneakyThrows;



@Document("auditlogs")
public class ServiceAuditLog {

    public enum ServiceName{
        CARD_AND_FACE_STATUS_WITH_OCR,
        CARD_STATUS_WITH_EID_READER,
        CARD_STATUS_WITH_OCR,
        CARD_AND_FACE_AUTH_WITH_OCR,
        CARD_AND_FACE_VERIFY_WITH_OCR,
        BATCH_CARD_STATUS,
        CARD_AND_FINGERPRINT_VERIFICATION_WITH_EID_READER_AND_BIOMETRIC_SENSOR,
        CARD_STATUS_WITH_MANUAL_ENTRY,
        CARD_AND_FACE_STATUS_REMOTE_VERIFICATION,
        CARD_AND_FACE_AUTH_WITH_MANUAL_ENTRY,
        CARD_AND_FACE_VERIFY_WITH_MANUAL_ENTRY,
        CARD_AND_FINGERPRINT_STATUS_WITH_MANUAL_ENTRY,
        PASSPORT_STATUS_WITH_MANUAL_ENTRY,
        PASSPORT_STATUS_WITH_OCR,

        PASSPORT_AND_FACE_VERIFY_OCR,
        PASSPORT_AND_FACE_VERIFY_MANUAL_ENTRY,


        SERVICE_PROVIDER_ONBOARDED,
        SERVICE_CREDITS_FEE_COLLECTION,
        USER_SUBSCRIBTION_FEE,
        SUBSCRIBER_ONBOARDED,
        CERTIFICATE_PAIR_ISSUED,
        SUBSCRIBER_AUTHENTICATED,
        SUBSCRIBER_AUTHENTICATION_FAILED,
        SUBSCRIBER_ACCOUNT_LOCKED,
        SUBSCRIBER_STATUS_UPDATED,
        AUTHENTICATION_INITIATED,
        SUBSCRIBER_LOGOUT,
        DIGITALLY_SIGNED,
        KEY_PAIR_GENERATED,
        CSR_CREATED,
        CERTIFICATE_GENERATED,
        CERTIFICATE_REVOKED,
        CERTIFICATE_EXPIRED,
        PKI_AUTHENTICATED,
        SIGNATURE_VERIFIED,
        DIGITALLY_SIGNING_FAILED,
        SUBSCRIBER_REGISTRATION,
        PIN_VERIFICATION,
        TOKEN_INTROSPECT,
        REGISTRATION_OTP_SENT,
        CHECK_CERT_STATUS,
        SIGN_DATA_HSM,
        NIRA_API,
        WALLET_AUTHENTICATION,
        INIT_SDK,
        CHECK_FACE_LIVENESS,
        INIT_SDK_JS,
        CARD_AND_TOUCHLESS_FINGERPRINT_VERIFICATION,
        GET_REMOTE_VERIFICATION_DATA,
        CREATE_REMOTE_VERIFICATION_LINK,
        CARD_STATUS_WITH_NFC,
        CARD_AND_FINGERPRINT_VERIFICATION_WITH_NFC_AND_BIOMETRIC_SENSOR,
        OTHER
    }

    public enum TransactionType{
        BUSINESS,SYSTEM_ACTIVITY
    }


    public enum LogMessageType{
        ERROR,INFO,WARNING,SUCCESS,FAILURE
    }

    public enum SignatureType{
        XADES, PADES, CADES,DATA,NONE
    }
    @NotNull
    private ServiceName serviceName;
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private LogMessageType logMessageType;
    private SignatureType signatureType;
    private String identifier;
    @NotNull @NotEmpty
    private String correlationID;
    @NotNull @NotEmpty
    private String transactionID;
    private String subTransactionID;
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime timestamp;
    @NotNull @NotEmpty
    private String startTime;
    private String endTime;
    private String geoLocation;
    private String callStack;
    @NotNull @NotEmpty
    private String logMessage;
    private String transactionSubType;
    private String serviceProviderName;
    private String serviceProviderAppName;
    private Boolean eSealUsed;
    @NotNull @NotEmpty
    private String checksum;

    private AuthenticationType authenticationType;

    private UserActivityType userActivityType;

    public ServiceAuditLog() {

    }

    public ServiceAuditLog(String identifier, String correlationID, String transactionID, String subTransactionID,
                           LocalDateTime timestamp,
                           String startTime, String endTime, String geoLocation, String callStack,
                           ServiceName serviceName, TransactionType transactionType, String transactionSubType,
                           LogMessageType logMessageType, String logMessage, String serviceProviderName,
                           String serviceProviderAppName, SignatureType signatureType, Boolean eSealUsed, String checksum, AuthenticationType authenticationType, UserActivityType userActivityType) {



        this.identifier = identifier;
        this.correlationID = correlationID;
        this.transactionID = transactionID;
        this.subTransactionID = subTransactionID;
        this.timestamp=timestamp;
        this.startTime=startTime;
        this.endTime=endTime;
        this.geoLocation = geoLocation;
        this.callStack=callStack;
        this.serviceName = serviceName;
        this.transactionType = transactionType;
        this.transactionSubType=transactionSubType;
        this.logMessageType = logMessageType;
        this.logMessage = logMessage;
        this.serviceProviderName = serviceProviderName;
        this.serviceProviderAppName = serviceProviderAppName;
        this.signatureType = signatureType;
        this.eSealUsed = eSealUsed;
        this.checksum = checksum;
        this.authenticationType = authenticationType;
        this.userActivityType = userActivityType;
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

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public UserActivityType getUserActivityType() {
        return userActivityType;
    }

    public void setUserActivityType(UserActivityType userActivityType) {
        this.userActivityType = userActivityType;
    }

    @SneakyThrows
    @Override
    public String toString() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("serviceName",serviceName);
            jsonObject.put("transactionType",transactionType);
            jsonObject.put("signatureType",signatureType);
            jsonObject.put("identifier",identifier);
            jsonObject.put("correlationID",correlationID);
            jsonObject.put("transactionID",transactionID);
            jsonObject.put("subTransactionID",subTransactionID);
            jsonObject.put("timestamp",timestamp);
            jsonObject.put("startTime",startTime);
            jsonObject.put("endTime",endTime);
            jsonObject.put("geoLocation",geoLocation);
            jsonObject.put("callStack",callStack);
            jsonObject.put("logMessage",logMessage);
            jsonObject.put("logMessageType",logMessageType);
            jsonObject.put("transactionSubType",transactionSubType);
            jsonObject.put("serviceProviderName",serviceProviderName);
            jsonObject.put("serviceProviderAppName",serviceProviderAppName);
            jsonObject.put("eSealUsed",eSealUsed);
            jsonObject.put("checksum",checksum);
            jsonObject.put("authenticationType",authenticationType);
            jsonObject.put("userActivityType",userActivityType);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject.toString();
        }

        return jsonObject.toString();

    }


}
