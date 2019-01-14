package com.example.usuario.bakery91;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private boolean accessToInternet;
    private UpdateProductActivity updateProductActivity;

    public DownloadImage(ImageView bmImage,UpdateProductActivity updateProductActivity) {
        this.bmImage = bmImage;
        this.updateProductActivity = updateProductActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        accessToInternet = true;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            accessToInternet = false;
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        if(accessToInternet==false){
            updateProductActivity.NoInternetAlert();
        }
    }
}