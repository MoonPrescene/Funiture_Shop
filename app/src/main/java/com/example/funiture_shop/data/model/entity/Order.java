package com.example.funiture_shop.data.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "orders")
public class Order implements Serializable {
    @PrimaryKey
    @NonNull
    private String orderID = "";
    private String userID = "";
    private int quantity = 0;
    private String timeCreate = "";
    private String address = "";
    private int status = 0;
    private double total = 0.0;

    @Ignore
    private ArrayList<InvoiceLine> list = new ArrayList<>();


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


    public ArrayList<InvoiceLine> getList() {
        return list;
    }

    public void setList(ArrayList<InvoiceLine> list) {
        this.list = list;
    }
}
