package com.yisipu.chartmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yisipu.chartmap.R;
import com.yisipu.chartmap.bean.ShipBean;
import com.yisipu.chartmap.dialog.IsCloseWarm;
import com.yisipu.chartmap.servicer.MyDataServicer;
import com.yisipu.chartmap.utils.ShipTypeUtils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/5/9.
 */
public class AisListAdapter extends BaseAdapter {

    private Context context;

    private List<ShipBean> list;


    public AisListAdapter(Context context, List<ShipBean> list) {
        this.context = context;
        Collections.sort(list);
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }
   IsCloseWarm isCloseWarm=null;
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_ais, null);
            holder = new ViewHolder();
            holder.tv_is_my = (TextView) convertView.findViewById(R.id.is_my);
            holder.tv_mmsi = (TextView) convertView.findViewById(R.id.tv_mmsi);
//            holder.tv_ship_name = (TextView) convertView.findViewById(R.id.tv_ship_name);
//            holder.tv_hang_su = (TextView) convertView.findViewById(R.id.tv_hang_su);
//            holder.tv_fang_wei = (TextView) convertView.findViewById(R.id.tv_fang_wei);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.tv_cx= (TextView) convertView.findViewById(R.id.tv_cx);
            holder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
//            holder.tv_ship_chinese_name = (TextView) convertView.findViewById(R.id.tv_ship_chinese_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        ShipBean item = list.get(position);
//        if (!list.get(0).getMyShip()) {
//
//            holder.tv_index.setText("序号:" + (position + 1));
//        } else {
//
//            holder.tv_index.setText("序号:" + position);
//        }

        ShipBean item = list.get(position);
        if (!list.get(0).getMyShip()) {

            holder.tv_index.setText(""+(position + 1));

        } else {

            holder.tv_index.setText(""+position);
            if(item.getIsCollision()==1){
                holder.tv_index.setBackgroundColor(0xffff0000);
                holder.tv_index.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isCloseWarm=new IsCloseWarm(context);
                        isCloseWarm.setOnNegativeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isCloseWarm.dismiss();
                            }
                        });
                        isCloseWarm.setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyDataServicer.cancelBaojing(context);
                                isCloseWarm.dismiss();

                            }
                        });
                        isCloseWarm.show();
                    }
                });
            }else{
                holder.tv_index.setBackgroundColor(0xffffffff);
                holder.tv_index.setOnClickListener(null);

            }
        }
//        holder.itemText.setText(item.getLockName());
        /*
        代表为本船
         */
        if (item.getMyShip()) {
            holder.tv_is_my.setVisibility(View.VISIBLE);
//            holder.tv_fang_wei.setVisibility(View.GONE);
//            holder.tv_distance.setVisibility(View.GONE);
            holder.tv_index.setVisibility(View.GONE);
        } else {
            holder.tv_is_my.setVisibility(View.GONE);
            holder.tv_index.setVisibility(View.VISIBLE);
//            holder.tv_fang_wei.setVisibility(View.VISIBLE);
//            holder.tv_distance.setVisibility(View.VISIBLE);
        }

        if(!String.valueOf(item.getMMSI()).equals("999999999")||!item.getMyShip()) {

            holder.tv_mmsi.setText(""+item.getMMSI());
        }else {
            holder.tv_mmsi.setText("" + "000000000");

        }
//        holder.tv_mmsi.setText(""+item.getMMSI());
        String cx= ShipTypeUtils.getShipTypeString(item.getType());
        holder.tv_cx.setText(""+cx);
//        holder.tv_mmsi.setText("MMSI:" + item.getMMSI());
//        holder.tv_ship_name.setText("英文船名:" + item.getEnglishName());
        String country = item.getMMSI() + "";
        String a = country.substring(0, 3);

//        if (a.equals("412") || a.equals("413")) {
//            holder.tv_ship_chinese_name.setText("中文船名:" + item.getChineseName());
//            holder.tv_ship_chinese_name.setVisibility(View.VISIBLE);
//        } else {
//            holder.tv_ship_chinese_name.setVisibility(View.GONE);
//        }
//        DecimalFormat df1 = new DecimalFormat("##0.0");
//        if (item.getSog() != -1 && item.getSog() != 1023) {
//            holder.tv_hang_su.setText("航速:" + df1.format(item.getSog() / (10.0)) + " Kn");
//        }
//        if (item.getFangwei() != -1) {
//            if (item.getFangwei() < 0) {
//                item.setFangwei(item.getFangwei() + 360);
//            }
//            DecimalFormat df2 = new DecimalFormat("##0.0");
//            if (!df2.format(item.getFangwei()).equals("NaN")) {
//                holder.tv_fang_wei.setText("方位:" + df2.format(item.getFangwei()) + " °");
//            }
//        }
        if (item.getDistance() != -1&&item.getLatitude()!=-1&&item.getLongitude()!=-1&&item.getLongitude()<=180&&item.getLatitude()<=90) {
            if (item.getMyShip()){
                holder.tv_distance.setText("");
            }else {
            DecimalFormat df3 = new DecimalFormat("##0.00");
            holder.tv_distance.setText(df3.format(item.getDistance() / (1852)) + " nm");
            }
        }else{
            holder.tv_distance.setText("");
    }
//        if (item.getDistance() != -1) {
//            DecimalFormat df3 = new DecimalFormat("##0.00");
//
//
//            holder.tv_distance.setText("距离:" + df3.format(item.getDistance() / (1852)) + " nm");
//        }

        return convertView;
    }

    static public class ViewHolder {


        TextView tv_is_my;
        TextView tv_mmsi;
        TextView tv_ship_name;
        TextView tv_hang_su;
        TextView tv_fang_wei;
        TextView tv_distance;
        TextView tv_index;
        TextView tv_cx;
        TextView tv_ship_chinese_name;
    }

}
