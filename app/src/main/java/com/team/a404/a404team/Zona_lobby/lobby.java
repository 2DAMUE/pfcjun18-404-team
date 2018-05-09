package com.team.a404.a404team.Zona_lobby;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.team.a404.a404team.DetallesPerfilActivity;
import com.team.a404.a404team.MainActivities.MainMapActivity;
import com.team.a404.a404team.R;
import com.team.a404.a404team.SplashScreen;

public class lobby extends AppCompatActivity {

    protected Button v_btn_iniciarSesion,v_btn_registro;
    private FirebaseAuth firebaseAuth;
    private TextView nombreypass,cuentagoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        getSupportActionBar().hide();
        v_btn_iniciarSesion = (Button) findViewById(R.id.btn_iniciarSesion);
        v_btn_registro = (Button) findViewById(R.id.btn_registro);
        /*
        int color = Color.parseColor("#7C0032");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(color);
        }
        */
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent1 = new Intent(lobby.this, MainMapActivity.class);
            startActivity(intent1);

        }

        v_btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(lobby.this);
                dialog.setContentView(R.layout.login_dialog);
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
                nombreypass = (TextView)dialog.findViewById(R.id.userPass);
                cuentagoogle = (TextView)dialog.findViewById(R.id.googleAcc);
                nombreypass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(lobby.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in, R.anim.zoom_back_out);
                    }
                });
            }
        });
        v_btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(lobby.this, RegistroActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.zoom_back_out);
            }
        });
    }
}
