package com.dtt.logs.KycEmitter;

import com.dtt.logs.Model.AuditLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransactionRecordEmitter {

    private final Map<String, Sinks.Many<String>> sinks = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    @Autowired
    public TransactionRecordEmitter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Flux<String> getStream(String transactionId) {
        Sinks.Many<String> sink = sinks.computeIfAbsent(transactionId,
                key -> Sinks.many().multicast().onBackpressureBuffer());
        return sink.asFlux();
    }

    public void emit(String transactionId, AuditLog log) {
        Sinks.Many<String> sink = sinks.computeIfAbsent(transactionId,
                key -> Sinks.many().multicast().onBackpressureBuffer());

        try {
            JSONObject response = new JSONObject();
            if (log != null) {
                response.put("success", true);
                response.put("message", "Record fetched successfully");
                response.put("result", new JSONObject(objectMapper.writeValueAsString(log)));
            } else {
                response.put("success", false);
                response.put("message", "No record found for the given transaction ID");
                response.put("result", JSONObject.NULL);
            }
            sink.tryEmitNext(response.toString());
        } catch (Exception e) {
            sink.tryEmitNext("{\"success\": false, \"message\": \"Error emitting transaction record\"}");
        }
    }
}
