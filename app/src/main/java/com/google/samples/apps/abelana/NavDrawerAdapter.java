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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter to display data in the side navigation drawer. Refer to FeedAdapter for a full explanation
 * on how the adapters work
 */
public class NavDrawerAdapter extends ArrayAdapter<DrawerItem> {
    private Context mContext;
    private LayoutInflater mInflater;
    int mLayoutResource;
    List<DrawerItem> mDrawerItems;

    public NavDrawerAdapter(Context context, int resource, List<DrawerItem> drawerItems) {
        super(context, resource, drawerItems);
        mContext = context;
        mLayoutResource = resource;
        mDrawerItems = drawerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //set the view to the list item layout
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResource, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.navItemTitle);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.navItemIcon);

        //add the title and icon
        String name = mDrawerItems.get(position).getTitle();
        textView.setText(name);

        imageView.setImageResource(mDrawerItems.get(position).getResId());

        return convertView;
    }
}
