package com.example.usuario.bakery91;


import android.util.Base64;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class UpdateProduct extends AsyncTask {

    private UpdateProductActivity updateProductActivity;
    private ProgressDialog progressDialog;
    private StringBuffer response;
    private String responseText,ImageBase64;
    private Uri selectedImage;
    private ProductDetail productUpdated;
    private boolean accessToInternet;
    private boolean ProductExist;
    private boolean biggerThan1mb;

    public UpdateProduct(UpdateProductActivity updateProductActivity,ProgressDialog progressDialog,ProductDetail productUpdated,Uri selectedImage ){

        this.updateProductActivity = updateProductActivity;
        this.progressDialog = progressDialog;
        this.productUpdated = productUpdated;
        this.selectedImage = selectedImage;

    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private String convertToBase64(String imagePath)
    {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] byteArrayImage = baos.toByteArray();
        long imageSize = byteArrayImage.length; // convert to MB
        float imageSizeKb = (float)imageSize/1024;
        float imageSizeMb =  imageSizeKb/1024;

        if(imageSizeMb > 0.7){
            biggerThan1mb = true;
        }

        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog.setMessage("Actualizando Producto");
        progressDialog.setCancelable(false);
        progressDialog.show();
        accessToInternet = true;

        if(selectedImage != Uri.EMPTY) {
            final String selectedFilePath = getRealPathFromURI(updateProductActivity, selectedImage);
            ImageBase64 = convertToBase64(selectedFilePath);
        }else{
            ImageBase64 = "NO_NEW_IMAGE";
        }

        if(biggerThan1mb==true){
            updateProductActivity.ImageIsToBig();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return getWebServiceResponseData();
    }

    protected Void getWebServiceResponseData(){

        try {
            String path;
            org.json.JSONObject addProductParameters = new org.json.JSONObject();
            addProductParameters.put("Name",productUpdated.getName());
            addProductParameters.put("Price",productUpdated.getPrice());
            addProductParameters.put("ProductionCost",productUpdated.getProductionCost());

            if(selectedImage != Uri.EMPTY){
                addProductParameters.put("Image",ImageBase64);
                 path = updateProductActivity.getString(R.string.url_rest_products)+String.valueOf(productUpdated.getId());
            }else{
                path = updateProductActivity.getString(R.string.url_rest_productsDetail)+String.valueOf(productUpdated.getId());
            }

            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(25000);
            conn.setConnectTimeout(25000);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization",updateProductActivity.getString(R.string.rest_token));
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            String json = addProductParameters.toString();
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
                Log.d(TAG, "Code:" + String.valueOf(responseCode));
                if(responseCode == 400){
                    ProductExist = true;
                }else {
                    accessToInternet = false;
                }
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            Log.d(TAG, "data:" + responseText);
            responseText = response.toString();
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
        }

        if (ProductExist == true){
            updateProductActivity.ProductExist();
        }

        if(ProductExist == false && accessToInternet == true && biggerThan1mb == false){
            updateProductActivity.ProductUpdated();
        }
    }
}
