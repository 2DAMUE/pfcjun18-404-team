package com.team.a404.a404team.HomeActivities.MiMascotaPerdida;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.team.a404.a404team.Datos.Marcadores_perdidos;
import com.team.a404.a404team.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class SelecionarUbicacionMapaFragment extends Fragment implements OnMapReadyCallback, BlockingStep {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private double longitud, latitud;
    private boolean contador;
    private FloatingActionButton FAB;
    private Marker map_marcador;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mapa_select, container, false);
        return mView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceStade) {
        super.onViewCreated(view, savedInstanceStade);

        mMapView = (MapView) mView.findViewById(R.id.map);

        mMapView.setVisibility(View.INVISIBLE);

        contador = true;

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
        userLocationFAB();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            mGoogleMap = googleMap;
            mMapView.setVisibility(View.VISIBLE);
            MapsInitializer.initialize(getContext());


            UiSettings uiSettings = mGoogleMap.getUiSettings();
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            MiUbucacion();


            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mGoogleMap.clear();
                    map_marcador = mGoogleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marcador_rojo)));
                }
            });

            /**  /INFO DE LOS MARCADORES **/


            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }


    // ----------------------------------------------------------------------------------------------------------------------------------


    private void PonerMarcador(){
        LatLng ubi = new LatLng(latitud, longitud);

        map_marcador = mGoogleMap.addMarker(new MarkerOptions()
            .position(ubi)
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marcador_rojo)));


    }


    private void userLocationFAB() {
        FAB = (FloatingActionButton) mView.findViewById(R.id.myLocationButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mGoogleMap.getMyLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    LatLng actual = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actual, 16));
                }
            }
        });
    }

    /**
     * METODO PARA MOVER EL MAPA A TU UBICACION
     */

    public void MiUbucacion() {

        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                longitud = location.getLongitude();
                latitud = location.getLatitude();
                //setLatLng(location.getLatitude(),location.getLongitude());
                LatLng actual = new LatLng(latitud, longitud);

                if (contador) {
                    PonerMarcador();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));
                    contador = false;
                }

                // mMap.setInfoWindowAdapter();
            }
        });
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        Log.e("ID"," > "+SelecionMascotaPerdidaFragment.v_id_mascota);
        Log.e("UBI"," > "+map_marcador.getPosition());
        EnviarDatos();
        Toast.makeText(getActivity(), "Tu marcador de a creado", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToPrevStep();
            }
        }, 0L);
    }


    public void EnviarDatos(){
        DatabaseReference db_marcador_perdida = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas").push();
        db_marcador_perdida.setValue(new Marcadores_perdidos(SelecionMascotaPerdidaFragment.v_id_mascota, map_marcador.getPosition().latitude, map_marcador.getPosition().longitude, firebaseAuth.getCurrentUser().getUid(),SelecionMascotaPerdidaFragment.telefono));
        DatabaseReference db_mi_marcador_perdida = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("mis_marcadores").push();
        db_mi_marcador_perdida.setValue(db_marcador_perdida.getKey());
    }
}