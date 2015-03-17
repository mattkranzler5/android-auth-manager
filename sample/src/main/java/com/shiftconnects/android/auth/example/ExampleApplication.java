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

import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.shiftconnects.android.auth.AccountAuthenticator;
import com.shiftconnects.android.auth.AuthenticationManager;
import com.shiftconnects.android.auth.example.service.BitlyOAuthTokenService;
import com.shiftconnects.android.auth.example.service.BitlyRetrofitService;
import com.shiftconnects.android.auth.example.service.TraktOAuthTokenService;
import com.shiftconnects.android.auth.example.util.GsonConverter;
import com.shiftconnects.android.auth.util.AESCrypto;
import com.shiftconnects.android.auth.util.AuthConstants;
import com.shiftconnects.android.auth.util.Crypto;
import com.uwetrottmann.trakt.v2.TraktV2;

import retrofit.RestAdapter;

/**
 * Created by mattkranzler on 2/25/15.
 */
public class ExampleApplication extends Application {

    private static final String BITLY_CLIENT_ID = "your_client_id";
    private static final String BITLY_CLIENT_SECRET = "your_client_secret";

    public static final String TRAKT_CLIENT_ID = "your_client_id";
    public static final String TRAKT_CLIENT_SECRET = "your_client_secret";

    private static Crypto AES_CRYPTO;

    public static final String BITLY_ACCOUNT = "com.bitly.ACCOUNT_TYPE";
    public static final String BITLY_AUTH_TOKEN = "com.bitly.AUTH_TOKEN_TYPE";
    public static AccountAuthenticator BITLY_ACCOUNT_AUTHENTICATOR;
    public static AuthenticationManager BITLY_AUTHENTICATION_MANAGER;
    public static BitlyRetrofitService BITLY_SERVICE;

    // Trakt
    public static final String TRAKT_ACCOUNT = "tv.trakt.ACCOUNT_TYPE";
    public static final String TRAKT_AUTH_TOKEN = "tv.trakt.AUTH_TOKEN_TYPE";
    public static AccountAuthenticator TRAKT_ACCOUNT_AUTHENTICATOR;
    public static AuthenticationManager TRAKT_AUTHENTICATION_MANAGER;

    public static TraktV2 TRAKT;

    @Override public void onCreate() {
        super.onCreate();

        AES_CRYPTO = new AESCrypto(getSharedPreferences("crypto", Context.MODE_PRIVATE));

        BITLY_SERVICE = new RestAdapter.Builder()
                .setEndpoint("https://api-ssl.bitly.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(new Gson()))
                .build()
                .create(BitlyRetrofitService.class);

        AuthConstants.setDebug(true);

        BITLY_AUTHENTICATION_MANAGER = new AuthenticationManager(
                AccountManager.get(this),
                new BitlyOAuthTokenService(BITLY_SERVICE),
                AES_CRYPTO,
                BITLY_CLIENT_ID,
                BITLY_CLIENT_SECRET
        );

        BITLY_ACCOUNT_AUTHENTICATOR = new AccountAuthenticator(
                this,
                BitlyLoginActivity.class,
                BITLY_AUTHENTICATION_MANAGER
        );

        TRAKT_AUTHENTICATION_MANAGER = new AuthenticationManager(
                AccountManager.get(this),
                new TraktOAuthTokenService(),
                AES_CRYPTO,
                TRAKT_CLIENT_ID,
                TRAKT_CLIENT_SECRET
        );

        TRAKT_ACCOUNT_AUTHENTICATOR = new AccountAuthenticator(
                this,
                TraktOAuthActivity.class,
                TRAKT_AUTHENTICATION_MANAGER
        );

        TRAKT = new TraktV2().setApiKey(TRAKT_CLIENT_ID);
    }
}
