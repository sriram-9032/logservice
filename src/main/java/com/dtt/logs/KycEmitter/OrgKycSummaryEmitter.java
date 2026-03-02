package com.dtt.logs.KycEmitter;

import com.dtt.logs.dto.OrgKycSummaryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

@Component
public class OrgKycSummaryEmitter {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final ObjectMapper objectMapper;

    public OrgKycSummaryEmitter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void emit(List<OrgKycSummaryDto> summaries) {
        try {
            JSONArray resultArray = new JSONArray();
            for (OrgKycSummaryDto dto : summaries) {
                String json = objectMapper.writeValueAsString(dto);
                resultArray.put(new JSONObject(json));
            }

            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "Org KYC summary fetched successfully");
            response.put("result", resultArray);

            sink.tryEmitNext(response.toString());
        } catch (Exception e) {
            sink.tryEmitNext("{\"success\": false, \"message\": \"Error serializing Org KYC summary\"}");
        }
    }

    public Flux<String> getStream() {
        return sink.asFlux();
    }
}
