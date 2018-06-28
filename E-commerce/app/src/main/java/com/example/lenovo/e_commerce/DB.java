package com.example.lenovo.e_commerce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by LENOVO on 12/3/2017.
 */
public class DB extends SQLiteOpenHelper {
    public static String dbname="Ecomm";

    SQLiteDatabase db;
    public DB(Context contex)
    {
        super(contex,dbname,null,43);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Customers (CustID integer primary key autoincrement,username text not null,Password text not null,gender text,Birthdate text,Job text,Address text not null,Email text not null)");
        db.execSQL("create table Orders (OrdID integer primary key autoincrement,Orddate text not null,Address text not null, Cust_ID integer ,FOREIGN KEY (Cust_ID) References  Customers (CustID))");
        db.execSQL("create table OrderDetails (OrdID integer NOT NULL ,ProID integer NOT NULL ,Quantity integer not null,primary key(OrdID,ProID) )");
        db.execSQL("create table Categories (CatID integer primary key autoincrement,CatName text not null)");
        db.execSQL("create table Products (ProID integer primary key autoincrement,ProName text not null,price integer not null,Quantity integer not null, CatID integer ,FOREIGN KEY (CatID) References  Categories (CatID))");
        db.execSQL("create table cart (ProName text not null,price integer not null,Quantity integer not null, username text ,FOREIGN KEY (username) References  Customers (username))");



        db.execSQL("Insert into Categories(CatName)" + "values ('watches For Mens')");
        db.execSQL("Insert into Categories(CatName)" + "values ('watches For Womens')");
        db.execSQL("Insert into Categories(CatName)" + "values ('watches For Kids')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Fossil_FS4835_Leather_Watch-Blue','3395','2','1')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Deisel_DZ1513-Casual_Men_Watch-Brown','1999','2','1')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Fossil_FS5279_Leather_Watch-for_Men-Brown','3295','2','1')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Miyoko_MQ-4401_Leather_Watch-Camel','320','2','1')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Michael_Kors_MK3364_Stainless_Steel_Watch-Silver','2799','2','2')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Fossil_ES3487_Leather_Watch-for_Women-Beige','2595','2','2')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Quartz_BLT-FFF_Stainless_Steel_Watch-Gold','115','2','2')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Q&Q_VR00-05PlasticWatch-Blue','299','2','3')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Q&Q_VQ68-015PResinWatch-Dark Blue','280','2','3')");
        db.execSQL("Insert into Products (ProName,price,Quantity,CatID)"+"values ('Q&Q_VQ68-011PlasticWatch-Yellow','199','2','3')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Customers");
        db.execSQL("drop table if exists Orders");
        db.execSQL("drop table if exists OrderDetails");
        db.execSQL("drop table if exists Categories");
        db.execSQL("drop table if exists  Products");
        db.execSQL("drop table if exists  cart");
        onCreate(db);

    }
    //////////////////////////////mainActivity/////////////////////////////////////////
    public boolean Sign_In_Check(String name,String Pass)
    {
        boolean test=false;
        db=getReadableDatabase();
        String[] rowDetails={"username","Password"};
        String whereCluse = "username =? AND Password =?";
        String[] rowValues = {name , Pass};

        Cursor cursor=db.query("Customers",rowDetails,whereCluse,rowValues,null,null,null);


        if(cursor.getCount() > 0)
            test=true;
        db.close();
        return test;

    }
    //////////////////////////////cart/////////////////////////////////////////
    public Cursor getCartDetails(String username)
    {
        db=getReadableDatabase();

        Cursor cur = db.rawQuery("select * from cart where username = '"+username+"'",null);
        if(cur!= null)
        {
            cur.moveToFirst();
        }
        return  cur;
    }
    public void deleteItemFromCart(String name,String username)
    {
        db=getWritableDatabase();
        db.delete("cart","ProName='"+name+"' and username='"+username+"'",null);
    }
    //////////////////////////////category/////////////////////////////////////////
    public void addtoCart(String name , int price , int amount,String username)
    {
        db=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("ProName",name);
        row.put("price",price);
        row.put("Quantity",amount);
        row.put("username",username);
        db.insert("cart",name,row);

    }
    public String getproductprice(String name)
    {
        db = getReadableDatabase();
        Cursor cur = db.rawQuery("select price from Products where ProName ='"+name+"'",null);
        if(cur.getCount()!=0)
        {
            cur.moveToFirst();

        }
        return  cur.getString(0);

    }
    public boolean checkIfProductExistInCart(String name,String username)
    {
        boolean test=false;
        Cursor cur = db.rawQuery("select * from cart where ProName='"+name+"' and username='"+username+"'",null);
        if(cur.getCount()<=0)
        {
            test=false;
        }
        else
            test=true;

        return test;
    }

