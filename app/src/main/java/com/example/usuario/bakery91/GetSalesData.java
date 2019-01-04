package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

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

public class GetSalesData extends AsyncTask {

    private SalesDataActivity salesDataActivity;
    private ProgressDialog progressDialog;
    private StringBuffer response;
    private String responseText;
    private String firstDate;
    private String secondDate;
    private ArrayList<ProductDataResume> datasResumeList;
    private boolean accessToInternet;

    public GetSalesData(SalesDataActivity salesDataActivity,ProgressDialog progressDialog,ArrayList<ProductDataResume> datasResumeList,String firstDate,String secondDate){
        this.salesDataActivity = salesDataActivity;
        this.progressDialog = progressDialog;
        this.datasResumeList = datasResumeList;
        this.firstDate = firstDate;
        this.secondDate = secondDate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Showing progress dialog
        progressDialog = new ProgressDialog(salesDataActivity);
        progressDialog.setMessage("Cargando Datos");
        progressDialog.setCancelable(false);
        progressDialog.show();
        datasResumeList.clear();
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

        salesDataActivity.createlist();

        if(accessToInternet == false)
            salesDataActivity.NoInternetAlert();
    }

    protected Void getWebServiceResponseData(){

        try {
            JSONObject ordersParameters = new JSONObject();
            ordersParameters.put("firstDate",firstDate);
            ordersParameters.put("secondDate",secondDate);
            String path = salesDataActivity.getString(R.string.url_rest_salesReport);
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization",salesDataActivity.getString(R.string.rest_token));
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
                String ProductName = jsonobject.getString("ProductName");
                int Quantity = jsonobject.getInt("Quantity");
                float Cost = (float)jsonobject.getDouble("Cost");
                float Price = (float)jsonobject.getDouble("Price");
                ProductDataResume productData = new ProductDataResume(ProductName,Quantity,Cost,Price);
                datasResumeList.add(productData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
