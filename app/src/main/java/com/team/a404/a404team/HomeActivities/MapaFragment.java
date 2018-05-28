package com.team.a404.a404team.HomeActivities;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.database.ChildEventListener;
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
    private TextView nombre, descripcion;
    private Button aceptar;
    private DatabaseReference all_marcadores;
    private ArrayList<Marcadores_perdidos> marcadores = new ArrayList<>();
    private int i = 0;

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

            /** CREAR MARCADORES EN EL MAPA **/
            all_marcadores = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas");
            all_marcadores.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Marcadores_perdidos datos_marcador = dataSnapshot.getValue(Marcadores_perdidos.class);
                    marcadores.add(datos_marcador);

                    LatLng ubi = new LatLng(marcadores.get(i).getLatitud(), marcadores.get(i).getLongitud());

                    Marker map_marcador = mGoogleMap.addMarker(new MarkerOptions()
                            .position(ubi)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_logomaps)));

                    map_marcador.setTag(marcadores.get(i).getId_mascota()+"##" + marcadores.get(i).getOwner());

                    i++;
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            /**  /CREAR MARCADORES EN EL MAPA **/


            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    LatLng posicionClick = marker.getPosition();

                    String tagAll = (String) marker.getTag();
                    String[] info = tagAll.split("##");
                    String id = info[0];
                    String owner = info[1];

                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_anuncio);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();

                    nombre = (TextView) dialog.findViewById(R.id.event_name);
                    descripcion = (TextView) dialog.findViewById(R.id.event_description);
                    aceptar = (Button) dialog.findViewById(R.id.btn_accept);
                    aceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    DatabaseReference info_mascota = FirebaseDatabase.getInstance().getReference("usuarios").child(owner).child("mascotas").child(id);
                    info_mascota.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<AnuncioInformation> anuncio = new ArrayList<AnuncioInformation>();
                            AnuncioInformation nuevo = dataSnapshot.getValue(AnuncioInformation.class);
                            anuncio.add(nuevo);
                            nombre.setText(nuevo.getNombre());
                            descripcion.setText(nuevo.getDescripcion());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    return false;
                }
            });
            mGoogleMap.setMyLocationEnabled(true);

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
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));
                    contador = false;
                }

                // mMap.setInfoWindowAdapter();
            }
        });
    }


}