package com.example.unitkotekeymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Register extends AppCompatActivity {
    EditText mFullName, mRank, mMobile, mEmail, mUnit, mPassword, mOtp;
    Button mRegisterBtn,mDobButton, mAppointment, mOtpBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseAuth fAuth2;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    FirebaseFirestore fStore2;
    public static String userID;
    String appointmentVal = "";
    String[] appointments ={"None","CO","2IC", "Adjutant","Quarter Master", "Duty Officer"};
    String otpSent;
    String otpEntered;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initDatePicker();
        //connect variables to UI
        mFullName = findViewById(R.id.fullName);
        mRank = findViewById(R.id.rank);
        mMobile =findViewById(R.id.mobile);
        mEmail = findViewById(R.id.email);
        mUnit = findViewById(R.id.unit);
        mOtp = findViewById(R.id.otpEditText);
        mDobButton= findViewById(R.id.datePickerBtn);
        mAppointment = findViewById(R.id.appointmentBtn);
        mPassword = findViewById(R.id.password);
        mRegisterBtn =findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.createText);
        mOtpBtn = findViewById(R.id.otpBtn);

        fAuth = FirebaseAuth.getInstance();
        fAuth2 = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStore2 = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);



        if(fAuth.getCurrentUser() != null){
            Toast.makeText(Register.this, "User already logged in! Please log out to register new account.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOtp.setText("");
                progressBar.setVisibility(View.VISIBLE);
                Random random = new Random();
                int code = random.nextInt(7999)+1000;
                otpSent = String.valueOf(code);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String date = simpleDateFormat2.format(calendar.getTime());
                String creationDate = date;

                String adminEmail = "ron@email.com";
                String adminPassword = "123456";
                fAuth2.signInWithEmailAndPassword(adminEmail,adminPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DocumentReference documentReference2 = fStore2.collection("OTP").document("OTP Code");
                            Map<String,Object> user1 = new HashMap<>();
                            user1.put("Appointment", appointmentVal);
                            user1.put("Code", otpSent);
                            user1.put("Code Creation Date", creationDate);
                            documentReference2.set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "OTP sent to admin database!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    FirebaseAuth.getInstance().signOut();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, " Could Not sent OTP to admin database!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    FirebaseAuth.getInstance().signOut();
                                }
                            });
                        }
                        else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        mAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Select Appointment:");
                builder.setSingleChoiceItems(appointments, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appointmentVal = appointments[which];
                    }
                });
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAppointment.setText(appointmentVal);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Register.this, "Error! Could not save selected appointment!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if all fields are filled or not.
                String fullName = mFullName.getText().toString();
                String rank = mRank.getText().toString();
                String mobile = mMobile.getText().toString();
                String email = mEmail.getText().toString().trim();
                String unit = mUnit.getText().toString();
                String dob = mDobButton.getText().toString();
                String password = mPassword.getText().toString().trim();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                String date = simpleDateFormat2.format(calendar.getTime());
                String RegDate = date;

                if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Name Is Required!");
                    return;
                }
                if(TextUtils.isEmpty(rank)){
                    mRank.setError("Rank Is Required!");
                    return;
                }
                if(TextUtils.isEmpty(mobile)){
                    mMobile.setError("Phone number Is Required!");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Is Required!");
                    return;
                }
                if(TextUtils.isEmpty(unit)){
                    mUnit.setError("Unit Is Required!");
                    return;
                }
                if(TextUtils.isEmpty(dob)){
                    mDobButton.setError("DOB Is Required!");
                    return;
                }
                if(TextUtils.isEmpty(appointmentVal)){
                    mAppointment.setError("Appointment Is Required!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password Is Required ");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password Should Be 6 or more characters long!");
                    return;
                }
                //Start the progress bar
                progressBar.setVisibility(View.VISIBLE);

                        otpEntered = mOtp.getText().toString();
                        if(otpEntered.equals(otpSent)){
                            otpSent = "iurfhurg22hu4hfuoehuqr23ou3brgjvvyYD4RDTUgotyi37th"; //reset OTP
                            mOtp.setText("");//reset OTP box

                            //If OTP is correct, register user to firebase.
                            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        userID = "Profile " + mAppointment.getText().toString();
                                        DocumentReference documentReference = fStore.collection("Users").document(userID);
                                        Map<String,Object> user = new HashMap<>();
                                        user.put("Name",fullName);
                                        user.put("Email", email);
                                        user.put("Rank", rank);
                                        user.put("Phone",mobile);
                                        user.put("Unit", unit);
                                        user.put("DOB", dob);
                                        user.put("Appointment", appointmentVal);
                                        user.put("Date", RegDate);
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Register.this, "Profile Info Saved To Database!", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Register.this, " Could Not Save Profile Info To Database!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        startActivity(new Intent(getApplicationContext(), RegSuccessful.class));
                                    }
                                    else{
                                        Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        otpSent = "iurfhurg22hu4hfuoehuqr23ou3brgjvvyYD4RDTUgotyi37th"; //reset OTP
                                        mOtp.setText("");//reset OTP box
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            Toast.makeText(Register.this, "Error! Entered OTP is not correct", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;
                String date= makeDateString(dayOfMonth,month,year);
                mDobButton.setText(date);
            }
        };
        Calendar cal=Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);
        int month= cal.get(Calendar.MONTH);
        int day= cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return dayOfMonth + "/" + month + "/" + year;
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}