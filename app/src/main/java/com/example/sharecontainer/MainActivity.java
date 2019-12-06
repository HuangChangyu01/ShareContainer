package com.example.sharecontainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sharecontainer.Tool.ExitActivity;
import com.example.sharecontainer.User.User_index;
import com.hb.dialog.dialog.LoadingDialog;
import com.xuexiang.xui.widget.button.ButtonView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText user;
    private EditText password;
    private ButtonView dengru;
    private ButtonView zhuche;
    private Button wangji;
    private CheckBox rember;
    private static OkHttpClient httpClient;
    private SharedPreferences sp;// 存储密码
    private SharedPreferences sp2;// 存储信鸽设备注册码
    private LoadingDialog loadingDialog; // 加载弹窗窗口
    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitActivity.getInstance().addActivity(this);//加入关闭类的list中

        user=findViewById(R.id.uesr);
        password=findViewById(R.id.password);
        dengru=findViewById(R.id.Denru);
        zhuche= findViewById(R.id.zhuche);
        wangji=findViewById(R.id.Wanji);
        rember=findViewById(R.id.jizhu);

        // 获取SharedPreferences对象
        sp=this.getSharedPreferences("info", MainActivity.MODE_PRIVATE);
        sp2=this.getSharedPreferences("Register",MainActivity.MODE_PRIVATE);
        //记住密码加载值
        user.setText(sp.getString("userName",null));
        password.setText(sp.getString("pwd",null));
        rember.setChecked(sp.getBoolean("ischecked",false));
        //点击注册按钮
        zhuche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    // do something
                    mLastClickTime = nowTime;
                    Intent in=new Intent(MainActivity.this,Register.class);
                    startActivity(in);
                } else {
                    Toast.makeText(MainActivity.this, "不要重复点击", Toast.LENGTH_SHORT).show();
                }

            }
        });

        wangji.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Retrievepassword.class);
                startActivity(intent);
            }
        });
        httpClient=new OkHttpClient();
        Login();
        //生成注册码方法 --必须保持运行
//        makeRegister();
    }

    /**
     * 登入方法
     */
    private String id;//账号
    private String pass;//密码
    public void Login(){
        dengru.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    // do something
                    mLastClickTime = nowTime;
                    id=user.getText().toString().trim();
                    pass=password.getText().toString().trim();
                    if(id.equals("")){
                        Toast.makeText(MainActivity.this,"账号不可为空",Toast.LENGTH_LONG).show();
                    }
                    else if(pass.equals("")){
                        Toast.makeText(MainActivity.this,"密码不可为空",Toast.LENGTH_LONG).show();
                    }
                    else{
                        //进度加载弹窗窗口
                        loadingDialog = new LoadingDialog(MainActivity.this);
                        loadingDialog.setMessage("loading");
                        loadingDialog.show();

                        FormBody formBody=new FormBody.Builder().addEncoded("user",id)
                                .addEncoded("password",pass).build();
                        Request request=new Request.Builder().post(formBody)
                                .url("http://47.95.234.172:8080/test/LoginServlet").build();
                        httpClient.newCall(request).enqueue(new Callback() {
                            public void onFailure( Call call,  IOException e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        loadingDialog.dismiss();//关闭加载窗口
                                        Toast.makeText(MainActivity.this,"请求错误",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            public void onResponse( Call call,  Response response) throws IOException {
                                //关闭加载窗口
                                loadingDialog.dismiss();
                                final String aa=response.body().string();
                                final String[] arr = aa.split(",");
                                // 回归主线程，在主线程进行UI操作
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if(arr[0].equals("登录成功"))
                                        {
                                            SharedPreferences.Editor editor=sp.edit();
                                            MainActivity.this.finish();//退出
                                            if(rember.isChecked()){
                                                editor.putString("userName",id);
                                                editor.putString("pwd",pass);
                                                editor.putBoolean("ischecked",true);
                                                editor.commit();
                                            } else {
                                                editor.putString("userName","");
                                                editor.putString("pwd","");
                                                editor.putBoolean("ischecked",false);
                                                editor.commit();
                                            }

                                            Intent in=new Intent(MainActivity.this, User_index.class);
                                            usedata data=(usedata)getApplication();
                                            data.setImg(arr[5]);
                                            data.setId(arr[1]);
                                            data.setName(arr[2]);
                                            data.setSex(arr[3]);
                                            data.setBir(arr[4]);
                                            data.setDindan(arr[6]);
                                            data.setPassword(pass);
                                            startActivity(in);
                                        } else{
                                            Toast.makeText(MainActivity.this,aa,Toast.LENGTH_LONG).show();
                                            password.setText("");
                                            user.requestFocus();
                                        }
                                    }
                                });
                            }
                        });
                    }
                } else {
                    Toast.makeText(MainActivity.this, "不要重复点击", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 注册信鸽注册码 存入SharedPreferences  name=Register
     */
//    public void makeRegister(){
//        XGPushConfig.enableDebug(MainActivity.this,true);
//        XGPushManager.registerPush(MainActivity.this, new XGIOperateCallback() {
//            public void onSuccess(Object data, int flag) {
//                Log.d("TPush", "注册成功，设备token为：" + data);
//                SharedPreferences.Editor editor=sp2.edit();
//                editor.putString("RegisterId",data.toString());
//                editor.commit();
//            }
//            public void onFail(Object data, int errCode, String msg) {
//                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
//            }
//        });
//    }

}
