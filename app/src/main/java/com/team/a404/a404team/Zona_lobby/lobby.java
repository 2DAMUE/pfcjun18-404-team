package com.team.a404.a404team.Zona_lobby;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.team.a404.a404team.R;

public class lobby extends AppCompatActivity {

    protected Button v_btn_iniciarSesion,v_btn_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        getSupportActionBar().hide();
        v_btn_iniciarSesion = (Button) findViewById(R.id.btn_iniciarSesion);
        v_btn_registro = (Button) findViewById(R.id.btn_registro);
        int color = Color.parseColor("#7C0032");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(color);
        }

        v_btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(lobby.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
        v_btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(lobby.this, RegistroActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
