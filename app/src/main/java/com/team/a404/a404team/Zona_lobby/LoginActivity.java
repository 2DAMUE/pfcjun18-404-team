package com.team.a404.a404team.Zona_lobby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.team.a404.a404team.HomeActivities.HomeActivity;
import com.team.a404.a404team.R;
import com.team.a404.a404team.VentanasEstado.ActivitySuccess;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    private FirebaseAuth firebaseAuth;

    private EditText v_user, v_pass;
    private Button v_btn_login;
    private TextView nopass;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        v_user = (EditText) findViewById(R.id.edit_user);
        v_pass = (EditText) findViewById(R.id.edit_telefono);
        nopass = (TextView) findViewById(R.id.nopass);
        v_btn_login = (Button) findViewById(R.id.btn_enviar);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent1);
        }

        v_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Iniciando Sesión...");
                progressDialog.show();
                userLogin();
            }
        });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(LoginActivity.this, lobby.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        overridePendingTransition(R.anim.zoom_back_in,R.anim.right_out);
    }

    public void closeSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v_btn_login.getWindowToken(), 0);
    }

    private void userLogin() {
        String usr = v_user.getText().toString().trim();
        String pwd = v_pass.getText().toString().trim();


        if (TextUtils.isEmpty(usr)) {
            // email is empty
            progressDialog.hide();
            Snackbar.make(v_btn_login, getString(R.string.meteuser), Snackbar.LENGTH_LONG).setAction("Action", null).show();

            return;
            // para la ejecucion
        }
        if (TextUtils.isEmpty(pwd)) {
            // password is empty
            progressDialog.hide();
            Snackbar.make(v_btn_login, getString(R.string.metepass), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            // para la ejecucion
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(usr, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    progressDialog.hide();
                    closeSoftKeyBoard();
                    startActivity(new Intent(LoginActivity.this, ActivitySuccess.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    progressDialog.hide();
                    Snackbar.make(v_btn_login, getString(R.string.nolog), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

