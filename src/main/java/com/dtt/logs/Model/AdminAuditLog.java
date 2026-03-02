package com.dtt.logs.Model;

import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document("auditlogs")
public class AdminAuditLog {
    @Id
    ObjectId id;

    @com.dtt.logs.Model.NotNull
    private String moduleName;
    @NotNull
    @Indexed
    private String serviceName;
    @NotNull
    private String activityName;
    @NotNull
    private String userName;
    @Indexed
    private LocalDateTime timestamp;
    private String dataTransformation;
    public enum LogMessageType{
        ERROR,INFO,WARNING,SUCCESS,FAILURE
    }
    @NotNull
    private LogMessageType logMessageType;
    @NotNull
    private String logMessage;
    @NotNull
    private String checksum;

    public AdminAuditLog(String moduleName, String serviceName, String activityName, String userName,
                         LocalDateTime timestamp, String dataTransformation, LogMessageType logMessageType, String logMessage,
                         String checksum) {
        this.moduleName = moduleName;
        this.serviceName = serviceName;
        this.activityName = activityName;
        this.userName = userName;
        this.timestamp = timestamp;
        this.dataTransformation = dataTransformation;
        this.logMessageType = logMessageType;
        this.logMessage = logMessage;
        this.checksum = checksum;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDataTransformation() {
        return dataTransformation;
    }

    public void setDataTransformation(String dataTransformation) {
        this.dataTransformation = dataTransformation;
    }

    public LogMessageType getLogMessageType() {
        return logMessageType;
    }

    public void setLogMessageType(LogMessageType logMessageType) {
        this.logMessageType = logMessageType;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
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
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("_id",id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("moduleName",moduleName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("serviceName",serviceName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("activityName",activityName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("userName",userName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("timestamp",timestamp);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("dataTransformation",dataTransformation);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("logMessageType",logMessageType);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("logMessage",logMessage);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("checksum",checksum);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject.toString();
    }
}
