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
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Fragment used for the 'Following' tab
 */
public class FriendsFragment extends Fragment {
    private final String LOG_TAG = FriendsFragment.class.getSimpleName();
    static final int PICK_CONTACT_REQUEST = 1;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listview_friends);
        setHasOptionsMenu(true);

        //See FeedFragment for a full explanation of how these API calls work
        AbelanaClient client = new AbelanaClient();
        client.mGetFollowing.getFollowing(Data.aTok, new Callback<AbelanaClient.Persons>() {
            @Override
            public void success(AbelanaClient.Persons persons, Response response) {
                Data.mFollowingNames = new ArrayList<String>();
                Data.mFollowingIds = new ArrayList<String>();
                if (persons.persons != null) {
                    for (AbelanaClient.Person p : persons.persons) {
                        Data.mFollowingNames.add(p.name);
                        Data.mFollowingIds.add(p.personid);
                    }

                    //set the adapter for the friends listview
                    listView.setAdapter(new FriendsAdapter(getActivity()));
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String personId = Data.mFollowingIds.get(position);
                String personName = Data.mFollowingNames.get(position);
                Log.v(LOG_TAG, "Person id is " + personId);
                Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
                intent.putExtra("id", personId);
                intent.putExtra("name", personName);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friends, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Uses a built-in Android intent to display email addresses from your contacts
        if (id == R.id.action_find_friends) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
            startActivityForResult(intent, PICK_CONTACT_REQUEST);
        }
        /* Alternatively, the user can manually input an email address of a friend they'd like to follow.
         * Note, currently the user must already have the app for the follow to work.
         */
        if (id == R.id.manual_friend_search) {
            showDialog();
        }

        return super.onOptionsItemSelected(item);

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getResources().getString(R.string.friend_dialog_title));

        final EditText input = new EditText(getActivity());
        input.setHint(getActivity().getResources().getString(R.string.friend_dialog_hint));
        builder.setView(input);
        builder.setPositiveButton(getActivity().getResources().getString(R.string.friend_dialog_positive),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = input.getText().toString();
                sendFollowRequest(email);
            }
        });

        builder.setNegativeButton(getActivity().getResources().getString(R.string.friend_dialog_negative),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Canceled
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the EMAIL column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Email.ADDRESS};

                // Perform the query on the contact to get the EMAIL column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getActivity().getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the email from the EMAIL column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                final String email = cursor.getString(column);
                Log.v(LOG_TAG, "EMAIL IS " + email);
                // Do something with the email. In our case, send the follow request
                sendFollowRequest(email);
            }
        }

    }

    private void sendFollowRequest(final String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Follow request failed. Please enter a valid email address.",
                    Toast.LENGTH_SHORT).show();
        } else {
            //See FeedFragment for a full explanation on how these API calls work
            AbelanaClient abelanaClient = new AbelanaClient();
            abelanaClient.mFollow.follow(Data.aTok, Utilities.base64Encoding(email), new Callback<AbelanaClient.Status>() {
                @Override
                public void success(AbelanaClient.Status status, Response response) {
                    Toast.makeText(getActivity(), "Follow request sent to " + email + "!",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }
    }
}
