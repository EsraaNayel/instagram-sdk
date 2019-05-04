package com.mina_mikhail.instagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import com.mina_mikhail.instagram.callback.InstagramResponse;
import com.mina_mikhail.instagram.ui.InstagramLogInActivity;

/*
 * *
 *  * Created by Mina Mikhail on 05/05/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
 */

public class InstagramSDK {

  @SuppressLint("StaticFieldLeak")
  private static volatile InstagramSDK INSTANCE;

  private Context context;
  private String clientID;
  private String clientSecret;
  private String redirectURL;
  private InstagramResponse response;

  public static synchronized InstagramSDK getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new InstagramSDK();
    }
    return INSTANCE;
  }

  public void logIn(Context context, String clientID, String clientSecret, String redirectURL,
      @NonNull InstagramResponse response) {
    this.context = context;
    this.clientID = clientID;
    this.clientSecret = clientSecret;
    this.redirectURL = redirectURL;
    this.response = response;

    initInstagram();
  }

  private void initInstagram() {
    InstagramLogInActivity.open(context);
  }

  public InstagramResponse getResponse() {
    return response;
  }

  public String getClientID() {
    return clientID;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public String getRedirectURL() {
    return redirectURL;
  }
}