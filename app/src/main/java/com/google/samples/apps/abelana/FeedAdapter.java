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
    private LayoutInflater mInflater;
    String LOG_TAG = FeedAdapter.class.getSimpleName();

    public FeedAdapter(Context context) {
        mContext = context;
        // Ensure we get a different ordering of images on each run.
        //Collections.addAll(mUrls, Data.URLS);
        mUrls = Data.mFeedUrls;
        mLikes = Data.mLikes;
        mNames = Data.mNames;
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
        final String url = getItem(position);
        SquaredImageView imageView = (SquaredImageView) convertView.findViewById(R.id.feed_photo);
        Picasso.with(mContext).load(url).into(imageView);

        //add the username
        String name = mNames.get(position);
        TextView namesView = (TextView) convertView.findViewById(R.id.feed_username);
        namesView.setText(name);

        //add the number of mLikes
        String numLikes = mLikes.get(position).toString();
        final TextView likesView = (TextView) convertView.findViewById(R.id.feed_likes);
        likesView.setText(numLikes + " likes");

        //add like button listener
        ImageButton likeButton = (ImageButton) convertView.findViewById(R.id.like_button);
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
                            int numLikes = Integer.parseInt(text.substring(0, 1)) + 1;
                            likesView.setText(numLikes + " likes");
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
