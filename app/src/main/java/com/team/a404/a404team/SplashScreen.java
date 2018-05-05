package com.team.a404.a404team;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        Thread t = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    //lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
                    //lottieAnimationView.playAnimation();
                    sleep(1500);
                }catch (Exception e){

                } finally {
                    Intent myIntent = new Intent(SplashScreen.this, tipo_login.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(myIntent);
                }
            }
        };
        t.start();

    }
}
