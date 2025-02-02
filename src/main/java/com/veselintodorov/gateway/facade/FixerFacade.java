package com.veselintodorov.gateway.facade;

import com.veselintodorov.gateway.dto.FixerResponseDto;

public interface FixerFacade {
    void saveRates(FixerResponseDto dto);
}
