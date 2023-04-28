package com.example.imaggenbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.MalformedURLException;

public class OpenAIClient {
    private static DallE imgGenerator;
    private static ChatGPT chatGenerator;

    public OpenAIClient() throws IOException {

        // private static ImageEditor imgEditor;
        String OPENAI_API_KEY = getOpenAIAPIKey();
        imgGenerator = new DallE(OPENAI_API_KEY);
        chatGenerator = new ChatGPT(OPENAI_API_KEY);
        // imgEditor = new ImageEditor(this.OPENAI_API_KEY);
    }

    public static String genImage(String prompt, int noOfImages, String imgSize) throws IOException {

        String response = imgGenerator.generateImage(prompt, noOfImages, imgSize);

        System.out.println("Response from Dalle is: \n" + response);
        String response_new = getURLFromResponseDict(response, true);

//        System.out.println("Response sent to UI is: \n" + response_new);
//        return response_new;

        return response;
    }

    public static String genChat(String prompt, String role) throws IOException {
        String response = chatGenerator.callAPI(prompt, role);
        return getURLFromResponseDict(response, false);
    }

    private static String getURLFromResponseDict(String responseBody, boolean dallEResponse)
            throws JsonProcessingException {
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the response string into a JsonNode object
        JsonNode rootNode = objectMapper.readTree(responseBody);

        if (dallEResponse) {
            // DallE response

            // Extract the prompt response attribute as a string
            JsonNode data_first_child = rootNode.get("data").get(0);

            // Return the URL
            return data_first_child.get("url").asText();
        } else {
            // Chatgpt response

            JsonNode choices_first_child = rootNode.get("choices").get(0);

            // Return the response content
            return choices_first_child.get("message").get("content").asText();
        }

    }

    private static String getOpenAIAPIKey() throws IOException {
        String filename = "";

        Dotenv dotenv = SaveImage.getDotenv(filename);
        return dotenv.get("OPENAI_API_KEY");
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
        if (exclude.length() > 2) {
            mainPrompt += "Exclude any " + exclude + " from the image.";
        }
        if (include.length() > 2) {
            mainPrompt += "Include " + include + " in the image.";
        }
        if (backgroundColor.length() > 2) {
            mainPrompt += "The background of the image must be " + backgroundColor + ".";
        }
        return mainPrompt;
    }

    public String generateImage(String prompt, int noOfImages, String imgSize)
            throws IOException {
        /* String prompt;
        int noOfImages = 1;
        String imgSize = "1024x1024";*/
        String requestBody = String.format("{ \"prompt\": \"%s\", \"n\": %d, \"size\": \"%s\" }", prompt,
                noOfImages,
                imgSize);
        return imageGenerator.newRequest(requestBody);
    }

    public String editImage(String apiKey) throws MalformedURLException {
        return "temp";
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
    // public String callApi(String content)  throws IOException{
    //     return callApi("user", content);
    // }
}