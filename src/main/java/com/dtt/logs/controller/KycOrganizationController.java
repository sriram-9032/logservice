package com.dtt.logs.controller;

import com.dtt.logs.dto.KycOrganizationDto;
import com.dtt.logs.service.impl.KycOrganizationImpl;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kyc/api/organization")
public class KycOrganizationController {

    private final KycOrganizationImpl kycOrganization;

    public KycOrganizationController(KycOrganizationImpl kycOrganization) {
        this.kycOrganization = kycOrganization;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveOrganization(@RequestBody KycOrganizationDto dto) {
        JSONObject response = new JSONObject();

        try {
            String message = kycOrganization.saveOrganization(dto);

            if ("Organization already exists".equalsIgnoreCase(message)) {
                response.put("success", false);
                response.put("message", message);
                response.put("result", JSONObject.NULL);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response.toString());
            }

            response.put("success", true);
            response.put("message", "Organization saved successfully");
            response.put("result", JSONObject.NULL);
            return ResponseEntity.ok(response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false, \"message\": \"Invalid JSON format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false, \"message\": \"Error saving organization\"}");
        }
    }

}
