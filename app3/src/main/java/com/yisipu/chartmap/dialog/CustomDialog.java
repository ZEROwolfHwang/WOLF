package com.yisipu.chartmap.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yisipu.chartmap.R;

public class CustomDialog extends Dialog {
	private  Button save_other_name;
	private  EditText et_other_name;
	private EditText  tv_chinese_name;
	private TextView tv_mmsi,tv_name,tv_huhao,tv_imo,tv_weidu,tv_jingdu,tv_hang_xiang,tv_fang_wei,tv_hang_su,tv_distance,
			tv_chan_shou,tv_acc,tv_zhuanxiang,tv_status,tv_ship_length,
			tv_ship_width,tv_country,tv_type;
	    private Button  btn_ok;

	    public CustomDialog(Context context) {
	   
	    	super(context,R.style.CustomDialog);

	        setCustomDialog();
	  
	    }
	private  Button  save_chinese_2;
	    private void setCustomDialog() {
	        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_map_detail, null);
			save_chinese_2=(Button) mView. findViewById(R.id.save_chinese_2);
			save_chinese_2.setVisibility(View.INVISIBLE);
			save_other_name=(Button) mView.findViewById(R.id.save_other_name);
			save_other_name.setVisibility(View.INVISIBLE);
			et_other_name=(EditText) mView.findViewById(R.id.et_other_name);
			et_other_name.setKeyListener(null);
			tv_mmsi=(TextView) mView.findViewById(R.id.tv_mmsi);
			tv_huhao=(TextView) mView.findViewById(R.id.tv_huhao);
			tv_imo=(TextView) mView.findViewById(R.id.tv_imo);
			tv_weidu=(TextView) mView.findViewById(R.id.tv_wei_du);
			tv_jingdu=(TextView)mView. findViewById(R.id.tv_jingdu);
			tv_hang_xiang=(TextView) mView.findViewById(R.id.tv_hang_xiang);
			tv_fang_wei=(TextView)mView. findViewById(R.id.tv_fang_wei);
			tv_hang_su=(TextView) mView.findViewById(R.id.tv_hang_su);
			tv_distance=(TextView) mView.findViewById(R.id.tv_distance);
			tv_chan_shou=(TextView) mView.findViewById(R.id.tv_chuan_shou);
			tv_acc=(TextView) mView.findViewById(R.id.tv_jing_du);
			tv_zhuanxiang=(TextView) mView.findViewById(R.id.tv_zhuan_xing);
			tv_status=(TextView) mView.findViewById(R.id.tv_zhuang_tai);
			tv_ship_length=(TextView) mView.findViewById(R.id.tv_ship_length);
			tv_ship_width=(TextView) mView.findViewById(R.id.tv_ship_width);
			tv_country=(TextView) mView.findViewById(R.id.tv_county);
			tv_type=(TextView) mView.findViewById(R.id.tv_type);
			tv_name=(TextView)mView. findViewById(R.id.tv_name);
			tv_chinese_name=(EditText) mView.findViewById(R.id.tv_chinese_name);
			tv_chinese_name.setKeyListener(null);
					btn_ok=(Button) mView.findViewById(R.id.btn_ok);
	      
	        super.setContentView(mView);
	    }
	     
	
	     
	     @Override
	    public void setContentView(int layoutResID) {
	    	 
	    }


	    public void setContentView(View view, LayoutParams params) {
	    }
	 
	    @Override
	    public void setContentView(View view) {
	    }
	 
	    /**
	     * 确定键监听器
	     * @param listener
	     */ 
	    public void setOnPositiveListener(View.OnClickListener listener){ 
	       btn_ok.setOnClickListener(listener);
	    } 
