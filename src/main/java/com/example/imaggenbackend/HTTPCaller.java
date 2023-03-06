package com.example.imaggenbackend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HTTPCaller {

    URL URLToCall;
    String requestMethod;
    String APIKey = "";
    String contentType;

    public HTTPCaller(String URLToCall, String requestMethod, String APIKey, String contentType)
            throws MalformedURLException {

        this.URLToCall = new URL(URLToCall);
        this.requestMethod = requestMethod; // GET, POST, PUT, etc
        this.APIKey = APIKey; // Key if any
        this.contentType = contentType;
    }

    public static InputStream fetch(URL URLToCall, String requestMethod, String requestBody, String APIKey,
            String contentType) throws IOException {

        HttpURLConnection httpConn = (HttpURLConnection) URLToCall.openConnection();

        // httpConn.setRequestMethod("POST");
        if (requestMethod.length() > 0) {
            httpConn.setRequestMethod(requestMethod);
        } else {
            httpConn.setRequestMethod("GET");
        }

        if (contentType.length() > 0) {
            // httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Content-Type", contentType);
        }

        if (APIKey.length() > 0) {
            // httpConn.setRequestProperty("Authorization", "Bearer " + "xxxxx");
            httpConn.setRequestProperty("Authorization", "Bearer " + APIKey);
        }

        if (requestBody.length() > 0) {
            httpConn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(requestBody);
            writer.flush();
            writer.close();
            httpConn.getOutputStream().close();
        }

        int responseCode = httpConn.getResponseCode();
        InputStream responseStream = responseCode == 200 ? httpConn.getInputStream() : httpConn.getErrorStream();
        return responseStream;
    }

    public InputStream fetch(String requestBody) throws IOException {
        return HTTPCaller.fetch(this.URLToCall, this.requestMethod, requestBody, this.APIKey, this.contentType);
    }

    public String newRequest(String requestBody) throws IOException {
        String response;

        InputStream responseStream = this.fetch(requestBody);
        try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
            response = s.hasNext() ? s.next() : "";
            s.close();
        }
        System.out.println(response);
        return response;
    }

}