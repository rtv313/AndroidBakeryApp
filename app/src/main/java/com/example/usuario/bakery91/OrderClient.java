package com.example.usuario.bakery91;
import java.util.Date;

public class OrderClient {
    int  id;
    String Name;
    String LastName;
    String Phone;
    String Status;
    String Note;
    String OrderDate;

   public OrderClient(int id , String Name,String LastName,String Phone, String Status, String Note,String OrderDate)
   {
       this.id = id;
       this.Name = Name;
       this.LastName = LastName;
       this.Phone = Phone;
       this.Status = Status;
       this.Note = Note;
       this.OrderDate = OrderDate;
   }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public String getNote() {
        return Note;
    }

    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    public String getOrderDate() {
        return OrderDate;
    }
}
