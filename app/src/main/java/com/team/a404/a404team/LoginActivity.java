package com.team.a404.a404team;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText user,pass;
    private Button login;
    private TextView noCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.pass1);
        noCuenta = (TextView) findViewById(R.id.nocuenta);
        login = (Button) findViewById(R.id.logbutt);

    }
}
