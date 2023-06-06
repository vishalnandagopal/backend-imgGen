package com.example.imgGenBackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * A class for working with JSON. Processing requests from ChatGPT, Dall-E and Stability AI. Preparing {@code data.json} file, {@code export.manifest} and getting JSON objects from JSON strings.
 *
 * @author Vishal N (vishalnandagopal.com)
 */
public class JSONPreparer {

    /**
     * Processes the JSON given by the ChatGPT API and returns its answer.
     *
     * @param responseBody - The body of the response given by the ChatGPT API.
     * @return ChatGPT's reply
     * @throws JsonProcessingException If there is an issue while processing the JSON
     */
    public static String extractAnswerFromChatGPTResponse(String responseBody)
            throws JsonProcessingException {

        JsonNode rootNode = JSONPreparer.getJsonObjectMap(responseBody);

        // We are handling a response from ChatGPT. We need to extract the reply as a text.
        JsonNode choices_first_child = rootNode.get("choices").get(0);

        // Return the response content
        return choices_first_child.get("message").get("content").asText();
    }

    /**
     * Processes the JSON given by the Stability AI API. Since it returns the images encoded as a base64 string, this functions saves the images and returns their IDs.{@link Image Image} objects
     *
     * @param responseBody The raw response returned by the API call
     * @return An {@link ArrayList} of URLs as strings
     * @throws JsonProcessingException
     */
    public static ArrayList<String> extractBase64ImageFromStabilityAIResponse(String responseBody) throws JsonProcessingException {

        JsonNode rootNode = JSONPreparer.getJsonObjectMap(responseBody);

        // We are handling a response from Stability AI. We need to extract the image SRCs from the response
        ArrayList<String> base64EncodedStrings = new ArrayList<>();

        int noOfImages = rootNode.size();

        for (int i = 0; i < noOfImages; i++) {
            base64EncodedStrings.add(rootNode.get("artifacts").get(i).get("base64").asText());
        }

        // Return the base64 encoded versions of the images
        return base64EncodedStrings;
    }

    /**
     * Processes the JSON given by the Dall-E API and returns the URLs of the images it created as a response to your API call.
     *
     * @param responseBody - The body of the response given by the Dall-E API.
     * @return An {@code ArrayList<String>} of the URLs of the images generated by Dall-E.
     * @throws JsonProcessingException If there is an issue with processing the JSON
     */
    public static ArrayList<String> extractImageURLsFromDallEResponse(String responseBody)
            throws JsonProcessingException {

        JsonNode rootNode = JSONPreparer.getJsonObjectMap(responseBody);

        // We are handling a response from Dall-E. We need to extract the image SRCs from the response
        ArrayList<String> imgSRCs = new ArrayList<>();

        if (rootNode.get("data") != null) {
            int noOfImages = rootNode.get("data").size();

            for (int i = 0; i < noOfImages; i++) {
                imgSRCs.add(rootNode.get("data").get(i).get("url").asText());
            }
        }
        // Return the URLs
        return imgSRCs;
    }


