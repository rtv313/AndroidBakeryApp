package com.example.usuario.bakery91;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;


public class GetProduct extends AsyncTask {

    private UpdateProductActivity updateProductActivity;
    private ProgressDialog progressDialog;
    private StringBuffer response;
    private String responseText;
    private ProductDetail productDetail;
    private boolean accessToInternet;
    private String productId;

    public GetProduct(ProgressDialog progressDialog,String productId,UpdateProductActivity updateProductActivity,ProductDetail productDetail){
        this.progressDialog = progressDialog;
        this.productId = productId;
        this.updateProductActivity = updateProductActivity;
        this.productDetail = productDetail;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(updateProductActivity);
        progressDialog.setMessage("Cargando Producto");
        progressDialog.setCancelable(false);
        progressDialog.show();
        accessToInternet = true;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return getWebServiceResponseData();
    }

    protected Void getWebServiceResponseData(){

        try {
            String path = updateProductActivity.getString(R.string.url_rest_productsDetail)+productId;
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization", updateProductActivity.getString(R.string.rest_token));

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
            productDetail.setId((int)(long) json.get("id"));
            productDetail.setName((String)json.get("Name"));
            productDetail.setPrice((float)(double)json.get("Price"));
            productDetail.setProductionCost((float)(double)json.get("ProductionCost"));
            productDetail.setImageUrl((String)json.get("Image"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        // Dismiss the progress dialog
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        if(accessToInternet == false) {
            updateProductActivity.NoInternetAlert();
        }else{
            updateProductActivity.SetProductDetail();
        }
    }
}
