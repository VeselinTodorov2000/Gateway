package com.veselintodorov.gateway.converter.impl;

import com.veselintodorov.gateway.converter.JsonConverter;
import com.veselintodorov.gateway.dto.JsonRequestDto;
import com.veselintodorov.gateway.entity.RequestLog;
import org.springframework.stereotype.Service;

@Service
public class JsonConverterImpl implements JsonConverter {
    @Override
    public RequestLog mapRequestDtoToEntity(JsonRequestDto dto) {
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestId(dto.getRequestId().toString());
        requestLog.setClientId(dto.getClientId());
        requestLog.setTime(dto.getTimestamp());
        requestLog.setServiceName("Service 1");
        return requestLog;
    }
}
