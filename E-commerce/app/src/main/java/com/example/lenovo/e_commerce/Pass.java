package com.example.lenovo.e_commerce;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Pass extends AppCompatActivity {
DB db=new DB(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        final String name= getIntent().getExtras().getString("name");
        final EditText email=(EditText)findViewById(R.id.Email);
        final Button btn=(Button)findViewById(R.id.answer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString()==null || email.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"Please enter your email", Toast.LENGTH_LONG).show();
                }
                else

                {
                        if(email.getText().toString().equals(db.validEmail(name)) )
                        {
                            try{
                                Intent intent=new Intent(Intent.ACTION_SEND);
                                intent.setData(Uri.parse("mailto:"));
                                String[] to={db.validEmail(name)};
                                intent.putExtra(Intent.EXTRA_EMAIL,to);
                                intent.putExtra(Intent.EXTRA_SUBJECT,"Your Password");
                                intent.putExtra(Intent.EXTRA_TEXT,"hey whats up that is your password: "+db.password(name,email.getText().toString()));
                                intent.setType("message/rfc822");
                                startActivity(intent);
                                Toast.makeText(Pass.this, "Please see your mail", Toast.LENGTH_SHORT).show();



                            }
                            catch (Exception e){
                                Toast.makeText(Pass.this, "sorry,can't send mail", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext()," email is incorrect",Toast.LENGTH_LONG).show();
                        }



                }

            }
        });


    }
}
