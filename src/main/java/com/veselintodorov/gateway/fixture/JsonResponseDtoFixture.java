package com.veselintodorov.gateway.fixture;

import com.veselintodorov.gateway.dto.JsonResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static JsonResponseDto historyResponseByCurrencyRates(String currencyCode, List<CurrencyRate> rates, String baseCurrency) {
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setSuccess(true);
        responseDto.setBaseCurrency(baseCurrency);
        responseDto.setCurrencyCode(currencyCode);
        responseDto.setRates(rates.stream().collect(Collectors.toMap(CurrencyRate::getTimestamp, CurrencyRate::getRate)));
        return responseDto;
    }
}
