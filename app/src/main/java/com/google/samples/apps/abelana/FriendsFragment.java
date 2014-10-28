package com.google.samples.apps.abelana;


import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FriendsFragment extends Fragment {
    // Query listener object is part of the Action Bar search widget. Detects when a query is submitted.
    final private SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        // Required method, not used in our case
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }

        // Calls the searchRestaurants() method when a query is submitted
        @Override
        public boolean onQueryTextSubmit(String query) {
            Toast.makeText(getActivity(), "Searching for: " + query + "...", Toast.LENGTH_SHORT).show();
            return false;
        }
    };
    private final String LOG_TAG = FriendsFragment.class.getSimpleName();
    static final int PICK_CONTACT_REQUEST = 1;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_friends);
        setHasOptionsMenu(true);
        //set the adapter for the friends listview
        listView.setAdapter(new FriendsAdapter(getActivity()));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friends, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(queryListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
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
