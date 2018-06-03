package com.example.dennismeisner.gimprove;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Checks configuration when app is started.
 * If there is no valid token yet, the LoginActivity is opened.
 * Otherwise the app-content is opened.
 */
public class SystemCheck extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private TokenChecker tokenChecker;
    protected Boolean hasToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_check);

        sharedPreferences = this.getSharedPreferences(
                "com.example.dennismeisner.gimprove.app", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("Token", "").apply();
    }

    protected void onStart() {
        super.onStart();

        Boolean hasToken = false;

        String token = sharedPreferences.getString("Token", "");
        tokenChecker = new TokenChecker(token);

        // check token

        System.out.println("START: has token = " + hasToken.toString());
        tokenChecker.execute();
    }

    private void forward() {
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
    public class TokenChecker extends AsyncTask<Void, Void, Boolean> {

        private final String token;

        TokenChecker(String inToken) {
            token = inToken;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            System.out.println("Check token: " + token);
            if(token.equals("")) {
                return false;
            } else {

                try {
                    // Check whether token is still valid by making a http-Request.
                    HttpURLConnection connection = (HttpURLConnection) new
                            URL(getResources().getString(R.string.UserProfile)).openConnection();
                    String authString = "Token " + token;
                    connection.setRequestProperty ("Authorization", authString);
                    int responseCode = connection.getResponseCode();
                    if(responseCode == 200 || responseCode == 201) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                    return false;
                }
            }
        }

        protected void onPostExecute(final Boolean success) {
            tokenChecker = null;

            // forward to loginActivity
            if(!success) {
                hasToken = false;
            } else {
                hasToken = true;
            }
            forward();
        }
    }
}
