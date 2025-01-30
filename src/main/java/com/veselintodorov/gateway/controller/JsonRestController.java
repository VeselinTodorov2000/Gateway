package com.veselintodorov.gateway.controller;

import com.veselintodorov.gateway.converter.JsonConverter;
import com.veselintodorov.gateway.dto.json.JsonRequestDto;
import com.veselintodorov.gateway.dto.json.JsonResponseDto;
import com.veselintodorov.gateway.entity.CurrencyRate;
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
import java.util.List;
import java.util.UUID;

import static com.veselintodorov.gateway.fixture.JsonResponseDtoFixture.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/json_api", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
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

    @PostMapping("/history")
    ResponseEntity<JsonResponseDto> history(@RequestBody JsonRequestDto dto) {
        if (requestExists(dto.getRequestId())) {
            return ResponseEntity.badRequest().body(failureResponse());
        }
        try {
            statisticsService.saveJsonRequest(jsonConverter.mapRequestDtoToEntity(dto));
            List<CurrencyRate> ratesForLastHours = currencyRateService.getRatesForLastHours(dto.getCurrencyCode(), dto.getTimestamp(), dto.getHours());
            return ResponseEntity.ok().body(historyResponseByCurrencyRates(dto.getCurrencyCode(), ratesForLastHours, contextService.baseCurrency()));
        } catch (CurrencyNotFoundException e) {
            return ResponseEntity.badRequest().body(failureResponse());
        }
    }

    private boolean requestExists(UUID requestUuid) {
        return statisticsService.requestAlreadyExists(requestUuid.toString());
    }
}
