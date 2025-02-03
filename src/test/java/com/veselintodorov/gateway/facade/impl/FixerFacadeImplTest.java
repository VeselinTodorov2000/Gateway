package com.veselintodorov.gateway.facade.impl;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.service.CurrencyRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FixerFacadeImplTest {
    @InjectMocks
    private FixerFacadeImpl fixerFacade;
    @Spy
    private CurrencyRateService currencyRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveRates_shouldCallCurrencyRateServiceWithCorrectData() {
        FixerResponseDto fixerResponse = new FixerResponseDto();
        fixerResponse.setBase("EUR");
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", BigDecimal.valueOf(1.1));
        rates.put("GBP", BigDecimal.valueOf(0.85));
        fixerResponse.setRates(rates);

        fixerFacade.saveRates(fixerResponse);

        verify(currencyRateService).saveRates(anyList(), anyString());
    }
}