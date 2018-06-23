package com.example.dennismeisner.gimprove.Utilities;

import android.content.SharedPreferences;

public class TokenManager {

    // private static TokenManager instance;
    private String token;
    private SharedPreferences sharedPreferences;

    public TokenManager(SharedPreferences sharedPreferences) {
        sharedPreferences = sharedPreferences;
        token = sharedPreferences.getString("Token", "");
    }

    /*
    private TokenManager() {
        sharedPreferences = .getSharedPreferences(
                "com.example.dennismeisner.gimprove.app", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("Token", "");
    }

    public static TokenManager getInstance() {
        if(TokenManager.instance == null) {
            TokenManager.instance = new TokenManager();
        }
        return TokenManager.instance;
    }*/

    public String getToken() {
        return this.token;
    }
}
