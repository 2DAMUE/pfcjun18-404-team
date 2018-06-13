package com.team.a404.a404team.HomeActivities;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.andreilisun.swipedismissdialog.SwipeDismissDialog;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.team.a404.a404team.Datos.DB_Datos_Perfil;
import com.team.a404.a404team.Datos.DatosMascota;
import com.team.a404.a404team.Datos.Marcadores_otras;
import com.team.a404.a404team.Datos.Marcadores_paseo;
import com.team.a404.a404team.Datos.Marcadores_perdidos;
import com.team.a404.a404team.HomeActivities.HoraDelPaseo.CreateMarcadorPaseo;
import com.team.a404.a404team.HomeActivities.MiMascotaPerdida.CreateMarcadorPerdida;
import com.team.a404.a404team.HomeActivities.OtraMascotaPerdida.CreateMarcardorOtra;
import com.team.a404.a404team.R;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private LinearLayout v_vista_error_carga;
    private double longitud, latitud;
    private boolean contador;
    private TextView v_mascota_nombre, v_mascota_raza, v_mascota_rasgos, v_usuario_owner, v_mascota_text_raza, v_telefono;
    private ImageView v_icon_borrar;
    private CircularImageView v_foto_mascota;
    private DatabaseReference all_marcadores;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ArrayList<Marcadores_perdidos> marcadores_rojo = new ArrayList<Marcadores_perdidos>();
    private ArrayList<Marcadores_paseo> marcadores_azul = new ArrayList<Marcadores_paseo>();
    private ArrayList<Marcadores_otras> marcadores_verdes = new ArrayList<Marcadores_otras>();
    private ArrayList<String> marcadores_rojo_id = new ArrayList<String>();
    private ArrayList<String> marcadores_azul_id = new ArrayList<String>();
    private ArrayList<String> marcadores_verdes_id = new ArrayList<String>();
    private FloatingActionButton v_fab_myloca, v_fab_EncontreMascota, v_fab_Paseo, v_fab_PerdiMiMascota;
    private FloatingActionsMenu v_fab_menu;
    private String id, owner, id_marcador, markerid ,url_foto_mascota;
    private FrameLayout fl_interceptor;
    private Button v_btn_visto;

    //SwipeDismissDialog


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

        fl_interceptor = (FrameLayout) mView.findViewById(R.id.fl_interceptor);

        mMapView.setVisibility(View.INVISIBLE);
        v_fab_myloca.setVisibility(View.INVISIBLE);
        v_fab_menu.setVisibility(View.INVISIBLE);

        fl_interceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (v_fab_menu.isExpanded()) {
                    v_fab_menu.collapse();
                }
                return false;
            }
        });

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
                    Log.e("LOGG", " > " + id_marcador);

                    switch (tipo) {
                        case 1:
                            InfoDialogoPerdido();
                            break;
                        case 2:
                            InfoDialogoPaseo();
                            break;
                        case 3:
                            InfoDialogoOtra(marker);
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

    /**
     * MENU DEL FAB
     */

    private void PerdiMiMascota() {
        v_fab_PerdiMiMascota = (FloatingActionButton) mView.findViewById(R.id.fab_PerdiMiMascota);
        v_fab_PerdiMiMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), CreateMarcadorPerdida.class);
                startActivity(intent1);
            }
        });
    }

    private void Paseo() {
        v_fab_Paseo = (FloatingActionButton) mView.findViewById(R.id.fab_Paseo);
        v_fab_Paseo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_ventana = new Intent(getActivity(), CreateMarcadorPaseo.class);
                startActivity(new_ventana);

            }
        });
    }

    private void EncontreMascota() {
        v_fab_EncontreMascota = (FloatingActionButton) mView.findViewById(R.id.fab_EncontreMascota);
        v_fab_EncontreMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_ventana = new Intent(getActivity(), CreateMarcardorOtra.class);
                startActivity(new_ventana);

            }
        });
    }

    /**
     *  INFORMACION DE LOS MARCADORES
     */

    private void InfoDialogoOtra(final Marker marker) {

        final SwipeDismissDialog dialog_info_perdido = new SwipeDismissDialog.Builder(getContext())
                .setLayoutResId(R.layout.dialog_mascotas_otra)
                .build()
                .show();

        v_icon_borrar = (ImageView) dialog_info_perdido.findViewById(R.id.icon_borrar);

        if (owner.equals(firebaseAuth.getCurrentUser().getUid())) {
            v_icon_borrar.setVisibility(View.VISIBLE);
        } else {
            v_icon_borrar.setVisibility(View.INVISIBLE);
        }

        v_mascota_rasgos = (TextView) dialog_info_perdido.findViewById(R.id.rasgos_otra);
        v_foto_mascota = (CircularImageView) dialog_info_perdido.findViewById(R.id.foto_mascotaotra);
        v_telefono = (TextView) dialog_info_perdido.findViewById(R.id.telf_otra);
        v_btn_visto = (Button)dialog_info_perdido.findViewById(R.id.btn_vistro_otra);



        DatabaseReference info_mascota = FirebaseDatabase.getInstance().getReference("marcadores").child("encontradas").child(id_marcador);
        info_mascota.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Marcadores_otras mark = dataSnapshot.getValue(Marcadores_otras.class);
                v_mascota_rasgos.setText(mark.getRasgos());
                v_telefono.setText(mark.getTelefono());
                url_foto_mascota = mark.getUrl_foto();
                Picasso.get().load(url_foto_mascota).into(v_foto_mascota);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /** FUNCIONES ON CLIK INFO PERDIDA */

        v_icon_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BorrarMarcador(id_marcador, dialog_info_perdido, 1);
            }
        });
        v_telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealizarLlamada(v_telefono.getText().toString());
            }
        });

        v_btn_visto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setMessage("Va a mover el marcador a su posición actual"); // FALTA PARSEAR
                builder.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference change_marker = FirebaseDatabase.getInstance().getReference("marcadores").child("encontradas").child(id_marcador);
                                LatLng actual = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                                change_marker.child("latitud").setValue(actual.latitude);
                                change_marker.child("longitud").setValue(actual.longitude);
                                dialog_info_perdido.dismiss();
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

        v_foto_mascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_foto_mascota = new Dialog(getContext(), R.style.Theme_Dialog_Translucent);
                dialog_foto_mascota.setContentView(R.layout.dialog_mapa_info_foto);
                dialog_foto_mascota.setCanceledOnTouchOutside(true);
                dialog_foto_mascota.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#25000000")));
                dialog_foto_mascota.show();

                ImageView v_foto = (ImageView) dialog_foto_mascota.findViewById(R.id.img_mascota);
                ImageView v_cerrar = (ImageView) dialog_foto_mascota.findViewById(R.id.icon_cerrar);

                Picasso.get().load(url_foto_mascota).into(v_foto);
                v_cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_foto_mascota.hide();
                    }
                });
            }
        });



    }

    private void InfoDialogoPerdido() {

        final SwipeDismissDialog dialog_info_perdido = new SwipeDismissDialog.Builder(getContext())
                .setLayoutResId(R.layout.dialog_mapa_info_perdida)
                .build()
                .show();

        v_icon_borrar = (ImageView) dialog_info_perdido.findViewById(R.id.icon_borrar);

        if (owner.equals(firebaseAuth.getCurrentUser().getUid())) {
            v_icon_borrar.setVisibility(View.VISIBLE);
        } else {
            v_icon_borrar.setVisibility(View.INVISIBLE);
        }

        v_mascota_nombre = (TextView) dialog_info_perdido.findViewById(R.id.mascota_nombre);
        v_mascota_raza = (TextView) dialog_info_perdido.findViewById(R.id.mascota_raza);
        v_mascota_text_raza = (TextView) dialog_info_perdido.findViewById(R.id.text_rasgos);
        v_mascota_rasgos = (TextView) dialog_info_perdido.findViewById(R.id.mascota_rasgos);
        v_usuario_owner = (TextView) dialog_info_perdido.findViewById(R.id.usuario_owner);
        v_foto_mascota = (CircularImageView) dialog_info_perdido.findViewById(R.id.foto_mascota);
        v_telefono = (TextView) dialog_info_perdido.findViewById(R.id.usuario_telefono);
        v_btn_visto = (Button)dialog_info_perdido.findViewById(R.id.btn_accept);



        DatabaseReference info_mascota = FirebaseDatabase.getInstance().getReference("usuarios").child(owner).child("mascotas").child(id);
        info_mascota.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatosMascota d_mascota = dataSnapshot.getValue(DatosMascota.class);

                v_mascota_nombre.setText(d_mascota.getNombre());
                v_mascota_raza.setText(d_mascota.getRaza());
                markerid = d_mascota.getMarker_id();
                url_foto_mascota = d_mascota.getUrl_foto();
                DatabaseReference info_marker = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas").child(markerid);
                info_marker.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Marcadores_perdidos mark = dataSnapshot.getValue(Marcadores_perdidos.class);
                        v_telefono.setText(mark.getTelefono());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if (d_mascota.getRasgos().length() > 2) {
                    v_mascota_rasgos.setText(d_mascota.getRasgos());
                } else {
                    v_mascota_text_raza.setVisibility(View.INVISIBLE);
                    v_mascota_rasgos.setVisibility(View.INVISIBLE);
                }
                Picasso.get().load(d_mascota.getUrl_foto()).into(v_foto_mascota);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference info_owner = FirebaseDatabase.getInstance().getReference("usuarios").child(owner);
        info_owner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DB_Datos_Perfil d_perfil = dataSnapshot.getValue(DB_Datos_Perfil.class);
                v_usuario_owner.setText(d_perfil.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /** FUNCIONES ON CLIK INFO PERDIDA */

        v_icon_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BorrarMarcador(id_marcador, dialog_info_perdido, 1);
            }
        });
        v_telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealizarLlamada(v_telefono.getText().toString());
            }
        });

        v_btn_visto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setMessage("Va a mover el marcador a su posición actual"); // FALTA PARSEAR
                builder.setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference change_marker = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas").child(markerid);
                                LatLng actual = new LatLng(mGoogleMap.getMyLocation().getLatitude(), mGoogleMap.getMyLocation().getLongitude());
                                change_marker.child("latitud").setValue(actual.latitude);
                                change_marker.child("longitud").setValue(actual.longitude);
                                dialog_info_perdido.dismiss();
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

        v_foto_mascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog_foto_mascota = new Dialog(getContext(), R.style.Theme_Dialog_Translucent);
                dialog_foto_mascota.setContentView(R.layout.dialog_mapa_info_foto);
                dialog_foto_mascota.setCanceledOnTouchOutside(true);
                dialog_foto_mascota.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#25000000")));
                dialog_foto_mascota.show();

                ImageView v_foto = (ImageView) dialog_foto_mascota.findViewById(R.id.img_mascota);
                ImageView v_cerrar = (ImageView) dialog_foto_mascota.findViewById(R.id.icon_cerrar);

                Picasso.get().load(url_foto_mascota).into(v_foto);
                v_cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_foto_mascota.hide();
                    }
                });
            }
        });



    }

    private void InfoDialogoPaseo() {

        final SwipeDismissDialog dialog_info_paseo = new SwipeDismissDialog.Builder(getContext())
                .setLayoutResId(R.layout.dialog_mapa_info_paseo)
                .build()
                .show();
        /*
        final Dialog dialog_info_paseo = new Dialog(getContext(), R.style.Theme_Dialog_Translucent);
        dialog_info_paseo.setContentView(R.layout.dialog_mapa_info_paseo);
        dialog_info_paseo.setCanceledOnTouchOutside(true);
        dialog_info_paseo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#25000000")));
        dialog_info_paseo.show();
        */

        v_usuario_owner = (TextView) dialog_info_paseo.findViewById(R.id.usuario_owner);
        final TextView v_text_comentario = (TextView) dialog_info_paseo.findViewById(R.id.text_comentario);

        v_icon_borrar = (ImageView) dialog_info_paseo.findViewById(R.id.icon_borrar);

        if (owner.equals(firebaseAuth.getCurrentUser().getUid())) {
            v_icon_borrar.setVisibility(View.VISIBLE);
        } else {
            v_icon_borrar.setVisibility(View.INVISIBLE);
        }

        Log.e("ID","> "+id_marcador);
        DatabaseReference info_mascota = FirebaseDatabase.getInstance().getReference("marcadores").child("paseo").child(id_marcador);
        info_mascota.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Marcadores_paseo d_info_paseo = dataSnapshot.getValue(Marcadores_paseo.class);

                v_text_comentario.setText(d_info_paseo.getComentario());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference info_owner = FirebaseDatabase.getInstance().getReference("usuarios").child(owner);
        info_owner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DB_Datos_Perfil d_perfil = dataSnapshot.getValue(DB_Datos_Perfil.class);
                v_usuario_owner.setText(d_perfil.getNombre());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        v_icon_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BorrarMarcador(id_marcador, dialog_info_paseo, 2);
            }
        });


    }

    /**
     *  LLAMAR POR TELEFONO
     */

    public void RealizarLlamada(String telf) {
        final int request = 1;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+telf));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, request);
            } else {
                startActivity(callIntent);
            }
        }
    }

    /**
     * BORRAR MARCADOR
     */

    private void BorrarMarcador(final String id_marcador_total, final SwipeDismissDialog dialog_total, final int tipo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.dialog_delete_marker_title));
        builder.setMessage(getString(R.string.dialog_delete_marker_body));
        builder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference borrar_marcador = null;
                        switch (tipo) {
                            case 1:
                                borrar_marcador = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas").child(id_marcador_total);
                                break;
                            case 2:
                                borrar_marcador = FirebaseDatabase.getInstance().getReference("marcadores").child("paseo").child(id_marcador_total);
                                break;
                            case 3:
                                borrar_marcador = FirebaseDatabase.getInstance().getReference("marcadores").child("encontradas").child(id_marcador_total);
                                break;
                        }
                        borrar_marcador.removeValue();
                        CogerTodosMarcadores();
                        dialog_total.dismiss();
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

    /**
     * IR A MI UBICACION BOTON FAB
     */

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
                marcadores_rojo_id.clear();
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
            map_marcador.setTag(marcadores_rojo.get(i).getId_mascota() + "##" + marcadores_rojo.get(i).getOwner() + "##1##" + marcadores_rojo_id.get(i));
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
                marcadores_rojo_id.clear();
                for (DataSnapshot dato : dataSnapshot.getChildren()) {
                    Marcadores_paseo mpaseo = dato.getValue(Marcadores_paseo.class);
                    marcadores_azul.add(mpaseo);
                    marcadores_azul_id.add(dato.getKey());
                }
                MeterMarcadoresAzul();
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    /**
     * CREAR MARCADORES EN EL MAPA PASO VERDE
     */

    public void CogerMarcadoresOtra() {

        all_marcadores = FirebaseDatabase.getInstance().getReference("marcadores").child("encontradas");
        all_marcadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                marcadores_verdes = new ArrayList<>();
                marcadores_rojo_id.clear();
                for (DataSnapshot dato : dataSnapshot.getChildren()) {
                    Marcadores_otras mpaseo = dato.getValue(Marcadores_otras.class);
                    marcadores_verdes.add(mpaseo);
                    marcadores_verdes_id.add(dato.getKey());
                }
                MeterMarcadoresVerde();
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    public void MeterMarcadoresVerde() {
        for (int i = 0; i < marcadores_verdes.size(); i++) {
            LatLng ubi = new LatLng(marcadores_verdes.get(i).getLatitud(), marcadores_verdes.get(i).getLongitud());

            Marker map_marcador = mGoogleMap.addMarker(new MarkerOptions()
                    .position(ubi)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marcador_verde)));
            Log.e("TTT", "> " + marcadores_verdes_id.get(i));
            map_marcador.setTag("nada##" + marcadores_verdes.get(i).getMarker_id() + "##3##" + marcadores_verdes_id.get(i));
        }

    }
    public void MeterMarcadoresAzul() {
        for (int i = 0; i < marcadores_azul.size(); i++) {
            LatLng ubi = new LatLng(marcadores_azul.get(i).getLatitud(), marcadores_azul.get(i).getLongitud());

            Marker map_marcador = mGoogleMap.addMarker(new MarkerOptions()
                    .position(ubi)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marcador_azul)));
            Log.e("TTT", "> " + marcadores_azul_id.get(i));
            map_marcador.setTag("nada##" + marcadores_azul.get(i).getOwner() + "##2##" + marcadores_azul_id.get(i));
        }

    }

    /**
     * METODO PARA ACTUALIZAR Y CARGAR LOS MARCADORES EN EL MAPA
     */

    private void CogerTodosMarcadores() {

        DatabaseReference marcadores_todos = FirebaseDatabase.getInstance().getReference("marcadores");

        marcadores_todos.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                mGoogleMap.clear();
                CogerMarcadoresPerdidas();
                CogerMarcadoresPaseo();
                CogerMarcadoresOtra();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mGoogleMap.clear();
                CogerMarcadoresPerdidas();
                CogerMarcadoresPaseo();
                CogerMarcadoresOtra();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mGoogleMap.clear();
                CogerMarcadoresPerdidas();
                CogerMarcadoresPaseo();
                CogerMarcadoresOtra();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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