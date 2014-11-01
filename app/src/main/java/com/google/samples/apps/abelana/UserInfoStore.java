package com.google.samples.apps.abelana;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;

public class UserInfoStore {
    private SharedPreferences mPrefs;
    public String PRIVATE_STORAGE_NAME = "userinfostore";
    public String ID_TOKEN_KEY = "idTokenKey";
    public String GITKIT_USER_KEY = "gitkitUserKey";

    public UserInfoStore(Context ctx) {
        mPrefs = ctx.getSharedPreferences(PRIVATE_STORAGE_NAME, Context.MODE_PRIVATE);
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
                .apply();
    }

    public boolean isUserLoggedIn() {
        return getSavedIdToken() != null && getSavedGitkitUser() != null;
    }
}
