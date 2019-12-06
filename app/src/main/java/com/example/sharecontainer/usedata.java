package com.example.sharecontainer;

import android.app.Application;
import android.content.Context;

import com.xuexiang.xui.XUI;

import cn.jpush.android.api.JPushInterface;

/**
 * 存放用户数据类
 */
public class usedata extends Application {
    private   String img;
    private  String id;
    private  String name;
    private  String sex;
    private  String bir;
    private  String password;
    private  String Dindan;

    private static Context mContext;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBir() {
        return bir;
    }

    public void setBir(String bir) {
        this.bir = bir;
    }

    public String getDindan() {
        return Dindan;
    }

    public void setDindan(String dindan) {
        Dindan = dindan;
    }



    public void onCreate() {
        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        JPushInterface.setDebugMode(true);//开启极光推送loggcat
        JPushInterface.init(this);// 加载
        mContext = getApplicationContext();
    }
}
