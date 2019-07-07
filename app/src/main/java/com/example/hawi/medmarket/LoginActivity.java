package com.example.hawi.medmarket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    TextView txtAlert;
    EditText etPhone ,etPass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SharedManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,RecycleActivity.class));
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT ...");

        txtAlert =(TextView)findViewById(R.id.loginAlert);
        etPass =(EditText)findViewById(R.id.loginPass);
        etPhone =(EditText)findViewById(R.id.loginPhone);

    }//End onCreate(Bundle)

    public void btnLogin(View view) {
        String phone = etPhone.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        if(etPhone.length() == 0 && etPass.length() == 0){
            txtAlert.setText("Please fill all information");
            return;
        }else {
            txtAlert.setText("");
            checkLogin(phone,pass);
        }
    }

    private void checkLogin(final String phone,final String pass){

        final Context mCnt = this;

        progressDialog.setMessage("Register user...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(!(jsonObject.getBoolean("error"))){
                                SharedManager.getInstance(mCnt).userLogin(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getInt("phone")
                                );
                                finish();
                                startActivity(new Intent(LoginActivity.this,RecycleActivity.class));
                            }else{
                                txtAlert.setText(jsonObject.getString("msg"));
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
                pramas.put("phone",phone);
                pramas.put("password",pass);
                return pramas;
            }
        };

        AppController.getmInstance().addToRequestQueue(stringRequest);
    }

    public void btnTxtReg(View view) {
        finish();
        startActivity(new Intent(this,RegisterActivity.class));
    }

}
