package com.example.sharecontainer;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.sharecontainer.AdapterClass.Info;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.service.JPushMessageReceiver;


public class MyReceiver extends JPushMessageReceiver {
    private static Handler uhandler;
    private Info info;
    private List<Info> list=new ArrayList<>();
    public static void  handlerMessage(Handler handler){
        MyReceiver.uhandler=handler;
    }
    /**
     * 第一次安装注册的极光码
     * @param context
     * @param s
     */
    public void onRegister(Context context, String s) {
        super.onRegister(context, s);
        Log.d("jgtscheshi01", "ID码: "+s);

    }
    /**
     * 接收推送的消息
     * @param context
     * @param customMessage
     */
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        String data = customMessage.message;
        Log.d("jgtscheshi01", "推送收到的信息: " + customMessage.message);
        Info[] in=new Info[20];
        if(data!=null){
            if(data.equals("订单完成")){
                uhandler.obtainMessage(2,"金额").sendToTarget();
            }
           else if(data.equals("余额不足")){
                uhandler.obtainMessage(3,"金额").sendToTarget();
            }
           else if(data.equals("空")){
                uhandler.obtainMessage(1,"").sendToTarget();
            }
           else if(data.equals("您没有购物")){
                uhandler.obtainMessage(0,"您尚未购物").sendToTarget();
            }
           else if(data.equals("等待开门超时")){
                uhandler.obtainMessage(0,"等待开门超时").sendToTarget();
            }
           else{
                String[] datali=data.split("-");
                String zong=datali[datali.length-2];// 总额
//             String flag=datali[datali.length-1];// 是否关门结账了 关门=2
                for(int i=0;i<datali.length-2;i++){
                    String[] li2=datali[i].split(",");
                    String id=li2[0].substring(1);// 商品序列号
                    String name=li2[1];//商品名字
                    String page=li2[2];//价格
                    String img=li2[3].substring(0,li2[3].length()-1); //图片
                    info=new Info(R.mipmap.kele,name,id,page);
                    in[i]=info;
                }
                uhandler.obtainMessage(123456,in).sendToTarget();

            }
        }

    }

}
