package com.example.usuario.bakery91;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        ArrayList<Order> ordersArrayList = new ArrayList<Order>();
        Order nuevaOrden = new Order();
        nuevaOrden.name ="Don raro";
        nuevaOrden.status = "Pendiente";

        Order nuevaOrden2 = new Order();
        nuevaOrden2.name ="Don raro";
        nuevaOrden2.status = "Pendiente";

        ordersArrayList.add(nuevaOrden);
        ordersArrayList.add(nuevaOrden2);

        orders =(ListView) findViewById(R.id.orders_list);
        MyAdapter adapter = new MyAdapter(this,ordersArrayList);
        orders.setAdapter(adapter);
        orders.setOnItemClickListener(this);
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
}
