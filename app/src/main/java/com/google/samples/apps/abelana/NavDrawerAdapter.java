package com.google.samples.apps.abelana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zafir on 10/24/14.
 */
public class NavDrawerAdapter extends ArrayAdapter<DrawerItem> {
    private Context mContext;
    private final List<String> mUsers = new ArrayList<String>();
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
