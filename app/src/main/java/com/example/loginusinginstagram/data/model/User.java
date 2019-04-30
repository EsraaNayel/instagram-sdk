package com.example.loginusinginstagram.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Esraa Nayel on 4/13/2019.
 */
public class User implements Parcelable {

  private String id;
  private String username;
  private String profile_picture;
  private String full_name;
  private String bio;
  private String website;
  private String is_business;

  public User() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getProfile_picture() {
    return profile_picture;
  }

  public void setProfile_picture(String profile_picture) {
    this.profile_picture = profile_picture;
  }

  public String getFull_name() {
    return full_name;
  }

  public void setFull_name(String full_name) {
    this.full_name = full_name;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getIs_business() {
    return is_business;
  }

  public void setIs_business(String is_business) {
    this.is_business = is_business;
  }

  protected User(Parcel in) {
    id = in.readString();
    username = in.readString();
    profile_picture = in.readString();
    full_name = in.readString();
    bio = in.readString();
    website = in.readString();
    is_business = in.readString();
  }

  public static final Creator<User> CREATOR = new Creator<User>() {
    @Override
    public User createFromParcel(Parcel in) {
      return new User(in);
    }

    @Override
    public User[] newArray(int size) {
      return new User[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(username);
    dest.writeString(profile_picture);
    dest.writeString(full_name);
    dest.writeString(bio);
    dest.writeString(website);
    dest.writeString(is_business);
  }
}
