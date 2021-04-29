package com.example.unitkotekeymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Authentication extends AppCompatActivity {
    Button approveBtn;
    Button declineBtn;
    Button refreshBtn;
    ProgressBar progressBar;
    TextView nameText;
    TextView appointmentText;
    TextView keyText;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String fullName;
    String appointment;
    String key;
    String date;
    String time;
    String result;


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
                fullName = value;
                nameText.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Authentication.this, "Failed to load Name data!", Toast.LENGTH_SHORT).show();
            }
        });

        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value1 = dataSnapshot.getValue(String.class);
                appointment = value1;
                appointmentText.setText(value1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Authentication.this, "Failed to load Appointment data!", Toast.LENGTH_SHORT).show();
            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value2 = dataSnapshot.getValue(String.class);
                key = value2;
                keyText.setText(value2);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Authentication.this, "Failed to load Key/Box data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        approveBtn = findViewById(R.id.approveBtn);
        declineBtn = findViewById(R.id.declineBtn);
        refreshBtn = findViewById(R.id.goHomeBtn);
        progressBar = findViewById(R.id.progressBar3);
        nameText = findViewById(R.id.nameText);
        appointmentText = findViewById(R.id.appointmentText);
        keyText = findViewById(R.id.keyText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("ACTION");

                myRef.setValue("Approved").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), Approved.class));
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
                        date = Note.getActionDate();
                        time = Note.getActionTime();
                        userID = simpleDateFormat2.format(calendar.getTime());
                        result = "Approved";
                        DocumentReference documentReference = fStore.collection("Logs").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("Name",fullName);
                        user.put("Appointment", appointment);
                        user.put("Key", key);
                        user.put("Date", date);
                        user.put("Result", result);
                        user.put("Time", time);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Authentication.this, "Action saved to Log Data.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Authentication.this, "Error! Action could not be saved to Log Data.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Authentication.this, "Error! Could not approve request.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("ACTION");

                myRef.setValue("Declined").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), Declined.class));
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
                        date = simpleDateFormat.format(calendar.getTime());
                        result = "Declined";
                        time = simpleDateFormat1.format(calendar.getTime());
                        userID = simpleDateFormat2.format(calendar.getTime());
                        DocumentReference documentReference = fStore.collection("Logs").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("Name",fullName);
                        user.put("Appointment", appointment);
                        user.put("Key", key);
                        user.put("Date", date);
                        user.put("Result", result);
                        user.put("Time", time);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Authentication.this, "Action saved to Log Data.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Authentication.this, "Error! Action could not be saved to Log Data.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        progressBar.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Authentication.this, "Error! Could not decline request.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("PERSON");
                DatabaseReference myRef1 = database.getReference("APPOINTMENT");
                DatabaseReference myRef2 = database.getReference("KEY_NAME");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        nameText.setText(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(Authentication.this, "Failed to load Name data!", Toast.LENGTH_SHORT).show();
                    }
                });

                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        appointmentText.setText(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(Authentication.this, "Failed to load Appointment data!", Toast.LENGTH_SHORT).show();
                    }
                });

                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        keyText.setText(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(Authentication.this, "Failed to load Key/Box data!", Toast.LENGTH_SHORT).show();
                    }
                });

                progressBar.setVisibility(View.GONE);

            }

        });
    }
}