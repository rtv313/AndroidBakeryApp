package com.example.usuario.bakery91;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SalesDataActivity extends AppCompatActivity {

    private ListView productsDataList;
    private ProgressDialog dialog;
    private ArrayList<ProductDataResume> productsData;
    protected static TextView txtFirstDate,txtSecondDate;
    private TextView totalIconme;
    private TextView totalEarnings;
    private String firstDate,secondDate;
    private Date validatorDate1,validatorDate2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_data);

        dialog = new ProgressDialog(SalesDataActivity.this);

        Calendar calendar = Calendar.getInstance();

        int lastDayInt = calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        String lastDay = String.valueOf(lastDayInt);

        int monthInt = calendar.get(Calendar.MONTH)+1;
        String month;
        if(monthInt < 10){
            month = "0" + String.valueOf(monthInt);
        }else{
            month = String.valueOf(monthInt);
        }

        int yearInt = calendar.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);
        firstDate =  year+"-"+month+"-01";
        secondDate = year+"-"+month+"-"+lastDay;

        txtFirstDate = findViewById(R.id.selected_time1);
        txtSecondDate = findViewById(R.id.selected_time2);
        totalIconme = findViewById(R.id.TxtFullIncome);
        totalEarnings = findViewById(R.id.TxtEarnings);

        Button displayFirstDateBtn = findViewById(R.id.btn_select_time);
        Button displaySecondDateBtn = findViewById(R.id.btn_select_time2);
        Button searchProductsData = findViewById(R.id.btnSearch);

        productsData = new ArrayList<ProductDataResume>();
        productsDataList = findViewById(R.id.productsDataList);
        assert displayFirstDateBtn != null;

        displayFirstDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.setActivity(SalesDataActivity.this,true);
                mDatePicker.show(getSupportFragmentManager(), "Selecciona una fecha");
            }
        });

        displaySecondDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.setActivity(SalesDataActivity.this,false);
                mDatePicker.show(getSupportFragmentManager(), "Selecciona una fecha");
            }
        });

        searchProductsData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String fecha =  validatorDate1.toString();

                if(validatorDate1.after(validatorDate2)){
                    Toast.makeText(SalesDataActivity.this,"Segunda fecha es menor a la primera",Toast.LENGTH_LONG).show();
                }else {
                    new GetSalesData(SalesDataActivity.this, dialog, productsData, firstDate, secondDate).execute();
                }
            }
        });
        setFirstDate(firstDate,yearInt,monthInt-1,1);
        setSecondDate(secondDate,yearInt,monthInt-1,lastDayInt);
        new GetSalesData(this,dialog,productsData,firstDate,secondDate).execute();
    }
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private SalesDataActivity salesDataActivity;
        private boolean firstButton;

       public void setActivity(SalesDataActivity salesDataActivity,boolean firstButton){
           this.salesDataActivity = salesDataActivity;
           this.firstButton = firstButton;
       }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month++;
            String yearS = String.valueOf(year);
            String monthS;
            String dayS;

            if(month < 10){
                monthS = "0"+String.valueOf(month);
            }else{
                monthS = String.valueOf(month);
            }

            if(day < 10){
                dayS = "0"+String.valueOf(day);
            }else{
                dayS= String.valueOf(day);
            }

            String Date  = yearS+"-"+monthS+"-"+dayS;

            if(firstButton == true){
                salesDataActivity.setFirstDate(Date,year,month-1,day);
            }else{
                salesDataActivity.setSecondDate(Date,year,month-1,day);
            }
        }
    }

    public void setFirstDate(String firstDate,int year, int month, int day){
        this.firstDate = firstDate;
        this.txtFirstDate.setText(firstDate);
        validatorDate1 = new Date(year,month,day);
    }

    public void setSecondDate(String secondDate,int year, int month, int day){
        this.secondDate = secondDate;
        this.txtSecondDate.setText(secondDate);
        validatorDate2 = new Date(year,month,day);
    }


    private void CalculateIncome(ArrayList<ProductDataResume> productsData){

        float totalIncome = 0.0f;
        float totalCost = 0.0f;

        for (int i = 0; i < productsData.size(); i++){
            ProductDataResume product = productsData.get(i);
            totalIncome += product.getQuantity() * product.getPrice();
            totalCost += product.getQuantity() * product.getCost();
        }

        float earningTotal = totalIncome - totalCost;

        totalIconme.setText(String.valueOf(totalIncome) +" $");
        totalEarnings.setText(String.valueOf(totalCost) +" $");
    }

    public void createlist(){
        CalculateIncome(productsData);
        MyAdapterProductData adapter = new MyAdapterProductData(SalesDataActivity.this, productsData);
        productsDataList.setAdapter(adapter);
    }

    public void NoInternetAlert(){
        Toast.makeText(this,"No hay internet, conectate para ver tus datos",Toast.LENGTH_LONG).show();
    }
}

// My MyAdapterProductResume
class MyAdapterProductData extends BaseAdapter {

    ArrayList<ProductDataResume> productsData;
    LayoutInflater lInflater;
    private int totalProducts;

    MyAdapterProductData(Context context, ArrayList<ProductDataResume> productsData){
        super();
        this.productsData = productsData;
        lInflater = LayoutInflater.from(context);

        for (int i = 0; i < productsData.size(); i++){
            totalProducts += productsData.get(i).getQuantity();
        }
    }

    @Override
    public int getCount(){
        return productsData.size();
    }

    @Override
    public Object getItem(int index){
        return productsData.get(index);
    }

    @Override
    public long getItemId(int id){
        return id;
    }

    @Override
    public View getView(int item, View view, ViewGroup parent){

        view = lInflater.inflate(R.layout.product_data_row,null);
        TextView Product = view.findViewById(R.id.txtProduct);
        TextView Quantity = view.findViewById(R.id.txtQuantity);
        TextView Percentage = view.findViewById(R.id.txtPercentage);
        TextView Income = view.findViewById(R.id.txtIncome);
        TextView Earning = view.findViewById(R.id.txtEarnings);

        ProductDataResume productDataResumeItem = productsData.get(item);

        Product.setText(productDataResumeItem.getProductName());
        Quantity.setText(String.valueOf(productDataResumeItem.getQuantity()));

        int percentage = (100*productDataResumeItem.getQuantity())/totalProducts;
        Percentage.setText(String.valueOf(percentage)+"%");

        float totalIncome = productDataResumeItem.getPrice() * (float)productDataResumeItem.getQuantity();
        Income.setText(String.valueOf(totalIncome)+" $");

        float totalEarning  = totalIncome - ((float)productDataResumeItem.getQuantity() * productDataResumeItem.getCost());
        Earning.setText(String.valueOf(totalEarning)+" $");

        return view;
    }
} // Adapter End
