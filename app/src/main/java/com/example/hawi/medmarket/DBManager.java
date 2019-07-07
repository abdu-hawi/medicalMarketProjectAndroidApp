package com.example.hawi.medmarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hawi on 30/01/18.
 */

public class DBManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "med_mar";
    private static final int DB_VERSION = 1;
    private static final String TB_NAME = "items";
    private static final String TB_ADDRESS = "address";

    private Context cmt;

    public DBManager(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TB_NAME +" " +
                "( `id` INTEGER NOT NULL, " +
                "`name` TEXT NOT NULL,`desc` TEXT NOT NULL, `price` REAL NOT NULL, " +
                "`unit` TEXT NOT NULL, `img` TEXT NOT NULL ,`isFav` INTERGER DEFAULT 0,"+
                "`isShip` INTERGER DEFAULT 0 ,`qty` INTERGER DEFAULT 1 ,`amtStock` INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+TB_ADDRESS+"(`name` TEXT NOT NULL," +
                " `mobile` INTEGER NOT NULL, `country` TEXT NOT NULL,`city` TEXT NOT NULL," +
                "`address` TEXT NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
        onCreate(db);
    }

    public void addItems(JSONArray response){
        for (int count = 0 ; count < response.length() ; count++){
            try{
                JSONObject obj = response.getJSONObject(count);

                ContentValues val = new ContentValues();
                val.put("id",obj.getInt("ID"));
                val.put("name",obj.getString("Name"));
                val.put("desc",obj.getString("Description"));
                val.put("price",obj.getDouble("SellPrice"));
                val.put("unit",obj.getString("Unit"));
                val.put("img",obj.getString("Image"));
                val.put("amtStock",obj.getInt("AmountStock"));

                SQLiteDatabase db = this.getWritableDatabase();
                db.insert(TB_NAME,null,val);

                db.close();

            }// end try
            catch(JSONException e) {
                e.printStackTrace();
            }// end catch
        }// end for
    }// end addItems

    public List<items> getInboxFromDB(){

        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "SELECT * FROM "+TB_NAME ;//
        Cursor cursor = db.rawQuery(qry,null);

        List<items> itemsList = new ArrayList<items>();
        if(cursor.moveToFirst()) {
            do {
                items items = new items();

                items.setId(cursor.getInt(cursor.getColumnIndex("id")));
                items.setName(cursor.getString(cursor.getColumnIndex("name")));
                items.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
                items.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                items.setImg(cursor.getString(cursor.getColumnIndex("img")));
                items.setQtyUnit(cursor.getString(cursor.getColumnIndex("unit")));
                items.setIsFav(cursor.getInt(cursor.getColumnIndex("isFav")));
                items.setIsShop(cursor.getInt(cursor.getColumnIndex("isShip")));

                itemsList.add(items);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemsList;
    }

    public List<ItemDetail> getItemById(int itemId){

        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "SELECT * FROM "+TB_NAME+" WHERE id = "+itemId ;//
        Cursor cursor = db.rawQuery(qry,null);
        int cnt = 0;
        List<ItemDetail> itemsList = new ArrayList<ItemDetail>();
        if(cursor.moveToFirst()) {
            do {
                cnt += 1;
                ItemDetail items = new ItemDetail();

                items.setId(cursor.getInt(cursor.getColumnIndex("id")));
                items.setName(cursor.getString(cursor.getColumnIndex("name")));
                items.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
                items.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                items.setImg(cursor.getString(cursor.getColumnIndex("img")));
                items.setQtyUnit(cursor.getString(cursor.getColumnIndex("unit")));
                items.setIsFav(cursor.getInt(cursor.getColumnIndex("isFav")));
                items.setIsShop(cursor.getInt(cursor.getColumnIndex("isShip")));
                items.setQty(cursor.getInt(cursor.getColumnIndex("amtStock")));

                itemsList.add(items);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemsList;
    }

    public void updateShip(JSONArray response){
        for (int count = 0 ; count < response.length() ; count++){
            try{
                JSONObject obj = response.getJSONObject(count);
                int id = obj.getInt("IID");
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("UPDATE "+TB_NAME+" SET isShip = 1 WHERE id = "+id);
                db.close();
            }// end try
            catch(JSONException e) {
                e.printStackTrace();
            }// end catch
        }// end for
    }// end updateShip

    public void updateWish(JSONArray response){
        for (int count = 0 ; count < response.length() ; count++){
            try{
                JSONObject obj = response.getJSONObject(count);
                int id = obj.getInt("IID");
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("UPDATE "+TB_NAME+" SET isFav = 1 WHERE id = "+id);
                db.close();
            }// end try
            catch(JSONException e) {
                e.printStackTrace();
            }// end catch
        }// end for
    }// end updateShip

    public List<AddressDetails> getAddress(){

        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "SELECT * FROM "+TB_ADDRESS+" ";//
        Cursor cursor = db.rawQuery(qry,null);

        List<AddressDetails> itemsList = new ArrayList<AddressDetails>();
        if(cursor.moveToFirst()) {
            do {
                AddressDetails items = new AddressDetails();

                items.setName(cursor.getString(cursor.getColumnIndex("name")));
                items.setMobile(cursor.getInt(cursor.getColumnIndex("mobile")));
                items.setCountry(cursor.getString(cursor.getColumnIndex("country")));
                items.setCity(cursor.getString(cursor.getColumnIndex("city")));
                items.setAddress(cursor.getString(cursor.getColumnIndex("address")));

                itemsList.add(items);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemsList;
    }

    public void insetAddressToDB(String name,int mobile, String country ,String city,String address){

        ContentValues val = new ContentValues();
        val.put("name",name);
        val.put("mobile",mobile);
        val.put("country",country);
        val.put("city",city);
        val.put("address",address);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TB_ADDRESS,null,val);

        db.close();
    }

    public void updateQty(int qty,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TB_NAME+" SET `qty` = "+qty+" WHERE id = "+id);
        db.close();
    }// end updateShip
}
