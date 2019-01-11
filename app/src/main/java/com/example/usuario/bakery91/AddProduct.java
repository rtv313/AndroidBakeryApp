package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;


public class AddProduct extends AsyncTask {

    private ProgressDialog progressDialog;
    private StringBuffer response;
    private String responseText;
    private AddProductActivity addProductActivity;
    private Uri selectedImage;
    private boolean accessToInternet;
    private String Name,ImageBase64;
    private float Price,ProductionCost;
    private boolean ProductExist;
    private boolean  biggerThan1mb;


    public AddProduct(AddProductActivity addProductActivity, Uri selectedImage,ProgressDialog progressDialog,String Name,float Price,float ProductionCost){
        this.addProductActivity = addProductActivity;
        this.selectedImage = selectedImage;
        this.progressDialog = progressDialog;
        this.Name = Name;
        this.Price = Price;
        this.ProductionCost = ProductionCost;
        ProductExist = false;
        biggerThan1mb = false;
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
        bm.compress(Bitmap.CompressFormat.JPEG, 85, baos);
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
        progressDialog = new ProgressDialog(addProductActivity);
        progressDialog.setMessage("Creando nuevo Producto");
        progressDialog.setCancelable(false);
        progressDialog.show();

        accessToInternet = true;
        final String selectedFilePath = getRealPathFromURI(addProductActivity, selectedImage);
        ImageBase64 = convertToBase64(selectedFilePath);

        if(biggerThan1mb==true){
            addProductActivity.ImageIsToBig();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if(biggerThan1mb==false)
            return getWebServiceResponseData();
        else
            return null;
    }

    protected Void getWebServiceResponseData(){

        try {
            JSONObject addProductParameters = new JSONObject();
            addProductParameters.put("Name",Name);
            addProductParameters.put("Price",Price);
            addProductParameters.put("ProductionCost",ProductionCost);
            addProductParameters.put("Image",ImageBase64);

            String path = addProductActivity.getString(R.string.url_rest_products);
            URL url = new URL(path);
            Log.d(TAG, "ServerData: " + path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(25000);
            conn.setConnectTimeout(25000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Authorization",addProductActivity.getString(R.string.rest_token));
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
            addProductActivity.NoInternetAlert();
        }

        if (ProductExist == true){
            addProductActivity.ProductExist();
        }

        if(ProductExist == false && accessToInternet == true && biggerThan1mb == false){
            addProductActivity.ProductCreated();
        }
    }
}



