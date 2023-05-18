# ImgGen Backend

## Intro

A Spring application that serves as the backend for the angular frontend. This serves as an customized intermediate
between the angular frontend and OpenAI servers.

## Install the following

1. Maven - [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

## Environmental keys (Important)

-   Put your OpenAI key in a file named `bmc.env` in the same folder as this README. The structure of the `bmc.env` file
    is :-
    ```
    OPENAI_API_KEY=xxxxx
    ```
-   The backend will not be able to communicate with the OpenAI servers without the API key.

## Running the file

This project uses Maven as it's build tool. It was tested using Adoptium Termurin OpenJDK, but should work on any JDK.

Open the [imgGenRestAPI.java](./src/main/java/com/example/imgGenBackend/imgGenRestAPI.java) file in any IDE like or
IntelliJ Ideai or VSCode (with
the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) installed).
It has a `main` function which you can run.

1. In the backend-ImgGen folder, run `mvnw spring-boot:run`

The imgGenRestAPI.java file has the `main` function.

## Files and their functions

Refer to comments in the JavaDoc format written with each file and the important functions they contain.
