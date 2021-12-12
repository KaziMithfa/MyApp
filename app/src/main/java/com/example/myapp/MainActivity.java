package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static int RequestCode = 1;

    private ImageView imageView;
    private EditText nameEditText;
    private TextView progressText;
    private ProgressBar progressBar;
    private Button updateBtn;
    private Uri ImageUri;
    boolean imageisClicked = false;
    private String name;
    private DatabaseReference userName;
    private StorageReference userpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageId);
        nameEditText = findViewById(R.id.nameEditTextId);
        progressText = findViewById(R.id.progressTextId);
        progressBar = findViewById(R.id.ProgressBarId);
        updateBtn = findViewById(R.id.update_btn);

        progressText.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        userName = FirebaseDatabase.getInstance().getReference().child("Cars");
        userpic = FirebaseStorage.getInstance().getReference().child("Images");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,RequestCode);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();

                if(name!= null && imageisClicked!= false)
                {
                    UploadInfo(name);
                }
                else{
                    Toast.makeText(MainActivity.this, "Please , insert the name and picture....", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }

    private void UploadInfo(String name) {

        progressText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String key = userName.push().getKey();
        userpic.child(key+"jpg.").putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                userpic.child(key+"jpg.").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String Url = uri.toString();
                        HashMap carHashMap = new HashMap();
                        carHashMap.put("Name",name);
                        carHashMap.put("Url",Url);

                        userName.child(key).setValue(carHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(MainActivity.this, "Your info has been updated successfully......", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                finish();
                               
                            }
                        });
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress( UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                progressText.setText(progress+"%");

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RequestCode && data!= null)
        {
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
            imageisClicked = true;


        }
    }
}