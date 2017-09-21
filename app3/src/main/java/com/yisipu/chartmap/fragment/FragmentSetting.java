package com.yisipu.chartmap.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import com.fjhtxl.sdk.WorkService;
import com.fjhtxl.sdk.listener.IMultipleListener;
import com.fjhtxl.sdk.net.GmsConfig;
import com.fjhtxl.sdk.protocol.entity.CmdParam;
import com.fjhtxl.sdk.protocol.entity.DevInfo;
import com.fjhtxl.sdk.protocol.entity.MsgInfo;
import com.fjhtxl.sdk.task.SendObjTask;
import com.fjhtxl.sdk.util.ConfigUtil;
import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.AisSettingActivity;
import com.yisipu.chartmap.BjSettingActivity;
import com.yisipu.chartmap.R;
import com.yisipu.chartmap.SosSettingActivity;
import com.yisipu.chartmap.dialog.CustomDialog2;
import com.yisipu.chartmap.servicer.DownAPKService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/5 0005.
 */
public class FragmentSetting extends Fragment implements View.OnClickListener {
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
    private TextView aissz;
    private TextView soscs;
    private TextView hypt;
    private TextView jcgx;
    private TextView bjsz;
    int kg = 0;
    String strfence;
    String gj = "";
    String hy = "";
    private CheckBox sgbj, zdbj, ypbj;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    private final String BJ = "/sys/bus/i2c/devices/3-0053/flash_feature";
    private MediaPlayer mPlayer = null;
    private Vibrator vibrator = null;
    private Activity mActivity;
    private static volatile FragmentSetting fragmentSetting;

