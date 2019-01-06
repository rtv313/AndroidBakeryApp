package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductsMenuActivity extends ActivityWithMenu implements AdapterView.OnItemClickListener {
    private ListView productsView;
    private ProgressDialog dialog;
    private ArrayList<Product> products = new ArrayList<Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        layout = R.layout.activity_products_menu;
        menuTitle = "Productos";
        super.onCreate(savedInstanceState);

        productsView = findViewById(R.id.products_list);
        dialog = new ProgressDialog(ProductsMenuActivity.this);

        Button BtnAddProduct = findViewById(R.id.btnAddProduct);

        BtnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        new GetProducts(this,dialog,products).execute();
    }


    @Override
    public void onItemClick(AdapterView<?> av , View v, int position, long id){
        Product product = (Product)productsView.getItemAtPosition(position);
        Intent intent = new Intent(ProductsMenuActivity.this, MainActivity.class);
        intent.putExtra("ProductId",product.getId());
        startActivity(intent);
    }


    public void createlist(){
        MyProductAdapter adapter = new MyProductAdapter(this, products);
        productsView.setAdapter(adapter);
        productsView.setOnItemClickListener(this);
    }

    public void NoInternetAlert(){
        Toast.makeText(this,"No hay internet, conectate para ver tus productos",Toast.LENGTH_LONG).show();
    }
}

// My Adapter
class MyProductAdapter extends BaseAdapter {

    ArrayList<Product> productsArrayList;
    LayoutInflater lInflater;

    MyProductAdapter(Context context, ArrayList<Product> products){
        productsArrayList = products;
        lInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return productsArrayList.size();
    }

    @Override
    public Object getItem(int index){
        return productsArrayList.get(index);
    }

    @Override
    public long getItemId(int id){
        return id;
    }

    @Override
    public View getView(int item, View view, ViewGroup parent){

        view = lInflater.inflate(R.layout.product_row,null);

        TextView productName = view.findViewById(R.id.txtProductName);
        TextView productPrice = view.findViewById(R.id.txtProductPrice);

        Product productItem = productsArrayList.get(item);

        productName.setText(productItem.getName());
        productPrice.setText(String.valueOf(productItem.getPrice())+" $");
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        if(item % 2 == 0)
            linearLayout.setBackgroundColor(Color.parseColor("#e7e8e5"));

        return view;
    }
} // Adapter End


