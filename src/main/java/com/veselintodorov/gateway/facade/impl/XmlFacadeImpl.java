package com.veselintodorov.gateway.facade.impl;

import com.veselintodorov.gateway.converter.XmlConverter;
import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.facade.XmlFacade;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import com.veselintodorov.gateway.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.veselintodorov.gateway.fixture.XmlResponseDtoFixture.historyResponseByCurrencyRates;
import static com.veselintodorov.gateway.fixture.XmlResponseDtoFixture.responseByCurrencyRate;

@Service
public class XmlFacadeImpl implements XmlFacade {
    private final XmlConverter xmlConverter;
    private final CurrencyRateService currencyRateService;
    private final StatisticsService statisticsService;
    private final ContextService contextService;

    public XmlFacadeImpl(XmlConverter xmlConverter, CurrencyRateService currencyRateService, StatisticsService statisticsService, ContextService contextService) {
        this.xmlConverter = xmlConverter;
        this.currencyRateService = currencyRateService;
        this.statisticsService = statisticsService;
        this.contextService = contextService;
    }

    @Override
    public void saveRequest(XmlRequestDto requestDto) {
        statisticsService.saveRequest(xmlConverter.mapRequestDtoToEntity(requestDto));
    }

    @Override
    public XmlResponseDto findCurrentRate(XmlRequestDto requestDto) throws CurrencyNotFoundException {
        String currencyCode = requestDto.getGetRequest().getCurrency();
        BigDecimal rate = currencyRateService.findLatestCurrencyRateForBaseByCurrencyCode(currencyCode);
        return responseByCurrencyRate(currencyCode, rate, contextService.baseCurrency());
    }

    @Override
    public XmlResponseDto findHistoryRate(XmlRequestDto requestDto) throws CurrencyNotFoundException {
        String currencyCode = requestDto.getHistoryRequest().getCurrency();
        Long period = requestDto.getHistoryRequest().getPeriod();
        List<CurrencyRate> rates = currencyRateService.getRatesForLastHours(currencyCode, Instant.now(), period);
        return historyResponseByCurrencyRates(currencyCode, rates, contextService.baseCurrency());
    }
}
