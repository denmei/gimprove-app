package com.example.dennismeisner.gimprove.Utilities;

import com.example.dennismeisner.gimprove.GimproveModels.ExerciseUnit;
import com.example.dennismeisner.gimprove.GimproveModels.Set;
import com.example.dennismeisner.gimprove.GimproveModels.TrainUnit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebInterface {

    @GET("trainunit_list_rest/")
    Call<List<TrainUnit>> loadTrainUnits(@Header("Authorization") String token);

    @GET("exerciseunit_list_rest/")
    Call<List<ExerciseUnit>> loadExerciseUnits(@Header("Authorization") String token);

    @GET("set_list_rest/")
    Call<List<Set>> loadSets(@Header("Authorization") String token);

    @POST("set_detail_rest/{setId}")
    // TODO: Pass parameters
    Call<List<Set>> updateSet(@Path("setId") String setId,
                              @Header("Authorization") String token,
                              @Body Set set);
}
