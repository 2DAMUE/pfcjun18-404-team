package com.team.a404.a404team.HomeActivities.PerfilUsuario;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.team.a404.a404team.HomeActivities.HomeActivity;
import com.team.a404.a404team.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InfoMascota extends AppCompatActivity {
    private CircularImageView fotomascota;
    private TextView pnombre, praza, prasgos;
    private String nombre, raza, rasgos, url_foto,marker_id;
    private Button btn_borrar;
    private DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference("usuarios")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mascotas");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final int PICK_IMAGE_REQUEST = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_mascota);
        fotomascota = (CircularImageView) findViewById(R.id.imagen_mascota);
        getSupportActionBar().setTitle(R.string.pet_data);
        pnombre = (TextView) findViewById(R.id.pet_name);
        praza = (TextView) findViewById(R.id.pet_race);
        prasgos = (TextView) findViewById(R.id.pet_details);
        btn_borrar = (Button) findViewById(R.id.btn_borrar);
        btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMascota();
            }
        });
        fotomascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nombre = extras.getString("nombre");
            raza = extras.getString("raza");
            rasgos = extras.getString("rasgos");
            url_foto = extras.getString("url");
            marker_id = extras.getString("marker_id");
            pnombre.setText(nombre);
            praza.setText(raza);
            prasgos.setText(rasgos);

        }
        if (TextUtils.isEmpty(url_foto)) {
            Picasso.get().load(R.drawable.dog).into(fotomascota);
        } else {
            Picasso.get().load(url_foto).into(fotomascota);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void deleteMascota() {
        final DatabaseReference deleteData = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mascotas").child(nombre);
        if (!TextUtils.isEmpty(url_foto)) {
            StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(url_foto);
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        if(!TextUtils.isEmpty(marker_id)){
            deleteMarcador();
        }
        deleteData.child("nombre").removeValue();
        deleteData.child("raza").removeValue();
        deleteData.child("rasgos").removeValue();
        deleteData.child("url_foto").removeValue();
        deleteData.child("marker_id").removeValue();
        Toast.makeText(InfoMascota.this, getString(R.string.toast_pet_deleted), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(InfoMascota.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void deleteMarcador(){
        DatabaseReference deleteMarker = FirebaseDatabase.getInstance().getReference("marcadores")
                .child("perdidas").child(marker_id);
        deleteMarker.child("id_mascota").removeValue();
        deleteMarker.child("latitud").removeValue();
        deleteMarker.child("longitud").removeValue();
        deleteMarker.child("owner").removeValue();
        deleteMarker.child("telefono").removeValue();
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getInstance().getReference().child("images").child(firebaseAuth.getCurrentUser().getUid())
                .child("mascotas").child(nombre).child(nombre);
        if (resultcode == RESULT_OK && requestcode == PICK_IMAGE_REQUEST) {
            imageUri = data.getData();
            fotomascota.setImageURI(imageUri);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
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
                    DataRef.child(nombre).child("url_foto").setValue(downloadUrl.toString());
                    Log.d("downloadUrl-->", "" + downloadUrl);
                }
            });
        }
    }
}

