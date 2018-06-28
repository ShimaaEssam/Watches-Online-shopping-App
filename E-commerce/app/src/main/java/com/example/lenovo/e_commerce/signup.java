package com.example.lenovo.e_commerce;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    Calendar BD;
    int day, month, year;
    TextView text, username, pass, address, g, job, email;
    Button add;
    CheckBox check;
    private DB db;
    CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeView();


        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(signup.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        if (monthOfYear < 10) {
                            monthOfYear = 0 + monthOfYear;
                        }
                        if (dayOfMonth < 10) {
                            dayOfMonth = 0 + dayOfMonth;
                        }
                        text.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(validateForm()){
                    db.Sign_up(username.getText().toString(), pass.getText().toString(), g.getText().toString(), job.getText().toString(), text.getText().toString(), address.getText().toString(), email.getText().toString());
                    Toast.makeText(signup.this, "Signup done", Toast.LENGTH_LONG).show();
                    Toast.makeText(signup.this,  username.getText() + " " + email.getText(), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(signup.this, MainActivity.class);
                    startActivity(i);
                }
            }


        });
    }

    private void initializeView() {
        text = (TextView) findViewById(R.id.textView);
        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.pass);
        address = (EditText) findViewById(R.id.address);
        g = (EditText) findViewById(R.id.Gender);
        job = (EditText) findViewById(R.id.job);
        email = (EditText) findViewById(R.id.Email);
        add = (Button) findViewById(R.id.Addtodb);
        check = (CheckBox) findViewById(R.id.checkBox);
        initializeDatePicker();
    }

    private void initializeDatePicker(){
        db = new DB(getApplicationContext());
        BD = Calendar.getInstance();
        day = BD.get(Calendar.DAY_OF_MONTH);
        month = BD.get(Calendar.MONTH);
        year = BD.get(Calendar.YEAR);
        month = month + 1;
        text.setText(day + "/" + month + "/" + year);
    }


    protected boolean validateEmail(String e) {
        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailpattern);
        Matcher matcher = pattern.matcher(e);
        return matcher.matches();


    }

    protected boolean validatePassword(String p) {
        //return t if pass valid
        if (p != null && p.length() > 5) {
            return true;
        } else
            return false;

    }

    protected boolean validateForm() {
        if (!validateEmail(email.getText().toString())) {
            email.setError("Invalid Email");
            email.requestFocus();
            return false ;
        }
        if (!validatePassword(pass.getText().toString())) {
            pass.setError("Invalid password");
            pass.requestFocus();
            return  false ;
        }

        if (username.getText().toString() == null || username.getText().length() == 0){
            username.setError("Required Field");
            username.requestFocus();
            return  false ;
        }

        if (address.getText().toString() == null || address.getText().length() == 0){
            address.setError("Required Field");
            address.requestFocus();
            return  false ;
        }
        if (job.getText().toString() == null || job.getText().length() == 0){
            address.setError("Required Field");
            address.requestFocus();
            return  false ;
        }
        return true ;
    }

}
