package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.entity.RequestLog;

public interface StatisticsService {
    void saveRequest(RequestLog requestLog);
    boolean requestAlreadyExists(String requestId);
}
