package com.whatsapp.lnmbusservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.whatsapp.lnmbusservice.AdminLogin.ShowError;

public class user_detail extends AppCompatActivity {

    TextInputLayout inputFirstName, inputLastName, inputBatch, inputGender;

    MaterialButton button;

    String userFirstName, userLastName, userBatch, userGender;

    DatabaseReference databaseReference;

    String mail;
    String roll;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference("user")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.child(roll).exists()){
                            Intent intent = new Intent(user_detail.this, bookBus.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        String[] gender = new String[]{"Male", "Female", "Prefer Not to Say"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.gender_input, gender);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteGender);
        autoCompleteTextView.setAdapter(adapter);

        String[] batch = new String[]{"Y-19", "Y-20", "Y-21", "Y-22"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.batch_input, batch);

        AutoCompleteTextView autoCompleteTextView2 = findViewById(R.id.autoCompleteBatch);
        autoCompleteTextView2.setAdapter(adapter2);

        inputFirstName = findViewById(R.id.input_first_name_details);
        inputLastName = findViewById(R.id.input_last_name_details);
        inputGender = findViewById(R.id.input_gender_details);
        inputBatch = findViewById(R.id.input_batch_details);

        button = findViewById(R.id.btn_continue_details);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        mail = firebaseAuth.getCurrentUser().getEmail();

        String[] split = mail.split("@");
        roll = split[0];

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Insert();

            }
        });

    }

    private void Insert(){

        if (inputFirstName.getEditText() != null && inputLastName.getEditText() != null && inputGender.getEditText() != null && inputBatch.getEditText() != null) {
            userFirstName = inputFirstName.getEditText().getText().toString().toLowerCase();
            userLastName = inputLastName.getEditText().getText().toString().toLowerCase();
            userBatch = inputBatch.getEditText().getText().toString();
            userGender = inputGender.getEditText().getText().toString();
        }

        if (userFirstName.isEmpty()) {
            ShowError(inputFirstName, "Please Fill this field");
            return;
        }
        if (userLastName.isEmpty()) {
            ShowError(inputLastName, "Please Fill this field");
            return;
        }
        if (userGender.isEmpty()) {
            ShowError(inputGender, "Please Fill this field");
            return;
        }
        if (userBatch.isEmpty()) {
            ShowError(inputGender, "Please Fill this field");
            return;
        }

        User user = new User(userFirstName, userLastName, userGender, userBatch);

//        String email = firebaseAuth.getCurrentUser().getEmail();
//        String email = databaseReference.push().getKey();

        databaseReference.child("user")
                .child(roll)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(user_detail.this, "Details Inserted Sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        startActivity(new Intent(user_detail.this, bookBus.class));
        finish();

    }
}