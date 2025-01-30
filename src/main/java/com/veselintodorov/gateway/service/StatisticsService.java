package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.entity.RequestLog;

public interface StatisticsService {
    void saveJsonRequest(RequestLog requestLog);

    boolean requestAlreadyExists(String requestId);
}
