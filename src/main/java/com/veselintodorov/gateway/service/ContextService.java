package com.veselintodorov.gateway.service;

import java.math.BigDecimal;

public interface ContextService {
    String baseCurrency();

    BigDecimal findByCurrencyCode(String currencyCode);
}
