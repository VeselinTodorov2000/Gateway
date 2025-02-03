package com.veselintodorov.gateway.converter.impl;

import com.veselintodorov.gateway.dto.xml.GetRequest;
import com.veselintodorov.gateway.dto.xml.HistoryRequest;
import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.entity.RequestLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.veselintodorov.gateway.businessEnum.ServiceType.EXTERNAL_SERVICE_XML;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlConverterImplTest {

    private XmlConverterImpl xmlConverter;

    @BeforeEach
    void setUp() {
        xmlConverter = new XmlConverterImpl();
    }

    @Test
    void mapRequestDtoToEntity_shouldConvertCorrectly_forGetRequest() {
        XmlRequestDto dto = new XmlRequestDto();
        dto.setId("requestId123");
        dto.setGetRequest(new GetRequest());
        dto.getGetRequest().setConsumer("consumer123");

        RequestLog requestLog = xmlConverter.mapRequestDtoToEntity(dto);

        assertNotNull(requestLog);
        assertEquals(dto.getId(), requestLog.getRequestId());
        assertEquals("consumer123", requestLog.getClientId());
        assertNotNull(requestLog.getTime());
        assertEquals(EXTERNAL_SERVICE_XML.getName(), requestLog.getServiceName());
    }

    @Test
    void mapRequestDtoToEntity_shouldConvertCorrectly_forHistoryRequest() {
        XmlRequestDto dto = new XmlRequestDto();
        dto.setId("requestId456");
        dto.setHistoryRequest(new HistoryRequest());
        dto.getHistoryRequest().setConsumer("consumer456");

        RequestLog requestLog = xmlConverter.mapRequestDtoToEntity(dto);

        assertNotNull(requestLog);
        assertEquals(dto.getId(), requestLog.getRequestId());
        assertEquals("consumer456", requestLog.getClientId());
        assertNotNull(requestLog.getTime());
        assertEquals(EXTERNAL_SERVICE_XML.getName(), requestLog.getServiceName());
    }
}
