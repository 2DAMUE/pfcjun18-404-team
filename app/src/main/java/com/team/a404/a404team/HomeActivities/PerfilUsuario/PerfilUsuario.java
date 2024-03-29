package com.team.a404.a404team.HomeActivities.PerfilUsuario;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.team.a404.a404team.Datos.UserInformation;
import com.team.a404.a404team.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.*;

public class PerfilUsuario extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
    private Button guarda;
    private String photo;
    private EditText nomb, email;
    private ImageView imagenperfil;
    private CircularImageView imagen;
    private static final int PICK_IMAGE_REQUEST = 100;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseUser usuario = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        nomb = findViewById(R.id.nombremodifica);
        email = findViewById(R.id.correoElectronicomodifica);
        guarda = findViewById(R.id.guarda);
        imagenperfil = (ImageView) findViewById(R.id.imagenperfil);
        imagen = (CircularImageView)findViewById(R.id.imagen);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();

            }
        });
        DataRef.child(usuario.getUid());
        DataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                ArrayList<UserInformation> userlist = new ArrayList<UserInformation>();
                FirebaseUser usero = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("usuarios/"+usero.getUid());
                //userlist.add(user);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        UserInformation user = dataSnapshot.getValue(UserInformation.class);
                        Log.v("MSG",""+user.getUsername());
                        nomb.setText(user.getNombre());
                        email.setText(user.getEmail());
                        photo = user.getUrlphoto();
                        if(!TextUtils.isEmpty(photo)){
                            Picasso.get().load(photo).into(imagen);
                        }else{
                            Picasso.get().load(R.drawable.avatarpic).into(imagen);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        guarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuardaDatos();
            }
        });


    }

    private void OpenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images").child(firebaseAuth.getCurrentUser().getUid()).child("userphoto.jpg");
        if (resultcode == RESULT_OK && requestcode == PICK_IMAGE_REQUEST) {
            imageUri = data.getData();
            imagen.setImageURI(imageUri);
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
                    DataRef.child(usuario.getUid()).child("urlfoto").setValue(downloadUrl.toString());
                    Log.d("downloadUrl-->", "" + downloadUrl);
                    Snackbar.make(guarda, getString(R.string.save_data), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
    }
    private void GuardaDatos() {
        String nombre = nomb.getText().toString().trim();
        String em = email.getText().toString().trim();
        DataRef.child(usuario.getUid()).child("nombre").setValue(nombre);
        DataRef.child(usuario.getUid()).child("email").setValue(em);
        Snackbar.make(guarda, getString(R.string.guardadatos), Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}