package com.veselintodorov.gateway.fixture;

import com.veselintodorov.gateway.dto.JsonResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;

import java.util.Map;

public class JsonResponseDtoFixture {
    public static JsonResponseDto failureResponse() {
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setSuccess(false);
        return responseDto;
    }

    public static JsonResponseDto responseByCurrencyRate(CurrencyRate currencyRate) {
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setSuccess(true);
        responseDto.setBaseCurrency(currencyRate.getBaseCurrency());
        responseDto.setCurrencyCode(currencyRate.getCurrency());
        responseDto.setRates(Map.of(currencyRate.getTimestamp(), currencyRate.getRate()));
        return responseDto;
    }
}
