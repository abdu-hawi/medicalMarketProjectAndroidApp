package com.example.hawi.medmarket.assest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hawi.medmarket.DBManager;
import com.example.hawi.medmarket.medvolley.AppController;
import com.example.hawi.medmarket.medvolley.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hawi on 18/02/18.
 */

public class isShipping {
    private int iId ,uId,ship;
    private Context cmt;

    public isShipping(int uId, int iId, int ship,Context cmt) {
        this.iId = iId;
        this.uId = uId;
        this.ship = ship;
        this.cmt = cmt;
        shipping();
        updateShipInDB();
    }

    public isShipping(int uId,Context cmt) {
        this.iId = iId;
        this.uId = uId;
        this.ship = ship;
        this.cmt = cmt;
        deleteshipping();
        deleteShipInDB();
    }

    private void updateShipInDB() {
        DBManager dbManager = new DBManager(getCmt());
        SQLiteDatabase db = dbManager.getWritableDatabase();
        db.execSQL("UPDATE items SET isShip = "+getShip()+" WHERE id = "+getiId());
        db.close();
    }

    private void deleteShipInDB() {
        DBManager dbManager = new DBManager(getCmt());
        SQLiteDatabase db = dbManager.getWritableDatabase();
        db.execSQL("UPDATE items SET isShip = 0 WHERE id = "+getiId());
        db.close();
    }

    public Context getCmt() {
        return cmt;
    }

    public void setCmt(Context cmt) {
        this.cmt = cmt;
    }

    public int getiId() {
        return iId;
    }

    public int getuId() {
        return uId;
    }

    public int getShip() {
        return ship;
    }

    private void deleteshipping() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_SHIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("err",error.getMessage());
                    }
                }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> pramas = new HashMap<>();
                pramas.put("UID",String.valueOf(getuId()));
                return pramas;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest);

    }
    private void shipping() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_SHIPPING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("err",error.getMessage());
                    }
                }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> pramas = new HashMap<>();
                pramas.put("IID",String.valueOf(getiId()));
                pramas.put("UID",String.valueOf(getuId()));
                pramas.put("SHIP",String.valueOf(getShip()));
                return pramas;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest);

    }// end inboxOnCreate

}
