package com.example.loginusinginstagram.data.network;

import com.example.loginusinginstagram.data.network.response.AccessTokenResponse;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Esraa Nayel on 4/13/2019.
 */
public interface ApiInterface {

  @FormUrlEncoded
  @POST("https://api.instagram.com/oauth/access_token/")
  Single<AccessTokenResponse> getAccessToken(
      @Field("client_id") String client_id,
      @Field("client_secret") String client_secret,
      @Field("grant_type") String grant_type,
      @Field("redirect_uri") String redirect_uri,
      @Field("code") String code
  );
}