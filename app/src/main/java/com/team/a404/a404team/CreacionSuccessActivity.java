package com.team.a404.a404team;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class CreacionSuccessActivity extends AppCompatActivity {
    private LottieAnimationView lottieAnimationView;
    private estado e = new estado();
    private TextView v_text_ok;
    private Button v_btn_next;
    private FrameLayout v_fondo_color;
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
                animator.setDuration(3000);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                if (e.getV_estado() == 1){
                    lottieAnimationView.setAnimation("anim_tick.json");
                    updateColor(Color.parseColor("#2ecc71"));
                    lottieAnimationView.pauseAnimation();
                }else if (e.getV_estado() == 2){
                    updateColor(Color.parseColor("#FFCC2E31"));
                    lottieAnimationView.setAnimation("anim_tick.json");
                    lottieAnimationView.pauseAnimation();
                }else{
                    lottieAnimationView.playAnimation();
                }
                Log.v("Cual",""+e.getV_estado());

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
}
