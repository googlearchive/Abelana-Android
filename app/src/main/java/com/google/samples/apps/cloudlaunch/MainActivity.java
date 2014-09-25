package com.google.samples.apps.cloudlaunch;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.samples.apps.cloudlaunch.gitkit.Gitkit;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        //Initializes the application with the proper default settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Button test = (Button) rootView.findViewById(R.id.sign_in_button);
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent gitkitIntent = new Intent(getActivity(), Gitkit.class);
                    startActivity(gitkitIntent);
                }
            });

            return rootView;
        }
    }
}
