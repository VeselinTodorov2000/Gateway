package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.converter.XmlConverter;
import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.fixture.XmlResponseDtoFixture;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import com.veselintodorov.gateway.service.StatisticsService;
import com.veselintodorov.gateway.service.XmlService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.veselintodorov.gateway.fixture.XmlResponseDtoFixture.historyResponseByCurrencyRates;
import static com.veselintodorov.gateway.fixture.XmlResponseDtoFixture.responseByCurrencyRate;

@Service
public class XmlServiceImpl implements XmlService {
    private final XmlConverter xmlConverter;
    private final StatisticsService statisticsService;
    private final CurrencyRateService currencyRateService;
    private final ContextService contextService;

    public XmlServiceImpl(StatisticsService statisticsService, CurrencyRateService currencyRateService, XmlConverter xmlConverter, ContextService contextService) {
        this.statisticsService = statisticsService;
        this.currencyRateService = currencyRateService;
        this.xmlConverter = xmlConverter;
        this.contextService = contextService;
    }

    @Override
    public XmlResponseDto handleValidXmlRequest(XmlRequestDto dto) throws CurrencyNotFoundException {
        statisticsService.saveJsonRequest(xmlConverter.mapRequestDtoToEntity(dto));
        if (isGetRequest(dto)) {
            String currencyCode = dto.getGetRequest().getCurrency();
            BigDecimal rate = currencyRateService.findLatestCurrencyRateForBaseByCurrencyCode(currencyCode);
            return responseByCurrencyRate(currencyCode, rate, contextService.baseCurrency());
        }
        else {
            String currencyCode = dto.getHistoryRequest().getCurrency();
            Long period = dto.getHistoryRequest().getPeriod();
            List<CurrencyRate> rates = currencyRateService.getRatesForLastHours(currencyCode, Instant.now(), period);
            return historyResponseByCurrencyRates(currencyCode, rates, contextService.baseCurrency());
        }

    }

    private boolean isGetRequest(XmlRequestDto dto) {
        return dto.getGetRequest() != null && dto.getHistoryRequest() == null;
    }
}
