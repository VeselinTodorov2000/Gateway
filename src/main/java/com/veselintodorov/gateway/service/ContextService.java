package com.veselintodorov.gateway.service;

import java.math.BigDecimal;

public interface ContextService {
    String baseCurrency();

    BigDecimal findRateByCurrencyCode(String currencyCode);

    void saveCurrencyRate(String currencyCode, BigDecimal currencyRate);
}
