package com.example.dennismeisner.gimprove.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.session.MediaSession;

import com.example.dennismeisner.gimprove.GimproveModels.ExerciseUnit;
import com.example.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.GimproveModels.TrainUnit;
import com.example.dennismeisner.gimprove.GimproveModels.User;
import com.example.dennismeisner.gimprove.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserRepository {

    private String token;
    private User user;
    private Context context;
    private WebInterface webInterface;
    private SharedPreferences preferences;

    public UserRepository(String token, Context context, SharedPreferences preferences, String baseUrl) {
        this.user = User.getInstance();
        this.context = context;
        this.preferences = preferences;
        this.setTokenString(token);
        Retrofit adapter = new Retrofit.Builder()
                // "https://gimprove-test.herokuapp.com/tracker/"
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webInterface = adapter.create(WebInterface.class);
    }

    public UserRepository(String token, Context context, String URL) {
        this.user = User.getInstance();
        this.context = context;
        this.setTokenString(token);
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webInterface = adapter.create(WebInterface.class);
    }

    public void setTokenString(String token) {
        this.token = "Token " + token;
    }

    public void updateTrainUnits() throws IOException, JSONException {
        webInterface.loadTrainUnits(this.token).enqueue(new Callback<List<TrainUnit>>() {
            @Override
            public void onResponse(Call<List<TrainUnit>> call, Response<List<TrainUnit>> response) {
                User.getInstance().setTrainUnits(response.body());
            }

            @Override
            public void onFailure(Call<List<TrainUnit>> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(call.request().url());
            }
        });
    }

    public void sendUpdateSet(final Set newSet) {

        try {
            webInterface.updateSet(newSet.getId(), this.token, newSet.getJsonString()).enqueue(
                    new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            System.out.println(t.getMessage());
                            System.out.println(call.request().url());
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateUserData(final String name, final int id) {
        user = User.getInstance();
        webInterface.getUserProfile(this.token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.body() != null) {
                                //
                                String body = response.body().string();
                                System.out.println(body);
                                JSONObject jsonResponse = new JSONObject(body);
                                String rfid = (String) jsonResponse.get("rfid_tag");
                                user.setUserAttributes(name, id, rfid);
                                preferences.edit().putString("rfid_tag", rfid).apply();
                                //System.out.println(jsonResponse.toString());
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            this.onFailure(call, e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println(t.getMessage());
                        System.out.println(call.request().url());
                    }
                });
    }

    public void updateExerciseUnits() throws IOException, JSONException {
        webInterface.loadExerciseUnits(this.token).enqueue(new Callback<List<ExerciseUnit>>() {
            @Override
            public void onResponse(Call<List<ExerciseUnit>> call, Response<List<ExerciseUnit>> response) {
                User.getInstance().setExerciseUnits(response.body());
            }

            @Override
            public void onFailure(Call<List<ExerciseUnit>> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(call.request().url());
            }
        });
    }

    public void updateSets() throws IOException, JSONException {
        webInterface.loadSets(this.token).enqueue(new Callback<List<Set>>() {
            @Override
            public void onResponse(Call<List<Set>> call, Response<List<Set>> response) {
                User.getInstance().setSets(response.body());
            }

            @Override
            public void onFailure(Call<List<Set>> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(call.request().url());
            }
        });
    }

    public void updateUser(String name, int id) throws IOException, JSONException {
        this.updateTrainUnits();
        this.updateExerciseUnits();
        this.updateSets();
        this.updateUserData(name, id);
    }
}
