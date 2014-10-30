package com.google.samples.apps.abelana;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

    public List<DrawerItem> mNavItems = new ArrayList<DrawerItem>();
    public static String aTok;


    static String[] URLS = {
            AbelanaThings.getImage("0001"),
            AbelanaThings.getImage("0002"),
            AbelanaThings.getImage("0003"),
            AbelanaThings.getImage("0004"),
            AbelanaThings.getImage("0005"),
            AbelanaThings.getImage("0006"),
            AbelanaThings.getImage("0007"),
            AbelanaThings.getImage("0008"),
            AbelanaThings.getImage("0009")
    };


    static final String[] NAMES = {
            "Allen",
            "Billy",
            "Carl",
            "Dennis",
            "Ernest",
            "Francesc",
            "George",
            "Helen",
            "Ian"
    };

    static final Integer[] NUM_LIKES = {0, 1, 2, 3, 4, 5, 6, 7, 8
    };

    public static void getTimeline() {
        mClient = new AbelanaClient();

        mClient.mTimeline.timeline(aTok, "0", new Callback<AbelanaClient.Timeline>() {
            @Override
            public void success(AbelanaClient.Timeline timelineResponse, Response response) {
                for (AbelanaClient.TimelineEntry e: timelineResponse.entries) {
                    mFeedUrls.add(AbelanaThings.getImage(e.photoid));
                    mLikes.add(e.likes);
                    mNames.add(e.name);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public static void getProfile() {


        mClient.mGetMyProfile.getMyProfile(aTok, "0", new Callback<AbelanaClient.Timeline>() {
            @Override
            public void success(AbelanaClient.Timeline timeline, Response response) {
                for (AbelanaClient.TimelineEntry e: timeline.entries) {
                    Log.v("foo", "data returned!" );
                    mProfileUrls.add(AbelanaThings.getImage(e.photoid));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public static void getFProfile(String id) {
        mFollowingProfileUrls = new ArrayList<String>();
        mClient.mFProfile.fProfile(aTok, id, "0", new Callback<AbelanaClient.Timeline>() {
            @Override
            public void success(AbelanaClient.Timeline timeline, Response response) {
                for (AbelanaClient.TimelineEntry e: timeline.entries) {
                    mFollowingProfileUrls.add(AbelanaThings.getImage(e.photoid));
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static void getFollowing() {
        mClient.mGetFollowing.getFollowing(aTok, new Callback<AbelanaClient.Persons>() {
            @Override
            public void success(AbelanaClient.Persons persons, Response response) {
                for (AbelanaClient.Person p: persons.persons) {
                    mFollowingNames.add(p.name);
                    mFollowingUrls.add(AbelanaThings.getImage(p.personid));
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public Data(Context context) {
        mNavItems.add(new DrawerItem("Home", R.drawable.ic_home));
        mNavItems.add(new DrawerItem("My Profile", R.drawable.ic_profile));
        mNavItems.add(new DrawerItem("Following", R.drawable.ic_friends));
        mNavItems.add(new DrawerItem("Settings", R.drawable.ic_settings_inactive));
    }
}
