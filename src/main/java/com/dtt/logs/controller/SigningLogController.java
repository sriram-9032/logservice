package com.dtt.logs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dtt.logs.service.impl.SigningLogImpl;
import com.dtt.logs.service.impl.SigningLogPageImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dtt.logs.dto.service.ApiResponse;
import com.dtt.logs.dto.service.AuditDTO;
import com.dtt.logs.dto.service.LogsDTO;
import com.dtt.logs.dto.service.NiraPageDTO;
import com.dtt.logs.dto.service.OtpDTO;
import com.dtt.logs.dto.service.PageDTO;
import com.dtt.logs.Model.ServiceAuditLog;
import com.dtt.logs.enums.service.ServiceName;
import com.dtt.logs.enums.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ug.daes.DAESService;
import ug.daes.Result;


@RequestMapping("/signing-log/api")
@RestController
public class SigningLogController {

    private final SigningLogImpl auditService;

    private final SigningLogPageImpl pageService;


    MongoTemplate mongoTemplate;

    public SigningLogController(SigningLogImpl auditService, SigningLogPageImpl pageService, MongoTemplate mongoTemplate) {
        this.auditService = auditService;
        this.pageService = pageService;
        this.mongoTemplate = mongoTemplate;
    }

    @KafkaListener(topics = "${signing.log.topic}", groupId = "kyc-logs", containerFactory = "auditDTOServiceContainerFactory")
    public void getMessage(AuditDTO auditDTO) {
        try {
            System.out.println("Listening to signing.log.topic");

         new ObjectMapper().registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
             auditService.PostAudit(auditDTO);
            System.out.println(auditDTO + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/audit-logs/{page}")
    public String getAuthnLog(@PathVariable(value = "page") int page, @RequestBody PageDTO pageDTO) {
        page -= 1;
        int _perPage = 10;
        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            Page<ServiceAuditLog> auditLogPage = pageService.matchFilters(pageDTO, pageable);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true);
            jsonObject.put("message", "logs fetched");
            JSONObject result = new JSONObject();

            result.put("data", convertToJSONObject(auditLogPage.getContent()));
            result.put("currentPage", auditLogPage.getNumber() + 1);
            result.put("totalCount", auditLogPage.getTotalElements());
            result.put("totalPages", auditLogPage.getTotalPages());
            jsonObject.put("result", result);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping({"/audit-logs/onboarding-failure/{page}"})
    public String getAuthnLog(@PathVariable("page") int page, @RequestBody LogsDTO authPageDTO) {
        --page;
        System.out.println(authPageDTO);
        Integer perPage = authPageDTO.getPerPage();
        int _perPage = perPage == null ? 10 : perPage;

        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            List<ServiceName> list = new ArrayList<>();
            String transactionType = authPageDTO.getTransactionType();
            if (transactionType == null) {
                list.add(ServiceName.SUBSCRIBER_AUTHENTICATION_FAILED);
                list.add(ServiceName.DIGITALLY_SIGNING_FAILED);
            } else {
                if (transactionType.equals(TransactionType.SIGNING.name())) {
                    list.add(ServiceName.DIGITALLY_SIGNING_FAILED);
                }

                if (transactionType.equals(TransactionType.AUTHENTICATION.name())) {
                    list.add(ServiceName.SUBSCRIBER_AUTHENTICATION_FAILED);
                }
            }

            Page<ServiceAuditLog> auditLogPage = this.pageService.authFailMatchFilters(list, authPageDTO, pageable);
            return this.pageService.getPages(auditLogPage);
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }
    @PostMapping("/audit-logs/onboarding/otp")
    public ResponseEntity<String> getLastOtp(@RequestBody OtpDTO otpDTO) {
        try {
            Query query = new Query();

            query.addCriteria(new Criteria("identifier").is(otpDTO.getIdentifier()));
            List<ServiceAuditLog> auditLogs = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "timestamp")),
                    ServiceAuditLog.class, "auditlogs");
            for (ServiceAuditLog auditLog : auditLogs) {
                if (auditLog.getServiceName() == ServiceAuditLog.ServiceName.REGISTRATION_OTP_SENT)
                    return new ResponseEntity<>(auditLog.toString(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping("/audit-logs/onboarding/log")
    public ApiResponse onboardingLog(@RequestBody OtpDTO otp) {
        Query query = new Query();
        Query query1 = new Query();
        List<Object> otpLog = new ArrayList<>();
        List<Object> niraApiLog = new ArrayList<>();
        List<Object> onboardingLog = new ArrayList<>();
        Map<Object, Object> result = new HashMap<>();
        ApiResponse response = new ApiResponse();
        System.out.println("encrypt string :: "+encryptedString(otp.getEmail()));
        try {
            query.addCriteria(new Criteria("identifier").is(otp.getSuid()));
            query1.addCriteria(new Criteria("identifier").is(encryptedString(otp.getEmail())));
            System.out.println("auditLog :: "+otp.getSuid());
            List<ServiceAuditLog> auditLogs = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "timestamp")),
                    ServiceAuditLog.class, "auditlogs");
            List<ServiceAuditLog> auditLog1 = mongoTemplate.find(query1.with(Sort.by(Sort.Direction.DESC, "timestamp")),
                    ServiceAuditLog.class, "auditlogs");
            for (ServiceAuditLog auditLog : auditLogs) {
                if (auditLog.getServiceName() == ServiceAuditLog.ServiceName.SUBSCRIBER_ONBOARDED) {
                    System.out.println("onboardingLog :: "+auditLog);
                    onboardingLog.add(auditLog);
                }
            }

            for (ServiceAuditLog auditLog : auditLogs) {
                if (auditLog.getServiceName() == ServiceAuditLog.ServiceName.NIRA_API) {
                    niraApiLog.add(auditLog);
                }
            }

            for (ServiceAuditLog auditLog : auditLog1) {
                if (auditLog.getServiceName() == ServiceAuditLog.ServiceName.REGISTRATION_OTP_SENT) {
                    otpLog.add(auditLog);
                }
            }
            result.put("otpLog", otpLog);
            result.put("niraApiLog", niraApiLog);
            result.put("onboardingLog", onboardingLog);
            response.setSuccess(true);
            response.setMessage("Onboarding Logs");
            response.setResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/nira-log/{page}")
    public String postAuditLogsByFilters(@PathVariable("page") int page,@Validated @RequestBody NiraPageDTO niraPageDTO) {
        page-=1;
        Integer perPage=niraPageDTO.getPerPage();
        int _perPage= (perPage==null)?100:perPage;
        try{
            Pageable pageable= PageRequest.of(page,_perPage);
            Page<ServiceAuditLog> auditLogPage= auditService.matchFilters(niraPageDTO,pageable);
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
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String encryptedString(String s) {
        try {
            Result result = DAESService.encryptData(s);
            return new String(result.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public JSONArray convertToJSONObject(List<ServiceAuditLog> auditLogList) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (ServiceAuditLog auditLog : auditLogList) {
            jsonArray.put(new JSONObject(auditLog.toString()));
        }
        return jsonArray;
    }

}
