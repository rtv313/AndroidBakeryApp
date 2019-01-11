package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class AddProductActivity extends Activity {
    Button buttonAddProduct;
    EditText editTextName,editTextCost,editTextPrice;
    ImageView imgView;
    private ProgressDialog dialog;
    private static int RESULT_LOAD_IMG = 1;
    Uri selectedImage = Uri.EMPTY;
    boolean biggerThan1mb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCost = findViewById(R.id.editTextCost);

        imgView = findViewById(R.id.imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery();
            }
        });

        buttonAddProduct = findViewById(R.id.btnAddProduct);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = ValidateInputFields();

                if (valid == true) {
                    String Name = editTextName.getText().toString();
                    float Price  = Float.parseFloat(editTextPrice.getText().toString());
                    float Cost  = Float.parseFloat(editTextCost.getText().toString());
                    new AddProduct(AddProductActivity.this, selectedImage, dialog, Name, Price, Cost).execute();
                }
            }
        });

        dialog = new ProgressDialog(AddProductActivity.this);
    }

    public boolean ValidateInputFields(){

        String Name = editTextName.getText().toString();
        String emptyName = Name.replace(" ","");

        if(TextUtils.isEmpty(Name) || emptyName.equals("")) {
            Toast.makeText(getBaseContext(), "Ingresa un nombre", Toast.LENGTH_LONG).show();
            return false;
        }

        String Price  = editTextPrice.getText().toString();

        if(TextUtils.isEmpty(Price)) {
            Toast.makeText(getBaseContext(), "Ingresa un Precio", Toast.LENGTH_LONG).show();
            return false;
        }

        String Cost  = editTextCost.getText().toString();
        if(TextUtils.isEmpty(Cost)) {
            Toast.makeText(getBaseContext(), "Ingresa un Costo", Toast.LENGTH_LONG).show();
            return false;
        }


        if(selectedImage == Uri.EMPTY){
            Toast.makeText(getBaseContext(), "Selecciona una foto", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void NoInternetAlert(){
        Toast.makeText(this,"No hay internet, conectate para agregar un producto",Toast.LENGTH_LONG).show();
    }

    public void ProductExist(){
        Toast.makeText(this,"Ya hay un producto con este nombre",Toast.LENGTH_LONG).show();
    }

    public void ProductCreated(){
        Toast.makeText(this,"Producto Creado!",Toast.LENGTH_LONG).show();
    }

    public void ImageIsToBig(){
        Toast.makeText(this,"La imagen es mayor a 0.7Mb busca una mas pequeña",Toast.LENGTH_LONG).show();
    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                selectedImage = data.getData();
                imgView.setImageURI(selectedImage);
            } else {
                Toast.makeText(this, "No escogiste una imagen", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            selectedImage = Uri.EMPTY;
        }
    }
}
