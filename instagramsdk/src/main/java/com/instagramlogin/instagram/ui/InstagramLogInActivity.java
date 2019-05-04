package com.instagramlogin.instagram.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.instagramlogin.instagram.InstagramSDK;
import com.instagramlogin.instagram.R;
import com.instagramlogin.instagram.callback.InstagramResponse;
import com.instagramlogin.instagram.data.enums.ErrorCode;
import com.instagramlogin.instagram.data.model.InstagramUser;
import com.instagramlogin.instagram.data.network.ApiClient;
import com.instagramlogin.instagram.data.network.response.AccessTokenResponse;
import com.instagramlogin.instagram.databinding.ActivityInstagramLogInBinding;
import com.instagramlogin.instagram.utils.Constants;
import com.instagramlogin.instagram.utils.NetworkUtils;
import com.uber.autodispose.ScopeProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.instagramlogin.instagram.data.enums.ErrorCode.ERROR_NO_INTERNET;
import static com.instagramlogin.instagram.data.enums.ErrorCode.ERROR_OTHER;
import static com.instagramlogin.instagram.data.enums.ErrorCode.ERROR_USER_CANCELLED;
import static com.instagramlogin.instagram.data.enums.ErrorCode.ERROR_USER_CANCELLED_MSG;
import static com.instagramlogin.instagram.data.enums.QueryParameter.AuthorizationUrlParameters.CLIENT_ID;
import static com.instagramlogin.instagram.data.enums.QueryParameter.AuthorizationUrlParameters.REDIRECT_URI;
import static com.instagramlogin.instagram.data.enums.QueryParameter.AuthorizationUrlParameters.RESPONSE_TYPE;
import static com.instagramlogin.instagram.data.enums.QueryParameter.CodeUrlParameters.CODE;
import static com.instagramlogin.instagram.utils.Constants.AUTHORIZATION_URL;
import static com.instagramlogin.instagram.utils.WebViewUtils.clearCookies;
import static com.uber.autodispose.AutoDispose.autoDisposable;

/**
 * Created by Esraa Nayel on 5/4/2019.
 */

