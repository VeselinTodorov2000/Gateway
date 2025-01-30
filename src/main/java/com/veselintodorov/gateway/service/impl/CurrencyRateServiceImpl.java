package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.repository.CurrencyRateRepository;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {
    private final CurrencyRateRepository currencyRateRepository;
    private final ContextService contextService;

    public CurrencyRateServiceImpl(CurrencyRateRepository currencyRateRepository, ContextService contextService) {
        this.currencyRateRepository = currencyRateRepository;
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
            contextService.saveCurrencyRate(currency, rate);
        });
    }

    @Override
    public BigDecimal findLatestCurrencyRateForBaseByCurrencyCode(String currencyCode) throws CurrencyNotFoundException {
        if (contextService.findRateByCurrencyCode(currencyCode) != null) {
            return contextService.findRateByCurrencyCode(currencyCode);
        }
        return currencyRateRepository.findLatestByCurrencyAndBaseCurrency(currencyCode, contextService.baseCurrency())
                .map(CurrencyRate::getRate)
                .orElseThrow(() -> new CurrencyNotFoundException(currencyCode));
    }

    @Override
    public List<CurrencyRate> getRatesForLastHours(String currencyCode, Instant timestamp, Long hours) throws CurrencyNotFoundException {
        Instant timeThreshold = timestamp.minus(hours, ChronoUnit.HOURS);
        List<CurrencyRate> rates = currencyRateRepository.findRecentByCurrencyAndBaseCurrency(currencyCode, contextService.baseCurrency(), timeThreshold);
        if(rates.isEmpty()) {
            throw new CurrencyNotFoundException((currencyCode));
        }
        return rates;
    }
}
