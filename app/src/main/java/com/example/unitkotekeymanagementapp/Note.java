
package com.example.unitkotekeymanagementapp;
        import com.google.firebase.firestore.Exclude;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;

public class Note {
    private String documentId;
    private String Name;
    private String Rank;
    private String DOB;
    private String  Email;
    private String  Phone;
    private String Unit;
    private String Key;
    private String Date;
    private String Time;
    private String Appointment;
    private String Result;


    public Note() {
        //public no-arg constructor needed
    }
    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public Note(String Name, String Rank, String DOB, String Email, String Phone, String Unit, String Appointment,
                String Key,String Date,String Time,String Result) {
        this.Name = Name;
        this.Rank = Rank;
        this.DOB = DOB;
        this.Email = Email;
        this.Phone = Phone;
        this.Unit = Unit;
        this.Key = Key;
        this.Date = Date;
        this.Time   =Time;
        this.Appointment = Appointment;
        this.Result = Result;
    }
    public String getName() {
        return Name;
    }
    public String getRank() {
        return Rank;
    }
    public String getDob() {
        return DOB;
    }
    public String getEmail() {
        return Email;
    }
    public String getPhone() {
        return Phone;
    }
    public String getUnit() {
        return Unit;
    }
    public String getAppointment() {
        return Appointment;
    }
    public String getKey() {
        return Key;
    }
    public String getDate() {
        return Date;
    }
    public String getTime() {
        return Time;
    }
    public String getResult() {
        return Result;
    }
    public static String getUserId()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());
        String time = simpleDateFormat1.format(calendar.getTime());
        return date + time;
    }
    public static String getActionDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        String date = simpleDateFormat.format(calendar.getTime());
        return date.toString();
    }
    public static String getActionTime()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat1.format(calendar.getTime());
        return time.toString();
    }


}