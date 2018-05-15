package com.example.dennismeisner.gimprove;

import org.json.JSONObject;

public interface SocketListener {

    public void onSocketMessage(JSONObject message);

    public void onSocketClosed(int code, String reason, boolean remote);

    public void onSocketOpen();

}
