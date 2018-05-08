package com.team.a404.a404team.Zona_lobby;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.team.a404.a404team.DetallesPerfilActivity;
import com.team.a404.a404team.R;

public class RegistroActivity extends AppCompatActivity {
    protected EditText v_mail, v_pass, nombre, telf;
    protected Button v_enviar;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference DataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();

        v_mail = (EditText) findViewById(R.id.edit_email);
        v_pass = (EditText) findViewById(R.id.edit_telefono);
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



        //if (password1.equals(password2)){
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
                        // se ha registrado
                        Intent intent = new Intent(RegistroActivity.this,DetallesPerfilActivity.class);
                        startActivity(intent);



                    } else {
                        Toast.makeText(RegistroActivity.this, getString(R.string.noregistro), Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }
            });
        /*
        }else{
            Toast.makeText(RegistroActivity.this, getString(R.string.nopass), Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
        */
    }
}
