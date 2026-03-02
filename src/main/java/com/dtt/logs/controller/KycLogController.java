package com.dtt.logs.controller;

import com.dtt.logs.KycEmitter.KycStatsEmitter;
import com.dtt.logs.KycEmitter.OrgKycSummaryEmitter;

import com.dtt.logs.Model.UnknownTransactions;
import com.dtt.logs.dto.*;
import com.dtt.logs.Model.AuditLog;
import com.dtt.logs.repository.central.UnknownTransactionRepo;
import com.dtt.logs.service.impl.KycLogImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;

@RequestMapping("/kyc-log/api")
@RestController
public class KycLogController {

    private final KycLogImpl auditService;


    private final ObjectMapper objectMapper;


    private final KycStatsEmitter kycStatsEmitter;


    private final OrgKycSummaryEmitter orgKycSummaryEmitter;


    private final UnknownTransactionRepo unknownTransactionRepository;

    public KycLogController(KycLogImpl auditService, ObjectMapper objectMapper, KycStatsEmitter kycStatsEmitter, OrgKycSummaryEmitter orgKycSummaryEmitter, UnknownTransactionRepo unknownTransactionRepository) {
        this.auditService = auditService;
        this.objectMapper = objectMapper;
        this.kycStatsEmitter = kycStatsEmitter;
        this.orgKycSummaryEmitter = orgKycSummaryEmitter;
        this.unknownTransactionRepository = unknownTransactionRepository;
    }

