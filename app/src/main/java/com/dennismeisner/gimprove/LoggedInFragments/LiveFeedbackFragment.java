package com.dennismeisner.gimprove.LoggedInFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dennismeisner.gimprove.Utilities.SocketListener;
import com.dennismeisner.gimprove.Utilities.TokenManager;
import com.dennismeisner.gimprove.Utilities.WebsocketClientGimprove;
import com.dennismeisner.gimprove.Activities.LoggedInActivity;
import com.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;


public class LiveFeedbackFragment extends Fragment implements SocketListener {

    OnSetFinishedListener mCallback;
    private WebsocketClientGimprove client;
    private CircleProgressView progressCircle;
    private TextView exerciseName;
    private TextView weightText;
    private boolean active;
    private Button connectButton;
    private int lastRep;
    private TokenManager tokenManager;
    private Map<String, String> header;
    private SharedPreferences sharedPreferences;

    public LiveFeedbackFragment() {
        // Required empty public constructor
    }

    public static LiveFeedbackFragment newInstance() {
        LiveFeedbackFragment fragment = new LiveFeedbackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_live_feedback, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(
                "com.example.dennismeisner.gimprove.app", Context.MODE_PRIVATE);
        tokenManager = new TokenManager(sharedPreferences);
        header = new HashMap<String, String>();
        header.put("authorization", "Token " + this.tokenManager.getToken());
        this.createNewWebsocketClient(header);

        active = false;
        lastRep = 0;
        exerciseName = (TextView) view.findViewById(R.id.exerciseName);
        weightText = (TextView) view.findViewById(R.id.weightText);

        // Initialize Connect-Button
        connectButton = (Button) view.findViewById(R.id.connectButton);
        this.connectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(client.isClosed()) {
                            createNewWebsocketClient(header);
                        }
                    }
                }
        );

        // Initialize ProgressCircle
        this.progressCircle = (CircleProgressView) view.findViewById(R.id.circleView);
        this.progressCircle.setValue(0);
        this.progressCircle.setUnit(null);
        this.progressCircle.setUnitVisible(false);
        this.progressCircle.setTextMode(TextMode.VALUE);
        this.progressCircle.setSeekModeEnabled(false);
        this.progressCircle.setAutoTextSize(true);
    }

    private void createNewWebsocketClient(Map<String, String> header) {
        try {
            client = new WebsocketClientGimprove(
                    header,
                    tokenManager,
                    new URI(getResources().getString(R.string.Websocket)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.addListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnSetFinishedListener) {
            mCallback = (OnSetFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSetFinishedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(getActivity() instanceof LoggedInActivity) {
            ((LoggedInActivity) getActivity()).setActionBarTitle(getResources()
                    .getString(R.string.actionbar_live_tracking));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

        this.lastRep = 0;
        this.active = true;
        this.weightText.setText(Double.toString(weight) + "kg");
        this.exerciseName.setText(exerciseName);
    }

    /**
     *
     */
    private void resetProgress() {
        progressCircle.setValue(0);
        progressCircle.setText("0");
        progressCircle.setUnitVisible(true);
        progressCircle.setTextMode(TextMode.TEXT);
        active = false;
        lastRep = 0;
        weightText.setText("");
        exerciseName.setText("");
        System.out.println("RESET");
    }

    public void onBackPressed() {
        client.close();
        getActivity().finish();
    }

    @Override
    public void onSocketMessage(JSONObject message) {

        final JSONObject jsonMessage = message;

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    Boolean end = jsonMessage.getBoolean("end");
                    if(end) {
                        // set finished
                        resetProgress();
                        String name = jsonMessage.getString("exercise_name");
                        System.out.println(jsonMessage.toString());
                        //TODO replace by real exercise unit
                        jsonMessage.put("exercise_unit", "");
                        Set newSet = new Set(jsonMessage);
                        mCallback.onSetFinished(name, newSet);
                    } else {
                        if(!active) {
                            String name = jsonMessage.getString("exercise_name");
                            Double weight = jsonMessage.getDouble("weight");
                            initExercise(100, 0, weight, name);
                        } else if(jsonMessage.getInt("repetitions") == 1) {
                            weightText.setText(Double.toString(jsonMessage.getDouble("weight")) + "kg");
                        }
                        int value = jsonMessage.getInt("repetitions");
                        if (value > lastRep) {
                            lastRep = value;
                            progressCircle.setValueAnimated(value * 10);
                            progressCircle.setText(Integer.toString(value));

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onSocketClosed(int code, String reason, boolean remote) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                connectButton.setBackgroundColor(getResources().getColor(R.color.grimproveOrange));
            }
        });
    }

    @Override
    public void onSocketOpen() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                connectButton.setBackgroundColor(getResources().getColor(R.color.gimproveGray));
            }
        });
    }

    // Container-Activity must implement this interface.
    public interface OnSetFinishedListener {
        public void onSetFinished(String exerciseName, Set newSet);
    }
}
