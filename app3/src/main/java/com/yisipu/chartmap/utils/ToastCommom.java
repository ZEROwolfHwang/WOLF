package com.yisipu.chartmap.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yisipu.chartmap.R;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class ToastCommom {
    private static ToastCommom toastCommom;

    private Toast toast;

    private ToastCommom() {
    }

    public static ToastCommom createToastConfig() {
        if (toastCommom == null) {
            toastCommom = new ToastCommom();
        }
        return toastCommom;
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param root
     * @param tvString
     */

    public void ToastShow(Context context, ViewGroup root, String tvString,int mg) {
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_xml, root);
        TextView text = (TextView) layout.findViewById(R.id.text);
        ImageView imageView= (ImageView) layout.findViewById(R.id.sos_iv);
        if (mg==1){
            imageView.setVisibility(View.VISIBLE);
        }
        text.setText(tvString);
        toast = new Toast(context);
        toast.setGravity(Gravity.LEFT, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
