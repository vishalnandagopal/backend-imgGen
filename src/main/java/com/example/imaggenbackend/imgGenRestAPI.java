package com.example.imaggenbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@SpringBootApplication
@RestController
public class imgGenRestAPI {

    public static void main(String[] args) {
        SpringApplication.run(imgGenRestAPI.class, args);
    }

    @RestController
    public static class IndexController {
        @RequestMapping("/")
        public String index() {
            return "Welcome to the backend for Vishal and Bimal's imagGen";
        }
    }

    @RestController
    public static class postHelloController {
        static OpenAIClient openAIClient;

        public postHelloController() throws IOException {
            openAIClient = new OpenAIClient();
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
            // String exclude = payload.get("exclude")
            // String include = payload.get("include")
            // String backgroundColor = payload.get("backgroundColor")
            // String mainPrompt = DallEClient.promptBuilder(prompt, exclude, include, backgroundColor);
            String imgSize = payload.get("size");

            String imgSrc = OpenAIClient.genImage(prompt, noOfImages, imgSize);
            
            // String imgSrc = DallEClient.genImage(mainPrompt, noOfImages, imgSize);

            // String imgSrc = "https://www.simplilearn.com/ice9/free_resources_article_thumb/what_is_image_Processing.jpg";
            
            System.out.printf("%d - %s - %s %s\n", noOfImages, imgSrc, imgSize, prompt);
            
            return imgSrc;
        }

        @CrossOrigin(origins = "http://localhost:4200")
        @PostMapping("/chat")
        public String chat(@RequestBody Map<String, String> payload)
            // @RequestParam(name = "role", required = true, defaultValue="user) String role,
            // @RequestParam(name = "prompt", required = true) String prompt,
            // String imgSize)
                throws IOException {
            String prompt = payload.get("prompt");
            String role = payload.get("role");

            String chatResponse = OpenAIClient.genChat(prompt, role);

            System.out.printf("%s - %s - %s\n", prompt, chatResponse, role);

            return chatResponse;
        }
    }
}