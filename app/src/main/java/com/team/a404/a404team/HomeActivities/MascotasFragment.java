package com.team.a404.a404team.HomeActivities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.a404.a404team.Datos.DatosMascota;
import com.team.a404.a404team.HomeActivities.PerfilMascota.InfoMascota;
import com.team.a404.a404team.R;

import java.util.ArrayList;

public class MascotasFragment extends Fragment {
    private ImageView nuevaMascota;
    private ListView listaMascotas;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference DataRef;
    private EditText nombre, raza, descripcion;
    private Button saveButton;
    private ArrayList<DatosMascota> informacionMascotas = new ArrayList<>();
    private ArrayList<String> nombreMascotas = new ArrayList<>();


    public MascotasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mascotas, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        informacionMascotas.clear();
        nombreMascotas.clear();
        DataRef = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("mascotas");

        Log.i("INFO",""+DataRef);

        listaMascotas = (ListView) view.findViewById(R.id.listmascotas);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombreMascotas);
        listaMascotas.setAdapter(arrayAdapter);
        listaMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (nombreMascotas.get(i).equals(informacionMascotas.get(i).getNombre())) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            String nombre = informacionMascotas.get(i).getNombre();
                            String raza = informacionMascotas.get(i).getRaza();
                            String descript = informacionMascotas.get(i).getRasgos();
                            String url = informacionMascotas.get(i).getUrl_foto();
                            String idmark = informacionMascotas.get(i).getMarker_id();
                            Intent intent = new Intent(getContext(), InfoMascota.class);
                            intent.putExtra("nombre", nombre);
                            intent.putExtra("raza", raza);
                            intent.putExtra("rasgos", descript);
                            intent.putExtra("url", url);
                            intent.putExtra("marker_id",idmark);
                            startActivity(intent);
                        }
                    }, 1000);


                } else {
                    for (String nom : nombreMascotas) {
                        if (informacionMascotas.get(i).getNombre().equals(nom)) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    String nombre = informacionMascotas.get(i).getNombre();
                                    String raza = informacionMascotas.get(i).getRaza();
                                    String descript = informacionMascotas.get(i).getRasgos();
                                    String url = informacionMascotas.get(i).getUrl_foto();
                                    String idmark = informacionMascotas.get(i).getMarker_id();
                                    Intent intent = new Intent(getContext(), InfoMascota.class);
                                    intent.putExtra("nombre", nombre);
                                    intent.putExtra("raza", raza);
                                    intent.putExtra("rasgos", descript);
                                    intent.putExtra("url", url);
                                    intent.putExtra("marker_id",idmark);
                                    startActivity(intent);
                                }
                            }, 1000);

                        }
                    }
                }
            }
        });
        DataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DatosMascota pet = dataSnapshot.getValue(DatosMascota.class);
                nombreMascotas.add(pet.getNombre());
                informacionMascotas.add(pet);
                arrayAdapter.notifyDataSetChanged();
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
        nuevaMascota = (ImageView) view.findViewById(R.id.nuevaMascota);
        nuevaMascota.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_nueva_macota);
                dialog.show();
                final DatabaseReference datosMascota = FirebaseDatabase.getInstance().getReference("usuarios")
                        .child(firebaseAuth.getCurrentUser().getUid()).child("mascotas");
                nombre = (EditText) dialog.findViewById(R.id.nombre_m);
                raza = (EditText) dialog.findViewById(R.id.raza_m);
                descripcion = (EditText) dialog.findViewById(R.id.descripcion_m);
                saveButton = (Button) dialog.findViewById(R.id.btn_enviar);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nom = nombre.getText().toString().trim();
                        String rac = raza.getText().toString().trim();
                        String desc = descripcion.getText().toString().trim();
                        if (TextUtils.isEmpty(nom) && TextUtils.isEmpty(rac) && TextUtils.isEmpty(desc)) {
                            Toast.makeText(getContext(), getString(R.string.toast_no_pet_data),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            datosMascota.child(nom).child("nombre").setValue(nom);
                            datosMascota.child(nom).child("raza").setValue(rac);
                            datosMascota.child(nom).child("rasgos").setValue(desc);
                            datosMascota.child(nom).child("url_foto").setValue("");
                            datosMascota.child(nom).child("marker_id").setValue("");
                            listaMascotas.invalidateViews();
                            arrayAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), getString(R.string.toast_no_image),
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                });
            }
        });

    }
}
