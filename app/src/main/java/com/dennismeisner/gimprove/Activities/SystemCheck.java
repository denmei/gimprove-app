package com.dennismeisner.gimprove.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dennismeisner.gimprove.Utilities.RequestManager;
import com.dennismeisner.gimprove.Utilities.ResponseObject;
import com.dennismeisner.gimprove.Utilities.TokenManager;
import com.example.dennismeisner.gimprove.R;

import org.json.JSONObject;

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
    }

    protected void onStart() {
        super.onStart();

        Boolean hasToken = false;

        String token = sharedPreferences.getString("Token", "");
        tokenChecker = new TokenChecker(token, sharedPreferences, this);

        // check token
        tokenChecker.execute();
    }

    private void forward() {
        // forward to loginActivity
        if(!hasToken) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            Intent loggedInIntent = new Intent(this, LoggedInActivity.class);
            startActivity(loggedInIntent);
        }

    }


    /**
     * Checks whether there is already a valid token in SharedPreferences.
     *
     * @return True, if there is a valid token. False otherwise.
     */
    public class TokenChecker extends AsyncTask<Void, Void, Boolean> {

        private final String token;
        private final SharedPreferences sharedPreferences;
        private TokenManager tokenManager;
        private RequestManager requestManager;

        TokenChecker(String inToken, SharedPreferences sharedPrefs, Context context) {
            token = inToken;
            sharedPreferences = sharedPrefs;
            tokenManager = new TokenManager(sharedPreferences);
            requestManager = new RequestManager(context, tokenManager);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(token.equals("")) {
                return false;
            } else {

                try {
                    // Check whether token is (still) valid by making a http-Request.
                    ResponseObject response = requestManager.getRequest(new
                            URL(getResources().getString(R.string.UserProfile)));
                    if(response.getResponseCode() == 200 || response.getResponseCode() == 201) {
                        // Update sharedPreferences
                        JSONObject resp = response.getResponseContent();
                        sharedPreferences.edit().putInt("userid", resp.getInt("user")).apply();
                        sharedPreferences.edit().putString("rfid_tag", resp.getString("rfid_tag")).apply();
                        sharedPreferences.edit().putString("date_of_birth", resp.getString("date_of_birth")).apply();
                        sharedPreferences.edit().putString("gym", resp.getString("gym")).apply();
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    // TODO: Logging
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
