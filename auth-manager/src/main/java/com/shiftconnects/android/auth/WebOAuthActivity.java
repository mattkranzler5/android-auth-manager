/*
 * Copyright (C) 2015 P100 OG, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shiftconnects.android.auth;

import android.accounts.AccountAuthenticatorActivity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Base class to create an OAuth 2.0 authorization flow using an embedded {@link
 * android.webkit.WebView}.
 */
public abstract class WebOAuthActivity extends AccountAuthenticatorActivity implements AuthenticatorActivity {

    private WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        webview = (WebView) findViewById(R.id.webview);

        setupViews(webview);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /**
         * Force the text-to-speech accessibility Javascript plug-in service on Android 4.2.2 to
         * get shutdown, to avoid leaking its context.
         *
         * http://stackoverflow.com/a/18798305/1000543
         */
        if (webview != null) {
            webview.getSettings().setJavaScriptEnabled(false);
            webview = null;
        }
    }

    protected void setupViews(WebView webview) {
        webview.setWebViewClient(webViewClient);

        // make sure we start fresh
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        webview.clearCache(true);
        webview.getSettings().setJavaScriptEnabled(true);

        // load the authorization page
        String authUrl = getAuthorizationUrl();
        if (TextUtils.isEmpty(authUrl)) {
            throw new IllegalStateException("You must provide a valid authorization URL via getAuthorizationUrl()");
        } else {
            webview.loadUrl(authUrl);
        }
    }

    protected WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            setProgressBarIndeterminate(true);
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setProgressBarIndeterminateVisibility(false);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                String failingUrl) {
            handleAuthorizationError(errorCode, description);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && url.startsWith(getRedirectUri())) {
                handleRedirect(Uri.parse(url));
                finish();
                return true;
            }
            return false;
        }
    };

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract String getAuthorizationUrl();

    protected abstract String getRedirectUri();

    protected abstract void handleRedirect(Uri uri);

    protected abstract void handleAuthorizationError(int errorCode, String description);
}