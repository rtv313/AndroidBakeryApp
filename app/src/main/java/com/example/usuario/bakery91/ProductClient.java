package com.example.usuario.bakery91;

public class ProductClient {

    public String name;
    public int quantity;
    public float price;

    public ProductClient(String name, int quantity,float price){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public float getPrice(){
        return price;
    }
}
