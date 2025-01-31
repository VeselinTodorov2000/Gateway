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
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<CurrencyRate> currencyRates = fixerResponse.getRates().entrySet().stream()
                .map(entry -> {
                    CurrencyRate currencyRate = new CurrencyRate();
                    currencyRate.setBaseCurrency(baseCurrency);
                    currencyRate.setCurrency(entry.getKey());
                    currencyRate.setRate(entry.getValue());
                    currencyRate.setTimestamp(timestamp);
                    return currencyRate;
                })
                .collect(Collectors.toList());

        currencyRateRepository.saveAll(currencyRates);
        contextService.saveCurrencyRates(currencyRates);
    }

    @Override
    public BigDecimal findLatestCurrencyRateForBaseByCurrencyCode(String currencyCode) throws CurrencyNotFoundException {
        Optional<BigDecimal> cacheValueByCurrencyCode = contextService.findRateByCurrencyCode(currencyCode);
        if (cacheValueByCurrencyCode.isPresent()) {
            return cacheValueByCurrencyCode.get();
        }
        return currencyRateRepository.findLatestByCurrencyAndBaseCurrency(currencyCode, contextService.baseCurrency())
                .map(CurrencyRate::getRate)
                .orElseThrow(() -> new CurrencyNotFoundException(currencyCode));
    }

    @Override
    public List<CurrencyRate> getRatesForLastHours(String currencyCode, Instant timestamp, Long hours) throws CurrencyNotFoundException {
        Instant timeThreshold = timestamp.minus(hours, ChronoUnit.HOURS);
        List<CurrencyRate> rates = currencyRateRepository.findRecentByCurrencyAndBaseCurrency(currencyCode, contextService.baseCurrency(), timeThreshold);
        if (rates.isEmpty()) {
            throw new CurrencyNotFoundException(currencyCode);
        }
        return rates;
    }
}
