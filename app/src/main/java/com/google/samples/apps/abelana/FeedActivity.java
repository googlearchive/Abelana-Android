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

//The central activity of the app.
public class FeedActivity extends BaseActivity {
    private final String LOG_TAG = FeedActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Must set content view before calling parent class in order for navigation drawer to work
        setContentView(R.layout.base_activity);
        super.onCreate(savedInstanceState);

        //Initializes the application with the proper default settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Replace the current fragment with the Feed/Camera fragment via transaction
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FeedFragment())
                    .commit();
        }

    }
}
