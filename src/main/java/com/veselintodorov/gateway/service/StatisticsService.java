package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.entity.RequestLog;

import java.util.UUID;

public interface StatisticsService {
    void saveJsonRequest(RequestLog requestLog);

    boolean requestAlreadyExists(UUID requestUuid);
}
