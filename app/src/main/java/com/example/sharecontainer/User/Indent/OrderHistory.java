package com.example.sharecontainer.User.Indent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharecontainer.R;
import com.example.sharecontainer.User.User_index;
import com.example.sharecontainer.usedata;
import com.xuexiang.xui.widget.progress.loading.ARCLoadingView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderHistory extends Fragment {
    private boolean isVisible;
    private View view;
    private ListView list;
    private List<Map<String,Object>> data=new ArrayList<Map<String, Object>>();;
    private SimpleAdapter simpleAdapter;
    private  Map<String,Object> map;
    private OkHttpClient httpClient=new OkHttpClient();
    private String[] dingdang;
    private ARCLoadingView af;//对话宽
    private Button my; //获取父页面的my控件
    private Button sao;//获取父页面的sao控件

    private Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 123:
                    String dan=msg.obj.toString();
                    Log.d("cheshi03", "onResponse: "+dan);
                    String[] danli=dan.split("[+]");
                    map=new  HashMap<String,Object>();
                    map.put("time",danli[danli.length-1]);
                    map.put("pay",danli[1]);
                    data.add(map);
                    simpleAdapter.notifyDataSetChanged();
                    af.stop();
                    my.setEnabled(true);
                    sao.setEnabled(true);
                    break;
                case 1234:
                    Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    af.stop();
                    my.setEnabled(true);
                    sao.setEnabled(true);
                    break;
            }
        }
    };


    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.orderhistory, container, false);
        list=view.findViewById(R.id.list);
        my=getActivity().findViewById(R.id.u_in_my);
        sao=getActivity().findViewById(R.id.u_in_sao);
        my.setEnabled(false);
        sao.setEnabled(false);


        data.clear();
        af=view.findViewById(R.id.abcd);
        af.start();


        simpleAdapter=new SimpleAdapter(getActivity(),data,R.layout.item2,new String[]{"time","pay"},
                new int[]{R.id.i2_time,R.id.i_tv3});
        list.setAdapter(simpleAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text1=view.findViewById(R.id.i2_time);
                String dd=text1.getText().toString();
                for(int j=0;j<dingdang.length;j++) {
                    String dan=dingdang[i].substring(1,dingdang[i].length()-1);// 这人的一条订单
                    String[] danli=dan.split("[+]");//该条订单详情
                    if(dd.equals(danli[danli.length-1])){
                        Intent in=new Intent(getActivity(),Detailedorders.class);
                        in.putExtra("Dingdang",dan);
                        startActivity(in);
                        Toast.makeText(getActivity(), text1.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }


            }
        });
        dingdangSelect();
        return  view;
    }

    /**
     * 订单查询
     */
    public  void  dingdangSelect(){
        usedata use=(usedata)getActivity().getApplication();
        FormBody formBody=new FormBody.Builder().addEncoded("type","bussiness")
                .addEncoded("u_id",use.getId()).build();
        Request request=new Request.Builder().post(formBody)
                .url("http://47.95.234.172:8080/test/ReturnServlet").build();
        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),"请求错误",Toast.LENGTH_LONG).show();
                        my.setEnabled(true);
                        sao.setEnabled(true);
                    }
                });

            }
            public void onResponse( Call call,  Response response) throws IOException {
                final String  str=response.body().string();
              //  String  str="{1/2019.11.24 13:41:20+43.0+<6B050E1B,可乐,21.0>+<7BEC971B,汉堡,22.0>+1+2019.11.24 13:41:20}-{1/2019.11.24 13:42:07+44.0+<6B050E1B,可乐,21.0>+<CB32AB1B,薯条,23.0>+1+2019.11.24 13:42:07}-{1/2019.11.24 13:42:08+44.0+<6B050E1B,可乐,21.0>+<CB32AB1B,薯条,23.0>+0+2019.11.24 13:42:08}-";
                Log.d("cheshi04", "onResponse: "+str);
                if(str!=null){
                    if(str.equals("无订单")){
                        Message m=new Message();
                        m.what=1234;
                        m.obj="暂无订单";
                        handler.sendMessage(m);
                    }
                    else{
                        String[] strli=str.split("-");//每条订单集合(包含未付订单)
                        dingdang=strli;
                        boolean flag=true;
                        for(int i=0;i<strli.length;i++) {
                            String dan=strli[i].substring(1,strli[i].length()-1);
                            System.out.println(dan); // 这人的一条订单
                            String[] danli=dan.split("[+]"); //订单详情
                            if(danli[danli.length-2].equals("1")){ //查询已付订单
                                Message m=new Message();
                                m.what=123;
                                m.obj=dan;
                                handler.sendMessage(m);
                                flag=false;
                            }
                        }
                        if(flag){
                            Message m=new Message();
                            m.what=1234;
                            m.obj="暂无完成订单";
                            handler.sendMessage(m);
                        }
                    }
                }

            }
        });
    }



//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if(hidden){
//            isVisible=false;
//            cancelAll(httpClient);
//        }else {
//            isVisible=true;
//        }
//    }
//    public static void cancelAll(OkHttpClient client) {
//        if (client == null) return;
//        for (Call call : client.dispatcher().queuedCalls()) {
//            call.cancel();
//        }
//        for (Call call : client.dispatcher().runningCalls()) {
//            call.cancel();
//        }
//    }


    //    public static void cancelTag(OkHttpClient client, Object tag) {
//        if (client == null || tag == null) return;
//        for (Call call : client.dispatcher().queuedCalls()) {
//            if (tag.equals(call.request().tag())) {
//                call.cancel();
//            }
//        }
//        for (Call call : client.dispatcher().runningCalls()) {
//            if (tag.equals(call.request().tag())) {
//                call.cancel();
//            }
//        }
//    }







}
