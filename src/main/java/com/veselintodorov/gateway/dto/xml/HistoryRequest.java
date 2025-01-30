package com.veselintodorov.gateway.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class HistoryRequest extends BaseRequest {
    @JacksonXmlProperty(isAttribute = true)
    private Long period;

    @JacksonXmlProperty(isAttribute = false)
    private String currency;

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
