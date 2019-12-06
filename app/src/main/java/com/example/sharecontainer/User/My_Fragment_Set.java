package com.example.sharecontainer.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sharecontainer.MainActivity;
import com.example.sharecontainer.R;
import com.example.sharecontainer.Tool.ExitActivity;
import com.example.sharecontainer.User.SetPassword.Frist_Setpaypwd;
import com.example.sharecontainer.User.SetPassword.Set_Password;
import com.example.sharecontainer.User.SetPassword.Set_Paypassword;
import com.example.sharecontainer.usedata;
import com.hb.dialog.dialog.ConfirmDialog;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class My_Fragment_Set extends AppCompatActivity implements View.OnClickListener {
    private TitleBar titleBar;
    private Button password;
    private Button paypassword;
    private Button zhuxiao;
    private Button tuichu;
    private OkHttpClient httpClient=new OkHttpClient();
    public static My_Fragment_Set instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__fragment__set);

        ExitActivity.getInstance().addActivity(this);//加入关闭类的list中
        instance=this;
        //获取控件
        password=findViewById(R.id.set_pass);
        paypassword=findViewById(R.id.set_paypass);
        zhuxiao=findViewById(R.id.set_zhuxiao);
        tuichu=findViewById(R.id.set_tuichu);
        titleBar=findViewById(R.id.titlebar);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        //添加监听
        password.setOnClickListener(this);
        paypassword.setOnClickListener(this);
        zhuxiao.setOnClickListener(this);
        tuichu.setOnClickListener(this);
    }
    // 点击事件
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_pass: //修改登入密码
                Intent in2=new Intent(My_Fragment_Set.this, Set_Password.class);
                startActivity(in2);
                break;
            case R.id.set_paypass:
                selectPassword();// 设置支付密码
                break;
            case R.id.set_zhuxiao://回到主界面
                Intent in4=new Intent(My_Fragment_Set.this, MainActivity.class);
                in4.setFlags(in4.FLAG_ACTIVITY_CLEAR_TASK | in4.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in4);

                break;
            case R.id.set_tuichu:
                ExitActivity.getInstance().exit();//加载exti（）方法退出所有Activity
                break;
        }

    }

    /**
     * / 查询是设置支付密码 进行跳转
     */
    public  void  selectPassword(){
        usedata use=(usedata)getApplication();
        httpClient=new OkHttpClient();
        FormBody formBody=new FormBody.Builder().addEncoded("type","u_account")
                .addEncoded("u_id",use.getId()).build();
        Request request=new Request.Builder().post(formBody)
                .url("http://47.95.234.172:8080/test/ReturnServlet").build();
        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(My_Fragment_Set.this,"请求错误",Toast.LENGTH_LONG).show();
                    }
                });
            }
            public void onResponse( Call call,  Response response) throws IOException {
                // 返回的是  id，支付密码，余额，扣款协议 0：不同意  1：同意
                String aa=response.body().string();
                final String[] arr=aa.split(",");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(arr[1].equals("null") )
                        {
                            ConfirmDialog showDialog = new ConfirmDialog(My_Fragment_Set.this);
                            showDialog .setLogoImg(R.mipmap.dialog_notice).setMsg("您尚未开启支付服务，是否设置支付密码");
                            showDialog .setClickListener(new ConfirmDialog.OnBtnClickListener() {
                                public void ok() {
                                    Intent in=new Intent(My_Fragment_Set.this, Frist_Setpaypwd.class);
                                    startActivity(in);
                                }
                                public void cancel() {

                                }
                            });
                            showDialog .show();
                        }
                        else
                        {
                            //跳转改密码界面
                            Intent in3=new Intent(My_Fragment_Set.this, Set_Paypassword.class);
                            startActivity(in3);
                        }
                    }
                });


            }
        });
    }
}
