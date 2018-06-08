package com.team.a404.a404team.HomeActivities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.team.a404.a404team.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class HomeActivity extends AppCompatActivity {

    private static int STORAGE_PERMISSION_CODE = 2;
    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        getSupportActionBar().hide();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);
        navigation.setSelectedItemId(R.id.navigation_map);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment ventana = null;
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    ventana = new MascotasFragment();
                    ColorWindow(1);
                    break;
                case R.id.navigation_map:
                    requestStoragePermission();
                    ventana = new MapaFragment();
                    ColorWindow(0);
                    break;
                case R.id.navigation_profile:
                    ventana = new PerfilFragment();
                    ColorWindow(1);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, ventana).commit();

            return true;
        }
    };

    public void requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
            }
        }
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Thread t = new Thread(){
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        try {
                            sleep(500);
                        }catch (Exception e){

                        } finally {
                            navigation.setSelectedItemId(R.id.navigation_map);
                        }
                    }
                };
                t.start();
            } else {

            }
        }
    }

    public void ColorWindow(int valor){
        switch (valor){
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    getWindow().setStatusBarColor(android.graphics.Color.parseColor("#25000000"));
                }
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    getWindow().setStatusBarColor(android.graphics.Color.parseColor("#a60143"));
                }
                break;
        }
    }

}
