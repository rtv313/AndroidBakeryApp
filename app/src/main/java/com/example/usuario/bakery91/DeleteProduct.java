package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class DeleteProduct extends AsyncTask {

    private int productId;
    private boolean accessToInternet,deleted;
    private UpdateProductActivity updateProductActivity;
    private ProgressDialog progressDialog;

    public DeleteProduct(int productId, ProgressDialog progressDialog, UpdateProductActivity updateProductActivity){
        this.productId = productId;
        this.progressDialog = progressDialog;
        this.updateProductActivity = updateProductActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(updateProductActivity);
        progressDialog.setMessage("Borrando Producto");
        progressDialog.setCancelable(false);
        progressDialog.show();
        accessToInternet = true;
        deleted = true;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return getWebServiceResponseData();
    }

    protected Void getWebServiceResponseData(){

        try {

            String path = updateProductActivity.getString(R.string.url_rest_productsDetail)+String.valueOf(productId);
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization",updateProductActivity.getString(R.string.rest_token));
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response code: " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK){

            }else{
                Log.d(TAG, "Code:" + String.valueOf(responseCode));
                if(responseCode == 400){
                    deleted = false;
                }else {
                    accessToInternet = false;
                }
                return null;
            }
        }
        catch(Exception e){
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
        }

        if(deleted == true){
            updateProductActivity.ProductDeleted();
        }else{
            updateProductActivity.ProductNoDeleted();
        }
    }
}
