package com.veselintodorov.gateway.service.impl;

import com.veselintodorov.gateway.dto.FixerResponseDto;
import com.veselintodorov.gateway.facade.FixerFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FixerFetchServiceImplTest {

    private final String fixerApiUrl = "url";
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private FixerFacade fixerFacade;
    @InjectMocks
    private FixerFetchServiceImpl fixerFetchService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field fixerApiUrlField = FixerFetchServiceImpl.class.getDeclaredField("fixerApiUrl");
        fixerApiUrlField.setAccessible(true);
        fixerApiUrlField.set(fixerFetchService, fixerApiUrl);
    }

    @Test
    void fetchAndSaveCurrencyRates_Success() {
        FixerResponseDto fixerResponseDto = new FixerResponseDto();
        ResponseEntity<FixerResponseDto> responseEntity = new ResponseEntity<>(fixerResponseDto, HttpStatus.OK);
        when(restTemplate.getForEntity(fixerApiUrl, FixerResponseDto.class)).thenReturn(responseEntity);

        fixerFetchService.fetchAndSaveCurrencyRates();

        verify(fixerFacade, times(1)).saveRates(fixerResponseDto);
    }

    @Test
    void fetchAndSaveCurrencyRates_Failure() {
        ResponseEntity<FixerResponseDto> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.getForEntity(fixerApiUrl, FixerResponseDto.class)).thenReturn(responseEntity);

        fixerFetchService.fetchAndSaveCurrencyRates();

        verify(fixerFacade, times(0)).saveRates(any());
    }
}
