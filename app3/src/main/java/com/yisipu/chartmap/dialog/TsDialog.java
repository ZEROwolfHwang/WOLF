package com.yisipu.chartmap.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yisipu.chartmap.R;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class TsDialog extends Dialog {
    private Button  ts_ok;
    public TsDialog(Context context) {
        super(context, R.style.CustomDialog);

        setCustomDialog();
    }
    private void setCustomDialog() {

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.activity_ts_detail, null);
        ts_ok=(Button) mView.findViewById(R.id.ts_ok);
        super.setContentView(mView);
    }
    @Override
    public void setContentView(int layoutResID) {

    }


    public void setContentView(View view, LinearLayout.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener){
        ts_ok.setOnClickListener(listener);
    }
}
