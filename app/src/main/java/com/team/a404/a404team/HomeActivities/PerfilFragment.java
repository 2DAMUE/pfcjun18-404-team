package com.team.a404.a404team.HomeActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.a404.a404team.Datos.UserInformation;
import com.team.a404.a404team.HomeActivities.PerfilUsuario.PerfilUsuario;
import com.team.a404.a404team.R;
import com.team.a404.a404team.Zona_lobby.lobby;


public class PerfilFragment extends Fragment {

    private View mView;
    private Toolbar toolbar;
    private Button v_ir_editar_perfil, v_logout;
    private TextView textView2;

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
        textView2 = (TextView) mView.findViewById(R.id.textView2);
        FirebaseUser usero = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("usuarios/"+usero.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInformation user = dataSnapshot.getValue(UserInformation.class);
                Log.v("MSG",""+user.getUsername());
                //textView2.setText(user.getUsername());
                textView2.setText(user.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        v_ir_editar_perfil = (Button) mView.findViewById(R.id.ir_editar_perfil);
        v_ir_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), PerfilUsuario.class);
                startActivity(intent1);
            }
        });
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        v_logout = (Button) mView.findViewById(R.id.log_out_perfil);
        v_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        firebaseAuth.signOut();
                        Intent intent = new Intent(getContext(), lobby.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

            }
        });


    }
}
