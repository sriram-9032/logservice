package com.dtt.logs.controller;

import com.dtt.logs.Model.AdminAuditLog;
import com.dtt.logs.dto.AuditDto;
import com.dtt.logs.dto.PageDto;
import com.dtt.logs.service.impl.EnterpriseLogImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/enterprise-logs/api")

@RestController
public class EnterpriseController {

    private final EnterpriseLogImpl auditService;

    public EnterpriseController(EnterpriseLogImpl auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(topics = "${enterprise.log.topic}", groupId = "kyc-logs", containerFactory = "auditDtoContainerFactory")
    public void getMessage( AuditDto jsonObj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        try{
            System.out.println("Listening to enterprise.log.topic");

            String s = mapper.writeValueAsString(jsonObj);
            AuditDto auditDTO= mapper.readValue(s, AuditDto.class);
            auditService.PostAudit(auditDTO);
            System.out.println(auditDTO+"\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/audit-logs/{page}")
    public String postAuditLogsByFilters(@PathVariable("page") int page, @Validated @RequestBody PageDto pageDTO)
    {
        page-=1;
        Integer perPage=pageDTO.getPerPage();
        int _perPage= (perPage==null)?10:perPage;
        try{
            Pageable pageable= PageRequest.of(page,_perPage);
            Page<AdminAuditLog> auditLogPage= auditService.matchFilters(pageDTO,pageable);
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

    @GetMapping("/audit-logs/{page}")
    public String getAuditLogsByFilters(@PathVariable("page") int page)
    {
        page-=1;
        PageDto pageDTO=new PageDto();
        Integer perPage=pageDTO.getPerPage();
        int _perPage= (perPage==null)?10:perPage;
        try{
            Pageable pageable= PageRequest.of(page,_perPage);
            Page<AdminAuditLog> auditLogPage= auditService.matchFilters(pageDTO,pageable);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("success",true);
            jsonObject.put("message","logs fetched");
            JSONObject result= new JSONObject();
            result.put("logs",convertToJSONObject(auditLogPage.getContent()));
            result.put("currentPage",auditLogPage.getNumber()+1);
            result.put("totalCount",auditLogPage.getTotalElements());
            result.put("totalPages",auditLogPage.getTotalPages());
            jsonObject.put("result",result);
            return jsonObject.toString();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public JSONArray convertToJSONObject(List<AdminAuditLog> auditLogList) throws JSONException {
        JSONArray jsonArray=new JSONArray();
        for(AdminAuditLog auditLog: auditLogList )
        {
            jsonArray.put(new JSONObject(auditLog.toString()));
        }
        return jsonArray;
    }

}
