package com.team.a404.a404team.HomeActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.team.a404.a404team.Datos.PerfilUsuario;
import com.team.a404.a404team.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PerfilInfo extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
    private Button button2;
    private CircularImageView imagen;
    Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 100;
    StorageReference storageReference;
    FirebaseUser usuario = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button2 = (Button) findViewById(R.id.button2);
        imagen = findViewById(R.id.imagen);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PerfilInfo.this, PerfilUsuario.class);
                startActivity(intent1);
            }
        });
    }
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
        }
    }
}
