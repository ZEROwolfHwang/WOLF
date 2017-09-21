package com.yisipu.chartmap.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yisipu.chartmap.R;
import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.db.DBManager;
import com.yisipu.chartmap.dialog.CoursePointDialog;
import com.yisipu.chartmap.dialog.UpdateCoursePointDialog;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class CoursePointAdapter extends BaseAdapter {

    private Context context;

    private List<String> list;
    private String course_name;
    CoursePointDialog d2;

    public CoursePointAdapter(Context context, List<String> list, String course_name, CoursePointDialog dl) {
        this.context = context;
//        Collections.sort(list);
        this.list = list;
        this.course_name = course_name;
        this.d2 = dl;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    private UpdateCoursePointDialog dcp;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_course_point, null);
            holder = new ViewHolder();
            holder.tv_point_name = (TextView) convertView.findViewById(R.id.tv_point_name);
            holder.ib = (ImageButton) convertView.findViewById(R.id.ib_update);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        String item = list.get(position);
        holder.tv_point_name.setText(item);
        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager dbManager = new DBManager(context);
                final CollectPointBean sp = dbManager.getCourse(course_name, position);
                if (sp != null) {
                    dcp = new UpdateCoursePointDialog(context, sp.getName(), "" + sp.getLongitude(), "" + sp.getLatitude());
                    dcp.setNoOnclickListener(null, new UpdateCoursePointDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dcp.dismiss();
                        }
                    });
                    dcp.setYesOnclickListener(null, new UpdateCoursePointDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            String name2 = dcp.getEt_name().getText().toString();
                            String wd = dcp.getEt_wd().getText().toString();
                            String jd = dcp.getEt_jd().getText().toString();
                            if (TextUtils.isEmpty(name2)) {
                                Toast.makeText(context, "航点名不能为空", Toast.LENGTH_SHORT).show();

                                return;
                            }
                            if (TextUtils.isEmpty(wd)) {
                                Toast.makeText(context, "纬度不能为空", Toast.LENGTH_SHORT).show();

                                return;
                            }
                            if (TextUtils.isEmpty(jd)) {
                                Toast.makeText(context, "经度不能为空", Toast.LENGTH_SHORT).show();

                                return;
                            }
                            if (Double.parseDouble(jd) > 180) {
                                Toast.makeText(context, "经度数值错误", Toast.LENGTH_SHORT).show();

                                return;
                            }
                            if (Double.parseDouble(wd) > 90) {
                                Toast.makeText(context, "纬度数值错误", Toast.LENGTH_SHORT).show();

                                return;
                            }
                            DBManager dbManager = new DBManager(context);


//                            int i=0;
//                            for( i=0;i<childList.get(0).size();i++){
//                                if(childList.get(0).get(i).equals(name)){
//
//
//                                    break;
//                                }
//                            }
//                            childList.get(0).remove(i);
//                            childList.get(0).add(i,name2);
//                            adpter.notifyDataSetChanged();
                            sp.setName(name2);
                            sp.setLongitude(Double.parseDouble(jd));
                            sp.setLatitude(Double.parseDouble(wd));
//                            sp.setImage(dcp.getPosition());
                            dbManager.updateCollectBean(sp);
                            d2.updateList(position, name2);

                            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();

                            dcp.dismiss();
                        }
                    });
                    dcp.show();
                }
            }
        });
//        holder.itemText.setText(item.getLockName());
        /*
        代表为本船
         */


//        if (item.getDistance() != -1) {
//            DecimalFormat df3 = new DecimalFormat("##0.00");
//
//
//            holder.tv_distance.setText("距离:" + df3.format(item.getDistance() / (1852)) + " nm");
//        }

        return convertView;
    }

    static public class ViewHolder {


        TextView tv_point_name;
        ImageButton ib;
    }

}
