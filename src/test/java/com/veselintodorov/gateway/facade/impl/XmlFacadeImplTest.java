package com.veselintodorov.gateway.facade.impl;

import com.veselintodorov.gateway.converter.XmlConverter;
import com.veselintodorov.gateway.dto.xml.GetRequest;
import com.veselintodorov.gateway.dto.xml.HistoryRequest;
import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import com.veselintodorov.gateway.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.*;

class XmlFacadeImplTest {

    @InjectMocks
    private XmlFacadeImpl xmlFacade;

    @Mock
    private XmlConverter xmlConverter;

    @Mock
    private CurrencyRateService currencyRateService;

    @Mock
    private StatisticsService statisticsService;

    @Mock
    private ContextService contextService;

    private XmlRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestDto = new XmlRequestDto(); 

        GetRequest getRequest = new GetRequest();
        getRequest.setCurrency("USD");
        requestDto.setGetRequest(getRequest);

        HistoryRequest historyRequest = new HistoryRequest();
        historyRequest.setCurrency("EUR");
        historyRequest.setPeriod(24L);
        requestDto.setHistoryRequest(historyRequest);
    }

    @Test
    void saveRequest_shouldCallStatisticsServiceSave() {
        when(xmlConverter.mapRequestDtoToEntity(requestDto)).thenReturn(null);

        xmlFacade.saveRequest(requestDto);

        verify(statisticsService, times(1)).saveRequest(any());
    }

    @Test
    void findCurrentRate_shouldCallCurrencyRateService() throws CurrencyNotFoundException {
        String currencyCode = "USD";
        BigDecimal rate = BigDecimal.valueOf(1.5);
        when(currencyRateService.findLatestCurrencyRateForBaseByCurrencyCode(currencyCode)).thenReturn(rate);

        xmlFacade.findCurrentRate(requestDto);

        verify(currencyRateService, times(1)).findLatestCurrencyRateForBaseByCurrencyCode(currencyCode);
    }

    @Test
    void findHistoryRate_shouldCallCurrencyRateService() throws CurrencyNotFoundException {
        String currencyCode = "EUR";
        Long period = 24L;
        Instant timestamp = Instant.parse("2007-12-03T10:15:30.00Z");
        when(currencyRateService.getRatesForLastHours(currencyCode, timestamp, period)).thenReturn(null);

        xmlFacade.findHistoryRate(requestDto);

        verify(currencyRateService, times(1)).getRatesForLastHours(anyString(), any(), any());
    }
}
