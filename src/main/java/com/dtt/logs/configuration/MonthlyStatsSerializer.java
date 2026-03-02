package com.dtt.logs.configuration;

import com.dtt.logs.dto.MonthYearComparator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

public class MonthlyStatsSerializer extends StdSerializer<Map<String, Map<String, Long>>> {

    private final transient MonthYearComparator comparator = new MonthYearComparator();

    public MonthlyStatsSerializer() {
        this(null);
    }

    public MonthlyStatsSerializer(Class<Map<String, Map<String, Long>>> t) {
        super(t);
    }

    @Override
    public void serialize(Map<String, Map<String, Long>> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        value.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(comparator))
                .forEachOrdered(entry -> {
                    try {
                        gen.writeFieldName(entry.getKey());
                        provider.defaultSerializeValue(entry.getValue(), gen);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        gen.writeEndObject();
    }
}
