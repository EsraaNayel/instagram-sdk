package com.example.loginusinginstagram;

import android.app.Activity;
import android.content.Intent;
import com.example.loginusinginstagram.ui.SignInActivity;

/**
 * Created by Esraa Nayel on 4/13/2019.
 */
public class Instagram {

  // SignIn Request Data
  public static final String CLIENT_ID = "clientId";
  public static final String CLIENT_SECRET = "clientSecret";
  public static final String REDIRECT_URL = "redirectURL";

  // SignIn Request Code
  public static final int REQ_CODE_SIGN_IN = 111;

  // SignIn Request State
  public static final int SUCCESS = 222;
  public static final int FAILURE = 333;

  // SignIn Response
  public static final String USER = "instagramUser";
  public static final String ERROR_TYPE = "errorType";

  // Error Types
  public static final int ERROR_NO_INTERNET = 1;
  public static final int ERROR_USER_CANCELLED = 2;
  public static final int ERROR_OTHER = 3;

  public static void signIn(Activity activity, String clientId, String clientSecret,
      String redirectURL) {
    Intent intent = new Intent(activity, SignInActivity.class);
    intent.putExtra(Instagram.CLIENT_ID, clientId);
    intent.putExtra(Instagram.CLIENT_SECRET, clientSecret);
    intent.putExtra(Instagram.REDIRECT_URL, redirectURL);
    activity.startActivityForResult(intent, REQ_CODE_SIGN_IN);
  }
}