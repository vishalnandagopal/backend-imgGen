package com.example.imgGenBackend;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A container class for working with ChatGPT and Dall-E, and allows you to call both APIs with different parameters, while using the same API key.
 *
 * @author Vishal N (vishalnandagopal.com)
 */
public class AIClients {
    private static final String envFileName = "bmc.env";
    private final DallEClient dallEClient;
    private final ChatGPTClient chatGPTClient;
    private final StabilityAIClient stabilityAIClient;

    /**
     * Constructor for the {@link AIClients OpenAIClient} class. Fetches the API key using method, and sets new {@link DallEClient}, {@link ChatGPTClient} and {@link StabilityAIClient} as private members using the API key.
     *
     * @throws IOException If I/O error occurs
     */
    public AIClients() throws IOException {

        String OPENAI_API_KEY = Miscellaneous.readDotenv(envFileName, "OPENAI_API_KEY");
        String STABILITY_AI_API_KEY = Miscellaneous.readDotenv(envFileName, "STABILITY_AI_API_KEY");
        this.dallEClient = new DallEClient(OPENAI_API_KEY);
        this.chatGPTClient = new ChatGPTClient(OPENAI_API_KEY);
        this.stabilityAIClient = new StabilityAIClient(STABILITY_AI_API_KEY);
    }

    /**
     * Iterates through the responses of either Dall-E or Stability AI, and based on the source, returns an {@link ArrayList ArrayList} of {@link Image} objects.
     * @param extractedResponse The response from either Dall-E or Stability AI
     * @param source "d" for Dall-E. "s" for Stability AI
     * @return An {@link ArrayList} of {@link Image} objects.
     * @throws IOException
     */
    public ArrayList<Image> createImageObjects(ArrayList<String> extractedResponse, String source) throws IOException {
        ArrayList<Image> images = new ArrayList<>();

        if (source.equals("d")) {
            for (String imageURL : extractedResponse) {

                images.add(new Image(new URL(imageURL), "d"));
            }
        } else if (source.equals("s")) {
            for (String base64EncodedString : extractedResponse) {
                images.add(new Image(base64EncodedString, "s"));
            }
        }
        return images;
    }

    /**
     * Generate one or more images with the given prompt by using the Dall-E API..
     *
     * @param prompt     The prompt to be supplied to Dall-E
     * @param noOfImages The number of images that have to be generated - usually 1
     * @param imgSize    The size of the image that will be generated. Valid options are {@code 256x256},{@code 512x512} and {@code 1024x1024}
     * @return An ArrayList of {@code Image} objects that each contains the URL of the image, as well as it's UUID.
     * @throws IOException If I/O error occurs
     */
    public ArrayList<Image> genImage(String prompt, int noOfImages, String imgSize) throws IOException {
        String dallEResponse = dallEClient.callDallEAPI(prompt, noOfImages, imgSize);
        String stabilityAIResponse = stabilityAIClient.callStabilityAIAPI(prompt, noOfImages);
        return new ArrayList<>() {{
            addAll(createImageObjects(JSONPreparer.extractImageURLsFromDallEResponse(dallEResponse), "d"));
            addAll(createImageObjects(JSONPreparer.extractBase64ImageFromStabilityAIResponse(stabilityAIResponse), "s"));
        }};
    }

    /**
     * Generate content using ChatGPT, which can further be used as a description for pages, or text for small paragraphs.
     *
     * @param userPrompt The prompt that the content should be generated using.
     * @return The content generated by ChatGPT
     * @throws IOException If I/O error occurs
     */
    public String generateContent(String userPrompt) throws IOException {
        String systemPromptForDescriptionPrompt = "You have to act as an assistant that helps users write content for websites. Your role is to generate content from a topic given by the user. The content generated should be suitable enough to be put on a website. The user might give context such as the content should be a description, which means it should be a short summary. Unless explicitly mentioned by the user to be longer, the content generated should not exceed 5 sentences, and there should not be any repeated content, such as 2 lines that mean and convey the same thing in the generated content. It has to be unique and creative.";
        return genChat(systemPromptForDescriptionPrompt, userPrompt);
    }


