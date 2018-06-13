package com.team.a404.a404team.Zona_lobby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.a404.a404team.R;
import com.team.a404.a404team.VentanasEstado.ActivitySuccess;

public class RegistroActivity extends AppCompatActivity {
    protected EditText v_mail, v_pass, v_nombre;
    protected Button v_btn_enviar;
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
        v_btn_enviar = (Button) findViewById(R.id.btn_enviar);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        v_btn_enviar.setOnClickListener(new View.OnClickListener() {
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

    public void closeSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v_mail.getWindowToken(), 0);
    }


    private void registrar() {
        String email = v_mail.getText().toString().trim();
        String pass = v_pass.getText().toString().trim();
        String name = v_nombre.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            // email is empty
            Snackbar.make(v_btn_enviar, getString(R.string.meteuser), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            // para la ejecucion
            return;
        }

        if (TextUtils.isEmpty(email)) {
            // email is empty
            Snackbar.make(v_btn_enviar, getString(R.string.metemail), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            // para la ejecucion
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            // password is empty
            Snackbar.make(v_btn_enviar, getString(R.string.metepass), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            // para la ejecucion
            return;
        }
        try{
            progressDialog.setMessage(getString(R.string.registrando));
            progressDialog.show();
        }catch (Exception x){
            Log.v("Error TRY"," > "+x);
        }


        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Perfil creado , agregamos en nombre y correo.
                    SalvarDatos();
                    closeSoftKeyBoard();
                    progressDialog.hide();
                    startActivity(new Intent(RegistroActivity.this, ActivitySuccess.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                } else {
                    Snackbar.make(v_btn_enviar, getString(R.string.noregistro), Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

    }
}