//	    /**
//	     * 取消键监听器
//	     * @param listener
//	     */
//	    public void setOnNegativeListener(View.OnClickListener listener){
//	        negativeButton.setOnClickListener(listener);
//	    }

	public TextView getTv_chinese_name() {
		return tv_chinese_name;
	}

	public void setTv_chinese_name(EditText tv_chinese_name) {
		this.tv_chinese_name = tv_chinese_name;
	}

	public TextView getTv_mmsi() {
		return tv_mmsi;
	}

	public void setTv_mmsi(TextView tv_mmsi) {
		this.tv_mmsi = tv_mmsi;
	}

	public TextView getTv_name() {
		return tv_name;
	}

	public void setTv_name(TextView tv_name) {
		this.tv_name = tv_name;
	}

	public TextView getTv_huhao() {
		return tv_huhao;
	}

	public void setTv_huhao(TextView tv_huhao) {
		this.tv_huhao = tv_huhao;
	}

	public TextView getTv_imo() {
		return tv_imo;
	}

	public void setTv_imo(TextView tv_imo) {
		this.tv_imo = tv_imo;
	}

	public TextView getTv_weidu() {
		return tv_weidu;
	}

	public void setTv_weidu(TextView tv_weidu) {
		this.tv_weidu = tv_weidu;
	}

	public TextView getTv_jingdu() {
		return tv_jingdu;
	}

	public void setTv_jingdu(TextView tv_jingdu) {
		this.tv_jingdu = tv_jingdu;
	}

	public TextView getTv_hang_xiang() {
		return tv_hang_xiang;
	}

	public void setTv_hang_xiang(TextView tv_hang_xiang) {
		this.tv_hang_xiang = tv_hang_xiang;
	}

	public TextView getTv_fang_wei() {
		return tv_fang_wei;
	}

	public void setTv_fang_wei(TextView tv_fang_wei) {
		this.tv_fang_wei = tv_fang_wei;
	}

	public TextView getTv_hang_su() {
		return tv_hang_su;
	}

	public void setTv_hang_su(TextView tv_hang_su) {
		this.tv_hang_su = tv_hang_su;
	}

	public TextView getTv_distance() {
		return tv_distance;
	}

	public void setTv_distance(TextView tv_distance) {
		this.tv_distance = tv_distance;
	}

	public TextView getTv_chan_shou() {
		return tv_chan_shou;
	}

	public void setTv_chan_shou(TextView tv_chan_shou) {
		this.tv_chan_shou = tv_chan_shou;
	}

	public TextView getTv_acc() {
		return tv_acc;
	}

	public void setTv_acc(TextView tv_acc) {
		this.tv_acc = tv_acc;
	}

	public TextView getTv_zhuanxiang() {
		return tv_zhuanxiang;
	}

	public void setTv_zhuanxiang(TextView tv_zhuanxiang) {
		this.tv_zhuanxiang = tv_zhuanxiang;
	}

	public TextView getTv_status() {
		return tv_status;
	}

	public void setTv_status(TextView tv_status) {
		this.tv_status = tv_status;
	}

	public TextView getTv_ship_length() {
		return tv_ship_length;
	}

	public void setTv_ship_length(TextView tv_ship_length) {
		this.tv_ship_length = tv_ship_length;
	}

	public TextView getTv_ship_width() {
		return tv_ship_width;
	}

	public void setTv_ship_width(TextView tv_ship_width) {
		this.tv_ship_width = tv_ship_width;
	}

	public TextView getTv_country() {
		return tv_country;
	}

	public void setTv_country(TextView tv_country) {
		this.tv_country = tv_country;
	}

	public TextView getTv_type() {
		return tv_type;
	}

	public void setTv_type(TextView tv_type) {
		this.tv_type = tv_type;
	}

	public Button getSave_other_name() {
		return save_other_name;
	}

	public void setSave_other_name(Button save_other_name) {
		this.save_other_name = save_other_name;
	}

	public EditText getEt_other_name() {
		return et_other_name;
	}

	public void setEt_other_name(EditText et_other_name) {
		this.et_other_name = et_other_name;
	}

	public Button getSave_chinese_2() {
		return save_chinese_2;
	}

	public void setSave_chinese_2(Button save_chinese_2) {
		this.save_chinese_2 = save_chinese_2;
	}
}
	

