package com.example.dennismeisner.gimprove.Utilities;

import com.example.dennismeisner.gimprove.GimproveModels.TrainUnit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebInterface {

    @GET("trainunit_list_rest/")
    Call<List<TrainUnit>> loadTrainUnits(@Header("Authorization") String token);
}
