package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.RequestLog;
import com.veselintodorov.gateway.repository.RequestLogRepository;
import com.veselintodorov.gateway.service.StatisticsService;
import jakarta.transaction.Transactional;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final RequestLogRepository requestLogRepository;
    private final CacheManager cacheManager;

    public StatisticsServiceImpl(RequestLogRepository requestLogRepository, CacheManager cacheManager) {
        this.requestLogRepository = requestLogRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Transactional
    public void saveJsonRequest(RequestLog requestLog) {
        requestLogRepository.save(requestLog);
        Objects.requireNonNull(cacheManager.getCache("requestLog")).put(requestLog.getRequestId(), true);
    }

    @Override
    public boolean requestAlreadyExists(String requestId) {
        Cache cache = cacheManager.getCache("requestLog");
        if (cache != null && Boolean.TRUE.equals(cache.get(requestId, Boolean.class))) {
            return true;
        }
        return requestLogRepository.findByRequestId(requestId).isPresent();
    }
}
