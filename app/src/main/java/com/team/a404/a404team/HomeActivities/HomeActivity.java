package com.team.a404.a404team.HomeActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.team.a404.a404team.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        IrMaps();
        getSupportActionBar().hide();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    IrAnuncios();
                    return true;
                case R.id.navigation_map:
                    IrMaps();
                    return true;
                case R.id.navigation_profile:
                    IrPerfil();
                    return true;
            }
            return true;
        }
    };

    public void IrAnuncios() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new AnunciosFragment()).commit();
    }
    public void IrMaps() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new MapaFragment()).commit();
    }
    public void IrPerfil() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, new MapaFragment()).commit();
    }

}
