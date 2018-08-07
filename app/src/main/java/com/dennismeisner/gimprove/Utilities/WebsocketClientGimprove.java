package com.dennismeisner.gimprove.Utilities;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.LinkedList;
import java.util.Map;

import org.json.*;

public class WebsocketClientGimprove extends WebSocketClient {

    // private static WebsocketClientGimprove instance;
    private TokenManager tokenManager;
    private Map<String, String> header;
    private LinkedList<SocketListener> socketListeners;
    private URI websocketURL;

    /*
    Private constructor for Singleton-pattern
    private WebsocketClientGimprove() throws URISyntaxException {
        super(new URI("ws://gimprove-test.herokuapp.com/ws/tracker/"));

        // create header
        tokenManager = TokenManager.getInstance();
        header = new HashMap<String, String>();
        header.put("authorization", "Token " + this.tokenManager.getToken());

        socketListeners = new LinkedList<SocketListener>();
        connect();
    }

    public static WebsocketClientGimprove getInstance() throws URISyntaxException {
        if(WebsocketClientGimprove.instance == null) {
            WebsocketClientGimprove.instance = new WebsocketClientGimprove();
        }
        return WebsocketClientGimprove.instance;
    }
    */

    public WebsocketClientGimprove(Map<String, String> header, TokenManager tokenManager,
                                   URI serverURL) {
        super(serverURL, header);

        this.tokenManager = tokenManager;

        socketListeners = new LinkedList<SocketListener>();
        connect();

        this.header = header;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("connected");
        System.out.println(handshakedata.getHttpStatus());
        System.out.println(handshakedata.getHttpStatusMessage());
        for (int i = 0; i < this.socketListeners.size(); i++) {
            socketListeners.get(i).onSocketOpen();
        }
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
        System.out.println("Connection closed.");
        for (int i = 0; i < this.socketListeners.size(); i++) {
            socketListeners.get(i).onSocketClosed(code, reason, remote);
        }
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("An error occured" + ex);
    }

    public void addListener(SocketListener newListener) {
        this.socketListeners.add(newListener);
    }

}
