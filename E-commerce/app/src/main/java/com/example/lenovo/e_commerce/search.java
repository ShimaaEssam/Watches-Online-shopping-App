package com.example.lenovo.e_commerce;

import android.content.Intent;
import android.database.Cursor;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;

import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity {
    ListView listview;
    Cursor matched;
    int voiceCode=1;
    EditText editText;
    DB db;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == voiceCode && resultCode == RESULT_OK) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            editText.setText(text.get(0));
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
            arrayAdapter.clear();
            listview.setAdapter(arrayAdapter);
            matched = db.getProductDetails(text.get(0));
            if (matched != null) {
                while (!matched.isAfterLast()) {
                   arrayAdapter.add(matched.getString(1));
                    matched.moveToNext();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
         editText=(EditText)findViewById(R.id.editText);
        listview=(ListView)findViewById(R.id.listView4);
        Button btn=(Button)findViewById(R.id.button2) ;
        ImageView imageView=(ImageView)findViewById(R.id.imageView);
        arrayAdapter = new ArrayAdapter<String>(search.this,android.R.layout.simple_list_item_1);
        db=new DB(getApplicationContext());
        listview.setAdapter(arrayAdapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();
                String pname = editText.getText().toString();
                matched = db.displayproductmatched(pname);
                if (matched == null) {
                    Toast.makeText(getApplicationContext(), "Not Found ", Toast.LENGTH_LONG).show();
                } else {
                    if (matched != null) {
                        while (!matched.isAfterLast()) {

                            arrayAdapter.add(matched.getString(0));

                            matched.moveToNext();
                        }
                       matched.moveToFirst();
                    }
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   {

                       Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                       startActivityForResult(i,voiceCode);

                    }

                }


        });


    }
}

