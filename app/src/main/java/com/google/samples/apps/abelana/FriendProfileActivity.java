package com.google.samples.apps.abelana;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
                //set the adapter for the ic_profile gridview
                gridView.setAdapter(new ProfileAdapter(getApplicationContext()));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
