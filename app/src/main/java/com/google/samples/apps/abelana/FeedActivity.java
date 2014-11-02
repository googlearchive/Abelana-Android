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

import android.os.Bundle;
import android.preference.PreferenceManager;


public class FeedActivity extends BaseActivity {
    private final String LOG_TAG = FeedActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Must set content view before calling parent class in order for navigation drawer to work
        setContentView(R.layout.base_activity);
        super.onCreate(savedInstanceState);

        //Initializes the application with the proper default settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String aTok = extras.getString("aTok");
            AbelanaUser user = AbelanaThings.start(getApplicationContext(), aTok);
        }*/

        //Replace the current fragment with the Feed/Camera fragment via transaction
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FeedFragment())
                    .commit();
        }

//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int realWidth = metrics.widthPixels;
//        int realHeight = metrics.heightPixels;
//        //realWidth x RealHeight = 720x1184
//        float inchX = realWidth/metrics.xdpi;
//        float inchY = realHeight/metrics.ydpi;
//        //inchX x inchY = 2.28 X 3.75
//        int dpX = (int)(realWidth / metrics.scaledDensity);
//        int dpY = (int)(realHeight / metrics.scaledDensity);
//        //dpX x dpY = 360x592


    }
}
