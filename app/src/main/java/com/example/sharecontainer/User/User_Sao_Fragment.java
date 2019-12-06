package com.example.sharecontainer.User;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sharecontainer.R;
import com.example.sharecontainer.User.ConfirmOPenDoor.OpenDoor;
import com.example.sharecontainer.User.Indent.ShoppingIndent;
import com.example.sharecontainer.User.SetPassword.Frist_Setpaypwd;
import com.example.sharecontainer.usedata;
import com.example.sharecontainer.utils.XToastUtils;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;
import com.hb.dialog.dialog.ConfirmDialog;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class User_Sao_Fragment extends Fragment implements View.OnClickListener {
    private View view;
    private TitleBar titleBar;
    private ShadowButton sao_re1;
    private OkHttpClient httpClient=new OkHttpClient();

    private long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;
//    private String[] arr;//用于接收服务器返回用户是否有设置密码，是否同意扣款协议

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_saofragment, container, false);
        sao_re1=view.findViewById(R.id.sao_re1);
        titleBar=view.findViewById(R.id.titlebar);
        titleBar.disableLeftView();  //禁用左侧的图标及文字
        sao_re1.setOnClickListener(this);
        return view;
    }


    public void onClick(View view) {
        // 返回的是  id，支付密码，余额，扣款协议 0：不同意  1：同意
        switch (view.getId()){
            case R.id.sao_re1:
                long nowTime = System.currentTimeMillis();
                if (nowTime - mLastClickTime > TIME_INTERVAL) {
                    // do something
                    mLastClickTime = nowTime;
                    setPasswod();
                }

                break;
        }
    }

    /**
     * 申请权限通过后跳转相机
     */
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission
                    .CAMERA)) {
                Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 跳出相机扫码
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    /**
     * 动态权限的回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
            case Constant.REQ_PERM_EXTERNAL_STORAGE:
                // 文件读写权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * 启动相机后 扫描结果的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            // 扫码结果
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //判断是否符合我们设备的二维码
            if(scanResult.startsWith("XRYH01")){
                Intent intent2=new Intent(getActivity(),OpenDoor.class);
                intent2.putExtra("facilityRegister",scanResult);
                startActivity(intent2);
            }
            else if(scanResult.equals("正在购买")){
                new MaterialDialog.Builder(getActivity())
                        .iconRes(R.drawable.icon_tip)
                        .title("尊敬的客户")
                        .content("当前正在购买,请稍等")
                        .positiveText("确定").show();
            }
            else{
                //Toast.makeText(getActivity(),"二维码不符合",Toast.LENGTH_SHORT).show();
                new MaterialDialog.Builder(getActivity())
                        .iconRes(R.drawable.icon_tip)
                        .title("提示")
                        .content("二维码不符")
                        .positiveText("确定").show();
            }
        }
    }

    /**
     * 检查当前用户是否有设置支付密码
     */
    private void  setPasswod(){
        final usedata use=(usedata)getActivity().getApplication();
        FormBody formBody=new FormBody.Builder()
                .addEncoded("type","u_account").addEncoded("u_id",use.getId()).build();
        Request request=new Request.Builder().post(formBody)
                .url("http://47.95.234.172:8080/test/ReturnServlet").build();
        httpClient.newCall(request).enqueue(new Callback() {
            public void onFailure( Call call,  IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),"请求错误",Toast.LENGTH_LONG).show();
                    }
                });
            }
            public void onResponse( Call call,  Response response) throws IOException {
                // 返回的是  id，支付密码，余额，扣款协议 0：不同意  1：同意
                final String aa=response.body().string();
                final String[] arr=aa.split(",");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if(arr[1].equals("null") )
                        {
                            ConfirmDialog showDialog = new ConfirmDialog(getActivity());
                            showDialog .setLogoImg(R.mipmap.dialog_notice).setMsg("您尚未开启支付服务，是否设置支付密码");
                            showDialog .setClickListener(new ConfirmDialog.OnBtnClickListener() {
                                public void ok() {
                                    Intent in=new Intent(getActivity(), Frist_Setpaypwd.class);
                                    startActivity(in);

                                }
                                public void cancel() {

                                }
                            });
                            showDialog .show();
                        }
                        else if(use.getDindan().equals("0")){
//                            Toast.makeText(getActivity(),"有未完成订单"+use.getDindan(),Toast.LENGTH_SHORT).show();
                            new MaterialDialog.Builder(getActivity())
                                    .iconRes(R.drawable.icon_warning)
                                    .title("您好")
                                    .content("您当前有未结算的订单！请完成订单尚可购物！")
                                    .positiveText("确定").dismissListener(new DialogInterface.OnDismissListener() {
                                public void onDismiss(DialogInterface dialogInterface) {
                                    Intent intent=new Intent(getActivity(),User_index.class);
                                    intent.putExtra("id",1);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                                    .show();
                        }
                        else
                        {
                            startQrCode();//调用相机
                        }

                        // Toast.makeText(getActivity().getApplicationContext(),arr[2],Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    //
}
