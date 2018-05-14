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
        mCircleView = this.initCircle();
    }

    @Override
    public void onBackPressed() {
        client.close();
        finish();
    }

    @Override
    public void onSocketMessage(JSONObject message) {
        try {
            int value = message.getInt("repetitions");
            mCircleView.setValue(value * 10);
            mCircleView.setText(Integer.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private CircleProgressView initCircle() {
        CircleProgressView mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setUnit(null);
        mCircleView.setMaxValue(100);
        mCircleView.setValue(0);
        mCircleView.setUnitVisible(true);
        mCircleView.setTextMode(TextMode.TEXT);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                System.out.println(value);
            }
        });
        return mCircleView;
    }
}
