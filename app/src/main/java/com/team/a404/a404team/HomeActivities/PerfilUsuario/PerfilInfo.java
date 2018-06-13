package com.team.a404.a404team.HomeActivities.PerfilUsuario;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.team.a404.a404team.Datos.UserInformation;
import com.team.a404.a404team.R;

import java.io.IOException;

public class PerfilInfo extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
    private String photo;

    private CircularImageView imagen;
    Uri imageUri;
    TextView textView2;
    private static final int PICK_IMAGE_REQUEST = 100;
    StorageReference storageReference;
    FirebaseUser usuario = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView2 = findViewById(R.id.textView2);
        imagen = findViewById(R.id.imagen);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("usuarios/"+usuario.getUid());
        //userlist.add(user);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserInformation user = dataSnapshot.getValue(UserInformation.class);
                Log.v("MSG",""+user.getUsername());
                textView2.setText(user.getUsername());
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

}
