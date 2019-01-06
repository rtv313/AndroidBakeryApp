package com.example.usuario.bakery91;

import android.provider.ContactsContract;

public class Product {

    public int id;
    public String Name;
    public float Price;
    public float ProductionCost;
    public String ImageURL;

    public Product(int id, String name, float price, float productionCost, String imageURL) {
        this.id = id;
        Name = name;
        Price = price;
        ProductionCost = productionCost;
        ImageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public float getProductionCost() {
        return ProductionCost;
    }

    public void setProductionCost(float productionCost) {
        ProductionCost = productionCost;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}

