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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.shiftconnects.android.auth.AuthenticationManager;
import com.uwetrottmann.trakt.v2.entities.User;
import com.uwetrottmann.trakt.v2.exceptions.OAuthUnauthorizedException;


/**
 * Created by mattkranzler on 3/13/15.
 */
public class TraktUserDisplayActivity extends Activity implements AuthenticationManager.Callbacks {

    private static final String TAG = TraktUserDisplayActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExampleApplication.TRAKT_AUTHENTICATION_MANAGER.addCallbacks(this);
        authenticate();
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        ExampleApplication.TRAKT_AUTHENTICATION_MANAGER.removeCallbacks(this);
    }


    private void authenticate() {
        ExampleApplication.TRAKT_AUTHENTICATION_MANAGER.authenticate(
                this,
                ExampleApplication.TRAKT_ACCOUNT,
                ExampleApplication.TRAKT_AUTH_TOKEN
        );
    }

    @Override
    public void onAuthenticationCanceled() {
        finish();
    }

    @Override
    public void onAuthenticationSuccessful(String authToken) {
        ExampleApplication.TRAKT.setAccessToken(authToken);
        new AsyncTask<Void, Void, User>() {

            @Override
            protected User doInBackground(Void... params) {
                try {
                    User user = ExampleApplication.TRAKT.users().settings().user;
                    return user;
                } catch (OAuthUnauthorizedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(User user) {
                if (user != null) {
                    Log.d(TAG, "Retrieved trakt user. Username: " + user.username);
                } else {
                    Log.d(TAG, "An error occurred while trying to retrieve trakt user");
                }
            }
        }.execute();
    }

    @Override
    public void onAuthenticationNetworkError() {

    }

    @Override
    public void onAuthenticationFailed(Exception e) {

    }

    @Override
    public void onAuthenticationInvalidated(String invalidatedAuthToken) {

    }
}
