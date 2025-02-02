package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.entity.CurrencyRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ContextService {
    String baseCurrency();

    void saveBaseCurrency(String baseCurrency);

    Optional<BigDecimal> findRateByCurrencyCode(String currencyCode);

    boolean findRequestById(String requestId);

    void saveRequestById(String requestId);

    void saveCurrencyRates(List<CurrencyRate> currencyRates);
}
