package com.yisipu.chartmap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;

import java.util.List;

public class SosSettingActivity extends SerialPortActivity {
    private EditText sos_phone;
    private SharedPreferences sp;
    private Context ctx;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_sos_setting);
        initTitle();
        sos_phone = (EditText) findViewById(R.id.sos_phone);
        sos_phone.setKeyListener(null);//认证版设置为文本只读
        sp = getSharedPreferences("Phone", MODE_PRIVATE);
        //获取本地的报警电话，默认为：110
        phone = sp.getString("sosphone", "0591968195");
        sos_phone.setText(phone);


    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {

    }

    @Override
    void receiver(String str) {

    }

    @Override
    void gpsreceiver(Gpsbean gpsbean) {

    }

    @Override
    void hdopreceiver(Jdbean jdbean) {

    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }


    /*
     保存按钮的监听（认证版取消该监听）
      */
//    public void keep(View view) {
//        if (sos_phone.getText().toString().equals("")) {
//            Toast.makeText(SosSettingActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            ctx = SosSettingActivity.this;
//            sp = ctx.getSharedPreferences("Phone", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("sosphone", sos_phone.getText().toString());
//            editor.commit();
//            Toast.makeText(SosSettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
//
//        }
//    }

    /*
        获取号码
          */
    public void huoqu(View view) {

        phone = sp.getString("sosphone", "0591968195");
        // 取得输入的电话号码串
        if (phone != null && phone != "") {
            Intent phoneIntent = new Intent(
                    "android.intent.action.CALL", Uri.parse("tel:"
                    + phone));
            // 启动e
            startActivity(phoneIntent);
            new AlertDialog.Builder(SosSettingActivity.this).setTitle("系统提示")//设置对话框标题

                    .setMessage("紧急呼救").show();


        }
        // 否则Toast提示一下
        else {
            Toast.makeText(SosSettingActivity.this, "号码不能为空",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void initTitle() {
        hideTitleRight();
        setTitleText("设置列表");
        showTitle();
    }
}
