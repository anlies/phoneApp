package com.example.zhantuoer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registerActivity extends AppCompatActivity {
    private EditText mRadmin;
    private Button mregaiter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_register);
        mregaiter=(Button)findViewById(R.id.mregister);
        mregaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadmin=(EditText)findViewById(R.id.Radmin);
                String text=mRadmin.getText().toString();
                if(text.length()==0){
                    Toast.makeText(getApplicationContext(),"手机号不能为空！",Toast.LENGTH_SHORT).show();
                }else if(text.length()!=11){
                    Toast.makeText(getApplicationContext(),"请输入正确的手机号！",Toast.LENGTH_SHORT).show();
                }else{
                    String string=mRadmin.getText().toString();
                    Intent intent=new Intent(registerActivity.this,ReceiveActivity.class);
                    intent.putExtra("String",string);//传递手机号
                    startActivity(intent);


                }
            }
        });



    }
}
