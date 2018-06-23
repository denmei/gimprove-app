package com.example.dennismeisner.gimprove.Utilities;

import android.content.Context;
import android.media.session.MediaSession;

import com.example.dennismeisner.gimprove.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import com.example.dennismeisner.gimprove.R;
import com.example.dennismeisner.gimprove.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestManager {

    private Context context;
    private TokenManager tokenManager;

    public RequestManager(Context context, TokenManager tokenManager) {
        this.context = context;
        this.tokenManager = tokenManager;
    }

    private responseObject getRequest(String[] requestDataTuples, URL requestURL)
            throws IOException, JSONException {
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setRequestMethod("GET");
        String token = tokenManager.getToken();
        String authString = "Token " + token;
        connection.setRequestProperty ("Authorization", authString);
        Scanner scanner = new Scanner(connection.getInputStream());
        JSONObject resp = new JSONObject(scanner.useDelimiter("\\A").next());
        int responseCode = connection.getResponseCode();
        JSONObject responseContent = new JSONObject(connection.getResponseMessage());
        return new responseObject(responseCode, responseContent);
    }

    private responseObject postRequest(String[][] requestDataTuples, URL requestURL) throws
            IOException, JSONException {
        // make request
        HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream());
        StringBuilder requestDataBuilder = new StringBuilder();
        for(String[] dataTuple:requestDataTuples) {
            requestDataBuilder.append(URLEncoder.encode(dataTuple[0], "UTF-8") + "="
                    + URLEncoder.encode(dataTuple[1], "UTF-8"));
        }
        String requestData = requestDataBuilder.toString();
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
        return new responseObject(responseCode, responseContent);
    }

    /* public responseObject getTrainUnits() {

    }*/

    public class responseObject {

        private int responseCode;
        private JSONObject responseContent;

        public responseObject(int responseCode, JSONObject responseContent) {
            this.responseCode = responseCode;
            this.responseContent = responseContent;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public JSONObject getResponseContent() {
            return responseContent;
        }
    }

}
