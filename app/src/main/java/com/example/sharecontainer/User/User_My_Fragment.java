package com.example.sharecontainer.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharecontainer.R;
import com.example.sharecontainer.User.Charge.TopUpRecharge;
import com.example.sharecontainer.User.SetPassword.Frist_Setpaypwd;
import com.example.sharecontainer.usedata;
import com.hb.dialog.dialog.ConfirmDialog;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class User_My_Fragment extends Fragment implements View.OnClickListener {
    private CircleImageView mymessage;
    private Button mymessage2;
    private Button my_shezhi;
    private Button my_chong;
    private TitleBar titleBar;
    private TextView my_name;
    private View view;
    private TextView money;
    private String money1;
    private String mess = null; // 保存查询结果  id，支付密码，余额，扣款协议 0：不同意  1：同意
    private OkHttpClient httpClient;
    private Handler hhandler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    money.setText("￥ "+msg.obj.toString());
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_myfragment, container, false);

        //获取控件
        mymessage=view.findViewById(R.id.my_imgb1);
        mymessage2=view.findViewById(R.id.my_message);
        my_name=view.findViewById(R.id.my_name);
        my_shezhi=view.findViewById(R.id.my_shezhi);
        money=view.findViewById(R.id.money);
        my_chong=view.findViewById(R.id.my_chong);
        titleBar=view.findViewById(R.id.titlebar);
        titleBar.disableLeftView();
        // 添加监听
        mymessage.setOnClickListener(this);
        mymessage2.setOnClickListener(this);
        my_shezhi.setOnClickListener(this);
        my_chong.setOnClickListener(this);

//        Bundle bundle =this.getArguments();//得到从Activity传来的数据
//        if(bundle!=null){
//            mess = bundle.getString("content");
//        }
//        String[] arr=mess.split(",");
//        usedata use=(usedata)getActivity().getApplication();
//        my_name.setText(use.getName());//设置名称
//        mymessage2.setText(use.getId());//设置id

        return view;

    }



    /**
     * 当加载页面或从别的Activity页面返回时需加载的Activity生命周期方法 初始化
     */

    public void onResume() {
        super.onResume();
        usedata use=(usedata)getActivity().getApplication();
        // 调用余额查询方法
        money();
        my_name.setText(use.getName());//设置名称
        mymessage2.setText(" UID:"+use.getId());//设置ID
    }

    /**
     * 点击事件方法
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()){
            // 点击头像跳转信息修改页面
            case R.id.my_imgb1:
                Intent in=new Intent(getActivity(),My_Fragment_Message.class);
                in.putExtra("content",mess);
                startActivity(in);
                break;
            // 点击id跳转信息修改页面
            case R.id.my_message:
                Intent in2=new Intent(getActivity(),My_Fragment_Message.class);
                in2.putExtra("content",mess);
                startActivity(in2);
                break;
            case R.id.my_shezhi:
                Intent in3=new Intent(getActivity(),My_Fragment_Set.class);//跳转设置页面
                startActivity(in3);
                break;
            //充值
            case R.id.my_chong:
                String[] arr=mess.split(",");
                if(arr[1].equals("null")){
                    ConfirmDialog showDialog = new ConfirmDialog(getActivity());
                    showDialog .setLogoImg(R.mipmap.dialog_notice).setMsg("您尚未开启支付服务，是否设置支付密码");
                    showDialog .setClickListener(new ConfirmDialog.OnBtnClickListener() {
                        public void ok() {
                            //点击是跳转到设置支付密码界面
                            Intent in=new Intent(getActivity(), Frist_Setpaypwd.class);
                            startActivity(in);
                        }
                        public void cancel() {
                        }
                    });
                    showDialog .show();
                }
                else{
                    //跳转到充值页面
                    Intent in5=new Intent(getActivity(), TopUpRecharge.class);
                    startActivity(in5);
                }
                break;
        }
    }



    /**
     * 查询用户支付信息，并将用户余额更新到UI
     */
    public  void  money(){
        usedata use=(usedata)getActivity().getApplication();
        httpClient=new OkHttpClient();
        FormBody formBody=new FormBody.Builder().addEncoded("type","u_account")
                .addEncoded("u_id",use.getId()).build();
        Request request=new Request.Builder().post(formBody)
                .url("http://47.95.234.172:8080/test/ReturnServlet").build();
        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure( Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),"请求错误",Toast.LENGTH_LONG).show();
                    }
                });
            }
            public void onResponse( Call call,  Response response) throws IOException {
                // 返回的是  id，支付密码，余额，扣款协议 0：不同意  1：同意
                final String  aa=response.body().string();
                mess=aa;
                final String[] arr=aa.split(",");
                Message ms=new Message();
                ms.what=1;
                ms.obj=arr[2];
                hhandler.sendMessage(ms);
            }
        });
    }

//    public void onDestroy() {
//        super.onDestroy();
//        cancelAll(httpClient);
//    }
//
//    public static void cancelAll(OkHttpClient client) {
//        if (client == null) return;
//        for (Call call : client.dispatcher().queuedCalls()) {
//            call.cancel();
//        }
//        for (Call call : client.dispatcher().runningCalls()) {
//            call.cancel();
//        }
//    }

}

