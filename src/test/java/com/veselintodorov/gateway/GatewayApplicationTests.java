package com.veselintodorov.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootTest
@EnableCaching
@EnableScheduling
@EnableAsync
class GatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
