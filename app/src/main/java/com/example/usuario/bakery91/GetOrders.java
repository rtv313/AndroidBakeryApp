package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

class GetOrders extends AsyncTask {

    private OrdersClients ContextOrderClients;
    private ProgressDialog progressDialog;
    private StringBuffer response;
    private String responseText;
    private ListView ordersList;
    private ArrayList<OrderClient> orders;
    private String orderStatus;
    private boolean accessToInternet;

    public GetOrders(ProgressDialog progressDialog,OrdersClients ContextOrderClients,ArrayList<OrderClient> orders,String orderStatus)
    {
        this.progressDialog = progressDialog;
        this.ContextOrderClients = ContextOrderClients;
        this.orders = orders;
        this.orderStatus = orderStatus;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Showing progress dialog
        progressDialog = new ProgressDialog(ContextOrderClients);
        progressDialog.setMessage("Cargando Pedidos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        orders.clear();
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

        ContextOrderClients.createlist();

        if(accessToInternet == false)
            ContextOrderClients.NoInternetAlert();
    }


    protected Void getWebServiceResponseData(){

        try {
            JSONObject ordersParameters = new JSONObject();
            ordersParameters.put("Status",orderStatus);
            String path = ContextOrderClients.getString(R.string.url_rest_orders);
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization",ContextOrderClients.getString(R.string.rest_token));
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            String json = ordersParameters.toString();
            os.write(json.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response code: " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK){
                // Reading response from input Stream
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
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
                int id = jsonobject.getInt("id");
                String Name = jsonobject.getString("Name");
                String LastName = jsonobject.getString("LastName");
                String Phone = jsonobject.getString("Phone");
                String Status = jsonobject.getString("Status");
                String Note = jsonobject.getString("Note");
                String OrderDate = jsonobject.getString("OrderDate");

                Log.d(TAG, "id:" + id);
                Log.d(TAG, "Name:" + Name);
                OrderClient order = new OrderClient(id,Name,LastName,Phone,Status,Note,OrderDate);
                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}