package com.veselintodorov.gateway.fixture;

import com.veselintodorov.gateway.dto.xml.RateEntry;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class XmlResponseDtoFixture {
    public static ResponseEntity<XmlResponseDto> failureResponse() {
        XmlResponseDto responseDto = new XmlResponseDto();
        responseDto.setSuccess(false);
        return new ResponseEntity<>(responseDto, BAD_REQUEST);
    }

    public static ResponseEntity<XmlResponseDto> noCurrencyFoundResponse(String currencyCodeFailureMessage) {
        XmlResponseDto responseDto = new XmlResponseDto();
        responseDto.setSuccess(false);
        responseDto.setCurrency(currencyCodeFailureMessage);
        return new ResponseEntity<>(responseDto, NOT_FOUND);
    }

    public static XmlResponseDto responseByCurrencyRate(String currencyCode, BigDecimal currencyRate, String baseCurrency) {
        XmlResponseDto responseDto = new XmlResponseDto();
        responseDto.setSuccess(true);
        responseDto.setBaseCurrency(baseCurrency);
        responseDto.setCurrency(currencyCode);
        responseDto.setRates(Collections.singletonList(new RateEntry(Instant.now(), currencyRate)));
        return responseDto;
    }

    public static XmlResponseDto historyResponseByCurrencyRates(String currencyCode, List<CurrencyRate> rates, String baseCurrency) {
        XmlResponseDto responseDto = new XmlResponseDto();
        responseDto.setSuccess(true);
        responseDto.setBaseCurrency(baseCurrency);
        responseDto.setCurrency(currencyCode);
        responseDto.setRates(rates.stream().map(rate -> new RateEntry(rate.getTimestamp(), rate.getRate())).toList());
        return responseDto;
    }
}
