package com.example.unitkotekeymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Declined extends AppCompatActivity {
    TextView nameText;
    TextView appointmentText;
    TextView keyText;
    TextView dateText;
    TextView timeText;
    Button exitBtn;
    String fullName;
    String appointment;
    String key;
    String appDate;
    String appTime;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PERSON");
        DatabaseReference myRef1 = database.getReference("APPOINTMENT");
        DatabaseReference myRef2 = database.getReference("KEY_NAME");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                fullName ="  Name: "+ value;
                nameText.setText(fullName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Declined.this, "Failed to load Name data!", Toast.LENGTH_SHORT).show();
            }
        });

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value1 = dataSnapshot.getValue(String.class);
                appointment = "  Appointment: " + value1;
                appointmentText.setText(appointment);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Declined.this, "Failed to load Appointment data!", Toast.LENGTH_SHORT).show();
            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value2 = dataSnapshot.getValue(String.class);
                key = "  Key: " + value2;
                keyText.setText(key);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Declined.this, "Failed to load Key/Box data!", Toast.LENGTH_SHORT).show();
            }
        });
        appDate = "  Date: " + Note.getActionDate();
        dateText.setText(appDate);
        appTime = "  Time: " + Note.getActionTime();
        timeText.setText(appTime);

        FirebaseDatabase appDatabase = FirebaseDatabase.getInstance();
        DatabaseReference appMyRef = appDatabase.getReference("FLAG");

        appMyRef.setValue("N").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Declined.this, "Flag reset successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Declined.this, "Error! Could not Reset Flag!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declined);
        nameText = findViewById(R.id.decNameText);
        appointmentText = findViewById(R.id.decAppointmentText);
        keyText = findViewById(R.id.decKeyText);
        dateText = findViewById(R.id.decDateText);
        timeText = findViewById(R.id.decTimeText);
        exitBtn = findViewById(R.id.decExitBtn);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}