package com.google.samples.apps.abelana;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zafir on 10/15/14.
 */
final class Data {
    public static final List<String> mUrls = new ArrayList<String>();
    public static final List<String> mNames = new ArrayList<String>();
    public static final List<Integer> mLikes = new ArrayList<Integer>();
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

    public Data(String aTok) {
        AbelanaClient abelanaClient = new AbelanaClient();
        abelanaClient.mTimeline.timeline(aTok, "0", new Callback<AbelanaClient.TimelineResponse>() {
            @Override
            public void success(AbelanaClient.TimelineResponse timelineResponse, Response response) {
                for (AbelanaClient.TimelineEntry e: timelineResponse.entries) {
                    mUrls.add(AbelanaThings.getImage(e.photoid));
                    mLikes.add(e.likes);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
