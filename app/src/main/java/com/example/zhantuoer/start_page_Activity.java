package com.example.zhantuoer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class start_page_Activity extends AppCompatActivity {
    private TextView textView;
    static start_page_Activity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page_);
        IfFirstTimeOpen();
        instance=this;
        //
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        textView=(TextView)findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(start_page_Activity.this,RecyclerActivity.class);
                startActivity(intent);
            }
        });

    }
    private CountDownTimer timer=new CountDownTimer(1200,800) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            Intent intent=new Intent(start_page_Activity.this,RecyclerActivity.class);
            startActivity(intent);
        }
    };
    //判断应用是否第一次启动
    private void IfFirstTimeOpen(){
        SharedPreferences sharedPreferences=getSharedPreferences("is",MODE_PRIVATE);
        boolean if_first_time= sharedPreferences.getBoolean("if_first_time",true);
        SharedPreferences.Editor editor=sharedPreferences.edit();
      if(if_first_time){
          Intent intent=new Intent(start_page_Activity.this,Guide_Page_Activity.class);
          startActivity(intent);
          finish();
          editor.putBoolean("if_first_time",false);
          editor.commit();
      }else{
          timer.start();
      }

    }

}
