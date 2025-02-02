package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.service.ContextService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ContextServiceImpl implements ContextService {
    private final Cache currencyRatesCache;
    private final Cache requestLogCache;

    public ContextServiceImpl(CacheManager cacheManager) {
        this.currencyRatesCache = cacheManager.getCache("currencyRates");
        this.requestLogCache = cacheManager.getCache("requestLog");
    }

    @Override
    public String baseCurrency() {
        if (currencyRatesCache != null) {
            return currencyRatesCache.get("baseCurrency", String.class);
        }
        throw new RuntimeException("No currency rates cache found");
    }

    @Override
    public void saveBaseCurrency(String baseCurrency) {
        if (currencyRatesCache != null) {
            currencyRatesCache.put("baseCurrency", baseCurrency);
        }
    }

    @Override
    public Optional<BigDecimal> findRateByCurrencyCode(String currencyCode) {
        if (currencyRatesCache != null) {
            return Optional.ofNullable(currencyRatesCache.get(currencyCode, BigDecimal.class));
        }
        return Optional.empty();
    }

    @Override
    public void saveCurrencyRates(List<CurrencyRate> currencyRates) {
        if (currencyRatesCache != null) {
            currencyRates.forEach(rate -> currencyRatesCache.put(rate.getCurrency(), rate.getRate()));
        }
    }

    @Override
    public boolean findRequestById(String requestId) {
        return requestLogCache != null && Boolean.TRUE.equals(requestLogCache.get(requestId, Boolean.class));
    }

    @Override
    public void saveRequestById(String requestId) {
        if (requestLogCache != null) {
            requestLogCache.put(requestId, true);
        }
    }
}