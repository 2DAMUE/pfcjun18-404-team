package com.team.a404.a404team.HomeActivities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.team.a404.a404team.Datos.AnuncioInformation;
import com.team.a404.a404team.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MascotasFragment extends Fragment {
    private FloatingActionButton nuevaMascota;
    private ListView listaMascotas;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference DataRef;
    private EditText nombre, raza, descripcion;
    private CircularImageView image;
    private Button saveButton;
    private ArrayList<AnuncioInformation> informacionMascotas = new ArrayList<>();
    private ArrayList<String> nombreMascotas = new ArrayList<>();

    public MascotasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mascotas, container, false);
        informacionMascotas.clear();
        nombreMascotas.clear();
        DataRef = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("mascotas");
        listaMascotas = (ListView)rootView.findViewById(R.id.listmascotas);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, nombreMascotas);
        listaMascotas.setAdapter(arrayAdapter);
        listaMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        DataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AnuncioInformation pet = dataSnapshot.getValue(AnuncioInformation.class);
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

        nuevaMascota = (FloatingActionButton)rootView.findViewById(R.id.nuevaMascota);
        nuevaMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_nueva_macota);
                dialog.show();
                final DatabaseReference datosMascota = FirebaseDatabase.getInstance().getReference("usuarios")
                        .child(firebaseAuth.getCurrentUser().getUid()).child("mascotas");
                nombre = (EditText)dialog.findViewById(R.id.nombre_m);
                raza = (EditText)dialog.findViewById(R.id.raza_m);
                descripcion = (EditText)dialog.findViewById(R.id.descripcion_m);
                saveButton = (Button) dialog.findViewById(R.id.btn_enviar);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nom = nombre.getText().toString().trim();
                        String rac = raza.getText().toString().trim();
                        String desc = descripcion.getText().toString().trim();
                        datosMascota.child(nom).child("nombre").setValue(nom);
                        datosMascota.child(nom).child("raza").setValue(rac);
                        datosMascota.child(nom).child("descripcion").setValue(desc);
                        listaMascotas.invalidateViews();
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });
        return rootView;
    }
}
