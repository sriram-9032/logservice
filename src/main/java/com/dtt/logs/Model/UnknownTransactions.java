package com.dtt.logs.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("UnknownTransactions")
public class UnknownTransactions {
    @Id
    private String id;

    private String payload;          // The actual "hi" or bad JSON
    private String errorReason;      // The exception message
    private String originalTopic;
    private Long originalOffset;
    private LocalDateTime timestamp;

    // Constructors, Getters, and Setters
    public UnknownTransactions() {
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public String getOriginalTopic() {
        return originalTopic;
    }

    public void setOriginalTopic(String originalTopic) {
        this.originalTopic = originalTopic;
    }

    public Long getOriginalOffset() {
        return originalOffset;
    }

    public void setOriginalOffset(Long originalOffset) {
        this.originalOffset = originalOffset;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
