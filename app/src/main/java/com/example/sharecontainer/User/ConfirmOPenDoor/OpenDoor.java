package com.example.sharecontainer.User.ConfirmOPenDoor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sharecontainer.R;
import com.example.sharecontainer.User.Indent.ShoppingIndent;
import com.example.sharecontainer.usedata;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenDoor extends AppCompatActivity {
//    private TextView aa;
    private Button opendoor;
    private String d_token;
    private String u_token;
    private static OkHttpClient httpClient=new OkHttpClient();

    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_door);
        // 获取二维码数据--设备信鸽注册码
        Intent intent2=getIntent();
        d_token=intent2.getStringExtra("facilityRegister").substring(6);
        //获取客户端信鸽注册码
        String registrationID= JPushInterface.getRegistrationID(this);
        u_token=registrationID;
//        SharedPreferences sp=this.getSharedPreferences("Register", OpenDoor.MODE_PRIVATE);
//        u_token=sp.getString("RegisterId",null);




//        aa.setText(d_token);

        opendoor=findViewById(R.id.opendoor);
        opendoor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    // do something
                    mLastClickTime = nowTime;
                    openDoorHttp(); //请求服务，发送开门指令
                }

            }
        });
    }
    /**
     * 请求服务，发送开门指令
     */
    public  void openDoorHttp(){
        Toast.makeText(OpenDoor.this,"点击到了",Toast.LENGTH_SHORT).show();
        usedata data=(usedata)getApplication();
        FormBody formBody=new FormBody.Builder().addEncoded("u_id",data.getId())
                .addEncoded("option","open")
                .addEncoded("d_token",d_token)
                .addEncoded("u_token",u_token)
                .build();
        Request request=new Request.Builder().post(formBody)
                .url("http://47.95.234.172:8080/test/OpenDoorServlet").build();
        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                Log.d("Cheshi01", "收到消息：" +"没有收到");
            }
            public void onResponse( Call call, Response response) throws IOException {
                String abc=response.body().string();
                Log.d("Cheshi01", "设备注册码：" +abc);
                Log.d("Cheshi01", "客户端本身注册码：" +u_token);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Intent intent=new Intent(OpenDoor.this, ShoppingIndent.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
