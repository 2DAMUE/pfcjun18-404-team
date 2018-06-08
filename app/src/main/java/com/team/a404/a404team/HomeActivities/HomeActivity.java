package com.team.a404.a404team.HomeActivities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.team.a404.a404team.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class HomeActivity extends AppCompatActivity {

    AlertDialog alertDialog;
    private static int STORAGE_PERMISSION_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_map);


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
                    //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    IrMaps();
                    return true;
                case R.id.navigation_profile:
                    IrPerfil();
                    return true;
            }
            return false;
        }
    };

    public void IrAnuncios() {
        //FragmentManager manager = getSupportFragmentManager();
        //manager.beginTransaction().replace(R.id.container, new MascotasFragment()).commit();

        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.container,new MascotasFragment()).addToBackStack(null).commit();



        ConColor();
    }

    public void IrMaps() {
        requestStoragePermission();
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.container,new MapaFragment(),new MapaFragment().getClass().getName()).addToBackStack("F_MAIN").commit();

        //FragmentManager manager = getSupportFragmentManager();
        //manager.beginTransaction().replace(R.id.container, new MapaFragment()).commit();
        Trasparente();
    }

    public void IrPerfil() {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.replace(R.id.container,new PerfilFragment()).addToBackStack(null).commit();
        //FragmentManager manager = getSupportFragmentManager();
        //manager.beginTransaction().replace(R.id.container, new PerfilFragment()).commit();
        ConColor();
    }

    public void Trasparente(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(android.graphics.Color.parseColor("#25000000"));
        }
    }
    public void ConColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(android.graphics.Color.parseColor("#a60143"));
        }
    }


    private void loadFragment(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newFragment,newFragment.getClass().getName())
                .addToBackStack("F_MAIN")
                .commit();
    }

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
                gohome();
            } else {

            }
        }
    }

    public void gohome(){
        Thread t = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    sleep(500);
                }catch (Exception e){

                } finally {
                    IrMaps();
                }
            }
        };
        t.start();


    }

}
