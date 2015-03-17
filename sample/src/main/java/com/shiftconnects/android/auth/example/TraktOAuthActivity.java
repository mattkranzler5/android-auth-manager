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

package com.shiftconnects.android.auth.example;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.shiftconnects.android.auth.WebOAuthActivity;
import com.uwetrottmann.trakt.v2.TraktV2;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by mattkranzler on 3/13/15.
 */
public class TraktOAuthActivity extends WebOAuthActivity {

    private static final String LOCAL_HOST = "http://localhost";

    private String state;

    @Override
    protected String getAuthorizationUrl() {
        state = new BigInteger(130, new SecureRandom()).toString(32);
        try {
            OAuthClientRequest request = TraktV2.getAuthorizationRequest(
                    ExampleApplication.TRAKT_CLIENT_ID,
                    LOCAL_HOST,
                    state,
                    null);
            return request.getLocationUri();
        } catch (OAuthSystemException e) {
            Log.e(TraktOAuthActivity.class.getName(), e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected String getRedirectUri() {
        return LOCAL_HOST;
    }

    @Override
    protected void handleRedirect(Uri uri) {
        String authorizationCode = uri.getQueryParameter("code");
        if (!TextUtils.equals(state, uri.getQueryParameter("state"))) {
            authorizationCode = null;
        }
        if (TextUtils.isEmpty(authorizationCode)) {
            Toast.makeText(this, "An error occurred while trying to authenticate with Trakt.", Toast.LENGTH_SHORT).show();
        } else {
            new AsyncTask<String, Void, Intent>() {

                @Override
                protected Intent doInBackground(String... params) {
                    final String accessToken = ExampleApplication.TRAKT_AUTHENTICATION_MANAGER.loginWithAuthorizationCode(
                            params[0],
                            "trakt",
                            ExampleApplication.TRAKT_ACCOUNT,
                            ExampleApplication.TRAKT_AUTH_TOKEN,
                            LOCAL_HOST,
                            true
                    );
                    final Intent intent = new Intent();
                    final Bundle data = new Bundle();
                    final String accountType = getIntent().getStringExtra(KEY_ACCOUNT_TYPE);
                    data.putString(KEY_ACCOUNT_NAME, "trakt");
                    data.putString(KEY_ACCOUNT_TYPE, accountType);
                    data.putString(KEY_AUTHTOKEN, accessToken);
                    intent.putExtras(data);

                    return intent;
                }

                @Override
                protected void onPostExecute(Intent intent) {
                    finishLogin(intent);
                }
            }.execute(authorizationCode);
        }
    }

    private void finishLogin(Intent intent) {
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void handleAuthorizationError(int errorCode, String description) {

    }
}
