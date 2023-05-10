package com.example.imaggenbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class OpenAIClient {
    private final DallE imgGenerator;
    private final ChatGPT chatGenerator;

    public OpenAIClient() throws IOException {

        String OPENAI_API_KEY = getOpenAIAPIKey();
        imgGenerator = new DallE(OPENAI_API_KEY);
        chatGenerator = new ChatGPT(OPENAI_API_KEY);
        // imgEditor = new ImageEditor(this.OPENAI_API_KEY);
    }

    private static String extractAnswerFromChatGPTResponse(String responseBody)
            throws JsonProcessingException {

        System.out.println(responseBody);

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the response string into a JsonNode object
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // We are handling a response from ChatGPT. We need to extract the reply as a text.

        JsonNode choices_first_child = rootNode.get("choices").get(0);

        // Return the response content
        return choices_first_child.get("message").get("content").asText();
    }

    private static ArrayList<String> extractImgSRCsFromDallEResponse(String responseBody)
            throws JsonProcessingException {

        System.out.println(responseBody);

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the response string into a JsonNode object
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // We are handling a response from Dall-E. We need to extract the image SRCs from the response
        ArrayList<String> imgSRCs = new ArrayList<>();


        int noOfImages = rootNode.get("data").size();

        for (int i = 0; i < noOfImages; i++) {
            imgSRCs.add(rootNode.get("data").get(i).get("url").asText());
        }

        // Return the URLs
        return imgSRCs;
    }

    private static String getOpenAIAPIKey() throws IOException {
        return Miscellaneous.readDotenv("bmc.env", "OPENAI_API_KEY", true);
    }

    public pageExportBuilder.imageRequestResponse genImage(String prompt, int noOfImages, String imgSize) throws IOException {
        String response = imgGenerator.generateImage(prompt, noOfImages, imgSize);
        System.out.println("Response from Dall-E is: \n" + response);
        ArrayList<String> imgSRCs = extractImgSRCsFromDallEResponse(response);

        int zipID = Miscellaneous.generateRandomZipID();

        for (String imageURL : imgSRCs) {
            pageExportBuilder.downloadImage(imageURL, zipID);
        }
        System.out.println(pageExportBuilder.getZipPath(String.valueOf(zipID)));

        // {
        //            "zipID":zipID,
        // "data":"url1, url2"
        // }
        return new pageExportBuilder.imageRequestResponse(zipID, imgSRCs);
    }

    public String genChat(String prompt, String role) throws IOException {
        String response = chatGenerator.callAPI(prompt, role);
        return extractAnswerFromChatGPTResponse(response);
    }


}

class DallE {
    final static String GEN_IMAGE_API_URL = "https://api.openai.com/v1/images/generations";
    final static String EDIT_IMAGE_API_URL = "https://api.openai.com/v1/images/edits";
    HTTPCaller imageGenerator;
    HTTPCaller imageEditor;

    public DallE(String apiKey) throws MalformedURLException {
        this.imageGenerator = new HTTPCaller(GEN_IMAGE_API_URL, "POST", apiKey, "application/json");
        this.imageEditor = new HTTPCaller(EDIT_IMAGE_API_URL, "POST", apiKey, "application/json");

    }

    public static String imgPromptBuilder(String prompt, String exclude, String include, String backgroundColor) {
        String mainPrompt = prompt + ".";
        if (exclude != null) {
            if (exclude.length() > 2) {
                mainPrompt += "Exclude any " + exclude + " from the image.";
            }
        }
        if (include != null) {
            if (include.length() > 2) {
                mainPrompt += "Include " + include + " in the image.";
            }
        }
        if (backgroundColor != null) {
            if (backgroundColor.length() > 2) {
                mainPrompt += "The background of the image must be " + backgroundColor + ".";
            }
        }
        return mainPrompt;
    }

    public String generateImage(String prompt, int noOfImages, String imgSize)
            throws IOException {
         /*String prompt;
        int noOfImages = 1;
        String imgSize = "1024x1024"; */
        String requestBody = String.format("{ \"prompt\": \"%s\", \"n\": %d, \"size\": \"%s\" }", prompt,
                noOfImages,
                imgSize);
        return imageGenerator.newRequest(requestBody);
    }
}

class ChatGPT {

    final static String API_URL = "https://api.openai.com/v1/chat/completions";
    HTTPCaller httpCaller;

    ChatGPT(String apiKey) throws MalformedURLException {
        this.httpCaller = new HTTPCaller(API_URL, "POST", apiKey, "application/json");
    }

    public String callAPI(String content, String role)
            throws IOException {
        // String role
        // String content

        ArrayList<String> validRoles = new ArrayList<>() {
            {
                add("system");
                add("assistant");
                add("user");
            }
        };

        if (!validRoles.contains(role)) {
            role = "user";
        }

        String model = "gpt-3.5-turbo";

        String requestBody = String.format("{" +
                        "\"model\": \"%s\"," +
                        "\"messages\": [" +
                        "{" +
                        "\"role\": \"%s\"," +
                        "\"content\": \"%s!\"" +
                        "}" +
                        "]}",
                model, role, content);

        return httpCaller.newRequest(requestBody);
    }
}