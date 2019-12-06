package com.example.sharecontainer.AdapterClass;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharecontainer.R;

import java.util.List;

public class MyAdapterD extends BaseAdapter {
    private List<Info> appInfos;
    private Context context;

    public MyAdapterD( Context context,List<Info> info){
        this.appInfos=info;
        this.context=context;
    }
    public int getCount() {
        return appInfos.size();
    }

    public Object getItem(int i) {
        return appInfos.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View item=view!=null? view:View.inflate(context,R.layout.item,null);
        ImageView i_img=item.findViewById(R.id.i_img);
        TextView name=item.findViewById(R.id.i_tv1);
        TextView nub=item.findViewById(R.id.i_tv2);
        TextView money=item.findViewById(R.id.i_tv3);

        final  Info a=appInfos.get(i);
        i_img.setImageResource(a.getImg());
        name.setText(a.getName());
        nub.setText(a.getNub());
        money.setText(a.getMoney());

        return item;
    }
}

