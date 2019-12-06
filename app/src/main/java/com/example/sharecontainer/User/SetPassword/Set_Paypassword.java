package com.example.sharecontainer.User.SetPassword;

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
import com.example.sharecontainer.usedata;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Set_Paypassword extends AppCompatActivity implements View.OnClickListener {
    private Button paypass_b1;
    private ImageButton pay_fanhui;
    private EditText pay_oldpass;
    private EditText pay_newpass1;
    private EditText pay_newpass2;
    private OkHttpClient  httpClient= new OkHttpClient();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__paypassword);
        ExitActivity.getInstance().addActivity(this);//加入关闭类的list中

        paypass_b1=findViewById(R.id.paypass_b1);
        pay_fanhui=findViewById(R.id.pay_fanhui);
        pay_oldpass=findViewById(R.id.pay_oldpass);
        pay_newpass1=findViewById(R.id.pay_newpass1);
        pay_newpass2=findViewById(R.id.pay_newpass2);

        pay_fanhui.setOnClickListener(this);
        paypass_b1.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pay_fanhui:
                Set_Paypassword.this.finish();
                break;
            case R.id.paypass_b1:
                http();
                break;
        }
    }

    /**
     * 请求服务
     */
    public void http(){
        usedata use=(usedata)getApplication();
        String payoldpwd=pay_oldpass.getText().toString().trim();
        String paynewpwd=pay_newpass1.getText().toString().trim();
        String paynewpwd1=pay_newpass2.getText().toString().trim();
        if(payoldpwd.equals("")){
            Toast.makeText(Set_Paypassword.this,"旧密码不可为空", Toast.LENGTH_SHORT).show();
            pay_oldpass.requestFocus();
        }
        else if(paynewpwd.equals("")||paynewpwd1.equals("")){
            Toast.makeText(Set_Paypassword.this,"新密码不可为空", Toast.LENGTH_SHORT).show();
            pay_newpass1.requestFocus();
        }
        else if(paynewpwd.equals(paynewpwd1)==false){
            Toast.makeText(Set_Paypassword.this,"两次密码不一致", Toast.LENGTH_SHORT).show();
            pay_newpass1.setText("");
            pay_newpass2.setText("");
            pay_newpass1.requestFocus();
        }
        else{

            FormBody formBody=new FormBody.Builder()
                    .addEncoded("type","paypassword")
                    .addEncoded("u_id",use.getId())
                    .addEncoded("u_old_paypassword",payoldpwd)
                    .addEncoded("u_paypassword",paynewpwd1).build();
            Request request=new Request.Builder().post(formBody)
                    .url("http://47.95.234.172:8080/test/SetServlet").build();
            httpClient.newCall(request).enqueue(new Callback() {
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Set_Paypassword.this,"请求错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                public void onResponse(Call call,  Response response) throws IOException {
                    final  String aa=response.body().string();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(aa.equals("支付密码修改成功")){
                                Toast.makeText(Set_Paypassword.this,aa,Toast.LENGTH_SHORT).show();
                                Set_Paypassword.this.finish();
                                My_Fragment_Set.instance.finish();
                            }
                            else if(aa.equals("修改失败:原密码输入错误"))
                            {
                                Toast.makeText(Set_Paypassword.this,aa,Toast.LENGTH_SHORT).show();
                                pay_oldpass.setText("");
                                pay_newpass1.setText("");
                                pay_newpass2.setText("");
                                pay_oldpass.requestFocus();
                            }
                            else{
                                Toast.makeText(Set_Paypassword.this,aa,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

}
