package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.repository.CurrencyRateRepository;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import jakarta.transaction.Transactional;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private final CurrencyRateRepository currencyRateRepository;
    private final CacheManager cacheManager;
    private final ContextService contextService;


    public CurrencyRateServiceImpl(CurrencyRateRepository currencyRateRepository, CacheManager cacheManager, ContextService contextService) {
        this.currencyRateRepository = currencyRateRepository;
        this.cacheManager = cacheManager;
        this.contextService = contextService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveRates(FixerResponseDto fixerResponse) {
        String baseCurrency = fixerResponse.getBase();
        Instant timestamp = Instant.now();

        fixerResponse.getRates().forEach((currency, rate) -> {
            CurrencyRate currencyRate = new CurrencyRate();
            currencyRate.setBaseCurrency(baseCurrency);
            currencyRate.setCurrency(currency);
            currencyRate.setRate(rate);
            currencyRate.setTimestamp(timestamp);

            currencyRateRepository.save(currencyRate);
            cacheRate(currency, rate);
        });
    }

    @Override
    public BigDecimal findLatestCurrencyRateForBaseByCurrencyCode(String currencyCode) throws CurrencyNotFoundException {
        if (contextService.findByCurrencyCode(currencyCode) != null) {
            return contextService.findByCurrencyCode(currencyCode);
        }
        return currencyRateRepository.findLatestByCurrencyAndBaseCurrency(currencyCode)
                .map(CurrencyRate::getRate)
                .orElseThrow(() -> new CurrencyNotFoundException(currencyCode));
    }

    public void cacheRate(String currencyCode, BigDecimal currencyRate) {
        Cache currencyRatesCache = cacheManager.getCache("currencyRatesCache");
        currencyRatesCache.put(currencyCode, currencyRate);
    }
}
