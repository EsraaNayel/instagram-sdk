package com.example.loginusinginstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.instagramlogin.instagram.InstagramSDK;
import com.instagramlogin.instagram.callback.InstagramResponse;
import com.instagramlogin.instagram.data.model.InstagramUser;
import com.squareup.picasso.Picasso;

/*
 * *
 *  * Created by Mina Mikhail on 04/05/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
 */

public class MainActivity
    extends AppCompatActivity
    implements InstagramResponse {

  private String clientId = "9e1800a37cca47708ecfe0fc3106bb05";
  private String clientSecret = "f02d4c1509b64b3e8bffe3957a8227ed";
  private String redirectURL = "http://appsinnovate.com/";

  private View userData;
  private ImageView userPhoto;
  private TextView userID;
  private TextView userName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    userData = findViewById(R.id.ll_user_data);
    userPhoto = findViewById(R.id.iv_user_photo);
    userID = findViewById(R.id.tv_user_id);
    userName = findViewById(R.id.tv_user_name);

    findViewById(R.id.btn_log_in).setOnClickListener(
        v -> InstagramSDK.getInstance()
            .logIn(MainActivity.this, clientId, clientSecret, redirectURL, this));
  }

  @Override
  public void onInstagramLogInSuccess(InstagramUser user) {
    Picasso.get().load(user.getProfile_picture()).into(userPhoto);
    userID.setText(user.getId());
    userName.setText(user.getFull_name());

    userData.setVisibility(View.VISIBLE);
  }

  @Override
  public void onInstagramLogInFail(int errorCode, String errorMessage) {
    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
  }
}