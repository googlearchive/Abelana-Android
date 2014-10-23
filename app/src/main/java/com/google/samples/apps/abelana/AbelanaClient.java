package com.google.samples.apps.abelana;


import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by zafir on 10/16/14.
 */
public class AbelanaClient {
    private static final String API_URL = "https://endpoints-dot-abelana-222.appspot.com";
    private static final String LOG_TAG = AbelanaClient.class.getSimpleName();
    public static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .build();


    interface Refresh {
        @GET("/user/{atok}/refresh")
        JSONObject refresh(
                @Path("atok") String atok
        );
    }


    interface Wipeout {
        @DELETE("/user/{atok}")
        JSONObject wipeout(
                @Path("atok") String atok
        );
    }

    interface ImportFacebook {
        @POST("/user/{atok}/facebook/{fbkey}")
        JSONObject importResponse(
                @Path("atok") String atok,
                @Path("fbkey") String fbkey
        );
    }

    interface ImportGPlus {
        @POST("/user/{atok}/plus/{plkey}")
        JSONObject importResponse(
                @Path("atok") String atok,
                @Path("plkey") String plkey
        );
    }

    interface ImportYahoo {
        @POST("/user/{atok}/yahoo/{ykey}")
        JSONObject importResponse(
                @Path("atok") String atok,
                @Path("ykey") String ykey
        );
    }

    interface FriendsList {
        @GET("/user/{atok}/friend")
        JSONObject friends(
                @Path("atok") String atok
        );
    }

    interface UserPhoto {
        @GET("/user/{atok}/photo")
        JSONObject photo(
                @Path("atok") String atok
        );
    }

    interface AddFriend {
        @PUT("/user/{atok}/friend/{friendid}")
        JSONObject friendResponse(
                @Path("atok") String atok,
                @Path("friendid") String friendid
        );
    }

    interface GetFriend {
        @GET("/user/{atok}/friend/{friendid}")
        JSONObject photo(
                @Path("atok") String atok,
                @Path("friendid") String friendid
        );
    }

    interface Register {
        @PUT("/user/{atok}/device/{regid}")
        JSONObject registerResponse(
                @Path("atok") String atok,
                @Path("regid") String regid
        );
    }

    interface Unregister {
        @DELETE("/user/{atok}/device/{regid}")
        JSONObject registerResponse(
                @Path("atok") String atok,
                @Path("regid") String regid
        );
    }

    interface MyProfile {
        @GET("/user/{atok}/profile/{lastid}")
        JSONObject profile(
                @Path("atok") String atok,
                @Path("lastid") String lastid
        );
    }

    interface FriendsProfile {
        @GET("/user/{atok}/friend/{friendid}/profile/{lastid}")
        JSONObject profile(
                @Path("atok") String atok,
                @Path("friendid") String friendid,
                @Path("lastid") String lastid
        );
    }

    interface AddComment {
        @POST("/photo/{atok}/{photoid}/comment")
        JSONObject commentResponse(
                @Path("atok") String atok,
                @Path("photoid") String photoid
        );
    }

    interface Like {
        @PUT("/photo/{atok}/{photoid/like}/comment")
        JSONObject likeResponse(
                @Path("atok") String atok,
                @Path("photoid") String photoid
        );
    }

    interface Unlike {
        @DELETE("/photo/{atok}/{photoid/like}/comment")
        JSONObject likeResponse(
                @Path("atok") String atok,
                @Path("photoid") String photoid
        );
    }

    interface GetComments {
        @GET("/photo/{atok}/{photoid}/comments")
        JSONObject comments(
                @Path("atok") String atok,
                @Path("photoid") String photoid
        );
    }

    static class TimelineEntry {
        Long created;
        String userid;
        String photoid;
        int likes;
    }

    static class TimelineResponse {
        String kind;
        TimelineEntry[] entries;
    }
    interface Timeline {
        @GET("/user/{atok}/timeline/{lastid}")
        void timeline(
                @Path("atok") String atok,
                @Path("lastid") String lastid,
                Callback<TimelineResponse> callback
        );
    }

    Timeline mTimeline = restAdapter.create(Timeline.class);

    static class LoginResponse {
        String kind;
        String atok;
    }

    interface Login {
        @GET("/user/{gittok}/login/{displayName}/{photoUrl}")
        void login(
                @Path("gittok") String gittok,
                @Path("displayName") String displayName,
                @Path("photoUrl") String photoUrl,
                Callback<LoginResponse> callback
        );
    }
    Login mLogin = restAdapter.create(Login.class);


    public static void main(String... args) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        Timeline abelanaTimeline = restAdapter.create(Timeline.class);

        //Response timelineResponse = abelanaTimeline.timeline("LES001", "0");
        //System.out.println(timelineResponse);

        abelanaTimeline.timeline("LES001", "0", new Callback<TimelineResponse>() {
            @Override
            public void success(TimelineResponse timelineResponse, Response response) {
                System.out.println(timelineResponse.kind);
                System.out.println(timelineResponse.entries[0].photoid);
                TimelineEntry[] arr = timelineResponse.entries;
                for (TimelineEntry e: arr) {
                    System.out.println(e.likes);
                    System.out.println(e.photoid);
                }
                //System.out.println(arr.length);
            }

            @Override
            public void failure(RetrofitError error) {
                error.getStackTrace();
            }
        });


        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
