    /**
     * Generate a suitable prompt that can be supplied to Dall-E, by asking ChatGPT to make the user prompt descriptive and to expand further on it.
     *
     * @param userPrompt The prompt given by the user, meant to be used to generate images.
     * @return A clear and descriptive prompt that conveys has a very similar meaning when compared the original user prompt.
     * @throws IOException If I/O error occurs
     */
    public String generateImagePrompt(String userPrompt) throws IOException {
        String systemPromptForImagePrompt = "You have to act as an assistant that helps users generated prompts for an AI image generation system named Dall-E. It can create realistic images and art from a description in natural language. The user will provide a short and simple description of the image they want to generate. Help them generate a suitable prompt that can be fed to Dall-E 2 to generate clear, appealing images without any distortions or blur. You can expand on the prompt in case it would lead to a more descriptive prompt. Generate an clear, well-detailed and descriptive prompt for any prompt provided by the user. Make sure the prompt you generate is in first person speech. Do not include commands or text that asks it to generate an image, since Dall-E assumes it already. You just have to provide a description of the image that the user can prompt Dall-E with. Do not include any additional text while providing your reply such as anything indicating that you are replying with a prompt. The user should be able to use your prompt as it is without any deletions or additions. Do not frame the prompt as a question, such as asking whether such a prompt would be good or bad.";
        return genChat(systemPromptForImagePrompt, userPrompt);
    }

    /**
     * Talk to the ChatGPT API and supply it with the given system and user prompts.
     *
     * @param systemPrompt The prompt that can act as the system/admin prompt when communicating with ChatGPT. This can include it's set of rules and what it must do to each user's prompt.
     * @param userPrompt   The prompt given by the user.
     * @return ChatGPT's reply
     * @throws IOException If I/O error occurs
     */
    public String genChat(String systemPrompt, String userPrompt) throws IOException {

        String[] messageContents = {systemPrompt, userPrompt};
        System.out.println(userPrompt);
        String[] roles = {"system", "user"};
        String response = chatGPTClient.callChatGPTAPI(messageContents, roles);
        return JSONPreparer.extractAnswerFromChatGPTResponse(response);
    }
}

/**
 * A class to talk to the Dall-E API.
 */
class DallEClient {

    /**
     * The URL of the API to be used for communicating with Dall-E.
     */
    final static String IMAGE_GEN_API_URL = "https://api.openai.com/v1/images/generations";
    HTTPCaller httpCaller;

    /**
     * Constructor that creates a new {@link HTTPCaller} with the given API key and URL.
     *
     * @param apiKey The API key used to communicate with the API
     * @throws MalformedURLException If there is an issue with creating the URL object due to URL being malformed.
     */
    public DallEClient(String apiKey) throws MalformedURLException {
        this.httpCaller = new HTTPCaller(IMAGE_GEN_API_URL, "POST", apiKey, "application/json", "");
    }

    /**
     * Build prompt that can be sent to Dall-E
     *
     * @param prompt          The main prompt
     * @param exclude         Stuff to exclude
     * @param include         Stuff to include
     * @param backgroundColor The background color of the image
     * @return A single string that can be supplied as a prompt to Dall-E
     */
    public static String promptBuilderForDallE(String prompt, String exclude, String include, String backgroundColor) {
        String mainPrompt = prompt + ".";
        if (exclude != null) {
            if (exclude.length() > 2) {
                mainPrompt += String.format(" You must not include any of the following in the image: \"%s\"", exclude);
            }
        }
        if (include != null) {
            if (include.length() > 2) {
                mainPrompt += String.format(" The image must contain the following \"%s\"", include);
            }
        }
        if (backgroundColor != null) {
            if (backgroundColor.length() > 2) {
                mainPrompt += " The background color of the image must be " + backgroundColor + ".";
            }
        }
        return mainPrompt;
    }

    /**
     * Call the Dall-E API
     *
     * @param prompt     The prompt
     * @param noOfImages Number of images
     * @param imgSize    The size of the image
     * @return The raw response body
     * @throws IOException If there is an error while calling the API
     */
    public String callDallEAPI(String prompt, int noOfImages, String imgSize)
            throws IOException {
        System.out.println(prompt);
        String requestBody = String.format("{ \"prompt\": \"%s\", \"n\": %d, \"size\": \"%s\" }", prompt,
                noOfImages,
                imgSize);
        String responseBody = httpCaller.newRequest(requestBody);
        System.out.println("Response from Dall-E is: \n" + responseBody);
        return responseBody;
    }
}

