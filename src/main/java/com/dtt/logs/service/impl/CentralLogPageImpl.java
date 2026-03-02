package com.dtt.logs.service.impl;

import com.dtt.logs.dto.central.AuthPageDTO;
import com.dtt.logs.dto.central.SigningFailedDTO;
import com.dtt.logs.Model.AuditLog;
import com.dtt.logs.enums.central.LogMessageType;
import com.dtt.logs.enums.central.ServiceName;
import com.dtt.logs.enums.central.TransactionStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CentralLogPageImpl {



    private final MongoTemplate mongoTemplate;

    public CentralLogPageImpl(@Qualifier("centralLogMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Page<AuditLog> matchFilters(List<ServiceName> list, AuthPageDTO authPageDTO, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("serviceName").in(list));
        LocalDateTime endDate = convertToLocalDateTime(authPageDTO.getEndDate());
        LocalDateTime startDate = convertToLocalDateTime(authPageDTO.getStartDate());
        String identifier = authPageDTO.getIdentifier();
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
        if (identifier != null && !identifier.isEmpty()) {
            query.addCriteria(Criteria.where("identifier").is(identifier));
        }


        query = query.with(pageable).skip((long) pageable.getPageSize() * pageable.getPageNumber()).limit(pageable.getPageSize());
        List<AuditLog> auditLogList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
                , AuditLog.class, "auditlogs");
        long recCount = mongoTemplate.count(query.skip(-1).limit(-1), AuditLog.class);
        Page<AuditLog> auditPage = new PageImpl<>(auditLogList, pageable, recCount);
        return auditPage;

    }

    public List<AuditLog> signingFailedFilters(List<ServiceName> list, SigningFailedDTO authPageDTO)  {
        Query query = new Query();
        query.addCriteria(Criteria.where("serviceName").in(list));
        LocalDateTime endDate = convertToLocalDateTime(authPageDTO.getEndDate());
        LocalDateTime startDate = convertToLocalDateTime(authPageDTO.getStartDate());
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




        List<AuditLog> auditLogList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
                , AuditLog.class, "auditlogs");
        return auditLogList;

    }

    public Page<AuditLog> onboardingMatchFilters(List<ServiceName> list, AuthPageDTO authPageDTO, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("serviceName").in(list));
        LocalDateTime endDate = convertToLocalDateTime(authPageDTO.getEndDate());
        LocalDateTime startDate = convertToLocalDateTime(authPageDTO.getStartDate());
        String identifier = authPageDTO.getIdentifier();
        System.out.println("identifier::"+identifier);

        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("timestamp").lt(endDate).gte(startDate));
        }
        if (startDate != null && endDate == null) {
            query.addCriteria(Criteria.where("timestamp").gte(startDate));
        }
        if (endDate != null && startDate == null) {
            query.addCriteria(Criteria.where("timestamp").lt(endDate));
        }

        if (identifier != null && !identifier.isEmpty()) {
            query.addCriteria(Criteria.where("identifier").is(identifier));
        }

        query = query.with(pageable).skip((long) pageable.getPageSize() * pageable.getPageNumber()).limit(pageable.getPageSize());
        List<AuditLog> auditLogList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
                , AuditLog.class, "auditlogs");
        long recCount = mongoTemplate.count(query.skip(-1).limit(-1), AuditLog.class);
        Page<AuditLog> auditPage = new PageImpl<>(auditLogList, pageable, recCount);
        return auditPage;

    }

    public String getPages(Page<AuditLog> auditLogPage) throws JSONException {
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

    public LocalDateTime convertToLocalDateTime(String time){
        try{
            if(time!=null)
            {
                return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Invalid Date Format");
        }

    }
    public JSONArray convertToJSONObject(List<AuditLog> auditLogList) throws JSONException {
        JSONArray jsonArray=new JSONArray();
        for(AuditLog auditLog: auditLogList )
        {
            jsonArray.put(new JSONObject(auditLog.toString()));
        }
        return jsonArray;
    }

}
