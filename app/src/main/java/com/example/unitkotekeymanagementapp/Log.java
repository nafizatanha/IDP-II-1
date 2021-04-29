package com.example.unitkotekeymanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log extends AppCompatActivity {

    String fileName = "";
    String filePath = "";
    String fileContent = "";


    private TextView textViewData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Logs");

    private DatePickerDialog datePickerDialog2;
    private DatePickerDialog datePickerDialog3;
    private  Button dateButton;
    private  Button dateButton2;
    Button searchButton;
    Button printButton;
    ProgressBar progressBar;
    String logDate;
    String logDate2;
    int dateCheck;
    int dateCheck2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        textViewData = findViewById(R.id.scrollTextView3);
        initDatePicker2();
        initDatePicker3();
        printButton = findViewById(R.id.goHomeBtn);
        dateButton = findViewById(R.id.dateBtn);
        dateButton2 = findViewById(R.id.dateBtn2);
        searchButton = findViewById(R.id.searchBtn);
        progressBar = findViewById(R.id.progressBarLog);

        logDate = dateButton.getText().toString();
        logDate2 = dateButton2.getText().toString();



        if(!isExternalStorageAvailableForRW()){
            textViewData.setText("Bal nai");
        }

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( dateCheck2 >= dateCheck){
                    if(TextUtils.isEmpty(logDate)){
                        printButton.setError("Please Select a Starting Date!");
                        return;
                    }
                    if(TextUtils.isEmpty(logDate2)){
                        printButton.setError("Please Select a Ending Date!");
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    notebookRef.get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    String data = "";
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        Note note = documentSnapshot.toObject(Note.class);
                                        note.setDocumentId(documentSnapshot.getId());
                                        String documentId = note.getDocumentId();
                                        String name = note.getName();
                                        String key = note.getKey();
                                        String date = note.getDate();
                                        String result = note.getResult();
                                        String time = note.getTime();
                                        String appointment = note.getAppointment();

                                        for(int i = dateCheck; i<=dateCheck2;i++){
                                            String check = Integer.toString(i);
                                            if(documentId.contains(check)){
                                                data += " Log ID: " + documentId
                                                        + "\n    Name:  " + name +
                                                        "\n    Appointment:  " + appointment + "\n    Requested Key:  "
                                                        + key +"\n    Date:  " + date  + "\n    Time:  " + time
                                                        + "\n    Result:  " + result     +"\n\n";}

                                        }
                                    }
                                    fileContent = data;
                                    if(fileContent.equals("")){
                                        Toast.makeText(Log.this,"No log found for selected time interval.", Toast.LENGTH_LONG).show();
                                        textViewData.setText("");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        textViewData.setText(data);
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                                        String fileNameTemp = simpleDateFormat.format(calendar.getTime());
                                        fileName = "Log_"+ fileNameTemp + ".doc";
                                        filePath = "Logs";

                                        File myExFile = new  File(getExternalFilesDir(filePath),fileName);
                                        FileOutputStream fos = null;
                                        try {
                                            fos = new FileOutputStream(myExFile);
                                            fos.write(fileContent.getBytes());
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(Log.this,"Log Data Saved To file.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(Log.this,"End Date should be later than or same as Starting Date.", Toast.LENGTH_LONG).show();
                }


            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (dateCheck2 >= dateCheck){
                   if(TextUtils.isEmpty(logDate)){
                       searchButton.setError("Please Select a Starting Date!");
                       return;
                   }
                   if(TextUtils.isEmpty(logDate2)){
                       searchButton.setError("Please Select a Ending Date!");
                       return;
                   }
                   progressBar.setVisibility(View.VISIBLE);
                   notebookRef.get()
                           .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                               @Override
                               public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                   String data = "";
                                   for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                       Note note = documentSnapshot.toObject(Note.class);
                                       note.setDocumentId(documentSnapshot.getId());
                                       String documentId = note.getDocumentId();
                                       String name = note.getName();
                                       String key = note.getKey();
                                       String date = note.getDate();
                                       String result = note.getResult();
                                       String time = note.getTime();
                                       String appointment = note.getAppointment();

                                       for(int i = dateCheck; i<=dateCheck2;i++){
                                           String check = Integer.toString(i);
                                           if(documentId.contains(check)){
                                               data += " Log ID: " + documentId
                                                       + "\n    Name:  " + name +
                                                       "\n    Appointment:  " + appointment + "\n    Requested Key:  "
                                                       + key +"\n    Date:  " + date  + "\n    Time:  " + time
                                                       + "\n    Result:  " + result     +"\n\n";}
                                       }
                                   }
                                   if(data.equals("")){
                                       Toast.makeText(Log.this,"No log found for selected time interval.", Toast.LENGTH_LONG).show();
                                       textViewData.setText("");
                                       progressBar.setVisibility(View.GONE);
                                   }
                                   else{
                                   textViewData.setText(data);
                                   progressBar.setVisibility(View.GONE);}
                               }
                           });
               }
               else{
                   Toast.makeText(Log.this,"End Date should be later than or same as Starting Date.", Toast.LENGTH_LONG).show();
               }

            }
        });


    }

    private boolean isExternalStorageAvailableForRW() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    private void initDatePicker3() {
        DatePickerDialog.OnDateSetListener dateSetListener3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               month = month+1;
                String date = makeDateString2(dayOfMonth,month,year);
                int date2 = makeDateInt(dayOfMonth,month,year);
                dateButton2.setText(date);
                dateCheck2 = date2;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog3 = new DatePickerDialog(this, style, dateSetListener3,year,month,day);
        datePickerDialog3.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void initDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = makeDateString2(dayOfMonth,month,year);
                int date2 = makeDateInt(dayOfMonth,month,year);
                dateButton.setText(date);
                dateCheck = date2;
            }
        };

        Calendar cal = Calendar.getInstance();
          int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog2 = new DatePickerDialog(this, style, dateSetListener2,year,month,day);
        datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
    private String makeDateString2(int dayOfMonth, int month, int year) {
        if (month<10){
            return dayOfMonth+"/0"+month+"/"+year;
        }
        else{return dayOfMonth + "/" + month + "/" + year;}
    }

    private int makeDateInt(int dayOfMonth, int month, int year) {
        if (month<10){
            return year*10000+ month*100+ dayOfMonth;
        }
        else{return year*1000+ month*100+ dayOfMonth;}
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null) {
                    return;
                }
                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());
                    String documentId = note.getDocumentId();
                    String name = note.getName();
                    String key = note.getKey();
                    String date = note.getDate();
                    String result = note.getResult();
                    String time = note.getTime();
                    String appointment = note.getAppointment();
                    data += "  Log ID: " + documentId
                            + "\n    Name:  " + name +
                            "\n    Appointment:  " + appointment + "\n    Requested Key:  "
                            + key +"\n    Date:  " + date  + "\n    Time:  " + time
                            + "\n    Result:  " + result     +"\n\n";
                }
                textViewData.setText(data);
            }
        });
    }&*/


    public void openDatePicker2(View view) {
        datePickerDialog2.show();
    }

    public void openDatePicker3(View view) {
        datePickerDialog3.show();
    }

}


