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
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Adapter used to display the main feed of photos.
 */
public class FeedAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mIds = new ArrayList<String>();
    private List<String> mNames = new ArrayList<String>();
    private List<Integer> mLikes = new ArrayList<Integer>();
    private List<Boolean> mILike = new ArrayList<Boolean>();
    private List<String> mFeedUrls = new ArrayList<String>();
    private int lastPosition = 0;

    private LayoutInflater mInflater;
    String LOG_TAG = FeedAdapter.class.getSimpleName();

    public FeedAdapter(Context context) {
        mContext = context;
        mIds = Data.mFeedIds;
        mLikes = Data.mLikes;
        mNames = Data.mNames;
        mILike = Data.mILike;
        mFeedUrls = Data.mFeedUrls;
    }

    @Override
    public int getCount() {
        return mIds.size();
    }

    @Override
    public String getItem(int position) {
        return mIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //Inflate the view for the layout of one feed item in the list
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_feed, null);
        }

        //Retrieve the image from the array at the given position, and load it with Picasso
        final String photoId = getItem(position);
        SquaredImageView imageView = (SquaredImageView) convertView.findViewById(R.id.feed_photo);
        String url = mFeedUrls.get(position);
        Log.v(LOG_TAG, "URL IS " + url);
        Picasso.with(mContext).load(url).into(imageView);

        /* To help make scrolling a bit smoother with less load time, warm the cache
         * by loading images above or below the current image depending on scroll direction.
         * We compute scroll direction by comparing the current position vs the last position
         */
        if (position >= lastPosition) {
            int index = position + 2;
            if (index < mFeedUrls.size()) {
                Picasso.with(mContext).load(mFeedUrls.get(index)).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // cache is now warmed up
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }
        } else {
            int index = position - 2;
            if (index >= 0) {
                Picasso.with(mContext).load(mFeedUrls.get(index)).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // cache is now warmed up
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }
        }

        //add the username
        String name = mNames.get(position);
        TextView namesView = (TextView) convertView.findViewById(R.id.feed_username);
        namesView.setText(name);

        //add the number of mLikes
        String numLikes = mLikes.get(position).toString();
        final TextView likesView = (TextView) convertView.findViewById(R.id.feed_likes);
        likesView.setText(numLikes + " likes");

        /* set like button - the behavior is set to allow a like of the photo, currently does not
         * implement unlike functionality
         */

        final ImageButton likeButton = (ImageButton) convertView.findViewById(R.id.like_button);

        if (mILike.get(position)) {
            likeButton.setImageResource(R.drawable.ic_favorite_active);
            //users can only like once, and cannot unlike
            likeButton.setClickable(false);
        } else {
            likeButton.setImageResource(R.drawable.ic_favorite_inactive);
            //add like button listener
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AbelanaClient abelanaClient = new AbelanaClient();
                    Log.v(LOG_TAG, "Button listener photo ID is " + photoId);
                    if (photoId != null) {
                        abelanaClient.mLike.like(Data.aTok, photoId, new Callback<AbelanaClient.Status>() {
                            @Override
                            public void success(AbelanaClient.Status status, Response response) {
                                String text = (String) likesView.getText();
                                int index = text.indexOf(" ");
                                int numLikes = Integer.parseInt(text.substring(0, index)) + 1;
                                mLikes.add(position, numLikes);
                                mILike.add(position, true);
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
        }

        //keep track of position to detect scrolling up vs down
        lastPosition = position;
        return convertView;
    }

}
