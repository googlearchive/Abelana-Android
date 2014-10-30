package com.google.samples.apps.abelana;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FriendsFragment extends Fragment {
    private final String LOG_TAG = FriendsFragment.class.getSimpleName();
    static final int PICK_CONTACT_REQUEST = 1;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.listview_friends);
        setHasOptionsMenu(true);
        AbelanaClient client = new AbelanaClient();
        client.mGetFollowing.getFollowing(Data.aTok, new Callback<AbelanaClient.Persons>() {
            @Override
            public void success(AbelanaClient.Persons persons, Response response) {
                Data.mFollowingNames = new ArrayList<String>();
                Data.mFollowingUrls = new ArrayList<String>();
                for (AbelanaClient.Person p: persons.persons) {
                    Data.mFollowingNames.add(p.name);
                    Data.mFollowingUrls.add(AbelanaThings.getImage(p.personid));
                }

                //set the adapter for the friends listview
                listView.setAdapter(new FriendsAdapter(getActivity()));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String url = Data.mFollowingUrls.get(position);
                String personId = AbelanaThings.extractPhotoID(url);
                Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
                intent.putExtra("id", personId);
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

        if (id == R.id.action_find_friends) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
            startActivityForResult(intent, PICK_CONTACT_REQUEST);
        }

        return super.onOptionsItemSelected(item);

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
                AbelanaClient abelanaClient = new AbelanaClient();
                abelanaClient.mFollow.follow(Data.aTok, email, new Callback<AbelanaClient.Status>() {
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
}
