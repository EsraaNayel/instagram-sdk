package com.instagramlogin.instagram.utils;

/**
 * Created by Esraa Nayel on 5/4/2019.
 */

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class WebViewUtils {

  public static void clearCookies(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
      CookieManager.getInstance().removeAllCookies(null);
      CookieManager.getInstance().flush();
    } else {
      CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
      cookieSyncMngr.startSync();
      CookieManager cookieManager = CookieManager.getInstance();
      cookieManager.removeAllCookie();
      cookieManager.removeSessionCookie();
      cookieSyncMngr.stopSync();
      cookieSyncMngr.sync();
    }
  }
}