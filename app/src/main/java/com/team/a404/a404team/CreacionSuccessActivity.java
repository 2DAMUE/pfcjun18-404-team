package com.team.a404.a404team;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class CreacionSuccessActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    TextView v_text_ok;
    Button v_btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_success);
        getSupportActionBar().hide();
        v_text_ok = (TextView) findViewById(R.id.text_ok);
        v_btn_next = (Button) findViewById(R.id.btn_next);

        v_text_ok.setVisibility(View.INVISIBLE);
        v_btn_next.setVisibility(View.INVISIBLE);


        int color = Color.parseColor("#2ecc71");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(color);
        }


        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animationview);
        lottieAnimationView.playAnimation();

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                v_text_ok.setVisibility(View.VISIBLE);
                v_btn_next.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }
}