    public ArrayList<String> getcategories()
    {
        ArrayList<String> arr= new ArrayList<>();
        db=getReadableDatabase();
        Cursor cur = db.rawQuery("select CatName from Categories",null);
        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            arr.add(cur.getString(0));
            cur.moveToNext();
        }
        return arr;
    }
    public int getAmountOfProductInCart(String name,String username)
    {
        db=getReadableDatabase();
        int amount=0;
        Cursor cur = db.rawQuery("select Quantity from cart "+"where ProName='"+name+"' and username='"+username+"'",null);
        if(cur.getCount()>0)
        {
            cur.moveToFirst();
            amount=cur.getInt(0);

        }
        else

        {
            amount=0;
        }
        return amount;
    }
    public int getcatID(String catName)
    {

        db=getReadableDatabase();
        Cursor cur = db.rawQuery("select CatID from Categories "+"where CatName='"+catName+"'",null);
        if(cur.getCount()!=0)
        {
            cur.moveToFirst();
            return cur.getInt(0);
        }
        return 0;
    }
    public Cursor getproductName(int catid)
    {
        db = getReadableDatabase();
        Cursor cur = db.rawQuery("select ProName from  Products where CatID ="+catid,null);
        if(cur.getCount()!=0)
        {
            cur.moveToFirst();
            return  cur;
        }
        return  cur;

    }
    //////////////////////////////Order/////////////////////////////////////////
    public int getCustomerID(String name)
    {

        db=getReadableDatabase();
        Cursor cur = db.rawQuery("select CustID from Customers "+"where username='"+name+"'",null);
        if(cur.getCount()!=0)
        {
            cur.moveToFirst();
            return cur.getInt(0);
        }
        return 0;
    }
    public void updateorders(String date,int id,String addr)
    {

        ContentValues row=new ContentValues();
        row.put("Orddate",date);
        row.put("Address",addr);
        row.put("Cust_ID",id);
        db=getWritableDatabase();
        db.insert("Orders",null,row);
        db.close();


    }
    public String get_address(String name)
    {
        db=getReadableDatabase();
        Cursor cur = db.rawQuery("select Address from Customers "+"where username='"+name+"'",null);
        if(cur.getCount()!=0)
        {
            cur.moveToFirst();
            return cur.getString(0);
        }
        return null;
    }
    //////////////////////////////order-details/////////////////////////////////////////
    public Cursor displayOrders()
    {

        db=getReadableDatabase();
        Cursor cur = db.rawQuery("select OrdID,Orddate,Address,Cust_ID from Orders ",null);
        if(cur.getCount()!=0)
            if(cur.getCount()!=0)
            {
                cur.moveToFirst();
                return  cur;
            }
        return  cur;
    }
    //////////////////////////////Pass/////////////////////////////////////////
    public String validEmail(String name)
    {

        db=getReadableDatabase();
        Cursor cur = db.rawQuery("select Email from Customers "+"where username='"+name+"'",null);
        if(cur.getCount()!=0)
        {
            cur.moveToFirst();
            return cur.getString(0);
        }
        return null;
    }

    public String password(String name,String email){
        db=getReadableDatabase();
        String[] rowDetails={"Password"};
        String whereCluse = "username =? AND Email =?";
        String[] rowValues = {name , email};

        Cursor cursor=db.query("Customers",rowDetails,whereCluse,rowValues,null,null,null);

        if(cursor.getCount() !=0)
        {
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        db.close();
        return null;

    }
    //////////////////////////////search/////////////////////////////////////////
    public Cursor getProductDetails(String prodName)
    {
        db=getReadableDatabase();
        String [] arr = {prodName};
        Cursor cur = db.rawQuery("select * from Products where ProName like ?",arr);
        if(cur!= null)
        {
            cur.moveToFirst();
        }
        return  cur;
    }
    public Cursor displayproductmatched(String name)
    {

        db=getReadableDatabase();
        String[] rows={"ProName"};
        Cursor cur = db.query("Products",rows, "ProName like ?",new String[]{"%"+name+"%"},null,null,null,null);
        if(cur.getCount()!=0)
            if(cur.getCount()!=0)
            {
                cur.moveToFirst();
                return  cur;
            }
        return  cur;

    }
    //////////////////////////////signup/////////////////////////////////////////
    public void Sign_up(String name,String Pass,String G,String Job,String bd,String addr,String Email)
    {

        ContentValues row=new ContentValues();
        row.put("username",name);
        row.put("Password",Pass);
        row.put("gender",G);
        row.put("Birthdate",bd);
        row.put("Job",Job);
        row.put("Address",addr);
        row.put("Email",Email);
        db=getWritableDatabase();
        db.insert("Customers",null,row);
        db.close();


    }

    //////////////////////////////update/////////////////////////////////////////
    public int Quantity_check(String name)
    {
        db=getReadableDatabase();
        int amount=0;
        Cursor cur = db.rawQuery("select Quantity from Products "+"where ProName='"+name+"'",null);
        if(cur.getCount()>0)
        {
            cur.moveToFirst();
            amount=cur.getInt(0);

        }
        else

        {
            amount=0;
        }
        return amount;
    }
    public void updateproduct(String oldname,int Quantity)
    {
        db=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("Quantity",Quantity);
        db.update("cart",row,"ProName='"+oldname+"'",null);
    }
//////////////////////////////gps/////////////////////////////////////////

    public void update_Address(String name,String addr)
    {
        db=getWritableDatabase();
        db.rawQuery("update Customers set Address = '"+addr+"' where username= '"+name+"'",null);
    }


}

