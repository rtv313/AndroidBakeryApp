package com.example.usuario.bakery91;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class OrdersClients extends ActivityWithMenu implements OnItemClickListener{
    public enum List_Status {PENDIENTE,CANCELADO,TERMINADO}
    ListView orders;
    ProgressDialog dialog;
    ArrayList<OrderClient> ordersAbstract = new ArrayList<OrderClient>();
    ArrayList<OrderClient> ordersAbstractFilter = new ArrayList<OrderClient>();
    List_Status status = List_Status.PENDIENTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layout = R.layout.activity_orders_clients_menu;
        menuTitle = "Pedidos";
        super.onCreate(savedInstanceState);

        Button buttonPending = (Button)findViewById(R.id.btnPending);
        Button buttonFinished = (Button)findViewById(R.id.btnFinished);
        Button buttonCancel = (Button)findViewById(R.id.btnCancel);
        EditText searchBox = (EditText)findViewById(R.id.searchText);

        buttonPending.setBackgroundResource(R.drawable.button_bg_active);
        buttonPending.setTextColor(Color.WHITE);
        // Test RESTFUL
        dialog = new ProgressDialog(OrdersClients.this);
        orders =(ListView) findViewById(R.id.orders_list);
        new GetOrders(dialog,this,ordersAbstract,"PENDIENTE").execute();

        buttonPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetOrders(dialog,OrdersClients.this,ordersAbstract,"PENDIENTE").execute();
                status = List_Status.PENDIENTE;

                Button buttonPending = (Button)findViewById(R.id.btnPending);
                buttonPending.setBackgroundResource(R.drawable.button_bg_active);
                buttonPending.setTextColor(Color.WHITE);

                Button buttonFinished = (Button)findViewById(R.id.btnFinished);
                buttonFinished.setBackgroundResource(R.drawable.button_bg);
                buttonFinished.setTextColor(Color.BLACK);

                Button buttonCancel = (Button)findViewById(R.id.btnCancel);
                buttonCancel.setBackgroundResource(R.drawable.button_bg);
                buttonCancel.setTextColor(Color.BLACK);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetOrders(dialog,OrdersClients.this,ordersAbstract,"CANCELADO").execute();
                status = List_Status.CANCELADO;

                Button buttonPending = (Button)findViewById(R.id.btnPending);
                buttonPending.setBackgroundResource(R.drawable.button_bg);
                buttonPending.setTextColor(Color.BLACK);

                Button buttonFinished = (Button)findViewById(R.id.btnFinished);
                buttonFinished.setBackgroundResource(R.drawable.button_bg);
                buttonFinished.setTextColor(Color.BLACK);

                Button buttonCancel = (Button)findViewById(R.id.btnCancel);
                buttonCancel.setBackgroundResource(R.drawable.button_bg_active);
                buttonCancel.setTextColor(Color.WHITE);
            }
        });

        buttonFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetOrders(dialog,OrdersClients.this,ordersAbstract,"TERMINADO").execute();
                status = List_Status.TERMINADO;

                Button buttonPending = (Button)findViewById(R.id.btnPending);
                buttonPending.setBackgroundResource(R.drawable.button_bg);
                buttonPending.setTextColor(Color.BLACK);

                Button buttonFinished = (Button)findViewById(R.id.btnFinished);
                buttonFinished.setBackgroundResource(R.drawable.button_bg_active);
                buttonFinished.setTextColor(Color.WHITE);

                Button buttonCancel = (Button)findViewById(R.id.btnCancel);
                buttonCancel.setBackgroundResource(R.drawable.button_bg);
                buttonCancel.setTextColor(Color.BLACK);
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                OrdersClients.this.filterList(s.toString());
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> av ,View v,int position,long id){
        OrderClient order = (OrderClient)orders.getItemAtPosition(position);
    }


    public void createlist() {
        MyAdapter adapter = new MyAdapter(this, ordersAbstract);
        orders.setAdapter(adapter);
        orders.setOnItemClickListener(this);
        EditText searchBox = (EditText)findViewById(R.id.searchText);
        filterList(searchBox.getText().toString());
    }

    public void NoInternetAlert(){
        Toast.makeText(this,"No hay internet, conectate para ver tus pedidos",Toast.LENGTH_LONG).show();
    }

    public void filterList(String filterText ){

        ordersAbstractFilter.clear();

        for(int i=0; i < ordersAbstract.size(); i++)
        {
            OrderClient filterItem =  ordersAbstract.get(i);
            String fullName = filterItem.getName()+ filterItem.getLastName();
            if(fullName.toLowerCase().contains(filterText.toLowerCase())){
                ordersAbstractFilter.add(filterItem);
            }
        }

        MyAdapter adapter = new MyAdapter(OrdersClients.this,ordersAbstractFilter);
        orders.setAdapter(adapter);
        orders.setOnItemClickListener(OrdersClients.this);
    }
}



// My Adapter
class MyAdapter extends BaseAdapter{

    ArrayList<OrderClient> ordersArrayList;
    LayoutInflater lInflater;

    MyAdapter(Context context,ArrayList<OrderClient> orders){
        ordersArrayList = orders;
        lInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return ordersArrayList.size();
    }

    @Override
    public Object getItem(int index){
        return ordersArrayList.get(index);
    }

    @Override
    public long getItemId(int id){
        return id;
    }

    @Override
    public View getView(int item, View view, ViewGroup parent){

        view = lInflater.inflate(R.layout.order_row,null);
        TextView clientName = view.findViewById(R.id.textViewClient);
        OrderClient clientItem =ordersArrayList.get(item);
        clientName.setText(clientItem.getName() +" "+ clientItem.getLastName());
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        if(item % 2 == 0)
            linearLayout.setBackgroundColor(Color.parseColor("#e7e8e5"));

        return view;
    }
} // Adapter End





