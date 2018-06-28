package com.example.lenovo.e_commerce;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class order_details extends AppCompatActivity {
Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        final String name=getIntent().getExtras().getString("name");
        ListView l=(ListView)findViewById(R.id.listView3);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
l.setAdapter(adapter);
        DB db = new DB(this);

        cursor=db.displayOrders();
        if(cursor!=null)
        {
            while(!cursor.isAfterLast())
            {
                adapter.add("orderID: "+cursor.getString(0)+" orderDate : "+cursor.getString(1)+" Address : "+cursor.getString(2)+" CustID : "+cursor.getString(3));
                cursor.moveToNext();
            }
        }
        cursor.moveToFirst();

    }
}
