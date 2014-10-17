package com.google.samples.apps.cloudlaunch;

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
        @GET("/user/{atok}/friends")
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
        @PUT("/user/{atok}/friends/{friendid}")
        JSONObject friendResponse(
                @Path("atok") String atok,
                @Path("friendid") String friendid
        );
    }

    interface GetFriend {
        @GET("/friend/{atok}/{friendid}")
        JSONObject photo(
                @Path("atok") String atok,
                @Path("friendid") String friendid
        );
    }

    interface Register {
        @PUT("/device/{atok}/{regid}")
        JSONObject registerResponse(
                @Path("atok") String atok,
                @Path("regid") String regid
        );
    }

    interface Unregister {
        @DELETE("/device/{atok}/{regid}")
        JSONObject registerResponse(
                @Path("atok") String atok,
                @Path("regid") String regid
        );
    }

    interface Timeline {
        @GET("/timeline/{atok}/{lastid}")
        Response timeline(
                @Path("atok") String atok,
                @Path("lastid") String lastid
        );
    }

    interface MyProfile {
        @GET("/profile/{atok}/{lastid}")
        JSONObject profile(
                @Path("atok") String atok,
                @Path("lastid") String lastid
        );
    }

    interface FriendsProfile {
        @GET("/profile/{atok}/{userid}/{lastid}")
        JSONObject profile(
                @Path("atok") String atok,
                @Path("userid") String userid,
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

    static class LoginResponse {
        String Status;
        String Atok;
    }

    interface Login {
        @GET("/login/{gittok}")
        void login(
                @Path("gittok") String gittok,
                Callback<LoginResponse> fred
        );
    }

    public static void main(String... args) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        Timeline abelanaTimeline = restAdapter.create(Timeline.class);

        Response timelineResponse = abelanaTimeline.timeline("LES001", "0");
        System.out.println(timelineResponse);

        Login test = restAdapter.create(Login.class);
        test.login("Les", new Callback<LoginResponse>() {
                    public void success(LoginResponse l, Response r) {
                        System.out.println(l.Status + " " + l.Atok);
                    }

                    public void failure(RetrofitError e) {
                        System.out.println("failure");
                    }
                }
        );

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
