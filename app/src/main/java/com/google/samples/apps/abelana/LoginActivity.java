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

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.identitytoolkit.GitkitClient;
import com.google.identitytoolkit.GitkitUser;
import com.google.identitytoolkit.IdToken;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Handles logging in using the Google Identity Toolkit - refer to the product documentation
 * for more information https://developers.google.com/identity-toolkit/
 */
public class LoginActivity extends FragmentActivity implements OnClickListener {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    public UserInfoStore mUserInfoStore;
    private GitkitClient mGitkitClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mUserInfoStore = new UserInfoStore(this);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.hide();

        // Step 1: Create a GitkitClient.
        // The configurations are set in the AndroidManifest.xml. You can also set or overwrite them
        // by calling the corresponding setters on the GitkitClient builder.
        //

        mGitkitClient = GitkitClient.newBuilder(this, new GitkitClient.SignInCallbacks() {
            // Implement the onSignIn method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process succeeds. A Gitkit IdToken and the signed
            // in account information are passed to the callback.
            @Override
            public void onSignIn(IdToken idToken, GitkitUser user) {
                mUserInfoStore.saveIdTokenAndGitkitUser(idToken, user);
                showProfilePage(idToken, user);
            }

            // Implement the onSignInFailed method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process fails.
            @Override
            public void onSignInFailed() {
                Toast.makeText(LoginActivity.this, "Sign in failed", Toast.LENGTH_LONG).show();
            }
        }).build();


        // Step 2: Check if there is an already signed in user.
        // If there is an already signed in user, show the ic_profile page and welcome message.
        // Otherwise, show a sign in button.
        //
        if (mUserInfoStore.isUserLoggedIn()) {
            showProfilePage(mUserInfoStore.getSavedIdToken(), mUserInfoStore.getSavedGitkitUser());
        } else {
            showSignInPage();
        }

    }


    // Step 3: Override the onActivityResult method.
    // When a result is returned to this activity, it is maybe intended for GitkitClient. Call
    // GitkitClient.handleActivityResult to check the result. If the result is for GitkitClient,
    // the method returns true to indicate the result has been consumed.
    //

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (!mGitkitClient.handleActivityResult(requestCode, resultCode, intent)) {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }


    // Step 4: Override the onNewIntent method.
    // When the app is invoked with an intent, it is possible that the intent is for GitkitClient.
    // Call GitkitClient.handleIntent to check it. If the intent is for GitkitClient, the method
    // returns true to indicate the intent has been consumed.

    @Override
    protected void onNewIntent(Intent intent) {
        if (!mGitkitClient.handleIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    public void showSignInPage() {
        setContentView(R.layout.fragment_main);
        Button button = (Button) findViewById(R.id.sign_in_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mGitkitClient.startSignIn();
            }
        });
        if (!mUserInfoStore.wasShown()) {
            displayDialog();
            mUserInfoStore.saveDialog();
        }

    }

    private void displayDialog() {
        // Build the about body view and append the link to see OSS licenses
        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        aboutBody.append(Html.fromHtml(getString(R.string.splash_dialog_body)));

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        TextView aboutBodyView = (TextView) layoutInflater.inflate(R.layout.dialog_about, null);
        aboutBodyView.setText(aboutBody);
        aboutBodyView.setMovementMethod(new LinkMovementMethod());
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.splash_dialog_title))
                .setView(aboutBodyView)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .show();
    }



    private void showProfilePage(IdToken idToken, final GitkitUser user) {
        Log.v(LOG_TAG, "Token is: " + idToken.toString() + " Account is: " + user.toString());
        //Save the user's basic information in the Data class for use throughout the app
        Data.mDisplayName = user.getDisplayName();
        Data.mEmail = user.getEmail();
        Data.aTok = mUserInfoStore.getAccToken();
        String photoUrl = user.getPhotoUrl();
        if (photoUrl == null) photoUrl = "null";
        //Refer to the FeedFragment to understand how these APIs work
        if(Data.aTok == null){
            AbelanaClient abelanaClient = new AbelanaClient();

            abelanaClient.mLogin.login(idToken.getTokenString(),
                    Utilities.base64Encoding(user.getDisplayName()),
                    Utilities.base64Encoding(photoUrl),
                    new Callback<AbelanaClient.ATOKJson>() {

                        public void success(AbelanaClient.ATOKJson l, Response r) {
                            String aTok = l.atok;
                            Log.v(LOG_TAG, "DONE! Token is " + aTok);
                            Data.aTok = aTok;
                            loginDone();
                        }

                        public void failure(RetrofitError e) {
                            Log.v(LOG_TAG, "Failure at login!");
                            Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG);
                            showSignInPage();
                        }
                    });
        }


    }

    public void loginDone() {

        AbelanaClient abelanaClient = new AbelanaClient();

        Data.helpful = mUserInfoStore.getHelpful();
        if(Data.helpful == null) {
            abelanaClient.mGetSecretKey.getSecretKey(Data.aTok, new Callback<AbelanaClient.Status>() {
                @Override
                public void success(AbelanaClient.Status status, Response response) {
                    Data.helpful = status.status;

                    haveSecret();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.v(LOG_TAG, "Failure at secret key!");
                    Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG);
                    showSignInPage();
                }
            });

        } else {
            haveSecret();
        }
    }

    //Once login is complete, launches the FeedActivity to get into the app
    public void haveSecret () {
        mUserInfoStore.saveToken(Data.aTok, Data.helpful);
        AbelanaUser au = AbelanaThings.start(getApplicationContext(), Data.aTok, Data.helpful);

        Intent feedIntent = new Intent(getApplicationContext(), FeedActivity.class);
        startActivity(feedIntent);
        finish();
    }
    // Step 5: Respond to user actions. Required method.
    @Override
    public void onClick(View v) {
        //Nothing to do here
    }

}
