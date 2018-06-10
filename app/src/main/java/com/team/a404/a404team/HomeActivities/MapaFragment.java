package com.team.a404.a404team.HomeActivities;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.team.a404.a404team.Datos.DB_Datos_Mascotas;
import com.team.a404.a404team.Datos.DB_Datos_Perfil;
import com.team.a404.a404team.Datos.Marcadores_paseo;
import com.team.a404.a404team.Datos.Marcadores_perdidos;
import com.team.a404.a404team.HomeActivities.MiMascotaPerdida.CreateMarcadorPerdida;
import com.team.a404.a404team.R;
import com.team.a404.a404team.pruebas.pruebas_dialogo;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private LinearLayout v_vista_error_carga;
    private double longitud, latitud;
    private boolean contador;
    private TextView v_mascota_nombre, v_mascota_raza, v_mascota_rasgos, v_usuario_owner,v_mascota_text_raza;
    private ImageView v_icon_borar;
    private CircularImageView v_foto_mascota;
    private DatabaseReference all_marcadores;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ArrayList<Marcadores_perdidos> marcadores_rojo = new ArrayList<Marcadores_perdidos>();
    private ArrayList<Marcadores_paseo> marcadores_azul = new ArrayList<Marcadores_paseo>();
    private ArrayList<String> marcadores_rojo_id = new ArrayList<String>();
    private FloatingActionButton v_fab_myloca ,v_fab_EncontreMascota,v_fab_Paseo ,v_fab_PerdiMiMascota;
    private FloatingActionsMenu v_fab_menu;
    private String id,owner,id_marcador ;



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
        v_fab_myloca = (FloatingActionButton) mView.findViewById(R.id.myLocationButton);
        v_fab_menu = (FloatingActionsMenu) mView.findViewById(R.id.fab_menu);

        mMapView.setVisibility(View.INVISIBLE);
        v_fab_myloca.setVisibility(View.INVISIBLE);
        v_fab_menu.setVisibility(View.INVISIBLE);


        contador = true;

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }
        userLocationFAB();
        PerdiMiMascota();
        Paseo();
        EncontreMascota();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            v_vista_error_carga.setVisibility(View.VISIBLE);
            return;
        } else {
            mGoogleMap = googleMap;
            mMapView.setVisibility(View.VISIBLE);
            v_fab_menu.setVisibility(View.VISIBLE);
            v_fab_myloca.setVisibility(View.VISIBLE);
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
                    id = info[0];
                    owner = info[1];
                    int tipo = Integer.parseInt(info[2]);
                    id_marcador = info[3];
                    Log.e("LOGG"," > "+id_marcador);

                    switch (tipo){
                        case 1:
                            InfoDialogoPerdido();
                            break;
                        case 2:
                            InfoDialogoPaseo();
                            break;
                        case 3:
                            break;
                    }


                    return false;
                }
            });
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }


    // ----------------------------------------------------------------------------------------------------------------------------------

    private void PerdiMiMascota(){
        v_fab_PerdiMiMascota = (FloatingActionButton) mView.findViewById(R.id.fab_PerdiMiMascota);
        v_fab_PerdiMiMascota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(getActivity(), CreateMarcadorPerdida.class);
                        startActivity(intent1);
                    }
                });
    }

    private void Paseo(){
        v_fab_Paseo = (FloatingActionButton) mView.findViewById(R.id.fab_Paseo);
        v_fab_Paseo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(getActivity(), pruebas_dialogo.class);
                        startActivity(intent1);

                    }
                });
    }

    private void EncontreMascota(){
        v_fab_EncontreMascota = (FloatingActionButton) mView.findViewById(R.id.fab_EncontreMascota);
        v_fab_EncontreMascota.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
    }


    private void InfoDialogoPerdido() {
        final Dialog dialog_info_perdido = new Dialog(getContext(), R.style.Theme_Dialog_Translucent);
        dialog_info_perdido.setContentView(R.layout.dialog_mapa_info_perdida);
        dialog_info_perdido.setCanceledOnTouchOutside(true);
        dialog_info_perdido.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#30000000")));
        dialog_info_perdido.show();

        FrameLayout v_pantalla = (FrameLayout) dialog_info_perdido.findViewById(R.id.anuncio_pantalla);
        LinearLayout v_contenido_ventana = (LinearLayout) dialog_info_perdido.findViewById(R.id.contenido_ventana);

        v_icon_borar = (ImageView) dialog_info_perdido.findViewById(R.id.icon_borrar);

        if (owner.equals(firebaseAuth.getCurrentUser().getUid())) {
            v_icon_borar.setVisibility(View.VISIBLE);
        }else {
            v_icon_borar.setVisibility(View.INVISIBLE);
        }
        v_mascota_nombre = (TextView) dialog_info_perdido.findViewById(R.id.mascota_nombre);
        v_mascota_raza = (TextView) dialog_info_perdido.findViewById(R.id.mascota_raza);
        v_mascota_text_raza = (TextView) dialog_info_perdido.findViewById(R.id.text_rasgos);
        v_mascota_rasgos = (TextView) dialog_info_perdido.findViewById(R.id.mascota_rasgos);
        v_usuario_owner = (TextView) dialog_info_perdido.findViewById(R.id.usuario_owner);
        v_foto_mascota = (CircularImageView) dialog_info_perdido.findViewById(R.id.foto_mascota);

        DatabaseReference info_mascota = FirebaseDatabase.getInstance().getReference("usuarios").child(owner).child("mascotas").child(id);
        info_mascota.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DB_Datos_Mascotas d_mascota = dataSnapshot.getValue(DB_Datos_Mascotas.class);

                if (d_mascota.getNombre() != ""){
                    v_mascota_nombre.setText(d_mascota.getNombre());
                }else
                    v_mascota_nombre.setVisibility(View.INVISIBLE);

                if (d_mascota.getRaza() != ""){
                    v_mascota_raza.setText(d_mascota.getRaza());
                }else
                    v_mascota_raza.setVisibility(View.INVISIBLE);

                if (d_mascota.getRasgos().length() > 2){
                    v_mascota_rasgos.setText(d_mascota.getRasgos());
                }else {
                    v_mascota_text_raza.setVisibility(View.INVISIBLE);
                    v_mascota_rasgos.setVisibility(View.INVISIBLE);
                }
                CogerFotoMascota();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        DatabaseReference info_owner = FirebaseDatabase.getInstance().getReference("usuarios").child(owner);
        info_owner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DB_Datos_Perfil d_perfil = dataSnapshot.getValue(DB_Datos_Perfil.class);
                v_usuario_owner.setText(d_perfil.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        v_icon_borar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Borrar Marcador");
                builder.setMessage("¿Esta seguro que quiere borrarlo este marcador?");
                builder.setPositiveButton("SI",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference borrar_marcador = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas").child(id_marcador);
                                borrar_marcador.removeValue();
                                CogerTodosMarcadores();
                                dialog_info_perdido.hide();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog_confirmar = builder.create();
                dialog_confirmar.show();
            }
        });

    }

    private void InfoDialogoPaseo() {
        final Dialog dialog = new Dialog(getContext(), R.style.Theme_Dialog_Translucent);
        dialog.setContentView(R.layout.dialog_mapa_info_paseo);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#25000000")));
        dialog.show();

        FrameLayout v_pantalla = (FrameLayout) dialog.findViewById(R.id.anuncio_pantalla);
        LinearLayout v_contenido_ventana = (LinearLayout) dialog.findViewById(R.id.contenido_ventana);
        v_usuario_owner = (TextView) dialog.findViewById(R.id.usuario_owner);


        DatabaseReference info_owner = FirebaseDatabase.getInstance().getReference("usuarios").child(owner);
        info_owner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DB_Datos_Perfil d_perfil = dataSnapshot.getValue(DB_Datos_Perfil.class);
                v_usuario_owner.setText(d_perfil.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }



    private void CogerFotoMascota(){
        try{
            StorageReference stor = FirebaseStorage.getInstance().getReference().child("images/" + owner.toString() + "/userphoto.jpg");  //Cambiar a foto de la mascota (Sergio)
            final long ONE_MEGABYTE = 1024 * 1024;
            stor.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    v_foto_mascota.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    v_foto_mascota.setImageResource(R.drawable.dog);
                }
            });
        }catch (Exception x){
            v_foto_mascota.setImageResource(R.drawable.dog);
        }

    }

    /** IR A MI UBICACION BOTON FAB */

    private void userLocationFAB() {
        v_fab_myloca.setOnClickListener(new View.OnClickListener() {
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
     * CREAR MARCADORES EN EL MAPA PERDIDO , ROJO
     */

    public void CogerMarcadoresPerdidas() {

        all_marcadores = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas");
        all_marcadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                marcadores_rojo = new ArrayList<>();
                for (DataSnapshot dato : dataSnapshot.getChildren()) {
                    Marcadores_perdidos mp = dato.getValue(Marcadores_perdidos.class);
                    marcadores_rojo.add(mp);
                    marcadores_rojo_id.add(dato.getKey());
                }
                MeterMarcadoresRojo();
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void MeterMarcadoresRojo() {
        for (int i = 0; i < marcadores_rojo.size(); i++) {
            LatLng ubi = new LatLng(marcadores_rojo.get(i).getLatitud(), marcadores_rojo.get(i).getLongitud());

            Marker map_marcador = mGoogleMap.addMarker(new MarkerOptions()
                    .position(ubi)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marcador_rojo)));
            map_marcador.setTag(marcadores_rojo.get(i).getId_mascota() + "##" + marcadores_rojo.get(i).getOwner()+"##1##"+marcadores_rojo_id.get(i));
        }

    }


    /**
     * CREAR MARCADORES EN EL MAPA PASO AZUL
     */

    public void CogerMarcadoresPaseo() {

        all_marcadores = FirebaseDatabase.getInstance().getReference("marcadores").child("paseo");
        all_marcadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                marcadores_azul = new ArrayList<>();

                for (DataSnapshot dato : dataSnapshot.getChildren()) {
                    Marcadores_paseo mpaseo = dato.getValue(Marcadores_paseo.class);
                    marcadores_azul.add(mpaseo);
                }
                MeterMarcadoresAzul();
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void MeterMarcadoresAzul() {
        for (int i = 0; i < marcadores_azul.size(); i++) {
            LatLng ubi = new LatLng(marcadores_azul.get(i).getLatitud(), marcadores_azul.get(i).getLongitud());

            Marker map_marcador = mGoogleMap.addMarker(new MarkerOptions()
                    .position(ubi)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marcador_azul)));
            map_marcador.setTag("nada##" + marcadores_azul.get(i).getOwner()+"##2");
        }

    }


    /**
     * BORRAR MARCADOR
     */


    private void Borrar_marcador(){

    }

    private void CogerTodosMarcadores(){
        mGoogleMap.clear();
        CogerMarcadoresPerdidas();
        CogerMarcadoresPaseo();
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

                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));
                    contador = false;
                    CogerTodosMarcadores();
                }

                // mMap.setInfoWindowAdapter();
            }
        });
    }

}