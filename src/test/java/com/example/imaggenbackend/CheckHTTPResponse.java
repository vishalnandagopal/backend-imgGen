package com.example.imaggenbackend;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.boot.test.web.server.LocalServerPort;


//
import static org.junit.Assert.assertEquals;
//@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckHTTPResponse {

    @LocalServerPort
    private int port = 8080;

    @Autowired
    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    public  void shouldPassIfStringMatches() throws Exception {
        assertEquals("D:\\BMC_Internship\\Coding\\ImagGenBackend\\src\\main\\resources\\static\\sample_image.png",
                testRestTemplate.getForObject("http://localhost:" + port + "/",
                        String.class));
    }
}
