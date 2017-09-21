package com.yisipu.chartmap.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.yisipu.chartmap.R;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
public class FragmentKB extends Fragment{
    private static volatile FragmentKB fragmentKB;

    public static FragmentKB getInstance() {
        if (fragmentKB == null) {
            synchronized (FragmentKB.class) {
                if (fragmentKB == null) {
                    fragmentKB = new FragmentKB();
                }
            }
        }
        return fragmentKB;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = null;
        if (mView == null && inflater != null) {
            mView = inflater.inflate(R.layout.fragment_kb, null);
        }
        return mView;
    }

}
