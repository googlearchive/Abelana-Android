package com.google.samples.apps.abelana;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        //set the adapter for the profile gridview
        gridView.setAdapter(new ProfileAdapter(getActivity()));
        return rootView;

    }
}
