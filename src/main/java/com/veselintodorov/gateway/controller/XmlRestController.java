package com.veselintodorov.gateway.controller;

import com.veselintodorov.gateway.converter.XmlConverter;
import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.ContextService;
import com.veselintodorov.gateway.service.CurrencyRateService;
import com.veselintodorov.gateway.service.StatisticsService;
import com.veselintodorov.gateway.service.XmlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.veselintodorov.gateway.fixture.XmlResponseDtoFixture.failureResponse;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(path = "/xml_api", consumes = APPLICATION_XML_VALUE, produces = APPLICATION_XML_VALUE)
public class XmlRestController {
    private final StatisticsService statisticsService;
    private final CurrencyRateService currencyRateService;
    private final XmlConverter xmlConverter;
    private final XmlService xmlService;
    private final ContextService contextService;

    public XmlRestController(StatisticsService statisticsService,
                             CurrencyRateService currencyRateService,
                             XmlConverter xmlConverter,
                             XmlService xmlService,
                             ContextService contextService) {
        this.statisticsService = statisticsService;
        this.currencyRateService = currencyRateService;
        this.xmlConverter = xmlConverter;
        this.xmlService = xmlService;
        this.contextService = contextService;
    }

    @PostMapping(path = "/command")
    public ResponseEntity<XmlResponseDto> command(@RequestBody XmlRequestDto dto) {
        if (requestExists(dto.getId())) {
            return ResponseEntity.badRequest().body(failureResponse());
        }
        try {
            return new ResponseEntity<>(xmlService.handleValidXmlRequest(dto), HttpStatus.CREATED);
        }
        catch (CurrencyNotFoundException c) {
            return ResponseEntity.badRequest().body(failureResponse());
        }
    }

    private boolean requestExists(String requestId) {
        return statisticsService.requestAlreadyExists(requestId);
    }
}
