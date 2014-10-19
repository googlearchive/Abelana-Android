package com.google.samples.apps.cloudlaunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zafir on 10/19/14.
 */
public class FriendsAdapter extends BaseAdapter {
    private Context mContext;
    private final List<String> mUsers = new ArrayList<String>();
    private LayoutInflater mInflater;

    public FriendsAdapter(Context context) {
        mContext = context;

        // Ensure we get a different ordering of images on each run.
        Collections.addAll(mUsers, Data.NAMES);

        // Triple up the list.
        ArrayList<String> copy = new ArrayList<String>(mUsers);
        mUsers.addAll(copy);
        mUsers.addAll(copy);
        mUsers.addAll(copy);
        mUsers.addAll(copy);
        mUsers.addAll(copy);
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public String getItem(int position) {
        return mUsers.get(position);
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
            convertView = mInflater.inflate(R.layout.list_item_friends, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textview_friend);

        //add the image
        String name = getItem(position);
        textView.setText(name);

        return convertView;
    }
}
