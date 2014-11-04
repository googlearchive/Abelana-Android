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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/* Activity handle the apps settings. Refer to the Android documentation for how to implement
 * settings. http://developer.android.com/guide/topics/ui/settings.html
 */
public class SettingsActivity extends Activity {
    public static final String KEY_PREF_PROFILE_VISIBILITY = "pref_profile_visibility";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Preference signOut = findPreference("pref_sign_out");
            Preference wipeout = findPreference("pref_wipeout");
            Preference about = findPreference("pref_about");

            wipeout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog builder = new AlertDialog.Builder(getActivity())
                            .setMessage(getActivity().getResources().getString(R.string.wipeout_warning_message))
                            .setTitle(getActivity().getResources().getString(R.string.wipeout_title))
                            .setPositiveButton(getActivity().getResources().getString(R.string.wipeout_positive),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            AbelanaClient client = new AbelanaClient();
                                            client.mWipeout.wipeout(Data.aTok, new Callback<AbelanaClient.Status>() {

                                                @Override
                                                public void success(AbelanaClient.Status status, Response response) {
                                                    Toast.makeText(getActivity(),
                                                            getActivity().getResources().getString(R.string.wipeout_success),
                                                            Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void failure(RetrofitError error) {
                                                    error.printStackTrace();
                                                }
                                            });

                                        }
                                    })
                            .setNegativeButton(getActivity().getResources().getString(R.string.wipeout_cancel),
                                    new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //User canceled the dialog
                                }
                            })
                            .create();
                    builder.show();
                    return true;
                }
            });

            signOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Logout by clearing the login information from the shared preferences
                    UserInfoStore client = new UserInfoStore(getActivity());
                    client.clearLoggedInUser();
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);

                    //clear the backstack
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    return true;
                }
            });

            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    // Build the about body view and append the link to see OSS licenses
                    SpannableStringBuilder aboutBody = new SpannableStringBuilder();
                    aboutBody.append(Html.fromHtml(getActivity().getResources().getString(R.string.splash_dialog_body)));

                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    TextView aboutBodyView = (TextView) layoutInflater.inflate(R.layout.dialog_about, null);
                    aboutBodyView.setText(aboutBody);
                    aboutBodyView.setMovementMethod(new LinkMovementMethod());
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Abelana Demo")
                            .setView(aboutBodyView)
                            .setPositiveButton("ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .show();
                    return true;
                }
            });

        }
    }
}
