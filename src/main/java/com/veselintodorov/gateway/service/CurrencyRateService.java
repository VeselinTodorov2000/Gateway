package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;

public interface CurrencyRateService {
    void saveRates(FixerResponseDto fixerResponse);

    CurrencyRate findLatestCurrencyRateForBaseByCurrencyCode(String currencyCode) throws CurrencyNotFoundException;
}
