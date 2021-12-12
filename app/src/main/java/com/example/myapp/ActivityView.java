package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ActivityView extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private Button deleteBtn;
    private DatabaseReference ref;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        String key = getIntent().getStringExtra("key");

        imageView = findViewById(R.id.NewImageId);
        textView = findViewById(R.id.NewTextId);
        deleteBtn = findViewById(R.id.delete_Btn);
        ref = FirebaseDatabase.getInstance().getReference().child("Cars");
        storageReference = FirebaseStorage.getInstance().getReference().child("Images").child(key+"jpg.");



        ref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    if(snapshot.hasChild("Name") && snapshot.hasChild("Url"))
                    {
                        String name = snapshot.child("Name").getValue().toString();
                        String url = snapshot.child("Url").getValue().toString();

                        textView.setText(name);
                        Picasso.get().load(url).into(imageView);



                    }
                }

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ref.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                Toast.makeText(ActivityView.this, "The item has been deleted......", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });

            }
        });





    }
}