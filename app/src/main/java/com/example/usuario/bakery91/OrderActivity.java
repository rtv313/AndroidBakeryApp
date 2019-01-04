package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends ActivityWithMenu implements OnItemSelectedListener,View.OnTouchListener {

    private int orderId;
    private List<String> status = new ArrayList<String>();
    private ProgressDialog dialog;
    private ListView productsView;
    private TextView clientName,clientTel,clientNote,clientTotalPrice;
    private ArrayList<ProductClient> products = new ArrayList<ProductClient>();
    private Spinner spinner;
    private String idClient;
    private boolean userSelect;
    private String previousStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        layout = R.layout.activity_order;
        menuTitle = "Pedido";
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int orderId = intent.getIntExtra("OrderId",0);
        idClient  = String.valueOf(orderId);

        clientName = findViewById(R.id.textViewClientName);
        clientTel = findViewById(R.id.textViewTel);
        clientNote = findViewById(R.id.textViewNote);
        clientTotalPrice = findViewById(R.id.textViewPriceTotal);

        status.add("PENDIENTE");
        status.add("TERMINADO");
        status.add("CANCELADO");

        userSelect = false;
        spinner = findViewById(R.id.orderStatus);
        spinner.setOnItemSelectedListener(this);
        spinner.setOnTouchListener(this);
        dialog = new ProgressDialog(OrderActivity.this);
        productsView = (ListView) findViewById(R.id.products_list);

        new GetOrderBreakDown(dialog,this,products,idClient).execute();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (userSelect) {
            String actualStatus = status.get(position);
            new ChangeOrderStatus(actualStatus,previousStatus,this,idClient).execute();
            userSelect = false;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {

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
        previousStatus = client.getStatus();
    }

    public void NoInternetAlert(){
        Toast.makeText(this,"No hay internet, conectate para ver tus pedido",Toast.LENGTH_LONG).show();
    }

    public void NoInternetAlertForUpdate(){
        Toast.makeText(this,"No hay internet, conectate para actualizar el pedido",Toast.LENGTH_LONG).show();
    }

    public void CreateOrderStatus(String option){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        switch(option){
            case "PENDIENTE":
                spinner.setSelection(0);
                break;

            case "TERMINADO":
                spinner.setSelection(1);
                break;

            case "CANCELADO":
                spinner.setSelection(2);
                break;

            default:
                spinner.setSelection(0);
                break;
        }
    }

    @Override
    public void onBackPressed(){

        if (menuOpen == false){
            super.onBackPressed();
            Intent intent = new Intent(OrderActivity.this, OrdersClients.class);
            startActivity(intent);
            finish();
        }else{
            this.mDrawerLayout.closeDrawers();
        }
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
