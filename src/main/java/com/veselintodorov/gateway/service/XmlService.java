package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;

public interface XmlService {
    XmlResponseDto handleValidXmlRequest(XmlRequestDto dto) throws CurrencyNotFoundException;
}
