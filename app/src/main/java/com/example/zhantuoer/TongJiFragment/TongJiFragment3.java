package com.example.zhantuoer.TongJiFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhantuoer.Fragment.JiLu_structor;
import com.example.zhantuoer.R;

public class TongJiFragment3 extends Fragment {
    private View view;
    private RelativeLayout relativeLayout;
    private TextView count;
    private TextView baifenbi_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tongji,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view=getView();
        initView();
    }
    private void initView(){
        relativeLayout=(RelativeLayout)view.findViewById(R.id.fragment_background);
        relativeLayout.setBackgroundResource(R.drawable.bg_tongji3);
        TextView text1 =(TextView)view.findViewById(R.id.textView6);
        count =(TextView)view.findViewById(R.id.count);
        TextView text2 =(TextView)view.findViewById(R.id.textView9);
        TextView text3 =(TextView)view.findViewById(R.id.textView10);
        baifenbi_text = (TextView)view.findViewById(R.id.baifenbi);
        if(JiLu_structor.qiandao_wei==0){
            text1.setTextColor(Color.parseColor("#eeeeeeff"));
            text1.setText("恭喜你");
            text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);

            count.setText("你已签到了今天的");
            count.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
            count.setTextColor(Color.parseColor("#ddddddff"));

            text2.setTextColor(Color.parseColor("#ffffffff"));
            text2.setText("全部任务");
            text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,38);

            text3.setTextColor(Color.parseColor("#ffffffff"));
            text3.setText("你又一次");

            baifenbi_text.setText("超越了自己！");
            baifenbi_text.setTextColor(Color.parseColor("#ccccccff"));
        }else{
            text1.setText("你还有任务");
            text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
            text1.setTextColor(Color.parseColor("#eeeeeeff"));
            count.setText("没有签到哦！");
            count.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
            count.setTextColor(Color.parseColor("#ddddddff"));
            text2.setText("快回去签到吧！");
            text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,38);
            text2.setTextColor(Color.parseColor("#ffffffff"));
        }
    }
}
