package com.veselintodorov.gateway.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GetRequest extends BaseRequest {
    @JacksonXmlProperty(isAttribute = true)
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
