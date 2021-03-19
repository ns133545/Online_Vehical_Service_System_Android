package com.example.vehicle_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditProfile extends AppCompatActivity {
    TextView NameChange, PasswordChange, MobileNoChange;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        NameChange = findViewById(R.id.NameChange);
        PasswordChange = findViewById(R.id.passwordChange);
        MobileNoChange = findViewById(R.id.PhoneChange);
        btn = findViewById(R.id.Edit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, UpdateProfile.class);
                startActivity(intent);

            }
        });
    }
}