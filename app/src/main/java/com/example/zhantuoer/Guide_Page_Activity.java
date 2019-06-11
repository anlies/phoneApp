package com.example.zhantuoer;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.zhantuoer.GuideFragment.GuideFragment1;
import com.example.zhantuoer.GuideFragment.GuideFragment2;
import com.example.zhantuoer.GuideFragment.GuideFragment3;
import com.example.zhantuoer.GuideFragment.GuideFragment4;

import java.util.ArrayList;
import java.util.List;

public class Guide_Page_Activity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<View> listPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide__page_);
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
        listPicture.add(findViewById(R.id.drop4));
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
        listView.add(new GuideFragment1());
        listView.add(new GuideFragment2());
        listView.add(new GuideFragment3());
        listView.add(new GuideFragment4());
        return listView;
    }
}
