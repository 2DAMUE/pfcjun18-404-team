package com.team.a404.a404team;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.a404.a404team.Zona_lobby.LoginActivity;
import com.team.a404.a404team.Zona_lobby.lobby;

public class DetallesPerfilActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference DataRef;
    private FirebaseUser usuario;
    private estado e = new estado();
    private EditText v_name, v_telefono;
    private Button v_btn_enviar;
    private boolean enviado_1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_perfil);
        v_name = (EditText) findViewById(R.id.edit_name);
        v_telefono = (EditText) findViewById(R.id.edit_telefono);
        v_btn_enviar = (Button) findViewById(R.id.btn_enviar);

        getSupportActionBar().hide();



        v_btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviado_1 = false;
                if (v_name.getText().toString().trim().length() > 0 && v_name.getText().toString().trim().length() <= 20) {
                    if (v_telefono.getText().toString().trim().length() == 9) {
                        SalvarDatos();
                    } else {
                        Snackbar.make(view, "ERROR", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(view, "ERROR", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        usuario = firebaseAuth.getCurrentUser();
        DataRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(usuario.getUid());

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(DetallesPerfilActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }
    private void SalvarDatos(){

        final String name = v_name.getText().toString().trim();
        final String telf = v_telefono.getText().toString().trim();

        DataRef.orderByValue().limitToLast(2).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                DataRef.child("nombre").setValue(name);
                DataRef.child("telefono").setValue(telf);
                DataRef.child("email").setValue(usuario.getEmail());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                enviado_1 = true;
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


        Thread t_e = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    e.setV_estado(3);
                    //Toast.makeText(getBaseContext(), "Enviando....",Toast.LENGTH_LONG).show();
                    Log.v("-------- Entra","Enviando...");
                    Intent intent = new Intent(DetallesPerfilActivity.this,CreacionSuccessActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    sleep(10000);
                    if (enviado_1){
                        e.setV_estado(1);
                        Log.v("-------- Entra","Enviado");
                    }
                }catch (Exception e){
                    Log.v("-------- Error","------------------------");

                } finally {
                    if (!enviado_1){
                        e.setV_estado(2);
                        Log.v("-------- Entra","Sin Internet");
                    }
                }
            }
        };
        t_e.start();
    }

}

