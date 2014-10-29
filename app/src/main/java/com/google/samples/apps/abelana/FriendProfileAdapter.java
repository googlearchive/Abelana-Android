package com.google.samples.apps.abelana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zafir on 10/29/14.
 */
public class FriendProfileAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mUrls = new ArrayList<String>();
    private LayoutInflater mInflater;

    public FriendProfileAdapter(Context context) {
        mContext = context;
        mUrls = Data.mFollowingProfileUrls;
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
        Picasso.with(mContext).load(url).into(imageView);

        return imageView;
    }
}
