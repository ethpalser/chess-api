package com.chess.api.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoggingFilterTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void givenRequest_whenInvokeUnauthenticatedEndpoint_then200() {
        ResponseEntity<String> result = testRestTemplate.getForEntity("/anonymous", String.class);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        // Inspect console output to verify information has been logged
    }

}
