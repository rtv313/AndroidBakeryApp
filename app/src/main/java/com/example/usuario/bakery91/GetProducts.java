package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

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

public class GetProducts extends AsyncTask {

    private ProductsMenuActivity ContextProductsMenu;
    private ProgressDialog progressDialog;
    private StringBuffer response;
    private String responseText;
    private ArrayList<Product> products;
    private String orderStatus;
    private boolean accessToInternet;

    public  GetProducts(ProductsMenuActivity ContextProductsMenu, ProgressDialog progressDialog,ArrayList<Product> products){

        this.progressDialog = progressDialog;
        this.ContextProductsMenu = ContextProductsMenu;
        this.products = products;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Showing progress dialog
        progressDialog = new ProgressDialog(ContextProductsMenu);
        progressDialog.setMessage("Cargando Productos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        products.clear();
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

        ContextProductsMenu.createlist();

        if(accessToInternet == false)
            ContextProductsMenu.NoInternetAlert();
    }

    protected Void getWebServiceResponseData(){

        try {
            JSONObject ordersParameters = new JSONObject();
            ordersParameters.put("Status",orderStatus);
            String path = ContextProductsMenu.getString(R.string.url_rest_products);
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization",ContextProductsMenu.getString(R.string.rest_token));

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
                float  Price = (float)jsonobject.getDouble("Price");
                float ProductionCost = (float)jsonobject.getDouble("ProductionCost");
                String Image = jsonobject.getString("Image");
                Product product = new Product(id,Name,Price,ProductionCost,Image);
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
