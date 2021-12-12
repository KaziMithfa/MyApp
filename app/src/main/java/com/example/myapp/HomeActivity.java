package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private EditText inputEditText;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private FirebaseRecyclerOptions<Car>options;
    private FirebaseRecyclerAdapter<Car,CarViewHolder>adapter;
    private DatabaseReference CarRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inputEditText = findViewById(R.id.searchEditTextId);
        recyclerView = findViewById(R.id.recylerviewId);
        floatingActionButton = findViewById(R.id.add_new_itemId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        CarRef = FirebaseDatabase.getInstance().getReference().child("Cars");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loadData();



    }

    private void loadData() {

        options = new FirebaseRecyclerOptions.Builder<Car>().setQuery(CarRef,Car.class).build();
        adapter = new FirebaseRecyclerAdapter<Car, CarViewHolder>(options) {
            @Override
            protected void onBindViewHolder( CarViewHolder carViewHolder, int i,  Car car) {

                carViewHolder.fetchTextView.setText(car.getName());
                Picasso.get().load(car.getUrl()).into(carViewHolder.fetchImage);
                String key = getRef(i).getKey();


                carViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomeActivity.this,ActivityView.class);
                        intent.putExtra("key",key);
                        startActivity(intent);
                        finish();
                    }
                });

            }


            @Override
            public CarViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.samplelayout,parent,false);

                return new CarViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}