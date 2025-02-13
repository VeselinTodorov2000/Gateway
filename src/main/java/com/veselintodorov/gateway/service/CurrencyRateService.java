package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface CurrencyRateService {
    void saveRates(List<CurrencyRate> currencyRates, String baseCurrency);

    BigDecimal findLatestCurrencyRateForBaseByCurrencyCode(String currencyCode) throws CurrencyNotFoundException;

    List<CurrencyRate> getRatesForLastHours(String currencyCode, Instant timestamp, Long hours) throws CurrencyNotFoundException;
}
