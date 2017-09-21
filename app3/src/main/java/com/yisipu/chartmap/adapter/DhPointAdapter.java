package com.yisipu.chartmap.adapter;

import android.content.Context;
import android.location.GpsSatellite;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yisipu.chartmap.DhActivity;
import com.yisipu.chartmap.R;
import com.yisipu.chartmap.bean.CollectPointBean;
import com.yisipu.chartmap.constant.Constant;

import java.util.List;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class DhPointAdapter extends BaseAdapter {
    private Context context;
    List<CollectPointBean> dh_hdlist;

    public DhPointAdapter(Context context, List<CollectPointBean> dh_hdlist) {
        this.context = context;
        this.dh_hdlist = dh_hdlist;
    }

    @Override
    public int getCount() {
        return dh_hdlist.size();
    }

    @Override
    public Object getItem(int position) {
        return dh_hdlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dh_point_list_item, null);
            holder = new MyViewHolder();
            holder.hd = (TextView) convertView.findViewById(R.id.dh_list_item_hd);
            holder.iv_hd = (ImageView) convertView.findViewById(R.id.iv_hd);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final CollectPointBean collectPointBean = dh_hdlist.get(position);
        holder.hd.setText(collectPointBean.getName());
        if(collectPointBean!=null&&collectPointBean.getImage()!=-1) {
            holder.iv_hd.setBackgroundResource(Constant.hdImage[collectPointBean.getImage()]);
        }
        holder.hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DhActivity) context).huoquhd(collectPointBean.getName());
            }
        });

        return convertView;
    }


    public static class MyViewHolder {
        TextView hd;
        ImageView iv_hd;


        //        TextView gps_snr;

    }
}
