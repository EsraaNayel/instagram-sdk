package com.mina_mikhail.instagram.data.network;

import com.mina_mikhail.instagram.data.network.response.AccessTokenResponse;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.mina_mikhail.instagram.utils.Constants.GET_TOKEN_URL;

/*
 * *
 *  * Created by Mina Mikhail on 05/05/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
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