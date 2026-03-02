package com.dtt.logs.KycEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class KycStatsEmitter {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final ObjectMapper objectMapper;

    public KycStatsEmitter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void emit(Object statsDto) {
        try {
            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "Overall KYC statistics fetched successfully");
            response.put("result", new JSONObject(objectMapper.writeValueAsString(statsDto)));

            sink.tryEmitNext(response.toString());
        } catch (JsonProcessingException | JSONException e) {
            sink.tryEmitNext("{\"success\": false, \"message\": \"Error generating stats payload\"}");
        }
    }

    public Flux<String> getStream() {
        return Flux.concat(
                Flux.just("{\"info\": \"stream-started\"}"), // Forces headers to flush immediately
                sink.asFlux()
        );
    }
}
