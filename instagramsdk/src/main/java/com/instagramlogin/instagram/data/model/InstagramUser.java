package com.instagramlogin.instagram.data.model;

/**
 * Created by Esraa Nayel on 5/4/2019.
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