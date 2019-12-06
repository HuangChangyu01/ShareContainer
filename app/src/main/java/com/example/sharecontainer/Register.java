package com.example.sharecontainer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sharecontainer.Tool.ExitActivity;
import com.example.sharecontainer.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private ButtonView re_zhuche; //注册按钮
    private Button mibao;//密保
    private MaterialEditText zhanghao;// 账号
    private EditText mima;//密码
    private EditText mima2;//确认密码
    private EditText re_edmibao; //密保回答
    private ImageButton re_ImgBu1;//img1
    private ImageButton re_ImgBu2;//img2
    private OkHttpClient  httpClient = new OkHttpClient();
    private TitleBar titlebar;//标题
    final  String[] subj={"你母亲的生日?", "高中班主任姓名?", "你父亲生日是什么?"};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ExitActivity.getInstance().addActivity(this);

        mibao=findViewById(R.id.mibao);
        re_zhuche=findViewById(R.id.re_zhuce);
        re_ImgBu1=findViewById(R.id.re_ImgBu1);
        re_ImgBu2=findViewById(R.id.re_ImgBu2);
        zhanghao=findViewById(R.id.zhanghao);
        mima=findViewById(R.id.mima);
        mima2=findViewById(R.id.mima2);
        re_edmibao=findViewById(R.id.re_edmibao);
        titlebar=findViewById(R.id.titlebar);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        mima.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码不可见
        //监听Imgbutton1 是否触碰
        re_ImgBu2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // 触碰时
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.kang));//换背景图
                    mima.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
                }
                //松手恢复原来图片
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.chapasa));
                    mima.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码

                }
                return false;
            }
        });
        mima2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        re_ImgBu1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 触碰时
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.kang));//换背景图
                    mima2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
                }
                //松手恢复原来图片
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.mipmap.chapasa));
                    mima2.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码

                }
                return false;
            }
        });

        mibao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出密保单项框
                new AlertDialog.Builder(Register.this)
                        .setTitle("请选择密保问题?")
                        .setIcon(R.mipmap.mibao)
                        .setSingleChoiceItems(subj, 0,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mibao.setText(subj[which]);
                                    }
                                }).setPositiveButton("确定", null)
                        .show();

            }
        });
        // 注册按钮
        re_zhuche.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Zhuche();//调用注册方法
            }
        });

        zhanghao.addValidator(new RegexpValidator("必须以字母开头", "^[a-zA-Z][a-zA-Z0-9_]*$"));
        zhanghao.addValidator(new RegexpValidator("必须以字母开头", "^[a-zA-Z][a-zA-Z0-9_]*$"));
    }
    /**
     * 注册方法
     */
    public  void  Zhuche(){
        String re_id=zhanghao.getText().toString().trim();
        String re_mima=mima.getText().toString().trim();
        String re_mima2=mima2.getText().toString().trim();
        String re_mibao=mibao.getText().toString().trim();
        String re_huida=re_edmibao.getText().toString().trim();
        if(re_id.equals("")){
            Toast.makeText(Register.this,"账号不可为空", Toast.LENGTH_SHORT).show();
        }else if(re_mima.equals("")){
            Toast.makeText(Register.this,"密保不可为空", Toast.LENGTH_SHORT).show();
        }else if(re_mima.equals(re_mima2)==false){
            Toast.makeText(Register.this,"密码不一致", Toast.LENGTH_SHORT).show();
            re_mima="";
            re_mima2="";
            mima.requestFocus();
        }else if(re_huida.equals("")||re_mibao.trim().equals("请选择密保问题")){
            Toast.makeText(Register.this,"请选择密保问题且回答", Toast.LENGTH_SHORT).show();
        }else{
            FormBody formBody=new FormBody.Builder().addEncoded("user",re_id)
                    .addEncoded("password",re_mima)
                    .addEncoded("question",re_mibao)
                    .addEncoded("anwser",re_huida).build();
            Request request=new Request.Builder().post(formBody)
                    .url("http://47.95.234.172:8080/test/RegisterServlet").build();
            httpClient.newCall(request).enqueue(new Callback() {
                public void onFailure( Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Register.this,"请求错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                public void onResponse(Call call,Response response) throws IOException {
                    final String aa=response.body().string();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(aa.equals("1行收到影响注册成功")){
                                new AlertDialog.Builder(Register.this).setMessage("注册成功")
                                        .setPositiveButton("确定",null).create().show();
                            }
                            else
                            {
//                                Toast.makeText(Register.this,"注册成功",Toast.LENGTH_LONG).show();
                                XToastUtils.success("注册成功");
                                finish();
                            }

                        }
                    });
                }
            });
        }

    }
}
