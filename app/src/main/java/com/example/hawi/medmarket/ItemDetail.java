package com.example.hawi.medmarket;

/**
 * Created by Hawi on 18/02/18.
 */

public class ItemDetail {
    private String name,desc,qtyUnit,img ;
    private int qty ,  isFav ,isShop , id;
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(String qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public int getIsShop() {
        return isShop;
    }

    public void setIsShop(int isShop) {
        this.isShop = isShop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
