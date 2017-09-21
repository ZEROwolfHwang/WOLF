package com.yisipu.chartmap.tools;

import android.widget.Toast;

import com.yisipu.chartmap.MyApplication;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class ToastUtils {

    private static Toast mToast;

    /**
     * 单利土司
     *
     * @param content 土司的内容
     */
    public static void singleToast(String content) {

        Observable.just(content)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String content) {
                        if (mToast == null) {
                            mToast = Toast.makeText(MyApplication.sApplication, null, Toast.LENGTH_SHORT);
                        }
                        mToast.setText(content);
                        mToast.show();
                    }
                });

    }

}
