# ImagGen Backend

## Intro

A Spring application that serves as the backend for the angular frontend. This serves as an intermediate between the angular frontend and OpenAI servers.

## Install the following

1. Maven - [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

## Environmental keys (Important)

-   Put your OpenAI key in a file named `bmc.env` in the same folder as this README (or in the parent folder). The structure of the environemntal file is :-

    ```
    OPENAI_API_KEY=xxxxx
    ```

-   The project will not run without your OpenAI key.

## Running the file

This project uses Maven as it's build tool. It was tested using Adoptium Termurin OpenJDK, but should work on any JDK.

Open the [imgGenRestAPI.java](./src/main/java/com/example/imaggenbackend/imgGenRestAPI.java) file in any IDE like or IntelliJ Ideai or VSCode (with the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) installed). It has a `main` function which you can run.

1. In the backend-ImagGen folder, run `mvnw spring-boot:run`

The imgGenRestAPI.java file has the `main` function.

## Files and their functions

1. [`DallEClient`](./src/main/java/com/example/imaggenbackend/DallEClient.java)

-   The class serving as the client between the Rest API and the OpenAI servers. It contains the following classes -
    1. `ImageGenerator` - Used for generating images (DallE API) (Status - Completed)
    2. `ImageEditor` - Used for editing images (DallE API). (Status - in progress)
    3. `ChatGPT` - Used for talking to the ChatGPT API. (Status - in progress)

2. [`HTTPCaller`](./src/main/java/com/example/imaggenbackend/HTTPCaller.java)

-   The raw HTTP caller. You can pass a URL, request method (`GET` by default), content type, API Key, etc and initialise and object.

3. [`imgGenRestAPI`](./src/main/java/com/example/imaggenbackend/imgGenRestAPI.java)

-   The spring boot application that serves as the RestAPI. Currently has the following methods

    1. `/` - GET/POST - Used to see if application is working.
    2. `/images` - POST - Used to generate images

        Parameters 1. "prompt" - The prompt to be used for generating the image 2. "no" - No of images 3. "size" - Size of image ("1024x1024", etc). Can only send the sizes supported by OpenAI.
