package com.example.hawi.medmarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.hawi.medmarket.assest.isShipping;

import java.util.ArrayList;
import java.util.List;

public class BillActivity extends AppCompatActivity {

    int isSave,inJeddah ;
    int uID = SharedManager.getUserID();
    private ProgressDialog pDialog;
    private TableLayout t1;

    TextView txtTotAmt,billShipPrice,billShipCity;
    Double totAmt = 0.0;

    private DBManager dbManager = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        if(!(SharedManager.getInstance(this).isLoggedIn())){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            return;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Invoice Review");

        isSave = getIntent().getIntExtra("isSave",0);
        inJeddah = getIntent().getIntExtra("inJeddah",0);

        txtTotAmt = (TextView)findViewById(R.id.BillTotAmt);
        billShipPrice =(TextView)findViewById(R.id.billShipPrice);
        billShipCity = (TextView)findViewById(R.id.billShipCity);

        if(inJeddah == 1){ // jeddah check
            billShipPrice.setText("25");
            billShipCity.setText(R.string.shipping_price_inside_jeddah);
            totAmt = 25.0;
        }else{
            billShipPrice.setText("35");
            billShipCity.setText(R.string.shipping_price_outside_jeddah);
            totAmt = 35.0;
        }

        t1 = (TableLayout)findViewById(R.id.t1);
        t1.setColumnStretchable(0,true);
        t1.setColumnStretchable(1,true);
        t1.setColumnStretchable(2,true);
        t1.setColumnStretchable(3,true);
        t1.setColumnStretchable(4,true);
        t1.setColumnStretchable(5,true);

        getItemsFromDB();

    }

    private void getItemsFromDB() {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE isShip = 1",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double price = cursor.getDouble(cursor.getColumnIndex("price"));
            String unit = cursor.getString(cursor.getColumnIndex("unit"));

            makeRowView(id,name,price,unit );

            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        //onClickOnitem();
    }

    private void makeRowView(final int id, String name, final Double price,String unit ){

        TextView txtName = new TextView(this);
        final EditText etQty = new EditText(this);
        TextView txtUnit = new TextView(this);
        TextView txtPrice = new TextView(this);
        final TextView txtTotal = new TextView(this);
        final TextView txtDelete = new TextView(this);

//        int qty = 1;

        TableRow tr = new TableRow(this);
        tr.setPadding(2,0,2,0);

        txtName.setText(name);
        txtName.setTextSize(14);
        txtName.setGravity(Gravity.START);
        txtName.setWidth(150);

        etQty.setText(String.valueOf(1));
        etQty.setTextSize(14);
        etQty.setEms(3);
        etQty.setInputType(InputType.TYPE_CLASS_NUMBER);
        etQty.setGravity(Gravity.CENTER);

        txtUnit.setText(unit);
        txtUnit.setTextSize(14);
        txtUnit.setGravity(Gravity.CENTER);
        txtUnit.setWidth(40);

        txtPrice.setText(String.valueOf(price));
        txtPrice.setTextSize(14);
        txtPrice.setGravity(Gravity.CENTER);
        txtPrice.setWidth(50);

        int qty = Integer.parseInt(etQty.getText().toString());
        dbManager.updateQty(qty,id);
        final Double total = price * qty;
        totAmt = totAmt + total;

        txtTotal.setText(String.valueOf(total));
        txtTotal.setTextSize(14);
        txtTotal.setGravity(Gravity.CENTER);
        txtTotal.setWidth(80);

        txtDelete.setText("X");
        txtDelete.setTextSize(14);
        if(android.os.Build.VERSION.SDK_INT >= 23)
        txtDelete.getResources().getColor(R.color.colorRed,null);
        else txtDelete.getResources().getColor(R.color.colorRed);
        txtDelete.setTypeface(Typeface.DEFAULT_BOLD);
        txtDelete.setGravity(Gravity.CENTER);
        txtDelete.setWidth(30);

        txtTotAmt.setText(String.valueOf(totAmt));

        tr.setBackgroundResource(R.drawable.white_gray_border_bottom);

        tr.addView(txtName);
        tr.addView(etQty);
        tr.addView(txtUnit);
        tr.addView(txtPrice);
        tr.addView(txtTotal);
        tr.addView(txtDelete);

        t1.addView(tr);

        final String[] priceBefor = new String[1];
        etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (etQty.getText().toString().trim().length() != 0)
                priceBefor[0] = etQty.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //TODO: Fix when empty
                if(etQty.getText().toString().trim().length() == 0) etQty.setText(String.valueOf(0));
                else {
                    int txtQty = Integer.parseInt(etQty.getText().toString());
                    txtTotal.setText(String.valueOf(txtQty * price));
                    totAmt = totAmt + Double.parseDouble(txtTotal.getText().toString()) - ((Integer.parseInt(priceBefor[0])) * price);
                    txtTotAmt.setText(String.valueOf(totAmt));
                    dbManager.updateQty(txtQty,id);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow tt = (TableRow) txtDelete.getParent();
                TextView tv= (TextView)tt.getChildAt(3);
                String ss = tv.getText().toString();
                totAmt -= Double.parseDouble(ss);
                txtTotAmt.setText(String.valueOf(totAmt));
                tt.removeAllViews();
                new isShipping(uID,id,0,getBaseContext());
            }
        });
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
    public void btnClick(View view) {
        startActivity(new Intent(this,ConfirmActivity.class)
                .putExtra("inJeddah",inJeddah)
                .putExtra("isSave",isSave));
        finish();
    }

    public void onClickChangeCity(View view) {
        startActivity(new Intent(this,ShippingAddressActivity.class)
                .putExtra("inJeddah",inJeddah)
                .putExtra("isSave",isSave));
        finish();
    }
}
