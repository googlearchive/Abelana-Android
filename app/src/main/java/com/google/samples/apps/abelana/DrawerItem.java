package com.google.samples.apps.abelana;

/**
 * Created by zafir on 10/24/14.
 */
public class DrawerItem {
    private String title;
    private int resId;

    public DrawerItem(String title, int resId) {
        this.title = title;
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public int getResId() {
        return resId;
    }
}
