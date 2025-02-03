package com.veselintodorov.gateway.converter.impl;

import com.veselintodorov.gateway.converter.JsonConverter;
import com.veselintodorov.gateway.dto.json.JsonRequestDto;
import com.veselintodorov.gateway.entity.RequestLog;
import org.springframework.stereotype.Service;

import static com.veselintodorov.gateway.businessEnum.ServiceType.EXTERNAL_SERVICE_JSON;

@Service
public class JsonConverterImpl implements JsonConverter {
    @Override
    public RequestLog mapRequestDtoToEntity(JsonRequestDto dto) {
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestId(dto.getRequestId().toString());
        requestLog.setClientId(dto.getClientId());
        requestLog.setTime(dto.getTimestamp());
        requestLog.setServiceName(EXTERNAL_SERVICE_JSON.getName());
        return requestLog;
    }
}
