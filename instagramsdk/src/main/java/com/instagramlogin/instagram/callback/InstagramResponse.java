package com.instagramlogin.instagram.callback;

import com.instagramlogin.instagram.data.model.InstagramUser;

public interface InstagramResponse {

  void onInstagramLogInSuccess(InstagramUser user);

  void onInstagramLogInFail(int errorCode, String errorMessage);
}