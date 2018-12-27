package com.example.usuario.bakery91;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class OrderActivity extends ActivityWithMenu {

    private int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layout = R.layout.activity_order;
        menuTitle = "Pedido";
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int orderId = intent.getIntExtra("OrderId",0);
        ((TextView)findViewById(R.id.textView)).setText(String.valueOf(orderId));
    }
}
