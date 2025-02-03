package com.veselintodorov.gateway.facade.impl;

import com.veselintodorov.gateway.converter.JsonConverter;
import com.veselintodorov.gateway.dto.json.JsonRequestDto;
import com.veselintodorov.gateway.dto.json.JsonResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.facade.JsonFacade;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import com.veselintodorov.gateway.service.StatisticsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.veselintodorov.gateway.fixture.JsonResponseDtoFixture.historyResponseByCurrencyRates;
import static com.veselintodorov.gateway.fixture.JsonResponseDtoFixture.responseByCurrencyRate;

@Service
public class JsonFacadeImpl implements JsonFacade {
    private final JsonConverter jsonConverter;
    private final CurrencyRateService currencyRateService;
    private final StatisticsService statisticsService;
    private final ContextService contextService;

    public JsonFacadeImpl(JsonConverter jsonConverter, CurrencyRateService currencyRateService, StatisticsService statisticsService, ContextService contextService) {
        this.jsonConverter = jsonConverter;
        this.currencyRateService = currencyRateService;
        this.statisticsService = statisticsService;
        this.contextService = contextService;
    }

    @Override
    @Transactional
    public void saveRequest(JsonRequestDto requestDto) {
        statisticsService.saveRequest(jsonConverter.mapRequestDtoToEntity(requestDto));
    }

    @Override
    public JsonResponseDto findCurrentRate(JsonRequestDto requestDto) throws CurrencyNotFoundException {
        BigDecimal currencyRate = currencyRateService.findLatestCurrencyRateForBaseByCurrencyCode(requestDto.getCurrencyCode());
        return responseByCurrencyRate(requestDto.getCurrencyCode(), currencyRate, contextService.baseCurrency());
    }

    @Override
    public JsonResponseDto findHistoryRate(JsonRequestDto requestDto) throws CurrencyNotFoundException {
        String currencyCode = requestDto.getCurrencyCode();
        Instant timestamp = requestDto.getTimestamp();
        Long hours = requestDto.getHours();
        List<CurrencyRate> ratesForLastHours = currencyRateService.getRatesForLastHours(currencyCode, timestamp, hours);
        return historyResponseByCurrencyRates(currencyCode, ratesForLastHours, contextService.baseCurrency());
    }
}
