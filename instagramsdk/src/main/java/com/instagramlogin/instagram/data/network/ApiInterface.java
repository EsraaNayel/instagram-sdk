package com.instagramlogin.instagram.data.network;

import com.instagramlogin.instagram.data.network.response.AccessTokenResponse;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.instagramlogin.instagram.utils.Constants.GET_TOKEN_URL;

/**
 * Created by Esraa Nayel on 5/4/2019.
 */

public interface ApiInterface {

  @FormUrlEncoded
  @POST(GET_TOKEN_URL)
  Single<AccessTokenResponse> getAccessToken(
      @Field("client_id") String client_id,
      @Field("client_secret") String client_secret,
      @Field("grant_type") String grant_type,
      @Field("redirect_uri") String redirect_uri,
      @Field("code") String code
  );
}