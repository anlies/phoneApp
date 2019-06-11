package com.example.zhantuoer;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class More_InfoActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_more__info);
        readFile();
        LinearLayoutClick();
    }
    private void LinearLayoutClick(){
        LinearLayout Education=(LinearLayout) findViewById(R.id.Education);
        LinearLayout Work_Space=(LinearLayout)findViewById(R.id.Work_Space);
        LinearLayout City=(LinearLayout)findViewById(R.id.City);
        ImageView back = (ImageView)findViewById(R.id.back);
        ImageView send = (ImageView)findViewById(R.id.send);

        Education.setOnClickListener(this);
        Work_Space.setOnClickListener(this);
        City.setOnClickListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Education:
                final EditText education=new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入学历")
                        .setView(education)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView textView=(TextView)findViewById(R.id.true_education);
                                textView.setText(education.getText());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.Work_Space:
                final EditText workspace=new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入职业")
                        .setView(workspace)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView textView=(TextView)findViewById(R.id.true_work_space);
                                textView.setText(workspace.getText());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.City:
                final EditText city=new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入所在城市")
                        .setView(city)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView textView=(TextView)findViewById(R.id.true_city);
                                textView.setText(city.getText());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.send:
                Toast.makeText(this,"正在开发中",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //保存资料设置
    protected void saveFile(){
        TextView city=(TextView)findViewById(R.id.true_city);
        TextView education=(TextView)findViewById(R.id.true_education);
        TextView workspace=(TextView)findViewById(R.id.true_work_space);

        SharedPreferences.Editor editor=getSharedPreferences("Users_Info",MODE_PRIVATE).edit();
        editor.putString("city",city.getText().toString());
        editor.putString("education",education.getText().toString());
        editor.putString("workspace",workspace.getText().toString());
        editor.commit();
    }
    //读取资料设置
    protected void readFile(){

        TextView city=(TextView)findViewById(R.id.true_city);
        TextView education=(TextView)findViewById(R.id.true_education);
        TextView workspace=(TextView)findViewById(R.id.true_work_space);
        SharedPreferences sharedPreferences=getSharedPreferences("Users_Info",MODE_PRIVATE);
        String get_city=sharedPreferences.getString("city","");
        String get_education=sharedPreferences.getString("education","");
        String get_workspace=sharedPreferences.getString("workspace","");
        city.setText(get_city);
        education.setText(get_education);
        workspace.setText(get_workspace);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveFile();
    }
}
