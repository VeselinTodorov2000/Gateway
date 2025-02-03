package com.veselintodorov.gateway.controller;

import com.veselintodorov.gateway.dto.json.JsonRequestDto;
import com.veselintodorov.gateway.dto.json.JsonResponseDto;
import com.veselintodorov.gateway.facade.JsonFacade;
import com.veselintodorov.gateway.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JsonRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JsonFacade jsonFacade;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private JsonRestController jsonRestController;

    private JsonRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jsonRestController).build();

        requestDto = new JsonRequestDto();
        requestDto.setRequestId(UUID.randomUUID());
        requestDto.setClientId("client-123");
        requestDto.setTimestamp(Instant.now());
        requestDto.setCurrencyCode("USD");
    }

    @Test
    void current_shouldReturnCreated_WhenCurrencyExists() throws Exception {
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setCurrencyCode("USD");
        responseDto.setRates(Map.of(Instant.now(), BigDecimal.valueOf(1.2)));
        when(statisticsService.requestAlreadyExists(any(String.class))).thenReturn(false);
        when(jsonFacade.findCurrentRate(any(JsonRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/json_api/current")
                        .contentType("application/json")
                        .content("{\"requestId\":\"" + UUID.randomUUID() + "\", \"clientId\":\"client-123\", \"currencyCode\":\"USD\", \"timestamp\":123456789}")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.rates").exists());
    }

    @Test
    void current_shouldReturnFailure_WhenRequestAlreadyExists() throws Exception {
        UUID requestId = UUID.randomUUID();
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setCurrencyCode("USD");
        responseDto.setRates(Map.of(
                Instant.now().minusSeconds(3600), BigDecimal.valueOf(1.1),
                Instant.now(), BigDecimal.valueOf(1.2)
        ));

        when(statisticsService.requestAlreadyExists(requestId.toString())).thenReturn(true);

        mockMvc.perform(post("/json_api/current")
                        .contentType("application/json")
                        .content("{\"requestId\":\"" + requestId + "\", \"clientId\":\"client-123\", \"currencyCode\":\"USD\", \"timestamp\":123456789}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.currency").value("Request already exists"));
    }


    @Test
    void history_shouldReturnCreated_WhenCurrencyHistoryExists() throws Exception {
        // Arrange
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setCurrencyCode("USD");
        responseDto.setRates(Map.of(
                Instant.now().minusSeconds(3600), BigDecimal.valueOf(1.1),
                Instant.now(), BigDecimal.valueOf(1.2)
        ));
        when(statisticsService.requestAlreadyExists(any(String.class))).thenReturn(false);
        when(jsonFacade.findHistoryRate(any(JsonRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/json_api/history")
                        .contentType("application/json")
                        .content("{\"requestId\":\"" + UUID.randomUUID() + "\", \"clientId\":\"client-123\", \"currencyCode\":\"USD\", \"timestamp\":123456789, \"hours\":24}")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.rates").exists());
    }

    @Test
    void history_shouldReturnFailure_WhenRequestAlreadyExists() throws Exception {
        UUID requestId = UUID.randomUUID();
        JsonResponseDto responseDto = new JsonResponseDto();
        responseDto.setCurrencyCode("USD");
        responseDto.setRates(Map.of(
                Instant.now().minusSeconds(3600), BigDecimal.valueOf(1.1),
                Instant.now(), BigDecimal.valueOf(1.2)
        ));

        when(statisticsService.requestAlreadyExists(requestId.toString())).thenReturn(true);

        mockMvc.perform(post("/json_api/history")
                        .contentType("application/json")
                        .content("{\"requestId\":\"" + requestId + "\", \"clientId\":\"client-123\", \"currencyCode\":\"USD\", \"timestamp\":123456789, \"hours\":24}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.currency").value("Request already exists"));
    }

}
