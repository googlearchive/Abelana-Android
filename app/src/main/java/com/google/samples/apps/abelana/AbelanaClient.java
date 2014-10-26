package com.google.samples.apps.abelana;


import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by zafir on 10/16/14.
 */
public class AbelanaClient {
    //base URL for all the endpoints
    private static final String API_URL = "https://endpoints-dot-abelana-222.appspot.com";
    private static final String LOG_TAG = AbelanaClient.class.getSimpleName();
    //adapter used to build all the REST adapters
    public static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .build();

    //Create static classes for interface callback methods
    static class ATOKJson {
        String kind;
        String atok;
    }

    static class Status {
        String kind;
        String status;
    }

    static class FriendList {
        String kind;
        String[] friendid;
    }

    static class Friend {
        String kind;
        String friendid;
        String email;
        String name;
    }

    static class TimelineEntry {
        Long created;
        String userid;
        String photoid;
        int likes;
    }

    static class Comment {
        String friendid;
        String text;
    }

    static class Comments {
        String kind;
        Comment[] entries;
    }

    static class Timeline {
        String kind;
        TimelineEntry[] entries;
    }

    //Create all the interfaces for the REST endpoints
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
        @POST("/user/{atok}/facebook/{fbkey}")
        void importResponse(
                @Path("atok") String atok,
                @Path("fbkey") String fbkey,
                Callback<Status> callback
        );
    }

    interface ImportGPlus {
        @POST("/user/{atok}/plus/{plkey}")
        void importResponse(
                @Path("atok") String atok,
                @Path("plkey") String plkey,
                Callback<Status> callback
        );
    }

    interface ImportYahoo {
        @POST("/user/{atok}/yahoo/{ykey}")
        void importResponse(
                @Path("atok") String atok,
                @Path("ykey") String ykey,
                Callback<Status> callback
        );
    }

    interface GetFriendsList {
        @GET("/user/{atok}/friend")
        void getFriendsList(
                @Path("atok") String atok,
                Callback<FriendList> callback
        );
    }

    interface AddFriend {
        @PUT("/user/{atok}/friend/{friendid}")
        void addFriend(
                @Path("atok") String atok,
                @Path("friendid") String friendid,
                Callback<Status> callback
        );
    }

    interface GetFriend {
        @GET("/user/{atok}/friend/{friendid}")
        void getFriend(
                @Path("atok") String atok,
                @Path("friendid") String friendid,
                Callback<Friend> callback
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
        @GET("/user/{atok}/profile/{lastid}")
        void getMyProfile(
                @Path("atok") String atok,
                @Path("lastid") String lastid,
                Callback<Timeline> callback
        );
    }

    interface FriendProfile {
        @GET("/user/{atok}/friend/{friendid}/profile/{lastid}")
        void friendProfile(
                @Path("atok") String atok,
                @Path("friendid") String friendid,
                @Path("lastid") String lastid,
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

    //Create the REST adapters. The app will use this variables
    Login mLogin = restAdapter.create(Login.class);
    Refresh mRefresh = restAdapter.create(Refresh.class);
    GetSecretKey mGetSecretKey = restAdapter.create(GetSecretKey.class);
    Wipeout mWipeout = restAdapter.create(Wipeout.class);
    ImportFacebook mImportFacebook = restAdapter.create(ImportFacebook.class);
    ImportGPlus mImportGPlus = restAdapter.create(ImportGPlus.class);
    ImportYahoo mImportYahoo = restAdapter.create(ImportYahoo.class);
    GetFriendsList mGetFriendsList = restAdapter.create(GetFriendsList.class);
    AddFriend mAddFriend = restAdapter.create(AddFriend.class);
    GetFriend mGetFriend = restAdapter.create(GetFriend.class);
    Register mRegister = restAdapter.create(Register.class);
    Unregister mUnregister = restAdapter.create(Unregister.class);
    GetTimeline mTimeline = restAdapter.create(GetTimeline.class);
    GetMyProfile mGetMyProfile = restAdapter.create(GetMyProfile.class);
    FriendProfile mFriendProfile = restAdapter.create(FriendProfile.class);
    SetPhotoComments mSetPhotoComments = restAdapter.create(SetPhotoComments.class);
    Like mLike = restAdapter.create(Like.class);
    Unlike mUnlike = restAdapter.create(Unlike.class);
    Flag mFlag = restAdapter.create(Flag.class);
    GetComments mGetComments = restAdapter.create(GetComments.class);
    PostPhoto mPostPhoto = restAdapter.create(PostPhoto.class);


    //Testing harness for all the interfaces and endpoints
    public static void main(String... args) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

//        GetTimeline abelanaTimeline = restAdapter.create(GetTimeline.class);
//
//
//        abelanaTimeline.timeline("LES001", "0", new Callback<Timeline>() {
//            @Override
//            public void success(Timeline timelineResponse, Response response) {
//                System.out.println(timelineResponse.kind);
//                System.out.println(timelineResponse.entries[0].photoid);
//                TimelineEntry[] arr = timelineResponse.entries;
//                for (TimelineEntry e: arr) {
//                    System.out.println(e.likes);
//                    System.out.println(e.photoid);
//                }
//                //System.out.println(arr.length);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                error.getStackTrace();
//            }
//        });

//        Refresh abelanaRefresh = restAdapter.create(Refresh.class);
//
//        abelanaRefresh.refresh("LES001", new Callback<ATOKJson>() {
//            @Override
//            public void success(ATOKJson atokJson, Response response) {
//                System.out.println(atokJson.atok);
//                System.out.println(atokJson.kind);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
//
//        GetSecretKey abelanaGetSecretKey = restAdapter.create(GetSecretKey.class);
//
//        abelanaGetSecretKey.getSecretKey("LES001", new Callback<Status>() {
//            @Override
//            public void success(Status status, Response response) {
//                System.out.println(status.kind);
//                System.out.println(status.status);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
//
//        GetFriendsList abelanaGetFriendsList = restAdapter.create(GetFriendsList.class);
//
//        abelanaGetFriendsList.getFriendsList("LES001", new Callback<FriendList>() {
//            @Override
//            public void success(FriendList friendList, Response response) {
//                System.out.println(friendList.kind);
//                for (String s: friendList.friendid) {
//                    System.out.println(s);
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
//
//        GetFriend abelanaGetFriend = restAdapter.create(GetFriend.class);
//
//        abelanaGetFriend.getFriend("LES001", "00001", new Callback<Friend>() {
//            @Override
//            public void success(Friend friend, Response response) {
//                System.out.println(friend.email);
//                System.out.println(friend.friendid);
//                System.out.println(friend.kind);
//                System.out.println(friend.name);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });


        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
























