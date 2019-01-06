package com.example.usuario.bakery91;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {
    Button buttonOrders,buttonProducts, buttonOrdersResume, buttonSalesData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOrders = (Button)findViewById(R.id.btnOrders);
        buttonProducts = (Button)findViewById(R.id.btnProducts);
        buttonOrdersResume = (Button)findViewById(R.id.btnSalesData);
        buttonSalesData = (Button)findViewById(R.id.btnUsers);

        FirebaseMessaging.getInstance().subscribeToTopic("NuevosPedidos");

        buttonOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrdersClients.class);
                startActivity(intent);
            }
        });

        buttonProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsMenuActivity.class);
                startActivity(intent);
            }
        });

        buttonOrdersResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrdersResume.class);
                startActivity(intent);
            }
        });

        buttonSalesData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesDataActivity.class);
                startActivity(intent);
            }
        });
    }
}
