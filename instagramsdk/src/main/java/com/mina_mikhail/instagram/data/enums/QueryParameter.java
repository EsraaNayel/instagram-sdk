package com.mina_mikhail.instagram.data.enums;

/*
 * *
 *  * Created by Mina Mikhail on 05/05/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
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