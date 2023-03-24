package com.example.imaggenbackend;

import java.io.IOException;
import java.net.MalformedURLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

public class DallEClient {
    private static ImageGenerator imgGenerator;
    private static ChatGPT chatGenerator;

    public DallEClient() throws IOException {

        // private static ImageEditor imgEditor;
        String OPENAI_API_KEY = getOpenAIAPIKey();
        imgGenerator = new ImageGenerator(OPENAI_API_KEY);
        chatGenerator = new ChatGPT(OPENAI_API_KEY);
        // imgEditor = new ImageEditor(this.OPENAI_API_KEY);
    }

    public String promptBuilder(String prompt, String exclude, String include, String backgroundColor) {
        String mainPrompt = prompt + " .";
        if (exclude.length() > 0) {
            mainPrompt += "Exclude " + exclude + " .";
        }
        if (include.length() > 0) {
            mainPrompt += "Include " + include + " .";
        }
        if (backgroundColor.length() > 0) {
            mainPrompt += "The backgroundColor of the image must be " + backgroundColor + " .";
        }
        return mainPrompt;
    }

    public static String genImage(String prompt, int noOfImages, String imgSize) throws IOException {
        String response = imgGenerator.callAPI(prompt, noOfImages, imgSize);
        return getURLFromResponseDict(response, true);
    }

    public static String genChat(String prompt, String role)throws IOException{
        String response = chatGenerator.callAPI(prompt, role);
        return getURLFromResponseDict(response, false);
    }

    private static String getURLFromResponseDict(String responseBody, boolean dalleresponse)
            throws JsonProcessingException {
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the response string into a JsonNode object
        JsonNode rootNode = objectMapper.readTree(responseBody);

        if (dalleresponse){
            // DallE response

            // Extract the prompt response attribute as a string
            JsonNode data_first_child = rootNode.get("data").get(0);

            // Return the URL
            return data_first_child.get("url").asText();
        }
        else{
            // Chatgpt response

            JsonNode choices_first_child = rootNode.get("choices").get(0);

            // Return the response content
            return choices_first_child.get("message").get("content").asText();
        }

    }

    private static String getOpenAIAPIKey() throws IOException {
        Dotenv dotenv;
        String filename = "";

        if (Miscellaneous.checkIfExistsInDirectory(Miscellaneous.pathify("./", "bmc.env"), false)) {
            filename = "./bmc.env";
        } else if (Miscellaneous.checkIfExistsInDirectory(Miscellaneous.pathify("./../", "bmc.env"), false)) {
            filename = "./../bmc.env";
        }

        if (filename.length() > 0) {
            dotenv = Dotenv.configure().ignoreIfMissing().filename(filename).load();
        } else {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        }
        return dotenv.get("OPENAI_API_KEY");
    }
}

class ImageGenerator {
    HTTPCaller httpCaller;

    final static String API_URL = "https://api.openai.com/v1/images/generations";

    public ImageGenerator(String apiKey) throws MalformedURLException {
        this.httpCaller = new HTTPCaller(API_URL, "POST", apiKey, "application/json");
    }

    public String callAPI(String prompt, int noOfImages, String imgSize)
            throws IOException {
        /* String prompt;
        int noOfImages = 1;
        String imgSize = "1024x1024";*/
        String requestBody = String.format("{ \"prompt\": \"%s\", \"n\": %d, \"size\": \"%s\" }", prompt,
                noOfImages,
                imgSize);
        return httpCaller.newRequest(requestBody);
    }
}

class ImageEditor {

    static HTTPCaller httpCaller;

    final static String API_URL = "https://api.openai.com/v1/images/edits";

    public ImageEditor(String apiKey) throws MalformedURLException {
        httpCaller = new HTTPCaller(API_URL, "POST", apiKey, "application/json");
    }

}

class ChatGPT {

    HTTPCaller httpCaller;

    final static String API_URL = "https://api.openai.com/v1/chat/completions";

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