package com.example.hawi.medmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hawi.medmarket.assest.isShipping;
import com.example.hawi.medmarket.medvolley.AppController;
import com.example.hawi.medmarket.medvolley.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmActivity extends AppCompatActivity {

    TextView txtAName,txtAMobile,txtACountry,txtACity,txtAAddress,txtShipCity,txtTotAmt,txtShipPrice;
    int isSave,inJeddah;
    Double totAmt = 0.0;
    TableLayout t1 ;
    ProgressDialog progressDialog;
    private int uID = SharedManager.getUserID();
    DBManager dbManager;
    List<AddressDetails> addressDetails = new ArrayList<AddressDetails>();
    List<ConfirmBill> confirmBills = new ArrayList<ConfirmBill>();
    String name,country,city,address,mob;
    JSONObject jsonItems = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        if(!(SharedManager.getInstance(this).isLoggedIn())){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }

        this.setTitle("Confirmation order");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT ...");
        progressDialog.show();

        txtAName = (TextView)findViewById(R.id.confName);
        txtAMobile = (TextView)findViewById(R.id.confMobile);
        txtACountry = (TextView)findViewById(R.id.confCountry);
        txtACity = (TextView)findViewById(R.id.confCity);
        txtAAddress = (TextView)findViewById(R.id.confAddress);
        txtShipCity = (TextView)findViewById(R.id.confShipCity);
        txtTotAmt = (TextView)findViewById(R.id.confTotAmt);
        txtShipPrice = (TextView)findViewById(R.id.confShipPrice);

        isSave = getIntent().getIntExtra("isSave",0);
        inJeddah = getIntent().getIntExtra("inJeddah",0);

        if(inJeddah == 1){ // jeddah check
            txtShipPrice.setText("25");
            txtShipCity.setText(R.string.shipping_price_inside_jeddah);
            totAmt = 25.0;
        }else{
            txtShipPrice.setText("35");
            txtShipCity.setText(R.string.shipping_price_outside_jeddah);
            totAmt = 35.0;
        }

        t1 = (TableLayout)findViewById(R.id.t1);
        t1.setColumnStretchable(0,true);
        t1.setColumnStretchable(1,true);
        t1.setColumnStretchable(2,true);
        t1.setColumnStretchable(3,true);
        t1.setColumnStretchable(4,true);

        dbManager = new DBManager(this);
        getItemFromDB();
        getAdressFromDB();

        progressDialog.dismiss();
    }

    public void getItemFromDB(){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String qry = "SELECT `id`,`name`,`price`,`unit`,`qty` FROM items WHERE `isShip` = 1" ;//
        Cursor cursor = db.rawQuery(qry,null);

        if(cursor.moveToFirst()) {
            do {
                int iId = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String unit = cursor.getString(cursor.getColumnIndex("unit"));
                int qty = cursor.getInt(cursor.getColumnIndex("qty"));

                confirmBills.add(new ConfirmBill(iId,qty));

                setItem(iId,name,price,unit,qty);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void setItem(int iId,String name,Double price,String unit,int qty){
        TextView txtName = new TextView(this);
        TextView txtQty = new TextView(this);
        TextView txtUnit = new TextView(this);
        TextView txtPrice = new TextView(this);
        TextView txtTotal = new TextView(this);

        TableRow tr = new TableRow(this);
        //tr.setPadding(5,0,5,0);

        txtName.setText(name);
        txtName.setTextSize(14);
        txtName.setGravity(Gravity.START);
        txtName.setWidth(150);
        txtName.setPadding(2,0,2,0);

        txtQty.setText(String.valueOf(qty));
        txtQty.setTextSize(14);
        txtQty.setGravity(Gravity.START);
        txtQty.setWidth(40);

        txtUnit.setText(unit);
        txtUnit.setTextSize(14);
        txtUnit.setGravity(Gravity.START);
        txtUnit.setWidth(30);

        txtPrice.setText(String.valueOf(price));
        txtPrice.setTextSize(14);
        txtPrice.setGravity(Gravity.CENTER);
        txtPrice.setWidth(50);

        final Double total = price * qty;
        totAmt = totAmt + total;

        txtTotal.setText(String.valueOf(total));
        txtTotal.setTextSize(14);
        txtTotal.setGravity(Gravity.CENTER);
        txtTotal.setWidth(90);

        txtTotAmt.setText(String.valueOf(totAmt));

        tr.setBackgroundResource(R.drawable.white_gray_border_bottom);

        tr.addView(txtName);
        tr.addView(txtQty);
        tr.addView(txtUnit);
        tr.addView(txtPrice);
        tr.addView(txtTotal);

        t1.addView(tr);
    }

    public void getAdressFromDB(){
        addressDetails = dbManager.getAddress();
        name = addressDetails.get(0).getName();
        int mobile = addressDetails.get(0).getMobile();
        country = addressDetails.get(0).getCountry();
        city = addressDetails.get(0).getCity();
        address = addressDetails.get(0).getAddress();

        mob = String.valueOf(0)+ String.valueOf(mobile);
        txtAName.setText(name);
        txtAMobile.setText(mob);
        txtACountry.setText(country);
        txtACity.setText(city);
        txtAAddress.setText(address);
    }

    public void snedAllData(View view) {
        progressDialog.show();
        changeItemsToJsonOb();
        sendData();
        progressDialog.dismiss();
    }

    private void sendData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_ADD_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int idIn = jsonObject.getInt("msg");
                            Intent intent = new Intent(ConfirmActivity.this,ThanksPaymentActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("id",idIn);
                            startActivity(intent);
                            new isShipping(uID,getBaseContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //,city,address,isSave,jsonItems
                Map<String,String> pramas = new HashMap<>();
                pramas.put("user_id",String.valueOf(uID));
                pramas.put("rec_name",name);
                pramas.put("rec_mob",mob);
                pramas.put("rec_con",country);
                pramas.put("rec_city",city);
                pramas.put("rec_address",address);
                pramas.put("isSave",String.valueOf(isSave));
                pramas.put("final_total_amt",String.valueOf(totAmt));
                pramas.put("json_items",jsonItems.toString());
                return pramas;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    private void changeItemsToJsonOb() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < confirmBills.size(); i++) {

            int id = confirmBills.get(i).getId();
            int sQTY = confirmBills.get(i).getQty();
            try {
                JSONObject jsonRow = new JSONObject();
                jsonRow.put("IID", id);
                jsonRow.put("QTY", sQTY);

                jsonArray.put(jsonRow);
                jsonItems.put("recent", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void backToMarket(View view) {
        startActivity(new Intent(this,RecycleActivity.class));
        finish();
    }
}
