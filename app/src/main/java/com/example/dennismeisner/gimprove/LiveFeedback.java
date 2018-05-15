package com.example.dennismeisner.gimprove;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import at.grabner.circleprogress.UnitPosition;


public class LiveFeedback extends Activity implements SocketListener {

    private WebsocketClient client;
    CircleProgressView progressCircle;
    private TextView exerciseName;
    private boolean active;
    private Button connectButton;

    @Override
    /**
     * Creates view. Initiates websocket-connection to Gimprove-Server.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_feedback);

        active = false;
        exerciseName = (TextView) findViewById(R.id.exerciseName);

        // Initialize Connect-Button
        connectButton = (Button) findViewById(R.id.connectButton);
        this.connectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(client.isClosed()) {
                            try {
                                client = getWebsocket("ws://10.0.2.2:8000/ws/tracker/");
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Try connect");
                        }
                    }
                }
        );

        // Initialize ProgressCircle
        progressCircle = (CircleProgressView) findViewById(R.id.circleView);
        this.progressCircle.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                System.out.println(value);
            }
        });
        progressCircle.setValue(0);
        this.progressCircle.setUnit(null);
        this.progressCircle.setUnitVisible(true);
        this.progressCircle.setTextMode(TextMode.TEXT);

        // ws://10.0.2.2:8000/ws/tracker/
        // ws://gimprove-test.herokuapp.com/ws/tracker/
        // Open websocket
        try {
            client = this.getWebsocket("ws://10.0.2.2:8000/ws/tracker/");
        } catch (Exception e) {
            System.out.println("Exception");
        }

    }

    /* *** Initializers *** */

    /**
     * Initializer for ProgressCircle.
     * @param maxValue maximum value for cCircle.
     * @param initValue initial value for circle.
     *
     */
    private void initExercise(int maxValue, int initValue, String exerciseName) {
        this.progressCircle.setMaxValue(maxValue);
        this.progressCircle.setValue(initValue);
        this.progressCircle.setUnit(null);
        this.progressCircle.setUnitVisible(true);
        this.progressCircle.setTextMode(TextMode.TEXT);

        this.active = true;

        this.exerciseName.setText(exerciseName);
    }

    /* *** Websocket *** */

    private WebsocketClient getWebsocket(String url) throws URISyntaxException {
        WebsocketClient client = new WebsocketClient(new URI(url));
        client.connect();
        client.addListener(this);
        return client;
    }

    @Override
    public void onBackPressed() {
        client.close();
        finish();
    }

    @Override
    public void onSocketMessage(JSONObject message) {
        try {

            if(!active) {
                String name = message.getString("exercise_name");
                this.initExercise(100, 0, name);
            }

            int value = message.getInt("repetitions");
            progressCircle.setValueAnimated(value * 10);
            progressCircle.setText(Integer.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSocketClosed(int code, String reason, boolean remote) {
        this.connectButton.setBackgroundColor(getResources().getColor(R.color.grimproveOrange));
    }

    @Override
    public void onSocketOpen() {
        this.connectButton.setBackgroundColor(getResources().getColor(R.color.gimproveGray));
    }
}
