package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.RequestLog;
import com.veselintodorov.gateway.service.StatisticsPublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StatisticsPublisherServiceImpl implements StatisticsPublisherService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsPublisherServiceImpl.class);
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.statistics.exchange}")
    private String exchange;
    @Value("${rabbitmq.statistics.routing-key}")
    private String routingKey;

    public StatisticsPublisherServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendRequestLog(RequestLog requestLog) {
        rabbitTemplate.convertAndSend(exchange, routingKey, requestLog);
        logger.info("Sent request log: " + requestLog);
    }
}
