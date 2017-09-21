package com.yisipu.chartmap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.constant.Constant;

import java.util.List;


/**
 * Created by Administrator on 2016/7/11.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    private LinearLayout ll_title_content, ll_title_right, ll_title_left;
    private LinearLayout ll_title;
    //内容
    private LinearLayout ll_content;
    private ViewGroup.LayoutParams layoutParams;
    private ImageView iv_back;
    private TextView tv_text;
    private ImageView iv_title;
    private TextView tv_right;
    private Intent mIntent;
    private boolean isSetBaseContent = true;
    private TextView gpsjw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isSetBaseContent) {
            setContentView(R.layout.activity_base);
            findView();
        }
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();

        if(isOpen&& this.getCurrentFocus()!=null) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
//动态注册广播接收器
        registerBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    /**
     * 广播接收器
     *
     * @author len
     */
    public BroadcastReceiver AisBroadcast = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI
//            int progress = intent.getIntExtra("progress", 0);
//            mProgressBar.setProgress(progress);

            String a = intent.getStringExtra("selectMMsi");
            if (a != null) {
                receiver(a);
            }
//            String b = intent.getStringExtra("gps");
//            if (b != null) {
//               gps(b);
//
//            }
            Gpsbean gpsbean = new Gpsbean();
            gpsbean = (Gpsbean) intent.getSerializableExtra("gps");
            if (gpsbean != null) {
                gpsreceiver(gpsbean);
            }
            Jdbean jdbean = new Jdbean();
            jdbean = (Jdbean) intent.getSerializableExtra("pdop");
            if (jdbean != null) {
                hdopreceiver (jdbean);
            }


        String  toPager= intent.getStringExtra("toPager");
            if(toPager!=null&& !TextUtils.isEmpty(toPager)){

               int  isRuingBaoJing= intent.getIntExtra("isRuingBaoJing",-1);
                if(isRuingBaoJing==1) {
                    List<ShipBean> ls = (List<ShipBean>) intent.getSerializableExtra("baoJingShip");
                    toPager(toPager,isRuingBaoJing,ls);
                }else if(isRuingBaoJing==0){
                    toPager(toPager,isRuingBaoJing,null);
                }else{
                    toPager(toPager,-1,null);
                }

            }

        }

    };
    abstract void toPager(String str, int isRunningBaojing, List<ShipBean> ls);

    abstract void receiver(String str);

    abstract void gpsreceiver(Gpsbean gpsbean);//获取GPS经纬度的抽象方法

    abstract void hdopreceiver(Jdbean jdbean);//位置精度和水平精度的抽象方法

    public void findView() {
        ll_title_content = (LinearLayout) findViewById(R.id.ll_title_content);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_text = (TextView) findViewById(R.id.tv_title);
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        iv_title = (ImageView) findViewById(R.id.iv_title);
        gpsjw = (TextView) findViewById(R.id.tv_jing_weidu);
    }

    /*
    设置中间标题为图片
     */
    public void setDrawable(int res) {
        tv_text.setVisibility(View.GONE);
        iv_title.setBackgroundResource(res);
        iv_title.setVisibility(View.VISIBLE);


    }

    public void setRightString(String text) {
        tv_right.setText(text);
    }

    public void hideTitleLeft() {
        ll_title_left.setVisibility(View.INVISIBLE);
    }

    /*
    隐藏右标题
     */
    public void hideTitleRight() {
        ll_title_right.setVisibility(View.INVISIBLE);
    }

    /*
      显示标题
       */
    public void showTitle() {
        ll_title.setVisibility(View.VISIBLE);
    }

    /*
     隐藏标题
      */
    public void hideTitle() {
        ll_title.setVisibility(View.GONE);
    }


    /*
     设置标题名称
      */
    public void setTitleText(String str) {
        iv_title.setVisibility(View.GONE);
        tv_text.setVisibility(View.VISIBLE);
        tv_text.setText(str);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 设置layoutView内容布局
     */
    public void setLayoutView(int id) {
        LayoutInflater inflater = LayoutInflater.from(BaseActivity.this);
        View view = inflater.inflate(id, null);
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ll_content.addView(view, layoutParams);
    }

    //
    @Override
    protected void onDestroy() {

        //注销广播
        unregisterReceiver(AisBroadcast);// 注销接受消息广播
        super.onDestroy();
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Aisaction);

        registerReceiver(AisBroadcast, intentFilter);// 注册接受消息广播


    }

    //返回键
    public void onBackPress(View view) {
        finish();
    }
}