    @KafkaListener(topics = "${kyc.log.topic}", groupId = "kyc-logs", containerFactory = "kycAuditDtoContainerFactory")
    public void getMessage(KycAuditDto auditDTO) {

        try {

            System.out.println("Listening to kyc.log.topic + price " + auditDTO.getTransactionPrice());

            LocalDateTime timestamp = (auditDTO.getTimestamp() != null && !auditDTO.getTimestamp().isEmpty())
                    ? LocalDateTime.parse(auditDTO.getTimestamp())
                    : LocalDateTime.now();
            auditDTO.setTimestamp(timestamp.toString());
            ResponseEntity<?> auditLog = auditService.PostAudit(auditDTO);
            System.out.println(auditDTO + "\n");

            KycOverallStatsDto stats = auditService.getKycOverallStats();
            kycStatsEmitter.emit(stats);

            List<OrgKycSummaryDto> summaries = auditService.getOrgKycSummary();
            orgKycSummaryEmitter.emit(summaries);

            if (auditLog.getStatusCode().is2xxSuccessful()) {
                auditService.ServiceNameBasedCountMethod(auditDTO);
                auditService.upsertOrgidBasedCountSummary(auditDTO);
            } else {
                System.out.println("Audit failed: " + auditLog.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(
            topics = "central-log-DLT",
            groupId = "central-logs-dlt-group",
            containerFactory = "dltContainerFactory"
    )
    public void listenToDLT(ConsumerRecord<String, String> records) {
        System.out.println("Entered into DLT consumer");
        // 1. Safely get the Header objects first
        Header reasonHeader = records.headers().lastHeader("kafka_dlt-exception-message");
        Header topicHeader = records.headers().lastHeader("kafka_dlt-original-topic");

        // 2. Extract values only if headers exist
        String reason = (reasonHeader != null && reasonHeader.value() != null)
                ? new String(reasonHeader.value())
                : "Deserialization failed";

        UnknownTransactions unknown = new UnknownTransactions();
        unknown.setPayload(records.value());
        unknown.setErrorReason(reason);

        if (topicHeader != null && topicHeader.value() != null) {
            unknown.setOriginalTopic(new String(topicHeader.value()));
        } else {
            unknown.setOriginalTopic("Unknown Source");
        }

        // 3. Save to MongoDB
        unknownTransactionRepository.save(unknown);
        System.out.println("Poison Pill safely quarantined in MongoDB: " + records.value());
    }


    @PostMapping("/post/save/test/auditlogs")
    public ResponseEntity<String> saveKycAuditLog(@RequestBody KycAuditDto auditLog) {
        System.out.println("inside SaveKycAuditLog() :: " + auditLog);
        return auditService.PostAudit(auditLog);

    }

    @GetMapping("/test")
    public String test() {
        return "hi";

    }

    @PostMapping("/audit-logs/{page}")
    public String postAuditLogsByFilters(@PathVariable("page") int page, @Validated @RequestBody PageDto pageDTO) {
        page -= 1;
        Integer perPage = pageDTO.getPerPage();
        int _perPage = (perPage == null) ? 10 : perPage;
        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            Page<AuditLog> auditLogPage = auditService.matchFilters(pageDTO, pageable);
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

    @GetMapping("/audit-logs/{page}")
    public String getAuditLogsByFilters(@PathVariable("page") int page) {
        page -= 1;
        PageDto pageDTO = new PageDto();
        Integer perPage = pageDTO.getPerPage();
        int _perPage = (perPage == null) ? 10 : perPage;
        try {
            Pageable pageable = PageRequest.of(page, _perPage);
            Page<AuditLog> auditLogPage = auditService.matchFilters(pageDTO, pageable);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true);
            jsonObject.put("message", "logs fetched");
            JSONObject result = new JSONObject();
            result.put("logs", convertToJSONObject(auditLogPage.getContent()));
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

    @PostMapping("/filter/{page}")
    public String getFilteredKycLogs(@PathVariable("page") int page, @RequestBody KycLogFilterDto filterDto) {
        page = page - 1;
        int perPage = (filterDto.getSize() != null) ? filterDto.getSize() : 10;

        try {
            Pageable pageable = PageRequest.of(page, perPage);
            Page<AuditLog> auditLogPage = auditService.getFilteredLogsAsPage(filterDto, pageable);

            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "Filtered logs fetched successfully");

            JSONObject result = new JSONObject();
            result.put("logs", convertToJSONObject(auditLogPage.getContent()));
            result.put("currentPage", auditLogPage.getNumber() + 1);
            result.put("totalCount", auditLogPage.getTotalElements());
            result.put("totalPages", auditLogPage.getTotalPages());

            response.put("result", result);
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error fetching logs\"}";
        }
    }

    @PostMapping("/records")
    public String getKycRecordsByDateRange(@RequestBody DateRangeFilterDto dto) {
        try {
            int page = (dto.getPage() != null && dto.getPage() > 0) ? dto.getPage() - 1 : 0;
            int perPage = (dto.getPerPage() != null) ? dto.getPerPage() : 10;
            Pageable pageable = PageRequest.of(page, perPage);

            Page<AuditLog> auditLogPage = auditService.getKycRecordsByDateRange(dto, pageable);
            List<AuditLog> logs = auditLogPage.getContent();

            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "Records fetched successfully");

            JSONArray resultArray = new JSONArray();
            for (AuditLog log : logs) {
                String jsonStr = objectMapper.writeValueAsString(log);
                resultArray.put(new JSONObject(jsonStr));
            }


            response.put("currentPage", page + 1);
            response.put("perPage", perPage);
            response.put("totalCount", auditLogPage.getTotalElements());
            response.put("totalPages", auditLogPage.getTotalPages());
            response.put("result", resultArray);

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error fetching records\"}";
        }
    }


    @PostMapping("/records/organization/{organizationId}")
    public String getKycRecordsByOrgAndDate(
            @PathVariable String organizationId,
            @RequestBody DateRangeFilterDto dto) {
        try {


            int page = (dto.getPage() != null && dto.getPage() > 0) ? dto.getPage() - 1 : 0;
            int perPage = (dto.getPerPage() != null) ? dto.getPerPage() : 10;
            Pageable pageable = PageRequest.of(page, perPage);

            Page<AuditLog> auditLogPage = auditService.getKycRecordsByDateRange(dto, pageable);
            List<AuditLog> logs = auditLogPage.getContent();


            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "Organization records fetched successfully");

            JSONArray resultArray = new JSONArray();
            for (AuditLog log : logs) {
                String jsonStr = objectMapper.writeValueAsString(log);
                resultArray.put(new JSONObject(jsonStr));
            }


            response.put("result", resultArray);
            response.put("currentPage", page + 1);
            response.put("perPage", perPage);
            response.put("totalCount", auditLogPage.getTotalElements());
            response.put("totalPages", auditLogPage.getTotalPages());
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error fetching records\"}";
        }
    }

    @GetMapping("/records/transaction/{transactionId}")
    public String getKycRecordByTransactionId(@PathVariable String transactionId) {
        try {
            AuditLog log = auditService.getRecordByTransactionId(transactionId);

            JSONObject response = new JSONObject();
            if (log != null) {
                String jsonStr = objectMapper.writeValueAsString(log);
                response.put("success", true);
                response.put("message", "Record fetched successfully");
                response.put("result", new JSONObject(jsonStr));
            } else {
                response.put("success", false);
                response.put("message", "No record found for the given transaction ID");
                response.put("result", JSONObject.NULL);
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error fetching record\"}";
        }
    }

    @GetMapping("/records/all")
    public String getAllKycLogs() {
        try {
            List<AuditLog> logs = auditService.getAllLogsSortedByLatest();

            JSONArray resultArray = new JSONArray();
            for (AuditLog log : logs) {
                String jsonStr = objectMapper.writeValueAsString(log);
                resultArray.put(new JSONObject(jsonStr));
            }

            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "All logs fetched successfully");
            response.put("result", resultArray);

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error fetching logs\"}";
        }
    }

    @GetMapping("/records/org-summary")
    public String getOrgKycSummary() {
        try {
            List<OrgKycSummaryDto> summaries = auditService.getOrgKycSummary();

            JSONArray resultArray = new JSONArray();
            for (OrgKycSummaryDto dto : summaries) {
                String json = objectMapper.writeValueAsString(dto);
                resultArray.put(new JSONObject(json));
            }

            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "Org KYC summary fetched successfully");
            response.put("result", resultArray);

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error fetching org KYC summary\"}";
        }
    }

    @GetMapping("/kyc-summary/organization/{orgId}")
    public ResponseEntity<Map<String, Object>> getOrgKycSummaryByOrgId(@PathVariable String orgId) {
        OrgKycSummaryDto summary = auditService.getOrgKycSummaryByOrgId(orgId);

        if (summary == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Organization not found or has no KYC records");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Org KYC summary fetched successfully");
        response.put("result", summary);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/records/kyc-overall-stats")
    public String getOverallKycStats() {
        try {
            KycOverallStatsDto stats = auditService.getKycOverallStats();

            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "Overall KYC statistics fetched successfully");
            response.put("result", new JSONObject(objectMapper.writeValueAsString(stats)));

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"message\": \"Error fetching KYC statistics\"}";
        }
    }

    @PostMapping("/records/by-identifier")
    public String getLogsByIdentifier(@RequestBody IdentifierLogFilterDto dto) {
        try {
            PageResultDto result = auditService.getLogsByIdentifier(dto);

            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "Logs fetched successfully");
            response.put("currentPage", result.getCurrentPage());
            response.put("perPage", result.getPerPage());
            response.put("totalCount", result.getTotalCount());
            response.put("totalPages", result.getTotalPages());
            response.put("result", new JSONArray(objectMapper.writeValueAsString(result.getData())));

            return response.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"success\": false, \"message\": \"Error fetching logs\"}";
        }
    }


    public JSONArray convertToJSONObject(List<AuditLog> auditLogList) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (AuditLog auditLog : auditLogList) {
            jsonArray.put(new JSONObject(auditLog.toString()));
        }
        return jsonArray;
    }