    public static FragmentSetting getInstance() {
        if (fragmentSetting == null) {
            synchronized (FragmentSetting.class) {
                if (fragmentSetting == null) {
                    fragmentSetting = new FragmentSetting();
                }
            }
        }
        return fragmentSetting;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if(mActivity!=null) {
                hidShuRuFa(mActivity);
            }
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
            if(mActivity!=null) {
                hidShuRuFa(mActivity);
            }
        }
    }
    /*
     隐藏输入法
      */
    public void hidShuRuFa(Context context) {


        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if(isOpen&& mActivity.getCurrentFocus()!=null)

        {

            if(mActivity!=null) {
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = null;
        if (mView == null && inflater != null) {
            mView = inflater.inflate(R.layout.activity_setting, null);
            findView(mView);
        }

        //数据
        data_list = new ArrayList<String>();
        data_list.add("标准模式");
        data_list.add("智能模式");

        arr_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);//指定读取的对象
        //判断设置的模式
        md = sp.getString("mode", "标准模式");
        judgemode(md);
        strfence = sp.getString("kg", "关");
        if (strfence.equals("关")) {
            fence.setText("电子围栏：关");
        } else if (strfence.equals("开")) {
            fence.setText("电子围栏：开");
        }
        hy = sp.getString("hypt", "关");
        if (hy.equals("开")) {
            hypt.setText("海渔平台：开");
        }
        if (hy.equals("关")) {
            hypt.setText("海渔平台：关");
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

                }
                if (rb2.getId() == checkedId) {
                    editor.putInt("hl", 2);

                }
                if (rb3.getId() == checkedId) {
                    editor.putInt("hl", 3);

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
//        keyboard_list.add("AIS开关");
        keyboard_list.add("关闭告警");
        //快捷键1
        //判断设置的模式


        keyboard_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, keyboard_list);
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
                        editor.putString("keyboard", "关闭告警");
                        spinner2.setSelection(2);
                    } else {
                        editor.putString("keyboard", "计算器");
                        spinner2.setSelection(0);
                    }
                    Toast.makeText(getActivity(), "不能设置与快捷1相同功能", Toast.LENGTH_SHORT).show();
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
        kb2 = sp.getString("keyboard2", "关闭告警");
//        kb2 = sp.getString("keyboard2", "闹钟");
        judgekeyboard2(kb2);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == spinner2.getSelectedItemPosition()) {
                    SharedPreferences.Editor editor = sp.edit();//获取编辑对象
                    if (spinner3.getSelectedItemPosition() == 1) {
                        editor.putString("keyboard2", "关闭告警");
                        spinner3.setSelection(2);
                    } else {
                        editor.putString("keyboard2", "闹钟");
                        spinner3.setSelection(1);
                    }
                    Toast.makeText(getActivity(), "不能设置与快捷2相同功能", Toast.LENGTH_SHORT).show();
                } else {
                    setkeyboard2(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        aissz.setOnClickListener(this);
        soscs.setOnClickListener(this);
        hypt.setOnClickListener(this);
        jcgx.setOnClickListener(this);
        fence.setOnClickListener(this);
        bjsz.setOnClickListener(this);
        return mView;
    }

    private void findView(View v) {
        spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);
        spinner3 = (Spinner) v.findViewById(R.id.spinner3);
        ll = (LinearLayout) v.findViewById(R.id.setting_ll);
        radioGroup = (RadioGroup) v.findViewById(R.id.genderGroup);
        rb1 = (RadioButton) v.findViewById(R.id.rb1);
        rb2 = (RadioButton) v.findViewById(R.id.rb2);
        rb3 = (RadioButton) v.findViewById(R.id.rb3);
        fence = (TextView) v.findViewById(R.id.fence);
        aissz = (TextView) v.findViewById(R.id.setting_aissz);
        soscs = (TextView) v.findViewById(R.id.setting_soscs);
        hypt = (TextView) v.findViewById(R.id.hypt);
        jcgx = (TextView) v.findViewById(R.id.setting_jcgx);
        bjsz = (TextView) v.findViewById(R.id.setting_bjsz);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_aissz:
                Intent intent1 = new Intent(getActivity(), AisSettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.setting_soscs:
                Intent intent2 = new Intent(getActivity(), SosSettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.fence:
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
                break;
            case R.id.hypt:
                SharedPreferences.Editor editor4 = sp.edit();
                Toast.makeText(getActivity(), "海渔平台" + strfence, Toast.LENGTH_SHORT).show();
                GmsConfig config = new GmsConfig();
                config.setHost("218.85.80.122");
                config.setPort(8110);
//配置设备信息
               final DevInfo dev = new DevInfo();
                dev.setColor((byte) 2);
                dev.setVin("闽AMN001");
                dev.setSimNo("014727407995");
                ConfigUtil.initialize(config, dev);//初始通信及设备对象
                IMultipleListener iMultipleListener = new IMultipleListener() {
                    @Override
                    public void OnConnect(DevInfo devInfo, boolean b, short i) {

                         Logger.i("saaf"+b+"sgs"+i+"sf"+devInfo.getTokenCode()+"dgs"+devInfo.getClientId()+
                                 "ddj"+devInfo.getRemark()+"sg"+devInfo.getSimNo()+"dsg"+devInfo.getVin()+"dsg"+devInfo.getProvinceId()+
                         "see"+devInfo.describeContents()+"ddd"+devInfo.getSeqId());
                   if(i==1) {
                       /*
                       登录
                        */
                       SendObjTask.putData(dev);

                   }

                    }

                    @Override
                    public void OnRecData(MsgInfo msgInfo, CmdParam cmdParam) {
                        Logger.i("saaf"+"dfgf"+msgInfo.getDataArr()+"dsg"+
                        "dgsd"+cmdParam.getCmdId()+"dsg"+cmdParam.getSeqId()+"dg"+cmdParam.getSimNo());

                    }

                    @Override
                    public void OnSendData(CmdParam cmdParam, MsgInfo msgInfo, boolean b) {
                        Logger.i("saaf777"+"dfgf"+msgInfo.getDataArr()+"dsg"+
                                "dgsd"+cmdParam.getCmdId()+"dsg"+cmdParam.getSeqId()+"dg"+cmdParam.getSimNo()+"sgsg"+b);
                    }
                };


                //创建通信服务工作对象
                WorkService workService = new WorkService(iMultipleListener);


                strfence = sp.getString("hypt", "关");
                if (strfence.equals("开")) {
                    hypt.setText("海渔平台：关");
                    editor4.putString("hypt", "关");
                }

                if (strfence.equals("关")) {
                    hypt.setText("海渔平台：开");
                    editor4.putString("hypt", "开");
                }
                editor4.commit();
                break;
            case R.id.setting_jcgx:
                dialog = new CustomDialog2(getActivity());
                String myversion = null;
                try {
                    myversion = getVersionName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.setMessage("当前版本" + myversion + "是否更新版本");


                //判断设置的模式

                dialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //下载APK
                        sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);//指定读取的对象
                        dl = sp.getInt("download", 0);
                        if (dl == 0) {
                            Intent intent = new Intent();
                            intent.putExtra("apk_url", "http://count.liqucn.com/d.php?id=22709&urlos=android&from_type=web");
                            intent.setClass(getActivity(), DownAPKService.class);
                            getActivity().startService(intent);
                        } else {
                            Toast.makeText(getActivity(), "正在下载...", Toast.LENGTH_SHORT).show();
                        }
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
                break;
            case R.id.setting_bjsz:
                Intent intent7 = new Intent(getActivity(), BjSettingActivity.class);
                startActivity(intent7);
                break;
        }

    }

    private class CBOnCheckedChangListenter implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CheckBox cb = (CheckBox) buttonView;
            SharedPreferences.Editor editor = sp.edit();//获取编辑对象
            if (isChecked) {

                Toast.makeText(getActivity(), "开启了" + cb.getText() + "告警", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "取消了" + cb.getText(), Toast.LENGTH_SHORT).show();
            }
            editor.commit();
        }
    }


    private static void setNodeString(String path, String val) {
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
//    public void lpaqfw(View view) {
//        SharedPreferences.Editor editor = sp.edit();
//        gj = sp.getString("lpaqfw", "关");
//        if (gj.equals("开")) {
//            anquanfw.setText("范围报警：关");
//            editor.putString("lpaqfw", "关");
////            setNodeString(BJ, "0");
////            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
////            vibrator.cancel();
////            if (mPlayer != null) {
////                mPlayer.stop();
////                mPlayer.release();
////                mPlayer=null;
////            }
//        }
//        if (gj.equals("关")) {
//            anquanfw.setText("范围报警：开");
//            editor.putString("lpaqfw", "开");
//        }
//        editor.commit();
//    }

    /*
    检查更新
     */
//    public void update(View view) throws Exception {
//        dialog = new CustomDialog2(getActivity());
//        String myversion = getVersionName();
//        dialog.setMessage("当前版本" + myversion + "是否更新版本");
//
//
//        //判断设置的模式
//
//        dialog.setOnPositiveListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //下载APK
//                sp = getActivity().getSharedPreferences("sp", Context.MODE_PRIVATE);//指定读取的对象
//                dl = sp.getInt("download", 0);
//                if (dl == 0) {
//                    Intent intent = new Intent();
//                    intent.putExtra("apk_url", "http://count.liqucn.com/d.php?id=22709&urlos=android&from_type=web");
//                    intent.setClass(getActivity(), DownAPKService.class);
//                    getActivity().startService(intent);
//                } else {
//                    Toast.makeText(getActivity(), "正在下载...", Toast.LENGTH_SHORT).show();
//                }
////                Intent intent = new Intent();
////                intent.putExtra("apk_url", "http://count.liqucn.com/d.php?id=22709&urlos=android&from_type=web");
////                intent.setClass(SettingActivity.this, DownAPKService.class);
////                startService(intent);
//                diaLogger.dismiss();
//            }
//        });
//        dialog.setOnNegativeListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                diaLogger.dismiss();
//            }
//        });
//        dialog.show();
//    }
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
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
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
//        if (position == 2) {
//            editor.putString("keyboard", "AIS开关");
//        }
        if (position == 2) {
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
//        if (position == 2) {
//            editor.putString("keyboard2", "AIS开关");
//        }
        if (position == 2) {
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
//        if (kb.equals("AIS开关")) {
//            spinner2.setSelection(2);
//        }
        if (kb.equals("关闭告警")) {
            spinner2.setSelection(2);
        }
    }

    public void judgekeyboard2(String kb2) {
        if (kb2.equals("计算器")) {
            spinner3.setSelection(0);
        }
        if (kb2.equals("闹钟")) {
            spinner3.setSelection(1);
        }
//        if (kb2.equals("AIS开关")) {
//            spinner3.setSelection(2);
//        }
        if (kb2.equals("关闭告警")) {
            spinner3.setSelection(2);
        }
    }
}
