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



public class TongJiFragment2 extends Fragment {
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
        relativeLayout.setBackgroundResource(R.drawable.bg_tongji2);

        TextView text1 =(TextView)view.findViewById(R.id.textView6);
        text1.setTextColor(Color.parseColor("#eeeeeeff"));
        text1.setText("你定的目标中");
        text1.setTextSize(TypedValue.COMPLEX_UNIT_SP,38);

        count =(TextView)view.findViewById(R.id.count);
        count.setText("天数最长的为");
        count.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        count.setTextColor(Color.parseColor("#ddddddff"));

        TextView text2 =(TextView)view.findViewById(R.id.textView9);
        text2.setTextColor(Color.parseColor("#ffffffff"));
        text2.setText(JiLu_structor.long_tittle);
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,38);

        TextView text3 =(TextView)view.findViewById(R.id.textView10);
        text3.setTextColor(Color.parseColor("#ffffffff"));
        text3.setText("其天数为：");


        baifenbi_text = (TextView)view.findViewById(R.id.baifenbi);
        baifenbi_text.setText(JiLu_structor.long_day+"天");
        baifenbi_text.setTextColor(Color.parseColor("#ccccccff"));
    }
}
