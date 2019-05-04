package com.instagramlogin.instagram.data.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.instagramlogin.instagram.utils.Constants.AUTHORIZATION_URL;

/**
 * Created by Esraa Nayel on 5/4/2019.
 */

public class ApiClient {
  private static final String BASE_URL = AUTHORIZATION_URL + "/";

  private static ApiClient instance;

  private ApiInterface apiService;

  private ApiClient() {
    OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(120, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS).addNetworkInterceptor(new StethoInterceptor())
        .build();

    Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(
            GsonConverterFactory.create(new GsonBuilder().setLenient().create())).baseUrl(BASE_URL)
        .build();

    apiService = retrofit.create(ApiInterface.class);
  }

  public static ApiClient getInstance() {
    if (instance == null) {
      instance = new ApiClient();
    }
    return instance;
  }

  public ApiInterface getApiService() {
    return apiService;
  }
}