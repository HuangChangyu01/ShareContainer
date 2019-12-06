package com.example.sharecontainer.AdapterClass;

public class Info {
    private  int img;
    private  String name;
    private  String nub;
    private  String money;


    public Info(int img, String name, String nub, String money) {
        this.img = img;
        this.name = name;
        this.nub = nub;
        this.money = money;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNub() {
        return nub;
    }

    public void setNub(String nub) {
        this.nub = nub;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
