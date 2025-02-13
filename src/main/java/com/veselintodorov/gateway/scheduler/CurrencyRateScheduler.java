package com.veselintodorov.gateway.scheduler;

import com.veselintodorov.gateway.service.FixerFetchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRateScheduler {

    private final FixerFetchService fixerFetchService;

    public CurrencyRateScheduler(FixerFetchService fixerFetchService) {
        this.fixerFetchService = fixerFetchService;
    }

    @Scheduled(cron = "${currency.rate.update.interval}")
    public void updateCurrencyRates() {
        fixerFetchService.fetchAndSaveCurrencyRates();
    }
}
