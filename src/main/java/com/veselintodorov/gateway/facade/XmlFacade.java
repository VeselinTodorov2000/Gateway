package com.veselintodorov.gateway.facade;

import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;

public interface XmlFacade {
    void saveRequest(XmlRequestDto requestDto);
    XmlResponseDto findCurrentRate(XmlRequestDto requestDto) throws CurrencyNotFoundException;
    XmlResponseDto findHistoryRate(XmlRequestDto requestDto) throws CurrencyNotFoundException;
}
