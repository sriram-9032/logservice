package com.dtt.logs.service.impl;

import com.dtt.logs.Model.AuditLog;
import com.dtt.logs.Model.CountSPLog;
import com.dtt.logs.enums.central.ServiceName;
import com.dtt.logs.enums.central.SignatureType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CentralLogCountSpImpl {



    private final MongoTemplate mongoTemplate;

    public CentralLogCountSpImpl(@Qualifier("centralLogMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public CountSPLog createNewCountSPLog(String id)
    {
        CountSPLog countSPLog=new CountSPLog();
        countSPLog.setCountOfSignaturesWithXadesSuccess(0);countSPLog.setCountOfSignaturesWithXadesFailed(0);
        countSPLog.setCountOfSignaturesWithPadesSuccess(0);countSPLog.setCountOfSignaturesWithPadesFailed(0);
        countSPLog.setCountOfSignaturesWithCadesSuccess(0);countSPLog.setCountOfSignaturesWithCadesFailed(0);
        countSPLog.setCountOfSignaturesWithESealSuccess(0);countSPLog.setCountOfSignaturesWithESealFailed(0);
        countSPLog.setCountOfAuthenticationsFailed(0);countSPLog.setCountOfAuthenticationsSuccess(0);
        countSPLog.setLastUpdatedOn(LocalDateTime.now());countSPLog.setIdentifier(id);
        return countSPLog;
    }




    public void updateSigCount(Query query, List<CountSPLog> countSPLogs, CountSPLog newCountSPLog, ServiceName serviceName, SignatureType signatureType)
    {
        Update update= new Update();
        if(serviceName== ServiceName.DIGITALLY_SIGNED && signatureType!=null)
        {

            if(!countSPLogs.isEmpty())
            {

                if(signatureType== SignatureType.XADES)
                {
                    update.set("countOfSignaturesWithXadesSuccess",countSPLogs.get(0).getCountOfSignaturesWithXadesSuccess()+1);
                }
                if(signatureType== SignatureType.PADES)
                {
                    update.set("countOfSignaturesWithPadesSuccess",countSPLogs.get(0).getCountOfSignaturesWithPadesSuccess()+1);
                }
                if(signatureType== SignatureType.CADES)
                {
                    update.set("countOfSignaturesWithCadesSuccess",countSPLogs.get(0).getCountOfSignaturesWithCadesSuccess()+1);
                }
                if(signatureType== SignatureType.DATA)
                {
                    update.set("countOfSignaturesWithDataSuccess",countSPLogs.get(0).getCountOfSignaturesWithDataSuccess()+1);
                }
                mongoTemplate.findAndModify(query,update,CountSPLog.class);
            }
            else{
                if(signatureType== SignatureType.XADES)
                {
                    newCountSPLog.setCountOfSignaturesWithXadesSuccess(1);
                }
                if(signatureType== SignatureType.PADES)
                {
                    newCountSPLog.setCountOfSignaturesWithPadesSuccess(1);
                }
                if(signatureType== SignatureType.CADES)
                {
                    newCountSPLog.setCountOfSignaturesWithCadesSuccess(1);
                }
                if(signatureType== SignatureType.DATA)
                {
                    newCountSPLog.setCountOfSignaturesWithDataSuccess(1);                }
            }
        }
        if(serviceName== ServiceName.DIGITALLY_SIGNING_FAILED && signatureType!=null)
        {
            if(!countSPLogs.isEmpty()){
                if(signatureType== SignatureType.XADES)
                {
                    update.set("countOfSignaturesWithXadesFailed",countSPLogs.get(0).getCountOfSignaturesWithXadesFailed()+1);
                }
                if(signatureType== SignatureType.PADES)
                {
                    update.set("countOfSignaturesWithPadesFailed",countSPLogs.get(0).getCountOfSignaturesWithPadesFailed()+1);
                }
                if(signatureType== SignatureType.CADES)
                {
                    update.set("countOfSignaturesWithCadesFailed",countSPLogs.get(0).getCountOfSignaturesWithCadesFailed()+1);
                }
                if(signatureType== SignatureType.DATA)
                {
                    update.set("countOfSignaturesWithDataFailed",countSPLogs.get(0).getCountOfSignaturesWithDataFailed()+1);
                }
                mongoTemplate.findAndModify(query,update,CountSPLog.class);
            }
            else{
                if(signatureType== SignatureType.XADES)
                {
                    newCountSPLog.setCountOfSignaturesWithXadesFailed(1);
                }
                if(signatureType== SignatureType.PADES)
                {
                    newCountSPLog.setCountOfSignaturesWithPadesFailed(1);
                }
                if(signatureType== SignatureType.CADES)
                {
                    newCountSPLog.setCountOfSignaturesWithCadesFailed(1);
                }
                if(signatureType== SignatureType.DATA)
                {
                    newCountSPLog.setCountOfSignaturesWithDataFailed(1);                }
            }
        }

        System.out.println("updated signature count");
    }


    public void updateAuthCount(Query query, List<CountSPLog> countSPLogs, CountSPLog newCountSPLog, ServiceName serviceName)
    {
        Update update=new Update();
        if(serviceName== ServiceName.SUBSCRIBER_AUTHENTICATED)
        {
            if(!countSPLogs.isEmpty())
            {
                update.set("countOfAuthenticationsSuccess",countSPLogs.get(0).getCountOfAuthenticationsSuccess()+1);
                mongoTemplate.findAndModify(query,update,CountSPLog.class);
            }
            else{
                newCountSPLog.setCountOfAuthenticationsSuccess(1);
            }
        }
        if(serviceName== ServiceName.SUBSCRIBER_AUTHENTICATION_FAILED)
        {
            if(!countSPLogs.isEmpty())
            {
                update.set("countOfAuthenticationsFailed",countSPLogs.get(0).getCountOfAuthenticationsFailed()+1);
                mongoTemplate.findAndModify(query,update,CountSPLog.class);
            }
            else{
                newCountSPLog.setCountOfAuthenticationsFailed(1);
            }
        }
    }

    public void updateESealCount(Query query, List<CountSPLog> countSPLogs, CountSPLog newCountSPLog, Boolean eSeal, ServiceName serviceName)
    {
        Update update=new Update();
        if(serviceName== ServiceName.DIGITALLY_SIGNED && eSeal!=null && eSeal)
            {
                if(!countSPLogs.isEmpty())
                {
                    update.set("countOfSignaturesWithESealSuccess",countSPLogs.get(0).getCountOfSignaturesWithESealSuccess()+1);
                    mongoTemplate.findAndModify(query,update,CountSPLog.class);
                }
                else{
                    newCountSPLog.setCountOfSignaturesWithESealSuccess(1);
                }
            }

        if(serviceName== ServiceName.DIGITALLY_SIGNING_FAILED && eSeal!=null && eSeal)
            {
                if(!countSPLogs.isEmpty())
                {
                    update.set("countOfSignaturesWithESealFailed",countSPLogs.get(0).getCountOfSignaturesWithESealFailed()+1);
                    mongoTemplate.findAndModify(query,update,CountSPLog.class);
                }
                else{
                    newCountSPLog.setCountOfSignaturesWithESealFailed(1);
                }
            }

    }
    public ResponseEntity<String> postDayCount(AuditLog auditLog) {
        try {
            String spName = auditLog.getServiceProviderName();
            if(spName==null||spName.isEmpty())
            {
                System.out.println("can't update service provider count");
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            ServiceName serviceName = auditLog.getServiceName();
            String id= spName.trim() +"_"+LocalDate.now();
            SignatureType signatureType = auditLog.getSignatureType();
            if (signatureType == null) {
                System.out.println("can't update signature count");
            }
            Boolean eSeal = auditLog.geteSealUsed();
            Query query = new Query();
            query.addCriteria(Criteria.where("identifier").is(id));
            List<CountSPLog> countSPLogs = mongoTemplate.find(query, CountSPLog.class, "daycountspmodels");
            CountSPLog newCountSPLog = createNewCountSPLog(id);
            updateSigCount(query, countSPLogs, newCountSPLog, serviceName, signatureType);
            updateESealCount(query,countSPLogs,newCountSPLog,eSeal,serviceName);
            updateAuthCount(query, countSPLogs, newCountSPLog, serviceName);
            if (countSPLogs.isEmpty()) {
                mongoTemplate.insert(newCountSPLog, "daycountspmodels");
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
