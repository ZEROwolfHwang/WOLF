package com.yisipu.chartmap.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yisipu.chartmap.R;

/**
 * 首页确认框
 */
public class CustomDialog4 extends Dialog {
    private Button positiveButton;
    private TextView title, tvupdate;

    public CustomDialog4(Context context) {
        super(context, R.style.dialog);
        setCustomDialog2();
    }

    private void setCustomDialog2() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_normal4_layout, null);
//        title = (TextView) mView.findViewById(R.id.title);
//        editText = (EditText) mView.findViewById(R.id.number);
//        tvupdate = (TextView) mView.findViewById(R.id.tvupdate);
        positiveButton = (Button) mView.findViewById(R.id.positiveButton);
//        negativeButton = (Button) mView.findViewById(R.id.negativeButton);
        super.setContentView(mView);
    }

    //    public View getEditText(){
//        return editText;
//    }
    public void setMessage(String resId) {
        tvupdate.setText(resId);
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);
    }

    /**
     * 取消键监听器
     *
     * @param listener
     */
//    public void setOnNegativeListener(View.OnClickListener listener) {
//        negativeButton.setOnClickListener(listener);
//    }
}

