package com.yisipu.chartmap.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yisipu.chartmap.CollectActivity;
import com.yisipu.chartmap.R;
import com.yisipu.chartmap.adapter.CoursePointAdapter;
import com.yisipu.chartmap.adapter.DhPointAdapter;
import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class DhCoursePointDialog extends Dialog {

    //    private Button yes;//确定按钮

    private ImageView fh;//取消按钮
    //    private TextView titleTv;//消息标题文本
//    private TextView messageTv;//消息提示文本
//    private String titleStr;//从外界设置的title文本
//    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;
    //    private EditText et_name;
//    private EditText et_jd;
//    private EditText et_wd;
    private Context context;
    ListView ls7;
    String jd;
    String wd;
    String name;
    /*
    收藏界面的index航线
     */
//    int index;
//    CoursePointAdapter coursePointAdapter;
    DhPointAdapter dhPointAdapter;
    List<CollectPointBean> points = new ArrayList<>();
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器

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


    public DhCoursePointDialog(Context context, List<CollectPointBean> ls) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.points = ls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.dh_point_dialog, null);
        setContentView(view);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView(view);
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        fh.setOnClickListener(listener);
    }

    private void initEvent() {
//        //设置确定按钮被点击后，向外界提供监听
//        yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (yesOnclickListener != null) {
//                    yesOnclickListener.onYesClick();
//                }
//            }
//        });
//        //设置取消按钮被点击后，向外界提供监听
        fh.setOnClickListener(new View.OnClickListener() {
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
//        et_name.setText(name);
        if (points.size() > 0) {
            if (dhPointAdapter == null) {
//                coursePointAdapter = new CoursePointAdapter(context, points, name, this);
                dhPointAdapter = new DhPointAdapter(context, points);

                ls7.setAdapter(dhPointAdapter);
            }
        }

        //如果用户自定了title和message
//        if (titleStr != null) {
//            titleTv.setText(titleStr);
//        }
//        if (messageStr != null) {
//            messageTv.setText(messageStr);
//        }
        //如果设置按钮的文字
//        if (yesStr != null) {
//            yes.setText(yesStr);
//        }
//        if (noStr != null) {
//            no.setText(noStr);
//        }
    }

    /**
     * 初始化界面控件
     */
    private void initView(View view) {
//        yes = (Button) view.findViewById(R.id.yes);
        fh = (ImageView) view.findViewById(R.id.dh_point_dialog_close);
//        titleTv = (TextView) view.findViewById(R.id.title);
//
//        et_name = (EditText) view.findViewById(R.id.et_name);
        ls7 = (ListView) view.findViewById(R.id.dh_ls_point);
//        btn_confirm = (ImageButton) view.findViewById(R.id.ib_save);
    }

    //    /**
//     * 从外界Activity为Dialog设置标题
//     *
//     * @param title
//     */
//    public void setTitle(String title) {
//        titleStr = title;
//    }
//
//    /**
//     * 从外界Activity为Dialog设置dialog的message
//     *
//     * @param message
//     */
//    public void setMessage(String message) {
//        messageStr = message;
//    }
//
//
//    /**
//     * 设置确定按钮和取消被点击的接口
//     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

//    /*
//  更新dialog修改后list的回调
//   */
//    public void updateList(int index, String name2) {
//        points.remove(index);
//        points.add(index, name2);
//        coursePointAdapter.notifyDataSetChanged();
//    }
}
