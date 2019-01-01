package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class GetOrdersResume  extends AsyncTask {

    private OrdersResume ContextOrdersResume;
    private ProgressDialog progressDialog;
    private StringBuffer response;
    private String responseText;
    private ListView ordersList;
    private ArrayList<ProductResume> productsResume;
    private boolean accessToInternet;

    public GetOrdersResume(OrdersResume ContextOrdersResume,ProgressDialog progressDialog,ArrayList<ProductResume> productsResume){
        this.ContextOrdersResume = ContextOrdersResume;
        this.progressDialog = progressDialog;
        this.productsResume = productsResume;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Showing progress dialog
        progressDialog = new ProgressDialog(ContextOrdersResume);
        progressDialog.setMessage("Cargando Resumen de Pedidos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        productsResume.clear();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        accessToInternet = true;
        return getWebServiceResponseData();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        // Dismiss the progress dialog
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        ContextOrdersResume.createlist();

        if(accessToInternet == false)
            ContextOrdersResume.NoInternetAlert();
    }

    protected Void getWebServiceResponseData(){

        try {
            JSONObject ordersParameters = new JSONObject();
            String path = ContextOrdersResume.getString(R.string.url_rest_ordersResume);
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization",ContextOrdersResume.getString(R.string.rest_token));

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response code: " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK){
                // Reading response from input Stream
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            }else{
                accessToInternet = false;
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            Log.d(TAG, "data:" + responseText);
            responseText = response.toString();
            JSONArray jsonarray = new JSONArray(responseText);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String Name = jsonobject.getString("ProductName");
                int Quantity = jsonobject.getInt("Quantity");
                ProductResume productResume = new ProductResume(Name,Quantity);
                productsResume.add(productResume);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
