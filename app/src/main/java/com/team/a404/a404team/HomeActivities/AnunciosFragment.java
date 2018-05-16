package com.team.a404.a404team.HomeActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.a404.a404team.R;

public class AnunciosFragment extends Fragment {

    public AnunciosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_anuncios, container, false);
        return rootView;
    }
}
