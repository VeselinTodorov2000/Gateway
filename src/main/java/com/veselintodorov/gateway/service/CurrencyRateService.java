package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;

import java.math.BigDecimal;

public interface CurrencyRateService {
    void saveRates(FixerResponseDto fixerResponse);

    BigDecimal findLatestCurrencyRateForBaseByCurrencyCode(String currencyCode) throws CurrencyNotFoundException;
}
