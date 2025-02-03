package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.CurrencyRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextServiceImplTest {

    static final String testString = "EUR";
    @Mock
    private Cache currencyRatesCache;
    @Mock
    private Cache requestLogCache;
    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        when(cacheManager.getCache("currencyRatesCache")).thenReturn(currencyRatesCache);
        when(cacheManager.getCache("requestsCache")).thenReturn(requestLogCache);
    }

    @Test
    void baseCurrency_ShouldReturnBaseCurrency() {
        when(currencyRatesCache.get("baseCurrency", String.class)).thenReturn(testString);

        ContextServiceImpl contextService = new ContextServiceImpl(cacheManager);

        String baseCurrency = contextService.baseCurrency();
        assertEquals(testString, baseCurrency);
    }

    @Test
    void saveBaseCurrency_ShouldStoreBaseCurrency() {
        ContextServiceImpl contextService = new ContextServiceImpl(cacheManager);

        contextService.saveBaseCurrency("USD");
        verify(currencyRatesCache).put("baseCurrency", "USD");
    }

    @Test
    void findRateByCurrencyCode_ShouldReturnRate() {
        when(currencyRatesCache.get("USD", BigDecimal.class)).thenReturn(BigDecimal.valueOf(1.23));
        ContextServiceImpl contextService = new ContextServiceImpl(cacheManager);

        Optional<BigDecimal> rate = contextService.findRateByCurrencyCode("USD");
        assertTrue(rate.isPresent());
        assertEquals(BigDecimal.valueOf(1.23), rate.get());
    }

    @Test
    void saveCurrencyRates_ShouldStoreRates() {
        ContextServiceImpl contextService = new ContextServiceImpl(cacheManager);

        CurrencyRate rate1 = new CurrencyRate();
        rate1.setRate(BigDecimal.valueOf(1.23));
        rate1.setCurrency("USD");

        CurrencyRate rate2 = new CurrencyRate();
        rate2.setCurrency("EUR");
        rate2.setRate(BigDecimal.valueOf(0.89));

        contextService.saveCurrencyRates(List.of(rate1, rate2));

        verify(currencyRatesCache).put("USD", BigDecimal.valueOf(1.23));
        verify(currencyRatesCache).put("EUR", BigDecimal.valueOf(0.89));
    }

    @Test
    void findRequestById_ShouldReturnFalse_WhenCacheIsEmpty() {
        when(requestLogCache.get("request-123", Boolean.class)).thenReturn(null);
        ContextServiceImpl contextService = new ContextServiceImpl(cacheManager);

        boolean result = contextService.findRequestById("request-123");
        assertFalse(result);
    }

    @Test
    void saveRequestById_ShouldStoreRequestId() {
        ContextServiceImpl contextService = new ContextServiceImpl(cacheManager);

        contextService.saveRequestById("request-123");
        verify(requestLogCache).put("request-123", true);
    }
}
