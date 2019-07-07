package com.example.hawi.medmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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

public class RecycleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private List<items> items = new ArrayList<items>();
    private RecycleAdapter recycleAdapter;

    private static Button btn;
    static int shopAvilable ;

    ProgressDialog progressDialog;
    private int uID = SharedManager.getUserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        if(!(SharedManager.getInstance(this).isLoggedIn())){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ///////////

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("PLEASE WAIT ...");
        btn = (Button)findViewById(R.id.btnProcess);
        shopAvilable = 0;
        btnProc();
        onCreate();
        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
    }

    private void afterCreate(){
        items = new DBManager(this).getInboxFromDB();

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recycleAdapter = new RecycleAdapter(this,items,this);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new addressThread().start();
        progressDialog.dismiss();
    }

    private void onCreate(){

        final Context cmt = this;
        Map<String, String> params = new HashMap<String, String>();
        params.put("delta", "ss");

        progressDialog.show();

        final Activity activity = this;
        CustomRequest customRequest = new CustomRequest(
                Request.Method.POST,
                Constant.URL_INBOX,
                params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cmt.deleteDatabase("med_mar");
                DBManager db = new DBManager(cmt);
                db.addItems(response);
                shipCreate();
            }// End onResponse
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }
        );// End Custom Request

        AppController.getmInstance().addToRequestQueue(customRequest);
    }//End on create

    private void shipCreate(){
        final Context cmt = this;
        Map<String, String> params = new HashMap<String, String>();
        params.put("delta", String.valueOf(uID));

        progressDialog.show();

        CustomRequest customRequest = new CustomRequest(
                Request.Method.POST,
                Constant.URL_SHIP_GET,
                params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                DBManager db = new DBManager(cmt);
                db.updateShip(response);
                if(response.length()>0) changeShopRed();
                wishCreate();
            }// End onResponse
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }
        );// End Custom Request

        AppController.getmInstance().addToRequestQueue(customRequest);
    }

    private void wishCreate(){
        final Context cmt = this;
        Map<String, String> params = new HashMap<String, String>();
        params.put("delta", String.valueOf(uID));

        progressDialog.show();

        CustomRequest customRequest = new CustomRequest(
                Request.Method.POST,
                Constant.URL_WISH_GET,
                params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                DBManager db = new DBManager(cmt);
                db.updateWish(response);
                afterCreate();
                if(response.length()>0) changeFavRed();
            }// End onResponse
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }
        );// End Custom Request

        AppController.getmInstance().addToRequestQueue(customRequest);
    }

    private static Menu mOptionsMenu;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recycle,menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_purchases) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_wish) {

        }  else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
            SharedManager.getInstance(this).logout();
            startActivity(new Intent(this,LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void changeFavRed(){
        //TODO: apply save to data
        mOptionsMenu.findItem(R.id.fav_menu).setIcon(R.drawable.ic_fav_red);
    }
    public static final void changeFavBlack(){
        //TODO: apply not save to data
        mOptionsMenu.findItem(R.id.fav_menu).setIcon(R.drawable.ic_fav_menu);
    }

    public static final void changeShopRed(){
        //TODO: apply save to data
        mOptionsMenu.findItem(R.id.shop_menu).setIcon(R.drawable.ic_shop_red);
        shopAvilable = 1;
        btnProc();
    }
    public static final void changeShopBlack(){
        //TODO: apply not save to data
        mOptionsMenu.findItem(R.id.shop_menu).setIcon(R.drawable.ic_shop_menu);
        shopAvilable = 0;
        btnProc();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    // هذه الدالة تستخدم لتفعيل الزر والغاء تفعيله
    public static void btnProc() {
        if(shopAvilable == 0){
            btn.setEnabled(false);
            btn.setBackgroundResource(R.color.colorFire);
            btn.getResources().getColor(R.color.colorWhite);
            btn.setText(R.string.btn_disable);
        }else{
            btn.setEnabled(true);
            btn.setBackgroundResource(R.color.colorPrimary);
            btn.getResources().getColor(R.color.colorWhite);
            btn.setText(R.string.btn_enable);
        }
    }

    public void btnProcess(View view) {
        if(shopAvilable == 0){
            btn.setEnabled(false);
        }else{
            btn.setEnabled(true);
            startActivity(new Intent(this,ShippingAddressActivity.class).putExtra("activity",0));
        }
    }

    private class addressThread extends Thread{

        @Override
        public void run() {
            super.run();
            getAddress();
        }

        private void getAddress(){
            final Context cmt = getBaseContext();
            Map<String, String> params = new HashMap<String, String>();
            params.put("delta", String.valueOf(uID));
            CustomRequest customRequest = new CustomRequest(
                    Request.Method.POST,
                    Constant.URL_ADDRESS_GET,
                    params, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        JSONObject obj = response.getJSONObject(0);
                        String name = obj.getString("uName");
                        int mob = obj.getInt("uMobile");
                        String mobil = "0";
                        mobil = mobil+String.valueOf(mob);
                        int mobile = Integer.parseInt(mobil);
                        String country = obj.getString("uCountry");
                        String city = obj.getString("uCity");
                        String address = obj.getString("uAddress");

                        DBManager db = new DBManager(cmt);
                        db.insetAddressToDB(name,mobile,country,city,address);
                    }catch (JSONException e){

                    }

                }// End onResponse
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("TAG", "Error: " + error.getMessage());
                }
            }
            );// End Custom Request

            AppController.getmInstance().addToRequestQueue(customRequest);
        }
    }
}
