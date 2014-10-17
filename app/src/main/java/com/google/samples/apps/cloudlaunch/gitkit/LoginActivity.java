package com.google.samples.apps.cloudlaunch.gitkit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.identitytoolkit.client.GitkitClient;
import com.google.identitytoolkit.model.Account;
import com.google.identitytoolkit.model.IdToken;
import com.google.samples.apps.cloudlaunch.FeedActivity;
import com.google.samples.apps.cloudlaunch.R;

/**
 * Gitkit Demo.
 */
public class LoginActivity extends FragmentActivity implements OnClickListener {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    public static GitkitClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Step 1: Create a GitkitClient.
        // The configurations are set in the AndroidManifest.xml. You can also set or overwrite them
        // by calling the corresponding setters on the GitkitClient builder.
        //

        client = GitkitClient.newBuilder(this, new GitkitClient.SignInCallbacks() {
            // Implement the onSignIn method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process succeeds. A Gitkit IdToken and the signed
            // in account information are passed to the callback.
            @Override
            public void onSignIn(IdToken idToken, Account account) {
                showProfilePage(idToken, account);
            }

            // Implement the onSignInFailed method of GitkitClient.SignInCallbacks interface.
            // This method is called when the sign-in process fails.
            @Override
            public void onSignInFailed() {
                Toast.makeText(LoginActivity.this, "Sign in failed", Toast.LENGTH_LONG).show();
            }
        }).build();


        // Step 2: Check if there is an already signed in user.
        // If there is an already signed in user, show the profile page and welcome message.
        // Otherwise, show a sign in button.
        //

        IdToken idToken = client.getSavedIdToken();
        Account account = client.getSavedAccount();
        if (idToken != null && account != null) {
            showProfilePage(idToken, account);
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
        if (!client.handleActivityResult(requestCode, resultCode, intent)) {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }


    // Step 4: Override the onNewIntent method.
    // When the app is invoked with an intent, it is possible that the intent is for GitkitClient.
    // Call GitkitClient.handleIntent to check it. If the intent is for GitkitClient, the method
    // returns true to indicate the intent has been consumed.

    @Override
    protected void onNewIntent(Intent intent) {
        if (!client.handleIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    public void showSignInPage() {
        setContentView(R.layout.fragment_main);
        Button button = (Button) findViewById(R.id.sign_in_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                client.startSignIn();
            }
        });

    }


    private void showProfilePage(IdToken idToken, Account account) {
        Log.v(LOG_TAG, "Token is: " + idToken.toString() + " Account is: " + account.toString());

        Intent feedIntent = new Intent(getApplicationContext(), FeedActivity.class);
        startActivity(feedIntent);

    }


    // Step 5: Respond to user actions.
    // If the user clicks sign in, call GitkitClient.startSignIn() to trigger the sign in flow.
    // If the user clicks sign out, call GitkitClient.signOut() to clear state.
    // If the user clicks manage account, call GitkitClient.manageAccount() to show manage
    // account UI.
    @Override
    public void onClick(View v) {

    }

}
