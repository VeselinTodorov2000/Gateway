package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.service.ContextService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class ContextServiceImpl implements ContextService {
    private final CacheManager cacheManager;

    public ContextServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public String baseCurrency() {
        Cache currencyRatesCache = cacheManager.getCache("baseCurrency");
        if(currencyRatesCache != null) {
            return currencyRatesCache.get("baseCurrency", String.class);
        }
        throw new RuntimeException("No currency rates cache found");
    }
}
