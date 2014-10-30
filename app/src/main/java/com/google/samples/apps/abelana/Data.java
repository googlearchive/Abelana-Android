package com.google.samples.apps.abelana;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zafir on 10/15/14.
 */
final class Data {
    public static List<String> mFeedUrls = new ArrayList<String>();
    public static List<String> mNames = new ArrayList<String>();
    public static List<Integer> mLikes = new ArrayList<Integer>();
    private final String LOG_TAG = Data.class.getSimpleName();
    public static String mEmail;
    public static String mDisplayName;
    public static List<String> mProfileUrls = new ArrayList<String>();
    public static List<String> mFollowingNames = new ArrayList<String>();
    public static AbelanaClient mClient;
    public static List<String> mFollowingUrls = new ArrayList<String>();
    public static List<String> mFollowingProfileUrls = new ArrayList<String>();
    public static List<Boolean> mILike = new ArrayList<Boolean>();

    public List<DrawerItem> mNavItems = new ArrayList<DrawerItem>();
    public static String aTok;


    public Data(Context context) {
        mNavItems.add(new DrawerItem("Home", R.drawable.ic_home));
        mNavItems.add(new DrawerItem("My Profile", R.drawable.ic_profile));
        mNavItems.add(new DrawerItem("Following", R.drawable.ic_friends));
        mNavItems.add(new DrawerItem("Settings", R.drawable.ic_settings_inactive));
    }
}
