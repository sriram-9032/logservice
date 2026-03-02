package com.dtt.logs.dto;

public class AuthCountResult {
    private String id; // This maps to the _id from the $group stage
    private Long count;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}
