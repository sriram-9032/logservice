package com.dtt.logs.dto;

import java.util.List;
import lombok.Data;

@Data
public class DateRangeFilterDto {
    private String fromDate; // format: yyyy-MM-dd
    private String toDate;   // format: yyyy-MM-dd
    private List<String> serviceNames;

    private Integer page = 1;
    private Integer perPage = 10;


    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public List<String> getServiceNames() {
        return serviceNames;
    }

    public void setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }
}


