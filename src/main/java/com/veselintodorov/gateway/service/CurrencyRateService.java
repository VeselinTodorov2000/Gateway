package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.dto.FixerResponseDto;

public interface CurrencyRateService {
    void saveRates(FixerResponseDto fixerResponse);
}
