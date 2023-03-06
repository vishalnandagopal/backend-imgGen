package com.example.imaggenbackend;

import java.io.IOException;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
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

        public postHelloController() throws IOException {
            dallEClient = new DallEClient();
        }

        @CrossOrigin(origins = "http://localhost:4200")
        @PostMapping("/image")
        public String postImage(@RequestBody Map<String, String> payload)
                // @RequestParam(name = "prompt", required = true) String prompt,
                // @RequestParam(name = "no", required = true, defaultValue = "1") String no,
                // @RequestParam(name = "size", required = true, defaultValue = "1024x1024")
                // String imgSize)
                throws IOException {
            int noOfImages = Integer.parseInt(payload.get("no"));
            String prompt = payload.get("prompt");
            String imgSize = payload.get("size");
            String imgSrc = DallEClient.genImage(prompt, noOfImages, imgSize);
            // String imgSrc = "https://www.simplilearn.com/ice9/free_resources_article_thumb/what_is_image_Processing.jpg";
            System.out.printf("%d - %s - %s %s\n", noOfImages, imgSrc, imgSize, prompt);
            return imgSrc;
        }
    }
}