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

    /**
     * Sends a get request to the specified address and returns the response. Token will be added to
     * header!
     * @param requestURL Address for the http-request.
     * @return ResponseObject with the StatusCode and the Content of the answer.
     * @throws IOException
     * @throws JSONException
     * @throws AuthorizationException In case the server answers with a 400 or 401-message.
     */
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
        if (responseCode == 401 || responseCode == 400) {
             throw new AuthorizationException(connection.getInputStream().toString());
        }
        return new ResponseObject(responseCode, responseContent);
    }

    /**
     * Sends a post request to the specified address and returns the response. Token will be added
     * to the request's header.
     * @param requestDataTuples Dictionary of post-parameters.
     * @param requestURL Address for the http-request.
     * @return ResponseObject with the StatusCode and the Content of the answer.
     * @throws IOException
     * @throws JSONException
     */
    public ResponseObject postRequestWithToken(Map<String, String> requestDataTuples, URL requestURL)
            throws IOException, JSONException {

        String token = tokenManager.getToken();

        // make request
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty ("Authorization", "Token " + token);
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
     * Sends a post request to the specified address and returns the response. No Token added to the
     * header of the request.
     * @param requestDataTuples Dictionary of post-parameters.
     * @param requestURL Address for the http-request.
     * @return ResponseObject with the StatusCode and the Content of the answer.
     * @throws IOException
     * @throws JSONException
     */
    public ResponseObject postRequestNoToken(Map<String, String> requestDataTuples, URL requestURL)
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
