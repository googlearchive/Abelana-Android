package com.google.samples.apps.abelana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zafir on 10/19/14.
 */
public class FriendsAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mFollowing = new ArrayList<String>();
    private LayoutInflater mInflater;
    private List<String> mFollowingUrls = new ArrayList<String>();

    public FriendsAdapter(Context context) {
        mContext = context;

        mFollowing = Data.mFollowingNames;
        mFollowingUrls = Data.mFollowingUrls;
    }

    @Override
    public int getCount() {
        return mFollowing.size();
    }

    @Override
    public String getItem(int position) {
        return mFollowing.get(position);
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

        //add the name
        String name = getItem(position);
        textView.setText(name);

        //add the image
        String photoUrl = mFollowingUrls.get(position);
        BezelImageView imageView = (BezelImageView) convertView.findViewById(R.id.friend_image);
        Picasso.with(mContext).load(photoUrl).into(imageView);


        return convertView;
    }
}