/**
 * A class to talk to the Stability AI API
 */
class StabilityAIClient {
    /**
     * The URL of the API to be used for communicating with Stability-AI.
     */
    final static String STABILITY_AI_API_URL = "https://api.stability.ai/v1/generation/stable-diffusion-xl-beta-v2-2-2/text-to-image";
    HTTPCaller httpCaller;

    /**
     * Constructor that creates a new {@link HTTPCaller} with the given API key and URL.
     *
     * @param apiKey The API key used to communicate with the API
     * @throws MalformedURLException If there is an issue with creating the URL object due to URL being malformed.
     */
    StabilityAIClient(String apiKey) throws MalformedURLException {
        this.httpCaller = new HTTPCaller(STABILITY_AI_API_URL, "POST", apiKey, "application/json", "application/json");
    }

    /**
     * Call the Stability-AI API
     *
     * @param prompt     The prompt
     * @param noOfImages Number of images
     * @return The raw response body
     * @throws IOException If there is an error while calling the API
     */
    public String callStabilityAIAPI(String prompt, int noOfImages)
            throws IOException {
        String requestBody = String.format("{ \"text_prompts\": [{\"text\":\"%s\"}], \"cfg_scale\":20, \"samples\": %d}", prompt,
                noOfImages);
        String responseBody = httpCaller.newRequest(requestBody);
        System.out.println("Response from Stability AI is: \n" + responseBody.substring(0, 30));
        return responseBody;
    }

}

/**
 * A class to talk to the ChatGPT API
 */
class ChatGPTClient {

    /**
     * The URL of the API to be used for communicating with Dall-E.
     */
    final static String CHAT_GPT_API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * The model that must be used to generate a reply from ChatGPT.
     */
    final static String CHATGPT_MODEL = "gpt-3.5-turbo";

    HTTPCaller httpCaller;

    /**
     * Constructor that creates a new HTTPCaller with the given API key and URL.
     *
     * @param apiKey The API key used to communicate with the API
     * @throws MalformedURLException If there is an issue with creating the URL object due to URL being malformed.
     */
    ChatGPTClient(String apiKey) throws MalformedURLException {
        this.httpCaller = new HTTPCaller(CHAT_GPT_API_URL, "POST", apiKey, "application/json", "");
    }

    /**
     * Call the ChatGPT API using the private field {@link #httpCaller} by passing the message contents and their respective roles.
     *
     * @param messageContents String[] of the messages that should be passed to ChatGPT
     * @param roles           String[] of roles corresponding to each message in the {@code messageContents}[]
     * @return ChatGPT's response
     * @throws IOException If I/O error occurs
     */
    public String callChatGPTAPI(String[] messageContents, String[] roles)
            throws IOException {
        final ArrayList<String> validRoles = new ArrayList<>() {
            {
                add("system");
                add("assistant");
                add("user");
            }
        };

        // assert messageContents.length == roles.length;
        if (messageContents.length != roles.length) {
            throw new IllegalArgumentException(String.format("messageContents and roles arrays must be of same length. You have supplied messageContents of length %d and roles of length %d", messageContents.length, roles.length));
        }
        String messages = "";
        for (int i = 0; i < messageContents.length; i++) {
            if (!validRoles.contains(roles[i])) {
                roles[i] = "user";
            }
            if (messages.length() > 0) {
                messages = String.format("%s,{\"role\": \"%s\",\"content\": \"%s\"}", messages, roles[i], messageContents[i]);
            } else {
                messages = String.format("{\"role\": \"%s\",\"content\": \"%s\"}", roles[i], messageContents[i]);
            }
        }

        String requestBody = String.format("{ \"model\": \"%s\", \"messages\": [%s]}", CHATGPT_MODEL, messages);

        String responseBody = httpCaller.newRequest(requestBody);
        System.out.println("Response from ChatGPT is: \n" + responseBody);
        return responseBody;
    }
}