package com.whatsapp.lnmbusservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class AdminLogin extends AppCompatActivity {

    Button button;
    TextInputLayout textInputLayout;

    String  passwordOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        button = findViewById(R.id.btn_continue_details);
        textInputLayout = findViewById(R.id.password_text_input_login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputLayout.getEditText() != null) {
                    passwordOfUser = textInputLayout.getEditText().getText().toString();
                }

                if (passwordOfUser.isEmpty()) {
                    ShowError(textInputLayout, "Please fill this field");
                }
                else{

                    if(passwordOfUser.equals("2001")){
                        Toast.makeText(AdminLogin.this, "Loged In Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AdminOperations.class);
                        startActivity(intent);
                    }

                    else{
                        Toast.makeText(AdminLogin.this, "The Admin Code is Wrong", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    public static void ShowError(TextInputLayout field, String message) {
        field.setError(message);
    }
}