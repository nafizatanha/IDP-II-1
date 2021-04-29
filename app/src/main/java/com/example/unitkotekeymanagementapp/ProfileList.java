package com.example.unitkotekeymanagementapp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




public class ProfileList extends AppCompatActivity {

    Button homeBtn, selectBtn,loadBtn;
    String appointmentVal = "";
    String[] appointments ={"None","CO","2IC", "Adjutant","Quarter Master", "Duty Officer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        homeBtn = findViewById(R.id.goHomeBtn);
        selectBtn = findViewById(R.id.selectBtn);
        loadBtn = findViewById(R.id.loadListBtn);



        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }

            private void showOptionsDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileList.this);
                builder.setTitle("Select Profile:");
                builder.setSingleChoiceItems(appointments, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appointmentVal = appointments[which];
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        selectBtn.setText(appointmentVal);

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ProfileList.this, "Error! You did not select any appointment!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register.userID = "Profile " + selectBtn.getText().toString();
                startActivity(new Intent(getApplicationContext(), RegSuccessful.class));
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }


}