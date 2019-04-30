package com.example.loginusinginstagram.data.network.response;

/**
 * Created by Esraa Nayel on 4/13/2019.
 */
public class AccessTokenResponse {

  /**
   * access_token : 51a0220c1a94184a1bc13332ce9699c
   * user : {"id":"","username":"esraa_nayel","profile_picture"","full_name":"Esraa Nayel","bio":"Android Developer 📱\n","website":"","is_business":false}
   */

  private String access_token;
  private UserBean user;

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public UserBean getUser() {
    return user;
  }

  public void setUser(UserBean user) {
    this.user = user;
  }

  public static class UserBean {
    /**
     * id : 374186343
     * username : esraa_nayel
     * profile_picture :
     * full_name : Esraa Nayel
     * bio : Android Developer 📱
     * website :
     * is_business : false
     */

    private String id;
    private String username;
    private String profile_picture;
    private String full_name;
    private String bio;
    private String website;
    private boolean is_business;

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

    public boolean isIs_business() {
      return is_business;
    }

    public void setIs_business(boolean is_business) {
      this.is_business = is_business;
    }
  }
}
