package com.veselintodorov.gateway.converter.impl;

import com.veselintodorov.gateway.converter.XmlConverter;
import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.entity.RequestLog;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class XmlConverterImpl implements XmlConverter {
    @Override
    public RequestLog mapRequestDtoToEntity(XmlRequestDto dto) {
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestId(dto.getId());
        requestLog.setClientId(findClientId(dto));
        requestLog.setTime(Instant.now());
        requestLog.setServiceName("Service 2");
        return requestLog;
    }

    private String findClientId(XmlRequestDto dto) {
        if(dto.getGetRequest() != null) {
            return dto.getGetRequest().getConsumer();
        }
        else {
            return dto.getHistoryRequest().getConsumer();
        }
    }
}
