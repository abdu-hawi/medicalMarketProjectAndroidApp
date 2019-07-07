package com.example.hawi.medmarket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.hawi.medmarket.medvolley.AppController;
import com.example.hawi.medmarket.medvolley.Constant;
import com.example.hawi.medmarket.medvolley.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShippingAddressActivity extends AppCompatActivity {

    private List<AddressDetails> addressDetails = new ArrayList<AddressDetails>();

    TextView txtAlert;
    EditText etName,etMobile, etCountry,etCity,etAddress;
    CheckBox cbJeddah,cbSaveAddress;

    private int uID = SharedManager.getUserID();
//    private int mobile = SharedManager.getInstance(this).getUserPhone();
    private int fromActivity ; // This var use to know any is come from any activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        if(!(SharedManager.getInstance(this).isLoggedIn())){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Shipping Address");

        fromActivity = getIntent().getIntExtra("id",0);

        DBManager db = new DBManager(this);
        addressDetails = db.getAddress();

        txtAlert = (TextView)findViewById(R.id.payAlert);
        etName = (EditText)findViewById(R.id.payName);
        etMobile = (EditText)findViewById(R.id.payMobile);
        etCountry = (EditText)findViewById(R.id.payCountry);
        etCity = (EditText)findViewById(R.id.payCity);
        etAddress = (EditText)findViewById(R.id.payAddress);
        cbJeddah = (CheckBox)findViewById(R.id.payShipToJeddah);
        cbSaveAddress = (CheckBox)findViewById(R.id.paySaveAddress);

        if(addressDetails.size() > 0 ){
            getAddressFromDb();
        }else{
            int mobile = SharedManager.getInstance(this).getUserPhone();
            String mob = "0";
            mob = mob+String.valueOf(mobile);
            etMobile.setText(mob);
        }

    }

    private void getAddressFromDb() {
        etName.setText(addressDetails.get(0).getName());
        String mobile = "0";
        mobile = mobile+String.valueOf(addressDetails.get(0).getMobile());
        etMobile.setText(mobile);
        etCountry.setText(addressDetails.get(0).getCountry());
        etCity.setText(addressDetails.get(0).getCity());
        etAddress.setText(addressDetails.get(0).getAddress());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO FROM Other Activity
            case android.R.id.home:
                if (fromActivity == 0){ // From Recycle Activity
                    startActivity(new Intent(this,RecycleActivity.class));
                    finish();
                    break;
                }

        }
        return super.onOptionsItemSelected(item);
    }

    public void cbJeddahClick(View view) {
        if(cbJeddah.isChecked()){
            etCountry.setText(R.string.saudi_arabia);
            etCity.setText(R.string.jeddah);
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void btnClick(View view){

        int countFiled = 0;

        int name = etName.getText().toString().trim().length();
        int mobile = etMobile.getText().toString().trim().length();
        int country = etCountry.getText().toString().trim().length();
        int city = etCity.getText().toString().trim().length();
        int address = etAddress.getText().toString().trim().length();
        if(name == 0 && mobile ==0 && country==0 && city == 0 && address == 0){
            etName.setBackgroundResource(R.drawable.danger_border);
            etMobile.setBackgroundResource(R.drawable.danger_border);
            etAddress.setBackgroundResource(R.drawable.danger_border);
            etCity.setBackgroundResource(R.drawable.danger_border);
            etCountry.setBackgroundResource(R.drawable.danger_border);
            txtAlert.setText(R.string.all_field_are_required);
        }else{
            if(isEmpty(etName)){
                etName.setBackgroundResource(R.drawable.danger_border);
                countFiled = 1;
            }
            if(isEmpty(etMobile)){
                etMobile.setBackgroundResource(R.drawable.danger_border);
                countFiled = 1;
            }
            if(isEmpty(etCountry)){
                etCountry.setBackgroundResource(R.drawable.danger_border);
                countFiled = 1;
            }
            if(isEmpty(etCity)){
                etCity.setBackgroundResource(R.drawable.danger_border);
                countFiled = 1;
            }
            if(isEmpty(etAddress)){
                etAddress.setBackgroundResource(R.drawable.danger_border);
                countFiled = 1;
            }
            if (countFiled == 0) {
                // Here Continue
                addAddressToDB();
            }else{
                txtAlert.setText(R.string.please_fill_required_field);
            }
        } // end else if
    }// end btn click

    private void addAddressToDB() {
        final String name = etName.getText().toString().trim();
        final String mMobile = etMobile.getText().toString().trim();
        final String country = etCountry.getText().toString().trim();
        final String city = etCity.getText().toString().trim();
        final String address = etAddress.getText().toString().trim();

        //Constant.URL_ADDRESS_SET
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_ADDRESS_SET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!(obj.getBoolean("error"))){
                                String name = obj.getString("uName");
                                int mob = obj.getInt("uMobile");
                                String mobil = "0";
                                mobil = mobil+String.valueOf(mob);
                                int mobile = Integer.parseInt(mobil);
                                String country = obj.getString("uCountry");
                                String city = obj.getString("uCity");
                                String address = obj.getString("uAddress");

                                new SaveAddressThread (name,mobile,country,city,address).start();

                                int isSave = 0;
                                if(cbSaveAddress.isChecked()){
                                    isSave =1;
                                }
                                int inJeddah = 0;
                                if(cbJeddah.isChecked()){
                                    inJeddah =1;
                                    etCity.setText(R.string.jeddah);
                                    etCountry.setText(R.string.saudi_arabia);
                                }else{

                                    if( (etCity.getText().toString().trim()).toLowerCase().compareTo("jeddah") == 0 ||
                                            etCity.getText().toString().trim().compareTo("جدة") == 0 ||
                                            etCity.getText().toString().trim().compareTo("جده") == 0 ){
                                        inJeddah =1;
                                    }
                                }
                                finish();
                                startActivity(new Intent(ShippingAddressActivity.this,BillActivity.class)
                                        .putExtra("isSave",isSave)
                                        .putExtra("inJeddah",inJeddah));
                            }else{
                                txtAlert.setText(obj.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> pramas = new HashMap<>();
                pramas.put("ID", String.valueOf(uID));
                pramas.put("name", name);
                pramas.put("mobile", mMobile);
                pramas.put("country", country);
                pramas.put("city", city);
                pramas.put("address", address);
                return pramas;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    final Context mCtn = this;
    private class SaveAddressThread extends Thread{

        String name,country,city,address;
        int mobile;

        public SaveAddressThread(String name, int mobile, String country, String city, String address) {
            this.name = name;
            this.mobile = mobile;
            this.address = address;
            this.country = country;
            this.city = city;
        }

        @Override
        public void run() {
            super.run();
            DBManager db = new DBManager(mCtn);
            db.insetAddressToDB(name,mobile,country,city,address);
        }
    }


}// End class
