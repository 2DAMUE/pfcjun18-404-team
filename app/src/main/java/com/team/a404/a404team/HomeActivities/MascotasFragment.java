package com.team.a404.a404team.HomeActivities;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.team.a404.a404team.Datos.AnuncioInformation;
import com.team.a404.a404team.R;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MascotasFragment extends Fragment {
    private FloatingActionButton nuevaMascota;
    private ListView listaMascotas;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference DataRef;
    private EditText nombre, raza, descripcion;
    private CircularImageView imageNueva;
    private Button saveButton;
    private ArrayList<AnuncioInformation> informacionMascotas = new ArrayList<>();
    private ArrayList<String> nombreMascotas = new ArrayList<>();
    private String nom_mascota;
    private static final int PICK_IMAGE_REQUEST = 100;
    Uri imageUri;

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
        listaMascotas = (ListView) rootView.findViewById(R.id.listmascotas);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombreMascotas);
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

        nuevaMascota = (FloatingActionButton) rootView.findViewById(R.id.nuevaMascota);
        nuevaMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                nom_mascota = "";
                dialog.setContentView(R.layout.dialog_nueva_macota);
                dialog.show();
                final DatabaseReference datosMascota = FirebaseDatabase.getInstance().getReference("usuarios")
                        .child(firebaseAuth.getCurrentUser().getUid()).child("mascotas");
                nombre = (EditText) dialog.findViewById(R.id.nombre_m);
                raza = (EditText) dialog.findViewById(R.id.raza_m);
                descripcion = (EditText) dialog.findViewById(R.id.descripcion_m);
                saveButton = (Button) dialog.findViewById(R.id.btn_enviar);
                imageNueva = (CircularImageView) dialog.findViewById(R.id.img_nuevamascota);
                imageNueva.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nom_mascota = nombre.getText().toString().trim();
                        if (TextUtils.isEmpty(nom_mascota)
                                && TextUtils.isEmpty(raza.getText().toString())
                                && TextUtils.isEmpty(descripcion.getText().toString())) {
                            Toast.makeText(getContext(), "Introduzca primero los datos de la mascota.", Toast.LENGTH_SHORT).show();
                        } else {
                            OpenGallery();
                        }
                    }
                });
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nom = nombre.getText().toString().trim();
                        String rac = raza.getText().toString().trim();
                        String desc = descripcion.getText().toString().trim();
                        datosMascota.child(nom).child("nombre").setValue(nom);
                        datosMascota.child(nom).child("raza").setValue(rac);
                        datosMascota.child(nom).child("rasgos").setValue(desc);
                        listaMascotas.invalidateViews();
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });
        return rootView;
    }

    private void OpenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getInstance().getReference().child("images").child(firebaseAuth.getCurrentUser().getUid())
                .child("mascotas").child(nom_mascota).child(nom_mascota + ".jpg");
        if (resultcode == RESULT_OK && requestcode == PICK_IMAGE_REQUEST) {
            imageUri = data.getData();
            imageNueva.setImageURI(imageUri);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] img = baos.toByteArray();
            UploadTask uploadTask = storageRef.putBytes(img);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    try {
                        DataRef.child(nom_mascota).child("url_foto").setValue(downloadUrl.toString());
                    } catch (NullPointerException e) {
                        Log.d("WWW", e.getMessage());
                    }
                    Log.d("downloadUrl-->", "" + downloadUrl);
                }
            });
        }
    }
}
