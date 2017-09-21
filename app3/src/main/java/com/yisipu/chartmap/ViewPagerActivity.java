package com.yisipu.chartmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.orhanobut.logger.Logger;
import com.yisipu.chartmap.adapter.PagerAdapter;
import com.yisipu.chartmap.bean.Gpsbean;
import com.yisipu.chartmap.bean.Jdbean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.fragment.FragmentGPS;
import com.yisipu.chartmap.ui.MyViewPager;

import java.util.List;

public class ViewPagerActivity extends SerialPortActivity {


    private MyViewPager pager;
    private PagerAdapter adapter=null;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        initView();// 实例化控件
        initDate();// 初始化内容
    }
    //singleTask加载模式
    @Override
    protected void onNewIntent(Intent intent1) {
        super.onNewIntent(intent1);
        setIntent(intent1); //这一句必须的，否则Intent无法获得最新的数据
        Intent intent = getIntent();
        String s = intent.getStringExtra("jinrufg");
        if (s != null) {
            if (s.equals("1")&&pager.getCurrentItem()!=1) {
                setCurrentPage(1);// 默认选中样式
            }
            if (s.equals("2")&&pager.getCurrentItem()!=2) {
                setCurrentPage(2);// 默认选中样式
            }
        }
        String StringE = intent.getStringExtra("extra");
        if (StringE != null) {
            if (StringE.equals("1")) {
                setCurrentPage(1);// 默认选中样式
            }
            if (StringE.equals("2")) {
                setCurrentPage(2);// 默认选中样式
            }
            if (StringE.equals("3")) {
                setCurrentPage(3);// 默认选中样式
            }
            if (StringE.equals("4")) {
                setCurrentPage(4);// 默认选中样式
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();

        if(isOpen&& this.getCurrentFocus()!=null) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

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

    @Override
    void toPager(String str, int isRunningBaojing, List<ShipBean> ls) {
        Logger.i("sadk"+str);
        if (str != null && !TextUtils.isEmpty(str)) {
            if (str.equals("ais")) {
                if(adapter==null) {
                    adapter = new PagerAdapter(getSupportFragmentManager());
                    pager.setAdapter(adapter);
                    pager.setOffscreenPageLimit(5);
                    // 监听页面变化
                    pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int arg0) {
                            if (arg0 == 0) {
                                Intent intent = new Intent(ViewPagerActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            if (arg0 == 5) {
                                Intent intent = new Intent(ViewPagerActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            setCurrentPage(arg0);
                        }

                        @Override
                        public void onPageScrolled(int arg0, float arg1, int arg2) {
                            // TODO Auto-generated method stub
                            Logger.d("arg0+++", "" + arg0);
                            Logger.d("arg1+++", "" + arg1);
                            Logger.d("arg2+++", "" + arg2);
                        }


                        @Override
                        public void onPageScrollStateChanged(int arg0) {
                        }
                    });
                }
                if(pager.getCurrentItem()!=1) {
                    setCurrentPage(1);
                }

            } else if (str.equals("gps")) {
                if(adapter==null) {
                    adapter = new PagerAdapter(getSupportFragmentManager());
                    pager.setAdapter(adapter);
                    pager.setOffscreenPageLimit(5);
                    // 监听页面变化
                    pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int arg0) {
                            // TODO Auto-generated method stub

                            if (arg0 == 0) {
                                Intent intent = new Intent(ViewPagerActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            if (arg0 == 5) {
                                Intent intent = new Intent(ViewPagerActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            setCurrentPage(arg0);
                        }

                        @Override
                        public void onPageScrolled(int arg0, float arg1, int arg2) {
                            // TODO Auto-generated method stub
                            Logger.d("arg0+++", "" + arg0);
                            Logger.d("arg1+++", "" + arg1);
                            Logger.d("arg2+++", "" + arg2);
                        }


                        @Override
                        public void onPageScrollStateChanged(int arg0) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
                if(pager.getCurrentItem()!=2) {
                    setCurrentPage(2);
                }

            }

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
        if (jdbean != null) {
            FragmentGPS.getInstance().getgpsjd(jdbean);
        }
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {

    }

    private void initDate() {
        // 必须继承FragmentActivity才能用getSupportFragmentManager()；
        adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(5);
        // 监听页面变化
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

                if (arg0 == 0) {
                    Intent intent = new Intent(ViewPagerActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (arg0 == 5) {
                    Intent intent = new Intent(ViewPagerActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                setCurrentPage(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                Logger.d("arg0+++", "" + arg0);
                Logger.d("arg1+++", "" + arg1);
                Logger.d("arg2+++", "" + arg2);
            }


            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        Intent intent = getIntent();
        String s = intent.getStringExtra("jinrufg");
        if (s != null) {
            if (s.equals("1")&&pager.getCurrentItem()!=1) {
                setCurrentPage(1);// 默认选中样式
            }
            if (s.equals("2")&&pager.getCurrentItem()!=2) {
                setCurrentPage(2);// 默认选中样式
            }
        }
        String StringE = intent.getStringExtra("extra");
        if (StringE != null) {
            if (StringE.equals("1")) {
                setCurrentPage(1);// 默认选中样式
            }
            if (StringE.equals("2")) {
                setCurrentPage(2);// 默认选中样式
            }
            if (StringE.equals("3")) {
                setCurrentPage(3);// 默认选中样式
            }
            if (StringE.equals("4")) {
                setCurrentPage(4);// 默认选中样式
            }
        }

    }

    /**
     * 页面与head标签一致（可以设置head的按钮样式）
     *
     * @param arg0
     */
    private void setCurrentPage(int arg0) {
        switch (arg0) {
            case 0:
                pager.setCurrentItem(0);// 默认选中
                break;
            case 1:
                pager.setCurrentItem(1);
                break;
            case 2:
                pager.setCurrentItem(2);
                break;
            case 3:
                pager.setCurrentItem(3);

                break;
            case 4:

                pager.setCurrentItem(4);
                break;
            case 5:
                pager.setCurrentItem(5);
                break;
            default:
                break;
        }
    }

    /**
     * 实例化控件
     */
    private void initView() {
        pager = (MyViewPager) findViewById(R.id.vp_content);
    }
}
