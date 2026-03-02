package com.dtt.logs.service.impl;

import com.dtt.logs.dto.service.AuditDTO;
import com.dtt.logs.dto.service.NiraPageDTO;
import com.dtt.logs.Model.ServiceAuditLog;
import com.dtt.logs.repository.ob.ObAuditRepository;
import com.dtt.logs.service.iface.ObLogIface;

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
import java.util.List;

@Service
@Qualifier("obLogMongoTemplate")
public class ObLogImpl implements ObLogIface {

    private final ObAuditRepository auditRepository;


    private final MongoTemplate mongoTemplate;

    public ObLogImpl(ObAuditRepository auditRepository,@Qualifier("obLogMongoTemplate")MongoTemplate mongoTemplate) {
        this.auditRepository = auditRepository;
        this.mongoTemplate=mongoTemplate;
    }
    public void addAudit(ServiceAuditLog auditLog){
        auditRepository.insert(auditLog);
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
        return new PageImpl<ServiceAuditLog>(auditLogList,pageable,recCount);

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
            System.out.println(logMessageType);
            ServiceAuditLog.SignatureType signatureType= (auditDTO.getSignatureType());
            String msg=auditDTO.getLogMessage();
            String spname=auditDTO.getServiceProviderName();
            String spappname=auditDTO.getServiceProviderAppName();
            Boolean eseal=auditDTO.geteSealUsed();
            String checksum= auditDTO.getChecksum();
            ServiceAuditLog auditLog= new ServiceAuditLog(identifier,correlationID,tid,stid,time,stime,etime,loc,callStack,serviceName,
                    transactionType,transactionSubType,logMessageType,msg,spname,spappname,
                    signatureType,eseal,checksum ,null);
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
