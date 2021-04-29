package com.example.unitkotekeymanagementapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

public class RegSuccessful extends AppCompatActivity {
    TextView fullName, rank, dob, mobile, email, unit, appointment;
    Button homeBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_successful);
        fullName = findViewById(R.id.fullName);
        rank = findViewById(R.id.rank);
        dob = findViewById(R.id.dob);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        unit = findViewById(R.id.unit);
        appointment = findViewById(R.id.appointment);
        homeBtn = findViewById(R.id.homeBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = Register.userID;


        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    fullName.setText(documentSnapshot.getString("Name"));
                    rank.setText(documentSnapshot.getString("Rank"));
                    dob.setText(documentSnapshot.getString("DOB"));
                    mobile.setText(documentSnapshot.getString("Phone"));
                    email.setText(documentSnapshot.getString("Email"));
                    unit.setText(documentSnapshot.getString("Unit"));
                    appointment.setText(documentSnapshot.getString("Appointment"));
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileList.class));
            }
        });

      Register.userID = "";
    }
}