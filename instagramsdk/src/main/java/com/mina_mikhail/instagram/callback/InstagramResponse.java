package com.mina_mikhail.instagram.callback;

import com.mina_mikhail.instagram.data.model.InstagramUser;

public interface InstagramResponse {

  void onInstagramLogInSuccess(InstagramUser user);

  void onInstagramLogInFail(int errorCode, String errorMessage);
}