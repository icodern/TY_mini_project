package com.example.tyminiproject.Model;

public class Request {

    String messName;
    String Menu;
    String TotalPrice;
    String Quantity;
    String CustName;
    String phone;
    String Image;

    public Request() {
    }


    public String getMessName() {
        return messName;
    }

    public void setMessName(String messName) {
        this.messName = messName;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Request(String messName, String menu, String totalPrice, String quantity, String custName, String phone) {
        this.messName = messName;
        Menu = menu;
        TotalPrice = totalPrice;
        Quantity = quantity;
        CustName = custName;
        this.phone = phone;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Request(String messName, String menu, String totalPrice, String quantity, String custName, String phone, String image) {
        this.messName = messName;
        Menu = menu;
        TotalPrice = totalPrice;
        Quantity = quantity;
        CustName = custName;
        this.phone = phone;
        Image = image;
    }

}
