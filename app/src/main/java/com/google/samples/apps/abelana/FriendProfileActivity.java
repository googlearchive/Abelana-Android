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
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

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
        final TextView profileName = (TextView) findViewById(R.id.profile_text_1);
        AbelanaClient client = new AbelanaClient();
        client.mFProfile.fProfile(Data.aTok, personid, "0", new Callback<AbelanaClient.Timeline>() {
            @Override
            public void success(AbelanaClient.Timeline timeline, Response response) {
                Data.mFollowingProfileUrls = new ArrayList<String>();
                profileName.setText(timeline.entries[0].name + "'s profile");
                if (timeline.entries != null) {
                    for (AbelanaClient.TimelineEntry e : timeline.entries) {
                        Data.mFollowingProfileUrls.add(AbelanaThings.getImage(e.photoid));
                    }
                    //set the adapter for the gridview
                    gridView.setAdapter(new FriendProfileAdapter(getApplicationContext()));
                }
            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
