package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.entity.RequestLog;
import com.veselintodorov.gateway.repository.RequestLogRepository;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.StatisticsPublisherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {
    @InjectMocks
    private StatisticsServiceImpl statisticsService;
    @Mock
    private RequestLogRepository requestLogRepository;
    @Mock
    private ContextService contextService;
    @Mock
    private StatisticsPublisherService statisticsPublisherService;

    @Test
    void saveRequest_shouldSaveRequestToDbAndCache_whenValid() {
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestId("1");
        requestLog.setServiceName("");
        requestLog.setClientId("123");
        requestLog.setTime(Instant.parse("2007-12-03T10:15:30.00Z"));

        statisticsService.saveRequest(requestLog);

        verify(requestLogRepository).save(requestLog);
        verify(contextService).saveRequestById(requestLog.getRequestId());
        verify(statisticsPublisherService).sendRequestLog(requestLog);
    }

    @Test
    void requestExists_shouldReturnTrue_whenExistInCache() {
        String requestId = "1";
        when(contextService.findRequestById(requestId)).thenReturn(true);

        boolean result = statisticsService.requestAlreadyExists(requestId);

        assertThat(result).isTrue();
    }

    @Test
    void requestExists_shouldReturnTrue_whenExistInDatabase() {
        String requestId = "1";
        when(contextService.findRequestById(requestId)).thenReturn(false);
        when(requestLogRepository.findByRequestId(requestId)).thenReturn(Optional.of(new RequestLog()));

        boolean result = statisticsService.requestAlreadyExists(requestId);

        assertThat(result).isTrue();
    }

    @Test
    void requestExists_shouldReturnFalse_whenDoesNotExist() {
        String requestId = "1";
        when(contextService.findRequestById(requestId)).thenReturn(false);
        when(requestLogRepository.findByRequestId(requestId)).thenReturn(Optional.empty());

        boolean result = statisticsService.requestAlreadyExists(requestId);

        assertThat(result).isFalse();
    }
}