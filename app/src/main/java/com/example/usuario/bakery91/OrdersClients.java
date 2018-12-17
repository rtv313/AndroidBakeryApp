package com.example.usuario.bakery91;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import java.util.ArrayList;


class Order{
    String name;
    String status;
}


public class OrdersClients extends AppCompatActivity implements OnItemClickListener{

    ListView orders;
    ArrayList<Order> pendentOrdersArray;
    ArrayList<Order> finishedOrdersArray;
    ArrayList<Order> canceledOrdersArray;
    ProgressDialog dialog;
    ArrayList<OrderClient> ordersAbstract = new ArrayList<OrderClient>();
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

        Button buttonPending = (Button)findViewById(R.id.btnPending);
        Button buttonFinished = (Button)findViewById(R.id.btnFinished);
        Button buttonCancel = (Button)findViewById(R.id.btnCancel);

        // Test RESTFUL
         dialog = new ProgressDialog(OrdersClients.this);
         new GetOrders(dialog,this,ordersAbstract).execute();

        // Lista de ordenes pendientes
        pendentOrdersArray = new ArrayList<Order>();

        Order nuevaOrden = new Order();
        nuevaOrden.name ="Don raro";
        nuevaOrden.status = "Pendiente";

        Order nuevaOrden2 = new Order();
        nuevaOrden2.name ="Don raro";
        nuevaOrden2.status = "Pendiente";

        pendentOrdersArray.add(nuevaOrden);
        pendentOrdersArray.add(nuevaOrden2);

        // Lista de ordenes Terminadas
        finishedOrdersArray = new ArrayList<Order>();

        Order nuevaOrdenTerminada = new Order();
        nuevaOrdenTerminada.name ="Don raro";
        nuevaOrdenTerminada.status = "Terminada";

        Order nuevaOrdenTerminada2 = new Order();
        nuevaOrdenTerminada2.name ="Don raro";
        nuevaOrdenTerminada2.status = "Terminada";

        finishedOrdersArray.add(nuevaOrdenTerminada);
        finishedOrdersArray.add(nuevaOrdenTerminada2);

        // Lista de ordenes Canceladas
        canceledOrdersArray = new ArrayList<Order>();

        Order nuevaOrdenCancelada = new Order();
        nuevaOrdenCancelada.name ="Don raro";
        nuevaOrdenCancelada.status = "Cancelada";

        Order nuevaOrdenCancelada2 = new Order();
        nuevaOrdenCancelada2.name ="Don raro";
        nuevaOrdenCancelada2.status = "Cancelada";

        canceledOrdersArray.add(nuevaOrdenCancelada);
        canceledOrdersArray.add(nuevaOrdenCancelada2);

        orders =(ListView) findViewById(R.id.orders_list);
        MyAdapter adapter = new MyAdapter(this,pendentOrdersArray);
        orders.setAdapter(adapter);
        orders.setOnItemClickListener(this);

        buttonPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders =(ListView) findViewById(R.id.orders_list);
                MyAdapter adapter = new MyAdapter(OrdersClients.this,pendentOrdersArray);
                orders.setAdapter(adapter);
                orders.setOnItemClickListener(OrdersClients.this);
            }
        });

        buttonFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders =(ListView) findViewById(R.id.orders_list);
                MyAdapter adapter = new MyAdapter(OrdersClients.this,finishedOrdersArray);
                orders.setAdapter(adapter);
                orders.setOnItemClickListener(OrdersClients.this);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders =(ListView) findViewById(R.id.orders_list);
                MyAdapter adapter = new MyAdapter(OrdersClients.this,canceledOrdersArray);
                orders.setAdapter(adapter);
                orders.setOnItemClickListener(OrdersClients.this);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> av ,View v,int position,long id)
    {
        Order order = (Order)orders.getItemAtPosition(position);
    }
}

class MyAdapter extends BaseAdapter{

    ArrayList<Order> ordersArrayList;
    LayoutInflater lInflater;

    MyAdapter(Context context,ArrayList<Order> orders){
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
        TextView status = view.findViewById(R.id.textViewStatus);

        clientName.setText(ordersArrayList.get(item).name);
        status.setText(ordersArrayList.get(item).status);
        return view;
    }
} // Adapter End





