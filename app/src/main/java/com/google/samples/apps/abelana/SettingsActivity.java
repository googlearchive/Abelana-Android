package com.google.samples.apps.abelana;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


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

    /**
     * A placeholder fragment containing a simple view.
     */
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

            wipeout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog builder = new AlertDialog.Builder(getActivity())
                            .setMessage("This action cannot be undone!")
                            .setTitle("Erase all your data?")
                            .setPositiveButton("Erase", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AbelanaClient client = new AbelanaClient();
                                    client.mWipeout.wipeout(Data.aTok, new Callback<AbelanaClient.Status>() {

                                        @Override
                                        public void success(AbelanaClient.Status status, Response response) {
                                            Toast.makeText(getActivity(),
                                                    "Your data has been deleted.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            error.printStackTrace();
                                        }
                                    });

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

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
                    LoginActivity.client.resetAndSignOut();
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    //clear the backstack
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    return true;
                }
            });
        }
    }
}
