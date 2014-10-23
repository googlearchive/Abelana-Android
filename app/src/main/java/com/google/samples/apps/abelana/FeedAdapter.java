package com.google.samples.apps.abelana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zafir on 10/15/14.
 */
public class FeedAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mUrls = new ArrayList<String>();
    private final List<String> mNames = new ArrayList<String>();
    private List<Integer> mLikes = new ArrayList<Integer>();
    private LayoutInflater mInflater;

    public FeedAdapter(Context context) {
        mContext = context;
        // Ensure we get a different ordering of images on each run.
        //Collections.addAll(mUrls, Data.URLS);
        mUrls = Data.mUrls;
        mLikes = Data.mLikes;
        Collections.addAll(mNames,Data.NAMES);


        // Triple up the list.
        ArrayList<String> copy = new ArrayList<String>(mUrls);
        ArrayList<String> copyNames = new ArrayList<String>(mNames);
        ArrayList<Integer> copyLikes = new ArrayList<Integer>(mLikes);
        mUrls.addAll(copy);
        mUrls.addAll(copy);
        mNames.addAll(copyNames);
        mNames.addAll(copyNames);
        mLikes.addAll(copyLikes);
        mLikes.addAll(copyLikes);
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
        String url = getItem(position);
        SquaredImageView imageView = (SquaredImageView) convertView.findViewById(R.id.feed_photo);
        Picasso.with(mContext).load(url).into(imageView);

        //add the username
        String name = mNames.get(position);
        TextView namesView = (TextView) convertView.findViewById(R.id.feed_username);
        namesView.setText(name);

        //add the number of mLikes
        String numLikes = mLikes.get(position).toString();
        TextView likesView = (TextView) convertView.findViewById(R.id.feed_likes);
        likesView.setText(numLikes + " likes");

        return convertView;
    }

}
