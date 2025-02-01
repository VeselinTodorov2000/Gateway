package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.RequestLog;
import com.veselintodorov.gateway.repository.RequestLogRepository;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.StatisticsPublisherService;
import com.veselintodorov.gateway.service.StatisticsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final RequestLogRepository requestLogRepository;
    private final ContextService contextService;
    private final StatisticsPublisherService statisticsPublisherService;

    public StatisticsServiceImpl(RequestLogRepository requestLogRepository, ContextService contextService, StatisticsPublisherService statisticsPublisherService) {
        this.requestLogRepository = requestLogRepository;
        this.contextService = contextService;
        this.statisticsPublisherService = statisticsPublisherService;
    }

    @Override
    @Transactional
    public void saveRequest(RequestLog requestLog) {
        requestLogRepository.save(requestLog);
        contextService.saveRequestById(requestLog.getRequestId());
        statisticsPublisherService.sendRequestLog(requestLog);
    }

    @Override
    public boolean requestAlreadyExists(String requestId) {
        if (contextService.findRequestById(requestId)) {
            return true;
        }
        return requestLogRepository.findByRequestId(requestId).isPresent();
    }
}
