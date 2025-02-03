package com.veselintodorov.gateway.fixture;

import com.veselintodorov.gateway.dto.json.JsonResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class JsonResponseDtoFixture {
    public static ResponseEntity<JsonResponseDto> failureResponse() {
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setSuccess(false);
        responseDto.setCurrencyCode("Request already exists");
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<JsonResponseDto> noCurrencyFoundResponse(String currencyCodeFailureMessage) {
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setSuccess(false);
        responseDto.setCurrencyCode(currencyCodeFailureMessage);
        return new ResponseEntity<>(responseDto, NOT_FOUND);
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
