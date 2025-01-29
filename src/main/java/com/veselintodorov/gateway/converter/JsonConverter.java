package com.veselintodorov.gateway.converter;

import com.veselintodorov.gateway.dto.JsonRequestDto;
import com.veselintodorov.gateway.entity.RequestLog;

public interface JsonConverter {
    RequestLog mapRequestDtoToEntity(JsonRequestDto dto);
}
