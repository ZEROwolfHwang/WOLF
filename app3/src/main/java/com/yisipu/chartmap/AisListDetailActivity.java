package com.yisipu.chartmap;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.utils.ConvertUtils;
import com.yisipu.chartmap.utils.PinyinTool;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Ais设置Activity
 * Created by Administrator on 2016/7/29.
 */
public class AisListDetailActivity extends SerialPortActivity {
    private ShipBean sb;
    private  Button save_other_name;
    private  Button  save_chinese_2;

    private EditText tv_chinese_name;
    private TextView tv_mmsi,tv_name,tv_huhao,tv_imo,tv_weidu,tv_jingdu,tv_hang_xiang,tv_fang_wei,tv_hang_su,tv_distance,
    tv_chan_shou,tv_acc,tv_zhuanxiang,tv_status,tv_ship_length,
    tv_ship_width,tv_country,tv_type;
    private  EditText et_other_name;
    private ConvertUtils convertUtils=new ConvertUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_ais_detail);
        sb=(ShipBean) getIntent().getSerializableExtra("ShipBean");
//        Logger.i("aaaaaa"+sb.getMMSI());

        initTitle();

        initData();


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

    public void initData(){
        save_chinese_2=(Button) findViewById(R.id.save_chinese_2);
        save_other_name=(Button) findViewById(R.id.save_other_name);
        et_other_name=(EditText) findViewById(R.id.et_other_name);
       tv_mmsi=(TextView) findViewById(R.id.tv_mmsi);
        tv_huhao=(TextView) findViewById(R.id.tv_huhao);
        tv_imo=(TextView) findViewById(R.id.tv_imo);
        tv_weidu=(TextView) findViewById(R.id.tv_wei_du);
        tv_jingdu=(TextView) findViewById(R.id.tv_jingdu);
        tv_hang_xiang=(TextView) findViewById(R.id.tv_hang_xiang);
        tv_fang_wei=(TextView) findViewById(R.id.tv_fang_wei);
        tv_hang_su=(TextView) findViewById(R.id.tv_hang_su);
        tv_distance=(TextView) findViewById(R.id.tv_distance);
        tv_chan_shou=(TextView) findViewById(R.id.tv_chuan_shou);
        tv_acc=(TextView) findViewById(R.id.tv_jing_du);
        tv_zhuanxiang=(TextView) findViewById(R.id.tv_zhuan_xing);
        tv_status=(TextView) findViewById(R.id.tv_zhuang_tai);
        tv_ship_length=(TextView) findViewById(R.id.tv_ship_length);
        tv_ship_width=(TextView) findViewById(R.id.tv_ship_width);
        tv_country=(TextView) findViewById(R.id.tv_county);
        tv_type=(TextView) findViewById(R.id.tv_type);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_chinese_name=(EditText) findViewById(R.id.tv_chinese_name);

        if(!String.valueOf(sb.getMMSI()).equals("999999999")||!sb.getMyShip()) {

            tv_mmsi.setText("MMSI:" + sb.getMMSI());
        }else {
            tv_mmsi.setText("MMSI:" + "000000000");
        }
        if(null!=sb.getHuhao()) {
            tv_huhao.setText("呼号:" + sb.getHuhao());
        }
        if(null!=sb.getIMO()) {
            tv_imo.setText("IMO:" + sb.getIMO());
        }
        if(sb.getLatitude()<=90&&!(sb.getLatitude()==0&&sb.getLongitude()==0)&&sb.getLatitude()!=-1) {

            String weidu=convertUtils.Latitude(sb.getLatitude());
            tv_weidu.setText("纬度:" + weidu);
        }
        if(sb.getLongitude()<=180&&!(sb.getLatitude()==0&&sb.getLongitude()==0)&&sb.getLongitude()!=-1) {
            String jingdu=convertUtils.Longitude(sb.getLongitude());
            tv_jingdu.setText("经度:" + jingdu);
        }
        if(!(sb.getCog()>=3600)&&sb.getCog()!=-1) {
            tv_hang_xiang.setText("航向:" + sb.getCog() / (10.0));
        }
        DecimalFormat df = new DecimalFormat("##0.0");
        if(sb.getSog()!=1023&&sb.getSog()!=-1 ) {
            tv_hang_su.setText("航速:" + df.format(sb.getSog() / (10.0)) + " Kn");
        }
        if(sb.getReal_sudu()!=511&&sb.getReal_sudu()!=-1) {

            tv_chan_shou.setText("船艏:" + sb.getReal_sudu());
        }
        if(sb.getPrecision()!=-1) {
            tv_acc.setText("精度:" + sb.getPrecision());
        }
        if(sb.getRot()!=-1) {
            tv_zhuanxiang.setText("转向:" + sb.getRot());
        }
        if(sb.getStatus()!=-1) {
            tv_status.setText("状态:" + sb.getStatus());
        }
        if(-1!=sb.getDimBow()&&-1!=sb.getDimStern()){
            tv_ship_length.setText("船长:"+(sb.getDimBow()+sb.getDimStern()));
        }
        if(-1!=sb.getDimStarboard()&&-1!=sb.getDimPort()){

            tv_ship_width.setText("船宽:"+(sb.getDimStarboard()+sb.getDimPort()));
        }
        if(sb.getType()!=-1&&sb.getType()!=0) {
//            String a=ShipTypeUtils.getShipTypeString(sb.getType());
            tv_type.setText("类型:" + sb.getType());
        }
        tv_name.setText("英文船名:"+sb.getEnglishName());
        if(sb.getDistance()!=-1&&sb.getMyShip()!=true&&sb.getLatitude()<=90&&sb.getLongitude()<=180){
            DecimalFormat df3 = new DecimalFormat("##0.00");
            tv_distance.setText("距离:"+df3.format(sb.getDistance()/(1852))+" nm");

        }
        if(sb.getFangwei()!=-1&&sb.getMyShip()!=true&&sb.getLatitude()<=90&&sb.getLongitude()<=180){
            if(sb.getFangwei()<0){

                sb.setFangwei(sb.getFangwei()+360);
            }
            DecimalFormat df2 = new DecimalFormat("##0.0");

            tv_fang_wei.setText("方位:"+df2.format(sb.getFangwei())+" °");
        }
        if(sb.getCountry()!=-1) {
            String name = "country_type_" + sb.getCountry();
            Resources rc = getResources();
            int id = rc.getIdentifier(name, "string", "com.yisipu.chartmap");
            if (id == 0) {
            } else {

                tv_country.setText("国家:" + getString(id));
            }
        }
       final PinyinTool p=new PinyinTool();
        if(!sb.getOtherName().equals("")) {
            tv_chinese_name.setText(sb.getOtherName());
            save_chinese_2.setVisibility(View.INVISIBLE);
            tv_chinese_name.setKeyListener(null);
        }else{
            save_chinese_2.setVisibility(View.VISIBLE);
            if(!sb.getChineseNameChange().equals("")&&!sb.getChineseName().equals("")){
               try {
                  String a= p.toPinYin(sb.getChineseNameChange());
                   String b= p.toPinYin(sb.getChineseName());
                   if(a.equals(b)){
                       tv_chinese_name.setText(sb.getChineseNameChange());
                   }else {
                       tv_chinese_name.setText(sb.getChineseName());
                   }
               }catch (Exception exp){
                   if (!sb.getChineseName().equals("")) {

                       tv_chinese_name.setText( sb.getChineseName());
                   }

               }



            }else{
                tv_chinese_name.setText(sb.getChineseName());
            }

        }
        save_chinese_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String other=tv_chinese_name.getText().toString().trim();
                if(sb.getChineseName().equals("")) {
                    Toast.makeText(AisListDetailActivity.this,"暂无英文船名不可修改中文船名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(other)){
                    Toast.makeText(AisListDetailActivity.this,"中文船名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if(!sb.getChineseName().equals("")) {
                        String a = p.toPinYin(other);
                        String b = p.toPinYin(sb.getChineseName());
                        if (a.equals(b)) {
                            DBManager db = new DBManager(AisListDetailActivity.this);
                            sb.setChineseNameChange(other);
                            db.addShipBean(sb);
                            Toast.makeText(AisListDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AisListDetailActivity.this, "修改的中文船名必须为同音字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else{
                        return;
                    }
                }catch (Exception exp){
                    Toast.makeText(AisListDetailActivity.this,"保存失败",Toast.LENGTH_SHORT).show();

                }

            }
        });
        if(!sb.getOtherName().equals("")) {
            et_other_name.setText(sb.getOtherName());
        }
        save_other_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String other=et_other_name.getText().toString().trim();
                if(TextUtils.isEmpty(other)){
                    Toast.makeText(AisListDetailActivity.this,"别名不能为空",Toast.LENGTH_SHORT).show();
                   return;
                }
                DBManager db=new DBManager(AisListDetailActivity.this);
              sb.setOtherName(other);
                db.addShipBean(sb);
                Toast.makeText(AisListDetailActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void initTitle(){
        hideTitleRight();

        setTitleText("AIS列表详情");
        showTitle();
    }
    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }

}
