package com.team.a404.a404team.pruebas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.team.a404.a404team.R;

public class pruebas_dialogo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dddddd);


        SlidrConfig config = new SlidrConfig.Builder()
                .primaryColor(getResources().getColor(R.color.colorAccent))
                .secondaryColor(getResources().getColor(R.color.colorRojoRosaOscuro))
                .position(SlidrPosition.VERTICAL)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true|true)
                .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
                .build();

        Slidr.attach(this, config);
    }
}
