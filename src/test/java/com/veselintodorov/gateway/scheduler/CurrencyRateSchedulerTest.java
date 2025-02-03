package com.veselintodorov.gateway.scheduler;

import com.veselintodorov.gateway.service.FixerFetchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurrencyRateSchedulerTest {
    @InjectMocks
    private CurrencyRateScheduler currencyRateScheduler;
    @Mock
    private FixerFetchService fixerFetchService;

    @Test
    void updateCurrencyRates_shouldFetchAndSaveCurrencyRates() {
        currencyRateScheduler.updateCurrencyRates();

        verify(fixerFetchService).fetchAndSaveCurrencyRates();
    }
}