package com.example.zhantuoer.TongJiFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhantuoer.Fragment.JiLu_structor;
import com.example.zhantuoer.R;


public class TongJiFragment1 extends Fragment {
    private View view;
    private RelativeLayout relativeLayout;
    private TextView count;
    //已完成任务数占任务总数的百分比
    private double baifenbi=0.0;
    private TextView baifenbi_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tongji, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view=getView();
        initView();
        //Log.w(TAG, "onActivityCreated: "+JiLu_structor.qiandao_done );
    }

    private void initView(){
        TextView text1 =(TextView)view.findViewById(R.id.textView6);
        text1.setTextColor(getResources().getColor(R.color.md_blue_grey_500));
        text1.setText("你已完成");
        relativeLayout=(RelativeLayout)view.findViewById(R.id.fragment_background);
        relativeLayout.setBackgroundResource(R.drawable.bg_tongji1);
        count =(TextView)view.findViewById(R.id.count);
        count.setText(JiLu_structor.doneAll+"");
        count.setTextColor(getResources().getColor(R.color.md_blue_grey_500));
        TextView text2 =(TextView)view.findViewById(R.id.textView9);
        text2.setTextColor(getResources().getColor(R.color.md_blue_grey_500));
        text2.setText("个任务");
        TextView text3 =(TextView)view.findViewById(R.id.textView10);
        text3.setTextColor(Color.parseColor("#ddddddff"));
        text3.setText("占总数的");
        if(JiLu_structor.work_count!=0) {
            baifenbi = 1.0 * JiLu_structor.doneAll / JiLu_structor.work_count * 100;
        }
        baifenbi_text = (TextView)view.findViewById(R.id.baifenbi);
        baifenbi_text.setText(String.format("%.2f",baifenbi)+"%");
        baifenbi_text.setTextColor(Color.parseColor("#ccccccff"));
    }
}
