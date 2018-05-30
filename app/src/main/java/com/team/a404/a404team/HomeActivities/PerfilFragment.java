package com.team.a404.a404team.HomeActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.a404.a404team.R;


public class PerfilFragment extends Fragment {

    private View mView;
    private Toolbar toolbar;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_perfil_info, container, false);


        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceStade) {
        super.onViewCreated(view, savedInstanceStade);
        toolbar = (Toolbar) mView.findViewById(R.id.toolbar);





    }
}
