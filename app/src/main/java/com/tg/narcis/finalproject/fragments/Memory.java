package com.tg.narcis.finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.tg.narcis.finalproject.R;

public class Memory extends Fragment {

    View rootview;
    GridLayout grid;
    public Memory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_memory, container, false);
        grid = (GridLayout) rootview.findViewById(R.id.grid);


        return rootview;
    }

}
