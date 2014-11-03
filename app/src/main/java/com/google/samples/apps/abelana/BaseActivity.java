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

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.identitytoolkit.GitkitUser;
import com.squareup.picasso.Picasso;

/**
 * The BaseActivity is primarily responsible for implementing the Android navigation drawer.
 * It is the BaseActivity that other activities can extend. In this case, FeedActivity serves
 * as an extension.
 * Code for the navigation drawer adapted from the sample at
 * http://developer.android.com/training/implementing-navigation/nav-drawer.html
 */
public class BaseActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavItems;

    /* Navigation Drawer menu items start indexing at 1, because the header view is interpreted
     * as the 0th element, though it is not tappable.
     */
    protected static final int NAVDRAWER_ITEM_HOME = 1;
    protected static final int NAVDRAWER_ITEM_PROFILE = 2;
    protected static final int NAVDRAWER_ITEM_FOLLOWING = 3;
    protected static final int NAVDRAWER_ITEM_SETTINGS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle = mDrawerTitle = getTitle();
        mNavItems = getResources().getStringArray(R.array.nav_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        //initialize Data class to retrieve navigation drawer icons and text
        Data data = new Data();

        /* Add the header to the navigation drawer. Pulls the user's name, email address
         * and photo from the Google Identity Toolkit. The photo is placed into a custom
         * ImageView which gives the circle effect. Image loading over the network is done
         * with Picasso, a library from Square.
         */
        View header = View.inflate(this, R.layout.navdrawer_header, null);
        BezelImageView imageView = (BezelImageView) header.findViewById(R.id.profile_image);
        UserInfoStore client = new UserInfoStore(getApplicationContext());
        GitkitUser user = client.getSavedGitkitUser();
        Log.v("foo", "user is " + user);
        Picasso.with(getApplicationContext()).load(client.getSavedGitkitUser().getPhotoUrl())
                .into(imageView);
        TextView email = (TextView) header.findViewById(R.id.profile_email_text);
        email.setText(Data.mEmail);
        TextView name = (TextView) header.findViewById(R.id.profile_name_text);
        name.setText(Data.mDisplayName);
        // set up the drawer's list view with mNavItems and click listener
        mDrawerList.addHeaderView(header, null, false);
        mDrawerList.setAdapter(new NavDrawerAdapter(getApplicationContext(), R.layout.list_item_navdrawer,
                data.mNavItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description for accessibility */
                R.string.app_name  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(NAVDRAWER_ITEM_HOME);
        }
    }


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action mNavItems related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);

        }
    }

    /* Handles the flow from tapping items in the navigation drawer.
     * All new screens come from fragment transactions. Settings is an exception
     * because of the way Settings are implemented in Android.
     */
    private void selectItem(int position) {
        FragmentManager fragmentManager = getFragmentManager();

        if (position == NAVDRAWER_ITEM_SETTINGS) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        if (position == NAVDRAWER_ITEM_FOLLOWING) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_frame, new FriendsFragment())
            .commit();
        }
        if (position == NAVDRAWER_ITEM_HOME) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_frame, new FeedFragment())
                    .commit();
        }
        if (position == NAVDRAWER_ITEM_PROFILE) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_frame, new ProfileFragment())
                    .commit();
        }

        //update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}