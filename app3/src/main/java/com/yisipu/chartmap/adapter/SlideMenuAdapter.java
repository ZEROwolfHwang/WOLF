package com.yisipu.chartmap.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yisipu.chartmap.R;

import java.util.ArrayList;

public class SlideMenuAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> menuNameList;
	private int selectedPosition = 0;
	
	public SlideMenuAdapter(Context context , ArrayList<String> list){
		this.context = context;
		this.menuNameList = list;
	}
	
	public void setSelectedPosition(int position){
		this.selectedPosition = position;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuNameList.size();
	}

	@Override
	public Object getItem(int position) {
		return menuNameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.slidemenu_item, null);
			viewHolder = new ViewHolder();
			viewHolder.menuItemName = (TextView)convertView.findViewById(R.id.menu_name);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.menuItemName.setText(menuNameList.get(position));
		viewHolder.menuItemName.setBackgroundColor(Color.parseColor("#75313335"));
//		if(selectedPosition == position)
//			convertView.setBackgroundResource(R.drawable.bg);
//		else
//			convertView.setBackgroundColor(Color.parseColor("#75293237"));
		return convertView;
	}

	class ViewHolder{
		TextView menuItemName;
	}
	
}
