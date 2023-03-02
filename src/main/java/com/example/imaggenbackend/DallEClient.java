package com.example.imaggenbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DallEClient {
    public ImageGenerator imgGenerator;
    public ImageEditor imgEditor;

    public DallEClient() throws MalformedURLException {

        imgGenerator = new ImageGenerator(getOpenAIAPIKey("OPENAI_API_KEY"));
        imgEditor = new ImageEditor(getOpenAIAPIKey("OPENAI_API_KEY"));
    }

    public static String getOpenAIAPIKey(String keyName) {
        Dotenv dotenv = Dotenv.configure().filename("bmc.env").load();
        return dotenv.get(keyName);
    }
}

class ImageGenerator {
    static APICaller apiCaller;

    final static String API_URL = "https://api.openai.com/v1/images/generations";

    public ImageGenerator(String apiKey) throws MalformedURLException {
        apiCaller = new APICaller(API_URL, "POST", apiKey, "application/json");
    }

    public String generateImage(String prompt, int noOfImages, String imgSize)
            throws JsonMappingException, JsonProcessingException {
        // String prompt;
        // int noOfImages = 1;
        // String imgSize = "1024x1024";
        String requestBody = String.format("{ \"prompt\": \"%s\", \"n\": %d, \"size\": \"%s\" }", prompt, noOfImages,
                imgSize);
        // \"size\": \"1024x1024\"\n }";
        String response = "";
        String generated_image_url = "";
        try {
            response = apiCaller.newAPIRequest(requestBody);
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
        }
        generated_image_url = getURLFromResponseDict(response);
        return generated_image_url;
    }

    public static String getURLFromResponseDict(String responseBody)
            throws JsonMappingException, JsonProcessingException {

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the response string into a JsonNode object
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // Extract the URL attribute as a string
        JsonNode data_first_child = rootNode.get("data").get(0);
        String url = data_first_child.get("url").asText();
        // Return the URL
        return url;
    }
}

class ImageEditor {

    static APICaller apiCaller;

    final static String API_URL = "https://api.openai.com/v1/images/edits";

    public ImageEditor(String apiKey) throws MalformedURLException {
        apiCaller = new APICaller(API_URL, "POST", apiKey, "application/json");
    }
}

// class ImageEditor {

// final static String API_URL = "https://api.openai.com/v1/images/edits";
// static String API_Key;
// static APICaller APICaller = new APICaller(API_URL, "application/json",
// "POST", API_Key);

// public ImageEditor(String APIKey) {
// API_Key = APIKey;

// }

// public String editImage(String imgFilePath, int noOfImages, String imgSize) {
// // String prompt;
// // int noOfImages = 1;
// // String imgSize = "1024x1024";
// String requestBody = String.format("{ \"prompt\": \"%s\", \"n\": %d,
// \"size\": \"%s\" }", imgFilePath,
// noOfImages,
// imgSize);
// // \"size\": \"1024x1024\"\n }";

// String generated_image_url = "";
// try {
// generated_image_url = getURLFromResponse(APICaller(API_URL,
// "application/json",
// "POST", requestBody));
// } catch (Exception e) {
// System.err.println("Error : " + e.getMessage());
// }
// return generated_image_url;
// }
// }