package com.yisipu.chartmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;

import java.util.List;

public class BjSettingActivity extends SerialPortActivity implements  OnClickListener{
    private EditText sdgl;//速度过滤
    private Button bcpz;//保存按钮
    private SharedPreferences sp;
    String gj = "";
    private TextView gaojing;

    @Override
    protected void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();

        if(isOpen&& this.getCurrentFocus()!=null) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private TextView anquanfw;
    private boolean pandun(String str) {
        boolean ret = true;
        try {
            double d = Double.parseDouble(str);
            ret = true;
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }
    CheckBox cb_sgbj,cb_sgbj2,cb_ypbj,cb_ypbj2,cb_zdbj,cb_zdbj2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bj_setting);
        initTitle();
        gaojing = (TextView) findViewById(R.id.gjts);
        anquanfw = (TextView) findViewById(R.id.lpaqfw);
        sdgl = (EditText) findViewById(R.id.bj_sdgl);
        bcpz = (Button) findViewById(R.id.bj_bcpz);
        cb_sgbj = (CheckBox) findViewById(R.id.cb_sgbj);
        cb_ypbj = (CheckBox) findViewById(R.id.cb_ypbj);
        cb_zdbj = (CheckBox) findViewById(R.id.cb_zdbj);
        cb_sgbj2= (CheckBox) findViewById(R.id.cb_sgbj2);
        cb_ypbj2= (CheckBox) findViewById(R.id.cb_ypbj2);
        cb_zdbj2= (CheckBox) findViewById(R.id.cb_zdbj2);
sp=getSharedPreferences("sp",MODE_PRIVATE);
        String shanguang = sp.getString("sg", "开");
        String zhendong = sp.getString("zd", "开");
        String yinpin = sp.getString("yp", "开");

        String shanguang2 = sp.getString("fpsg", "开");
        String zhendong2 = sp.getString("fpzd", "开");
        String yinpin2 = sp.getString("fpyp", "开");


        cb_sgbj.setChecked(true);
        cb_sgbj.setClickable(false);
//        cb_sgbj.setEnabled(false);
        cb_ypbj2.setChecked(true);

        cb_ypbj2.setClickable(false);
        gj = sp.getString("gjts", "关");
        if (gj.equals("关")) {
            gaojing.setText("告警提示：关");
        } else if (gj.equals("开")) {
            gaojing.setText("告警提示：开");
        }
        gj = sp.getString("lpaqfw", "关");
        if (gj.equals("开")) {
            anquanfw.setText("范围报警：开");
        }
        if (gj.equals("关")) {
            anquanfw.setText("范围报警：关");
        }
//        if (shanguang.equals("开")) {
//           cb_sgbj.setChecked(true);
//        }else{
//            cb_sgbj.setChecked(false);
//        }
        if (zhendong.equals("开")) {
      cb_zdbj.setChecked(true);
        }else{
            cb_zdbj.setChecked(false);
        }
        if (yinpin.equals("开")) {
            cb_ypbj.setChecked(true);
        }else {
            cb_ypbj.setChecked(false);
        }
        if (shanguang2.equals("开")) {
            cb_sgbj2.setChecked(true);
        }else{
            cb_sgbj2.setChecked(false);
        }
        if (zhendong2.equals("开")) {
            cb_zdbj2.setChecked(true);
        }else{
            cb_zdbj2.setChecked(false);
        }
//        if (yinpin2.equals("开")) {
//            cb_ypbj2.setChecked(true);
//        }else {
//            cb_ypbj2.setChecked(false);
//        }
        gaojing.setOnClickListener(this);
        anquanfw.setOnClickListener(this);
        float z=sp.getFloat(  "sdgl",0.5f);
        sdgl.setText(""+z);
        bcpz.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String sdgltex = sdgl.getText().toString();
                if(TextUtils.isEmpty(sdgltex)){
                    Toast.makeText(BjSettingActivity.this, "速度过滤值不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
             Boolean a= pandun(sdgltex);
              if(a==false){
                    Toast.makeText(BjSettingActivity.this, "速度过滤输入格式有误", Toast.LENGTH_SHORT).show();
               return;
                }

               double sd=Double.parseDouble(sdgltex);
                if (sd >= 0.1 && sd <= 1) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putFloat("sdgl", (float) sd);
                    editor.commit();
                    Toast.makeText(BjSettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(BjSettingActivity.this, "速度过滤输入格式有误", Toast.LENGTH_SHORT).show();
              return;
                }
                SharedPreferences.Editor editor = sp.edit();


//                    if(cb_sgbj.isChecked()){
                        editor.putString("sg", "开");


//                    }else {
//                        editor.putString("sg", "关");
//                    }
                if(cb_zdbj.isChecked()){
                    editor.putString("zd", "开");


                }else {
                    editor.putString("zd", "关");
                }
                if( cb_ypbj.isChecked()){
                    editor.putString("yp", "开");


                }else {
                    editor.putString("yp", "关");
                }
                if(cb_sgbj2.isChecked()){
                    editor.putString("fpsg", "开");


                }else {
                    editor.putString("fpsg", "关");
                }
                if(cb_zdbj2.isChecked()){
                    editor.putString("fpzd", "开");


                }else {
                    editor.putString("fpzd", "关");
                }
//                if( cb_ypbj.isChecked()){
                    editor.putString("fpyp", "开");


//                }else {
//                    editor.putString("fpyp", "关");
//                }
                editor.commit();



            }
        });
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

    public void initTitle() {
        hideTitleRight();
        setTitleText("报警设置");
        showTitle();
    }

    @Override
    public void onBackPress(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();

        if(isOpen&& this.getCurrentFocus()!=null) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        super.onBackPress(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gjts:
                SharedPreferences.Editor editor2 = sp.edit();
                gj = sp.getString("gjts", "关");
                if (gj.equals("开")) {
                    gaojing.setText("告警提示：关");
                    editor2.putString("gjts", "关");
                }

                if (gj.equals("关")) {
                    gaojing.setText("告警提示：开");
                    editor2.putString("gjts", "开");
                }
                editor2.commit();
                break;

            case R.id.lpaqfw:
                SharedPreferences.Editor editor3 = sp.edit();
                gj = sp.getString("lpaqfw", "关");
                if (gj.equals("开")) {
                    anquanfw.setText("范围报警：关");
                    editor3.putString("lpaqfw", "关");

//            setNodeString(BJ, "0");
//            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            vibrator.cancel();
//            if (mPlayer != null) {
//                mPlayer.stop();
//                mPlayer.release();
//                mPlayer=null;
//            }
                }
                if (gj.equals("关")) {
                    anquanfw.setText("范围报警：开");
                    editor3.putString("lpaqfw", "开");
                }
                editor3.commit();
                break;
        }
    }


}
