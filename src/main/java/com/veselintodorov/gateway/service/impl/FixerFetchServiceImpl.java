package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
import com.veselintodorov.gateway.repository.CurrencyRateRepository;
import com.veselintodorov.gateway.service.FixerFetchService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class FixerFetchServiceImpl implements FixerFetchService {
    private final RestTemplate restTemplate;
    private final CurrencyRateRepository currencyRateRepository;
    private final CacheManager cacheManager;

    @Value("${fixer.api.url}")
    private String fixerApiUrl;

    public FixerFetchServiceImpl(RestTemplate restTemplate, CurrencyRateRepository currencyRateRepository, CacheManager cacheManager) {
        this.restTemplate = restTemplate;
        this.currencyRateRepository = currencyRateRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Transactional
    public void fetchAndSaveCurrencyRates() {
        FixerResponseDto fixerResponse = fetchCurrencyRates();
        if (fixerResponse != null) {
            saveRates(fixerResponse);
        } else {
            throw new RuntimeException("Failed to fetch data from Fixer.io");
        }
    }

    @Retryable(
            value = {RestClientException.class},
            backoff = @Backoff(delay = 2000)
    )
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
    public FixerResponseDto recover(RestClientException e) {
        //TODO change with logger
        System.err.println("Failed to fetch data after retries: " + e.getMessage());
        return null;
    }

    public void saveRates(FixerResponseDto fixerResponse) {
        String baseCurrency = fixerResponse.getBase();
        Instant timestamp = Instant.now();

        fixerResponse.getRates().forEach((currency, rate) -> {
            CurrencyRate currencyRate = new CurrencyRate();
            currencyRate.setBaseCurrency(baseCurrency);
            currencyRate.setCurrency(currency);
            currencyRate.setRate(rate);
            currencyRate.setTimestamp(timestamp);

            currencyRateRepository.save(currencyRate);
            cacheRate(currency, rate);
        });
    }

    public void cacheRate(String currencyCode, BigDecimal currencyRate) {
        Cache currencyRatesCache = cacheManager.getCache("currencyRatesCache");
        currencyRatesCache.put(currencyCode, currencyRate);
    }
}
