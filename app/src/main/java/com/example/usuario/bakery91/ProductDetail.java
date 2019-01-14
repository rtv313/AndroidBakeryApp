package com.example.usuario.bakery91;

public class ProductDetail {

    public ProductDetail(int id, String name, float price, float productionCost, String imageUrl) {
        this.id = id;
        Name = name;
        this.price = price;
        this.productionCost = productionCost;
        this.imageUrl = imageUrl;
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
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(float productionCost) {
        this.productionCost = productionCost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int id;
    public String Name;
    public float price;
    public float productionCost;
    public String imageUrl;

}
