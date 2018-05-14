package com.example.dennismeisner.gimprove;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.Socket;
import java.net.URI;
import java.util.LinkedList;

import org.json.*;

public class WebsocketClient extends WebSocketClient {

    private LinkedList<SocketListener> socketListeners;

    public WebsocketClient(URI serverUri) {
        super(serverUri);
        socketListeners = new LinkedList<SocketListener>();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("connected");
    }

    @Override
    public void onMessage(String message) {
        try {
            JSONObject jsonMessage = new JSONObject(message);
            for (int i = 0; i < this.socketListeners.size(); i++) {
                socketListeners.get(i).onSocketMessage(jsonMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {
        System.out.println("An error occured" + ex);
    }

    public void addListener(SocketListener newListener) {
        this.socketListeners.add(newListener);
    }

}
