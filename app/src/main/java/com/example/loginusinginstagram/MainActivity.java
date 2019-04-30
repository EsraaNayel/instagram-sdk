package com.example.loginusinginstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.loginusinginstagram.data.model.InstagramUser;

public class MainActivity extends AppCompatActivity {
  String clientId = "9e1800a37cca47708ecfe0fc3106bb05";
  String clientSecret = "f02d4c1509b64b3e8bffe3957a8227ed";
  String redirectURL = "http://appsinnovate.com/";

  ImageView ivInstagram;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ivInstagram = findViewById(R.id.iv_instagram_sign_in);


    findViewById(R.id.iv_instagram_sign_in).setOnClickListener(v -> {
      Instagram.signIn(MainActivity.this, clientId, clientSecret, redirectURL);
    });
    ivInstagram.setOnClickListener(
        v -> Instagram.signIn(MainActivity.this, clientId, clientSecret, redirectURL));
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == Instagram.REQ_CODE_SIGN_IN) {
      if (resultCode == Instagram.SUCCESS) {
        InstagramUser instagramUser = data.getParcelableExtra(Instagram.USER);

        Toast.makeText(MainActivity.this, instagramUser.getUser().getFull_name(), Toast.LENGTH_SHORT).show();

      } else if (resultCode == Instagram.FAILURE) {

        int errorType = data.getIntExtra(Instagram.ERROR_TYPE, 0);
        switch (errorType) {
          case Instagram.ERROR_NO_INTERNET:
            Toast.makeText(MainActivity.this, "Please check your internet connection",
                Toast.LENGTH_SHORT).show();
            break;
          case Instagram.ERROR_USER_CANCELLED:
            Toast.makeText(MainActivity.this, "User cancelled authorization", Toast.LENGTH_SHORT)
                .show();
            break;
          case Instagram.ERROR_OTHER:
            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            break;
        }
      }
    }
  }
}
