package com.example.hawi.medmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hawi.medmarket.medvolley.AppController;
import com.example.hawi.medmarket.medvolley.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    TextView txtAlert;
    EditText etName,etPhone,etPass,etConfPass;

    int countPass = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(SharedManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,RecycleActivity.class));
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT ...");

        txtAlert = (TextView)findViewById(R.id.regAlert);
        etName = (EditText)findViewById(R.id.regName);
        etPhone = (EditText)findViewById(R.id.regPhone);
        etPass = (EditText)findViewById(R.id.regPass);
        etConfPass = (EditText)findViewById(R.id.regConfPass);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etName.length() == 0){
                    etName.setBackgroundResource(R.drawable.danger_border);
                    txtAlert.setText("Please Write Your Name");
                    countPass = 1;
                }else{
                    etName.setBackgroundResource(R.drawable.success_border);
                    txtAlert.setText("");
                    countPass = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String sPass = etPass.getText().toString();
                String sConPass = etConfPass.getText().toString();
                sPass = sPass.trim();
                sConPass = sConPass.trim();
                if (sConPass.length() > 0){
                    if( sPass.compareTo(sConPass) == 0 ){
                        etConfPass.setBackgroundResource(R.drawable.success_border);
                        etPass.setBackgroundResource(R.drawable.success_border);
                        txtAlert.setText("");
                        countPass = 0 ;
                    } else{
                        etConfPass.setBackgroundResource(R.drawable.danger_border);
                        etPass.setBackgroundResource(R.drawable.danger_border);
                        txtAlert.setText("The Characters is sensitivity, Take sure!");
                        countPass = 1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etConfPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String sPass = etPass.getText().toString();
                String sConPass = etConfPass.getText().toString();
                sPass = sPass.trim();
                sConPass = sConPass.trim();
                if (sConPass.length() > 0){
                    if( sPass.compareTo(sConPass) == 0 ){
                        etConfPass.setBackgroundResource(R.drawable.success_border);
                        etPass.setBackgroundResource(R.drawable.success_border);
                        txtAlert.setText("");
                        countPass = 0 ;
                    } else{
                        etConfPass.setBackgroundResource(R.drawable.danger_border);
                        etPass.setBackgroundResource(R.drawable.danger_border);
                        txtAlert.setText("Password is not same, Take sure!");
                        countPass = 1;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String ss = etPhone.getText().toString();
                int ns = ss.length();
                if(ns == 10) {
                    checkPhone(etPhone.getText().toString());
                    txtAlert.setText("");
                }else{
                    etPhone.setBackgroundResource(R.drawable.danger_border);
                    txtAlert.setText("The phone number is 10 digit");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void checkPhone(final String phone){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_PHONE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("msg") == 0){
                                txtAlert.setText("");
                                etPhone.setBackgroundResource(R.drawable.success_border);
                                countPass = 0 ;
                            }else if (jsonObject.getInt("msg") == 1){
                                txtAlert.setText("The phone number is exists, Please Login");
                                etPhone.setBackgroundResource(R.drawable.danger_border);
                                countPass = 1 ;
                            }else{
                                txtAlert.setText("Sorry some is mistake, write your phone again!!");
                                etPhone.setBackgroundResource(R.drawable.danger_border);
                            }
                            //Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> pramas = new HashMap<>();
                pramas.put("phone",phone);
                return pramas;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest);

    }

    private void hidePDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT ...");
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void btnTxtLogin(View view) {
        finish();
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }
    public void btnReg(View view) {
        if(etName.length() == 0 && etPhone.length() == 0 && etPass.length() == 0 && etConfPass.length() == 0){
            txtAlert.setText("Please Fill All Input");
            etName.setBackgroundResource(R.drawable.danger_border);
            etPhone.setBackgroundResource(R.drawable.danger_border);
            etPass.setBackgroundResource(R.drawable.danger_border);
            etConfPass.setBackgroundResource(R.drawable.danger_border);
            countPass = 1;
            return;
        }
        if(countPass == 0){
            registerUser();
        }
    }

    private void registerUser() {
        final String phone = etPhone.getText().toString().trim();
        final String pass = etPass.getText().toString().trim();
        final String name = etName.getText().toString().trim();

        progressDialog.setMessage("Register user...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("msg") == 2){
                                txtAlert.setText("");
                                finish();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }else if (jsonObject.getInt("msg") == 1){
                                txtAlert.setText("The phone number is exists, Please Login");
                                etPhone.setBackgroundResource(R.drawable.danger_border);
                                countPass = 1 ;
                            }else{
                                txtAlert.setText("Sorry some is mistake, Please try again!!");
                            }
                            //Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> pramas = new HashMap<>();
                pramas.put("name",name);
                pramas.put("phone",phone);
                pramas.put("password",pass);
                return pramas;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest);
    }
}
