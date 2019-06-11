package com.example.zhantuoer.GuideFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.zhantuoer.R;
import com.example.zhantuoer.RecyclerActivity;

public class GuideFragment4 extends Fragment {
    private View view;
    private View IntentMain;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_guide__fragment,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view=getView();
        initView();
    }
    private void initView(){
        relativeLayout=(RelativeLayout)view.findViewById(R.id.fragment_background);
        relativeLayout.setBackgroundResource(R.drawable.guidepage4);
        IntentMain=view.findViewById(R.id.IntentMain);
        IntentMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), RecyclerActivity.class);
                startActivity(intent);
            }
        });
    }
}
