package com.example.dennismeisner.gimprove.Utilities;

import android.content.Context;
import android.media.session.MediaSession;

import com.example.dennismeisner.gimprove.GimproveModels.ExerciseUnit;
import com.example.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.GimproveModels.TrainUnit;
import com.example.dennismeisner.gimprove.GimproveModels.User;
import com.example.dennismeisner.gimprove.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserRepository {

    private RequestManager requestManager;
    private String token;
    private User user;
    private Context context;
    private WebInterface webInterface;

    public UserRepository(RequestManager requestManager, String token, Context context) {
        this.requestManager = requestManager;
        this.user = User.getInstance();
        this.context = context;
        this.setTokenString(token);
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://gimprove-test.herokuapp.com/tracker/")
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

    public void sendUpdateSet(Set newSet) {
        System.out.println("Send update after changes: ");
        System.out.println(newSet.toString());

        webInterface.updateSet(newSet.getId(), this.token, newSet);
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

    public void updateUser() throws IOException, JSONException {
        this.updateTrainUnits();
        this.updateExerciseUnits();
        this.updateSets();
    }
}
