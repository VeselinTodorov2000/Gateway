package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.facade.FixerFacade;
import com.veselintodorov.gateway.service.FixerFetchService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class FixerFetchServiceImpl implements FixerFetchService {
    private static final Logger logger = LoggerFactory.getLogger(FixerFetchServiceImpl.class);
    private final RestTemplate restTemplate;
    private final FixerFacade fixerFacade;

    @Value("${fixer.api.url}")
    private String fixerApiUrl;

    public FixerFetchServiceImpl(RestTemplate restTemplate, FixerFacade fixerFacade) {
        this.restTemplate = restTemplate;
        this.fixerFacade = fixerFacade;
    }

    @Override
    @Transactional
    public void fetchAndSaveCurrencyRates() {
        Optional<FixerResponseDto> fixerResponse = fetchCurrencyRates();
        fixerResponse.ifPresentOrElse(
                fixerFacade::saveRates,
                () -> logger.error("Failed to fetch data from Fixer.io - No data available")
        );
    }

    private Optional<FixerResponseDto> fetchCurrencyRates() {
        try {
            ResponseEntity<FixerResponseDto> response = restTemplate.getForEntity(fixerApiUrl, FixerResponseDto.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return Optional.of(response.getBody());
            } else {
                logger.warn("Received non-OK response from Fixer.io: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error while fetching data from Fixer.io", e);
        }
        return Optional.empty();
    }
}