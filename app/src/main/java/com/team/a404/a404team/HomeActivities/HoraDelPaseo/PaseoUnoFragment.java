package com.team.a404.a404team.HomeActivities.HoraDelPaseo;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.team.a404.a404team.R;

import java.util.Date;

public class PaseoUnoFragment extends Fragment implements BlockingStep {

    private View mView;
    public static String v_id_mascota;
    private LinearLayout v_grupo_de,v_grupo_hasta;
    private TextView v_text_de,v_text_hasta;
    private int hora_x,min_x;
    private String time_min = "";

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

        v_grupo_de = (LinearLayout) view.findViewById(R.id.grupo_de);
        v_text_de = (TextView) view.findViewById(R.id.text_de);
        v_grupo_hasta = (LinearLayout) view.findViewById(R.id.grupo_hasta);
        v_text_hasta = (TextView) view.findViewById(R.id.text_hasta);

        v_text_de.setText(MiHora(0));
        v_text_hasta.setText(MiHora(1));

        v_grupo_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hora_x = 0;
                min_x = 0;
                onCreateDialog(2).show();
                v_text_de.setText(hora_x +" : "+ParsearMin(min_x));
            }
        });

        v_grupo_hasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hora_x = 0;
                min_x = 0;
                onCreateDialog(2).show();
                v_text_hasta.setText(hora_x +" : "+ParsearMin(min_x));
            }
        });
    }



    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 2:
                return new TimePickerDialog(getActivity(), TimeLisen, hora_x, min_x, true);
        }
        return null;
    }


    public String MiHora(int i){
        Date date = new Date();

        if (i == 0){
            return date.getHours() +" : "+ParsearMin(date.getMinutes());
        }else{
            return date.getHours() +" : "+ParsearMin(date.getMinutes()+15);
        }


    }


    private TimePickerDialog.OnTimeSetListener TimeLisen = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int hora, int min) {
            hora_x = hora;
            min_x = min;
        }
    };

    public String ParsearMin(int min){

        if (min == 0){
            time_min = min+"0";
        }else if (min > 0 && min < 10){
            time_min = "0"+min;
        }else{
            time_min = ""+min;
        }
        return time_min;
    }



    /**
     * CONTROLADOR PARA PASAR LAS VENTANAS
     */


    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        
        if ((v_id_mascota != null)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.goToNextStep();
                }
            }, 0L);
        } else {
            Toast.makeText(getActivity(), "Seleccione una mascota", Toast.LENGTH_SHORT).show();
        }


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