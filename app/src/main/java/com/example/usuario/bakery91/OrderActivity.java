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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderActivity extends ActivityWithMenu {

    private int orderId;
    private ProgressDialog dialog;
    private ListView productsView;
    private TextView clientName,clientTel,clientNote,clientTotalPrice;
    private ArrayList<ProductClient> products = new ArrayList<ProductClient>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        layout = R.layout.activity_order;
        menuTitle = "Pedido";
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int orderId = intent.getIntExtra("OrderId",0);
        String id  = String.valueOf(orderId);

        clientName = findViewById(R.id.textViewClientName);
        clientTel = findViewById(R.id.textViewTel);
        clientNote = findViewById(R.id.textViewNote);
        clientTotalPrice = findViewById(R.id.textViewPriceTotal);

        dialog = new ProgressDialog(OrderActivity.this);
        productsView = (ListView) findViewById(R.id.products_list);
        new GetOrderBreakDown(dialog,this,products,id).execute();
    }

    public void createlist() {
        MyAdapterProduct adapter = new MyAdapterProduct(this, products);
        productsView.setAdapter(adapter);
    }

    public void createClient(OrderClient client , float totalPrice){
        clientName.setText(client.getName()+ " " + client.getLastName());
        clientTel.setText(client.getPhone());
        clientNote.setText(client.getNote());
        clientTotalPrice.setText(String.valueOf(totalPrice) +" $");
    }

    public void NoInternetAlert(){
        Toast.makeText(this,"No hay internet, conectate para ver tus pedido",Toast.LENGTH_LONG).show();
    }
}

// My Adapter
class MyAdapterProduct extends BaseAdapter {

    ArrayList<ProductClient> products;
    LayoutInflater lInflater;

    MyAdapterProduct(Context context, ArrayList<ProductClient> products){
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

        view = lInflater.inflate(R.layout.product_client,null);
        TextView Name = view.findViewById(R.id.textViewProduct);
        TextView Quantity = view.findViewById(R.id.textViewQuantity);
        TextView Price = view.findViewById(R.id.textViewPrice);

        ProductClient productItem = products.get(item);
        Name.setText(productItem.getName());
        Quantity.setText(String.valueOf(productItem.getQuantity()));
        float price = productItem.getPrice() * (float)productItem.getQuantity();
        Price.setText(String.valueOf(price));

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        if(item % 2 == 0)
            linearLayout.setBackgroundColor(Color.parseColor("#e7e8e5"));
        return view;
    }
} // Adapter End
