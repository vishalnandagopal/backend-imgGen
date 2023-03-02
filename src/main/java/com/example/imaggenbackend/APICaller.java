package com.example.imaggenbackend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.net.URL;

public class APICaller {

    String contentType;
    String requestMethod;
    String APIKey;
    URL APIURL;

    public APICaller(String APIURL, String requestMethod, String APIKey, String contentType)
            throws MalformedURLException {

        this.APIURL = new URL(APIURL);
        this.requestMethod = requestMethod;
        this.APIKey = APIKey;
        this.contentType = contentType;
    }

    public String newAPIRequest(String requestBody) throws IOException {
        // URL url = new URL("https://api.openai.com/v1/images/generations");
        HttpURLConnection httpConn = (HttpURLConnection) this.APIURL.openConnection();

        // requestMethod
        httpConn.setRequestMethod(this.requestMethod);
        // httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Content-Type", this.contentType);
        // httpConn.setRequestProperty("Content-Type", "application/json");

        httpConn.setRequestProperty("Authorization", "Bearer " + this.APIKey);

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write(requestBody);
        // writer.write("{\n \"prompt\": \"a white siamese cat\",\n \"n\": 1,\n
        // \"size\": \"1024x1024\"\n }");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();
        int responseCode = httpConn.getResponseCode();


        InputStream responseStream = responseCode / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        String response = "";
        try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
            response = s.hasNext() ? s.next() : "";
            s.close();
        }
        System.out.println(response);
        return response;
    }

}
