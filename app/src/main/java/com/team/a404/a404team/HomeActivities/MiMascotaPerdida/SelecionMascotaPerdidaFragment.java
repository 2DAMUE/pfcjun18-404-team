package com.team.a404.a404team.HomeActivities.MiMascotaPerdida;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.a404.a404team.R;

public class SelecionMascotaPerdidaFragment extends Fragment {

    private View mView;

    public SelecionMascotaPerdidaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_selecion_mascota_perdida, container, false);

        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceStade) {
        super.onViewCreated(view, savedInstanceStade);




    }
}