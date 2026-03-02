package com.dtt.logs.Model;

import com.dtt.logs.enums.central.LogMessageType;
import com.dtt.logs.enums.central.ServiceName;
import com.dtt.logs.enums.central.SignatureType;
import com.dtt.logs.enums.central.TransactionType;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document("auditlogs")
public class AuditLogCopy {
	@NotNull
	private ServiceName serviceName;
	@NotNull
	private TransactionType transactionType;
	@NotNull
	private LogMessageType logMessageType;
	private SignatureType signatureType;
	private String identifier;
	@NotNull
	@NotEmpty
	private String correlationID;
	@NotNull
	@NotEmpty
	private String transactionID;
	private String subTransactionID;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private LocalDateTime timestamp;
	@NotNull
	@NotEmpty
	private String startTime;
	private String endTime;
	private String geoLocation;
	private String callStack;
	@NotNull
	@NotEmpty
	private String logMessage;
	private String transactionSubType;
	private String serviceProviderName;
	private String serviceProviderAppName;
	private Boolean eSealUsed;
	@NotNull
	@NotEmpty
	private String checksum;
	private String deviceId;


	public AuditLogCopy(String identifier, String correlationID, String transactionID, String subTransactionID,
                        LocalDateTime timestamp, String startTime, String endTime, String geoLocation, String callStack,
                        ServiceName serviceName, TransactionType transactionType, String transactionSubType,
                        LogMessageType logMessageType, String logMessage, String serviceProviderName, String serviceProviderAppName,
                        SignatureType signatureType, Boolean eSealUsed, String checksum, String deviceId) {

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


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@SneakyThrows
	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("serviceName", serviceName);
			jsonObject.put("transactionType", transactionType);
			jsonObject.put("signatureType", signatureType);
			jsonObject.put("identifier", identifier);
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
			jsonObject.put(" deviceId;", deviceId);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonObject.toString();
		}

	}
}
