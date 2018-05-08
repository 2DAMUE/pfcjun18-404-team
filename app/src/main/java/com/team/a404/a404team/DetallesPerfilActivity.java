package com.team.a404.a404team;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DetallesPerfilActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference DataRef;

    private EditText v_name, v_telefono;
    private Button v_btn_enviar;


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
        DataRef = FirebaseDatabase.getInstance().getReference();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(DetallesPerfilActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }
    private void SalvarDatos(){

        DataRef.addChildEventListener(new ChildEventListener() {
            String name = v_name.getText().toString().trim();
            String telf = v_telefono.getText().toString().trim();
            FirebaseUser usuario = firebaseAuth.getCurrentUser();
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                DataRef.child("usuarios").child(usuario.getUid()).child("nombre").setValue(name);
                DataRef.child("usuarios").child(usuario.getUid()).child("telefono").setValue(telf);
                DataRef.child("usuarios").child(usuario.getUid()).child("email").setValue(usuario.getEmail());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Intent intent = new Intent(DetallesPerfilActivity.this,CreacionSuccessActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Toast.makeText(this, "Enviando",Toast.LENGTH_LONG).show();

    }
    public EditText getV_name() {
        return v_name;
    }

    public EditText getV_telefono() {
        return v_telefono;
    }

}

