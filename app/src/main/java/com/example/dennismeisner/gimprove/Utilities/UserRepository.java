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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
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
        webInterface.loadTrainUnits(this.token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = null;
                try {
                    body = response.body().string();
                    System.out.println(body);
                    JSONArray jsonSets = new JSONArray(body);
                    List<TrainUnit> downloadedUnits = new LinkedList<TrainUnit>();
                    for(int i = 0; i < jsonSets.length(); i++) {
                        JSONObject msg = (JSONObject) jsonSets.get(i);
                        TrainUnit newUnit = new TrainUnit(msg);
                        System.out.println(newUnit.toExtString());
                        User user = User.getInstance();
                        if (user.getTrainUnitById(newUnit.getId()) == null) {
                            user.addTrainUnit(newUnit);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(call.request().url());
            }
        });
    }

    public void sendUpdateSet(final Set newSet) {
        System.out.println("New set sendupdateset: " + newSet.toExtString());
        webInterface.updateSet(newSet.getId(), this.token, newSet.getHashMap()).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("Sendupdate Failure");
                        System.out.println(t.getMessage());
                        System.out.println(call.request().url());
                    }
                });
    }

    /**
     *
     * @param name Name of the user
     * @param id User id
     */
    public void updateUserData(final String name, final int id) {
        user = User.getInstance();
        webInterface.getUserProfile(this.token)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println("UPDAEUSERDATA");
                        try {
                            if(response.body() != null) {
                                String body = response.body().string();
                                System.out.println(body);
                                JSONObject jsonResponse = new JSONObject(body);
                                String rfid = (String) jsonResponse.get("rfid_tag");
                                System.out.println("RFID: " + rfid);
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
        webInterface.loadSets(this.token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    JSONArray jsonSets = new JSONArray(body);
                    LinkedList<Set> downloadedSets = new LinkedList<Set>();
                    for(int i = 0; i < jsonSets.length(); i++) {
                        JSONObject msg = (JSONObject) jsonSets.get(i);
                        Set newSet = new Set(msg);
                        User user = User.getInstance();
                        if (user.getSetById(newSet.getId()) == null) {
                           user.addSet(newSet);
                        }
                    }
                    // User.getInstance().setSets(downloadedSets);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // User.getInstance().setSets(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("SETS UPDATE failed");
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
