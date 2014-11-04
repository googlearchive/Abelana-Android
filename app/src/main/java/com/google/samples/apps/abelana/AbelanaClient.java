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
 * Uses Square Retrofit to turn Cloud Platform REST APIs into Java interfaces
 * See documentation at http://square.github.io/retrofit/
 */
public class AbelanaClient {
    //base URL for all the endpoints
    private static final String API_URL = "https://endpoints-dot-abelana-222.appspot.com";
    private static final String LOG_TAG = AbelanaClient.class.getSimpleName();

    //adapter used to build all the REST adapters
    public static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL).setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    /*Create static classes for interface callback methods. These classes match the return
    * the return type of the various APIs we define. Note: not all of these are in use by the
    * current iteration of the app.
     */
    static class ATOKJson {
        String kind;
        String atok;
    }

    static class Status {
        String kind;
        String status;
    }

    static class Persons {
        String kind;
        Person[] persons;
    }

    static class Person {
        String kind;
        String personid;
        String email;
        String name;
    }

    static class TimelineEntry {
        Long created;
        String userid;
        String name;
        String photoid;
        int likes;
        boolean ilike;
    }

    static class Comment {
        String personid;
        String text;
        Long time;
    }

    static class Comments {
        String kind;
        Comment[] entries;
    }

    static class Timeline {
        String kind;
        TimelineEntry[] entries;
    }

    static class Stats {
        int following;
        int followers;
    }

    //Create all the interfaces for the REST endpoints.
    interface Login {
        @GET("/user/{gittok}/login/{displayName}/{photoUrl}")
        void login(
                @Path("gittok") String gittok,
                @Path("displayName") String displayName,
                @Path("photoUrl") String photoUrl,
                Callback<ATOKJson> callback
        );
    }

    interface Refresh {
        @GET("/user/{atok}/refresh")
        void refresh(
                @Path("atok") String atok,
                Callback<ATOKJson> callback
        );
    }

    interface GetSecretKey {
        @GET("/user/{atok}/useful")
        void getSecretKey(
                @Path("atok") String atok,
                Callback<Status> callback
        );
    }

    interface Wipeout {
        @DELETE("/user/{atok}")
        void wipeout(
                @Path("atok") String atok,
                Callback<Status> callback
        );
    }

    interface ImportFacebook {
        @POST("/user/{atok}/following/facebook/{fbkey}")
        void importResponse(
                @Path("atok") String atok,
                @Path("fbkey") String fbkey,
                Callback<Status> callback
        );
    }

    interface ImportGPlus {
        @POST("/user/{atok}/following/plus/{plkey}")
        void importResponse(
                @Path("atok") String atok,
                @Path("plkey") String plkey,
                Callback<Status> callback
        );
    }

    interface ImportYahoo {
        @POST("/user/{atok}/following/yahoo/{ykey}")
        void importResponse(
                @Path("atok") String atok,
                @Path("ykey") String ykey,
                Callback<Status> callback
        );
    }

    interface GetFollowing {
        @GET("/user/{atok}/following")
        void getFollowing(
                @Path("atok") String atok,
                Callback<Persons> callback
        );
    }

    interface FollowByID {
        @PUT("/user/{atok}/following/{personid}")
        void addFriend(
                @Path("atok") String atok,
                @Path("personid") String personid,
                Callback<Status> callback
        );
    }

    interface GetPerson {
        @GET("/user/{atok}/following/{personid}")
        void getFriend(
                @Path("atok") String atok,
                @Path("personid") String personid,
                Callback<Person> callback
        );
    }

    interface Follow {
        @PUT("/user/{atok}/follow/{email}")
        void follow(
          @Path("atok") String atok,
          @Path("email") String email,
          Callback<Status> callback
        );
    }

    interface Register {
        @PUT("/user/{atok}/device/{regid}")
        void register(
                @Path("atok") String atok,
                @Path("regid") String regid,
                Callback<Status> callback
        );
    }

    interface Unregister {
        @DELETE("/user/{atok}/device/{regid}")
        void unregister(
                @Path("atok") String atok,
                @Path("regid") String regid,
                Callback<Status> callback
        );
    }

    interface GetTimeline {
        @GET("/user/{atok}/timeline/{lastid}")
        void timeline(
                @Path("atok") String atok,
                @Path("lastid") String lastid,
                Callback<Timeline> callback
        );
    }

    interface GetMyProfile {
        @GET("/user/{atok}/profile/{lastdate}")
        void getMyProfile(
                @Path("atok") String atok,
                @Path("lastdate") String lastdate,
                Callback<Timeline> callback
        );
    }

    interface FProfile {
        @GET("/user/{atok}/following/{personid}/profile/{lastdate}")
        void fProfile(
                @Path("atok") String atok,
                @Path("personid") String personid,
                @Path("lastdate") String lastdate,
                Callback<Timeline> callback
        );
    }

    interface SetPhotoComments {
        @POST("/photo/{atok}/{photoid}/comment/{text}")
        void setPhotoComments(
                @Path("atok") String atok,
                @Path("photoid") String photoid,
                @Path("text") String text,
                Callback<Status> callback
        );
    }

    interface Like {
        @PUT("/photo/{atok}/{photoid}/like")
        void like(
                @Path("atok") String atok,
                @Path("photoid") String photoid,
                Callback<Status> callback
        );
    }

    interface Unlike {
        @DELETE("/photo/{atok}/{photoid}/like")
        void unlike(
                @Path("atok") String atok,
                @Path("photoid") String photoid,
                Callback<Status> callback
        );
    }

    interface Flag {
        @GET("/photo/{atok}/{photoid}/flag")
        void flag(
                @Path("atok") String atok,
                @Path("photoid") String photoid,
                Callback<Status> callback
        );
    }

    interface GetComments {
        @GET("/photo/{atok}/{photoid}/comments")
        void getComments(
                @Path("atok") String atok,
                @Path("photoid") String photoid,
                Callback<Comments> callback
        );
    }


    interface PostPhoto {
        @POST("/photopush/{superid}")
        void postPhoto(
          @Path("superid") String superid,
          Callback<Void> callback
        );
    }

    interface Statistics {
        @GET("/user/{atok}/stats")
        void statistics(
          @Path("atok") String atok,
          Callback<Stats> callback
        );
    }

    /*Create the REST adapters. This is what you will use to instantiate the API calls
     * in the rest of the app (see the other classes for examples of how they are used)
     */
    Login mLogin = restAdapter.create(Login.class);
    Wipeout mWipeout = restAdapter.create(Wipeout.class);
    GetFollowing mGetFollowing = restAdapter.create(GetFollowing.class);
    GetTimeline mTimeline = restAdapter.create(GetTimeline.class);
    GetMyProfile mGetMyProfile = restAdapter.create(GetMyProfile.class);
    FProfile mFProfile = restAdapter.create(FProfile.class);
    Like mLike = restAdapter.create(Like.class);
    Unlike mUnlike = restAdapter.create(Unlike.class);
    Follow mFollow = restAdapter.create(Follow.class);
    Statistics mStatistics = restAdapter.create(Statistics.class);

    //Not using these APIs yet
    Refresh mRefresh = restAdapter.create(Refresh.class);
    GetSecretKey mGetSecretKey = restAdapter.create(GetSecretKey.class);
    FollowByID mFollowById = restAdapter.create(FollowByID.class);
    GetPerson mGetPerson = restAdapter.create(GetPerson.class);
    Register mRegister = restAdapter.create(Register.class);
    Unregister mUnregister = restAdapter.create(Unregister.class);
    PostPhoto mPostPhoto = restAdapter.create(PostPhoto.class);
    ImportFacebook mImportFacebook = restAdapter.create(ImportFacebook.class);
    ImportGPlus mImportGPlus = restAdapter.create(ImportGPlus.class);
    ImportYahoo mImportYahoo = restAdapter.create(ImportYahoo.class);
    SetPhotoComments mSetPhotoComments = restAdapter.create(SetPhotoComments.class);
    Flag mFlag = restAdapter.create(Flag.class);
    GetComments mGetComments = restAdapter.create(GetComments.class);


    //You can test the Retrofit code here in main
    public static void main(String... args) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        GetTimeline abelanaTimeline = restAdapter.create(GetTimeline.class);


        abelanaTimeline.timeline("authentication token", "0", new Callback<Timeline>() {
            @Override
            public void success(Timeline timelineResponse, Response response) {
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
























