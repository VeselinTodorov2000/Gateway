package com.veselintodorov.gateway.service;

import com.veselintodorov.gateway.entity.RequestLog;

public interface StatisticsPublisherService {
    void sendRequestLog(RequestLog requestLog);
}
