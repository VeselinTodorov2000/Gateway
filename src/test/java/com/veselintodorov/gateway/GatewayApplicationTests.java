package com.veselintodorov.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootTest
@EnableCaching
@EnableRetry
@EnableScheduling
@EnableAsync
class GatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
