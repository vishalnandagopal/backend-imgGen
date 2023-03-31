package com.example.imaggenbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@SpringBootApplication
@RestController
public class imgGenRestAPI {

    static OpenAIClient openAIClient;

    public imgGenRestAPI() throws IOException {
        openAIClient = new OpenAIClient();
    }

    public static void main(String[] args) {
        SpringApplication.run(imgGenRestAPI.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "Welcome to the backend for Vishal and Bimal's imagGen";
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
        String exclude = payload.get("exclude");
        String include = payload.get("include");
        String backgroundColor = payload.get("backgroundColor");
        String preparedPrompt = DallE.imgPromptBuilder(prompt, exclude, include, backgroundColor);


        String imgSize = payload.get("size");

        String imgSrc = OpenAIClient.genImage(preparedPrompt, noOfImages, imgSize);

        System.out.printf("%d - %s - %s %s\n", noOfImages, imgSrc, imgSize, preparedPrompt);

        return imgSrc;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> payload)
        // @RequestParam(name = "role", required = true, defaultValue="user) String role,
        // @RequestParam(name = "prompt", required = true) String prompt,
            throws IOException {
        String prompt = payload.get("prompt");

        prompt = "I am trying to generate a prompt for image generation using DALLE-2. I will provide the context for it. Help me generate a suitable prompt that can be fed to DALLE-2 to generate a clear, appealing image without any distortions or blur. The context is: " + prompt;
        prompt = prompt + "Generate an effective prompt for it. Prompt: ";

        String role = payload.get("role");

        String chatResponse = OpenAIClient.genChat(prompt, role);
        System.out.printf("%s - %s - %s\n", prompt, chatResponse, role);

        return chatResponse;
    }
}