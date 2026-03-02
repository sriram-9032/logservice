package com.dtt.logs.dto;
import com.dtt.logs.Model.AdminAuditLog;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;

@Document("auditlogs")
public class AuditDto implements  Serializable{
    private String moduleName;
    private String serviceName;
    private String activityName;
    private String userName;
    private String dataTransformation;

    private AdminAuditLog.LogMessageType logMessageType;
    private String logMessage;
    private String checksum;

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

    public String getDataTransformation() {
        return dataTransformation;
    }

    public void setDataTransformation(String dataTransformation) {
        this.dataTransformation = dataTransformation;
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

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public String toString() {
        return "AuditDTO{" +
                "moduleName='" + moduleName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", activityName='" + activityName + '\'' +
                ", userName='" + userName + '\'' +
                ", dataTransformation='" + dataTransformation + '\'' +
                ", logMessageType=" + logMessageType +
                ", logMessage='" + logMessage + '\'' +
                ", checksum='" + checksum + '\'' +
                '}';
    }
}
