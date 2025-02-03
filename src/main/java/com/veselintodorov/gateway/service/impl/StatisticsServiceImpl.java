package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.RequestLog;
import com.veselintodorov.gateway.repository.RequestLogRepository;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.StatisticsPublisherService;
import com.veselintodorov.gateway.service.StatisticsService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
    private final RequestLogRepository requestLogRepository;
    private final ContextService contextService;
    private final StatisticsPublisherService statisticsPublisherService;

    public StatisticsServiceImpl(RequestLogRepository requestLogRepository, ContextService contextService, StatisticsPublisherService statisticsPublisherService) {
        this.requestLogRepository = requestLogRepository;
        this.contextService = contextService;
        this.statisticsPublisherService = statisticsPublisherService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveRequest(RequestLog requestLog) {
        contextService.saveRequestById(requestLog.getRequestId());
        requestLogRepository.save(requestLog);
        sendRequestLogAsync(requestLog);
    }

    @Async
    public void sendRequestLogAsync(RequestLog requestLog) {
        try {
            statisticsPublisherService.sendRequestLog(requestLog);
        } catch (Exception e) {
            logger.error("Error while sending request log to RabbitMQ", e);
        }
    }


    @Override
    public boolean requestAlreadyExists(String requestId) {
        if (contextService.findRequestById(requestId)) {
            return true;
        }
        return requestLogRepository.findByRequestId(requestId).isPresent();
    }
}
