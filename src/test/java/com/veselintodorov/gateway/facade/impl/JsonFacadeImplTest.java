package com.veselintodorov.gateway.facade.impl;

import com.veselintodorov.gateway.converter.JsonConverter;
import com.veselintodorov.gateway.dto.json.JsonRequestDto;
import com.veselintodorov.gateway.dto.json.JsonResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import com.veselintodorov.gateway.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JsonFacadeImplTest {
    @InjectMocks
    private JsonFacadeImpl jsonFacade;

    @Mock
    private JsonConverter jsonConverter;

    @Spy
    private CurrencyRateService currencyRateService;

    @Spy
    private StatisticsService statisticsService;

    @Spy
    private ContextService contextService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRequest_shouldCallStatisticsService() {
        JsonRequestDto requestDto = new JsonRequestDto();

        jsonFacade.saveRequest(requestDto);

        verify(statisticsService, times(1)).saveRequest(any());
    }

    @Test
    void findCurrentRate_shouldReturnJsonResponse() throws CurrencyNotFoundException {
        JsonRequestDto requestDto = new JsonRequestDto();
        requestDto.setCurrencyCode("USD");
        requestDto.setRequestId(UUID.randomUUID());
        requestDto.setTimestamp(Instant.now());

        when(currencyRateService.findLatestCurrencyRateForBaseByCurrencyCode(any()))
                .thenReturn(BigDecimal.valueOf(1.1));
        when(contextService.baseCurrency()).thenReturn("EUR");

        JsonResponseDto response = jsonFacade.findCurrentRate(requestDto);

        assertNotNull(response);
        verify(currencyRateService, times(1)).findLatestCurrencyRateForBaseByCurrencyCode("USD");
        verify(contextService, times(1)).baseCurrency();
    }

    @Test
    void findHistoryRate_shouldReturnJsonResponse() throws CurrencyNotFoundException {
        JsonRequestDto requestDto = new JsonRequestDto();
        requestDto.setCurrencyCode("USD");
        requestDto.setTimestamp(Instant.now());
        requestDto.setHours(24L);

        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setRate(BigDecimal.ONE);
        when(currencyRateService.getRatesForLastHours(any(), any(), any()))
                .thenReturn(List.of(currencyRate));
        when(contextService.baseCurrency()).thenReturn("EUR");

        JsonResponseDto response = jsonFacade.findHistoryRate(requestDto);

        assertNotNull(response);
        verify(currencyRateService, times(1)).getRatesForLastHours(any(), any(), any());
        verify(contextService, times(1)).baseCurrency();
    }
}
