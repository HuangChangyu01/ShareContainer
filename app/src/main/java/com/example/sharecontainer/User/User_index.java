package com.example.sharecontainer.User;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sharecontainer.R;
import com.example.sharecontainer.Tool.ExitActivity;

public class User_index extends AppCompatActivity implements View.OnClickListener {

    private Button my;
    private Button sao;
    private Button ding;
    private User_My_Fragment user_my_fragment;
    private User_Sao_Fragment user_sao_fragment;
    private User_Ding_Fragment user_form_fragment;
    private FragmentManager fm;//事务
    private String data;//获得登入后返回的结果
    private  Bundle bundle;
    public static User_index instance=null;// 用于在其他界面关闭本Activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);
        instance=this;
        ExitActivity.getInstance().addActivity(this);//加入关闭类的list中



//        Intent in=getIntent();
//        data=in.getStringExtra("content");//读取登入后传送的数据
//        usedata aa=new usedata();
//        usedata use=(usedata)getApplication();
//        data=use.getId()+","+use.getName()+","+use.getSex()+","+use.getBir()+","+use.getImg();
        //创造要传送的给各frament的数据
//        bundle = new Bundle();
//        bundle.putString("content",data);
        //获取控件
        sao=findViewById(R.id.u_in_sao);
        ding=findViewById(R.id.u_in_ding);
        my=findViewById(R.id.u_in_my);
        //控件监听
        my.setOnClickListener(this);
        sao.setOnClickListener(this);
        ding.setOnClickListener(this);

        Intent in=getIntent();
        int id = in.getIntExtra("id", 0);
        Log.d("cheshi03" ,"onCreate: "+id);
        if (id == 1) {
            fm=getSupportFragmentManager();//开启事务
            if(user_form_fragment==null){
                user_form_fragment=new User_Ding_Fragment();
//                    user_form_fragment.setArguments(bundle);
            }
            fm.beginTransaction().replace(R.id.u_ib_f1,user_form_fragment).commit();
//            ding.setEnabled(false);
//            my.setEnabled(true);
//            sao.setEnabled(true);
            Drawable dra4 = getResources().getDrawable(R.drawable.my);
            dra4.setBounds(0, 0, dra4.getMinimumWidth(), dra4.getMinimumHeight());
            my.setCompoundDrawables(null,dra4,null,null);
            my.setTextColor(Color.BLACK);

            Drawable dra5 = getResources().getDrawable(R.drawable.sao5);
            dra5.setBounds(0, 0, dra5.getMinimumWidth(), dra5.getMinimumHeight());
            sao.setCompoundDrawables(null,dra5,null,null);
            sao.setTextColor(Color.BLACK);

            Drawable dra6 = getResources().getDrawable(R.drawable.ding2);
            dra6.setBounds(0, 0, dra6.getMinimumWidth(), dra6.getMinimumHeight());
            ding.setCompoundDrawables(null,dra6,null,null);
            ding.setTextColor(Color.RED);
        }
        else{
            //调用初始化方法
            initmy() ;
        }

    }
//    protected void onRestart() {
//        super.onRestart();
//        use=(usedata)getApplication();
//        data=use.getId()+","+use.getName()+","+use.getSex()+","+use.getBir()+","+use.getImg();
////        bundle = new Bundle();
//        if(bundle!=null){
//            bundle.putString("content",data);
//        }
//        else{
//            bundle = new Bundle();
//            bundle.putString("content",data);
//        }
//
//    }

    /**
     *  初始化页面--加载到我的基本信息页面
     */
    public void  initmy(){
        if(user_my_fragment==null){
            user_my_fragment=new User_My_Fragment();
        }
        fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.u_ib_f1,user_my_fragment).commit();//开启事务
//        my.setEnabled(false);
//        sao.setEnabled(true);
//        ding.setEnabled(true);
        Drawable dra = getResources().getDrawable(R.drawable.my2);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        my.setCompoundDrawables(null,dra,null,null);
        my.setTextColor(Color.RED);

        Drawable dra2 = getResources().getDrawable(R.drawable.sao5);
        dra2.setBounds(0, 0, dra2.getMinimumWidth(), dra2.getMinimumHeight());
        sao.setCompoundDrawables(null,dra2,null,null);
        sao.setTextColor(Color.BLACK);

        Drawable dra3 = getResources().getDrawable(R.drawable.ding);
        dra3.setBounds(0, 0, dra3.getMinimumWidth(), dra3.getMinimumHeight());
        ding.setCompoundDrawables(null,dra3,null,null);
        ding.setTextColor(Color.BLACK);


    }

    /**
     * 点击底部菜单方法
     * @param v
     */
    public void onClick(View v) {
        fm=getSupportFragmentManager();//开启事务
        switch (v.getId()){
            case R.id.u_in_my:
                initmy();
//                my.setEnabled(false);
//                sao.setEnabled(true);
//                ding.setEnabled(true);

                break;
            case R.id.u_in_sao:
                if(user_sao_fragment==null){
                    user_sao_fragment=new User_Sao_Fragment();

                }
                fm.beginTransaction().replace(R.id.u_ib_f1,user_sao_fragment).commit();
//                sao.setEnabled(false);
//                my.setEnabled(true);
//                ding.setEnabled(true);
                Drawable dra = getResources().getDrawable(R.drawable.my);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                my.setCompoundDrawables(null,dra,null,null);
                my.setTextColor(Color.BLACK);

                Drawable dra2 = getResources().getDrawable(R.drawable.sao6);
                dra2.setBounds(0, 0, dra2.getMinimumWidth(), dra2.getMinimumHeight());
                sao.setCompoundDrawables(null,dra2,null,null);
                sao.setTextColor(Color.RED);

                Drawable dra3 = getResources().getDrawable(R.drawable.ding);
                dra3.setBounds(0, 0, dra3.getMinimumWidth(), dra3.getMinimumHeight());
                ding.setCompoundDrawables(null,dra3,null,null);
                ding.setTextColor(Color.BLACK);
                break;
            case R.id.u_in_ding:
                if(user_form_fragment==null){
                    user_form_fragment=new User_Ding_Fragment();
//                    user_form_fragment.setArguments(bundle);
                }
                fm.beginTransaction().replace(R.id.u_ib_f1,user_form_fragment).commit();
//                ding.setEnabled(false);
//                my.setEnabled(true);
//                sao.setEnabled(true);
                Drawable dra4 = getResources().getDrawable(R.drawable.my);
                dra4.setBounds(0, 0, dra4.getMinimumWidth(), dra4.getMinimumHeight());
                my.setCompoundDrawables(null,dra4,null,null);
                my.setTextColor(Color.BLACK);

                Drawable dra5 = getResources().getDrawable(R.drawable.sao5);
                dra5.setBounds(0, 0, dra5.getMinimumWidth(), dra5.getMinimumHeight());
                sao.setCompoundDrawables(null,dra5,null,null);
                sao.setTextColor(Color.BLACK);

                Drawable dra6 = getResources().getDrawable(R.drawable.ding2);
                dra6.setBounds(0, 0, dra6.getMinimumWidth(), dra6.getMinimumHeight());
                ding.setCompoundDrawables(null,dra6,null,null);
                ding.setTextColor(Color.RED);
                break;
        }

    }

}
