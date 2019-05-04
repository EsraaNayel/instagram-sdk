package com.mina_mikhail.instagram.data.network.response;

import com.mina_mikhail.instagram.data.model.InstagramUser;

/*
 * *
 *  * Created by Mina Mikhail on 05/05/2019
 *  * Copyright (c) 2019 . All rights reserved.
 * *
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