public class InstagramLogInActivity
    extends AppCompatActivity {

  public static void open(Context context) {
    Intent intent = new Intent(context, InstagramLogInActivity.class);
    context.startActivity(intent);
  }

  private static final String TAG = "LINKED_IN_SDK";
  private ActivityInstagramLogInBinding mViewDataBinding;

  private WebView webView;
  private ProgressBar progressBar;

  private CompositeDisposable compositeDisposable;
  private InstagramUser instagramUser;

  private boolean shouldOpenNextPage = true;
  private boolean isCancelClicked = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (!NetworkUtils.isNetworkConnected(this)) {
      onInstagramSignInFailure(ERROR_NO_INTERNET, getResources().getString(R.string.no_internet));
    } else {
      performDataBinding();
      setUpViews();
    }
  }

  private void performDataBinding() {
    mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_instagram_log_in);
    mViewDataBinding.executePendingBindings();
  }

  private void setUpViews() {
    setUpLoader();

    initWebView();
  }

  private void setUpLoader() {
    progressBar = mViewDataBinding.progressBar;

    Sprite circle = new Circle();
    circle.setColor(getResources().getColor(R.color.libAccentColor));
    progressBar.setIndeterminateDrawable(circle);
  }

  @SuppressLint({ "SetJavaScriptEnabled" })
  private void initWebView() {
    webView = mViewDataBinding.webView;
    webView.requestFocus(View.FOCUS_DOWN);
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webView.setScrollbarFadingEnabled(true);
    webView.setVerticalScrollBarEnabled(false);
    webView.setWebChromeClient(new WebChromeClient());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      webView.setWebViewClient(getLollipopWebViewClient());
    } else {
      webView.setWebViewClient(getPreLollipopWebViewClient());
    }

    webView.setFindListener((activeMatchOrdinal, numberOfMatches, isDoneCounting) -> {
      if (numberOfMatches > 0) {
        showLoading();
      } else {
        hideLoading();
      }
    });
    webView.loadUrl(getAuthorizationUrl());
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  private WebViewClient getLollipopWebViewClient() {
    return new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        Log.w(TAG, "shouldOverrideUrlLoading-->" + url);
        if (shouldOpenNextPage) {
          Uri uri = Uri.parse(url);
          String code = uri.getQueryParameter(CODE);
          if (code != null && !TextUtils.isEmpty(code)) {
            Log.w(TAG, "code = " + code);
            showLoading();
            compositeDisposable = new CompositeDisposable();
            instagramUser = new InstagramUser();
            getUserData(code);
          }

          if (url.contains(ERROR_USER_CANCELLED_MSG)) {
            onInstagramSignInFailure(ERROR_USER_CANCELLED,
                getResources().getString(R.string.user_cancelled));
          }
        } else {
          if (isCancelClicked) {
            onInstagramSignInFailure(ERROR_USER_CANCELLED,
                getResources().getString(R.string.user_cancelled));
          } else {
            Toast.makeText(InstagramLogInActivity.this,
                getResources().getString(R.string.action_not_supported), Toast.LENGTH_SHORT).show();
          }
        }

        return super.shouldOverrideUrlLoading(view, request);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        Log.w(TAG, "onPageFinished-->" + url);

        webView.findAllAsync(ErrorCode.ERROR_MSG);
      }

      @Override
      public void onReceivedError(WebView view, int errorCode, String description,
          String failingUrl) {
        if (!NetworkUtils.isNetworkConnected(InstagramLogInActivity.this)) {
          onInstagramSignInFailure(ERROR_NO_INTERNET,
              getResources().getString(R.string.no_internet));
        } else {
          onInstagramSignInFailure(ERROR_OTHER,
              getResources().getString(R.string.some_error));
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
      }

      @Override
      public void onReceivedError(WebView view, WebResourceRequest request,
          WebResourceError error) {
        if (!NetworkUtils.isNetworkConnected(InstagramLogInActivity.this)) {
          onInstagramSignInFailure(ERROR_NO_INTERNET,
              getResources().getString(R.string.no_internet));
        } else {
          onInstagramSignInFailure(ERROR_OTHER, getResources().getString(R.string.some_error));
        }
        super.onReceivedError(view, request, error);
      }

      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (!NetworkUtils.isNetworkConnected(InstagramLogInActivity.this)) {
          onInstagramSignInFailure(ERROR_NO_INTERNET,
              getResources().getString(R.string.no_internet));
        } else {
          onInstagramSignInFailure(ERROR_OTHER,
              getResources().getString(R.string.some_error));
        }
        super.onReceivedSslError(view, handler, error);
      }
    };
  }

  private WebViewClient getPreLollipopWebViewClient() {
    return new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.w(TAG, "shouldOverrideUrlLoading-->" + url);
        if (shouldOpenNextPage) {
          Uri uri = Uri.parse(url);
          String code = uri.getQueryParameter(CODE);
          if (code != null && !TextUtils.isEmpty(code)) {
            Log.w(TAG, "code = " + code);
            showLoading();
            compositeDisposable = new CompositeDisposable();
            instagramUser = new InstagramUser();
            getUserData(code);
          }

          if (url.contains(ERROR_USER_CANCELLED_MSG)) {
            onInstagramSignInFailure(ERROR_USER_CANCELLED,
                getResources().getString(R.string.user_cancelled));
          }
        } else {
          if (isCancelClicked) {
            onInstagramSignInFailure(ERROR_USER_CANCELLED,
                getResources().getString(R.string.user_cancelled));
          } else {
            Toast.makeText(InstagramLogInActivity.this,
                getResources().getString(R.string.action_not_supported), Toast.LENGTH_SHORT).show();
          }
        }

        return super.shouldOverrideUrlLoading(view, url);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        Log.w(TAG, "onPageFinished-->" + url);

        webView.findAllAsync(ErrorCode.ERROR_MSG);
      }

      @Override
      public void onReceivedError(WebView view, int errorCode, String description,
          String failingUrl) {
        if (!NetworkUtils.isNetworkConnected(InstagramLogInActivity.this)) {
          onInstagramSignInFailure(ERROR_NO_INTERNET,
              getResources().getString(R.string.no_internet));
        } else {
          onInstagramSignInFailure(ERROR_OTHER,
              getResources().getString(R.string.some_error));
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
      }

      @Override
      public void onReceivedError(WebView view, WebResourceRequest request,
          WebResourceError error) {
        if (!NetworkUtils.isNetworkConnected(InstagramLogInActivity.this)) {
          onInstagramSignInFailure(ERROR_NO_INTERNET,
              getResources().getString(R.string.no_internet));
        } else {
          onInstagramSignInFailure(ERROR_OTHER,
              getResources().getString(R.string.some_error));
        }
        super.onReceivedError(view, request, error);
      }

      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (!NetworkUtils.isNetworkConnected(InstagramLogInActivity.this)) {
          onInstagramSignInFailure(ERROR_NO_INTERNET,
              getResources().getString(R.string.no_internet));
        } else {
          onInstagramSignInFailure(ERROR_OTHER,
              getResources().getString(R.string.some_error));
        }
        super.onReceivedSslError(view, handler, error);
      }
    };
  }

  private String getAuthorizationUrl() {
    return Uri.parse(AUTHORIZATION_URL)
        .buildUpon()
        .appendQueryParameter(CLIENT_ID, getClientID())
        .appendQueryParameter(REDIRECT_URI, getRedirectURL())
        .appendQueryParameter(RESPONSE_TYPE, CODE).toString();
  }

  private void getUserData(String code) {
    compositeDisposable.add(ApiClient.getInstance().getApiService()
        .getAccessToken(getClientID(), getClientSecret(), Constants.GRANT_TYPE, getRedirectURL(),
            code)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .as(autoDisposable(ScopeProvider.UNBOUND))
        .subscribeWith(new DisposableSingleObserver<AccessTokenResponse>() {
          @Override
          public void onSuccess(AccessTokenResponse profileResponse) {
            if (!compositeDisposable.isDisposed()) {
              instagramUser = profileResponse.getUser();
              getResponse().onInstagramLogInSuccess(instagramUser);

              clear();
              finish();
              dispose();
            }
          }

          @Override
          public void onError(Throwable e) {
            if (!compositeDisposable.isDisposed()) {
              if (!NetworkUtils.isNetworkConnected(InstagramLogInActivity.this)) {
                onInstagramSignInFailure(ERROR_NO_INTERNET,
                    getResources().getString(R.string.no_internet));
              } else {
                onInstagramSignInFailure(ERROR_OTHER, e.getMessage());
              }
              dispose();
            }
          }
        }));
  }

  private void onInstagramSignInFailure(int errorType, String errorMsg) {
    getResponse().onInstagramLogInFail(errorType, errorMsg);
    clear();
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
    clearCookies(this);

    if (webView == null) return;
    webView.clearCache(true);
    webView.clearHistory();
    webView.clearMatches();
    webView.clearFormData();
    webView.clearSslPreferences();
  }

  private InstagramSDK getInstagramInstance() {
    return InstagramSDK.getInstance();
  }

  private InstagramResponse getResponse() {
    return getInstagramInstance().getResponse();
  }

  private String getClientID() {
    return getInstagramInstance().getClientID();
  }

  private String getClientSecret() {
    return getInstagramInstance().getClientSecret();
  }

  private String getRedirectURL() {
    return getInstagramInstance().getRedirectURL();
  }

  @Override
  public void onBackPressed() {
    clear();
    onInstagramSignInFailure(ERROR_USER_CANCELLED,
        getResources().getString(R.string.user_cancelled));
  }

  @Override
  protected void onDestroy() {
    if (compositeDisposable != null) {
      compositeDisposable.dispose();
    }
    super.onDestroy();
  }
}