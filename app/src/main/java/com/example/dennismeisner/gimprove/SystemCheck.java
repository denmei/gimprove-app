package com.example.dennismeisner.gimprove;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Checks configuration when app is started.
 * If there is no valid token yet, the LoginActivity is opened.
 * Otherwise the app-content is opened.
 */
public class SystemCheck extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_check);

        sharedPreferences = this.getSharedPreferences(
                "com.example.dennismeisner.gimprove.app", Context.MODE_PRIVATE);

        // check token
        Boolean hasToken = this.hasValidToken();

        // forward to loginActivity
        if(!hasToken) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            Intent feedbackIntent = new Intent(this, LiveFeedback.class);
            startActivity(feedbackIntent);
        }

    }

    /**
     * Checks whether there is already a valid token in SharedPreferences.
     *
     * @return True, if there is a valid token. False otherwise.
     */
    private boolean hasValidToken() {
        String token = sharedPreferences.getString("Token", "");
        if(token.equals("")) {
            return false;
        } else if(true) {
            return true;
        } else {
            return true;
        }
    }
}