    /**
     * Retrieve the {@code data.json} that must be included in the zip, by replacing the IDs of the blobs with the image IDs and replacing the export ID to be the same as the one in the {@code export.manifest} file.
     *
     * @param imageIDs        The Arraylist of imageIDs as strings. Must contain 4.
     * @param exportID        The DWP page export ID
     * @param pageName        The name of the page to put into the {@code data.json}
     * @param pageDescription
     * @return A string that can be written to a {@code data.json} file as it is
     * @throws IOException IOException If there is an error while reading the template export.manifest or while processing the JSON.
     */
    public static String processDataJson(ArrayList<String> imageIDs, String exportID, String pageName, String pageDescription) throws IOException {
        File dataJsonFile = new File("./src/main/resources/templates/data.json");
        String fileContent = readFile(dataJsonFile.toPath());
        JsonNode jsonObject = getJsonObjectMap(fileContent);
        int count = 0;
        ArrayList<String> oldImageIDs = new ArrayList<>();
        for (JsonNode i : jsonObject) {
            ObjectNode changeableFieldsObject;
            ObjectNode changeableAttachmentObject;
            ObjectNode jsonChildObject = (ObjectNode) i;
            if (i.get("data").get("name").asText().equals("Attachment")) {
                // This section means the item in the JSON array is an attachment. Like an image.

                // Changing the ID, file size and filename
                changeableFieldsObject = ((ObjectNode) (jsonChildObject.get("data").get("fields"))); // Casting to a mutable object. JSONNode is immutable

                // Get the old ID and add it to oldImageIDs
                oldImageIDs.add(changeableFieldsObject.get("420003510").asText());

                // Change ID
                changeableFieldsObject.put("420003510", imageIDs.get(count));

                // Change file size
                changeableFieldsObject.put("420003507", String.valueOf(Miscellaneous.getFileSize(imageIDs.get(count))));

                // Change file name
                // String filename = String.format("Dall-E Generated Image - %d - %s.png", count, Miscellaneous.generateRandomFileName());
                String filename = imageIDs.get(count);
                changeableFieldsObject.put("420003506", filename);
                changeableFieldsObject.put("420003509", filename);

                changeableAttachmentObject = ((ObjectNode) (jsonChildObject.get("data").get("attachments").get(0)));
                changeableAttachmentObject.put("fileName", filename);
                changeableAttachmentObject.put("contentRef", imageIDs.get(count));
                count++;
            }
            else if (i.get("data").get("name").asText().equals("Page")) {
                // It is a page.
                ObjectNode pageObject = (ObjectNode) i;
                changeableFieldsObject = ((ObjectNode) (pageObject.get("data").get("fields")));
                changeableFieldsObject.put("420003507", exportID);

                changeableFieldsObject.put("420003505", pageName);
                changeableFieldsObject = ((ObjectNode) (pageObject.get("data").get("localizedFields").get("420003505")));
                changeableFieldsObject.put("en-US", pageName);
                changeableFieldsObject.put("_", pageName);
            }
        }
        String jsonString = jsonObject.toString();

        for (int i = 0; i < count; i++) {
            jsonString = jsonString.replace(oldImageIDs.get(i), imageIDs.get(i));
        }
        // Adding page description
        jsonString = jsonString.replace("Sample text for a text block", pageDescription);
        return jsonString;
    }

    /**
     * Prepares the {@code export.manifest} file
     *
     * @param exportID The export ID as a String
     * @return The entire contents that can be written to an {@code export.manifest} file, as it is.
     * @throws IOException If there is an error while reading the template {@code export.manifest} or while processing the JSON.
     */
    public static String processExportManifest(String exportID) throws IOException {
        File exportManifesFile = new File("./src/main/resources/templates/export.manifest");
        String fileContent = readFile(exportManifesFile.toPath());

        ObjectNode jsonObject = (ObjectNode) getJsonObjectMap(fileContent);

        ObjectNode contentObjectNode = (ObjectNode) jsonObject.get("content");

        ArrayNode arrayNode = (new ObjectMapper()).createArrayNode();
        arrayNode.add(exportID);
        contentObjectNode.set("ids", arrayNode);

        return jsonObject.toString();
    }

    /**
     * Process a JSON string and return it as a Java object.
     *
     * @param jsonStringToParse The JSON string to parse
     * @return A {@link JsonNode} object that can be read. The fields can be modified by casting it to an {@link ObjectNode}.
     * @throws JsonProcessingException If there is an issue while processing the JSON
     */
    public static JsonNode getJsonObjectMap(String jsonStringToParse) throws JsonProcessingException {
        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the response string into a JsonNode object
        return objectMapper.readTree(jsonStringToParse);
    }

    /**
     * Get the contents of a file as a String.
     *
     * @param filePath The path to the file, as a Path object.
     * @return Contents of a file, as a String
     * @throws IOException If there is an error while reading the file
     */
    public static String readFile(Path filePath) throws IOException {
        return Files.readString(filePath);
    }
}