package com.example.zhantuoer;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> FragmentList;
    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }
    public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> listFragment){
        super(fragmentManager);
        this.FragmentList=listFragment;
    }


    @Override
    public Fragment getItem(int arg0) {
        return FragmentList.get(arg0);
    }
    @Override
    public int getCount() {
        return FragmentList.size();
    }

}
