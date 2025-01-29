package com.veselintodorov.gateway.fixture;

import com.veselintodorov.gateway.dto.JsonResponseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class JsonResponseDtoFixture {
    public static JsonResponseDto failureResponse() {
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setSuccess(false);
        return responseDto;
    }

    public static JsonResponseDto responseByCurrencyRate(String currencyCode, BigDecimal currencyRate, String baseCurrency) {
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setSuccess(true);
        responseDto.setBaseCurrency(baseCurrency);
        responseDto.setCurrencyCode(currencyCode);
        responseDto.setRates(Map.of(Instant.now(), currencyRate));
        return responseDto;
    }
}
