/**
 * A REST API built using Spring, which serves as the backend for the angular frontend application. It exposes a way to call OpenAI APIs in a specific way and configuration.
 *
 * @author Vishal N
 * @version 1.0
 * @since 2023-01-09
 */
package com.example.imaggenbackend;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> payload) throws IOException {

        String prompt = payload.get("prompt");

        prompt = "I am trying to generate a prompt for image generation using DALL-E 2. I will provide the context for it. Help me generate a suitable prompt that can be fed to DALL-E 2 to generate a clear, appealing image without any distortions or blur. The context is: " + prompt;

        prompt = prompt + "Generate an effective prompt for it. Prompt: ";

        String role = payload.get("role");

        String chatResponse = openAIClient.genChat(prompt, role);
        System.out.printf("%s - %s - %s\n", prompt, chatResponse, role);

        return chatResponse;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/image", produces = "application/json")
    public pageExportBuilder.imageRequestResponse image(@RequestBody Map<String, String> payload) throws IOException {

        int noOfImages = Integer.parseInt(payload.get("no"));

        String prompt = payload.get("prompt");
        String exclude = payload.get("exclude");
        String include = payload.get("include");
        String imgSize = payload.get("size");
        String backgroundColor = payload.get("backgroundColor");

        String preparedPrompt = DallE.imgPromptBuilder(prompt, exclude, include, backgroundColor);

        ArrayList<String> validImgSizes = new ArrayList<>() {
            {
                add("1024x1024");
                add("512x512");
                add("256x256");
            }
        };
        if (!validImgSizes.contains(imgSize)) {
            imgSize = "1024x1024";
        }

        pageExportBuilder.imageRequestResponse zipIDtoImageSRCs = openAIClient.genImage(preparedPrompt, noOfImages, imgSize);
        System.out.printf("%d - %s - %s %s\n", noOfImages, zipIDtoImageSRCs, imgSize, preparedPrompt);
        /*
        ArrayList<String> al = new ArrayList<>() {
            {
                add("https://google.com");
                add("https://vishalnandagopal.com");
                add("https://gmail.com");
                add("https://youtube.com");
            }
        };
        OpenAIClient.TestClass zipIDtoImageSRCs = new OpenAIClient.TestClass(724771, al);
        */
        return zipIDtoImageSRCs;
    }

    @PostMapping("/getzip/{zipID}")
    public ResponseEntity<byte[]> streamZipFile(@PathVariable String zipID, HttpServletResponse response) throws IOException {
        try {
            String zipPath = pageExportBuilder.getZipPath(zipID);

            // Stream the zip file to the response

            FileInputStream fis = new FileInputStream(zipPath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
            os.close();
            bis.close();
            fis.close();

            // Set response entity and all associated properties.
            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            String.format("attachment; filename=\"Dall-E Images - %s.zip\"", zipID)
                    )
                    .contentType(MediaType.valueOf("application/zip"))
                    .contentLength(buffer.length)
                    .body(buffer);

        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ResponseEntity.internalServerError()
                    .body(String.format("Error while generating zip - %s", e)
                            .getBytes(StandardCharsets.UTF_8));
        }
    }
}