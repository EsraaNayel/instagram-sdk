package com.example.loginusinginstagram.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.example.loginusinginstagram.Instagram;
import com.example.loginusinginstagram.R;
import com.example.loginusinginstagram.data.model.InstagramUser;
import com.example.loginusinginstagram.data.network.ApiClient;
import com.example.loginusinginstagram.data.network.response.AccessTokenResponse;
import com.example.loginusinginstagram.utils.NetworkUtils;
import com.google.gson.Gson;
import com.uber.autodispose.ScopeProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.uber.autodispose.AutoDispose.autoDisposable;

/**
 * Created by Esraa Nayel on 4/13/2019.
 */
public class SignInActivity extends AppCompatActivity {

  private WebView webView;
  private ProgressBar progressBar;

  String accessToken;
  String userData;

  String responseType = "code";
  String grantType = "authorization_code";

  private String clientId, clientSecret, redirectURL;

  private CompositeDisposable compositeDisposable;
  private InstagramUser instagramUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!NetworkUtils.isNetworkConnected(SignInActivity.this)) {
      onInstagramSignInFailure(Instagram.ERROR_NO_INTERNET, "No Internet Connection");
    }
    setContentView(R.layout.activity_sign_in);

    clientId = getIntent().getStringExtra(Instagram.CLIENT_ID);
    clientSecret = getIntent().getStringExtra(Instagram.CLIENT_SECRET);
    redirectURL = getIntent().getStringExtra(Instagram.REDIRECT_URL);

    webView = findViewById(R.id.webView);
    progressBar = findViewById(R.id.progressBar);

    initWebView();
  }

  @SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
  private void initWebView() {
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webView.setWebChromeClient(new WebChromeClient());
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.w("Instagram", url);

        Uri uri = Uri.parse(url);
        String code = uri.getQueryParameter("code");
        if (code != null && !code.isEmpty()) {
          Log.w("Instagram", "code = " + code);
          showLoading();
          compositeDisposable = new CompositeDisposable();
          instagramUser = new InstagramUser();
          getAccessToken(code);
        }

        if (url.contains("error=user_cancelled_authorize")) {
          onInstagramSignInFailure(Instagram.ERROR_USER_CANCELLED, "User Cancelled");
        }

        return super.shouldOverrideUrlLoading(view, url);
      }

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (!NetworkUtils.isNetworkConnected(SignInActivity.this)) {
          onInstagramSignInFailure(Instagram.ERROR_NO_INTERNET, "No Internet Connection");
        }
        showLoading();
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        webView.findAllAsync("net::ERR_");
      }

      @Override
      public void onReceivedError(WebView view, int errorCode, String description,
          String failingUrl) {
        showLoading();
        super.onReceivedError(view, errorCode, description, failingUrl);
      }

      @Override
      public void onReceivedError(WebView view, WebResourceRequest request,
          WebResourceError error) {
        showLoading();
        super.onReceivedError(view, request, error);
      }

      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        showLoading();
        super.onReceivedSslError(view, handler, error);
      }

      @Override
      public void onReceivedHttpError(WebView view, WebResourceRequest request,
          WebResourceResponse errorResponse) {
        showLoading();
        super.onReceivedHttpError(view, request, errorResponse);
      }
    });
    webView.setFindListener((activeMatchOrdinal, numberOfMatches, isDoneCounting) -> {
      if (numberOfMatches > 0) {
        showLoading();
      } else {
        hideLoading();
      }
    });

    String url = getAuthorizationUrl();
    webView.loadUrl(url);
  }

  private String getAuthorizationUrl() {
    return Uri.parse("https://api.instagram.com/oauth/authorize")
        .buildUpon()
        .appendQueryParameter("client_id", clientId)
        .appendQueryParameter("redirect_uri", redirectURL)
        .appendQueryParameter("response_type", responseType).toString();
  }

  private void getAccessToken(String code) {
    compositeDisposable.add(ApiClient.getInstance().getApiService()
        .getAccessToken(clientId, clientSecret, grantType, redirectURL, code)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .as(autoDisposable(ScopeProvider.UNBOUND))
        .subscribeWith(new DisposableSingleObserver<AccessTokenResponse>() {
          @Override
          public void onSuccess(AccessTokenResponse profileResponse) {
            if (!compositeDisposable.isDisposed()) {
              accessToken = profileResponse.getAccess_token();
              userData = String.valueOf(profileResponse.getUser().getFull_name());
              onInstagramSignInSuccess(instagramUser);

              Log.w("instagram", "accessToken = " + accessToken);
              Log.w("instagram", "UserData " + new Gson().toJson(userData));
              dispose();
            }
          }

          @Override
          public void onError(Throwable e) {
            if (!compositeDisposable.isDisposed()) {
              if (!NetworkUtils.isNetworkConnected(SignInActivity.this)) {
                onInstagramSignInFailure(Instagram.ERROR_NO_INTERNET, "No Internet Connection");
              } else {
                onInstagramSignInFailure(Instagram.ERROR_OTHER, e.getMessage());
              }
              dispose();
            }
          }
        }));
  }

  @Override
  protected void onDestroy() {

    if (compositeDisposable != null) {
      compositeDisposable.dispose();
    }

    super.onDestroy();
  }

  private void onInstagramSignInSuccess(InstagramUser instagramUser) {
    clear();
    Intent intent = new Intent();
    intent.putExtra(Instagram.USER, instagramUser);
    setResult(Instagram.SUCCESS, intent);
    finish();
  }

  private void onInstagramSignInFailure(int errorType, String errorMsg) {
    Log.w("Instagram", errorMsg);
    clear();
    Intent intent = new Intent();
    intent.putExtra(Instagram.ERROR_TYPE, errorType);
    setResult(Instagram.FAILURE, intent);
    finish();
  }

  private void showLoading() {
    webView.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  private void hideLoading() {
    webView.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  private void clear() {
    if (webView == null) return;
    webView.clearCache(true);
    webView.clearHistory();
  }
}