package com.veselintodorov.gateway.controller;

import com.veselintodorov.gateway.dto.xml.GetRequest;
import com.veselintodorov.gateway.dto.xml.RateEntry;
import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import com.veselintodorov.gateway.facade.XmlFacade;
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
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class XmlRestControllerTest {

    @Mock
    private XmlFacade xmlFacade;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private XmlRestController xmlRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(xmlRestController).build();
    }

    @Test
    void command_shouldReturnCreated_WhenValidGetRequest() throws Exception {
        String requestId = "1";
        XmlRequestDto dto = new XmlRequestDto();
        dto.setId(requestId);
        GetRequest getRequest = new GetRequest();
        getRequest.setCurrency("USD");
        dto.setGetRequest(getRequest);
        dto.setHistoryRequest(null);

        XmlResponseDto responseDto = new XmlResponseDto();
        responseDto.setCurrency("USD");
        responseDto.setRates(Collections.singletonList(new RateEntry(Instant.now(), BigDecimal.valueOf(1.2))));

        when(statisticsService.requestAlreadyExists(requestId)).thenReturn(false);
        when(xmlFacade.findCurrentRate(dto)).thenReturn(responseDto);

        mockMvc.perform(post("/xml_api/command")
                        .contentType(APPLICATION_XML_VALUE)
                        .content("<XmlRequest><id>" + requestId + "</id><getRequest><currency>USD</currency></getRequest></XmlRequest>"))
                .andExpect(status().isCreated());
    }
}
