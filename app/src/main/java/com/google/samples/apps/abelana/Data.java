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

import java.util.ArrayList;
import java.util.List;

/**
 * Holds data objects centrally for use in various parts of the app
 */
final class Data {
    public static List<String> mFeedIds;
    public static List<String> mNames;
    public static List<Integer> mLikes;
    private final String LOG_TAG = Data.class.getSimpleName();
    public static String mEmail;
    public static String mDisplayName;
    public static List<String> mProfileUrls;
    public static List<String> mFollowingNames;
    public static List<String> mFollowingIds;
    public static List<String> mFollowingProfileUrls;
    public static List<Boolean> mILike;
    public static List<String> mFeedUrls;
    public static String sizeQualifier;
    public List<DrawerItem> mNavItems = new ArrayList<DrawerItem>();
    public static String aTok;
    public static String helpful;


    public Data() {
        mNavItems.add(new DrawerItem("Home", R.drawable.ic_home));
        mNavItems.add(new DrawerItem("My Profile", R.drawable.ic_profile));
        mNavItems.add(new DrawerItem("Following", R.drawable.ic_friends));
        mNavItems.add(new DrawerItem("Settings", R.drawable.ic_settings_inactive));
    }
}
