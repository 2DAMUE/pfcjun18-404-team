package com.team.a404.a404team.HomeActivities.PerfilMascota;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
    private DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference("usuarios")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mascotas");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final int PICK_IMAGE_REQUEST = 100;
    private FloatingActionButton v_fab_borrar, v_fab_editar;
    private FloatingActionsMenu v_fab_menu;
    private FrameLayout v_fondo;

    private AlertDialog.Builder builder;
    private AlertDialog dialog_confirmar;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_mascota);
        getSupportActionBar().hide();

        v_fab_menu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        v_fab_borrar = (FloatingActionButton) findViewById(R.id.fab_borrar);
        v_fondo = (FrameLayout) findViewById(R.id.fondo);

        v_fondo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (v_fab_menu.isExpanded()) {
                    v_fab_menu.collapse();
                }
                return false;
            }
        });


        fotomascota = (CircularImageView) findViewById(R.id.imagen_mascota);
        getSupportActionBar().setTitle(R.string.pet_data);
        pnombre = (TextView) findViewById(R.id.pet_name);
        praza = (TextView) findViewById(R.id.pet_race);
        prasgos = (TextView) findViewById(R.id.pet_details);
        v_fab_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(InfoMascota.this);
                builder.setCancelable(true);
                builder.setTitle("Ventana de confirmación");
                builder.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMascota();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
        deleteData.removeValue();
        Toast.makeText(InfoMascota.this, getString(R.string.toast_pet_deleted), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(InfoMascota.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void deleteMarcador(){

        DatabaseReference deleteMarker = FirebaseDatabase.getInstance().getReference("marcadores").child("perdidas").child(marker_id);
        deleteMarker.removeValue();
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getInstance().getReference().child("images").child(firebaseAuth.getCurrentUser().getUid()).child("mascotas").child(nombre).child(nombre);
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

