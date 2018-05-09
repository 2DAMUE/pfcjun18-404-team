package com.team.a404.a404team.Zona_lobby;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.a404.a404team.CreacionSuccessActivity;
import com.team.a404.a404team.DetallesPerfilActivity;
import com.team.a404.a404team.MainActivities.MainMapActivity;
import com.team.a404.a404team.R;
import com.team.a404.a404team.SplashScreen;

public class RegistroActivity extends AppCompatActivity {
    protected EditText v_mail, v_pass, v_nombre;
    protected Button v_enviar;
    private FirebaseAuth firebaseAuth,firebaseAuth2;
    private DatabaseReference DataRef2;
    private FirebaseUser usuario,usuario2;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();

        v_mail = (EditText) findViewById(R.id.edit_email);
        v_pass = (EditText) findViewById(R.id.edit_telefono);
        v_nombre = (EditText) findViewById(R.id.edit_name);
        v_enviar = (Button) findViewById(R.id.btn_enviar);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        v_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(RegistroActivity.this, lobby.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(R.anim.zoom_back_in,R.anim.right_out);
    }


    private void registrar() {
        String email = v_mail.getText().toString().trim();
        String password1 = v_pass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // email is empty
            Toast.makeText(this, getString(R.string.meteuser), Toast.LENGTH_SHORT).show();
            // para la ejecucion
            return;
        }
        if (TextUtils.isEmpty(password1)) {
            // password is empty
            Toast.makeText(this, getString(R.string.metepass), Toast.LENGTH_SHORT).show();
            // para la ejecucion
            return;
        }

        progressDialog.setMessage(getString(R.string.registrando));
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Perfil creado , agregamos en nombre y correo.
                    SalvarDatos();

                } else {
                    Toast.makeText(RegistroActivity.this, getString(R.string.noregistro), Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        });

    }
    private void SalvarDatos(){

        firebaseAuth2 = FirebaseAuth.getInstance();
        usuario2 = firebaseAuth2.getCurrentUser();
        DataRef2 = FirebaseDatabase.getInstance().getReference().child("usuarios").child(usuario2.getUid());

        final String name = v_nombre.getText().toString().trim();

        DataRef2.child("nombre").setValue(name);
        DataRef2.child("email").setValue(usuario2.getEmail());

        Log.v("Esto","2 "+usuario2.getEmail());

        progressDialog.hide();
        startActivity(new Intent(RegistroActivity.this, MainMapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
