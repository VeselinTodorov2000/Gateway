package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.repository.CurrencyRateRepository;
import com.veselintodorov.gateway.service.ContextService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceImplTest {
    @InjectMocks
    private CurrencyRateServiceImpl currencyRateService;
    @Mock
    private CurrencyRateRepository currencyRateRepository;
    @Mock
    private ContextService contextService;

    @Test
    void saveRates_shouldSaveRates_whenAtLeastOneRate_isProvided() {
        List<CurrencyRate> currencyRates = singletonList(new CurrencyRate());

        currencyRateService.saveRates(currencyRates, "EUR");

        verify(currencyRateRepository).saveAll(currencyRates);
        verify(contextService).saveCurrencyRates(currencyRates);
        verify(contextService).saveBaseCurrency("EUR");
    }

    @Test
    void findLatestCurrencyRateForBaseByCurrencyCode_shouldGetItFromCache_whenAvailableInCache() throws CurrencyNotFoundException {
        BigDecimal rate = BigDecimal.valueOf(1.96);
        when(contextService.findRateByCurrencyCode("BGN")).thenReturn(Optional.of(rate));

        BigDecimal resultRate = currencyRateService.findLatestCurrencyRateForBaseByCurrencyCode("BGN");

        assertEquals(rate, resultRate);
    }

    @Test
    void findLatestCurrencyRateForBaseByCurrencyCode_shouldGetItFromDb_whenNotAvailableInCache() throws CurrencyNotFoundException {
        BigDecimal rate = BigDecimal.valueOf(1.96);
        when(contextService.findRateByCurrencyCode("BGN")).thenReturn(Optional.empty());
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setRate(rate);
        when(contextService.baseCurrency()).thenReturn("EUR");
        when(currencyRateRepository.findLatestByCurrencyAndBaseCurrency("BGN", "EUR"))
                .thenReturn(Optional.of(currencyRate));

        BigDecimal resultRate = currencyRateService.findLatestCurrencyRateForBaseByCurrencyCode("BGN");

        assertEquals(rate, resultRate);
    }

    @Test
    void getRatesForLastHours_shouldReturnList_whenRatesFoundForThePeriod() throws CurrencyNotFoundException {
        BigDecimal rate = BigDecimal.valueOf(1.96);
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setRate(rate);
        Instant timestamp = Instant.now();
        when(contextService.baseCurrency()).thenReturn("EUR");
        when(currencyRateRepository.findRecentByCurrencyAndBaseCurrency("BGN", "EUR", timestamp))
                .thenReturn(singletonList(currencyRate));

        List<CurrencyRate> rates = currencyRateService.getRatesForLastHours("BGN", timestamp, 0L);

        assertEquals(1, rates.size());
    }

    @Test
    void getRatesForLastHours_shouldThrowException_whenNoRatesFoundForThePeriod() {
        assertThrows(CurrencyNotFoundException.class,
                () -> currencyRateService.getRatesForLastHours("BGN", Instant.now(), 0L));
    }
}