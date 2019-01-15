package com.example.usuario.bakery91;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProductActivity extends ActivityWithMenu {

    Button btnUpdateProduct,btnDeleteProduct;
    EditText editTextName,editTextCost,editTextPrice;
    ImageView imgView;
    private ProgressDialog dialog;
    private static int RESULT_LOAD_IMG = 1;
    Uri selectedImage = Uri.EMPTY;
    private ProductDetail productDetail;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layout = R.layout.activity_update_product;
        menuTitle = "Producto";
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int id = intent.getIntExtra("ProductId",0);
        productId  = String.valueOf(id);

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

        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean valid = ValidateInputFields();

                if (valid == true) {
                    String Name = editTextName.getText().toString();
                    float Price  = Float.parseFloat(editTextPrice.getText().toString());
                    float Cost  = Float.parseFloat(editTextCost.getText().toString());
                    int id  = Integer.parseInt(productId);
                    ProductDetail  productDetail = new ProductDetail(id,Name,Price,Cost,"");
                    new UpdateProduct(UpdateProductActivity.this,dialog,productDetail,selectedImage).execute();
                }
            }
        });

        dialog = new ProgressDialog(UpdateProductActivity.this);
        productDetail = new ProductDetail(1,"name",0.0f,0.0f,"url");
        new GetProduct(dialog,productId,UpdateProductActivity.this,productDetail).execute();

        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteProduct(productDetail.getId(),dialog,UpdateProductActivity.this).execute();
            }
        });
    }

    public void SetProductDetail(){
        // Here we set the EditTexts and image
        editTextName.setText(productDetail.getName(), TextView.BufferType.EDITABLE);
        editTextPrice.setText(String.valueOf(productDetail.getPrice()),TextView.BufferType.EDITABLE);
        editTextCost.setText(String.valueOf(productDetail.getProductionCost()),TextView.BufferType.EDITABLE);
        String URL = this.getString(R.string.url_rest_image) + productDetail.getImageUrl();
        new DownloadImage((ImageView)findViewById(R.id.imgView),this).execute(URL);
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

        return true;
    }

    public void NoInternetAlert(){
        Toast.makeText(this,"No hay internet, conectate para agregar un producto",Toast.LENGTH_LONG).show();
    }

    public void ProductExist(){
        Toast.makeText(this,"Ya hay un producto con este nombre",Toast.LENGTH_LONG).show();
    }

    public void ProductDeleted(){
        Intent intent = new Intent(UpdateProductActivity.this, ProductsMenuActivity.class);
        intent.putExtra("FromDeletedProduct",true);
        startActivity(intent);
        finish();
    }

    public void ProductNoDeleted(){
        Toast.makeText(this,"El producto no se puede borrar porque hay pedidos pendientes de el",Toast.LENGTH_LONG).show();
    }

    public void ProductUpdated(){
        Toast.makeText(this,"El producto se actualizo",Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed(){

        if (menuOpen == false){
            super.onBackPressed();
            Intent intent = new Intent(UpdateProductActivity.this, ProductsMenuActivity.class);
            startActivity(intent);
            finish();
        }else{
            this.mDrawerLayout.closeDrawers();
        }
    }
}
