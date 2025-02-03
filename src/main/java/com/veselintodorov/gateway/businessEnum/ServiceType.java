package com.veselintodorov.gateway.businessEnum;

public enum ServiceType {
    EXTERNAL_SERVICE_JSON("EXT_SERVICE_1"),
    EXTERNAL_SERVICE_XML("EXT_SERVICE_2");

    private final String serviceName;

    ServiceType(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getName() {
        return serviceName;
    }
}
