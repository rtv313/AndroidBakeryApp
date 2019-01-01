package com.example.usuario.bakery91;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrdersResume extends ActivityWithMenu {

    ListView productsView;
    ProgressDialog dialog;
    Button refreshButton;
    private ArrayList<ProductResume> products = new ArrayList<ProductResume>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layout = R.layout.activity_orders_resume;
        menuTitle = "Resumen de Pedidos";
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(OrdersResume.this);
        productsView = findViewById(R.id.products_list);
        refreshButton = findViewById(R.id.btnRefresh);

        callGetOrdersResume();
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGetOrdersResume();
            }
        });
    }

    public void callGetOrdersResume(){
        new GetOrdersResume(this,dialog,products).execute();
    }

    public void createlist() {
        MyAdapterProductResume adapter = new MyAdapterProductResume(OrdersResume.this, products);
        productsView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){

        if (menuOpen == false){
            super.onBackPressed();
            Intent intent = new Intent(OrdersResume.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            this.mDrawerLayout.closeDrawers();
        }
    }

    public void NoInternetAlert(){
        Toast.makeText(this,"No hay internet, conectate para ver tus pedidos",Toast.LENGTH_LONG).show();
    }
}


// My MyAdapterProductResume
class MyAdapterProductResume extends BaseAdapter {

    ArrayList<ProductResume> products;
    LayoutInflater lInflater;

    MyAdapterProductResume(Context context, ArrayList<ProductResume> products){
        this.products = products;
        lInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return products.size();
    }

    @Override
    public Object getItem(int index){
        return products.get(index);
    }

    @Override
    public long getItemId(int id){
        return id;
    }

    @Override
    public View getView(int item, View view, ViewGroup parent){

        view = lInflater.inflate(R.layout.order_product_resume,null);
        TextView productName = view.findViewById(R.id.textViewProduct);
        TextView productQuantity = view.findViewById(R.id.textViewQuantity);

        ProductResume productResumeItem = products.get(item);
        productName.setText(productResumeItem.getName());
        int Quantity = productResumeItem.getQuantity();
        productQuantity.setText(String.valueOf(Quantity));

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        if(item % 2 == 0)
            linearLayout.setBackgroundColor(Color.parseColor("#e7e8e5"));

        return view;
    }
} // Adapter End