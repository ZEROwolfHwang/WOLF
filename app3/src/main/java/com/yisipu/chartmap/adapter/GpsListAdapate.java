package com.yisipu.chartmap.adapter;

import android.content.Context;
import android.location.GpsSatellite;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yisipu.chartmap.R;
import com.yisipu.chartmap.bean.MyGpsSatellite;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2 0002.
 */
public class GpsListAdapate extends BaseAdapter {
    private Context context;
    List<MyGpsSatellite> gpsList;

    public GpsListAdapate(List< MyGpsSatellite> gpsList, Context context) {

        Collections.sort(gpsList);
        this.gpsList = gpsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gpsList.size();
    }

    @Override
    public Object getItem(int position) {
        return gpsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_gps2, null);
            holder = new MyViewHolder();
            holder.gps_id = (TextView) convertView.findViewById(R.id.gps_id);
//    holder.gps_snr= (TextView) convertView.findViewById(R.id.gps_snr);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.pb_progressbar);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        MyGpsSatellite satellite = gpsList.get(position);
//            holder.gps_id.setText("卫星编号：" + satellite.getPrn());
        holder.gps_id.setText("" + satellite.getPrn());
//        holder.gps_snr.setText("信噪比："+satellite.getSnr());
            int snr = (int) satellite.getSnr();
            holder.progressBar.setProgress(snr);
        return convertView;
    }

    public static class MyViewHolder {
        TextView gps_id;
        //        TextView gps_snr;
        ProgressBar progressBar;
    }
}
