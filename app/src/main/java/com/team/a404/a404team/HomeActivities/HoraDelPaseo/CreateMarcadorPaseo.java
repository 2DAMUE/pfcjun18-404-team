package com.team.a404.a404team.HomeActivities.HoraDelPaseo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.team.a404.a404team.R;

public class CreateMarcadorPaseo extends AppCompatActivity implements StepperLayout.StepperListener {

    private StepperLayout mStepperLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_marcador_perdida);
        getSupportActionBar().hide();
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapterPaseo(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {
        Toast.makeText(this, "onCompleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(VerificationError verificationError) {

        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }
}