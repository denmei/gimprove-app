package com.example.dennismeisner.gimprove;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;


public class LiveFeedback extends Activity implements SocketListener {

    private WebsocketClient client;
    CircleProgressView progressCircle;
    private TextView exerciseName;
    private TextView weightText;
    private boolean active;
    private Button connectButton;
    private String serverLink;
    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    /**
     * Creates view. Initiates websocket-connection to Gimprove-Server.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen:
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.live_feedback);

        sharedPreferences = this.getSharedPreferences(
                "com.example.dennismeisner.gimprove.app", Context.MODE_PRIVATE);
        serverLink = getResources().getString(R.string.Websocket);

        active = false;
        exerciseName = (TextView) findViewById(R.id.exerciseName);
        weightText = (TextView) findViewById(R.id.weightText);

        // Initialize Connect-Button
        connectButton = (Button) findViewById(R.id.connectButton);
        this.connectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(client.isClosed()) {
                            try {
                                client = getWebsocket(serverLink);
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
        this.progressCircle.setUnitVisible(false);
        this.progressCircle.setTextMode(TextMode.VALUE);
        this.progressCircle.setSeekModeEnabled(false);
        this.progressCircle.setAutoTextSize(true);
    }

    protected void onStart() {
        super.onStart();
        this.token = sharedPreferences.getString("Token", "");

        // Open websocket
        try {
            client = this.getWebsocket(serverLink);
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
    private void initExercise(int maxValue, int initValue, Double weight, String exerciseName) {
        this.progressCircle.setMaxValue(maxValue);
        this.progressCircle.setValue(initValue);
        this.progressCircle.setUnitVisible(true);
        this.progressCircle.setTextMode(TextMode.TEXT);

        this.active = true;
        this.weightText.setText(Double.toString(weight) + "kg");
        this.exerciseName.setText(exerciseName);
    }

    /* *** Websocket *** */

    private WebsocketClient getWebsocket(String url) throws URISyntaxException {
        Map<String, String> header = new HashMap<String, String>();
        header.put("authorization", "Token " + this.token);
        WebsocketClient client = new WebsocketClient(new URI(url), header);
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

        final JSONObject jsonMessage = message;

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    if(!active) {
                        String name = jsonMessage.getString("exercise_name");
                        Double weight = jsonMessage.getDouble("weight");
                        initExercise(100, 0, weight, name);
                    } else if(jsonMessage.getInt("repetitions") == 1) {
                        weightText.setText(Double.toString(jsonMessage.getDouble("weight")) + "kg");
                    }
                    int value = jsonMessage.getInt("repetitions");
                    progressCircle.setValueAnimated(value * 10);
                    progressCircle.setText(Integer.toString(value));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onSocketClosed(int code, String reason, boolean remote) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                connectButton.setBackgroundColor(getResources().getColor(R.color.grimproveOrange));
            }
        });
    }

    @Override
    public void onSocketOpen() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                connectButton.setBackgroundColor(getResources().getColor(R.color.gimproveGray));
            }
        });
    }
}
