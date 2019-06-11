package com.example.zhantuoer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.example.zhantuoer.TongJiFragment.TongJiFragment1;
import com.example.zhantuoer.TongJiFragment.TongJiFragment2;
import com.example.zhantuoer.TongJiFragment.TongJiFragment3;

import java.util.ArrayList;
import java.util.List;

public class TongJiActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<View> listPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_ji);
        getWindow().setFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN),WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
    }
    private void initView(){
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),showView()));
        listPicture=new ArrayList<View>();
        listPicture.add(findViewById(R.id.drop1));
        listPicture.add(findViewById(R.id.drop2));
        listPicture.add(findViewById(R.id.drop3));
        viewPager.setOnPageChangeListener(showPageChange);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    ViewPager.OnPageChangeListener showPageChange=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            for(int i=0;i<listPicture.size();i++){
                if(i==arg0){
                    listPicture.get(arg0).setBackgroundResource(R.drawable.drop_focus);
                }else{
                    listPicture.get(i).setBackgroundResource(R.drawable.drop_normal);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private List<Fragment> showView(){
        List<Fragment> listView = new ArrayList<Fragment>();
        listView.add(new TongJiFragment1());
        listView.add(new TongJiFragment2());
        listView.add(new TongJiFragment3());
        return listView;
    }
}
