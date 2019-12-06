package com.example.sharecontainer.User.Indent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharecontainer.R;
import com.example.sharecontainer.User.Charge.TopUpRecharge;
import com.example.sharecontainer.usedata;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyPayInputDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.progress.loading.ARCLoadingView;

import org.w3c.dom.Text;

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

public class WorderHistory extends Fragment {
    private Boolean isVisible;

    private View view;
    private TextView w_money;
    private Button w_pay;
    private ListView w_lv1;
    private OkHttpClient httpClient=new OkHttpClient();
    private List<Map<String,Object>> data=new ArrayList<Map<String, Object>>();
    private SimpleAdapter simpleAdapter;
    private  Map<String,Object> map;
    private String count;//总额
    private String Dingdangid; //订单号
    private ARCLoadingView af;
    private Button my; //获取父页面的my控件
    private Button sao;//获取父页面的sao控件
    private Handler whandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 123:
                    String dan=msg.obj.toString();
                    String[] shujuli=dan.split("[+]");
                    for(int i=2;i<shujuli.length-2;i++){
                        String aa=shujuli[i].substring(1,shujuli[i].length()-1);
                        String[] aali=aa.split(",");
                        map=new HashMap<String,Object>();
                        map.put("name",aali[1]);
                        map.put("id","ID:"+aali[0]);
                        map.put("pay",aali[2]);
                        data.add(map);
                   }
                   count=shujuli[1];
                   Dingdangid=shujuli[0];
                   w_money.setText("￥"+shujuli[1]);
                   simpleAdapter.notifyDataSetChanged();
                   af.stop();
                   my.setEnabled(true);
                   sao.setEnabled(true);
                   w_pay.setEnabled(true);//付款按钮 可点击
                   break;
               case 1234:
                   Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                   af.stop();
                   my.setEnabled(true);
                   sao.setEnabled(true);
                   break;
               case 12345:
                   Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                   af.stop();
                   my.setEnabled(true);
                   sao.setEnabled(true);
                   break;
           }
        }
    };
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.w_orderghistory, container, false);
        //清空数据表中的数据
        data.clear();
        //在未加载完毕请求时，不得点击其他页面
        my=getActivity().findViewById(R.id.u_in_my);
        sao=getActivity().findViewById(R.id.u_in_sao);
        my.setEnabled(false);
        sao.setEnabled(false);

        af=view.findViewById(R.id.abcd);
        w_money=view.findViewById(R.id.w_money);
        w_pay=view.findViewById(R.id.pay);
        w_lv1=view.findViewById(R.id.w_lv1);
        af.start();
        w_money.setText("￥0.00");
        simpleAdapter=new SimpleAdapter(getActivity(),data, R.layout.item,new String[]{"name","id","pay"},
                new int[]{R.id.i_tv1,R.id.i_tv2, R.id.i_tv3});
        w_lv1.setAdapter(simpleAdapter);
        dingdangSelect();//查询订单方法

        w_pay.setEnabled(false);

        w_pay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final MyPayInputDialog Dialog = new MyPayInputDialog(getActivity()).Builder();
                Dialog.setResultListener(new MyPayInputDialog.ResultListener() {
                    public void onResult(String result) {
                        http(result);
                        Dialog.dismiss();
                    }
                }).setTitle("支付");
                Dialog.show();
            }
        });
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
                .url("http://47.95.234.172:8080/test/ReturnServlet").tag(getActivity()).build();
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
                Log.d("cheshi04", "onResponse: "+str);
                if(str.equals("无订单")){
                    Message m=new Message();
                    m.what=1234;
                    m.obj="无待支付订单";
                    whandler.sendMessage(m);
                }
                else{
                    boolean flag=true;
                    String[] strli=str.split("-");//每条订单集合(包含未付订单)
                    for(int i=0;i<strli.length;i++) {
                        String dan=strli[i].substring(1,strli[i].length()-1);
                        System.out.println(dan); // 这人的一条订单
                        String[] danli=dan.split("[+]"); //订单详情
                        if(danli[danli.length-2].equals("0")){ //查询未付订单
                            Message m=new Message();
                            m.what=123;
                            m.obj=dan;
                            whandler.sendMessage(m);
                            flag=false;
                        }
                    }
                    if(flag==true){
                        Message m=new Message();
                        m.what=12345;
                        m.obj="无待付订单";
                        whandler.sendMessage(m);
                    }
                }
            }
        });
    }

    /**
     * 请求网络 验证密码
     * @param a
     */
    public  void http(final String a){
        final usedata use=(usedata)getActivity().getApplication();
        FormBody formBody=new FormBody.Builder()
                .addEncoded("u_id",use.getId())
                .addEncoded("order_id",Dingdangid)
                .addEncoded("count",count)
                .addEncoded("paypassword",a).build();
        Request request=new Request.Builder().post(formBody)
                .url("http://47.95.234.172:8080/test/PayServlet").build();
        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure( Call call,  IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),"请求错误",Toast.LENGTH_LONG).show();
                            my.setEnabled(true);
                            sao.setEnabled(true);
                        }
                    });
            }
            public void onResponse( Call call,  Response response) throws IOException {
                final  String aa=response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (aa.equals("支付成功")) {
                                use.setDindan("1");
                                data.clear();
                                w_money.setText("￥0.00");
                                simpleAdapter.notifyDataSetChanged();
                                new MaterialDialog.Builder(getContext())
                                        .iconRes(R.drawable.icon_tip)
                                        .title("提示")
                                        .content("支付成功")
                                        .positiveText("确定")
                                        .show();
                                w_pay.setEnabled(false); //支付成功后按钮不可点击
                            } else if (aa.equals("余额不足")) {
                                new MaterialDialog.Builder(getContext())
                                        .iconRes(R.drawable.icon_warning)
                                        .title("对不起")
                                        .content("您当前余额不足，是否充值")
                                        .positiveText("是").onPositive(new MaterialDialog.SingleButtonCallback() {
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        Intent in = new Intent(getActivity(), TopUpRecharge.class);
                                        startActivity(in);
                                    }
                                })
                                        .negativeText("否")
                                        .show();
                            } else {
                                Toast.makeText(getActivity(), aa, Toast.LENGTH_SHORT).show();
                            }
                            my.setEnabled(true);
                            sao.setEnabled(true);
                        }
                    });
            }
        });
    }

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
