package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyRateService {
    void saveRates(FixerResponseDto fixerResponse);

    BigDecimal findLatestCurrencyRateForBaseByCurrencyCode(String currencyCode) throws CurrencyNotFoundException;

    List<CurrencyRate> getRatesForLastHours(String currencyCode, Long hours) throws CurrencyNotFoundException;
}
