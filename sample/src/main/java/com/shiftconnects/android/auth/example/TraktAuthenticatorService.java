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

import com.shiftconnects.android.auth.AccountAuthenticator;
import com.shiftconnects.android.auth.AccountAuthenticatorService;

/**
 * Created by mattkranzler on 2/25/15.
 */
public class TraktAuthenticatorService extends AccountAuthenticatorService {

    @Override protected AccountAuthenticator getAccountAuthenticator() {
        return ExampleApplication.TRAKT_ACCOUNT_AUTHENTICATOR;
    }
}
