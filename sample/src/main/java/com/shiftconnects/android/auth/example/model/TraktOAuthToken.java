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

package com.shiftconnects.android.auth.example.model;

import com.shiftconnects.android.auth.model.OAuthToken;

import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;

/**
 * Created by mattkranzler on 3/13/15.
 */
public class TraktOAuthToken implements OAuthToken {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;

    public TraktOAuthToken(OAuthAccessTokenResponse response) {
        accessToken = response.getAccessToken();
        refreshToken = response.getRefreshToken();
        expiresIn = response.getExpiresIn();
    }

    @Override
    public String getAuthToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public long getExpiresIn() {
        return expiresIn == null ? 0 : expiresIn;
    }
}
