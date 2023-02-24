package com.example.imaggenbackend;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
//import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;


//
import static org.junit.Assert.assertEquals;
//@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckHTTPResponse {

    @LocalServerPort
    private int port = 4500;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public  void shouldPassIfStringMatches() throws Exception {
        assertEquals("I have no idea what I'm doing.",
                testRestTemplate.getForObject("http://localhost:" + port + "/",
                        String.class));
    }
}
