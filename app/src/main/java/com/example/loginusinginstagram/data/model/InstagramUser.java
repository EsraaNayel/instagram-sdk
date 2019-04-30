package com.example.loginusinginstagram.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Esraa Nayel on 4/13/2019.
 */
public class InstagramUser implements Parcelable {

  private String access_token;
  private User user;

  public InstagramUser() {
  }

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  protected InstagramUser(Parcel in) {
    access_token = in.readString();
    user = in.readParcelable(User.class.getClassLoader());
  }

  public static final Creator<InstagramUser> CREATOR = new Creator<InstagramUser>() {
    @Override
    public InstagramUser createFromParcel(Parcel in) {
      return new InstagramUser(in);
    }

    @Override
    public InstagramUser[] newArray(int size) {
      return new InstagramUser[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(access_token);
    dest.writeParcelable(user, flags);
  }
}