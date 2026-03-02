package com.dtt.logs.service.impl;

import com.dtt.logs.dto.service.*;
import com.dtt.logs.Model.ServiceAuditLog;
import com.dtt.logs.enums.service.LogMessageType;
import com.dtt.logs.enums.service.ServiceName;
import com.dtt.logs.enums.service.TransactionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SigningPortalPageImpl {

    private final MongoTemplate mongoTemplate;

    private final RestTemplate restTemplate;

    @Value("${central.signing-failed.url}")
    private String signingFailedUrl;

    public SigningPortalPageImpl( @Qualifier("signingPortalLogMongoTemplate") MongoTemplate mongoTemplate, RestTemplate restTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.restTemplate = restTemplate;
    }

    public Page<ServiceAuditLog> matchFilters(PageDTO pageDTO, Pageable pageable)
    {
        Query query=new Query();
        String cid= pageDTO.getCorrelationID();
        if(cid!=null && !cid.isEmpty())
        {
            query.addCriteria(Criteria.where("correlationID").is(cid));
        }

        query=query.with(pageable).skip((long) pageable.getPageSize() *pageable.getPageNumber()).limit(pageable.getPageSize());
        List<ServiceAuditLog> auditLogList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC,"timestamp")),ServiceAuditLog.class,"auditlogs");
        long recCount = mongoTemplate.count(query.skip(-1).limit(-1), ServiceAuditLog.class);
        Page<ServiceAuditLog> auditPage=new PageImpl<ServiceAuditLog>(auditLogList,pageable,recCount);
        return auditPage;



    }

    public Page<ServiceAuditLog> matchFilters(List<ServiceName> list, LogsDTO authPageDTO, Pageable pageable) throws JsonProcessingException {
        Query query = new Query();
        query.addCriteria(Criteria.where("serviceName").in(list));
        LocalDateTime endDate = convertToLocalDateTime(authPageDTO.getEndDate());
        LocalDateTime startDate = convertToLocalDateTime(authPageDTO.getStartDate());
        com.dtt.logs.enums.service.TransactionStatus transactionStatus = authPageDTO.getTransactionStatus();
        System.out.println("transactionStatus: " + transactionStatus);

        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("timestamp").lt(endDate).gte(startDate));
        }
        if (startDate != null && endDate == null) {
            query.addCriteria(Criteria.where("timestamp").gte(startDate));
        }
        if (endDate != null && startDate == null) {
            query.addCriteria(Criteria.where("timestamp").lt(endDate));
        }
        if (transactionStatus != null) {
            if (transactionStatus == TransactionStatus.BOTH) {
                List<LogMessageType> LIST = new ArrayList<>();
                LIST.add(LogMessageType.FAILURE);
                LIST.add(LogMessageType.SUCCESS);
                query.addCriteria(Criteria.where("logMessageType").in(LIST));

            }
            if (transactionStatus == TransactionStatus.SUCCESS) {
                query.addCriteria(Criteria.where("logMessageType").is(LogMessageType.SUCCESS));

            }
            if (transactionStatus == TransactionStatus.FAILED) {
                query.addCriteria(Criteria.where("logMessageType").is(LogMessageType.FAILURE));

            }
        }

        SigningFailedDTO signingFailedDTO= new SigningFailedDTO();
        if(startDate!=null)
            signingFailedDTO.setStartDate(convertToString(startDate));
        if(endDate!=null)
            signingFailedDTO.setEndDate(convertToString(endDate));
        signingFailedDTO.setTransactionStatus(TransactionStatus.FAILED);
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SigningFailedDTO> entity= new HttpEntity<SigningFailedDTO>(signingFailedDTO,headers);
        ApiResponse res=restTemplate.exchange(signingFailedUrl, HttpMethod.POST,entity, ApiResponse.class).getBody();
        System.out.println(res);
        ObjectMapper mapper= new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        SigningFailedResponse signingFailedAuditLogsResp=mapper.readValue(mapper.writeValueAsString(res.getResult()),SigningFailedResponse.class);
        System.out.println(signingFailedAuditLogsResp.getAuditLogList());
        List<ServiceAuditLog> signingFailedAuditLogs=signingFailedAuditLogsResp.getAuditLogList();
        query = query.with(pageable).skip((long) pageable.getPageSize() * pageable.getPageNumber()).limit(pageable.getPageSize());

        List<ServiceAuditLog> auditLogList=mongoTemplate.find(query,ServiceAuditLog.class);
        auditLogList.addAll(signingFailedAuditLogs);

        auditLogList.sort(Comparator.comparing(ServiceAuditLog::getTimestamp).reversed());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), auditLogList.size());
        Page<ServiceAuditLog> auditPage = new PageImpl<ServiceAuditLog>(auditLogList.subList(start, end), pageable, auditLogList.size());

        return auditPage;

    }

    public Page<ServiceAuditLog> authFailMatchFilters(List<ServiceName> list, LogsDTO authPageDTO, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("serviceName").in(list));
        LocalDateTime endDate = this.convertToLocalDateTime(authPageDTO.getEndDate());
        LocalDateTime startDate = this.convertToLocalDateTime(authPageDTO.getStartDate());
        TransactionStatus transactionStatus = authPageDTO.getTransactionStatus();
        System.out.println("transactionStatus: " + transactionStatus);
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("timestamp").lt(endDate).gte(startDate));
        }

        if (startDate != null && endDate == null) {
            query.addCriteria(Criteria.where("timestamp").gte(startDate));
        }

        if (endDate != null && startDate == null) {
            query.addCriteria(Criteria.where("timestamp").lt(endDate));
        }

        if (transactionStatus != null) {
            if (transactionStatus == TransactionStatus.BOTH) {
                List<LogMessageType> LIST = new ArrayList();
                LIST.add(LogMessageType.FAILURE);
                LIST.add(LogMessageType.SUCCESS);
                query.addCriteria(Criteria.where("logMessageType").in(LIST));
            }

            if (transactionStatus == TransactionStatus.SUCCESS) {
                query.addCriteria(Criteria.where("logMessageType").is(LogMessageType.SUCCESS));
            }

            if (transactionStatus == TransactionStatus.FAILED) {
                query.addCriteria(Criteria.where("logMessageType").is(LogMessageType.FAILURE));
            }
        }

        query = query.with(pageable).skip((long)pageable.getPageSize() * (long)pageable.getPageNumber()).limit(pageable.getPageSize());
        List<ServiceAuditLog> auditLogList = this.mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "timestamp")), ServiceAuditLog.class, "auditlogs");
        long recCount = this.mongoTemplate.count(query.skip(-1L).limit(-1), ServiceAuditLog.class);
        Page<ServiceAuditLog> auditPage = new PageImpl(auditLogList, pageable, recCount);
        return auditPage;
    }

    public LocalDateTime convertToLocalDateTime(String time){
        if(time!=null)
        {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return null;

    }

    public String convertToString(LocalDateTime localDateTime)
    {
        DateTimeFormatter dateTimeFormat=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormat);
    }

    public String getPages(Page<ServiceAuditLog> auditLogPage) throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("success",true);
        jsonObject.put("message","logs fetched");
        JSONObject result= new JSONObject();
        result.put("data",convertToJSONObject(auditLogPage.getContent()));
        result.put("currentPage",auditLogPage.getNumber()+1);
        result.put("totalCount",auditLogPage.getTotalElements());
        result.put("totalPages",auditLogPage.getTotalPages());
        jsonObject.put("result",result);
        return jsonObject.toString();
    }

    public JSONArray convertToJSONObject(List<ServiceAuditLog> auditLogList) throws JSONException {
        JSONArray jsonArray=new JSONArray();
        for(ServiceAuditLog auditLog: auditLogList )
        {
            jsonArray.put(new JSONObject(auditLog.toString()));
        }
        return jsonArray;
    }

}
