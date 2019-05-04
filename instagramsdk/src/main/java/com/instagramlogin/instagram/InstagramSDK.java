package com.instagramlogin.instagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import com.instagramlogin.instagram.callback.InstagramResponse;
import com.instagramlogin.instagram.ui.InstagramLogInActivity;

/**
 * Created by Esraa Nayel on 5/4/2019.
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