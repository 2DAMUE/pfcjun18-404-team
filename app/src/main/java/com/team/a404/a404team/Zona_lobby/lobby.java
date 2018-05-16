package com.team.a404.a404team.Zona_lobby;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.team.a404.a404team.HomeActivities.HomeActivity;
import com.team.a404.a404team.R;

public class lobby extends AppCompatActivity {

    protected Button v_btn_iniciarSesion,v_btn_registro,v_btn_iniciarSesionCorreo;
    private FirebaseAuth firebaseAuth;
    private SignInButton v_btn_iniciarSesionGoogle;
    private FrameLayout v_pantalla;

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
            Intent intent1 = new Intent(lobby.this, HomeActivity.class);
            startActivity(intent1);

        }

        v_btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog;
                // con este tema personalizado evitamos los bordes por defecto
                dialog = new Dialog(lobby.this, R.style.Theme_Dialog_Translucent);
                //deshabilitamos el título por defecto
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#86000000")));

                dialog.setContentView(R.layout.login_dialog);
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
                v_btn_iniciarSesionCorreo = (Button) dialog.findViewById(R.id.btn_iniciarPorCorreo);
                v_btn_iniciarSesionGoogle = (SignInButton)dialog.findViewById(R.id.btn_iniciarPorGoogle);
                v_pantalla = (FrameLayout) dialog.findViewById(R.id.pantalla);

                v_pantalla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                    }
                });

                v_btn_iniciarSesionCorreo.setOnClickListener(new View.OnClickListener() {
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
