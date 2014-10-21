package com.google.samples.apps.abelana;



import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FriendsFragment extends Fragment {


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_friends);

        //set the adapter for the friends listview
        listView.setAdapter(new FriendsAdapter(getActivity()));
        return rootView;
    }


}
