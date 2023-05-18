/**
 * A REST API built using Spring, which serves as the backend for the angular frontend application. It exposes a way to call OpenAI APIs in a specific way and configuration.
 *
 * @author Vishal N
 * @version 1.0
 * @since 2023-01-09
 */
package com.example.imgGenBackend;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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
        return "Welcome to the backend for Vishal and Bimal's imagGen application. For more details, contact vishaln@bmc.com";
    }

    @CrossOrigin(origins = "http://127.0.0.1:4200")
    @PostMapping(value = "/image_prompt")
    public String image_prompt(@RequestBody Map<String, String> payload) throws IOException {

        String prompt = payload.get("prompt");
        String chatResponse = openAIClient.generateImagePrompt(prompt);
        System.out.printf("%s - %s\n", prompt, chatResponse);

        return chatResponse;
    }

    @CrossOrigin(origins = "http://127.0.0.1:4200")
    @PostMapping("/chat_prompt")
    public String chat_prompt(@RequestBody Map<String, String> payload) throws IOException {

        String prompt = payload.get("prompt");

        String chatResponse = openAIClient.generateContent(prompt);
        System.out.printf("%s - %s\n", prompt, chatResponse);

        return chatResponse;
    }


    @CrossOrigin(origins = "http://127.0.0.1:4200")
    @PostMapping(value = "/image", produces = "application/json")
    public ArrayList<PageExportBuilder.Image> image(@RequestBody Map<String, String> payload) throws IOException {
        int noOfImages = 1;
        String prompt, exclude, include, imgSize, backgroundColor;

        try {
            noOfImages = Integer.parseInt(payload.get("no"));
            if (noOfImages > 4) {
                throw new IllegalArgumentException("Max only 4 images are permitted. Will be set to 4");
            }
        } catch (NumberFormatException e) {
            noOfImages = 1;
        } catch (IllegalArgumentException e) {
            noOfImages = 4;
        }

        prompt = payload.get("prompt");
        exclude = payload.get("exclude");
        include = payload.get("include");
        backgroundColor = payload.get("backgroundColor");

        String preparedPrompt = DallEClient.promptBuilderForDallE(prompt, exclude, include, backgroundColor);


        imgSize = "1024x1024";
        /* img size code, excluded this field from the form.
        imgSize = payload.get("size");
        ArrayList<String> validImgSizes = new ArrayList<>() {
            {
                add("1024x1024");
                add("512x512");
                add("256x256");
            }
        };
        if (!validImgSizes.contains(imgSize)) {
            imgSize = "1024x1024";
        }*/

        if (Objects.equals(payload.get("mock"), "true")) {
            // Send a mock API Response, instead of wasting OpenAI credits when testing.
            System.out.println("Using mock API");
            return mockImageAPIResponse();
        }
        return openAIClient.genImage(preparedPrompt, noOfImages, imgSize);
    }


    @PostMapping(value = "/getzipID", produces = "application/json")
    public String getZipID(@RequestBody Map<String, List<String>> payload) throws IOException {
        ArrayList<String> imageIDs = new ArrayList<>() {
            {
                addAll(payload.get("imageIDs"));
            }
        };

        if (imageIDs.size() != 4) {
            throw new IllegalArgumentException(String.format("4 IDs must be provided to the request. Only %d were provided", imageIDs.size()));
        }


        String zipID = PageExportBuilder.generateZipFile(imageIDs);

        return zipID;
    }


    // Previous route was to /getzip/{zipID}. But it always saved the file as zipID instead of zipID.zip even if explicitely mentioning in the response headers. So the route was changed to /getzip/{zipID}.zip.
    @GetMapping(value = "/getzip/{zipID}.zip")
    public ResponseEntity<byte[]> getzip(@PathVariable String zipID, HttpServletResponse
            response) throws IOException {

        String zipPath = PageExportBuilder.getZipPath(zipID);

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
        System.out.println("Sending zip - " + zipID);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/zip"))
                .contentLength(buffer.length)
                .body(buffer);
    }

    private ArrayList<PageExportBuilder.Image> mockImageAPIResponse() throws IOException {
        ArrayList<String> imageURLs = new ArrayList<>() {
            {
                add("https://imgd.aeplcdn.com/1056x594/n/cw/ec/44686/activa-6g-right-front-three-quarter.jpeg?q=75");
            }
        };
        return PageExportBuilder.DallEImageRequestResponse(imageURLs);
    }
}