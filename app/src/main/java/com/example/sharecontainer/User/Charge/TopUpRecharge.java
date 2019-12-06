package com.example.sharecontainer.User.Charge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.sharecontainer.R;
import com.example.sharecontainer.usedata;
import com.hb.dialog.myDialog.MyPayInputDialog;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopUpRecharge extends AppCompatActivity {
    private EditText top_money;
    private Button top_buton;
    private ImageButton top_fanhui;
    private String Paypwd;
    private OkHttpClient httpClient=new OkHttpClient();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_recharge);

        top_money=findViewById(R.id.top_money);
        top_buton=findViewById(R.id.top_buton);
        top_fanhui=findViewById(R.id.top_fanhui);
        top_money.requestFocus();

        top_buton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //弹出输入密码框 依赖
                final MyPayInputDialog Dialog = new MyPayInputDialog(TopUpRecharge.this).Builder();
                if(top_money.getText().toString().equals("") || top_money.getText().toString().equals("0")){
                    Toast.makeText(TopUpRecharge.this,"请输入正确金额",Toast.LENGTH_SHORT).show();
                }
                else{
                    Dialog.setResultListener(new MyPayInputDialog.ResultListener() {
                        public void onResult(String result) {
                            Paypwd=result;
                            http(Paypwd);
                            Dialog.dismiss();
                        }
                    }).setTitle("支付");
                    Dialog.show();
                }


            }
        });
        top_fanhui.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 请求网络 验证密码
     * @param a
     */
    public  void http(String a){
        usedata use=(usedata)getApplication();
        FormBody formBody=new FormBody.Builder()
                .addEncoded("type","value")
                .addEncoded("u_id",use.getId())
                .addEncoded("value",top_money.getText().toString())
                .addEncoded("u_paypassword",a)
                .addEncoded("set_type","add").build();
        Request request=new Request.Builder().post(formBody)
                .url("http://47.95.234.172:8080/test/SetServlet").build();
        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure( Call call,  IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(TopUpRecharge.this,"请求错误",Toast.LENGTH_LONG).show();
                    }
                });
            }
            public void onResponse( Call call,  Response response) throws IOException {
                final  String aa=response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(aa.equals("充值余额成功"))
                        {
                            TopUpRecharge.this.finish();
                            Toast.makeText(TopUpRecharge.this,aa,Toast.LENGTH_LONG).show();
                        }
                        else if(aa.equals("支付密码错误")){
                            Toast.makeText(TopUpRecharge.this,aa,Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(TopUpRecharge.this,aa,Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
    }
}
