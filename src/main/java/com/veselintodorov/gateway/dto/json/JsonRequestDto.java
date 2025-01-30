package com.veselintodorov.gateway.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public class JsonRequestDto {
    @JsonProperty(value = "requestId", required = true)
    private UUID requestId;
    @JsonProperty(value = "timestamp", required = true)
    private Instant timestamp;
    @JsonProperty(value = "client", required = true)
    private Long clientId;
    @JsonProperty(value = "currency", required = true)
    private String currencyCode;
    @JsonProperty(value = "period", required = false)
    private Long hours;

    public JsonRequestDto() {
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }
}
