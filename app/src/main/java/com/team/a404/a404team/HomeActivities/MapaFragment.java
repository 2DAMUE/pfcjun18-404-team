package com.team.a404.a404team.HomeActivities;


import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team.a404.a404team.R;
import com.team.a404.a404team.SplashScreen;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private LinearLayout v_vista_error_carga;
    private double longitud,latitud;
    private LatLng actual;
    private static boolean contador = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mapa, container, false);
        return mView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceStade) {
        super.onViewCreated(view, savedInstanceStade);

        mMapView = (MapView) mView.findViewById(R.id.map);

        v_vista_error_carga = (LinearLayout) mView.findViewById(R.id.vista_error_carga);

        mMapView.setVisibility(View.INVISIBLE);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            v_vista_error_carga.setVisibility(View.VISIBLE);
            return;
        }else{
            mGoogleMap = googleMap;
            mMapView.setVisibility(View.VISIBLE);
            MapsInitializer.initialize(getContext());

            if (contador){
                //MiUbucacion();
                contador = false;
            }


            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            final LatLng MELBOURNE = new LatLng(40.32293817775734, -3.868268993392121);
            Marker melbourne = googleMap.addMarker(new MarkerOptions()
                    .position(MELBOURNE)
                    .title("Melbourne")
                    .snippet(String.valueOf(R.layout.fragment_anuncios))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_logomaps)));

            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng posicionClick = marker.getPosition();
                    Toast.makeText(getActivity().getApplicationContext(), "has pulsado en el marcador y su posición " + posicionClick, Toast.LENGTH_LONG).show();
/*
                DialogDetalleMarket ddm = new DialogDetalleMarket();
                ddm.show(getFragmentManager(), "ddm");
*/
                    return false;
                }
            });

            mGoogleMap.setMyLocationEnabled(true);

        }
    }

    public void MiUbucacion() {

        mGoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                longitud = location.getLongitude();
                latitud = location.getLatitude();
                Log.v("lat", String.valueOf(latitud));
                //setLatLng(location.getLatitude(),location.getLongitude());
                LatLng actual = new LatLng(latitud, longitud);
                mGoogleMap.clear();
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 13));
                // mMap.setInfoWindowAdapter();
            }
        });

    }




}