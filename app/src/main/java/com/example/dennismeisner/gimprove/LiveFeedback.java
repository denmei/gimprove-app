package com.example.dennismeisner.gimprove;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import at.grabner.circleprogress.UnitPosition;


public class LiveFeedback extends Activity implements SocketListener {

    private TextView text;
    private WebsocketClient client;
    CircleProgressView mCircleView;

    @Override
    /*
    Creates view. Initiates websocket-connection to Gimprove-Server.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_feedback);

        // ws://10.0.2.2:8000/ws/tracker/
        // ws://gimprove-test.herokuapp.com/ws/tracker/

        try {
            client = new WebsocketClient(new URI("ws://10.0.2.2:8000/ws/tracker/"));
            client.connect();
            client.addListener(this);
        } catch (Exception e) {
            System.out.println("Exception");
        }

        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setUnit("");
        mCircleView.setMaxValue(10);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                System.out.println(value);
            }
        });
    }

    @Override
    public void onBackPressed() {
        client.close();
        finish();
    }

    @Override
    public void onSocketMessage(JSONObject message) {
        try {
            mCircleView.setValue(message.getInt("repetitions"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
