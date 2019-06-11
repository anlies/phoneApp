package com.example.zhantuoer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.content.ContentValues.TAG;

public class TieContentActivity extends Activity implements View.OnClickListener {
    private static final String URL = JsonTools.CONSTURL+"zhantuoer/userComment";
    private ProgressDialog progressDialog;
    //头像
    ImageView userImage;
    //头像url地址
    String user_image;
    //昵称
    TextView userName;
    //昵称字符串
    String user_name;
    //帖子id
    String sID;
    //发帖用户
    String fatie_user;
    //自己的用户名
    private String user_phone;
    //是否已登录
    private Boolean isLogin = false;
    Button guanzhu; Button tie_bottom;ImageView zan;ImageView zan_red;Button guanzhu2;
    //评论内容
    String InputComment;
    //webView
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tie_content);
        nav_click();
        setData_init();
        setData();
        webView = (WebView)findViewById(R.id.web_wiew);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(JsonTools.CONSTURL+"zhantuoer/signInfo?sid="+sID);
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readFile();
        if(isLogin){
            is_guanzhu(user_phone,fatie_user);
        }else{
            Toast.makeText(TieContentActivity.this,"登陆后就可以关注他人",Toast.LENGTH_LONG).show();
        }
        guanzhu.setOnClickListener(this);
        tie_bottom.setOnClickListener(this);
    }

    //获取用户名
    private void readFile(){
        SharedPreferences pref =getSharedPreferences("data",MODE_PRIVATE);
        user_phone =pref.getString("username","");
        if(user_phone.length()==0){
            isLogin = false;
        }else{
            isLogin = true;
        }
    }

    private void setData_init(){
        userImage = (ImageView)findViewById(R.id.userImage);
        userName = (TextView)findViewById(R.id.userName);
        Intent intent = getIntent();
        user_name = intent.getStringExtra("user_name");
        sID = intent.getStringExtra("tID");
        user_image = intent.getStringExtra("photo");
        fatie_user = intent.getStringExtra("guanzhu");
    }

    private void setData(){
        userName.setText(user_name);
        Glide.with(TieContentActivity.this)
                .load(user_image) //加载地址
                .placeholder(R.drawable.nav_icon)
                .error(R.drawable.nav_icon)
                .into(userImage);//显示的位置
    }

    private void nav_click(){
        ImageView back = (ImageView)findViewById(R.id.back);
        guanzhu = (Button)findViewById(R.id.guanzhu);
        guanzhu2 = (Button)findViewById(R.id.guanzhu2);
        tie_bottom = (Button)findViewById(R.id.tie_bottom);
        ImageView fenxiang = (ImageView)findViewById(R.id.fenxiang);
        zan = (ImageView)findViewById(R.id.zan);
        zan_red = (ImageView)findViewById(R.id.zan_red);
        back.setOnClickListener(this);
        fenxiang.setOnClickListener(this);
        zan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.guanzhu:
                if(isLogin){
                    guanzhu(user_phone,fatie_user);
                }else{
                    Toast.makeText(TieContentActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tie_bottom:
                if(isLogin){
                    showDialog();
                }else{
                    Toast.makeText(TieContentActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.zan:
                if(isLogin){
                    zan(sID,user_phone);
                }else{
                    Toast.makeText(TieContentActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fenxiang:
                Toast.makeText(TieContentActivity.this,"正在开发中",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    private void pinlun(String sid, String phoneNmuber,String text) {
        String qiandaoURL = URL + "?sid=" + sid +"&phoneNumber="+phoneNmuber+"&text=" + text;
        new MyAsyncTask().execute(qiandaoURL);
    }

    private void guanzhu(String phoneNmuber,String fatie_user) {
        String guanzhuURL = JsonTools.CONSTURL+"zhantuoer/UserAttention?attention="+phoneNmuber+"&beAttention=" + fatie_user;
        new MyAsyncTask().execute(guanzhuURL);
    }

    private void is_guanzhu(String phoneNmuber,String fatie_user) {
        String guanzhuURL = JsonTools.CONSTURL+"zhantuoer/UserJudgeAttention?attention="+phoneNmuber+"&beAttention=" + fatie_user;
        new is_guanzhuTask().execute(guanzhuURL);
    }

    private void zan(String sID,String phoneNmuber) {
        String url = JsonTools.CONSTURL+"zhantuoer/likesSign?sid="+sID+"&phoneNumber="+phoneNmuber;
        new ZanAsyncTask().execute(url);
    }

    //点击关注按钮后与网络通信
    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        String URIs;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TieContentActivity.this);
            progressDialog.setTitle("正在请求中，请等待");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            if (params.length < 1 || params[0] == null) {
                return null;
            }
            URIs = params[0];
            return JsonTools.loginhelp(URIs);
        }

        //运行在UI线程中，所以可以直接操作UI元素
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Log.w(TAG, "test: "+s );
            if(s.length()==0 && URIs.contains("userComment")){
                Toast.makeText(TieContentActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                webView.reload();
            }else if(s.equals("true")){
                guanzhu.setVisibility(View.INVISIBLE);
                guanzhu2.setVisibility(View.VISIBLE);
                Toast.makeText(TieContentActivity.this,"关注成功",Toast.LENGTH_SHORT).show();
            }
        }
    }


    //点击点赞按钮后与网络通信
    private class ZanAsyncTask extends AsyncTask<String, Integer, String> {

        String URIs;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(TieContentActivity.this);
            progressDialog.setTitle("正在请求中，请等待");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            if (params.length < 1 || params[0] == null) {
                return null;
            }
            URIs = params[0];
            return JsonTools.loginhelp(URIs);
        }

        //运行在UI线程中，所以可以直接操作UI元素
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            Log.w(TAG, "test: "+s );
            if(s.length()==0){
                zan.setVisibility(View.INVISIBLE);
                zan_red.setVisibility(View.VISIBLE);
                Toast.makeText(TieContentActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
            }
        }
    }


    //点击关注按钮后与网络通信
    private class is_guanzhuTask extends AsyncTask<String, Integer, String> {

        String URIs;

        @Override
        protected String doInBackground(String... params) {
            if (params.length < 1 || params[0] == null) {
                return null;
            }
            URIs = params[0];
            return JsonTools.loginhelp(URIs);
        }

        //运行在UI线程中，所以可以直接操作UI元素
        @Override
        protected void onPostExecute(String s) {
            Log.w(TAG, "test: "+s );
            if(s.equals("true")){
                guanzhu.setVisibility(View.INVISIBLE);
                guanzhu2.setVisibility(View.VISIBLE);
            }else {
                guanzhu.setVisibility(View.VISIBLE);
                guanzhu2.setVisibility(View.INVISIBLE);
            }
        }
    }
    //写评论弹出框

    public void showDialog() {
        final EditText comment=new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("写评论")
                .setView(comment)
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       InputComment=comment.getText().toString();
                       if(comment.length()==0){
                           Toast.makeText(TieContentActivity.this,"请输入内容",Toast.LENGTH_SHORT).show();
                       }else{
                            pinlun(sID,user_phone,InputComment);
                        }
                    }
                })
                .setNegativeButton("取消",null).show();
    }
}