    @PostMapping("/post/save/service-name-based-count-record")
    public ResponseEntity<String> serviceNameBasedCountRecord(@RequestBody KycAuditDto auditLog) {
        System.out.println("inside ServiceNameBasedCountRecord()");
        return auditService.ServiceNameBasedCountMethod(auditLog);
    }


    @PostMapping("/post/save/service-orgId-based-count")
    public ResponseEntity<String> upsertOrgidBasedCountSummary(@RequestBody KycAuditDto auditLog) {
        System.out.println("inside upsertOrgidBasedCountSummary()");
        return auditService.upsertOrgidBasedCountSummary(auditLog);
    }

    @PostMapping("/verify/transaction")
    public ApiResponse verifyOrganizationTransaction(@RequestBody KycTransactionDto auditLog) {
        System.out.println("inside verifyOrganizationTransaction()");
        return auditService.verifyOrganizationTransaction(auditLog);
    }

    @GetMapping("/get/subTransaction")
    public ApiResponse getSubTransactionLog(@RequestParam String coId) {
        System.out.println("inside getSubTransactionLog()");
        return auditService.getSubTransactionLog(coId);
    }

    @GetMapping("/get/overall-kyc-count")
    public ResponseEntity<ApiResponse> getOverallKycCount() {
        return ResponseEntity.ok(auditService.getOverallKycCount());
    }


}
