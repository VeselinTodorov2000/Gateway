package com.veselintodorov.gateway.converter.impl;

import com.veselintodorov.gateway.dto.json.JsonRequestDto;
import com.veselintodorov.gateway.entity.RequestLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static com.veselintodorov.gateway.businessEnum.ServiceType.EXTERNAL_SERVICE_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonConverterImplTest {

    private JsonConverterImpl jsonConverter;

    @BeforeEach
    void setUp() {
        jsonConverter = new JsonConverterImpl();
    }

    @Test
    void mapRequestDtoToEntity_shouldConvertCorrectly() {
        JsonRequestDto dto = new JsonRequestDto();
        dto.setRequestId(UUID.randomUUID());
        dto.setClientId("client123");
        dto.setTimestamp(Instant.now());

        RequestLog requestLog = jsonConverter.mapRequestDtoToEntity(dto);

        assertNotNull(requestLog);
        assertEquals(dto.getRequestId().toString(), requestLog.getRequestId());
        assertEquals(dto.getClientId(), requestLog.getClientId());
        assertEquals(dto.getTimestamp(), requestLog.getTime());
        assertEquals(EXTERNAL_SERVICE_JSON.getName(), requestLog.getServiceName());
    }
}
