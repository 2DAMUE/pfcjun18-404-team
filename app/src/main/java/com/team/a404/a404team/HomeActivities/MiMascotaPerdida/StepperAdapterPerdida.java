package com.team.a404.a404team.HomeActivities.MiMascotaPerdida;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

public class StepperAdapterPerdida extends AbstractFragmentStepAdapter {


    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";

    public StepperAdapterPerdida(FragmentManager fm, Context context) {
        super(fm, context);
    }


    @Override
    public Step createStep(int position) {
        switch (position){
            case 0:
                final SelecionMascotaPerdidaFragment step1 = new SelecionMascotaPerdidaFragment();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;

            case 1:
                final SelecionarUbicacionMapaFragment step2 = new SelecionarUbicacionMapaFragment();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
