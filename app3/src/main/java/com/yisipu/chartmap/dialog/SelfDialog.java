package com.yisipu.chartmap.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yisipu.chartmap.R;

/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class SelfDialog extends Dialog {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;
     private EditText et_w_d;
    private EditText et_w_f;
    private EditText et_w_m;
    private EditText et_j_d;
    private EditText et_j_f;
    private EditText et_j_m;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public SelfDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.free_exercise_sure_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
//        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
        if (messageStr != null) {
            messageTv.setText(messageStr);
        }
        //如果设置按钮的文字
        if (yesStr != null) {
            yes.setText(yesStr);
        }
        if (noStr != null) {
            no.setText(noStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        titleTv = (TextView) findViewById(R.id.title);

        et_w_d=(EditText) findViewById(R.id.et_w_d);
          et_w_f=(EditText) findViewById(R.id.et_w_f);
        et_w_m=(EditText) findViewById(R.id.et_w_m);
       et_j_d=(EditText) findViewById(R.id.et_j_d);
        et_j_f=(EditText) findViewById(R.id.et_j_f);
        et_j_m=(EditText) findViewById(R.id.et_w_m);

    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    public EditText getEt_w_d() {
        return et_w_d;
    }

    public void setEt_w_d(EditText et_w_d) {
        this.et_w_d = et_w_d;
    }

    public EditText getEt_w_f() {
        return et_w_f;
    }

    public void setEt_w_f(EditText et_w_f) {
        this.et_w_f = et_w_f;
    }

    public EditText getEt_j_f() {
        return et_j_f;
    }

    public void setEt_j_f(EditText et_j_f) {
        this.et_j_f = et_j_f;
    }

    public EditText getEt_j_d() {
        return et_j_d;
    }

    public void setEt_j_d(EditText et_j_d) {
        this.et_j_d = et_j_d;
    }

    public EditText getEt_w_m() {
        return et_w_m;
    }

    public void setEt_w_m(EditText et_w_m) {
        this.et_w_m = et_w_m;
    }

    public EditText getEt_j_m() {
        return et_j_m;
    }

    public void setEt_j_m(EditText et_j_m) {
        this.et_j_m = et_j_m;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
}
