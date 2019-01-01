package com.example.usuario.bakery91;

public class ProductResume {

    public String Name;
    public int Quantity;

    public ProductResume(String Name, int Quantity)
    {
        this.Name = Name;
        this.Quantity = Quantity;
    }

    public void setName(String name){
        this.Name = Name;
    }

    public String getName(){
        return Name;
    }

    public void setQuantity(int Quantity){
        this.Quantity = Quantity;
    }

    public int getQuantity(){
        return  Quantity;
    }
}
