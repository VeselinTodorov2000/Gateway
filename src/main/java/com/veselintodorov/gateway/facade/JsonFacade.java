package com.veselintodorov.gateway.facade;

import com.veselintodorov.gateway.dto.json.JsonRequestDto;
import com.veselintodorov.gateway.dto.json.JsonResponseDto;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;

public interface JsonFacade {
    void saveRequest(JsonRequestDto requestDto);
    JsonResponseDto findCurrentRate(JsonRequestDto requestDto) throws CurrencyNotFoundException;
    JsonResponseDto findHistoryRate(JsonRequestDto requestDto) throws CurrencyNotFoundException;
}
