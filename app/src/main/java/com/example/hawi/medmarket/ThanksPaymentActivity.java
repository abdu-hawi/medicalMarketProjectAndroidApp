package com.example.hawi.medmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class ThanksPaymentActivity extends AppCompatActivity {

    TextView thank,thankOrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_payment);

        thank = (TextView)findViewById(R.id.thank);
        thankOrd = (TextView)findViewById(R.id.thankOrdNo);
        final int id = getIntent().getIntExtra("id",0);
        final String name = getIntent().getStringExtra("name");

        String tt = getString(R.string.thank_you);
        String pay = getString(R.string.to_payment_with_us_your_order_number_is);
        pay = tt+" "+name+" "+pay;
        thank.setText(pay);
        thankOrd.setText(String.valueOf(id));
    }

    public void thankBtnClick(View view) {
        startActivity(new Intent(this,RecycleActivity.class));
        finish();
    }
}
