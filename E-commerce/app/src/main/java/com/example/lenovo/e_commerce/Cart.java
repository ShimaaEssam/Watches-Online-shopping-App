package com.example.lenovo.e_commerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Cart extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    DB db=new DB(this);
    Cursor cursor=null;
    String username;
    String text;
    String [] array;
    String [] items;
    int amount;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        DB db = new DB(this);

        Button save=(Button)findViewById(R.id.button);
        Button calc=(Button)findViewById(R.id.calc) ;
        Button gps=(Button)findViewById(R.id.gps);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
         username=getIntent().getExtras().getString("name");
         listView = (ListView)findViewById(R.id.listview1);
        registerForContextMenu(listView);
        cursor=db.getCartDetails(username);
        if(cursor!=null)
        {
            while(!cursor.isAfterLast())
            {
                adapter.add("product: "+cursor.getString(0)+" price: "+cursor.getString(1)+" amount: "+cursor.getString(2));
                cursor.moveToNext();
            }
        }
        cursor.moveToFirst();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openContextMenu(view);
                text = ((TextView)view).getText().toString();
                array=text.split(" ");

            }
        });
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] items = new String[adapter.getCount()];
                String [] temp = new String[items.length];
                int total=0;
                for (int i=0;i<adapter.getCount();i++)
                {
                    items[i]=adapter.getItem(i).toString();
                    temp = items[i].split(" ");
                    total+=((Integer.parseInt(temp[3]))*(Integer.parseInt(temp[5])));
                }
                Toast.makeText(getApplicationContext(),"Total price = "+total,Toast.LENGTH_LONG).show();
            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Cart.this,Order.class);
                i.putExtra("name",username);
                startActivity(i);
                adapter.clear();

            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Cart.this,GPS.class);
                i.putExtra("name",username);
                startActivity(i);

            }
        });

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        getMenuInflater().inflate(R.menu.cartmenu,menu);
    }

    @Override
    protected void onResume() {
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        Cursor cur = db.getCartDetails(username);
        while (!cur.isAfterLast())
        {
            adapter.add("product: "+cur.getString(0)+" price: "+cur.getString(1)+" amount: "+cur.getString(2));
            cur.moveToNext();
        }

        super.onResume();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.deleteItem:
                if(array[5].equals("1")){
               adapter.remove(text);
                db.deleteItemFromCart(array[1],username);
                Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_LONG).show();
                    return true;}
                else{
                    Intent updateInt=new Intent(Cart.this,update.class);
                    updateInt.putExtra("ProductName",array[1]);
                    startActivity(updateInt);
                    return true;
                }


            case R.id.editItem:
                Intent updateInt=new Intent(Cart.this,update.class);
                updateInt.putExtra("ProductName",array[1]);
                startActivity(updateInt);
                return true;
            default:
                return  false;
        }
    }

}

