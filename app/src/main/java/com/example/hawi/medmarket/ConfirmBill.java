package com.example.hawi.medmarket;

/**
 * Created by Hawi on 20/02/18.
 */

public class ConfirmBill {
    private int id,qty;
    private String item_name;
    private Double price;

    public ConfirmBill() {
    }

    public ConfirmBill(int id, int qty) {
        this.id = id;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
