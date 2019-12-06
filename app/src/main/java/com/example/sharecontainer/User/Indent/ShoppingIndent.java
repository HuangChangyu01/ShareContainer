package com.example.sharecontainer.User.Indent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharecontainer.AdapterClass.Info;
import com.example.sharecontainer.AdapterClass.MyAdapter;
import com.example.sharecontainer.MyReceiver;
import com.example.sharecontainer.R;
import com.example.sharecontainer.User.User_index;
import com.example.sharecontainer.usedata;
import com.hb.dialog.dialog.ConfirmDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

public class ShoppingIndent extends AppCompatActivity {
    private ListView lv;
    private MyAdapter myAdapter;
    private Info info;
    private List<Info> list=new ArrayList<>();
    private TextView Zhong_money;
    private Handler shandler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 123456:
                    Log.d("cheshi03", "新 "+123456);
                      list.clear();
                      final Info[] in2=(Info[])msg.obj;
                      int i=0;
                      int j=0;
                      double count=0;
                      while (in2[i++]!=null){
                          list.add(in2[j]);
                          count+= Double.valueOf(in2[j].getMoney());
                          j++;
                      }
                      myAdapter.notifyDataSetChanged();
                      Zhong_money.setText(String.valueOf(count));
                    break;
                case 2:
                    new MaterialDialog.Builder(ShoppingIndent.this)
                            .iconRes(R.drawable.icon_tip)
                            .title("提示")
                            .content("支付成功 金额￥"+Zhong_money.getText())
                            .positiveText("确定").dismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialogInterface) {
                            Intent intent=new Intent(ShoppingIndent.this, User_index.class);
                            intent.putExtra("id",1);
                            startActivity(intent);
                            finish();
                        }
                    })
                            .show();
                    break;
                case 3:
                    new MaterialDialog.Builder(ShoppingIndent.this)
                            .iconRes(R.drawable.icon_warning)
                            .title("提示")
                            .content("余额不足，订单待支付\n"+"金额￥"+Zhong_money.getText())
                            .positiveText("确定").dismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialogInterface) {
                            Intent intent=new Intent(ShoppingIndent.this, User_index.class);
                            intent.putExtra("id",1);
                            startActivity(intent);
                            finish();
                        }
                    })
                            .show();
                    usedata data=(usedata)getApplication();
                    data.setDindan("0");
                    break;
                case 1:
                    list.clear();
                    Zhong_money.setText("0.00");
                    myAdapter.notifyDataSetChanged();
                    break;
                case 0:
                    new MaterialDialog.Builder(ShoppingIndent.this)
                            .iconRes(R.drawable.icon_tip)
                            .title("尊敬的用户")
                            .content(msg.obj.toString())
                            .positiveText("确定").dismissListener(new DialogInterface.OnDismissListener() {
                        public void onDismiss(DialogInterface dialogInterface) {
                            Intent intent=new Intent(ShoppingIndent.this, User_index.class);
                            intent.putExtra("id",1);
                            startActivity(intent);
                            finish();
                        }
                    })
                            .show();
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_indent);

        lv=findViewById(R.id.shop_lv1);
        Zhong_money=findViewById(R.id.Zhong_money);
        myAdapter=new MyAdapter(this,list);
        lv.setAdapter(myAdapter);

        MyReceiver.handlerMessage(shandler);
    }


//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                Toast.makeText(this,"afadf",Toast.LENGTH_SHORT).show();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Toast.makeText(this,"afadf",Toast.LENGTH_SHORT).show();
//                break;
//            case MotionEvent.ACTION_UP:
//                Toast.makeText(this,"afadf",Toast.LENGTH_SHORT).show();
//
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

}
