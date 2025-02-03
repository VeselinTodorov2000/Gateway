package com.veselintodorov.gateway.controller;

import com.veselintodorov.gateway.dto.json.JsonRequestDto;
import com.veselintodorov.gateway.dto.json.JsonResponseDto;
import com.veselintodorov.gateway.facade.JsonFacade;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.veselintodorov.gateway.fixture.JsonResponseDtoFixture.failureResponse;
import static com.veselintodorov.gateway.fixture.JsonResponseDtoFixture.noCurrencyFoundResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/json_api", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class JsonRestController {
    private final JsonFacade jsonFacade;
    private final StatisticsService statisticsService;

    public JsonRestController(JsonFacade jsonFacade, StatisticsService statisticsService) {
        this.jsonFacade = jsonFacade;
        this.statisticsService = statisticsService;
    }

    @PostMapping("/current")
    ResponseEntity<JsonResponseDto> current(@RequestBody JsonRequestDto dto) {
        if (requestExists(dto.getRequestId())) {
            return failureResponse();
        }
        jsonFacade.saveRequest(dto);
        try {
            return new ResponseEntity<>(jsonFacade.findCurrentRate(dto), CREATED);
        } catch (CurrencyNotFoundException e) {
            return noCurrencyFoundResponse(e.getMessage());
        }
    }

    @PostMapping("/history")
    ResponseEntity<JsonResponseDto> history(@RequestBody JsonRequestDto dto) {
        if (requestExists(dto.getRequestId())) {
            return failureResponse();
        }
        jsonFacade.saveRequest(dto);
        try {
            return new ResponseEntity<>(jsonFacade.findHistoryRate(dto), CREATED);
        } catch (CurrencyNotFoundException e) {
            return noCurrencyFoundResponse(e.getMessage());
        }
    }

    private boolean requestExists(UUID requestUuid) {
        return statisticsService.requestAlreadyExists(requestUuid.toString());
    }
}
