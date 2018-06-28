package com.example.lenovo.e_commerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        final DB db = new DB(this);
        final EditText amounttxt=(EditText)findViewById(R.id.amountupdate);
        final Button savebtn = (Button)findViewById(R.id.saveupdate);
        final String name= getIntent().getExtras().getString("ProductName");

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newamount = Integer.parseInt(amounttxt.getText().toString());
                if(Integer.parseInt(amounttxt.getText().toString())==0){
                    Toast.makeText(getApplicationContext(), "please use delete if you want that", Toast.LENGTH_LONG).show();
                }
                if(Integer.parseInt(amounttxt.getText().toString())<=db.Quantity_check(name) &&Integer.parseInt(amounttxt.getText().toString()) !=0 ){
                        db.updateproduct(name, newamount);
                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "There is just amout =" +db.Quantity_check(name), Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
