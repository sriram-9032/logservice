package com.dtt.logs.service.impl;

import com.dtt.logs.dto.ApiResponse;
import com.dtt.logs.dto.AuthCountResult;
import com.dtt.logs.dto.service.AuditDTO;
import com.dtt.logs.dto.service.NiraPageDTO;
import com.dtt.logs.Model.ServiceAuditLog;
import com.dtt.logs.repository.authn.AuthnAuditRepository;
import com.dtt.logs.service.iface.AuthnLogIface;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Qualifier("authnLogMongoTemplate")
public class AuthnLogImpl implements AuthnLogIface {

     static final String CLASS = "AuthnLogImpl";

    private final AuthnAuditRepository auditRepository;

    private final MongoTemplate mongoTemplate;

    public AuthnLogImpl(AuthnAuditRepository auditRepository,@Qualifier("authnLogMongoTemplate") MongoTemplate mongoTemplate) {
        this.auditRepository = auditRepository;
        this.mongoTemplate=mongoTemplate;
    }
    public void addAudit(ServiceAuditLog auditLog){
        auditRepository.insert(auditLog);
    }

    @Override
    public ApiResponse getAuthenticationTypeCount(String identifier) {
        Aggregation aggregation = Aggregation.newAggregation(
                // Stage 1: Filter by both authentication types AND the specific user identifier
                Aggregation.match(
                        Criteria.where("authenticationType").in("APPROVED", "DECLINED", "FAILED")
                                .and("identifier").is(identifier)
                ),

                // Stage 2: Group by the type and count
                Aggregation.group("authenticationType").count().as("count")
        );

        AggregationResults<AuthCountResult> results = mongoTemplate.aggregate(
                aggregation, "auditlogs", AuthCountResult.class
        );

        // Initialize map with zeros to handle cases where a user has 0 of a certain type
        Map<String, Long> statsMap = new HashMap<>();
        statsMap.put("APPROVED", 0L);
        statsMap.put("DECLINED", 0L);
        statsMap.put("FAILED", 0L);

        results.getMappedResults().forEach(res ->
            statsMap.put(res.getId(), res.getCount()));

        return new com.dtt.logs.dto.ApiResponse(true, "Stats", statsMap);

    }

    public Page<ServiceAuditLog> matchFilters(NiraPageDTO pageDTO, Pageable pageable)
    {
        LocalDateTime endDate= convertToLocalDateTime(pageDTO.getEndDate());
        LocalDateTime startDate=convertToLocalDateTime(pageDTO.getStartDate());
        String docNo = pageDTO.getDocumentNumber();
        Query query=new Query();
        query.addCriteria(Criteria.where("serviceName").is(ServiceAuditLog.ServiceName.SUBSCRIBER_ONBOARDED));
        query.addCriteria(Criteria.where("logMessageType").is(ServiceAuditLog.LogMessageType.FAILURE));

        if(docNo!=null)
        {

            query.addCriteria(Criteria.where("logMessage").regex("^"+docNo));
        }

        if(startDate!=null && endDate!=null)
        {
            query.addCriteria(Criteria.where("timestamp").lt(endDate).gte(startDate));
        }
        if(startDate!=null && endDate==null)
        {
            query.addCriteria(Criteria.where("timestamp").gte(startDate));
        }
        if(endDate!=null && startDate==null)
        {
            query.addCriteria(Criteria.where("timestamp").lt(endDate));
        }

        query=query.with(pageable).skip((long) pageable.getPageSize() *pageable.getPageNumber()).limit(pageable.getPageSize());
        List<ServiceAuditLog> auditLogList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC,"timestamp")),ServiceAuditLog.class,"auditlogs");
        long recCount = mongoTemplate.count(query.skip(-1).limit(-1), ServiceAuditLog.class);
        return new PageImpl<>(auditLogList,pageable,recCount);

    }

    public LocalDateTime convertToLocalDateTime(String time){
        if(time!=null&&!time.isEmpty())
        {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return null;

    }


    public ServiceAuditLog PostAudit(AuditDTO auditDTO) {
        try{
            String identifier=auditDTO.getIdentifier();
            String correlationID=auditDTO.getCorrelationID();
            String tid=auditDTO.getTransactionID();
            String stid=auditDTO.getSubTransactionID();
            LocalDateTime time= LocalDateTime.now();
            String stime=auditDTO.getStartTime();
            String etime=auditDTO.getEndTime();
            String loc= auditDTO.getGeoLocation();
            String callStack=auditDTO.getCallStack();
            ServiceAuditLog.ServiceName serviceName= ServiceAuditLog.ServiceName.valueOf(auditDTO.getServiceName());
            ServiceAuditLog.TransactionType transactionType= (auditDTO.getTransactionType());
            String transactionSubType=auditDTO.getTransactionSubType();
            ServiceAuditLog.LogMessageType logMessageType= (auditDTO.getLogMessageType());
            System.out.println(CLASS + " PostAudit() :: "+logMessageType);
            ServiceAuditLog.SignatureType signatureType= (auditDTO.getSignatureType());
            String msg=auditDTO.getLogMessage();
            String spname=auditDTO.getServiceProviderName();
            String spappname=auditDTO.getServiceProviderAppName();
            Boolean eseal=auditDTO.geteSealUsed();
            String checksum= auditDTO.getChecksum();
            ServiceAuditLog auditLog= new ServiceAuditLog(identifier,correlationID,tid,stid,time,stime,etime,loc,callStack,serviceName,
                    transactionType,transactionSubType,logMessageType,msg,spname,spappname,
                    signatureType,eseal,checksum ,auditDTO.getAuthenticationType(),auditDTO.getUserActivityType());
            addAudit(auditLog);
            return auditLog;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public List<ServiceAuditLog> findById(String id){
        return auditRepository.findByIdentifier(id);
    }

}
