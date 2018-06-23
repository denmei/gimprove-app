package com.example.dennismeisner.gimprove.Utilities;

import org.json.JSONObject;

public class ResponseObject {

    private int responseCode;
    private JSONObject responseContent;

    public ResponseObject(int responseCode, JSONObject responseContent) {
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
