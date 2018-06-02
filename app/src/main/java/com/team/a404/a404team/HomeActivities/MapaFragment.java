package com.team.a404.a404team.HomeActivities;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.a404.a404team.Datos.AnuncioInformation;
import com.team.a404.a404team.Datos.Marcadores_perdidos;
import com.team.a404.a404team.R;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private LinearLayout v_vista_error_carga;
    private double longitud, latitud;
    private LatLng actual;
    private boolean contador;
    private TextView v_mascota_nombre, v_mascota_raza, v_mascota_rasgos, owner;
    private Button aceptar;
    private DatabaseReference all_marcadores;
    private ArrayList<Marcadores_perdidos> marcadores = new ArrayList<Marcadores_perdidos>();
    private AnuncioInformation af;
    FloatingActionButton FAB,nuevoAnuncio;


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
        contador = true;

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
        userLocationFAB();
        nuevoAnuncio = (FloatingActionButton)mView.findViewById(R.id.nuevoMarcador);
        nuevoAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGoogleMap.getMyLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    LatLng actual = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 16));
                    CrearMarcador(actual);
                }

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            v_vista_error_carga.setVisibility(View.VISIBLE);
            return;
        } else {
            mGoogleMap = googleMap;
            mMapView.setVisibility(View.VISIBLE);
            MapsInitializer.initialize(getContext());


            UiSettings uiSettings = mGoogleMap.getUiSettings();
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


            MiUbucacion();

            /**  /INFO DE LOS MARCADORES **/


            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng posicionClick = marker.getPosition();

                    String tagAll = (String) marker.getTag();
                    String[] info = tagAll.split("##");
                    String id = info[0];
                    String owner = info[1];

                    final Dialog dialog = new Dialog(getContext(), R.style.Theme_Dialog_Translucent);
                    dialog.setContentView(R.layout.dialog_anuncio);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#25000000")));
                    dialog.show();

                    FrameLayout v_pantalla = (FrameLayout) dialog.findViewById(R.id.anuncio_pantalla);
                    LinearLayout v_contenido_ventana = (LinearLayout) dialog.findViewById(R.id.contenido_ventana);

                    v_pantalla.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.hide();
                        }
                    });
                    v_mascota_nombre = (TextView) dialog.findViewById(R.id.mascota_nombre);
                    v_mascota_raza = (TextView) dialog.findViewById(R.id.mascota_rasgos);
                    v_mascota_rasgos = (TextView) dialog.findViewById(R.id.mascota_rasgos);


                    DatabaseReference info_mascota = FirebaseDatabase.getInstance().getReference("usuarios").child(owner).child("mascotas").child(id);
                    info_mascota.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<AnuncioInformation> anuncio = new ArrayList<AnuncioInformation>();
                            AnuncioInformation nuevo = dataSnapshot.getValue(AnuncioInformation.class);
                            anuncio.add(nuevo);
                            v_mascota_nombre.setText(nuevo.getNombre());
                            v_mascota_raza.setText(nuevo.getDescripcion());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    return false;
                }
            });

            //--------------------------------------------------
            /*
            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng posicionClick = marker.getPosition();
                    String mark = marker.getTag().toString();
                    final Dialog dialog = new Dialog(getContext(), R.style.Theme_Dialog_Translucent);
                    dialog.setContentView(R.layout.dialog_anuncio);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#25000000")));
                    dialog.show();

                    FrameLayout v_pantalla = (FrameLayout) dialog.findViewById(R.id.anuncio_pantalla);
                    LinearLayout v_contenido_ventana = (LinearLayout) dialog.findViewById(R.id.contenido_ventana);

                    v_pantalla.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.hide();
                        }
                    });
                    nombre = (TextView) dialog.findViewById(R.id.name);
                    descripcion = (TextView) dialog.findViewById(R.id.descript);
                    raza = (TextView) dialog.findViewById(R.id.race);
                    owner = (TextView) dialog.findViewById(R.id.event_name);
                    aceptar = (Button) dialog.findViewById(R.id.btn_accept);
                    MostarInfo(mark);


                    return false;
                }
            });
            */
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }


    // ----------------------------------------------------------------------------------------------------------------------------------

    private void userLocationFAB() {
        FAB = (FloatingActionButton) mView.findViewById(R.id.myLocationButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mGoogleMap.getMyLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    LatLng actual = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 16));
                }
            }
        });
    }


    /**
     * CREAR MARCADORES EN EL MAPA
     */
    public void CrearMarcador(LatLng position){

    }
    public void MostarInfo(String mark) {
        DatabaseReference info_mascota = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas").child(mark);
        info_mascota.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<AnuncioInformation> anuncio = new ArrayList<AnuncioInformation>();
                AnuncioInformation nuevo = dataSnapshot.getValue(AnuncioInformation.class);
                anuncio.add(nuevo);
                v_mascota_nombre.setText(nuevo.getNombre());
                v_mascota_rasgos.setText(nuevo.getDescripcion());
                v_mascota_raza.setText(nuevo.getRaza());
                owner.setText(nuevo.getOwner());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void CogerMarcadores() {

        all_marcadores = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas");
        all_marcadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                marcadores = new ArrayList<>();

                for (DataSnapshot dato : dataSnapshot.getChildren()) {
                    af = dato.getValue(AnuncioInformation.class);
                    marcadores.add(af);
                    Log.v("datosUsuarios",af.toString());
                }
                MeterMarcadores();
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void MeterMarcadores() {
        mGoogleMap.clear();
        Log.v("ESTO__2", "" + marcadores.size());
        for (int i = 0; i < marcadores.size(); i++) {
            LatLng ubi = new LatLng(marcadores.get(i).getLatitud(), marcadores.get(i).getLongitud());

            Marker map_marcador = mGoogleMap.addMarker(new MarkerOptions()
                    .position(ubi)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_logomaps)));

            map_marcador.setTag(marcadores.get(i).getId_mascota() + "##" + marcadores.get(i).getOwner());
            Log.v("ESTO__3", "" +ubi);
            Log.v("ESTO__3", "" +marcadores.get(i).getId_mascota() + "##" + marcadores.get(i).getOwner());
        }

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
                Log.v("lat", String.valueOf(latitud));
                //setLatLng(location.getLatitude(),location.getLongitude());
                LatLng actual = new LatLng(latitud, longitud);

                if (contador) {
                    CogerMarcadores();
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));
                    contador = false;
                }

                // mMap.setInfoWindowAdapter();
            }
        });
    }


}