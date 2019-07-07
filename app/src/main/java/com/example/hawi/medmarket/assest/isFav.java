package com.example.hawi.medmarket.assest;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hawi.medmarket.medvolley.AppController;
import com.example.hawi.medmarket.medvolley.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hawi on 18/02/18.
 */

public class isFav {
    private int iId ;
    private int uId ;
    private int Fav;

    public isFav(int uId ,int iId, int Fav) {
        this.iId = iId;
        this.uId = uId;
        this.Fav = Fav;
        Fav();
    }

    public int getIId() {
        return iId;
    }

    public int getUId() {
        return uId;
    }

    public int getFav() {
        return Fav;
    }


    private void Fav() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_FAV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("resp",response);
                        Log.d("inTrash",String.valueOf(getFav()));
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
                pramas.put("UID",String.valueOf(getUId()));
                pramas.put("IID",String.valueOf(getIId()));
                pramas.put("WISH",String.valueOf(getFav()));
                return pramas;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest);

    }// end inboxOnCreate

}
