package com.example.dennismeisner.gimprove;

import org.json.JSONObject;

public interface SocketListener {

    public void onSocketMessage(JSONObject message);

}
