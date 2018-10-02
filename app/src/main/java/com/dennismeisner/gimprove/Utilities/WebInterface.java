package com.dennismeisner.gimprove.Utilities;

import com.dennismeisner.gimprove.Models.GimproveModels.ExerciseUnit;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebInterface {

    @GET("api_v0_tracker/trainunit_list_rest/")
    Call<ResponseBody> loadTrainUnits(@Header("Authorization") String token);

    @GET("api_v0_tracker/exerciseunit_list_rest/")
    Call<List<ExerciseUnit>> loadExerciseUnits(@Header("Authorization") String token);

    @GET("api_v0_tracker/set_list_rest/")
    Call<ResponseBody> loadSets(@Header("Authorization") String token);

    @GET("api_v0_tracker/set_detail_rest/{setId}")
    Call<ResponseBody> loadSetDetails(@Path("setId") String setId,
                             @Header("Authorization") String token);

    @PUT("api_v0_tracker/set_detail_rest/{setId}")
    Call<ResponseBody> updateSet(@Path("setId") String setId,
                                 @Header("Authorization") String token,
                                 @Body HashMap<String, Object> body);

    @GET("api_v0_main/userprofile_detail_rest/")
    Call<ResponseBody> getUserProfile(@Header("Authorization") String token);
}
