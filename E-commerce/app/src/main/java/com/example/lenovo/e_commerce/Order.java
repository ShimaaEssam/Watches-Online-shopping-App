package com.example.lenovo.e_commerce;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class Order extends AppCompatActivity {
DB db =new DB(this);
    Calendar BD;
    int day, month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Button con=(Button)findViewById(R.id.conf);
        Button ord=(Button)findViewById(R.id.ord);

        ListView l=(ListView)findViewById(R.id.listView2);

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
         final String name=getIntent().getExtras().getString("name");
        TextView text=(TextView)findViewById(R.id.Date);
        BD = Calendar.getInstance();
        day = BD.get(Calendar.DAY_OF_MONTH);
        month = BD.get(Calendar.MONTH);
        year = BD.get(Calendar.YEAR);
        month = month + 1;
        text.setText(day + "/" + month + "/" + year);
db.updateorders(text.getText().toString(),db.getCustomerID(name),db.get_address(name));
        Cursor cursor=db.getCartDetails(name);
        if(cursor!=null)
        {
            while(!cursor.isAfterLast())
            {
                adapter.add("product : "+cursor.getString(0)+" price : "+cursor.getString(1)+" amount : "+cursor.getString(2)+" username : "+name);
                cursor.moveToNext();
            }
        }
        cursor.moveToFirst();
        l.setAdapter(adapter);
        ord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Order.this,order_details.class);
                i.putExtra("name",name);
                startActivity(i);
            }
        });
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    String[] to={db.validEmail(name)};
                    intent.putExtra(Intent.EXTRA_EMAIL,to);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Your Order");
                    intent.putExtra(Intent.EXTRA_TEXT,"hey whats up that is your Address we will sent order in it: "+db.get_address(name));
                    intent.setType("message/rfc822");
                    startActivity(intent);
                    Toast.makeText(Order.this, "Please see your mail", Toast.LENGTH_SHORT).show();


                }
                catch (Exception e){
                    Toast.makeText(Order.this, "sorry,can't send mail", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}

