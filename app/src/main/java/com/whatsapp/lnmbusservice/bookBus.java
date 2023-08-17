package com.whatsapp.lnmbusservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class bookBus extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Bus> list;

    DatabaseReference databaseReference;
    busAdapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(bookBus.this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_bus);

        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Bus_Details");

        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new busAdapter(this,list);

        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Bus bus = dataSnapshot.getValue(Bus.class);
                    list.add(bus);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}