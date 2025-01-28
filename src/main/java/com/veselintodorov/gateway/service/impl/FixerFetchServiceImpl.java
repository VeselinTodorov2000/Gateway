package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.service.CurrencyRateService;
import com.veselintodorov.gateway.service.FixerFetchService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class FixerFetchServiceImpl implements FixerFetchService {
    private static final Logger logger = LoggerFactory.getLogger(FixerFetchServiceImpl.class);
    private final RestTemplate restTemplate;
    private final CurrencyRateService currencyRateService;
    @Value("${fixer.api.url}")
    private String fixerApiUrl;

    public FixerFetchServiceImpl(RestTemplate restTemplate, CurrencyRateService currencyRateService) {
        this.restTemplate = restTemplate;
        this.currencyRateService = currencyRateService;
    }

    @Override
    @Transactional
    public void fetchAndSaveCurrencyRates() {
        FixerResponseDto fixerResponse = fetchCurrencyRates();
        if (fixerResponse != null) {
            currencyRateService.saveRates(fixerResponse);
        } else {
            throw new RuntimeException("Failed to fetch data from Fixer.io");
        }
    }

    @Retryable
    private FixerResponseDto fetchCurrencyRates() {
        try {
            ResponseEntity<FixerResponseDto> response = restTemplate.getForEntity(fixerApiUrl, FixerResponseDto.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching data from Fixer.io", e);
        }
        return null;
    }

    @Recover
    public void recover(RestClientException e) {
        logger.error("Failed to fetch data after retries: " + e.getMessage());
    }
}