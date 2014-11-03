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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapater used to display the user's profile. Refer to FeedAdapter for a full explanation
 * for how these adapters work.
 */
public class ProfileAdapter extends BaseAdapter {
    private final String LOG_TAG = ProfileAdapter.class.getSimpleName();
    private Context mContext;
    private List<String> mUrls = new ArrayList<String>();
    private LayoutInflater mInflater;

    public ProfileAdapter(Context context) {
        mContext = context;
        mUrls = Data.mProfileUrls;
        Log.v("foo", "SIZE IS " + mUrls.size());
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
            convertView = mInflater.inflate(R.layout.list_item_profile, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.profile_photo);

        //add the image
        String url = getItem(position);
        String photoID = AbelanaThings.extractPhotoID(url);
        String qualifier = convertView.getResources().getString(R.string.size_qualifier);
        url = AbelanaThings.getImage(photoID + qualifier);
        Log.v(LOG_TAG, "URL IS " + url);
        Picasso.with(mContext).load(url).into(imageView);

        return imageView;
    }
}
