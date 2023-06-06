package com.example.imgGenBackend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * A class that allows you to issue network requests, make API calls, etc. You can create a {@link #HTTPCaller} object to make repetitive API calls by setting the API URL, the API Key, and other fields like content-type, etc. You can keep calling this object with different content by passing the body of the request to the {@link #newRequest} method.
 *
 * @author Vishal N (vishalnandagopal.com)
 */

public class HTTPCaller {

    /**
     * The URL to call every time when making an API call.
     */
    URL URLToCall;
    /**
     * The method to use - GET or POST. Default is GET
     */
    String requestMethod;
    /**
     * The API key to use with "Authorisation" request property, if any.
     */
    String APIKey;

    /**
     * The header to set for the accepts field
     */
    String acceptsHeader;
    /**
     * The content-type of the API call. Set to "application/json" by default.
     */
    String contentType;

    /**
     * The constructor for the {@link HTTPCaller} class.
     *
     * @param URLString     The URL to issue requests to
     * @param requestMethod The HTTP method to use - GET or POST. GET by default.
     * @param APIKey        The API key to use in the Authorization header, if any. None by default.
     * @param contentType   The content type of the request. "application/json" by default.
     * @throws MalformedURLException If the URL is not of the right format, an error is thrown by the URL object constructor
     */
    public HTTPCaller(String URLString, String requestMethod, String APIKey, String contentType, String acceptsHeader) throws MalformedURLException {

        this.URLToCall = new URL(URLString);
        this.requestMethod = requestMethod; // GET, POST, PUT, etc
        this.APIKey = APIKey; // Key if any
        this.contentType = contentType;
        this.acceptsHeader = acceptsHeader;
    }

    public static InputStream fetch(URL URLToCall, String requestMethod, String requestBody, String APIKey, String contentType, String acceptsHeader) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) URLToCall.openConnection();

        if (requestMethod.length() > 0) {
            // httpConn.setRequestMethod("POST");
            httpConn.setRequestMethod(requestMethod);
        } else {
            // Default is GET. Set to it when passed when an empty string.
            httpConn.setRequestMethod("GET");
        }

        if (contentType.length() > 0) {
            // httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Content-Type", contentType);
        } else {
            httpConn.setRequestProperty("Content-Type", "application/json");
        }

        if (APIKey.length() > 0) {
            // httpConn.setRequestProperty("Authorization", "Bearer " + "xxxxxxx");
            httpConn.setRequestProperty("Authorization", "Bearer " + APIKey);
        }

        if (acceptsHeader.length() > 0) {
            // httpConn.setRequestProperty("Accept", "");
            httpConn.setRequestProperty("Accept", acceptsHeader);
        } else {
            httpConn.setRequestProperty("Accept", "application/json");
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
        return responseCode == 200 ? httpConn.getInputStream() : httpConn.getErrorStream();
    }

    public InputStream fetch(String requestBody, String acceptsHeader) throws IOException {
        return HTTPCaller.fetch(this.URLToCall, this.requestMethod, requestBody, this.APIKey, this.contentType, "application/json");
    }

    public InputStream fetch(String requestBody) throws IOException {
        return HTTPCaller.fetch(this.URLToCall, this.requestMethod, requestBody, this.APIKey, this.contentType, this.acceptsHeader);
    }

    public String newRequest(String requestBody) throws IOException {
        String response;

        InputStream responseStream = this.fetch(requestBody);
        try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
            response = s.hasNext() ? s.next() : "";
        }
        // System.out.println(response);
        return response;
    }
}