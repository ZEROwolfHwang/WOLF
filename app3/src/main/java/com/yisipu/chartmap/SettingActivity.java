package com.yisipu.chartmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.dialog.CustomDialog2;
import com.yisipu.chartmap.servicer.DownAPKService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends SerialPortActivity {
    private Spinner spinner, spinner2, spinner3;
    private List<String> data_list, keyboard_list;
    private ArrayAdapter<String> arr_adapter, keyboard_adapter;
    private SharedPreferences sp;
    private String md = "", kb = "", kb2 = "";
    private CustomDialog2 dialog;
    private LinearLayout ll;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3;
    int x = 0;
    int dl = 0;
    private TextView fence;
    private TextView gaojing;
    private TextView anquanfw;
    int kg = 0;
    String strfence = "";
    String gj = "";
    private CheckBox sgbj, zdbj, ypbj;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    private final String BJ = "/sys/bus/i2c/devices/3-0053/flash_feature";
    private MediaPlayer mPlayer = null;
    private Vibrator vibrator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_setting);
        initTitle();
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        ll = (LinearLayout) findViewById(R.id.setting_ll);
        radioGroup = (RadioGroup) findViewById(R.id.genderGroup);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        fence = (TextView) findViewById(R.id.fence);
        gaojing = (TextView) findViewById(R.id.gjts);
        anquanfw = (TextView) findViewById(R.id.lpaqfw);
        //数据
        data_list = new ArrayList<String>();
        data_list.add("标准模式");
        data_list.add("智能模式");
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        sp = getSharedPreferences("sp", MODE_PRIVATE);//指定读取的对象
        //判断设置的模式
        md = sp.getString("mode", "标准模式");
        judgemode(md);
        strfence = sp.getString("kg", "关");
        if (strfence.equals("关")) {
            fence.setText("电子围栏：关");
        } else if (strfence.equals("开")) {
            fence.setText("电子围栏：开");
        }
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
        //海里选择
        x = sp.getInt("hl", 1);
        if (x == 1) {
            rb1.setChecked(true);
        }
        if (x == 2) {
            rb2.setChecked(true);
        }
        if (x == 3) {
            rb3.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = sp.edit();
                if (rb1.getId() == checkedId) {
                    editor.putInt("hl", 1);
                    Toast.makeText(SettingActivity.this, "1海里", Toast.LENGTH_SHORT).show();
                }
                if (rb2.getId() == checkedId) {
                    editor.putInt("hl", 2);
                    Toast.makeText(SettingActivity.this, "2海里", Toast.LENGTH_SHORT).show();
                }
                if (rb3.getId() == checkedId) {
                    editor.putInt("hl", 3);
                    Toast.makeText(SettingActivity.this, "3海里", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSharedPreferences(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //快捷键数据
        keyboard_list = new ArrayList<String>();
        keyboard_list.add("计算器");
        keyboard_list.add("闹钟");
        keyboard_list.add("AIS开关");
        keyboard_list.add("关闭告警");
        //快捷键1
        //判断设置的模式


        keyboard_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, keyboard_list);
        //设置样式
        keyboard_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner2.setAdapter(keyboard_adapter);
        kb = sp.getString("keyboard", "计算器");
        judgekeyboard(kb);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == spinner3.getSelectedItemPosition()) {
                    SharedPreferences.Editor editor = sp.edit();//获取编辑对象
                    if (spinner2.getSelectedItemPosition() == 0) {
                        editor.putString("keyboard", "AIS开关");
                        spinner2.setSelection(2);
                    } else {
                        editor.putString("keyboard", "计算器");
                        spinner2.setSelection(0);
                    }
                    Toast.makeText(SettingActivity.this, "不能设置与快捷2相同功能", Toast.LENGTH_SHORT).show();
                } else {
                    setkeyboard(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        快捷键2
//        判断设置的模式

        spinner3.setAdapter(keyboard_adapter);
        kb2 = sp.getString("keyboard2", "闹钟");
        judgekeyboard2(kb2);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == spinner2.getSelectedItemPosition()) {
                    SharedPreferences.Editor editor = sp.edit();//获取编辑对象
                    if (spinner3.getSelectedItemPosition() == 1) {
                        editor.putString("keyboard2", "AIS开关");
                        spinner3.setSelection(2);
                    } else {
                        editor.putString("keyboard2", "闹钟");
                        spinner3.setSelection(1);
                    }
                    Toast.makeText(SettingActivity.this, "不能设置与快捷1相同功能", Toast.LENGTH_SHORT).show();
                } else {
                    setkeyboard2(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //告警设置
        sgbj = (CheckBox) findViewById(R.id.cb_sgbj);
        zdbj = (CheckBox) findViewById(R.id.cb_zdbj);
        ypbj = (CheckBox) findViewById(R.id.cb_ypbj);

        sgbj.setOnCheckedChangeListener(new CBOnCheckedChangListenter() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        String sg = sp.getString("sg", "开");
        String zd = sp.getString("zd", "开");
        String yp = sp.getString("yp", "开");
        if (sg.equals("开")) {
            sgbj.setChecked(true);
        } else {
            sgbj.setChecked(false);
        }
        if (zd.equals("开")) {
            zdbj.setChecked(true);
        } else {
            zdbj.setChecked(false);
        }
        if (yp.equals("开")) {
            ypbj.setChecked(true);
        } else {
            ypbj.setChecked(false);
        }
        sgbj.setOnCheckedChangeListener(new CBOnCheckedChangListenter());
        zdbj.setOnCheckedChangeListener(new CBOnCheckedChangListenter());
        ypbj.setOnCheckedChangeListener(new CBOnCheckedChangListenter());


    }

    @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {

    }


    private class CBOnCheckedChangListenter implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CheckBox cb = (CheckBox) buttonView;
            SharedPreferences.Editor editor = sp.edit();//获取编辑对象
            if (isChecked) {

                Toast.makeText(SettingActivity.this, "开启了" + cb.getText() + "告警", Toast.LENGTH_SHORT).show();
                if (cb.getText().equals("闪光")) {
                    editor.putString("sg", "开");
                }
                if (cb.getText().equals("震动")) {
                    editor.putString("zd", "开");
                }
                if (cb.getText().equals("音频")) {
                    editor.putString("yp", "开");
                }
                cb.getTag();


            } else {
                if (cb.getText().equals("闪光")) {
                    editor.putString("sg", "关");
                }
                if (cb.getText().equals("震动")) {
                    editor.putString("zd", "关");
                }
                if (cb.getText().equals("音频")) {
                    editor.putString("yp", "关");
                }
                Toast.makeText(SettingActivity.this, "取消了" + cb.getText(), Toast.LENGTH_SHORT).show();
            }
            editor.commit();
        }
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
        setTitleText("设置列表");
        showTitle();
    }

    /*
    跳转到Ais设置
     */
    public void goToAisSetting(View view) {
        Intent intent = new Intent(SettingActivity.this, AisSettingActivity.class);
        startActivity(intent);
    }

    public void aaa(View view) {
        Toast.makeText(SettingActivity.this, kb + kb2, Toast.LENGTH_SHORT).show();
    }

    /*
   跳转到Sos设置
    */
    public void goToSosSetting(View view) {
        Intent intent = new Intent(SettingActivity.this, SosSettingActivity.class);
        startActivity(intent);
    }

    /*
    电子围栏
     */
    public void electronicfence(View view) {
        SharedPreferences.Editor editor = sp.edit();
        strfence = sp.getString("kg", "关");
        if (strfence.equals("开")) {
            fence.setText("电子围栏：关");
            editor.putString("kg", "关");
        }
        if (strfence.equals("关")) {
            fence.setText("电子围栏：开");
            editor.putString("kg", "开");
        }
        editor.commit();
    }
    /*
     海鱼平台
      */
    public void hypt(View view) {
        SharedPreferences.Editor editor = sp.edit();
        strfence = sp.getString("hypt", "关");
        if (strfence.equals("开")) {
            fence.setText("海鱼平台：关");

            editor.putString("hypt", "关");
        }
        if (strfence.equals("关")) {
            fence.setText("海鱼平台：开");
            editor.putString("hypt", "开");
        }
        editor.commit();
    }
    /*
    告警提示
     */
    public void gaojingtishi(View view) {
        SharedPreferences.Editor editor = sp.edit();
        gj = sp.getString("gjts", "关");
        if (gj.equals("开")) {
            gaojing.setText("告警提示：关");
            editor.putString("gjts", "关");
        }

            if (gj.equals("关")) {
                gaojing.setText("告警提示：开");
                editor.putString("gjts", "开");
            }
            editor.commit();
        }


    public static void setNodeString(String path, String val) {
        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bufWriter = new BufferedWriter(fw);
            bufWriter.write(val);  // 写操作
            fw.flush();
            bufWriter.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private static String getNodeString(String path) {
        String prop = "0";// 默认值
        try {
            FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(fr);
            prop = reader.readLine();
            reader.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }

    /*
       罗盘安全范围开关
        */
    public void lpaqfw(View view) {
        SharedPreferences.Editor editor = sp.edit();
        gj = sp.getString("lpaqfw", "关");
        if (gj.equals("开")) {
            anquanfw.setText("范围报警：关");
            editor.putString("lpaqfw", "关");
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
            editor.putString("lpaqfw", "开");
        }
        editor.commit();
    }

    /*
    检查更新
     */
    public void update(View view) throws Exception {
        dialog = new CustomDialog2(SettingActivity.this);
        String myversion = getVersionName();
        dialog.setMessage("当前版本" + myversion + "是否更新版本");


        //判断设置的模式

        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下载APK
                sp = getSharedPreferences("sp", MODE_PRIVATE);//指定读取的对象
                dl = sp.getInt("download", 0);
                if (dl == 0) {
                    Intent intent = new Intent();
                    intent.putExtra("apk_url", "http://count.liqucn.com/d.php?id=22709&urlos=android&from_type=web");
                    intent.setClass(SettingActivity.this, DownAPKService.class);
                    startService(intent);
                } else {
                    Toast.makeText(SettingActivity.this, "正在下载...", Toast.LENGTH_SHORT).show();
                }
//                Intent intent = new Intent();
//                intent.putExtra("apk_url", "http://count.liqucn.com/d.php?id=22709&urlos=android&from_type=web");
//                intent.setClass(SettingActivity.this, DownAPKService.class);
//                startService(intent);
                dialog.dismiss();
            }
        });
        dialog.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
//    //落水
//    public void cs(View view) throws Exception {
//        Intent intent = new Intent(SettingActivity.this, LsActivity.class);
//        startActivity(intent);
//    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    /*
    设置模式
     */
    public void setSharedPreferences(int position) {
        SharedPreferences.Editor editor = sp.edit();//获取编辑对象
        if (position == 0) {
            editor.putString("mode", "标准模式");//keyname是储存数据的键值名，同一个对象可以保存多个键值}
            ll.setVisibility(View.GONE);
        }
        if (position == 1) {
            editor.putString("mode", "智能模式");
//            dialog=new CustomDialog2(SettingActivity.this);
//            EditText editText = (EditText) dialog.getEditText();//方法在CustomDialog中实现
//            dialog.setOnPositiveListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    diaLogger.dismiss();
//                }
//            });
//            dialog.setOnNegativeListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    diaLogger.dismiss();
//                }
//            });
//            dialog.show();
            ll.setVisibility(View.VISIBLE);

        }
        editor.commit();//提交保存修改
    }

    /*
    判断默认模式
     */
    public void judgemode(String md) {
        if (md.equals("标准模式")) {
            spinner.setSelection(0);
        }
        if (md.equals("智能模式")) {
            spinner.setSelection(1);

        }
    }

    /*
   快捷键1模式
    */
    public void setkeyboard(int position) {
        SharedPreferences.Editor editor = sp.edit();//获取编辑对象
        if (position == 0) {
            editor.putString("keyboard", "计算器");//keyname是储存数据的键值名，同一个对象可以保存多个键值}
        }
        if (position == 1) {
            editor.putString("keyboard", "闹钟");
        }
        if (position == 2) {
            editor.putString("keyboard", "AIS开关");
        }
        if (position == 3) {
            editor.putString("keyboard", "关闭告警");
        }
        editor.commit();//提交保存修改
    }

    /*
  快捷键2模式
   */
    public void setkeyboard2(int position) {
        SharedPreferences.Editor editor = sp.edit();//获取编辑对象
        if (position == 0) {
            editor.putString("keyboard2", "计算器");//keyname是储存数据的键值名，同一个对象可以保存多个键值}
        }
        if (position == 1) {
            editor.putString("keyboard2", "闹钟");
        }
        if (position == 2) {
            editor.putString("keyboard2", "AIS开关");
        }
        if (position == 3) {
            editor.putString("keyboard2", "关闭告警");
        }
        editor.commit();//提交保存修改
    }

    public void judgekeyboard(String kb) {
        if (kb.equals("计算器")) {
            spinner2.setSelection(0);
        }
        if (kb.equals("闹钟")) {
            spinner2.setSelection(1);
        }
        if (kb.equals("AIS开关")) {
            spinner2.setSelection(2);
        }
        if (kb.equals("关闭告警")) {
            spinner2.setSelection(3);
        }
    }

    public void judgekeyboard2(String kb2) {
        if (kb2.equals("计算器")) {
            spinner3.setSelection(0);
        }
        if (kb2.equals("闹钟")) {
            spinner3.setSelection(1);
        }
        if (kb2.equals("AIS开关")) {
            spinner3.setSelection(2);
        }
        if (kb2.equals("关闭告警")) {
            spinner3.setSelection(3);
        }
    }
}
