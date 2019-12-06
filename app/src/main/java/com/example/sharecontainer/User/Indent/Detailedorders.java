package com.example.sharecontainer.User.Indent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.sharecontainer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detailedorders extends AppCompatActivity {
    private List<Map<String,Object>> data=new ArrayList<Map<String, Object>>();
    private SimpleAdapter simpleAdapter;
    private  Map<String,Object> map;
    private ListView d_list;
    private ImageView fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedoeders);

        d_list=findViewById(R.id.d_list);
        fanhui=findViewById(R.id.fanhui);

        Intent in=getIntent();
        String shuju=in.getStringExtra("Dingdang");
        Log.d("cheshi04", "订单页面: "+shuju);
        String[] shujuli=shuju.split("[+]");
        Log.d("cheshi04", "onResponse: "+shujuli[2]);
        for(int i=2;i<shujuli.length-2;i++){
            String aa=shujuli[i].substring(1,shujuli[i].length()-1);
            String[] aali=aa.split(",");
            Log.d("cheshi04", "订单详情: "+aali[0]+aali[1]+aali[2]);
            map=new HashMap<String,Object>();
            map.put("name",aali[1]);
            map.put("id","ID:"+aali[0]);
            map.put("pay",aali[2]);
            data.add(map);
       }

        simpleAdapter=new SimpleAdapter(Detailedorders.this,data, R.layout.item3,new String[]{"name","id","pay"},
                new int[]{R.id.i_tv1,R.id.i_tv2, R.id.i_tv3});
        d_list.setAdapter(simpleAdapter);

        fanhui.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}
