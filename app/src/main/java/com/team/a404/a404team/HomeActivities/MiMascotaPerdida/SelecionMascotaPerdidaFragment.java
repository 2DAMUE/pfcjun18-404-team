package com.team.a404.a404team.HomeActivities.MiMascotaPerdida;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.team.a404.a404team.Datos.DatosMascota;
import com.team.a404.a404team.R;

import java.util.ArrayList;

public class SelecionMascotaPerdidaFragment extends Fragment implements BlockingStep {

    private View mView;
    private ListView listaMascotas;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference DataRef;
    private ArrayList<DatosMascota> informacionMascotas = new ArrayList<>();
    private ArrayList<String> nombreMascotas = new ArrayList<>();
    private ArrayList<String> id_mascota_perfil = new ArrayList<>();
    private EditText telf_contacto;
    public static String v_id_mascota;
    public static String telefono;


    public SelecionMascotaPerdidaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_selecionar_mascota_perdida, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceStade) {
        super.onViewCreated(view, savedInstanceStade);
        informacionMascotas.clear();
        id_mascota_perfil.clear();
        nombreMascotas.clear();
        v_id_mascota = null;
        telf_contacto = (EditText) view.findViewById(R.id.telf_contact);
        CargarMascotasLista();

    }

    private void CargarMascotasLista() {
        DataRef = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("mascotas");
        listaMascotas = (ListView) mView.findViewById(R.id.listmascotas);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombreMascotas);
        listaMascotas.setAdapter(arrayAdapter);
        listaMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("INFO", " > " + id_mascota_perfil.get(i));
                telefono = telf_contacto.getText().toString().trim();
                if (!TextUtils.isEmpty(telefono)) {
                    v_id_mascota = id_mascota_perfil.get(i);
                } else {
                    Snackbar.make(listaMascotas, getString(R.string.toast_set_phone), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //Toast.makeText(getActivity(), getString(R.string.toast_set_phone), Toast.LENGTH_SHORT).show();
                }
            }
        });
        DataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatosMascota pet = dataSnapshot.getValue(DatosMascota.class);
                try {
                    if (pet.getMarker_id().length() == 0){
                        id_mascota_perfil.add(dataSnapshot.getKey());
                        nombreMascotas.add(pet.getNombre());
                        informacionMascotas.add(pet);
                    }
                }catch (Exception x){
                    Log.e("TRY",""+x);
                }


                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        if ((v_id_mascota != null)) {
            closeSoftKeyBoard();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.goToNextStep();
                }
            }, 0L);
        } else {
            Snackbar.make(listaMascotas, getString(R.string.toast_pet_select), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //Toast.makeText(getActivity(), getString(R.string.toast_pet_select), Toast.LENGTH_SHORT).show();
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