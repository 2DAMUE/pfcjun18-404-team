package com.team.a404.a404team;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private EditText user, pass;
    private Button login;
    private TextView noCuenta, nopass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.pass1);
        noCuenta = (TextView) findViewById(R.id.nocuenta);
        nopass = (TextView) findViewById(R.id.nopass);
        login = (Button) findViewById(R.id.logbutt);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
//            Intent intent1 = new Intent(LoginActivity.this, MainScreen.class);
//            startActivity(intent1);

        }
        noCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        closeSoftKeyBoard();
        if (view == login) {
            userLogin();
        }
        if (view == nopass) {
//            Intent intent = new Intent(LoginActivity.this, ForgetPwd.class);
//            startActivity(intent);
        }
    }

    public void closeSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(login.getWindowToken(), 0);
    }

    private void userLogin() {
        String usr = user.getText().toString().trim();
        String pwd = pass.getText().toString().trim();


        if (TextUtils.isEmpty(usr)) {
            // email is empty
            Snackbar.make(login, getString(R.string.meteuser), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
            // para la ejecucion
        }
        if (TextUtils.isEmpty(pwd)) {
            // password is empty
            Snackbar.make(login, getString(R.string.metepass), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            // para la ejecucion
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(usr, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
//                            Intent intent1 = new Intent(LoginActivity.this, MainScreen.class);
//                            startActivity(intent1);
                        } else {
                            Snackbar.make(login, getString(R.string.nolog), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }

                    }
                });
    }
}

