package com.yisipu.chartmap.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yisipu.chartmap.CollectActivity;
import com.yisipu.chartmap.R;
import com.yisipu.chartmap.adapter.CoursePointAdapter;
import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.constant.Constant;
import com.yisipu.chartmap.db.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建自定义的dialog，主要学习其实现原理
 * Created by chengguo on 2016/3/22.
 */
public class CoursePointDialog extends Dialog {

//    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private String titleStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;
     private EditText et_name;
    private EditText et_jd;
    private EditText et_wd;
    private Context context;
    ListView ls7;
    String jd;
    String wd;
    String name;
    /*
    收藏界面的index航线
     */
    int index;
    CoursePointAdapter coursePointAdapter;
    List<String> points=new ArrayList<>();
    private ImageButton btn_confirm;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
//    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

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
//     * @param str
//     * @param onYesOnclickListener
     */
//    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
//        if (str != null) {
//            yesStr = str;
//        }
//        this.yesOnclickListener = onYesOnclickListener;
//    }

    public CoursePointDialog(Context context, String course_name, List<String> ls,int index
                             ) {


        super(context, R.style.MyDialog);
        this.context=context;
        this.name=course_name;

        this.points=ls;
        this.index=index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.course_point_dialog, null);

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
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
//        yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (yesOnclickListener != null) {
//                    yesOnclickListener.onYesClick();
//                }
//            }
//        });
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
        et_name.setText(name);
       if(points.size()>0){
           if(coursePointAdapter==null) {
               coursePointAdapter=new CoursePointAdapter(context,points,name,this);
               ls7.setAdapter( coursePointAdapter);
           }
       }
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String course_name=et_name.getText().toString();

                if(TextUtils.isEmpty(course_name)){
                    Toast.makeText(context,"航线名不能为空",Toast.LENGTH_SHORT).show();
               return;
                }
                if(!course_name.equals(name)) {


                    DBManager db = new DBManager(context);
                    CollectPointBean cb = db.getCourseName(course_name);
                    if (cb != null) {
                        Toast.makeText(context, "航线名已存在，请重新命名", Toast.LENGTH_SHORT).show();
                    }
                    List<CollectPointBean> ls=db.getCoursePoints(name);
               for(CollectPointBean cp:ls){
                   cp.setCourse_name(course_name);
                   db.updateCollectBean(cp);
               }
                    ((CollectActivity)context).UpdateCourseName(index,name,course_name);
                   name=course_name;
                    points.clear();
                    DBManager dbManager=new DBManager(context);
                    List<CollectPointBean> l3=dbManager.getCoursePoints(name);
                    for(int i=0;i<l3.size();i++){
                        points.add(i,l3.get(i).getName());
                    }
                    coursePointAdapter=new CoursePointAdapter(context,points,name,CoursePointDialog.this);
                    ls7.setAdapter( coursePointAdapter);
                    coursePointAdapter.notifyDataSetChanged();
//                    points.add();
                    Toast.makeText(context,"航线名修改成功",Toast.LENGTH_SHORT).show();

//                    coursePointAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(context,"航线名修改成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        no = (Button) view.findViewById(R.id.no);
        titleTv = (TextView) view.findViewById(R.id.title);

        et_name=(EditText) view.findViewById(R.id.et_name);
        ls7=(ListView) view.findViewById(R.id.ls_point);
       btn_confirm=(ImageButton) view.findViewById(R.id.ib_save);
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




    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
    /*
  更新dialog修改后list的回调
   */
    public void updateList(int index,String name2){
        points.remove(index);
       points.add(index,name2);
coursePointAdapter.notifyDataSetChanged();
    }



}
