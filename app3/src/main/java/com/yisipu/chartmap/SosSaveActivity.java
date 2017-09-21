package com.yisipu.chartmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class SosSaveActivity extends SerialPortActivityNoBaseActivity {
    private EditText sos_phone;
    private SharedPreferences sp;
    private Context ctx;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setLayoutView(R.layout.activity_sos_setting);
//        initTitle();
//        sos_phone = (EditText) findViewById(R.id.sos_phone);
        String cmd2="!AIBBM,1,1,,0,14,DluC85=?Dh,4*55\r\n";
//        Logger.i("aaa:"+cmd2);
        UART1Tx(cmd2);

        sp = getSharedPreferences("Phone", MODE_PRIVATE);
        //获取本地的报警电话，默认为：110
        phone = sp.getString("sosphone", "0591968195");
          call();
        finish();


    }
    public void call() {
        Toast toast = null;
        phone = sp.getString("sosphone", "0591968195");
        // 取得输入的电话号码串
        if (phone != null && phone != "") {
            Intent phoneIntent = new Intent(
                    "android.intent.action.CALL", Uri.parse("tel:"
                    + phone));
            // 启动
            startActivity(phoneIntent);
            //自定义Toast
//            toastCommom.ToastShow(MainActivity.this, (ViewGroup) findViewById(R.id.toast_layout_root), "紧急警报");
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_xml,
                    (ViewGroup) findViewById(R.id.toast_layout_root));
            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER , 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);

            toast.show();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }



}
