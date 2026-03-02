package com.dtt.logs.service.impl;

import com.dtt.logs.dto.AuditDto;
import com.dtt.logs.dto.PageDto;
import com.dtt.logs.Model.AdminAuditLog;
import com.dtt.logs.repository.admin.AdminAuditRepository;
import com.dtt.logs.service.iface.AdminLogIface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdminLogImpl implements AdminLogIface {


    private final AdminAuditRepository adminAuditRepository;


    private final MongoTemplate mongoTemplate;

    public AdminLogImpl(AdminAuditRepository adminAuditRepository, MongoTemplate mongoTemplate) {
        this.adminAuditRepository = adminAuditRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Page<AdminAuditLog> matchFilters(PageDto pageDTO, Pageable pageable)
    {
        LocalDateTime endDate= convertToLocalDateTime(pageDTO.getEndDate());
        LocalDateTime startDate=convertToLocalDateTime(pageDTO.getStartDate());
        String moduleName= pageDTO.getModuleName();
        String userName= pageDTO.getUserName();
        Query query=new Query();

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
        if(userName!=null)
        {
            query.addCriteria(Criteria.where("userName").is(userName));

        }
        if(moduleName!=null)
        {
            query.addCriteria(Criteria.where("moduleName").is(moduleName));
        }

        query=query.with(pageable).skip((long) pageable.getPageSize() *pageable.getPageNumber()).limit(pageable.getPageSize());
        List<AdminAuditLog> auditLogList = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC,"timestamp")), AdminAuditLog.class,"auditlogs");
        long recCount = mongoTemplate.count(query.skip(-1).limit(-1), AdminAuditLog.class);
        return new PageImpl<>(auditLogList,pageable,recCount);


    }
    private void insertAuditLog(AdminAuditLog auditLog)
    {
        adminAuditRepository.insert(auditLog);
    }

    public ResponseEntity PostAudit(AuditDto auditDTO) {
        try{

            String moduleName=auditDTO.getModuleName();
            String serviceName=auditDTO.getServiceName();
            String activityName=auditDTO.getActivityName();
            String userName=auditDTO.getUserName();
            LocalDateTime timestamp=LocalDateTime.now();
            String dataTransformation=auditDTO.getDataTransformation();
            AdminAuditLog.LogMessageType logMessageType=auditDTO.getLogMessageType();
            String logMessage= auditDTO.getLogMessage();
            String checksum=auditDTO.getChecksum();
            AdminAuditLog auditLog= new AdminAuditLog(  moduleName,   serviceName,   activityName,   userName,
                    timestamp,  dataTransformation,   logMessageType,  logMessage,
                    checksum);
            insertAuditLog(auditLog);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public LocalDateTime convertToLocalDateTime(String time){
        if(time!=null&&!time.isEmpty())
        {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return null;
    }
}
