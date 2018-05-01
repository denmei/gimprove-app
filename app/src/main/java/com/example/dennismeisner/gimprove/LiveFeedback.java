package com.example.dennismeisner.gimprove;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import at.grabner.circleprogress.UnitPosition;


public class LiveFeedback extends Activity {

    private TextView text;
    CircleProgressView mCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_feedback);

        mCircleView = (CircleProgressView) findViewById(R.id.circleView);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                System.out.println(value);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
