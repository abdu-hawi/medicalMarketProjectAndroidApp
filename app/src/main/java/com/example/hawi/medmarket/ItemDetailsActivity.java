package com.example.hawi.medmarket;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.hawi.medmarket.assest.isFav;
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

public class ItemDetailsActivity extends AppCompatActivity {

    private List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();
    private ImageLoader imageLoader = AppController.getmInstance().getmImageLoader();

    //TODO: TextView detail_Shopping
    private NetworkImageView imageView;
    private TextView txtPrice ,txtUnit,txtQty,txtQtyUnit,txtOrderPrice,txtDesc;
    private EditText etOrderQty;
    private Button btnShowDetail,btnShipping,btnFav,btnBuy;
    private int uID = SharedManager.getUserID();

    int ordQty;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        if(!(SharedManager.getInstance(this).isLoggedIn())){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }

        id = getIntent().getIntExtra("id",0);
        itemDetails = new DBManager(this).getItemById(id);
        this.setTitle(itemDetails.get(0).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (NetworkImageView)findViewById(R.id.detail_img);
        txtPrice = (TextView)findViewById(R.id.detail_price);
        txtUnit = (TextView)findViewById(R.id.detail_unit);
        etOrderQty = (EditText)findViewById(R.id.detail_order_qyt);
        txtQty = (TextView)findViewById(R.id.detail_qty);
        txtQtyUnit = (TextView)findViewById(R.id.detail_unitQTY);
        txtOrderPrice = (TextView)findViewById(R.id.detail_ord_price);
        btnShowDetail = (Button)findViewById(R.id.detail_btn_show_detail);
        txtDesc = (TextView)findViewById(R.id.detail_desc);
        btnBuy = (Button)findViewById(R.id.detail_btnBuy);
        btnFav = (Button)findViewById(R.id.detail_addToWish);
        btnShipping = (Button)findViewById(R.id.detail_btnShip) ;

        onCrate();
    }

    private void onCrate(){
        imageView.setImageUrl(Constant.URL_INV_IMG+itemDetails.get(0).getImg(),imageLoader);
        txtPrice.setText(String.valueOf(itemDetails.get(0).getPrice()));
        txtUnit.setText(itemDetails.get(0).getQtyUnit());
        txtQty.setText(String.valueOf(itemDetails.get(0).getQty()));
        txtQtyUnit.setText(itemDetails.get(0).getQtyUnit());

        if(!isEmpty(etOrderQty)){
            ordQty = Integer.parseInt(etOrderQty.getText().toString());
            txtOrderPrice.setText(String.valueOf(ordQty * itemDetails.get(0).getPrice()+ 25));
        }

        etOrderQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!isEmpty(etOrderQty)){
                    ordQty = Integer.parseInt(etOrderQty.getText().toString());
                    txtOrderPrice.setText(String.valueOf(ordQty * itemDetails.get(0).getPrice()+ 25));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }); // end TextWatcher

        btnShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDescription(id);
            }
        });


        //TODO: btnShip & btnFav with menu item in recycle activity
        if(itemDetails.get(0).getIsFav() == 0)
            btnFav.setText(R.string.add_to_wish_list);
        else
            btnFav.setText(R.string.del_from_wish_list);

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG btn",String.valueOf(itemDetails.get(0).getIsFav()));
                if(itemDetails.get(0).getIsFav() == 1) {
                    btnFav.setText(R.string.add_to_wish_list);
                    new isFav(uID,id,0);
                    itemDetails.get(0).setIsFav(0);
                }else {
                    btnFav.setText(R.string.del_from_wish_list);
                    new isFav(uID,id,1);
                    itemDetails.get(0).setIsFav(1);
                }
            }
        });

    } // End onCreate

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this,RecycleActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDescription(int itemID){

        Map<String, String> params = new HashMap<String, String>();
        params.put("ID", String.valueOf(itemID));

        CustomRequest customRequest = new CustomRequest(
                Request.Method.POST,
                Constant.URL_DETAILS,
                params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    JSONObject obj = response.getJSONObject(0);
                    txtDesc.setText(obj.getString("itemDescription"));
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
