package com.example.usuario.bakery91;
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

public class OrdersClients extends AppCompatActivity implements OnItemClickListener{
    public enum List_Status {PENDIENTE,CANCELADO,TERMINADO}
    ListView orders;
    ProgressDialog dialog;
    ArrayList<OrderClient> ordersAbstract = new ArrayList<OrderClient>();
    List_Status status = List_Status.PENDIENTE;


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



    }

    @Override
    public void onItemClick(AdapterView<?> av ,View v,int position,long id)
    {
        OrderClient order = (OrderClient)orders.getItemAtPosition(position);
    }


    public void createlist() {
        MyAdapter adapter = new MyAdapter(this,ordersAbstract);
        orders.setAdapter(adapter);
        orders.setOnItemClickListener(this);
    }
}

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
        TextView status = view.findViewById(R.id.textViewStatus);

        clientName.setText(ordersArrayList.get(item).Name);
        status.setText(ordersArrayList.get(item).Status);
        return view;
    }
} // Adapter End





