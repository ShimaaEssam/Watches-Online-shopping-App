package com.example.lenovo.e_commerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Categories extends AppCompatActivity {
    DB db=new DB(Categories.this);
    Cursor cursor=null;
    ArrayAdapter<String> arrayAdapter;
    String proname;
    int selectedprodID;
    String username;
    int amount=0;
    TextView txt;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menushoppingcart,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.add:
                cursor.moveToPosition(selectedprodID);
                String n = cursor.getString(0);
                int price= Integer.parseInt(db.getproductprice(n));
                // if price == null
                boolean test=db.checkIfProductExistInCart(n,username);
                if(test==false)
                {
                    int count = db.getAmountOfProductInCart(n,username);
                    if(count==0)
                    {
                        amount=count+1;
                        db.addtoCart(n,price,amount,username);
                        Toast.makeText(getApplicationContext(),"Product added to cart , now amount is "+amount + " currentPrice = " + (amount * price),Toast.LENGTH_LONG ).show();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "This Product is already in shopping cart" , Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "This Product is already in shopping cart" , Toast.LENGTH_LONG).show();

                }


            default:
                return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Button show=(Button)findViewById(R.id.show);
        Button search=(Button)findViewById(R.id.search);
        final ListView listView=(ListView)findViewById(R.id.listView);
        final Spinner s=(Spinner)findViewById(R.id.spinner);
         username=getIntent().getExtras().getString("name");
        txt=(TextView)findViewById(R.id.textlogout) ;
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs =getSharedPreferences(MainActivity.PREFS_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.clear();
                editor.commit();
                username="";
                Intent i=new Intent(Categories.this,MainActivity.class);
                startActivity(i);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Categories.this,search.class);
                i.putExtra("name",username);
                startActivity(i);
            }
        });
        SpinnerAdapter spinnerAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,db.getcategories());
        s.setAdapter(spinnerAdapter);
        registerForContextMenu(listView);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedoption=s.getSelectedItem().toString();
                int selectid=db.getcatID(selectedoption);
                cursor=db.getproductName(selectid);
                arrayAdapter=new ArrayAdapter<String>(Categories.this,android.R.layout.simple_list_item_1);
                listView.setAdapter(arrayAdapter);

                if(cursor!=null)
                {
                    while(!cursor.isAfterLast())
                    {
                        arrayAdapter.add(cursor.getString(0));
                        cursor.moveToNext();
                    }
                }
                cursor.moveToFirst();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openContextMenu(view);
        proname=((TextView)view).getText().toString();
        selectedprodID=position;
    }
});
   show.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent i=new Intent(Categories.this,Cart.class);
           i.putExtra("name",username);
           startActivity(i);
       }
   });
    }
}

