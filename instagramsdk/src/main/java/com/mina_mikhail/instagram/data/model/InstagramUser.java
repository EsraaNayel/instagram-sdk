package com.mina_mikhail.instagram.data.model;

/*
 * *
 *  * Created by Mina Mikhail on 05/05/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
 */

public class InstagramUser {

  private String id;
  private String username;
  private String profile_picture;
  private String full_name;
  private String bio;
  private String website;
  private String is_business;

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getProfile_picture() {
    return profile_picture;
  }

  public String getFull_name() {
    return full_name;
  }

  public String getBio() {
    return bio;
  }

  public String getWebsite() {
    return website;
  }

  public String getIs_business() {
    return is_business;
  }
}