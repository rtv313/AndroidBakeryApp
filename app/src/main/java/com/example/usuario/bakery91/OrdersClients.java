package com.example.usuario.bakery91;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

public class OrdersClients extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_clients);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setting the title
        toolbar.setTitle("Pedidos");
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
    }
}
