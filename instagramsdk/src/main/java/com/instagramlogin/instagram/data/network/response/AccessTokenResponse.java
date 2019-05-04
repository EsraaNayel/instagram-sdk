package com.instagramlogin.instagram.data.network.response;

import com.instagramlogin.instagram.data.model.InstagramUser;

/**
 * Created by Esraa Nayel on 5/4/2019.
 */

public class AccessTokenResponse {

  /**
   * access_token : 51a0220c1a94184a1bc13332ce9699c
   * user : {"id":"","username":"esraa_nayel","profile_picture"","full_name":"Esraa Nayel","bio":"Android Developer 📱\n","website":"","is_business":false}
   */

  private String access_token;
  private InstagramUser user;

  public String getAccess_token() {
    return access_token;
  }

  public InstagramUser getUser() {
    return user;
  }
}
