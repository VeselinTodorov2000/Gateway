package com.veselintodorov.gateway.converter;

import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.entity.RequestLog;

public interface XmlConverter {
    RequestLog mapRequestDtoToEntity(XmlRequestDto dto);
}
