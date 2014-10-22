package com.google.samples.apps.abelana;



import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


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
}
