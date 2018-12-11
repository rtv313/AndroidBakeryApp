package com.example.usuario.bakery91;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button buttonOrders,buttonProducts,buttonSalesData,buttonUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOrders = (Button)findViewById(R.id.btnOrders);
        buttonProducts = (Button)findViewById(R.id.btnProducts);
        buttonSalesData = (Button)findViewById(R.id.btnSalesData);
        buttonUsers = (Button)findViewById(R.id.btnUsers);

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
                Toast.makeText(MainActivity.this,"Productos Menu",Toast.LENGTH_LONG).show();
            }
        });

        buttonSalesData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Datos de ventas Menu",Toast.LENGTH_LONG).show();
            }
        });

        buttonUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Usuarios Menu",Toast.LENGTH_LONG).show();
            }
        });
    }
}
