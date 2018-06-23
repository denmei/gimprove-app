package com.example.dennismeisner.gimprove.Utilities;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper class that provides possibility to make HTTP-Requests.
 */
public class RequestManager {

    private Context context;
    private TokenManager tokenManager;

    public RequestManager(Context context, TokenManager tokenManager) {
        this.context = context;
        this.tokenManager = tokenManager;
    }

    public ResponseObject getRequest(URL requestURL)
            throws IOException, JSONException, AuthorizationException {
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setRequestMethod("GET");
        String token = tokenManager.getToken();
        String authString = "Token " + token;
        connection.setRequestProperty ("Authorization", authString);
        Scanner scanner = new Scanner(connection.getInputStream());
        JSONObject responseContent = new JSONObject(scanner.useDelimiter("\\A").next());
        int responseCode = connection.getResponseCode();
        if (responseCode == 401) {
             throw new AuthorizationException(connection.getInputStream().toString());
        }
        return new ResponseObject(responseCode, responseContent);
    }

    public ResponseObject postRequest(Map<String, String> requestDataTuples, URL requestURL)
            throws IOException, JSONException {

        // make request
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());

        // build request data
        StringBuilder requestDataBuilder = new StringBuilder();
        Iterator mapIterator = requestDataTuples.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Map.Entry data = (Map.Entry) mapIterator.next();
            requestDataBuilder.append(URLEncoder.encode(data.getKey().toString(), "UTF-8") + "="
                    + URLEncoder.encode(data.getValue().toString(), "UTF-8"));
            if (mapIterator.hasNext()) {
                requestDataBuilder.append("&");
            }
        }
        String requestData = requestDataBuilder.toString();

        // send request
        output.write(requestData);
        output.flush();
        output.close();

        // parse answer
        int responseCode = connection.getResponseCode();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        String response = "";
        StringBuilder sb = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            response = sb.append(response).append(line).toString();
        }
        rd.close();
        JSONObject responseContent = new JSONObject(response);
        return new ResponseObject(responseCode, responseContent);
    }

    /**
     * Exception that should be thrown if there occured an authorization error.
     */
    public class AuthorizationException extends RuntimeException {
        public AuthorizationException() {}

        public AuthorizationException(String message) {
            super(message);
        }
    }

}
