package com.veselintodorov.gateway.controller;

import com.veselintodorov.gateway.converter.JsonConverter;
import com.veselintodorov.gateway.dto.JsonRequestDto;
import com.veselintodorov.gateway.dto.JsonResponseDto;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import com.veselintodorov.gateway.service.StatisticsService;
import com.veselintodorov.gateway.service.impl.ContextServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

import static com.veselintodorov.gateway.fixture.JsonResponseDtoFixture.failureResponse;
import static com.veselintodorov.gateway.fixture.JsonResponseDtoFixture.responseByCurrencyRate;

@RestController
@RequestMapping(path = "/json_api", consumes = "application/json", produces = "application/json")
public class JsonRestController {
    private final StatisticsService statisticsService;
    private final CurrencyRateService currencyRateService;
    private final JsonConverter jsonConverter;
    private final ContextService contextService;

    public JsonRestController(StatisticsService statisticsService, JsonConverter jsonConverter, CurrencyRateService currencyRateService, ContextServiceImpl contextService) {
        this.statisticsService = statisticsService;
        this.jsonConverter = jsonConverter;
        this.currencyRateService = currencyRateService;
        this.contextService = contextService;
    }

    @PostMapping("/current")
    ResponseEntity<JsonResponseDto> current(@RequestBody JsonRequestDto dto) {
        if (requestExists(dto.getRequestId())) {
            return ResponseEntity.badRequest().body(failureResponse());
        }
        try {
            statisticsService.saveJsonRequest(jsonConverter.mapRequestDtoToEntity(dto));
            BigDecimal currencyRate = currencyRateService.findLatestCurrencyRateForBaseByCurrencyCode(dto.getCurrencyCode());
            return ResponseEntity.ok().body(responseByCurrencyRate(dto.getCurrencyCode(), currencyRate, contextService.baseCurrency()));
        } catch (CurrencyNotFoundException e) {
            return ResponseEntity.badRequest().body(failureResponse());
        }
    }

    private boolean requestExists(UUID requestUuid) {
        return statisticsService.requestAlreadyExists(requestUuid);
    }
}
