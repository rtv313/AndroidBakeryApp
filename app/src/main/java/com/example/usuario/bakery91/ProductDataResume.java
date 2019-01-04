package com.example.usuario.bakery91;

public class ProductDataResume {
   public String ProductName;
   public int  Quantity;
   public float Cost;
   public float Price;

   public ProductDataResume(String ProductName,int Quantity,float Cost,float Price){
       this.ProductName = ProductName;
       this.Quantity = Quantity;
       this.Cost = Cost;
       this.Price = Price;
   }

   public void setProductName(String ProductName){
       this.ProductName = ProductName;
   }

   public String getProductName(){
       return ProductName;
   }

   public void setQuantity(int Quantity){
       this.Quantity = Quantity;
   }

   public int getQuantity(){
       return Quantity;
   }

   public void setCost(float Cost){
       this.Cost = Cost;
   }

   public float getCost(){
       return Cost;
   }

    public void setPrice(float Price){
        this.Price = Price;
    }

    public float getPrice(){
        return Price;
    }
}
