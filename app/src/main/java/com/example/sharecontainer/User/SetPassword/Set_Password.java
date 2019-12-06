package com.example.sharecontainer.User.SetPassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sharecontainer.R;
import com.example.sharecontainer.Tool.ExitActivity;
import com.example.sharecontainer.User.My_Fragment_Set;
import com.example.sharecontainer.User.User_index;
import com.example.sharecontainer.usedata;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Set_Password extends AppCompatActivity implements View.OnClickListener {
    private Button setpass_b1;
    private ImageButton set_fanhui;
    private EditText oldpass;
    private EditText newpass1;
    private EditText newpass2;
    private OkHttpClient httpClient;
//    private ImageView setpass_img1;
//    private ImageView setpass_img2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__password);
        ExitActivity.getInstance().addActivity(this);//加入关闭类的list中

        setpass_b1=findViewById(R.id.setpass_b1);
        set_fanhui=findViewById(R.id.set_fanhui);
        oldpass=findViewById(R.id.oldpass);
        newpass1=findViewById(R.id.newpass1);
        newpass2=findViewById(R.id.newpass2);

        set_fanhui.setOnClickListener(this);
        setpass_b1.setOnClickListener(this);
//        setpass_img1=findViewById(R.id.setpass_img1);
//        setpass_img2=findViewById(R.id.setpass_img2);

//        setpass_img1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // 触碰时
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.kang));//换背景图
//                    odd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
//                }
//                //松手恢复原来图片
//                else if(event.getAction() == MotionEvent.ACTION_UP)
//                {
//                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.chapasa));
//                    mima.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码
//
//                }
//                return false;
//            }
//        });

    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_fanhui:
                Set_Password.this.finish();
                break;
            case R.id.setpass_b1:
                http();
                break;
        }
    }

    /**
     * 请求服务器
     */
    public void http(){
        httpClient= new OkHttpClient();
        final usedata use=(usedata)getApplication();
        final String oldpaaword=oldpass.getText().toString().trim();
        final String newpassword=newpass1.getText().toString().trim();
        final String newpassword1=newpass2.getText().toString().trim();
        if(oldpaaword.equals("")){
            Toast.makeText(Set_Password.this,"旧密码不可为空", Toast.LENGTH_SHORT).show();
            oldpass.requestFocus();
        }
        else if(newpassword.equals("")||newpassword1.equals("")){
            Toast.makeText(Set_Password.this,"新密码不可为空", Toast.LENGTH_SHORT).show();
            newpass1.requestFocus();
        }
        else if(newpassword.equals(newpassword1)==false){
            Toast.makeText(Set_Password.this,"两次密码不一致", Toast.LENGTH_SHORT).show();
            newpass1.setText("");
            newpass2.setText("");
            newpass1.requestFocus();
        }
        else if(oldpaaword.equals(use.getPassword())==false){
            Toast.makeText(Set_Password.this,"旧密码错误", Toast.LENGTH_SHORT).show();
            oldpass.setText("");
            newpass1.setText("");
            newpass2.setText("");
            oldpass.requestFocus();

        }
        else{
            FormBody formBody=new FormBody.Builder().addEncoded("type","password")
                    .addEncoded("u_id",use.getId())
                    .addEncoded("u_password",newpassword).build();
            Request request=new Request.Builder().post(formBody)
                    .url("http://47.95.234.172:8080/test/SetServlet").build();
            httpClient.newCall(request).enqueue(new Callback() {
                public void onFailure( Call call,  IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Set_Password.this,"请求错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                public void onResponse(Call call,  Response response) throws IOException {
                    final  String aa=response.body().string();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(aa.equals("密码修改成功")){
                                Toast.makeText(Set_Password.this,aa,Toast.LENGTH_SHORT).show();
                                use.setPassword(newpassword);
                                Set_Password.this.finish();
                                My_Fragment_Set.instance.finish();
                            }
                            else{
                                Toast.makeText(Set_Password.this,aa,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
        }
    }
}
