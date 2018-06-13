package com.team.a404.a404team.HomeActivities.HoraDelPaseo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.team.a404.a404team.R;

public class PaseoUnoFragment extends Fragment implements BlockingStep {

    private View mView;
    private EditText v_text_comentario;
    public static String comentario = "";

    public PaseoUnoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_selecionar_tiempo_paseo, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceStade) {
        super.onViewCreated(view, savedInstanceStade);
        v_text_comentario = (EditText) view.findViewById(R.id.p_text_comentario);

    }

    public void closeSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    /**
     * CONTROLADOR PARA PASAR LAS VENTANAS
     */


    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        closeSoftKeyBoard();
        comentario = String.valueOf(v_text_comentario.getText());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
            }
        }, 0L);


    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        getActivity().finish();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}