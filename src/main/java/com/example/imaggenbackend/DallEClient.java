package com.example.imaggenbackend;

import java.io.IOException;
import java.net.MalformedURLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

public class DallEClient {
    private static ImageGenerator imgGenerator;
    // private static ImageEditor imgEditor;
    private String OPENAI_API_KEY;

    public DallEClient() throws IOException {

        this.OPENAI_API_KEY = getOpenAIAPIKey();
        imgGenerator = new ImageGenerator(this.OPENAI_API_KEY);
        // imgEditor = new ImageEditor(this.OPENAI_API_KEY);
    }

    public static String genImage(String prompt, int noOfImages, String imgSize) throws IOException {
        String response = imgGenerator.callAPI(prompt, noOfImages, imgSize);
        String generated_image_url = getURLFromResponseDict(response);
        return generated_image_url;
    }

    private static String getURLFromResponseDict(String responseBody)
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
        // String prompt;
        // int noOfImages = 1;
        // String imgSize = "1024x1024";
        String requestBody = String.format("{ \"prompt\": \"%s\", \"n\": %d, \"size\": \"%s\" }", prompt,
                noOfImages,
                imgSize);
        String response = httpCaller.newRequest(requestBody);
        return response;
    }
}

class ImageEditor {

    static HTTPCaller httpCaller;

    final static String API_URL = "https://api.openai.com/v1/images/edits";

    public ImageEditor(String apiKey) throws MalformedURLException {
        httpCaller = new HTTPCaller(API_URL, "POST", apiKey, "application/json");
    }

}