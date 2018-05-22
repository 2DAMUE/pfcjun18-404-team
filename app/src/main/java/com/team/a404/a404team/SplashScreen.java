package com.team.a404.a404team;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.team.a404.a404team.HomeActivities.HomeActivity;
import com.team.a404.a404team.VentanasEstado.ActivitySuccess;
import com.team.a404.a404team.Zona_lobby.lobby;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();


        Thread t = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    //lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
                    //lottieAnimationView.playAnimation();
                    sleep(1000);
                }catch (Exception e){

                } finally {
                    startActivity(new Intent(SplashScreen.this, lobby.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        };
        t.start();

    }
}
