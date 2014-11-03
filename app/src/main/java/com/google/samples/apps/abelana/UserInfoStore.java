/*
 * Copyright 2014 Google Inc. All rights reserved.
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

package com.google.samples.apps.abelana;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;

/*
 * Stores data that needs to persist using Android SharedPreferences
 */

public class UserInfoStore {
    private SharedPreferences mPrefs;
    public String PRIVATE_STORAGE_NAME = "userinfostore";
    public String ID_TOKEN_KEY = "idTokenKey";
    public String GITKIT_USER_KEY = "gitkitUserKey";

    public String ACC_TOKEN = "accessToken";
    public String HelpFul = "helpfulThing";

    public UserInfoStore(Context ctx) {
        mPrefs = ctx.getSharedPreferences(PRIVATE_STORAGE_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String AccToken, String helpful) {
        mPrefs.edit().putString(ACC_TOKEN, AccToken)
                .putString(HelpFul, helpful);
    }

    public String getHelpful() {
        return mPrefs.getString(HelpFul, null);
    }

    public String getAccToken() {
        return mPrefs.getString(ACC_TOKEN, null);
    }

    public void saveIdTokenAndGitkitUser(IdToken idToken, GitkitUser user) {
        mPrefs.edit()
                .putString(ID_TOKEN_KEY, idToken.getTokenString())
                .putString(GITKIT_USER_KEY, user.toString())
                .apply();
    }

    public IdToken getSavedIdToken() {
        String tokenString = mPrefs.getString(ID_TOKEN_KEY, null);
        if (tokenString != null) {
            IdToken idToken = IdToken.parse(tokenString);
            if (idToken != null && !idToken.isExpired()) {
                return idToken;
            }
        }
        return null;
    }

    public GitkitUser getSavedGitkitUser() {
        String userString = mPrefs.getString(GITKIT_USER_KEY, null);
        if (userString != null) {
            return GitkitUser.fromJsonString(userString);
        }
        return null;
    }

    public void clearLoggedInUser() {
        mPrefs.edit()
                .remove(ID_TOKEN_KEY)
                .remove(GITKIT_USER_KEY)
                .remove(ACC_TOKEN)
                .apply();
    }

    public boolean isUserLoggedIn() {
        return getSavedIdToken() != null && getSavedGitkitUser() != null;
    }
}
