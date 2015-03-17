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

package com.shiftconnects.android.auth.example.service;

import com.shiftconnects.android.auth.example.model.TraktOAuthToken;
import com.shiftconnects.android.auth.service.OAuthTokenService;
import com.uwetrottmann.trakt.v2.TraktV2;

import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;

/**
 * Created by mattkranzler on 3/13/15.
 */
public class TraktOAuthTokenService implements OAuthTokenService<TraktOAuthToken> {

    @Override
    public TraktOAuthToken getTokenWithAuthorizationCode(String clientId, String clientSecret, String authorizationCode, String redirectUri) {
        TraktOAuthToken token = null;
        try {
            OAuthAccessTokenResponse response = TraktV2.getAccessToken(clientId, clientSecret, redirectUri, authorizationCode);
            if (response != null) {
                token = new TraktOAuthToken(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token
                ;
    }

    @Override
    public TraktOAuthToken getTokenWithPassword(String clientId, String clientSecret, String userName, String password) {
        return null;
    }

    @Override
    public TraktOAuthToken getTokenWithRefreshToken(String clientId, String clientSecret, String refreshToken) {
        return null;
    }

    @Override
    public TraktOAuthToken getTokenWithClientCredentials(String clientId, String clientSecret) {
        return null;
    }
}
