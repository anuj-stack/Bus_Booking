package com.whatsapp.lnmbusservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class confirmation extends AppCompatActivity {

    TextView arrival, dept, time, roll, date;

    Button button;

    String Arrival,Dept,Times;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation);

        arrival = findViewById(R.id.conf_arrival);
        dept = findViewById(R.id.conf_dept);
        time = findViewById(R.id.conf_time);
        roll = findViewById(R.id.conf_roll);
        date = findViewById(R.id.conf_date);

        button = findViewById(R.id.back_bus);

        Arrival = getIntent().getStringExtra("Arrival_conf");
        Dept = getIntent().getStringExtra("Dept_conf");
        Times = getIntent().getStringExtra("Time_conf");

        arrival.setText(Arrival);
        dept.setText(Dept);
        time.setText(Times);

        firebaseAuth = FirebaseAuth.getInstance();

        String email = firebaseAuth.getCurrentUser().getEmail();

        String[] split = email.split("@");
        String name = split[0];

        roll.setText(name);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        date.setText(thisDate);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(confirmation.this, bookBus.class);
                startActivity(intent);
            }
        });


    }
}