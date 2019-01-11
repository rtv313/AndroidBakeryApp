package com.example.usuario.bakery91;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {
    Button buttonOrders,buttonProducts, buttonOrdersResume, buttonSalesData;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOrders = findViewById(R.id.btnOrders);
        buttonProducts = findViewById(R.id.btnProducts);
        buttonOrdersResume = findViewById(R.id.btnSalesData);
        buttonSalesData = findViewById(R.id.btnUsers);

        verifyStoragePermissions(this);

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
