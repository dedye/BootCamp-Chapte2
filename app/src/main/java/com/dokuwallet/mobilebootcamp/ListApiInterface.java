package com.dokuwallet.mobilebootcamp;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ListApiInterface {

    @FormUrlEncoded
    @POST("/DWMobileAPI/COPAPI.php")
    Call<JsonElement> Login(@Field("requestType") String requestType, @Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/DWMobileAPI/COPAPI.php")
    Call<HomeModel> ListData(@Field("requestType") String requestType);

}