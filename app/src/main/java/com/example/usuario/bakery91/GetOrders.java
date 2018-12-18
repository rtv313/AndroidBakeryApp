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

    public GetOrders(ProgressDialog progressDialog,OrdersClients ContextOrderClients,ArrayList<OrderClient> orders)
    {
        this.progressDialog = progressDialog;
        this.ContextOrderClients = ContextOrderClients;
        this.orders = orders;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Showing progress dialog
        progressDialog = new ProgressDialog(ContextOrderClients);
        progressDialog.setMessage("Cargando Pedidos");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return getWebServiceResponseData();
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        // Dismiss the progress dialog
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        ContextOrderClients.createlist();
    }


    protected Void getWebServiceResponseData(){

        try {

            JSONObject ordersParameters = new JSONObject();
            ordersParameters.put("Status","PENDIENTE");

            URL url;
            String path = "https://splendid-fly-96.localtunnel.me/Orders/";
            url = new URL(path);

            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization","Token 89f39b9d21b410971637dc5a76d60ab85a3a8da8");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            String json = ordersParameters.toString();
            os.write(json.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            Log.d(TAG, "Response code: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                // Reading response from input Stream
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String output;
                response = new StringBuffer();

                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
            }}
        catch(Exception e){
            e.printStackTrace();
        }

        responseText = response.toString();
        //Call ServerData() method to call webservice and store result in response
        //  response = service.ServerData(path, postDataParams);
        Log.d(TAG, "data:" + responseText);
        try {
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}