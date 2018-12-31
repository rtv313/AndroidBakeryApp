package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class GetOrderBreakDown extends AsyncTask {

    private OrderActivity ContextOrderClient;
    private ProgressDialog progressDialog;
    private StringBuffer response;
    private String responseText;
    private OrderClient client;
    private float totalPrice;
    private ArrayList<ProductClient> clientProducts;
    private boolean accessToInternet;
    private String clientId;

    public GetOrderBreakDown(ProgressDialog progressDialog, OrderActivity ContextOrderClient, ArrayList<ProductClient> clientProducts ,String clientId)
    {
        this.progressDialog = progressDialog;
        this.ContextOrderClient = ContextOrderClient;
        this.clientProducts = clientProducts;
        this.clientId = clientId;
        this.client = new OrderClient(0,"","","","","","");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Showing progress dialog
        progressDialog = new ProgressDialog(ContextOrderClient);
        progressDialog.setMessage("Cargando Cliente");
        progressDialog.setCancelable(false);
        progressDialog.show();
        clientProducts.clear();
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

        ContextOrderClient.createClient(client,totalPrice);
        ContextOrderClient.createlist();
        ContextOrderClient.CreateOrderStatus(client.getStatus());

        if(accessToInternet == false) {
            ContextOrderClient.NoInternetAlert();
        }
    }


    protected Void getWebServiceResponseData(){

        try {
            JSONObject ordersParameters = new JSONObject();
            String path = ContextOrderClient.getString(R.string.url_rest_orderClient)+clientId;
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization", ContextOrderClient.getString(R.string.rest_token));


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
            JSONParser parser = new JSONParser();
            responseText = response.toString();
            Object obj = parser.parse(responseText);
            JSONObject json = (JSONObject)obj;
            JSONObject clientJson = (JSONObject)json.get("Client");

            client.setId((int)(long) clientJson.get("id"));
            client.setName((String)clientJson.get("Name"));
            client.setLastName((String)clientJson.get("LastName"));
            client.setNote((String)clientJson.get("Note"));
            client.setPhone((String)clientJson.get("Phone"));
            client.setStatus((String)clientJson.get("Status"));
            client.setOrderDate((String)clientJson.get("OrderDate"));

            JSONArray productsJSON = (JSONArray)json.get("Products");
           // JSONObject jsonobject = (JSONObject) productsJSON.get(0);
           // String x  = (String)jsonobject.get("name");
            totalPrice = 0.0f;

            for (int i = 0; i < productsJSON.size(); i++) {
                JSONObject productJson = (JSONObject)  productsJSON.get(i);
                String name = (String) productJson.get("name");
                int quantity = (int)(long)productJson.get("quantity");
                float price = (float)(double)productJson.get("price");
                totalPrice += price * quantity;
                ProductClient product = new ProductClient(name,quantity,price);
                clientProducts.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
