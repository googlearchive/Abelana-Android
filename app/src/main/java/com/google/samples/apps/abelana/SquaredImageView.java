package com.google.samples.apps.abelana;

/**
 * Created by zafir on 10/15/14.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

/** An image view which always remains square with respect to its width. */
final class SquaredImageView extends ImageView {
    private GestureDetector gestureDetector;
    private Context mContext;
    public SquaredImageView(Context context) {
        super(context);
        mContext = context;
    }

    public SquaredImageView(Context context, AttributeSet attrs) {

        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureListener());
        mContext = context;
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            Toast.makeText(mContext, "Double tap", Toast.LENGTH_SHORT).show();

            return true;
        }
    }
}