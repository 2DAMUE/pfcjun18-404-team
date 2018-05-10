package com.team.a404.a404team;

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import com.airbnb.lottie.LottieDrawable;
import com.team.a404.a404team.MainActivities.MainMapActivity;
import com.team.a404.a404team.Zona_lobby.RegistroActivity;
import com.team.a404.a404team.Zona_lobby.lobby;

public class CreacionSuccessActivity extends AppCompatActivity {
    private LottieAnimationView lottieAnimationView;
    private TextView v_text_ok;
    private Button v_btn_next;
    private FrameLayout v_fondo_color;
    public static int estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_success);
        getSupportActionBar().hide();
        v_text_ok = (TextView) findViewById(R.id.text_ok);
        v_btn_next = (Button) findViewById(R.id.btn_next);
        v_fondo_color = (FrameLayout) findViewById(R.id.fondo_color);



        v_text_ok.setVisibility(View.INVISIBLE);
        v_btn_next.setVisibility(View.INVISIBLE);
        updateColor(Color.parseColor("#f1c40f"));

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animationview);
        lottieAnimationView.playAnimation();
        lottieAnimationView.setScale(1);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }
            @Override
            public void onAnimationEnd(Animator animator) {
                if (estado == 1){
                    lottieAnimationView.setAnimation("anim_tick.json");
                    updateColor(Color.parseColor("#2ecc71"));
                    gohome();
                }else if (estado == 2){
                    updateColor(Color.parseColor("#FFCC2E31"));
                    lottieAnimationView.setAnimation("anim_tick.json");
                    gohome();
                }else if (estado == 0){
                    lottieAnimationView.playAnimation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

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
                    sleep(3000);
                }catch (Exception e){

                } finally {
                    startActivity(new Intent(CreacionSuccessActivity.this, MainMapActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        };
        t.start();


    }
}
