package com.instagramlogin.instagram.data.enums;

/**
 * Created by Esraa Nayel on 5/4/2019.
 */

public class QueryParameter {

  public static class AuthorizationUrlParameters {
    public static final String RESPONSE_TYPE = "response_type";
    public static final String CLIENT_ID = "client_id";
    public static final String REDIRECT_URI = "redirect_uri";
  }

  public static class CodeUrlParameters {
    public static final String CODE = "code";
  }
}