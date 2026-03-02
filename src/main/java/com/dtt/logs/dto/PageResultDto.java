package com.dtt.logs.dto;

import com.dtt.logs.Model.AuditLog;

import java.util.List;

public class PageResultDto {
    private int currentPage;
    private int perPage;
    private long totalCount;
    private int totalPages;
    private List<AuditLog> data;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<AuditLog> getData() {
        return data;
    }

    public void setData(List<AuditLog> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PageResultDto{" +
                "currentPage=" + currentPage +
                ", perPage=" + perPage +
                ", totalCount=" + totalCount +
                ", totalPages=" + totalPages +
                ", data=" + data +
                '}';
    }
}
