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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.a404.a404team.Zona_lobby.LoginActivity;

public class DetallesPerfilActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference DataRef;

    private EditText et_name, telefono;
    private Button btn_enviar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_perfil);
        et_name = (EditText) findViewById(R.id.name);
        telefono = (EditText) findViewById(R.id.numero);
        btn_enviar = (Button) findViewById(R.id.btn_enviar);

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_name.getText().toString().trim().length() > 0 && et_name.getText().toString().trim().length() <= 20) {
                    if (telefono.getText().toString().trim().length() > 0) {
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
        String name = et_name.getText().toString().trim();
        String telf = telefono.getText().toString().trim();
        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        DataRef.child("usuarios").child(usuario.getUid()).child("nombre").setValue(name);
        DataRef.child("usuarios").child(usuario.getUid()).child("telefono").setValue(telf);
        DataRef.child("usuarios").child(usuario.getUid()).child("email").setValue(usuario.getEmail());
        Toast.makeText(this, getString(R.string.guardadatos),Toast.LENGTH_LONG).show();

        Intent intent = new Intent(DetallesPerfilActivity.this,CreacionSuccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

}

