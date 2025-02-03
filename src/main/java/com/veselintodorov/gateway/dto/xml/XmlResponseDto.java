package com.veselintodorov.gateway.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "response")
public class XmlResponseDto {
    @JacksonXmlProperty(localName = "success")
    private boolean success;

    @JacksonXmlProperty(localName = "baseCurrency")
    private String baseCurrency;

    @JacksonXmlProperty(localName = "currency")
    private String currency;

    @JacksonXmlElementWrapper(localName = "rates")
    @JacksonXmlProperty(localName = "rate")
    private List<RateEntry> rates;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<RateEntry> getRates() {
        return rates;
    }

    public void setRates(List<RateEntry> rates) {
        this.rates = rates;
    }
}
