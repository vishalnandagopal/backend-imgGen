package com.example.imaggenbackend;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class imgGenRestAPI {

    public static void main(String[] args) {
        SpringApplication.run(imgGenRestAPI.class, args);
    }

    @RestController
    public class IndexController {
        @RequestMapping("/")
        public String index() {
            return "Welcome to the backend for Vishal and Bimal's imagGen";
        }
    }

    @RestController
    public class postHelloController {
        static DallEClient dallEClient;

        public postHelloController() throws MalformedURLException {
            dallEClient = new DallEClient();
        }

        @PostMapping("/image")
        public String postHello(
                @RequestParam(name = "prompt", required = true) String prompt,
                @RequestParam(name = "no", required = true, defaultValue = "1") String no,
                @RequestParam(name = "size", required = true, defaultValue = "1024x1024") String imgSize)
                throws IOException {
            int noOfImages = Integer.parseInt(no);
            String img_src = DallEClient.genImage(prompt, noOfImages, imgSize);
            System.out.println(img_src);
            return img_src;
        }
    }
}