package com.google.samples.apps.cloudlaunch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zafir on 10/15/14.
 */
public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private final List<String> urls = new ArrayList<String>();

    public CustomAdapter(Context context) {
        mContext = context;

        // Ensure we get a different ordering of images on each run.
        Collections.addAll(urls, Data.URLS);
        Collections.shuffle(urls);

        // Triple up the list.
        ArrayList<String> copy = new ArrayList<String>(urls);
        urls.addAll(copy);
        urls.addAll(copy);
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SquaredImageView view = (SquaredImageView) convertView;
        if (view == null) {
            view = new SquaredImageView(mContext);
        }
        String url = getItem(position);

        Picasso.with(mContext).load(url).into(view);
        return view;
    }

}
