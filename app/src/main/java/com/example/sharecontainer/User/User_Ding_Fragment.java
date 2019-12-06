package com.example.sharecontainer.User;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sharecontainer.R;
import com.example.sharecontainer.User.Indent.OrderHistory;
import com.example.sharecontainer.User.Indent.WorderHistory;
import com.xuexiang.xui.widget.actionbar.TitleBar;

public class User_Ding_Fragment extends Fragment implements View.OnClickListener {
    private  View view;
    private Button form2;
    private Button form3;
    private OrderHistory orderHistory;
    private WorderHistory worderHistory;
    private TitleBar titleBar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_formfragment, container, false);

        form2=view.findViewById(R.id.text1);
        form3=view.findViewById(R.id.text2);
        titleBar=view.findViewById(R.id.titlebar);
        titleBar.disableLeftView();  //禁用标题左侧的图标及文字
        form2.setOnClickListener(this);
        form3.setOnClickListener(this);
        initmy();
        return view;
    }
    public void  initmy(){
        if(orderHistory==null){
            orderHistory=new OrderHistory();
        }
        FragmentManager fm=getChildFragmentManager();//开启事务
        fm.beginTransaction().replace(R.id.form_f1,orderHistory).commit();
        form2.setEnabled(false);
        form3.setEnabled(true);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text1:
                initmy();
                break;
            case R.id.text2:

                if(worderHistory==null){
                    worderHistory=new WorderHistory();
                }
                FragmentManager fm=getChildFragmentManager();//开启事务
                fm.beginTransaction().replace(R.id.form_f1,worderHistory).commit();
                form2.setEnabled(true);
                form3.setEnabled(false);
                break;
        }
    }


}
