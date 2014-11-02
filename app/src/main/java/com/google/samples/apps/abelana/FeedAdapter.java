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

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by zafir on 10/15/14.
 */
public class FeedAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mUrls = new ArrayList<String>();
    private List<String> mNames = new ArrayList<String>();
    private List<Integer> mLikes = new ArrayList<Integer>();
    private List<Boolean> mILike = new ArrayList<Boolean>();
    private LayoutInflater mInflater;
    String LOG_TAG = FeedAdapter.class.getSimpleName();

    public FeedAdapter(Context context) {
        mContext = context;
        // Ensure we get a different ordering of images on each run.
        //Collections.addAll(mUrls, Data.URLS);
        mUrls = Data.mFeedUrls;
        mLikes = Data.mLikes;
        mNames = Data.mNames;
        mILike = Data.mILike;
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public String getItem(int position) {
        return mUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //set the view to the list item layout
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_feed, null);
        }

        //add the image
        String baseUrl = getItem(position);
        SquaredImageView imageView = (SquaredImageView) convertView.findViewById(R.id.feed_photo);
        String photoID = AbelanaThings.extractPhotoID(baseUrl);
        String qualifier = convertView.getResources().getString(R.string.size_qualifier);
        final String url = AbelanaThings.getImage(photoID + qualifier);
        Log.v(LOG_TAG, "URL IS " + url);
        Picasso.with(mContext).load(url).into(imageView);

        //add the username
        String name = mNames.get(position);
        TextView namesView = (TextView) convertView.findViewById(R.id.feed_username);
        namesView.setText(name);

        //add the number of mLikes
        String numLikes = mLikes.get(position).toString();
        final TextView likesView = (TextView) convertView.findViewById(R.id.feed_likes);
        likesView.setText(numLikes + " likes");

        //set like button
        final ImageButton likeButton = (ImageButton) convertView.findViewById(R.id.like_button);

        if (mILike.get(position)) {
            likeButton.setImageResource(R.drawable.ic_favorite_active);
            //users can only like once, and cannot unlike
            likeButton.setClickable(false);
        }

        //add like button listener
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbelanaClient abelanaClient = new AbelanaClient();
                String photoId = AbelanaThings.extractPhotoID(url);
                Log.v(LOG_TAG, "Button listener photo ID is " + photoId);
                if (photoId != null) {
                    abelanaClient.mLike.like(Data.aTok, photoId, new Callback<AbelanaClient.Status>() {
                        @Override
                        public void success(AbelanaClient.Status status, Response response) {
                            String text = (String) likesView.getText();
                            int index = text.indexOf(" ");
                            int numLikes = Integer.parseInt(text.substring(0, index)) + 1;
                            likesView.setText(numLikes + " likes");
                            likeButton.setImageResource(R.drawable.ic_favorite_active);
                            likeButton.setClickable(false);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                        }
                    });
                }
            }
        });

        return convertView;
    }

}
