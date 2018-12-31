package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class ChangeOrderStatus extends AsyncTask {

    private boolean accessToInternet;
    private String status;
    private StringBuffer response;
    private String responseText;
    private String previousStatus;
    private OrderActivity orderActivity;
    private String clientId;

    public ChangeOrderStatus(String status,String previousStatus,OrderActivity orderActivity,String clientId){
        this.status = status;
        this.previousStatus = previousStatus;
        this.orderActivity = orderActivity;
        this.clientId = clientId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        accessToInternet = true;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return getWebServiceResponseData();
    }

    @Override
    protected void onPostExecute(Object o) {

        super.onPostExecute(o);

        if (accessToInternet == true){
            orderActivity.CreateOrderStatus(status);
        }else{
            orderActivity.CreateOrderStatus(previousStatus);
            orderActivity.NoInternetAlertForUpdate();
        }
    }

    protected Void getWebServiceResponseData(){

        try {
            org.json.JSONObject ordersParameters = new org.json.JSONObject();
            ordersParameters.put("Status",status);
            String path = orderActivity.getString(R.string.url_rest_orderClient)+clientId;
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization",orderActivity.getString(R.string.rest_token));
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            String json = ordersParameters.toString();
            os.write(json.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response code: " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK){
                accessToInternet = true;
            }else{
                accessToInternet = false;
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
