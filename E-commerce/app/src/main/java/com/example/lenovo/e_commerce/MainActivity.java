package com.example.lenovo.e_commerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
        public static String  PREFS_NAME="file";
    public static String PREF_USERNAME="username";
    public static String PREF_PASSWORD="password";
   EditText username;
  EditText Password;
    DB db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Signin_btn=(Button)findViewById(R.id.Signin);
        Button Signup_btn=(Button)findViewById(R.id.Signup);
        CheckBox check=(CheckBox)findViewById(R.id.checkpass);
         username=(EditText)findViewById(R.id.username);
        Password=(EditText)findViewById(R.id.pass);
        final CheckBox checkBox = (CheckBox)findViewById(R.id.rememberme);
        final TextView forget=(TextView)findViewById(R.id.forget);
        db=new DB(this);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        Signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=username.getText().toString();
                String pass=Password.getText().toString();

                boolean test= db.Sign_In_Check(name,pass);
                if(test==true) {
                    if(checkBox.isChecked())
                    {
                        rememberMefunc(name,pass);
                    }



                    Toast.makeText(MainActivity.this, "Sign_In Success", Toast.LENGTH_LONG).show();

                    Intent i=new Intent(MainActivity.this,Categories.class);
                    i.putExtra("name",name);
                    startActivity(i);
                }
                else {
                   Toast.makeText(MainActivity.this, "User name or Password is Wrong", Toast.LENGTH_LONG).show();
                }

            }
        });
        Signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,signup.class);
                startActivity(i);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=username.getText().toString();
                Intent i=new Intent(MainActivity.this,Pass.class);
                i.putExtra("name",userName);
                startActivity(i);
            }
        });
    }
  @Override
    protected void onStart() {
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);

        if (username != null || password != null)
        {
            //  showLogout(username);
            Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();




            Intent catIntent=new Intent(MainActivity.this,Categories.class);
            catIntent.putExtra("name",username);
            startActivity(catIntent);
        }
        super.onStart();
    }
    public void rememberMefunc(String user, String password){
        getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit().putString(PREF_USERNAME,user).putString(PREF_PASSWORD,password).commit();
    }
}

