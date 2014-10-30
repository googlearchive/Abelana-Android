package com.google.samples.apps.abelana;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FriendProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        final GridView gridView = (GridView) findViewById(R.id.gridview);
        Intent intent = getIntent();
        String personid = intent.getStringExtra("id");
        AbelanaClient client = new AbelanaClient();
        client.mFProfile.fProfile(Data.aTok, personid, "0", new Callback<AbelanaClient.Timeline>() {
            @Override
            public void success(AbelanaClient.Timeline timeline, Response response) {
                Data.mFollowingProfileUrls = new ArrayList<String>();
                for (AbelanaClient.TimelineEntry e: timeline.entries) {
                    Data.mFollowingProfileUrls.add(AbelanaThings.getImage(e.photoid));
                }
                //set the adapter for the gridview
                gridView.setAdapter(new ProfileAdapter(getApplicationContext()));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
