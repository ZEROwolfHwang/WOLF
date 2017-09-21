package com.yisipu.chartmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yisipu.chartmap.DhActivity;
import com.yisipu.chartmap.R;
import com.yisipu.chartmap.bean.CollectPointBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class DhhxtAdapter extends BaseAdapter {
    private Context context;
    List<String> hx_hdlist;

    public DhhxtAdapter(Context context, List<String> hx_hdlist) {
        this.context = context;
        this.hx_hdlist = hx_hdlist;
    }

    @Override
    public int getCount() {
        return hx_hdlist.size();
    }

    @Override
    public Object getItem(int position) {
        return hx_hdlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dh_hx_list_item, null);
            holder = new MyViewHolder();
            holder.hx = (TextView) convertView.findViewById(R.id.dh_list_item_hx);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final String s = hx_hdlist.get(position);
        holder.hx.setText(s + "");

        holder.hx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((DhActivity) context).huoquhd(collectPointBean.getName());
                ((DhActivity) context).huoquhx(s);
            }
        });

        return convertView;
    }


    public static class MyViewHolder {
        TextView hx;
        //        TextView gps_snr;

    }
}
