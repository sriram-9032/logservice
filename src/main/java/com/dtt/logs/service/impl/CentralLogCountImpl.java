package com.dtt.logs.service.impl;

import com.dtt.logs.Model.AuditLog;

import com.dtt.logs.Model.CountLog;

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
public class CentralLogCountImpl {


    private final MongoTemplate mongoTemplate;

    public CentralLogCountImpl(@Qualifier("centralLogMongoTemplate") MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public CountLog createNewCountLog(String id)
    {
        CountLog countLog=new CountLog();
        countLog.setCountOfServiceProviders(0);countLog.setCountOfCertificates(0);
        countLog.setCountOfSubscribers(0);
        countLog.setCountOfSignaturesWithXadesSuccess(0);countLog.setCountOfSignaturesWithXadesFailed(0);
        countLog.setCountOfSignaturesWithPadesSuccess(0);countLog.setCountOfSignaturesWithPadesFailed(0);
        countLog.setCountOfSignaturesWithCadesSuccess(0);countLog.setCountOfSignaturesWithCadesFailed(0);
        countLog.setCountOfSignaturesWithESealSuccess(0);countLog.setCountOfSignaturesWithESealFailed(0);
        countLog.setCountOfAuthenticationsFailed(0);countLog.setCountOfAuthenticationsSuccess(0);
        countLog.setLastUpdatedOn(LocalDateTime.now());countLog.setIdentifier(id);
        return countLog;
    }


    public void updateCertCount(Query query,List<CountLog> countLogs,CountLog newCountLog, ServiceName serviceName)
    {

        if(serviceName== ServiceName.CERTIFICATE_PAIR_ISSUED)
        {
            if(!countLogs.isEmpty())
            {
                Update update= new Update();
                update.set("countOfCertificates",countLogs.get(0).getCountOfCertificates()+2);
                mongoTemplate.findAndModify(query,update,CountLog.class);
            }
            else{
                newCountLog.setCountOfCertificates(2);

            }

        }

    }

    public void updateSigCount(Query query,List<CountLog> countLogs,CountLog newCountLog,ServiceName serviceName,SignatureType signatureType)
    {
        Update update= new Update();
        if(serviceName==ServiceName.DIGITALLY_SIGNED && signatureType!=null)
        {

            if(!countLogs.isEmpty())
            {

                if(signatureType== SignatureType.XADES)
                {
                    update.set("countOfSignaturesWithXadesSuccess",countLogs.get(0).getCountOfSignaturesWithXadesSuccess()+1);
                }
                if(signatureType== SignatureType.PADES)
                {
                    update.set("countOfSignaturesWithPadesSuccess",countLogs.get(0).getCountOfSignaturesWithPadesSuccess()+1);
                }
                if(signatureType== SignatureType.CADES)
                {
                    update.set("countOfSignaturesWithCadesSuccess",countLogs.get(0).getCountOfSignaturesWithCadesSuccess()+1);
                }
                if(signatureType== SignatureType.DATA)
                {
                    update.set("countOfSignaturesWithDataSuccess",countLogs.get(0).getCountOfSignaturesWithDataSuccess()+1);
                }
                mongoTemplate.findAndModify(query,update,CountLog.class);
            }
            else{
                if(signatureType== SignatureType.XADES)
                {
                    newCountLog.setCountOfSignaturesWithXadesSuccess(1);
                }
                if(signatureType== SignatureType.PADES)
                {
                    newCountLog.setCountOfSignaturesWithPadesSuccess(1);
                }
                if(signatureType== SignatureType.CADES)
                {
                    newCountLog.setCountOfSignaturesWithCadesSuccess(1);
                }
                if(signatureType== SignatureType.DATA)
                {
                    newCountLog.setCountOfSignaturesWithDataSuccess(1);
                }
            }
        }
        if(serviceName==ServiceName.DIGITALLY_SIGNING_FAILED && signatureType!=null)
        {
            if(!countLogs.isEmpty()){
                if(signatureType== SignatureType.XADES)
                {
                    update.set("countOfSignaturesWithXadesFailed",countLogs.get(0).getCountOfSignaturesWithXadesFailed()+1);
                }
                if(signatureType== SignatureType.PADES)
                {
                    update.set("countOfSignaturesWithPadesFailed",countLogs.get(0).getCountOfSignaturesWithPadesFailed()+1);
                }
                if(signatureType== SignatureType.CADES)
                {
                    update.set("countOfSignaturesWithCadesFailed",countLogs.get(0).getCountOfSignaturesWithCadesFailed()+1);
                }
                if(signatureType== SignatureType.DATA)
                {
                    update.set("countOfSignaturesWithDataFailed",countLogs.get(0).getCountOfSignaturesWithDataFailed()+1);
                }
                mongoTemplate.findAndModify(query,update,CountLog.class);
            }
            else{
                if(signatureType== SignatureType.XADES)
                {
                    newCountLog.setCountOfSignaturesWithXadesFailed(1);
                }
                if(signatureType== SignatureType.PADES)
                {
                    newCountLog.setCountOfSignaturesWithPadesFailed(1);
                }
                if(signatureType== SignatureType.CADES)
                {
                    newCountLog.setCountOfSignaturesWithCadesFailed(1);
                }
                if(signatureType== SignatureType.DATA)
                {
                    newCountLog.setCountOfSignaturesWithDataFailed(1);                }
            }
        }

    }

    public void updateSPCount(Query query,List<CountLog> countLogs,CountLog newCountLog,String spName)
    {
        Update update=new Update();
        if(spName!=null)
        {
            if(countLogs.isEmpty())
            {
                newCountLog.setCountOfServiceProviders(1);
            }
            else{
                update.set("countOfServiceProviders", countLogs.get(0).getCountOfServiceProviders()+1);
                mongoTemplate.findAndModify(query,update,CountLog.class);
            }

        }

    }
    public void updateAuthCount(Query query,List<CountLog> countLogs,CountLog newCountLog,ServiceName serviceName)
    {
        Update update=new Update();
        if(serviceName==ServiceName.SUBSCRIBER_AUTHENTICATED)
        {
            if(!countLogs.isEmpty())
            {
                update.set("countOfAuthenticationsSuccess",countLogs.get(0).getCountOfAuthenticationsSuccess()+1);
                mongoTemplate.findAndModify(query,update,CountLog.class);
            }
            else{
                newCountLog.setCountOfAuthenticationsSuccess(1);
            }
        }
        if(serviceName==ServiceName.SUBSCRIBER_AUTHENTICATION_FAILED)
        {
            if(!countLogs.isEmpty())
            {
                update.set("countOfAuthenticationsFailed",countLogs.get(0).getCountOfAuthenticationsFailed()+1);
                mongoTemplate.findAndModify(query,update,CountLog.class);
            }
            else{
                newCountLog.setCountOfAuthenticationsFailed(1);
            }
        }
    }

    public void updateSubCount(Query query,List<CountLog> countLogs,CountLog newCountLog,ServiceName serviceName)
    {
        if(serviceName==ServiceName.SUBSCRIBER_ONBOARDED)
        {
            Update update=new Update();
            if(!countLogs.isEmpty())
            {
                update.set("countOfSubscribers",countLogs.get(0).getCountOfSubscribers()+1);
                mongoTemplate.findAndModify(query,update,CountLog.class);
            }
            else{
                newCountLog.setCountOfSubscribers(1);
            }
        }
    }
    public void updateESealCount(Query query,List<CountLog> countLogs,CountLog newCountLog,Boolean eSeal,ServiceName serviceName)
    {
        Update update=new Update();
        if(serviceName==ServiceName.DIGITALLY_SIGNED && eSeal!=null && eSeal)
            {
                if(!countLogs.isEmpty())
                {
                    update.set("countOfSignaturesWithESealSuccess",countLogs.get(0).getCountOfSignaturesWithESealSuccess()+1);
                    mongoTemplate.findAndModify(query,update,CountLog.class);
                }
                else{
                    newCountLog.setCountOfSignaturesWithESealSuccess(1);
                }
            }

        if(serviceName==ServiceName.DIGITALLY_SIGNING_FAILED && eSeal!=null && eSeal){
                    if(!countLogs.isEmpty())
                    {
                        update.set("countOfSignaturesWithESealFailed",countLogs.get(0).getCountOfSignaturesWithESealFailed()+1);
                        mongoTemplate.findAndModify(query,update,CountLog.class);
                    }
                    else{
                        newCountLog.setCountOfSignaturesWithESealFailed(1);
                    }
                }


    }

    public ResponseEntity<String> postDayCount(AuditLog auditLog) {
        try {
            String serviceProviderName = auditLog.getServiceProviderName();
            ServiceName serviceName = auditLog.getServiceName();
            String id= LocalDate.now().toString();
            SignatureType signatureType = auditLog.getSignatureType();
            if (signatureType == null) {
                System.out.println("can't update signature count");
            }
            Boolean eSeal = auditLog.geteSealUsed();
            Query query = new Query();
            query.addCriteria(Criteria.where("identifier").is(id));
            List<CountLog> countLogs = mongoTemplate.find(query, CountLog.class, "daycountmodels");
            CountLog newCountLog = createNewCountLog(id);
            updateCertCount(query, countLogs, newCountLog, auditLog.getServiceName());
            updateSigCount(query, countLogs, newCountLog, serviceName, signatureType);
            updateESealCount(query,countLogs,newCountLog,eSeal,serviceName);
            updateSPCount(query, countLogs, newCountLog, serviceProviderName);
            updateAuthCount(query, countLogs, newCountLog, serviceName);
            updateSubCount(query, countLogs, newCountLog, serviceName);
            if (countLogs.isEmpty()) {
                mongoTemplate.insert(newCountLog, "daycountmodels");
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


}
