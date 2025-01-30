package com.veselintodorov.gateway.controller;

import com.veselintodorov.gateway.dto.xml.XmlRequestDto;
import com.veselintodorov.gateway.dto.xml.XmlResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(path = "/xml_api", consumes = APPLICATION_XML_VALUE, produces = APPLICATION_XML_VALUE)
public class XmlRestController {
    @PostMapping(path = "/command")
    public ResponseEntity<XmlResponseDto> command(@RequestBody XmlRequestDto dto) {
        return null;
    }
}
