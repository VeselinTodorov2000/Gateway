package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.RequestLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Field;
import java.time.Instant;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticsPublisherServiceImplTest {
    @InjectMocks
    private StatisticsPublisherServiceImpl statisticsPublisherService;
    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field exchangeField = StatisticsPublisherServiceImpl.class.getDeclaredField("exchange");
        Field routingKeyField = StatisticsPublisherServiceImpl.class.getDeclaredField("routingKey");

        exchangeField.setAccessible(true);
        routingKeyField.setAccessible(true);

        exchangeField.set(statisticsPublisherService, "test_exchange");
        routingKeyField.set(statisticsPublisherService, "test_routing_key");
    }

    @Test
    void sendRequestLog_shouldSend_whenRequest_isProvided() {
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestId("1");
        requestLog.setTime(Instant.now());
        requestLog.setClientId("123");
        requestLog.setServiceName("EXT_SERVICE");

        statisticsPublisherService.sendRequestLog(requestLog);

        verify(rabbitTemplate).convertAndSend("test_exchange", "test_routing_key", requestLog);
    }
}