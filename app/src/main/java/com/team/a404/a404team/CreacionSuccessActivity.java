package com.team.a404.a404team;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class CreacionSuccessActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_success);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animationview);
        lottieAnimationView.playAnimation();

    }
}
