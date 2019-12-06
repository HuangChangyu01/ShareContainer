package com.example.sharecontainer.User.SetPassword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sharecontainer.R;
import com.example.sharecontainer.usedata;
import com.hb.dialog.dialog.ConfirmDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Frist_Setpaypwd extends AppCompatActivity implements View.OnClickListener {
    private EditText first_pass;
    private EditText first_pass2;
    private Button first_buton;
    private ImageButton first_fanhui;
    private CheckBox check;
    private OkHttpClient httpClient=new OkHttpClient();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist__setpaypwd);

        first_pass=findViewById(R.id.first_pass);
        first_pass2=findViewById(R.id.first_pass2);
        first_buton=findViewById(R.id.first_buton);
        first_fanhui=findViewById(R.id.first_fanhui);
        check=findViewById(R.id.check);

        first_buton.setOnClickListener(this);
        first_fanhui.setOnClickListener(this);

        first_pass.setFocusable(true);//初始化第一行密码控件可编辑
        first_pass2.setFocusable(false);// 第二行不可编辑
        // 对first_pass输入框监听
        first_pass.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Editable aa = first_pass.getText();
                if(aa.length()==6){
                    first_pass2.setFocusable(true);
                    first_pass2.setFocusableInTouchMode(true);
                    first_pass2.requestFocus();
                }
                else
                {
                    first_pass2.setFocusable(false);
                }
            }
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    /**
     * 点击事件
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.first_buton:
                http();//
                break;
            case R.id.first_fanhui:
                Frist_Setpaypwd.this.finish();
                break;
        }

    }

    /**
     * 请求网络
     */
    public  void http(){
        usedata use=(usedata)getApplication();
        String paypass1= first_pass.getText().toString().trim();
        String paypass2=first_pass2.getText().toString().trim();
        if(paypass1.equals("")||paypass2.equals("")){
            Toast.makeText(Frist_Setpaypwd.this,"输入不可为空",Toast.LENGTH_SHORT).show();
        }
        else if(paypass1.equals(paypass2)==false){
            Toast.makeText(Frist_Setpaypwd.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
            first_pass.setText("");
            first_pass2.setText("");
            first_pass.requestFocus();
        }
        else if(!check.isChecked()){
            ConfirmDialog showDialog = new ConfirmDialog(this);
            showDialog.setLogoImg(R.mipmap.dialog_notice).setMsg("您需要同意自动扣款协议才可使用本支付服务");
            showDialog.setClickListener(new ConfirmDialog.OnBtnClickListener() {
                public void ok() {
                }
                public void cancel() {

                }
            });
            showDialog.show();
        }
        else{
            FormBody formBody=new FormBody.Builder().addEncoded("type","first_paypassword")
                    .addEncoded("u_id",use.getId())
                    .addEncoded("u_paypassword",paypass1).build();
            final Request request=new Request.Builder().post(formBody)
                    .url("http://47.95.234.172:8080/test/SetServlet").build();
            httpClient.newCall(request).enqueue(new Callback() {
                public void onFailure( Call call,  IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Frist_Setpaypwd.this,"请求错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                public void onResponse( Call call,  Response response) throws IOException {
                    final  String aa=response.body().string();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(aa.equals("支付密码修改成功")){
                                Toast.makeText(Frist_Setpaypwd.this,"支付密码设置成功",Toast.LENGTH_LONG).show();
                                Frist_Setpaypwd.this.finish();
                            }
                            else{
                                Toast.makeText(Frist_Setpaypwd.this,"设置失败",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }

    }
}
