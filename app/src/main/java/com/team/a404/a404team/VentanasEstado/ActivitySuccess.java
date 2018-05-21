package com.team.a404.a404team.VentanasEstado;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.team.a404.a404team.HomeActivities.HomeActivity;
import com.team.a404.a404team.R;

public class ActivitySuccess extends AppCompatActivity {

    private LottieAnimationView animationView;
    private FrameLayout v_fondo_color;
    public static int estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_success);
        getSupportActionBar().hide();
        v_fondo_color = (FrameLayout) findViewById(R.id.fondo_color);

        updateColor(Color.parseColor("#f1c40f"));

        animationView = (LottieAnimationView) findViewById(R.id.animationview);
        animationView.setAnimation("reload.json");
        animationView.loop(true);
        animationView.playAnimation();

        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                switch (estado){
                    case 1:
                        updateColor(Color.parseColor("#2ecc71"));
                        animationView.setAnimation("reload_OK.json");
                        animationView.playAnimation();
                        estado = 10;
                        break;
                    case 2:
                        updateColor(Color.parseColor("#cc2e2e"));
                        animationView.setAnimation("reload_ERROR.json");
                        animationView.playAnimation();
                        estado = 10;
                        break;
                    case 10:
                        animationView.cancelAnimation();
                        startActivity(new Intent(ActivitySuccess.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animationView.destroyDrawingCache();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                if (estado != 0){
                    animationView.loop(false);
                }
            }
        });
    }

    public void updateColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(color);
        }
        v_fondo_color.setBackgroundColor(color);
    }
    public void gohome(){
        Thread t = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    sleep(1000);
                    animationView.cancelAnimation();
                }catch (Exception e){

                } finally {
                    startActivity(new Intent(ActivitySuccess.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        };
        t.start();


    }
}
