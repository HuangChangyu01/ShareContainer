package com.example.sharecontainer.User;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sharecontainer.R;
import com.example.sharecontainer.Tool.ExitActivity;
import com.example.sharecontainer.usedata;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class My_Fragment_Message extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout Touxian;
    private LinearLayout name;
    private LinearLayout id;
    private LinearLayout sex;
    private LinearLayout bri;
    private ImageView sh_touxian;
    private TextView sh_name;
    private TextView sh_id;
    private TextView sh_sex;
    private TextView sh_bri;
    private Button save;
    private ImageButton fanhui;
    private OkHttpClient  httpClient;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int  PICTURE = 2;
    protected static Uri muri;
    private Bitmap mBitmap;
    private static final int ACTION_TYPE_PHOTO = 0;
    private String data;//获取床送的数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__fragment__message);
        ExitActivity.getInstance().addActivity(this);//加入关闭类的list中

        Touxian=findViewById(R.id.message_touxian);
        name=findViewById(R.id.message_name);
        id=findViewById(R.id.message_id);
        sex=findViewById(R.id.message_sex);
        bri=findViewById(R.id.message_date);
        save=findViewById(R.id.message_save);
        fanhui=findViewById(R.id.mes_fanhui);

        sh_touxian=findViewById(R.id.message_touxianjpg);
        sh_name=findViewById(R.id.message_nicheng);
        sh_id=findViewById(R.id.message_uid);
        sh_sex=findViewById(R.id.message_xinbie);
        sh_bri=findViewById(R.id.message_bri);

        Touxian.setOnClickListener(this);
        name.setOnClickListener(this);
        id.setOnClickListener(this);
        sex.setOnClickListener(this);
        bri.setOnClickListener(this);
        save.setOnClickListener(this);
        fanhui.setOnClickListener(this);

//        Intent in=getIntent();
//        data=in.getStringExtra("content");//读取登入后传送的数据
//        String[] arr=data.split(",");

//        sh_name.setText(arr[1]);
//        sh_id.setText(arr[0]);
//        sh_sex.setText(arr[2]);
//        sh_bri.setText(arr[3]);

        // 读去usedata类信息 加载到UI界面
        usedata use=(usedata)getApplication();
        sh_name.setText(use.getName());
        sh_id.setText(use.getId());
        sh_sex.setText(use.getSex());
        sh_bri.setText(use.getBir());

    }

    /**
     * 点击事件
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message_touxian:
                showDialog();
                break;
            case R.id.message_name:
                showname();//设置姓名弹窗
                break;
            case R.id.message_sex:
                showsex();//性别弹窗
                break;
            case R.id.message_date:
                showDatePickerDialog();//设置日期弹窗方法
                break;
            case R.id.message_save:
                http();
                break;
            case R.id.mes_fanhui:
                My_Fragment_Message.this.finish(); // 返回
                break;
            case R.id.message_id:
                break;
        }
    }

    /**
     * 日期弹窗方法
     */
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(My_Fragment_Message.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                sh_bri.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 姓名弹窗
     */
    public void showname(){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入昵称")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        sh_name.setText(et.getText().toString());
                    }
                }).setNegativeButton("取消",null).show();
    }

    /**
     * 性别弹窗
     */
    private  void showsex(){
        final String[] a={"男", "女"};
        new AlertDialog.Builder(this).setTitle("请选择性别")
                .setSingleChoiceItems(a, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sh_sex.setText(a[which]);
                            }
                        }).setPositiveButton("确定",null).show();
    }

    /**
     * 设置头像方式选项窗口方法
     */
    protected void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(My_Fragment_Message.this);
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照选取
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        muri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, muri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * 图片剪切方法
     * @param uri
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        muri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICTURE);
    }

    /**
     * 显示图片
     * @param data
     */
    protected void setImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            sh_touxian.setImageBitmap(mBitmap);//显示图片
        }
    }

    /**
     * 图片回调方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == My_Fragment_Message.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(muri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case PICTURE:
                    if (data != null) {
                        setImage(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 修改信息上传服务器
     */
    private  void  http(){
        httpClient=new OkHttpClient();
        String img="1";
        String id=sh_id.getText().toString().trim();
        String name=sh_name.getText().toString().trim();
        String sex=sh_sex.getText().toString().trim();
        String bri=sh_bri.getText().toString().trim();
        FormBody formBody=new FormBody.Builder().addEncoded("type","info")
                .addEncoded("u_id",id)
                .addEncoded("u_img","1")
                .addEncoded("u_name",name)
                .addEncoded("u_sex",sex)
                .addEncoded("u_birthday",bri).build();
        Request request=new Request.Builder().post(formBody)
                .url("http://47.95.234.172:8080/test/SetServlet").build();
        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call,IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(My_Fragment_Message.this,"请求错误",Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void onResponse(Call call,Response response) throws IOException {
                final String aa=response.body().string();
                final String[] arr = aa.split(",");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(My_Fragment_Message.this,arr[0],Toast.LENGTH_SHORT).show();
//                        sh_touxian.setImageBitmap();
                        usedata data=(usedata)getApplication();
//                        data.setImg(arr[5]);
                        data.setId(arr[1]);
                        data.setName(arr[2]);
                        data.setSex(arr[3]);
                        data.setBir(arr[4]);

                        sh_name.setText(arr[2]);
                        sh_id.setText(arr[1]);
                        sh_sex.setText(arr[3]);
                        sh_bri.setText(arr[4]);

                    }
                });
            }
        });
    }
}
