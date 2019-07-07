package com.example.hawi.medmarket;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Hawi on 30/01/18.
 */

public class items {

    private String name,desc,qtyUnit,img ;
    private int qty ,  isFav ,isShop , id;
    private double price;


    public items(){

    }

    public items(String name, int isFav) {
        this.name = name;
        this.isFav = isFav;
    }

    public items(String name, String desc, String qtyUnit, String img, int qty, int isFav, int isShop, int id, double price) {
        this.name = name;
        this.desc = desc;
        this.qtyUnit = qtyUnit;
        this.img = img;
        this.qty = qty;
        this.isFav = isFav;
        this.isShop = isShop;
        this.id = id;
        this.price = price;
    }

    public items(String name, String desc, String qtyUnit, String img, int isFav, int isShop, int id, double price) {
        this.name = name;
        this.desc = desc;
        this.qtyUnit = qtyUnit;
        this.img = img;
        this.isFav = isFav;
        this.isShop = isShop;
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIsShop() {
        return isShop;
    }

    public void setIsShop(int isShop) {
        this.isShop = isShop;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }
}
