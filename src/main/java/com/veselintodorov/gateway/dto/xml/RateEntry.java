package com.veselintodorov.gateway.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.math.BigDecimal;
import java.time.Instant;

public class RateEntry {
    @JacksonXmlProperty(isAttribute = true, localName = "timestamp")
    private Instant timestamp;

    @JacksonXmlProperty(isAttribute = true, localName = "value")
    private BigDecimal value;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
