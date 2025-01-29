package com.veselintodorov.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class JsonResponseDto {
    @JsonProperty(value = "success", required = true)
    private boolean success;
    @JsonProperty(value = "baseCurrency")
    private String baseCurrency;
    @JsonProperty(value = "currency")
    private String currencyCode;
    @JsonProperty(value = "rates")
    private Map<Instant, BigDecimal> rates;

    public JsonResponseDto() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Map<Instant, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<Instant, BigDecimal> rates) {
        this.rates = rates;
    }
}
