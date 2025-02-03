package com.veselintodorov.gateway.facade.impl;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.facade.FixerFacade;
import com.veselintodorov.gateway.service.CurrencyRateService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FixerFacadeImpl implements FixerFacade {
    private final CurrencyRateService currencyRateService;

    public FixerFacadeImpl(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @Override
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
        currencyRateService.saveRates(currencyRates, baseCurrency);
    }
}
