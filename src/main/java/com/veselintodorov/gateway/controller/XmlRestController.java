package com.veselintodorov.gateway.controller;

import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import com.veselintodorov.gateway.facade.XmlFacade;
import com.veselintodorov.gateway.handler.CurrencyNotFoundException;
import com.veselintodorov.gateway.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.veselintodorov.gateway.fixture.XmlResponseDtoFixture.failureResponse;
import static com.veselintodorov.gateway.fixture.XmlResponseDtoFixture.noCurrencyFoundResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(path = "/xml_api", consumes = APPLICATION_XML_VALUE, produces = APPLICATION_XML_VALUE)
public class XmlRestController {
    private final XmlFacade xmlFacade;
    private final StatisticsService statisticsService;

    public XmlRestController(XmlFacade xmlFacade, StatisticsService statisticsService) {
        this.xmlFacade = xmlFacade;
        this.statisticsService = statisticsService;
    }

    @PostMapping(path = "/command")
    public ResponseEntity<XmlResponseDto> command(@RequestBody XmlRequestDto dto) {
        if (requestExists(dto)) {
            return failureResponse();
        }
        xmlFacade.saveRequest(dto);
        try {
            return getResponseBasedOnRequestType(dto);
        } catch (CurrencyNotFoundException e) {
            return noCurrencyFoundResponse(e.getMessage());
        }
    }

    private ResponseEntity<XmlResponseDto> getResponseBasedOnRequestType(XmlRequestDto dto) throws CurrencyNotFoundException {
        if (isGetRequest(dto)) {
            return new ResponseEntity<>(xmlFacade.findCurrentRate(dto), CREATED);
        } else {
            return new ResponseEntity<>(xmlFacade.findHistoryRate(dto), CREATED);
        }
    }

    private boolean isGetRequest(XmlRequestDto dto) {
        return dto.getGetRequest() != null && dto.getHistoryRequest() == null;
    }

    private boolean requestExists(XmlRequestDto requestDto) {
        return statisticsService.requestAlreadyExists(requestDto.getId());
    }
}
