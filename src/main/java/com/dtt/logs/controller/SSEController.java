package com.dtt.logs.controller;

import com.dtt.logs.KycEmitter.KycStatsEmitter;
import com.dtt.logs.KycEmitter.OrgKycSummaryEmitter;

import com.dtt.logs.dto.*;
import com.dtt.logs.Model.AuditLog;

import com.dtt.logs.service.impl.KycLogImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.List;



@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/kyc-log/sse")
@RestController
public class SSEController {

    private final KycLogImpl auditService;


    private final ObjectMapper objectMapper;


    private final KycStatsEmitter kycStatsEmitter;


    private final OrgKycSummaryEmitter orgKycSummaryEmitter;

    public SSEController(KycLogImpl auditService, ObjectMapper objectMapper, KycStatsEmitter kycStatsEmitter, OrgKycSummaryEmitter orgKycSummaryEmitter) {
        this.auditService = auditService;
        this.objectMapper = objectMapper;
        this.kycStatsEmitter = kycStatsEmitter;
        this.orgKycSummaryEmitter = orgKycSummaryEmitter;
    }

    @GetMapping(value = "/records/kyc-overall-stats/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLogs() {
        System.out.println("SSE ");
        return Flux.concat(
                Flux.just("{\"status\":\"connected\"}"), // Initial push to "prime" the pump
                kycStatsEmitter.getStream()
        ).concatWith(Flux.never());
    }


    @GetMapping(value = "/kyc-summary/organization/sse/{orgId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamOrgKycSummaryByOrgId(@PathVariable String orgId) {
        return Flux.interval(Duration.ofSeconds(5)).map(tick -> {
            OrgKycSummaryDto summary = auditService.getOrgKycSummaryByOrgId(orgId);
            JSONObject response = new JSONObject();
            try {
                if (summary == null) {
                    response.put("success", false);
                    response.put("message", "Organization not found or has no KYC records");
                } else {
                    response.put("success", true);
                    response.put("message", "Org KYC summary fetched successfully");
                    response.put("result", new JSONObject(objectMapper.writeValueAsString(summary)));
                }
            } catch (JSONException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return response.toString();
        }).onErrorReturn("{\"success\": false, \"message\": \"Error fetching Org KYC summary\"}");
    }



    @GetMapping(value = "/records/org-summary/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamOrgKycSummary() {
        return orgKycSummaryEmitter.getStream();
    }

    @GetMapping(value = "/records/transaction/sse/{transactionId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamKycRecordByTransactionId(@PathVariable String transactionId) {
        return Flux.interval(Duration.ofSeconds(5)).map(tick -> {
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
        }).onErrorReturn("{\"success\": false, \"message\": \"Streaming error occurred\"}");
    }


    @GetMapping(value = "/audit-logs/sse/{page}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamAuditLogsByFilters(@PathVariable("page") int page) {
        return Flux.interval(Duration.ofSeconds(5)).map(tick -> {
            try {
                int adjustedPage = page - 1;
                PageDto pageDTO = new PageDto();
                Integer perPage = pageDTO.getPerPage();
                int _perPage = (perPage == null) ? 10 : perPage;
                Pageable pageable = PageRequest.of(adjustedPage, _perPage);
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
                System.out.println(e.getMessage());
                return "{\"success\": false, \"message\": \"Error fetching audit logs\"}";
            }
        }).onErrorReturn("{\"success\": false, \"message\": \"Streaming error occurred\"}");
    }

    public JSONArray convertToJSONObject(List<AuditLog> auditLogList) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (AuditLog auditLog : auditLogList) {
            jsonArray.put(new JSONObject(auditLog.toString()));
        }
        return jsonArray;
    }
}