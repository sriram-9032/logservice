package com.dtt.logs.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dtt.logs.Model.AuditLog;
import com.dtt.logs.service.impl.CentralLogPageImpl;
import com.dtt.logs.service.impl.CentralLogCountSpImpl;
import com.dtt.logs.service.impl.CentralLogCountImpl;
import com.dtt.logs.service.impl.CentralLogAggCountImpl;
import com.dtt.logs.service.impl.CentralLogAuditImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import com.dtt.logs.dto.ApiResponse;
import com.dtt.logs.dto.central.AuditDTO;
import com.dtt.logs.dto.central.AuthPageDTO;
import com.dtt.logs.dto.central.SigningFailedDTO;
import com.dtt.logs.dto.central.SigningFailedResponse;
import com.dtt.logs.enums.central.ServiceName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RequestMapping("/central-log/api")
@RestController
public class CentralLogController {

    private final CentralLogAuditImpl auditService;

    private final CentralLogAggCountImpl aggCountService;

    private final CentralLogCountImpl countService;

    private final CentralLogCountSpImpl countSpService;

    private final CentralLogPageImpl pageService;


    private final MongoTemplate mongoTemplate;

    public CentralLogController(CentralLogAuditImpl auditService, CentralLogAggCountImpl aggCountService, CentralLogCountImpl countService, CentralLogCountSpImpl countSpService, CentralLogPageImpl pageService, @Qualifier("centralLogMongoTemplate") MongoTemplate mongoTemplate) {
        this.auditService = auditService;
        this.aggCountService = aggCountService;
        this.countService = countService;
        this.countSpService = countSpService;
        this.pageService = pageService;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/log-counts")
    public String getLogCounts() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("message", JSONObject.NULL);
        LocalDate l = LocalDate.now();
        int year = l.getYear();
        int month = l.getMonthValue();
        String m = month < 10 ? "0" + month : "" + month;
        int day = l.getDayOfMonth();
        String d = day < 10 ? "0" + day : "" + day;
        JSONObject logcounts = new JSONObject();
        logcounts.put("dayCount", aggCountService.getDayCountAgg(year, m, d));
        logcounts.put("monthCount", aggCountService.getMonthCountAgg(m, year));
        logcounts.put("yearCount", aggCountService.getYearCountAgg(year));
        logcounts.put("cumulativeCount", aggCountService.getCumulativeAgg());
        jsonObject.put("result", logcounts);
        return jsonObject.toString().replace("\\\\", "");
    }


    @GetMapping("getLog")
    public ApiResponse getParticularLog(@RequestParam String logId) {
        AuditDTO doc = mongoTemplate.findById(logId, AuditDTO.class, "auditlogs");

        if (doc == null) {
            // Handle not found (could throw a custom exception)
            return new ApiResponse(true, "Log not found", null);
        }
        return new ApiResponse(true, "Log", doc);

    }

    @GetMapping("statistics/{spName}")
    public String getLogCountsBySpName(@PathVariable(value = "spName") String spName) throws JSONException {
        LocalDateTime date = LocalDateTime.now();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("message", JSONObject.NULL);
        JSONObject _jsonObject = new JSONObject();
        _jsonObject.put("weekCount", aggCountService.getWeekCountSP(date, spName));
        _jsonObject.put("monthCount", aggCountService.getMonthCountsSP(date, spName));
        _jsonObject.put("yearCount", aggCountService.getYearCountSP(date, spName));
        _jsonObject.put("cumulativeCount", aggCountService.getCumulativeCountSP(spName));
        jsonObject.put("result", _jsonObject);
        return jsonObject.toString().replace("\\\\", "");
    }


    @GetMapping("statistics/list/{spName}")
    public String getLogCountsByOid(@PathVariable(value = "spName") List<String> spName) throws JSONException {
        LocalDateTime date = LocalDateTime.now();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("message", JSONObject.NULL);
        JSONObject _jsonObject = new JSONObject();
        _jsonObject.put("weekCount", aggCountService.getWeekCountSPForOid(date, spName));
        _jsonObject.put("monthCount", aggCountService.getMonthCountsSPWithList(date, spName));
        _jsonObject.put("yearCount", aggCountService.getYearCountSPWithList(date, spName));
        _jsonObject.put("cumulativeCount", aggCountService.getCumulativeCountSPWithList(spName));
        jsonObject.put("result", _jsonObject);
        return jsonObject.toString().replace("\\\\", "");
    }

