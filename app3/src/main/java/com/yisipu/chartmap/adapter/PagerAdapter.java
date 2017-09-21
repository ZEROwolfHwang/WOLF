package com.yisipu.chartmap.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yisipu.chartmap.fragment.FragmentAisLctivity;
import com.yisipu.chartmap.fragment.FragmentCompass;
import com.yisipu.chartmap.fragment.FragmentGPS;
import com.yisipu.chartmap.fragment.FragmentKB;
import com.yisipu.chartmap.fragment.FragmentSetting;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list_fragments;

    public PagerAdapter(FragmentManager fm) {

        super(fm);
        list_fragments = new ArrayList<Fragment>();
        list_fragments.add(new FragmentKB());
        list_fragments.add(FragmentAisLctivity.getInstance());
        list_fragments.add(FragmentGPS.getInstance());
        list_fragments.add(FragmentCompass.getInstance());
        list_fragments.add(FragmentSetting.getInstance());
        list_fragments.add(new FragmentKB());

//		list_fragments.add(new FragmentD());
//		list_fragments.add(new FragmentG());
    }

    @Override
    public Fragment getItem(int arg0) {
        return list_fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return list_fragments != null ? list_fragments.size() : 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