    @PostMapping("audit-logs/authn/{page}")
    public String getAuthnLog(@PathVariable(value = "page") int page, @RequestBody AuthPageDTO authPageDTO) {
        page -= 1;
        System.out.println(authPageDTO);
        Integer perPage = authPageDTO.getPerPage();
        int _perPage = (perPage == null) ? 10 : perPage;
        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            List<ServiceName> list = new ArrayList<>();
            list.add(ServiceName.SUBSCRIBER_AUTHENTICATED);
            list.add(ServiceName.SUBSCRIBER_AUTHENTICATION_FAILED);
            Page<AuditLog> auditLogPage = pageService.matchFilters(list, authPageDTO, pageable);
            return pageService.getPages(auditLogPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // this api is not for dalil vm.Only for ICP digital id
    @PostMapping("audit-logs/wallet/authentication/{page}")
    public String getWalletAuthnLog(@PathVariable(value = "page") int page, @RequestBody AuthPageDTO authPageDTO) {
        page -= 1;
        System.out.println(authPageDTO);
        Integer perPage = authPageDTO.getPerPage();
        int _perPage = (perPage == null) ? 10 : perPage;
        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            List<ServiceName> list = new ArrayList<>();
            list.add(ServiceName.SUBSCRIBER_AUTHENTICATED);
            list.add(ServiceName.SUBSCRIBER_AUTHENTICATION_FAILED);
            list.add(ServiceName.WALLET_AUTHENTICATION);
            Page<AuditLog> auditLogPage = pageService.matchFilters(list, authPageDTO, pageable);
            return pageService.getPages(auditLogPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("audit-logs/onboarding/{page}")
    public String getOnboardingLogs(@PathVariable(value = "page") int page, @RequestBody AuthPageDTO authPageDTO) {
        page -= 1;
        System.out.println(authPageDTO);
        Integer perPage = authPageDTO.getPerPage();
        int _perPage = (perPage == null) ? 10 : perPage;
        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            List<ServiceName> list = new ArrayList<>();
            list.add(ServiceName.SUBSCRIBER_ONBOARDED);
            list.add(ServiceName.SUBSCRIBER_REGISTRATION);
            list.add(ServiceName.REGISTRATION_OTP_SENT);
            Page<AuditLog> auditLogPage = pageService.onboardingMatchFilters(list, authPageDTO, pageable);
            return pageService.getPages(auditLogPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("audit-logs/signing/{page}")
    public String getSigningLog(@PathVariable(value = "page") int page, @RequestBody AuthPageDTO authPageDTO) {
        page -= 1;
        Integer perPage = authPageDTO.getPerPage();
        int _perPage = (perPage == null) ? 10 : perPage;
        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            List<ServiceName> list = new ArrayList<>();
            list.add(ServiceName.DIGITALLY_SIGNED);
            list.add(ServiceName.DIGITALLY_SIGNING_FAILED);
            Page<AuditLog> auditLogPage = pageService.matchFilters(list, authPageDTO, pageable);
            return pageService.getPages(auditLogPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("audit-logs/{serviceName}/{page}")
    public String getServiceLogs(@PathVariable(value = "serviceName") String serviceName, @PathVariable(value = "page") int page, @RequestBody AuthPageDTO authPageDTO) {
        page -= 1;
        Integer perPage = authPageDTO.getPerPage();
        int _perPage = (perPage == null) ? 10 : perPage;
        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            List<ServiceName> list = new ArrayList<>();
            if (serviceName.equals("NON_RESIDENT_CARD")) {
                list.add(ServiceName.NON_RESIDENT_CARD);
            } else if (serviceName.equals("VISA_AUTHORITY")) {
                list.add(ServiceName.VISA_AUTHORITY);
            } else if (serviceName.equals("RENT_CAR")) {
                list.add(ServiceName.RENT_CAR);
            } else if (serviceName.equals("HOTEL")) {
                list.add(ServiceName.HOTEL);
            } else if (serviceName.equals("SIM")) {
                list.add(ServiceName.SIM);
            } else if (serviceName.equals("BLACKLISTED")) {
                list.add(ServiceName.BLACKLISTED);
            } else if (serviceName.equals("WALLET")) {
                list.add(ServiceName.WALLET);
            } else if (serviceName.equals("BANK")) {
                list.add(ServiceName.BANK);
            } else if (serviceName.equals("EXCHANGE_MONEY")) {
                list.add(ServiceName.EXCHANGE_MONEY);
            } else if (serviceName.equals("INSURANCE_HEALTH")) {
                list.add(ServiceName.INSURANCE_HEALTH);
            } else if (serviceName.equals("SCHOOL_ADMISSION")) {
                list.add(ServiceName.SCHOOL_ADMISSION);
            } else {
                list.add(ServiceName.DIGITALLY_SIGNED);
                list.add(ServiceName.DIGITALLY_SIGNING_FAILED);
                list.add(ServiceName.SUBSCRIBER_AUTHENTICATED);
                list.add(ServiceName.SUBSCRIBER_AUTHENTICATION_FAILED);
                list.add(ServiceName.NON_RESIDENT_CARD);
                list.add(ServiceName.VISA_AUTHORITY);
                list.add(ServiceName.RENT_CAR);
                list.add(ServiceName.HOTEL);
                list.add(ServiceName.SIM);
                list.add(ServiceName.BLACKLISTED);
                list.add(ServiceName.WALLET);
                list.add(ServiceName.BANK);
                list.add(ServiceName.INSURANCE_HEALTH);
                list.add(ServiceName.EXCHANGE_MONEY);
                list.add(ServiceName.SCHOOL_ADMISSION);
                list.add(ServiceName.OTHER);
            }

            Page<AuditLog> auditLogPage = pageService.matchFilters(list, authPageDTO, pageable);
            return pageService.getPages(auditLogPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/audit-logs/cert-log")
    public Map<Object, Object> getCertLog(@RequestBody AuthPageDTO dto) {
        Query query = new Query();
        Map<Object, Object> response = new HashMap<>();
        List<Object> certLog = new ArrayList<>();
        try {
            query.addCriteria(new Criteria("identifier").is(dto.getIdentifier()));
            List<AuditLog> auditLogs = mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "timestamp")),
                    AuditLog.class, "auditlogs");
            System.out.println("auditLogs :: " + auditLogs);
            for (AuditLog auditLog : auditLogs) {
                if (auditLog.getServiceName() == ServiceName.CERTIFICATE_PAIR_ISSUED) {
                    System.out.println("onboardingLog :: " + auditLog);
                    certLog.add(auditLog);
                }
            }
            response.put("success", true);
            response.put("message", "Cert Logs");
            response.put("result", certLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("audit-logs/signing-failed")
    public ResponseEntity<ApiResponse> getSigningFailedLogs(@RequestBody SigningFailedDTO authPageDTO) {
        try {
            List<ServiceName> list = new ArrayList<>();
            list.add(ServiceName.DIGITALLY_SIGNING_FAILED);
            List<AuditLog> auditLogs = pageService.signingFailedFilters(list, authPageDTO);
            SigningFailedResponse signingFailedResponse = new SigningFailedResponse();
            signingFailedResponse.setAuditLogList(auditLogs);
            return ResponseEntity.ok(new ApiResponse(true, "success", signingFailedResponse));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }


    @KafkaListener(topics = "${central.log.topic}", groupId = "kyc-logs", containerFactory = "auditDTOCentralContainerFactory")
    public void getMessage(AuditDTO message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        try {

            System.out.println("Listening to central.log.topic");
            String s = mapper.writeValueAsString(message);
            AuditLog auditLog = mapper.readValue(s, AuditLog.class);
            auditService.addAudit(auditLog);
            countService.postDayCount(auditLog);
            countSpService.postDayCount(auditLog);
            System.out.println(message + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/get/service/status")
    public ApiResponse getServiceStatus() {
        try {
            return new ApiResponse(true, "LogService is running", null);

        } catch (Exception e) {
            return new ApiResponse(false, "LogService is not running", null);
        }
    }

}
